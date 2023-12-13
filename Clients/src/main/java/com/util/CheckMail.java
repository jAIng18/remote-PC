package com.util;
import javax.imageio.ImageIO;
import javax.mail.internet.InternetAddress;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import java.util.ArrayList;

public class CheckMail {

    private String username, password;
    private String[] acpEmail = extractEmails();
    private Store store;
    public int count;
    private static CheckMail instance = new CheckMail();

    public static CheckMail getInstance() {
        return instance;
    }
    public static String[] extractEmails() {
        String text = "";
        try (BufferedReader br = new BufferedReader(new FileReader("acceptedMail.txt"))) {
            text = br.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String pattern = "[^,\\s]+@[^,\\s]+";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(text);

        List<String> results = new ArrayList<>();
        while (m.find()) {
            results.add(m.group());
        }

        return results.toArray(new String[0]);
    }
    private CheckMail() {
        try {
            getMail();
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "pop3");
            properties.put("mail.pop3.host", "pop.gmail.com");
            properties.put("mail.pop3.port", "995");
            properties.put("mail.pop3.starttls.enable", "true");
            Session emailSession = Session.getInstance(properties);
            this.store = emailSession.getStore("pop3s");
            store.connect("pop.gmail.com", username, password);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void getMail() {
        try (BufferedReader br = new BufferedReader(new FileReader("mail.txt"))) {
            this.username = br.readLine().trim();
            this.password = br.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean processRequest(String header){
        String[] parts = header.split("/");
        System.out.println(Arrays.toString(parts));
        return true;
    }

    public String listen(String command) {
        final AtomicReference<String> result = new AtomicReference<>(); // Using AtomicReference to store the result

        Thread thread = new Thread() {
            @Override
            public void run() {
                boolean runLoop = true;
                long startTime = System.currentTimeMillis();
                if (command != null && (command.contains("shutdown") || command.contains("restart"))) {
                    runLoop = false;
                }
                while (runLoop || (System.currentTimeMillis() - startTime < 10000)) {
                    try {
                        Thread.sleep(500);
                        Folder emailFolder = store.getFolder("INBOX");
                        emailFolder.open(Folder.READ_WRITE);
                        Message[] ls = emailFolder.getMessages();

                        if (ls.length != 0) {
                            Message newEmail = ls[0];
                            Address[] froms = newEmail.getFrom();
                            String from = (froms == null) ? "" : ((InternetAddress) froms[0]).getAddress();
                            String subject = newEmail.getSubject();
                            boolean exists = Arrays.asList(acpEmail).contains(from);

                            if (exists) {
                                String a = downloadAttachment(newEmail);
                                if(a == null)
                                    result.set(subject);
                                else
                                    result.set(a); // Set the result
                                emailFolder.close(true);
                                return; // Stop listening once an email is found
                            }
                            newEmail.setFlag(Flags.Flag.DELETED, true);
                        }
                        emailFolder.close(true);
                    } catch (InterruptedException e) {
                        System.out.println("Can't connect to mail");
                        e.printStackTrace();
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        thread.start();

        try {
            thread.join(); // Wait for the thread to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result.get(); // Return the stored result
    }

    public String downloadAttachment(Message message) throws MessagingException, IOException {
        Object content = message.getContent();
        if (content instanceof String) {
            return (String) content;
        } else {
            Multipart multiPart = (Multipart) content;
            int numberOfParts = multiPart.getCount();
            for (int partCount = 0; partCount < numberOfParts; partCount++) {
                MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                    String file = part.getFileName();
                    if (file.contains(".png")) {
                        Path folder = Path.of("cache");
                        if (!Files.exists(folder))
                            Files.createDirectory(folder);
                        BufferedImage image = ImageIO.read(part.getInputStream());
                        BufferedOutputStream out = new BufferedOutputStream(
                                Files.newOutputStream(folder.resolve(file)));
                        ImageIO.write(image, "png", out);
                        out.flush();
                        out.close();
                        return folder.resolve(file).toAbsolutePath().toString();
                    } else {
                        Path folder = Path.of("cache");
                        if (!Files.exists(folder))
                            Files.createDirectory(folder);
                        try (BufferedInputStream input = new BufferedInputStream(part.getInputStream());
                             BufferedOutputStream output = new BufferedOutputStream(Files.newOutputStream(folder.resolve(file)))
                        ) {
                            byte[] buffer = new byte[32768];
                            int read;
                            while ((read = input.read(buffer, 0, 32768)) != -1) {
                                output.write(buffer, 0, read);
                                output.flush();
                            }
                            return folder.resolve(file).toAbsolutePath().toString();
                        }
                    }
                }
            }
        }
        return null;
    }
    public static void main(String[] args) throws Exception {
        CheckMail check = CheckMail.getInstance();
        System.out.println("Listen...");
        check.listen("");
    }
}