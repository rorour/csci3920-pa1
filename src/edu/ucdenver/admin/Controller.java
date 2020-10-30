package edu.ucdenver.admin;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Controller {
    public Pane paneConnectServer;
    public TextField textfieldServer;
    public TextField textfieldPort;
    public Button btnConnect;
    public Pane paneLogin;
    public TextField textfieldEmail;
    public PasswordField passfieldPassword;
    public Button btnLogin;
    public Pane paneCatalog;
    public ComboBox comboUserType;
    public TextField textfieldUsername;
    public TextField textfieldUserEmail;
    public TextField textfieldUserPassword;
    public Button btnAddUser;
    public Button btnAddProduct;
    public TextField textfieldProductName;
    public TextField textfieldProductBrand;
    public TextField textfieldProductID;
    public DatePicker dateIncorporatedDate;
    public TextField textfieldProductDesc;
    public Button btnAddBook;
    public TextField textfieldBookTitle;
    public TextField textfieldProductBrand1;
    public TextField textfieldProductID1;
    public DatePicker dateIncorporatedDate1;
    public TextField textfieldProductDesc1;
    public Button btnExit;
    public Socket serverConnection = null;
    public ObjectOutputStream output = null;
    public ObjectInputStream input = null;

    public void connectToServer(ActionEvent actionEvent) {
//connects to server
        String ip = textfieldServer.getText();
        int port = Integer.parseInt(textfieldPort.getText());
        Alert alert = null;

        try {
            serverConnection = new Socket(ip, port);
            output = new ObjectOutputStream(serverConnection.getOutputStream());
            input = new ObjectInputStream(serverConnection.getInputStream());
        } catch (IOException e) {
            alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }

        if (serverConnection.isConnected()){
            alert = null;
            String servermessage = null;
            try {
                servermessage = (String)input.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            alert = new Alert(Alert.AlertType.CONFIRMATION, servermessage);
            alert.show();
            paneConnectServer.setVisible(false);
            paneConnectServer.setDisable(true);
            paneLogin.setVisible(true);
            paneLogin.setDisable(false);
        }
    }

    public void loginUser(ActionEvent actionEvent) {
        String email = null;
        String password = null;
        email = textfieldEmail.getText();
        password = passfieldPassword.getText();
        Alert alert = null;
        String servermessage;
        boolean loggedIn = false;

        try {
            output.writeObject(email);
            output.flush();
            output.writeObject(password);
            output.flush();
            servermessage = (String)input.readObject();
            alert = new Alert(Alert.AlertType.CONFIRMATION, servermessage);
            alert.show();
            if (servermessage.charAt(0) == '0'){
                loggedIn = true;
            }
        } catch (IOException | ClassNotFoundException e) {
            alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }

        if (loggedIn){
            paneLogin.setVisible(false);
            paneLogin.setDisable(true);
            paneCatalog.setVisible(true);
            paneCatalog.setDisable(false);
        }
    }

    public void addProduct(ActionEvent actionEvent) {
    }

    public void addBook(ActionEvent actionEvent) {
    }

    public void exitApplication(ActionEvent actionEvent) {
    }

    public void returnOrderReport(ActionEvent actionEvent) {
    }
}
