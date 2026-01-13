package util;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exc.EmailException;

public class EmailService {

    private static final String FROM_EMAIL = "uproad.online@gmail.com";
    private static final String PASSWORD = "mchmlnghnfhezrfe";  // App Password (not regular Gmail password)
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    private static Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });
    }

    public static void sendVerificationEmail(String toEmail, String verificationLink) throws EmailException {
        String subject = "Verify your email address";
        //String body = "Dear user,\n\nPlease verify your account by clicking the link below:\n <a href=" + verificationLink+ ">"+verificationLink+"</a>";
        String htmlBody =
                "<html>" +
                    "<body style='font-family: Arial, sans-serif; font-size: 14px; color: #333;'>"
                        + "<p>Dear user,</p>"
                        + "<p>Please verify your account by clicking the link below:</p>"
                        + "<p><a href=\"" + verificationLink + "\" "
                            + "style='display:inline-block; background-color:#007bff; color:#fff; "
                            + "padding:10px 15px; text-decoration:none; border-radius:5px;'>"
                            + "Verify Account</a></p>"
                        + "<p>If the button above doesn’t work, copy and paste this URL into your browser:</p>"
                        + "<p><a href=\"" + verificationLink + "\">" + verificationLink + "</a></p>"
                        + "<p>Thank you,<br>The Uproad Team</p>"
                    + "</body>"
                + "</html>";
        
        sendEmail(toEmail, subject, htmlBody);
    }
    
    public static void sendVerificationEmail(String toEmail, String verificationLink, String verificationDeepLink) throws EmailException {
        String subject = "Verify your email address";
        //String body = "Dear user,\n\nPlease verify your account by clicking the link below:\n <a href=" + verificationLink+ ">"+verificationLink+"</a>";
        String htmlBody =
                "<html>" +
                    "<body style='font-family: Arial, sans-serif; font-size: 14px; color: #333;'>"
                        + "<p>Dear user,</p>"
                        + "<p>Please verify your account by clicking the link below:</p>"
                        + "<p><a href=\"" + verificationDeepLink + "\" "
                            + "style='display:inline-block; background-color:#007bff; color:#fff; "
                            + "padding:10px 15px; text-decoration:none; border-radius:5px;'>"
                            + "Verify Account</a></p>"
                        + "<p>If the button above doesn’t work, copy and paste this URL into your browser:</p>"
                        + "<p><a href=\"" + verificationLink + "\">" + verificationLink + "</a></p>"
                        + "<p>Thank you,<br>The Uproad Team</p>"
                    + "</body>"
                + "</html>";
        
        sendEmail(toEmail, subject, htmlBody);
    }

    public static boolean sendPasswordResetEmail(String recipient, String resetLink) {
        try {
            String subject = "Password Reset Request";
			
			/*
			 * String htmlBody = "<html>" + "<body>" +
			 * "<p>Click the link below to reset your password:</p>" + "<p><a href=\"" +
			 * resetLink + "\">" + resetLink + "</a></p>" +
			 * "<p>If you did not request this, please ignore this email.</p>" + "</body>" +
			 * "</html>";
			 */
            
            String htmlBody =
                    "<html>" +
                        "<body style='font-family: Arial, sans-serif; font-size: 14px; color: #333;'>"
                            + "<p>Dear user,</p>"
                            + "<p>Please click the link below to reset your password:</p>"
                            + "<p><a href=\"" + resetLink + "\" "
                                + "style='display:inline-block; background-color:#007bff; color:#fff; "
                                + "padding:10px 15px; text-decoration:none; border-radius:5px;'>"
                                + "Reset Password</a></p>"
                            + "<p>If the button above doesn’t work, copy and paste this URL into your browser:</p>"
                            + "<p><a href=\"" + resetLink + "\">" + resetLink + "</a></p>"
                            + "<p>Thank you,<br>The Uproad Team</p>"
                        + "</body>"
                    + "</html>";
            
            sendEmail(recipient, subject, htmlBody);
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean sendPasswordResetEmail(String recipient, String resetLink, String resetDeepLink) {
        try {
            String subject = "Password Reset Request";
            
            String htmlBody =
                    "<html>" +
                        "<body style='font-family: Arial, sans-serif; font-size: 14px; color: #333;'>"
                            + "<p>Dear user,</p>"
                            + "<p>Please click the link below to reset your password:</p>"
                            + "<p><a href=\"" + resetDeepLink + "\" "
                                + "style='display:inline-block; background-color:#007bff; color:#fff; "
                                + "padding:10px 15px; text-decoration:none; border-radius:5px;'>"
                                + "Reset Password</a></p>"
                            + "<p>If the button above doesn’t work, copy and paste this URL into your browser:</p>"
                            + "<p><a href=\"" + resetLink + "\">" + resetLink + "</a></p>"
                            + "<p>Thank you,<br>The Uproad Team</p>"
                        + "</body>"
                    + "</html>";
            
            sendEmail(recipient, subject, htmlBody);
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Reusable general-purpose email method
    public static void sendEmail(String toEmail, String subject, String htmlBody) throws EmailException {
        try {
            Message message = new MimeMessage(getSession());
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setContent(htmlBody, "text/html; charset=utf-8");

            Transport.send(message);
            logger.info("Email sent to: " + toEmail);
            
        } catch (MessagingException e) {
        	if (e.getMessage() != null && e.getMessage().toLowerCase().contains("invalid")) {
                throw new EmailException("Invalid email address: " + toEmail, e);
            } else {
                throw new EmailException("Failed to send email to: " + toEmail, e);
            }
        }
    }
    
    
}

