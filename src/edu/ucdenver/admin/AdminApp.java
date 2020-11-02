package edu.ucdenver.admin;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdminApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("adminApplication.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Admin Application");
        primaryStage.setScene(new Scene(root, 840, 600));

        Controller controller = loader.getController();

        primaryStage.setOnCloseRequest(event -> {
            controller.shutdown();
            primaryStage.close();
        });

        primaryStage.show();

        ///original code
//        Parent root = FXMLLoader.load(getClass().getResource("adminApplication.fxml"));
//        primaryStage.setTitle("Admin Application");
//        primaryStage.setScene(new Scene(root, 840, 600));
//        primaryStage.show();
        ///
    }


    public static void main(String[] args) {
        launch(args);
    }
}
