package com.markian.rentitup.Config;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(String to, String subject, String templateName, Map<String, Object> templateVariables) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true, "UTF-8");

            Context  thymeleafContext = new Context();
            templateVariables.forEach(thymeleafContext::setVariable);
            String htmlContent =  templateEngine.process(templateName,thymeleafContext);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent,true);

            mailSender.send(mimeMessage);

        } catch (MailException e) {
            throw new RuntimeException("Failed to send email", e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}