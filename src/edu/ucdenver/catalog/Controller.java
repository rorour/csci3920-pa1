package edu.ucdenver.catalog;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Controller {
    public TextField textFieldServer;
    public TextField textFieldPort;
    public Button btnConnect;
    public Pane paneConnectServer;
    public Pane paneLogin;
    public Pane paneCatalog;
    public TextField textFieldEmail;
    public PasswordField passfieldPassword;
    public Button btnLogin;
    public Button btnAddToCart;
    public TextArea textareaDescription;
    public Button btnSearch;
    public TextField textfieldSearch;
    public ListView listOrders;
    public Button btnRemove;
    public Button btnFinalize;
    public ImageView imageOrder;
    public TextArea textareaOrderDesc;
    public ListView listFinalizedOrderList;
    public Button btnExit;
    public Socket serverConnection = null;
    public ObjectOutputStream output = null;
    public ObjectInputStream input = null;

    public void connectToServer(ActionEvent actionEvent) {
        //connects to server
        String ip = textFieldServer.getText();
        int port = Integer.parseInt(textFieldPort.getText());
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
            email = textFieldEmail.getText();
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

    public void addToCart(ActionEvent actionEvent) {

    }

    public void searchProduct(ActionEvent actionEvent) {
    }

    public void removeFromOrder(ActionEvent actionEvent) {
    }

    public void finalizeOrder(ActionEvent actionEvent) {
    }

    public void exitApplication(ActionEvent actionEvent) {
    }
}
