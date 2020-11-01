package edu.ucdenver.admin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdminApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("adminApplication.fxml"));
        primaryStage.setTitle("Admin Application");
        primaryStage.setScene(new Scene(root, 840, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}