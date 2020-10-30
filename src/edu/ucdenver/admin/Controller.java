package edu.ucdenver.admin;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

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

    public void connectToServer(ActionEvent actionEvent) {
        paneConnectServer.setVisible(false);
        paneConnectServer.setDisable(true);
        paneLogin.setVisible(true);
        paneLogin.setDisable(false);
    }

    public void loginUser(ActionEvent actionEvent) {
        paneLogin.setVisible(false);
        paneLogin.setDisable(true);
        paneCatalog.setVisible(true);
        paneCatalog.setDisable(false);
    }

    public void addProduct(ActionEvent actionEvent) {
    }

    public void addBook(ActionEvent actionEvent) {
    }

    public void exitApplication(ActionEvent actionEvent) {
    }
}
