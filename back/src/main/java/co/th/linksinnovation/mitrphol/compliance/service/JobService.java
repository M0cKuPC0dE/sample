/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.service;

import co.th.linksinnovation.mitrphol.compliance.model.Accord;
import co.th.linksinnovation.mitrphol.compliance.model.Accorded;
import co.th.linksinnovation.mitrphol.compliance.model.EvidenceFile;
import co.th.linksinnovation.mitrphol.compliance.model.LegalCategory;
import co.th.linksinnovation.mitrphol.compliance.model.LegalGroup;
import co.th.linksinnovation.mitrphol.compliance.model.LicenseFile;
import co.th.linksinnovation.mitrphol.compliance.model.UserDetails;
import co.th.linksinnovation.mitrphol.compliance.model.dataset.MailSummary;
import co.th.linksinnovation.mitrphol.compliance.repository.AccordRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.LegalcategoryRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.LegalgroupRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.UserDetailsRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 *
 * @author jirawong
 */
@Service
public class JobService {

    @Autowired
    private AccordRepository accordRepository;
    @Autowired
    private LegalgroupRepository legalgroupRepository;
    @Autowired
    private LegalcategoryRepository legalcategoryRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public JobService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Scheduled(cron = "0 0 10 * * *")
    @Transactional
    public void warningAccordIsExpire() {
        System.out.println("---> Warning Mail <----");
        List<Accord> findAll = accordRepository.findAll();
        for (Accord accord : findAll) {
            for (LicenseFile lf : accord.getLicenseFiles()) {
                if (lf != null && lf.getWarningDate()!= null) {
                    if (DateUtils.isSameDay(DateUtils.addYears(lf.getWarningDate(), -543), new Date())) {
                        warningMail(new ArrayList<>(accord.getLegalCategory().getOwners()).get(0), accord, lf.getName(), lf.getExpireDate());
                        break;
                    }
                }
            }
            for (EvidenceFile ef : accord.getEvidenceFiles()) {
                if (ef != null && ef.getWarningDate() != null) {
                    if (DateUtils.isSameDay(DateUtils.addYears(ef.getWarningDate(), -543), new Date())) {
                        warningMail(new ArrayList<>(accord.getLegalCategory().getOwners()).get(0), accord, ef.getName(), ef.getExpireDate());
                        break;
                    }
                }
            }
        }
    }

