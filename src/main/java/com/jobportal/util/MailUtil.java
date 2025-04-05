package com.jobportal.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.UnsupportedEncodingException;

//Creating Util to send Email
public class MailUtil {

    public static String createToken() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(30);
        for (int i = 0; i < 30; i++) {
            int index = (int) (chars.length() * Math.random());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    public static String getSiteUrl(HttpServletRequest servletRequest) { //Retrieving base url
        String siteUrl = servletRequest.getRequestURL().toString();
        return siteUrl.replace(servletRequest.getServletPath(), "");
    }

    public static void sendEmail(JavaMailSender mailSender, String userEmail, String resetLink) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        messageHelper.setFrom("emregokyar1940@gmail.com", "Job Portal Reset Password");
        messageHelper.setTo(userEmail);

        String subject = "Here Link to reset your password";
        String content = "<p>Hello,</p>"
                + "<p>We received a request to reset your password.</p>"
                + "<p>To proceed, please click the link below:</p>"
                + "<p><a href=\"" + resetLink + "\">Reset My Password</a></p>"
                + "<br>"
                + "<p>If you remember your password or did not request a reset, please ignore this email.</p>";

        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);
        mailSender.send(message);
    }
}
