package com.user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendResetEmail(String toEmail, String token) {

        String subject = "Password Reset Request";

        String resetLink = "http://localhost:3000/reset-password?token=" + token;
        String body = "Click the link to reset password:\n" + resetLink +
                "\n\nThis token is valid for 15 minutes.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

}
