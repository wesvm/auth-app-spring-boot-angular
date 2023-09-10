package com.api.login.services;

import com.api.login.models.Token;
import com.api.login.models.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final String APP_BASE_URL = "http://localhost:8080/api/auth";

    @Async
    public void send(
        User user,
        Token token
    ) throws MessagingException {
        String subject = "Email Verification";
        String recipientAddress = user.getEmail();
        String verificationLink = APP_BASE_URL + "/verify-email?token=" + token.getToken();

        String emailContent = "Hello " + user.getName() + ",\n\n"
                + "Please click the link below to verify your email:\n\n"
                + verificationLink + "\n\n"
                + "This link will expire in 10 minutes.\n\n"
                + "Thank you";

        sendEmail(recipientAddress, subject, emailContent);
    }

    private void sendEmail(
            String recipientAddress,
            String subject,
            String emailContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setFrom("hello@mail.com");
        helper.setTo(recipientAddress);
        helper.setSubject(subject);
        helper.setText(emailContent, true);

        mailSender.send(message);
    }

}
