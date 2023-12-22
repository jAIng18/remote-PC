package email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class SendMail {

    private Session session;
    private Message message;
    private String username, password;
    private static SendMail instance = new SendMail();

    public static SendMail getInstance()
    {
        return instance;
    }
    private void accountMail()
    {
        try (BufferedReader br = new BufferedReader(new FileReader("mail.txt"))) {
            this.username = br.readLine().trim();
            this.password = br.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SendMail()
    {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        accountMail();

        this.session =
                Session.getDefaultInstance(properties, new Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(
                                        username, password
                                );
                            }
                        }
                );
        try {
            this.message = new MimeMessage(this.session);

            this.message.setFrom(new InternetAddress(username));

            this.message.setRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress("clientaik22@gmail.com")
            );
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }
    public void Send(String subject, String text, String file)
            throws IOException, MessagingException
    {
        this.message.setSubject(subject);
        MimeBodyPart filePart = new MimeBodyPart();

        if (text != null)
            filePart.setText(text);
        if (file != null)
            filePart.attachFile(file);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(filePart);
        this.message.setContent(multipart);
        Transport.send(message);

        System.out.println("Sucessful...");
        return;
    }

    public static void main(String[] args)
    {
        try {
            SendMail.getInstance().Send("[Test]", "ScreenShot nè", null);
        }catch(IOException | MessagingException e)
        {
            e.printStackTrace();
        }
    }
}