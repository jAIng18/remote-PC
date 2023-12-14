package email;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ProcessPC {
    String os;
    Runtime runtime;

    private static ProcessPC instance = new ProcessPC();

    public ProcessPC() {
        try {
            this.os = System.getProperty("os.name").toLowerCase();
            this.runtime = Runtime.getRuntime();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static ProcessPC getInstance() {
        return instance;
    }

    public String ProcessList() {
        try {
            Process process = null;
            if (this.os.contains("win")) {
                process = runtime.exec("tasklist");
            } else if (this.os.contains("mac") || this.os.contains("nux") || this.os.contains("nix")) {
                process = runtime.exec("ps aux");
            }
            if (process != null) {
                String filename = "ProcessList " + ZonedDateTime.now().format(DateTimeFormatter
                        .ofPattern("dd-MM-yyyy HH-mm")) + ".txt";
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                }
                writer.close();
                reader.close();
                return filename;
            } else {
                return "Unsupported operating system";
            }
        } catch (Exception e) {
            return e.toString();
        }
    }


    public void StopProcess(String appname) {
        try {
            if (this.os.contains("win")) {
                if (appname.contains("exe")) {
                    this.runtime.exec("taskkill /F /IM " + appname);
                } else {
                    this.runtime.exec("taskkill /F /IM " + appname + ".exe");
                }
                System.out.println("Kill " + appname + " successfully");
            } else if (this.os.contains("mac") || this.os.contains("nix") || this.os.contains("nux")) {
                this.runtime.exec("pkill -f " + appname);
                System.out.println("Kill " + appname + " successfully");
            } else {
                System.out.println("Unsupported operating system");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public String StopProcess(int processid) {
        try {
            if (this.os.contains("win")) {
                this.runtime.exec("taskkill /F /PID " + processid);
                return "res/Kill " + processid + " successfully";
            } else if (this.os.contains("mac") || this.os.contains("nix") || this.os.contains("nux")) {
                this.runtime.exec("kill " + processid);
                return "res/Kill " + processid + " successfully";
            } else {
                System.out.println("Unsupported operating system");
            }
        } catch (Exception e) {
            return e.toString();
        }
        return "Cann't kill";
    }

    public String StartProcess(String path) {
        try {
            if (this.os.contains("win") || this.os.contains("nux") || this.os.contains("nix")) {
                this.runtime.exec(path);
                return "res/Start " + path + " successfully";
            } else if (this.os.contains("mac")) {
                this.runtime.exec("open -n " + path);
                return "res/Start " + path + " successfully";
            } else {
                return "Unsuported Operating System";
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return "Cann't kill";
    }

    public static void main(String[] arg) {
        System.out.println("List Process");
        ProcessPC processPC = new ProcessPC();
        processPC.StartProcess(" /System/Applications/Calculator.app");
        String s = processPC.ProcessList();
        System.out.println(s);
    }
}