    @Async
    private void warningMail(UserDetails u, Accord accord, String filename, Date expireDate) {
        System.out.println("------> send mail to " + u.getEmail());
        MimeMessage mail = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(u.getEmail());
            helper.setFrom("mpcompliance@mitrphol.com");
            helper.setSubject("แจ้งเตือน รายการเอกสารถึงกำหนดต่ออายุ");

            Context context = new Context();
            context.setVariable("department", accord.getLegalCategory().getDepartment().getName());
            context.setVariable("legal_name", accord.getLegalDuty().getCompliance().getLegalName());
            context.setVariable("legal_duty", accord.getLegalDuty().getName());
            context.setVariable("filename", filename);
            context.setVariable("expireDate", expireDate);
            helper.setText(templateEngine.process("warningmail", context), true);
            javaMailSender.send(mail);
        } catch (MessagingException ex) {
            Logger.getLogger(JobService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Scheduled(cron = "0 0 10 * * *")
    @Transactional
    public void resetAccordIsExpire() {
        List<Accord> findAll = accordRepository.findAll();
        for (Accord accord : findAll) {
            boolean flag = false;
            for (LicenseFile lf : accord.getLicenseFiles()) {
                if (lf != null && lf.getExpireDate() != null) {
                    if (DateUtils.isSameDay(DateUtils.addYears(lf.getExpireDate(), -543), new Date())) {
                        flag = true;
                        resetMail(new ArrayList<>(accord.getLegalCategory().getOwners()).get(0), accord, lf.getName(), lf.getExpireDate());
                        break;
                    }
                }
            }
            for (EvidenceFile ef : accord.getEvidenceFiles()) {
                if (ef != null && ef.getExpireDate() != null) {
                    if (DateUtils.isSameDay(DateUtils.addYears(ef.getExpireDate(), -543), new Date())) {
                        flag = true;
                        resetMail(new ArrayList<>(accord.getLegalCategory().getOwners()).get(0), accord, ef.getName(), ef.getExpireDate());
                        break;
                    }
                }
            }
            if (flag) {
                System.out.println(accord.getId());
                accord.setAccept(null);
                accord.setAccorded(null);
                accord.setApprove(null);
                accordRepository.save(accord);
            }
        }
    }

    @Transactional
    public void resetAccordAllExpire() {
        List<Accord> findAll = accordRepository.findAll();
        for (Accord accord : findAll) {
            boolean flag = false;
            for (LicenseFile lf : accord.getLicenseFiles()) {
                if (lf != null && lf.getExpireDate() != null) {
                    if ((DateUtils.isSameDay(DateUtils.addYears(lf.getExpireDate(), -543), new Date())) || DateUtils.addYears(lf.getExpireDate(), -543).before(new Date())) {
                        flag = true;
                        resetMail(new ArrayList<>(accord.getLegalCategory().getOwners()).get(0), accord, lf.getName(), lf.getExpireDate());
                        break;
                    }
                }
            }
            for (EvidenceFile ef : accord.getEvidenceFiles()) {
                if (ef != null && ef.getExpireDate() != null) {
                    if ((DateUtils.isSameDay(DateUtils.addYears(ef.getExpireDate(), -543), new Date())) || DateUtils.addYears(ef.getExpireDate(), -543).before(new Date())) {
                        flag = true;
                        resetMail(new ArrayList<>(accord.getLegalCategory().getOwners()).get(0), accord, ef.getName(), ef.getExpireDate());
                        break;
                    }
                }
            }
            if (flag) {
                System.out.println(accord.getId());
                accord.setAccept(null);
                accord.setAccorded(null);
                accord.setApprove(null);
                accordRepository.save(accord);
            }
        }
    }

    @Async
    private void resetMail(UserDetails u, Accord accord, String filename, Date expireDate) {
        System.out.println("------> send mail to " + u.getEmail());
        MimeMessage mail = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(u.getEmail());
            helper.setFrom("mpcompliance@mitrphol.com");
            helper.setSubject("แจ้งเตือน รายการเอกสารหมดอายุ");

            Context context = new Context();
            context.setVariable("department", accord.getLegalCategory().getDepartment().getName());
            context.setVariable("legal_name", accord.getLegalDuty().getCompliance().getLegalName());
            context.setVariable("legal_duty", accord.getLegalDuty().getName());
            context.setVariable("filename", filename);
            context.setVariable("expireDate", expireDate);
            helper.setText(templateEngine.process("resetmail", context), true);
            javaMailSender.send(mail);
        } catch (MessagingException ex) {
            Logger.getLogger(JobService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 10 * * *")
    public void coordinatorSummary() {
        List<String> asList = Arrays.asList("Coordinator");
        Map<String, List<MailSummary>> map;
        List<UserDetails> findUserByRole = userDetailsRepository.findUserByRole(asList);
        for (UserDetails ud : findUserByRole) {
            map = new HashMap<>();
            List<LegalGroup> legalGroups = legalgroupRepository.findByCoordinatesIn(ud);
            for (LegalGroup lg : legalGroups) {
                System.out.println("--> LG " + lg.getId() + " " + lg.getBuName());
                List<LegalCategory> legalCategories = legalcategoryRepository.findByLegalGroup(lg);
                for (LegalCategory lc : legalCategories) {
                    if (lc.getOwners().toArray().length == 0) {
                        continue;
                    }
                    String key = ((UserDetails) lc.getOwners().toArray()[0]).getNameEn();

                    MailSummary mailSummary = new MailSummary(
                            lc.getDepartment().getName(),
                            countProcessCoordinator(lc.getAccords()),
                            countAccorded(lc.getAccords(), Accorded.ACCORDED),
                            countAccorded(lc.getAccords(), Accorded.NOT_ACCORDED),
                            countAccorded(lc.getAccords(), null),
                            lc.getAccords().size()
                    );
                    if (map.containsKey(key)) {
                        List<MailSummary> get = map.get(key);
                        get.add(mailSummary);
                        map.put(key, get);
                    } else {
                        List<MailSummary> mailSummaries = new ArrayList<>();
                        mailSummaries.add(mailSummary);
                        map.put(key, mailSummaries);
                    }
                }
            }
            map = summaryAccorded(map);
            System.out.println("--------> map " + map);
            if (!map.isEmpty()) {
                coordinatorSummaryMail(ud, map);
            }
        }
    }

    private Integer countProcessCoordinator(List<Accord> accords) {
        Integer total = 0;
        for (Accord accord : accords) {
            if (accord.getAccept() == null && accord.getAccorded() != null && accord.getApprove() == null) {
                total++;
            }
        }
        return total;
    }

    private Integer countAccorded(List<Accord> accords, Accorded accorded) {
        Integer total = 0;
        for (Accord accord : accords) {
            if ((accorded == null && accord.getAccorded() == null) || (accorded != null && accorded.equals(accord.getAccorded()))) {
                total++;
            }
        }
        return total;
    }

    private Map<String, List<MailSummary>> summaryAccorded(Map<String, List<MailSummary>> map) {
        for (Map.Entry<String, List<MailSummary>> entry : map.entrySet()) {
            MailSummary total = new MailSummary();
            List<MailSummary> mailSummaries = entry.getValue();
            for (MailSummary mailSummary : mailSummaries) {
                total.setProcess(total.getProcess() + mailSummary.getProcess());
                total.setAccord(total.getAccord() + mailSummary.getAccord());
                total.setNoAccord(total.getNoAccord() + mailSummary.getNoAccord());
                total.setNoProcess(total.getNoProcess() + mailSummary.getNoProcess());
                total.setSummary(total.getSummary() + mailSummary.getSummary());
            }
            mailSummaries.add(total);
            map.put(entry.getKey(), mailSummaries);
        }
        return map;
    }

    private void coordinatorSummaryMail(UserDetails u, Map<String, List<MailSummary>> map) {
        System.out.println("------> send summary mail to coordinator " + u.getEmail());
        MimeMessage mail = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(u.getEmail());
            helper.setFrom("mpcompliance@mitrphol.com");
            helper.setSubject("สรุปการประเมินความสอดคล้องการปฎิบัติตามกฎหมายของผู้ดูแล");

            Context context = new Context();
            context.setVariable("map", map);
            helper.setText(templateEngine.process("coordinatorsummary", context), true);
            javaMailSender.send(mail);
        } catch (MessagingException ex) {
            Logger.getLogger(JobService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 10 * 5 *")
    public void approverSummary() {
        List<String> asList = Arrays.asList("Approver");
        Map<String, List<MailSummary>> map;
        List<UserDetails> findUserByRole = userDetailsRepository.findUserByRole(asList);
        for (UserDetails ud : findUserByRole) {
            map = new HashMap<>();
            List<LegalCategory> legalCategories = legalcategoryRepository.findByApproversIn(ud);
            for (LegalCategory lc : legalCategories) {
                if (lc.getOwners().toArray().length == 0) {
                    continue;
                }
                String key = ((UserDetails) lc.getOwners().toArray()[0]).getNameEn();

                MailSummary mailSummary = new MailSummary(
                        lc.getDepartment().getName(),
                        countProcessApprover(lc.getAccords()),
                        countAccorded(lc.getAccords(), Accorded.ACCORDED),
                        countAccorded(lc.getAccords(), Accorded.NOT_ACCORDED),
                        countAccorded(lc.getAccords(), null),
                        lc.getAccords().size()
                );
                if (map.containsKey(key)) {
                    List<MailSummary> get = map.get(key);
                    get.add(mailSummary);
                    map.put(key, get);
                } else {
                    List<MailSummary> mailSummaries = new ArrayList<>();
                    mailSummaries.add(mailSummary);
                    map.put(key, mailSummaries);
                }
            }
            map = summaryAccorded(map);
            System.out.println("--------> map " + map);
            if (!map.isEmpty()) {
                approverSummaryMail(ud, map);
            }
        }
    }

    private Integer countProcessApprover(List<Accord> accords) {
        Integer total = 0;
        for (Accord accord : accords) {
            if (accord.getAccept() != null && accord.getAccept() != false && accord.getAccorded() != null && accord.getApprove() == null) {
                total++;
            }
        }
        return total;
    }

    private void approverSummaryMail(UserDetails u, Map<String, List<MailSummary>> map) {
        System.out.println("------> send summary mail to approver " + u.getEmail());
        MimeMessage mail = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(u.getEmail());
            helper.setFrom("mpcompliance@mitrphol.com");
            helper.setSubject("สรุปการพิจารณาเห็นชอบการประเมินความสอดคล้องการปฎิบัติตามกฎหมายของผู้ดูแล (Owner)");

            Context context = new Context();
            context.setVariable("map", map);
            helper.setText(templateEngine.process("approversummary", context), true);
            javaMailSender.send(mail);
        } catch (MessagingException ex) {
            Logger.getLogger(JobService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
