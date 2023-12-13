package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root  = FXMLLoader.load(getClass().getResource("main.fxml"));
        stage.setTitle("Server");
        stage.initStyle(StageStyle.DECORATED);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
