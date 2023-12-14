package com.util;

import java.io.*;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
public class SendMail {
    private Session session;
    private Message message;
    private String email, password;
    private static SendMail instance = new SendMail();

    private void getMail() {
        try (BufferedReader br = new BufferedReader(new FileReader("Clients/mail.txt"))) {
            this.email = br.readLine().trim();
            this.password = br.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SendMail getInstance() {
        return instance;
    }
    public void Send(String subject) throws IOException, MessagingException {
        String to = "baochien465@gmail.com";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        getMail();
        this.session =
                Session.getDefaultInstance(
                        prop,
                        new Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(
                                        email, password
                                );
                            }
                        }
                );

        try {
            this.message = new MimeMessage(this.session);

            this.message.setFrom(new InternetAddress(email));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            String msg = "";

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);
            Transport.send(message);

            System.out.println("Mail successfully sent..");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws MessagingException, IOException {

        SendMail.getInstance().Send("sub");
    }
}
