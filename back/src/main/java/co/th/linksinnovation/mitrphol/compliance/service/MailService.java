/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.service;

import co.th.linksinnovation.mitrphol.compliance.model.Accord;
import co.th.linksinnovation.mitrphol.compliance.model.LegalCategory;
import co.th.linksinnovation.mitrphol.compliance.model.LegalGroup;
import co.th.linksinnovation.mitrphol.compliance.model.UserDetails;
import co.th.linksinnovation.mitrphol.compliance.repository.AccordRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.LegalgroupRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.UserDetailsRepository;
import java.util.List;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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

    @Async
    public void send2Coordinator(LegalGroup legalGroup,String username) {
        UserDetails user = userDetailsRepository.findOne(username);
        for (UserDetails u : legalGroup.getCoordinates()) {
            MimeMessage mail = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(mail, true);
                helper.setTo(u.getEmail());
                helper.setReplyTo("MitrpholServices@mitrphol.com");
                helper.setFrom("MitrpholServices@mitrphol.com");
                helper.setSubject("Compliance System Coordinator");
                StringBuilder sb = new StringBuilder();
                sb.append("คุณ ");
                sb.append(user.getNameTh());
                sb.append(" ได้กำหนดให้คุณเป็น Coordinator ของระบบ Compliance System ");
                sb.append(" สามารถเข้าใช้งานระบบได้ที่ https://compliance.mitrphol.com");
                helper.setText(sb.toString());
            } catch (MessagingException e) {
            } finally {
                javaMailSender.send(mail);
            }
        }

    }
    
    @Async
    public void send2Owner(LegalCategory legalCategory,String username) {
        UserDetails user = userDetailsRepository.findOne(username);
        for (UserDetails u : legalCategory.getOwners()) {
            MimeMessage mail = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(mail, true);
                helper.setTo(u.getEmail());
                helper.setReplyTo("MitrpholServices@mitrphol.com");
                helper.setFrom("MitrpholServices@mitrphol.com");
                helper.setSubject("Compliance System Owner");
                StringBuilder sb = new StringBuilder();
                sb.append("คุณ ");
                sb.append(user.getNameTh());
                sb.append(" ได้กำหนดให้คุณเป็น Owner ของระบบ Compliance System ");
                sb.append(" สามารถเข้าใช้งานระบบได้ที่ https://compliance.mitrphol.com");
                helper.setText(sb.toString());
            } catch (MessagingException e) {
            } finally {
                javaMailSender.send(mail);
            }
        }
    }
    
    @Async
    @Transactional
    public void compliance(Accord accord,String username) {
        Accord findOne = accordRepository.findOne(accord.getId());
        UserDetails user = userDetailsRepository.findOne(username);
        for (UserDetails u : findOne.getLegalCategory().getLegalGroup().getCoordinates()) {
            MimeMessage mail = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(mail, true);
                helper.setTo(u.getEmail());
                helper.setReplyTo("MitrpholServices@mitrphol.com");
                helper.setFrom("MitrpholServices@mitrphol.com");
                helper.setSubject("Compliance System");
                StringBuilder sb = new StringBuilder();
                sb.append("คุณ ");
                sb.append(user.getNameTh());
                sb.append(" ได้บันทึกผลการดำเนินงานตามกฎหมายในระบบ Compliance System แล้ว ");
                sb.append(" สามารถตรวจสอบได้ที่ https://compliance.mitrphol.com");
                helper.setText(sb.toString());
            } catch (MessagingException e) {
            } finally {
                javaMailSender.send(mail);
            }
        }
    }
}
