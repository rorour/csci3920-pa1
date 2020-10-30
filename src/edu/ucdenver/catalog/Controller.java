package edu.ucdenver.catalog;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

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


    public void connectToServer(ActionEvent actionEvent) {
        //connects to server

        //this is just a test
        paneConnectServer.setVisible(false);
        paneConnectServer.setDisable(true);
        paneLogin.setVisible(true);
        paneLogin.setDisable(false);

    }

    public void loginUser(ActionEvent actionEvent) {

        //this is just a test
        paneLogin.setVisible(false);
        paneLogin.setDisable(true);
        paneCatalog.setVisible(true);
        paneCatalog.setDisable(false);
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
