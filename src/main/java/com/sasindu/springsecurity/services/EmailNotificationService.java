package com.sasindu.springsecurity.services;

import com.sasindu.springsecurity.constants.ApplicationConstants;
import com.sasindu.springsecurity.helpers.EmailService;
import com.sasindu.springsecurity.helpers.EmailTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * Service class to handle email notifications
 */
@RequiredArgsConstructor
@Service
public class EmailNotificationService {
    private final EmailTemplateService _emailTemplateService;
    private final EmailService _emailService;

    /**
     * Sends an email to the user with the verification code
     * @param to - Email address of the recipient
     * @param otp - Verification code
     */
    public void sendPasswordResetOtpEmail(String to, String otp) {
        try {
            String subject = "Reset Your Password";
            Map<String, String> params = new HashMap<>();
            params.put("toEmail", to);
            params.put("otp", otp);

            String htmlContent = _emailTemplateService.generateTemplate(ApplicationConstants.PASSWORD_RESET_TEMPLATE, params);
            _emailService.sendHtmlEmail(to, subject, htmlContent);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Sends an email to the user with the verification code
     * @param to - Email address of the recipient
     * @param otp - Verification code
     */
    public void sendEmailVerificationOtpEmail(String to, String otp) {
        try {
            String subject = "Verify Your Email";
            Map<String, String> params = new HashMap<>();
            params.put("toEmail", to);
            params.put("otp", otp);

            String htmlContent = _emailTemplateService.generateTemplate(ApplicationConstants.EMAIL_VERIFY_TEMPLATE, params);
            _emailService.sendHtmlEmail(to, subject, htmlContent);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
