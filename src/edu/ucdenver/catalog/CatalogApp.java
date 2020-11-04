package edu.ucdenver.catalog;

import edu.ucdenver.catalog.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CatalogApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader catalogLoader = new FXMLLoader(getClass().getResource("catalogApplication.fxml"));
        Parent root = catalogLoader.load();
        primaryStage.setTitle("Catalog Application");
        primaryStage.setScene(new Scene(root, 840, 600));

        Controller controller = catalogLoader.getController();

        primaryStage.setOnCloseRequest(event -> {
            controller.shutdown();
            primaryStage.close();
        });

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
