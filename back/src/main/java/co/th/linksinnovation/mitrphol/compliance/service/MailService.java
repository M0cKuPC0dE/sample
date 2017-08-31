/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.service;

import co.th.linksinnovation.mitrphol.compliance.model.UserDetails;
import java.util.List;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 *
 * @author jirawong
 */
@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void send2Coordinator(List<UserDetails> userDetailses) {
        for (UserDetails u : userDetailses) {
            MimeMessage mail = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(mail, true);
                helper.setTo("natajrakp@mitrphol.com");
                helper.setReplyTo("natajrakp@mitrphol.com");
                helper.setFrom("natajrakp@mitrphol.com");
                helper.setSubject("Lorem ipsum");
                helper.setText("Lorem ipsum dolor sit amet [...]");
            } catch (MessagingException e) {
            } finally {
                javaMailSender.send(mail);
            }
        }

    }
}
