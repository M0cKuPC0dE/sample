/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.service;

import co.th.linksinnovation.mitrphol.compliance.model.Accord;
import co.th.linksinnovation.mitrphol.compliance.model.Accorded;
import co.th.linksinnovation.mitrphol.compliance.model.LegalCategory;
import co.th.linksinnovation.mitrphol.compliance.model.LegalGroup;
import co.th.linksinnovation.mitrphol.compliance.model.UserDetails;
import co.th.linksinnovation.mitrphol.compliance.repository.AccordRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.LegalgroupRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.UserDetailsRepository;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 *
 * @author jirawong
 */
@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private AccordRepository accordRepository;
    @Autowired
    private LegalgroupRepository legalgroupRepository;
    private final TemplateEngine templateEngine;

    @Autowired
    public MailService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    private String build(String title, String message) {
        Context context = new Context();
        context.setVariable("title", title);
        context.setVariable("message", message);
        return templateEngine.process("mail", context);
    }

    @Async
    private void send2Coordinator(LegalGroup legalGroup, String username) {
        UserDetails user = userDetailsRepository.findOne(username);
        for (UserDetails u : legalGroup.getCoordinates()) {
            MimeMessage mail = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(mail, true);
                helper.setTo(u.getEmail());
                helper.setFrom("mpcompliance@mitrphol.com");
                helper.setSubject("[Compliance System] มีรายการกฎหมายได้รับ Assign จาก Admin");

                Context context = new Context();
                context.setVariable("name", user.getNameTh());
                context.setVariable("amount", legalGroup.getLegalDuties().size());

                helper.setText(templateEngine.process("coordinator", context), true);
                javaMailSender.send(mail);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

    }

    @Async
    private void send2Owner(LegalCategory legalCategory, String username) {
        UserDetails user = userDetailsRepository.findOne(username);
        for (UserDetails u : legalCategory.getOwners()) {
            MimeMessage mail = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(mail, true);
                helper.setTo(u.getEmail());
                helper.setFrom("mpcompliance@mitrphol.com");
                helper.setSubject("[Compliance System] มีรายการกฎหมายรอการประเมินความสอดคล้องการปฏิบัติตามกฎหมาย จาก Compliance Coordinator");

                Context context = new Context();
                context.setVariable("name", user.getNameTh());
                context.setVariable("url", "https://compliance.mitrphol.com/checklist/login?key=" + u.getUuid());
                context.setVariable("amount", legalCategory.getLegalDuties().size());

                helper.setText(templateEngine.process("owner", context), true);
                javaMailSender.send(mail);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
    private void compliance(Accord accord, String username) {
        Accord findOne = accordRepository.findOne(accord.getId());
        UserDetails user = userDetailsRepository.findOne(username);
        List<Accord> accords = findOne.getLegalCategory().getAccords();
        List<UserDetails> coordinates = findOne.getLegalCategory().getLegalGroup().getCoordinates();
        asyncCompliance(coordinates, user, accords);
    }

    @Async
    private void asyncCompliance(List<UserDetails> coordinates, UserDetails user, List<Accord> accords) throws MailException {
        for (UserDetails u : coordinates) {
            MimeMessage mail = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(mail, true);
                helper.setTo(u.getEmail());
                helper.setFrom("mpcompliance@mitrphol.com");
                helper.setSubject("[Compliance System] มีรายการกฎหมายซึ่งประเมินความสอดคล้องแล้ว รอการพิจารณา");

                Context context = new Context();
                context.setVariable("name", user.getNameTh());

                int size = accords.size();
                int acc = 0;
                int nacc = 0;
                int ncc = 0;
                for (Accord a : accords) {
                    if (Accorded.ACCORDED.equals(a.getAccorded())) {
                        acc++;
                    } else if (Accorded.NOT_ACCORDED.equals(a.getAccorded())) {
                        nacc++;
                    } else if (Accorded.NOT_CONCERN.equals(a.getAccorded())) {
                        ncc++;
                    }
                }
                context.setVariable("total", size);
                context.setVariable("accord", acc);
                context.setVariable("not_accord", nacc);
                context.setVariable("not_concern", ncc);

                helper.setText(templateEngine.process("compliance", context), true);
                javaMailSender.send(mail);

            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    private void acceptNotification(Accord accord, String username) {
        Accord findOne = accordRepository.findOne(accord.getId());
        UserDetails user = userDetailsRepository.findOne(username);
        Set<UserDetails> approvers = findOne.getLegalCategory().getApprovers();
        List<Accord> accords = findOne.getLegalCategory().getAccords();
        asyncAccept(approvers, user, accords);
    }

    @Async
    private void asyncAccept(Set<UserDetails> approvers, UserDetails user, List<Accord> accords) throws MailException {
        for (UserDetails u : approvers) {
            MimeMessage mail = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(mail, true);
                helper.setTo(u.getEmail());
                helper.setFrom("mpcompliance@mitrphol.com");
                helper.setSubject("[Compliance System] มีรายการกฎหมายผ่านการพิจารณาจาก Compliance Coordinator รอการพิจารณา");

                Context context = new Context();
                context.setVariable("name", user.getNameTh());
                context.setVariable("url", "https://compliance.mitrphol.com/checklist/login?key=" + u.getUuid());
                int size = accords.size();
                int acc = 0;
                int nacc = 0;
                int ncc = 0;
                for (Accord a : accords) {
                    if (Accorded.ACCORDED.equals(a.getAccorded())) {
                        acc++;
                    } else if (Accorded.NOT_ACCORDED.equals(a.getAccorded())) {
                        nacc++;
                    } else if (Accorded.NOT_CONCERN.equals(a.getAccorded())) {
                        ncc++;
                    }
                }
                context.setVariable("total", accords.size());
                context.setVariable("accord", acc);
                context.setVariable("not_accord", nacc);
                context.setVariable("nothing", size - (acc + nacc + ncc));

                helper.setText(templateEngine.process("accept", context), true);
                javaMailSender.send(mail);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
    private void approveNotification(Accord accord, String username) {
        Accord findOne = accordRepository.findOne(accord.getId());
        UserDetails user = userDetailsRepository.findOne(username);
        Set<UserDetails> owners = findOne.getLegalCategory().getOwners();
        List<UserDetails> coordinates = findOne.getLegalCategory().getLegalGroup().getCoordinates();
        List<Accord> accords = findOne.getLegalCategory().getAccords();

        asyncApprove(owners, user, accords);
        asyncApprove(coordinates, user, accords);
    }

    @Async
    private void asyncApprove(Collection<? extends UserDetails> approvers, UserDetails user, List<Accord> accords) throws MailException {
        for (UserDetails u : approvers) {
            MimeMessage mail = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(mail, true);
                helper.setTo(u.getEmail());
                helper.setFrom("mpcompliance@mitrphol.com");
                helper.setSubject("[Compliance System] มีรายการกฎหมายที่ได้รับการพิจารณาจาก Approver");

                Context context = new Context();
                context.setVariable("name", user.getNameTh());
//                Map<Boolean, Long> collect = accords.stream().collect(Collectors.groupingBy(Accord::getApprove, Collectors.counting()));
                int size = accords.size();
                int app = 0;
                int napp = 0;
                for (Accord a : accords) {
                    if (Objects.equals(a.getApprove(), Boolean.TRUE)) {
                        app++;
                    } else if (Objects.equals(a.getApprove(), Boolean.FALSE)) {
                        napp++;
                    }
                }
                context.setVariable("total", size);
                context.setVariable("approve", app);
                context.setVariable("not_approve", napp);
                context.setVariable("nothing", size - (app + napp));

                helper.setText(templateEngine.process("approve", context), true);
                javaMailSender.send(mail);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }
}
