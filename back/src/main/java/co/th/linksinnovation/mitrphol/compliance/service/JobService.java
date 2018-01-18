/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.service;

import co.th.linksinnovation.mitrphol.compliance.model.Accord;
import co.th.linksinnovation.mitrphol.compliance.model.EvidenceFile;
import co.th.linksinnovation.mitrphol.compliance.model.LicenseFile;
import co.th.linksinnovation.mitrphol.compliance.model.UserDetails;
import co.th.linksinnovation.mitrphol.compliance.repository.AccordRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public JobService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Scheduled(cron = "0 0 9 * * *")
    @Transactional
    public void resetAccordIsExpire() {
        List<Accord> findAll = accordRepository.findAll();
        for (Accord accord : findAll) {
            boolean flag = false;
            for (LicenseFile lf : accord.getLicenseFiles()) {
                System.out.println("----> " + DateUtils.addYears(lf.getExpireDate(), -543) + " <-> " + new Date());
                if (lf != null && lf.getExpireDate() != null && DateUtils.isSameDay(DateUtils.addYears(lf.getExpireDate(), -543), new Date())) {
                    flag = true;
                    resetMail(new ArrayList<>(accord.getLegalCategory().getOwners()).get(0), accord, lf.getName(), lf.getExpireDate());
                    break;
                }
            }
            for (EvidenceFile ef : accord.getEvidenceFiles()) {
                if (ef != null && ef.getExpireDate() != null && DateUtils.isSameDay(DateUtils.addYears(ef.getExpireDate(), -543), new Date())) {
                    flag = true;
                    resetMail(new ArrayList<>(accord.getLegalCategory().getOwners()).get(0), accord, ef.getName(), ef.getExpireDate());
                    break;
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
        System.out.println("------> send mail to "+u.getEmail());
        MimeMessage mail = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(u.getEmail());
            helper.setFrom("mpcompliance@mitrphol.com");
            helper.setSubject("แจ้งเตือน รายการเอกสารถึงกำหนดต่ออายุ / ทบทวนการประเมิน /เอกสารหมดอายุ");

            Context context = new Context();
            context.setVariable("legal_duty", accord.getLegalDuty().getName());
            context.setVariable("filename", filename);
            context.setVariable("expireDate", expireDate);
            helper.setText(templateEngine.process("resetmail", context), true);
            javaMailSender.send(mail);
        } catch (MessagingException ex) {
            Logger.getLogger(JobService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
