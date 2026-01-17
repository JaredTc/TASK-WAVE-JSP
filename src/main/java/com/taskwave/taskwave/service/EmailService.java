package com.taskwave.taskwave.service;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import org.thymeleaf.context.Context;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    private final SpringTemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendWelcomeEmail(String to, String name) {
      try {
          Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("email",to);

            String html = templateEngine.process("email-templated", context);
          MimeMessage message = mailSender.createMimeMessage();
          MimeMessageHelper helper =
                  new MimeMessageHelper(message, true, "UTF-8");
          helper.setTo(to);
            helper.setSubject("Welcome to TaskWave!");
            helper.setText(html, true);
            mailSender.send(message);

      } catch (Exception e) {
          throw new RuntimeException(e);
      }
    }
}
