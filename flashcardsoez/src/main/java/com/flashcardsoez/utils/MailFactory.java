package com.flashcardsoez.utils;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailFactory {

    public class MailConfig {

        public static final String from = "nguyeniah2810@gmail.com";
        public static final String password = "spywxxbohxurpdot";
        public static final String HOST_NAME = "smtp.gmail.com";
        public static final int TSL_PORT = 587;

    }

    public static void sendEmail(String TO, String SUBJECT, String CONTENT) {
        // Get properties object
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", MailConfig.HOST_NAME);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", MailConfig.TSL_PORT);
        // get Session
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MailConfig.from, MailConfig.password);
            }
        });

        // compose message
        try {
            MimeMessage message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(TO));
            message.setSubject(SUBJECT);
            message.setContent(CONTENT, "text/html");
            Transport.send(message);
            System.out.println("Email sent");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
