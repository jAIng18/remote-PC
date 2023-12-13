package ui;

import email.CheckMail;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Controller {

    @FXML
    private Button btnStart;

    @FXML
    private TextField mails;

    @FXML
    private BorderPane main;

    public Parent listen;
    private void writeTextToFile(String text, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        try {
            listen = FXMLLoader.load(getClass().getResource("listen.fxml"));
            System.out.println("success");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void BtnPressed(ActionEvent event){
        String text = mails.getText();
        writeTextToFile(text, "acceptedMail.txt");
        main.setCenter(listen);
        CheckMail.getInstance().listen();
        System.out.println("listening...");
    }
}
