package com.example.project_orion.security.utils;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String resetUrl){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Password Reset Request");

            String htmlContent = String.format(
                    "<!DOCTYPE html>" +
                            "<html><head><style>" +
                            "body { font-family: Arial, sans-serif; background-color: #f4f4f9; margin: 0; padding: 0; }" +
                            ".email-container { max-width: 600px; margin: 20px auto; background: #ffffff; border: 1px solid #ddd; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); }" +
                            ".email-header { background-color: #2196f3; color: white; text-align: center; padding: 15px 0; font-size: 20px; font-weight: bold; }" +
                            ".email-body { padding: 20px; text-align: center; }" +
                            ".email-button { display: inline-block; background-color: #ffffff; color: #2196f3; text-decoration: none; padding: 10px 20px; border-radius: 5px; font-size: 16px; font-weight: bold; margin-top: 20px; border: 2px solid #2196f3; }" +
                            ".email-button:hover { background-color: #1976d2; color: white; }" +
                            ".email-footer { background-color: #f4f4f9; color: #666; text-align: center; padding: 15px; font-size: 12px; }" +
                            ".email-footer a { color: #2196f3; text-decoration: none; }" +
                            "</style></head><body>" +
                            "<div class='email-container'>" +
                            "<div class='email-header'>Password Reset Request</div>" +
                            "<div class='email-body'>" +
                            "<p>Dear User,</p>" +
                            "<p>We received a request to reset your password. If you did not make this request, you can safely ignore this email.</p>" +
                            "<p>To reset your password, please click the button below:</p>" +
                            "<a href='%s' class='email-button'>Reset Password</a>" +
                            "<p style='margin-top: 20px;'>If you encounter any issues, feel free to contact our support team.</p>" +
                            "</div>" +
                            "<div class='email-footer'>" +
                            "<p>Need assistance? Contact us at <a href='mailto:support@projectorion.com'>support@projectorion.com</a></p>" +
                            "<p>&copy; 2025 Project Orion. All rights reserved.</p>" +
                            "</div></div></body></html>",
                    resetUrl
            );

            helper.setText(htmlContent, true); // Enable HTML content
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendSignUpOTPEmail(String to, String otp){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Your Project Orion Verification Code");

            String htmlContent = String.format(
                    "<!DOCTYPE html>" +
                            "<html><head><style>" +
                            "body { font-family: Arial, sans-serif; background-color: #f4f4f9; margin: 0; padding: 0; }" +
                            ".email-container { max-width: 600px; margin: 20px auto; background: #ffffff; border: 1px solid #ddd; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); }" +
                            ".email-header { background-color: #4caf50; color: white; text-align: center; padding: 15px 0; font-size: 20px; font-weight: bold; }" +
                            ".email-body { padding: 20px; text-align: center; }" +
                            ".otp-code { font-size: 28px; font-weight: bold; color: #4caf50; margin: 20px 0; }" +
                            ".email-footer { background-color: #f4f4f9; color: #666; text-align: center; padding: 15px; font-size: 12px; }" +
                            ".email-footer a { color: #4caf50; text-decoration: none; }" +
                            "</style></head><body>" +
                            "<div class='email-container'>" +
                            "<div class='email-header'>Welcome to Project Orion!</div>" +
                            "<div class='email-body'>" +
                            "<p>Dear User,</p>" +
                            "<p>Your verification code is:</p>" +
                            "<p class='otp-code'>%s</p>" +
                            "<p>Please use this code to complete your sign-up process. This code is valid for the next 10 minutes.</p>" +
                            "<p>If you did not request this code, please contact us immediately.</p>" +
                            "</div>" +
                            "<div class='email-footer'>" +
                            "<p>Need help? Contact us at <a href='mailto:support@projectorion.com'>support@projectorion.com</a></p>" +
                            "<p>&copy; 2025 Project Orion. All rights reserved.</p>" +
                            "</div></body></html>",
                    otp
            );

            helper.setText(htmlContent, true); // Enable HTML content
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
