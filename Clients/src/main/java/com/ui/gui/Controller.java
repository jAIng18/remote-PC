package com.ui.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class Controller {
    @FXML
    private Button btnExit;

    @FXML
    private Button btnMem;

    @FXML
    private Button btnPolicy;

    @FXML
    private Button btnStart;

    @FXML
    private BorderPane mainPane;

    private Parent Mems, Policy, Start;

    public void initialize(){
        try {
            Mems = FXMLLoader.load(getClass().getResource("mem.fxml"));
            Policy = FXMLLoader.load(getClass().getResource("policy.fxml"));
            Start = FXMLLoader.load(getClass().getResource("start.fxml"));
            System.out.println("success");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleClick (ActionEvent event){
        if (event.getSource()==btnStart){
            System.out.println("start");
            mainPane.setCenter(Start);
        }
        else if (event.getSource()==btnPolicy){
            System.out.println("Overview");
            mainPane.setCenter(Policy);
        }
        else if (event.getSource()==btnMem){
            System.out.println("Members");
            mainPane.setCenter(Mems);
        }
        else if (event.getSource()==btnExit){
            System.exit(0);
        }
    }
}