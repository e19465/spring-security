package com.sasindu.springsecurity.helpers;


import com.sasindu.springsecurity.constants.ApplicationConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailTemplateService {

    @Value("${email.service.support.email}")
    private String SUPPORT_EMAIL;


    /**
     * Generate an email template based on the template name and parameters
     * @param templateName String
     * @param params Map<String, String>
     * @return String
     */
    public String generateTemplate(String templateName, Map<String, String> params) {
        return switch (templateName) {
            case ApplicationConstants.EMAIL_VERIFY_TEMPLATE ->
                    generateEmailVerifyEmailTemplate(params.get("toEmail"), params.get("otp"));
            case ApplicationConstants.PASSWORD_RESET_TEMPLATE ->
                    generatePasswordResetEmailTemplate(params.get("toEmail"), params.get("otp"));
            default -> null;
        };
    }


    /**
     * Generate an email template for email verification
     * @param toEmail String
     * @param otp String
     * @return String
     */
    private String generateEmailVerifyEmailTemplate(String toEmail, String otp) {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Email Verification</title>
                <style>
                    body {
                        font-family: "Times New Roman", Times, serif;
                        background-color: #f4f4f9;
                        margin: 0;
                        padding: 0;
                        color: #333;
                    }
                    .container {
                        max-width: 600px;
                        margin: 40px auto;
                        padding: 20px;
                        background-color: #fff;
                        border-radius: 12px;
                        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
                        overflow: hidden;
                    }
                    .header {
                        background: linear-gradient(90deg, #6a11cb, #2575fc);
                        color: #fff;
                        padding: 25px;
                        text-align: center;
                        font-size: 30px;
                        font-weight: bold;
                        letter-spacing: 2px;
                    }
                    h1 {
                        font-size: 26px;
                        margin: 20px 0 10px;
                        color: #333;
                        text-align: center;
                        font-weight: bold;
                    }
                    p {
                        font-size: 18px;
                        line-height: 1.6;
                        margin: 10px 0;
                        color: #555;
                    }
                    .otp {
                        font-size: 32px;
                        font-weight: bold;
                        color: #28a745;
                        margin: 25px 0;
                        text-align: center;
                        padding: 15px 0;
                        background-color: #f9f9f9;
                        border-radius: 8px;
                        border: 1px solid #ddd;
                    }
                    .footer {
                        margin-top: 40px;
                        font-size: 16px;
                        color: #777;
                        text-align: center;
                    }
                    .footer a {
                        color: #6a11cb;
                        text-decoration: none;
                        font-weight: bold;
                    }
                    .team_name {
                        font-weight: bold;
                        color: #2575fc;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">Email Verification</div>
                    <h1>Hello, <strong>%s</strong>!</h1>
                    <p>Thanks for signing up! Please verify your email address by entering the following OTP:</p>
                    <div class="otp"><strong>%s</strong></div>
                    <p>Enter this OTP on the verification screen to confirm your email address.</p>
                    <p>If you didn’t sign up for SHOPPY, you can safely ignore this email.</p>
                    <p>Thanks,<br><span class="team_name">SHOPPY Team</span></p>
                    <div class="footer">
                        <p>Need help? Contact us at <a href="mailto:%s">%s</a></p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(toEmail, otp, this.SUPPORT_EMAIL, this.SUPPORT_EMAIL);
    }


    /**
     * Generate an email template for password reset
     * @param toEmail String
     * @param otp String
     * @return String
     */
    private String generatePasswordResetEmailTemplate(String toEmail, String otp) {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Password Reset</title>
                <style>
                    body {
                        font-family: "Times New Roman", Times, serif;
                        background-color: #f4f4f9;
                        margin: 0;
                        padding: 0;
                        color: #333;
                    }
                    .container {
                        max-width: 600px;
                        margin: 40px auto;
                        padding: 20px;
                        background-color: #fff;
                        border-radius: 12px;
                        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
                        overflow: hidden;
                    }
                    .header {
                        background: linear-gradient(90deg, #6a11cb, #2575fc);
                        color: #fff;
                        padding: 25px;
                        text-align: center;
                        font-size: 30px;
                        font-weight: bold;
                        letter-spacing: 2px;
                    }
                    h1 {
                        font-size: 26px;
                        margin: 20px 0 10px;
                        color: #333;
                        text-align: center;
                        font-weight: bold;
                    }
                    p {
                        font-size: 18px;
                        line-height: 1.6;
                        margin: 10px 0;
                        color: #555;
                    }
                    .otp {
                        font-size: 32px;
                        font-weight: bold;
                        color: rgb(168, 38, 27);
                        margin: 25px 0;
                        text-align: center;
                        padding: 15px 0;
                        background-color: #f9f9f9;
                        border-radius: 8px;
                        border: 1px solid #ddd;
                    }
                    .footer {
                        margin-top: 40px;
                        font-size: 16px;
                        color: #777;
                        text-align: center;
                    }
                    .footer a {
                        color: #6a11cb;
                        text-decoration: none;
                        font-weight: bold;
                    }
                    .team_name {
                        font-weight: bold;
                        color: #2575fc;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">Password Reset Request</div>
                    <h1>Hello, <strong>%s</strong>!</h1>
                    <p>We received a request to reset your password. Use the OTP below to reset it:</p>
                    <div class="otp"><strong>%s</strong></div>
                    <p>If you didn't request a password reset, you can safely ignore this email.</p>
                    <p><strong>Thanks,</strong><br><span class="team_name">SHOPPY Team</span></p>
                    <div class="footer">
                        <p>Need help? Contact us at <a href="mailto:%s">%s</a></p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(toEmail, otp, this.SUPPORT_EMAIL, this.SUPPORT_EMAIL);
    }

}
