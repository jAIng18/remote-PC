package email;
import javax.mail.internet.InternetAddress;
import javax.mail.*;
import java.io.*;
import java.util.Arrays;
import java.util.Properties;
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

    public void listen() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                        Folder emailFolder = null;
                        emailFolder = store.getFolder("INBOX");
                        emailFolder.open(Folder.READ_WRITE);
                        Message[] ls = emailFolder.getMessages();
//                        boolean first = true;
//                        for (int i = ls.length - 1; i >= 0; i--) {
//                            var m = ls[i];
//                            String subject = m.getSubject();
//                            if (subject.startsWith("req") && first) {
//                                //boolean kq = Main.processRequest(subject);
//                                boolean kq = processRequest(subject);
//                                if (kq) {
//                                    System.out.println("Resolved " + subject);
//                                } else {
//                                    System.out.println("Rejected " + subject);
//                                }
//                                first = false;
//                            }
//                            m.setFlag(Flags.Flag.DELETED, true);
                        if (ls.length != 0)
                        {
                            Message newEmail = ls[0];
                            Address[] froms = newEmail.getFrom();
                            String from = (froms == null) ? "" : ((InternetAddress) froms[0]).getAddress();
                            String subject = newEmail.getSubject();
                            boolean exists = Arrays.asList(acpEmail).contains(from);
                            int kq = 0;
                            if (exists)
                                kq = Main.processRequest(subject);
                            if (kq == 1) {
                                System.out.println("Resolved " + subject);
                            } else if (kq == 0){
                                System.out.println("Rejected " + subject);
                            }else if(kq==2){
                                System.out.println("Bye...");
                                break;
                            }
                            newEmail.setFlag(Flags.Flag.DELETED, true);
                        }
                        emailFolder.close(true);
                    } catch (InterruptedException e) {
                        System.out.println("Can't connect to mail");
                        e.printStackTrace();
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();
    }
    public static void main(String[] args) throws Exception {
        CheckMail check = CheckMail.getInstance();
        check.listen();
    }
}