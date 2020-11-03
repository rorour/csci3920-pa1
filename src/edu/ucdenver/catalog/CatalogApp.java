package edu.ucdenver.catalog;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CatalogApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //TODO: Exit app without crashing server, like AdminApp
        Parent root = FXMLLoader.load(getClass().getResource("catalogApplication.fxml"));
        primaryStage.setTitle("Catalog Application");
        primaryStage.setScene(new Scene(root, 840, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
