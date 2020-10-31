package edu.ucdenver.admin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;

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
    public ComboBox secUserType;
    public Button btnAddUser;
    public Button btnAddBook;
    public Button btnExit;
    public Socket serverConnection = null;
    public ObjectOutputStream output = null;
    public ObjectInputStream input = null;
    public TextField textfieldDefaultCategory;
    public Button btnSetDefault;
    public TextField textDefaultCategory;
    public ListView listCategories;
    public TextField textfieldNewCategory;
    public Button btnAddCategory;
    public Button btnRemoveCategory;
    public TextField textfieldRemoveCategory;
    public ListView listFinalizedOrders;
    public DatePicker dateStart;
    public DatePicker dateEnd;
    public Button btnDateOrder;
    public TextField textProductName;
    public TextField textfieldProductID;
    public TextField textfieldProductBrand;
    public TextArea textAreaProductDesc;
    public DatePicker dateIncorporated;
    public ComboBox secProductType;
    public Pane paneBook;
    public TextField textfieldAuthor;
    public DatePicker datePublication;
    public TextField textfieldPages;
    public Pane paneAddHome;
    public TextField textfieldLocation;
    public Button btnHome;
    public TextField textfieldSerialNum;
    public TextField textfieldWarranty;
    public Button btnAddElectronic;
    public Pane paneComputer;
    public TextField textfieldSerialNumComp;
    public TextField textfieldWarrantyComp;
    public Button btnAddComputer;
    public TextField textfieldSpecs;
    public Pane panePhone;
    public TextField textfieldSerialNumPhone;
    public TextField textfieldWarrantyPhone;
    public Button btnAddPhone;
    public TextField textfieldImei;
    public TextField textfieldOS;
    public Pane paneElectronic;
    public TextField textAddUserName;
    public TextField textAddUserEmail;
    public TextField textAddUserPassword;
    public TextField textSetCategory;
    public ListView listProducts1;
    public ComboBox secProductCategoryAdd;
    public Button btnAddProductCategory;
    public TextArea textAProductCategories;
    public ListView listProducts2;
    public ComboBox secProductCategoryRemove;
    public Button btnRemoveProductCategory;
    public TextArea textAProductCategories2;
    public Button btnSave;
    public Button btnTerminate;

    private String productName;
    private String productId;
    private String productBrand;
    private LocalDate productIncorpDate;
    private String productDesc;
    private String productCategory;

    //TODO: Fill lists and add general function to GUI

    public Controller(){
        secUserType = new ComboBox<>();
        listCategories = new ListView<>();
        listFinalizedOrders = new ListView<>();
        secProductType = new ComboBox<>();


    }
    public void initialize(){
        //set default visibility
        paneConnectServer.setVisible(true);
        paneConnectServer.setDisable(false);
        paneLogin.setVisible(false);
        paneLogin.setDisable(true);
        paneCatalog.setVisible(false);
        paneCatalog.setDisable(false);

        //initialize currentdefault

        secUserType.setItems(FXCollections.observableArrayList("Customer", "Admin"));
        secProductType.setItems(FXCollections.observableArrayList("Book", "Home", "Electronic", "Computer", "Phone"));

        secProductType.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observable, String oldValue, String newValue) {
                System.out.println("OldValue: " + oldValue + "   NewValue: " + newValue);
                if(newValue != null){
                    if(newValue == "Book"){
                        paneBook.setVisible(true);
                        paneBook.setDisable(false);
                        paneAddHome.setVisible(false);
                        paneAddHome.setDisable(true);
                        paneElectronic.setVisible(false);
                        paneElectronic.setDisable(true);
                        paneComputer.setVisible(false);
                        paneComputer.setDisable(true);
                        panePhone.setVisible(false);
                        panePhone.setDisable(true);

                    }
                    else if(newValue == "Home"){
                        paneBook.setVisible(false);
                        paneBook.setDisable(true);
                        paneAddHome.setVisible(true);
                        paneAddHome.setDisable(false);
                        paneElectronic.setVisible(false);
                        paneElectronic.setDisable(true);
                        paneComputer.setVisible(false);
                        paneComputer.setDisable(true);
                        panePhone.setVisible(false);
                        panePhone.setDisable(true);
                    }
                    else if(newValue == "Electronic"){
                        paneBook.setVisible(false);
                        paneBook.setDisable(true);
                        paneAddHome.setVisible(false);
                        paneAddHome.setDisable(true);
                        paneElectronic.setVisible(true);
                        paneElectronic.setDisable(false);
                        paneComputer.setVisible(false);
                        paneComputer.setDisable(true);
                        panePhone.setVisible(false);
                        panePhone.setDisable(true);
                    }
                    else if(newValue == "Computer"){
                        paneBook.setVisible(false);
                        paneBook.setDisable(true);
                        paneAddHome.setVisible(false);
                        paneAddHome.setDisable(true);
                        paneElectronic.setVisible(false);
                        paneElectronic.setDisable(true);
                        paneComputer.setVisible(true);
                        paneComputer.setDisable(false);
                        panePhone.setVisible(false);
                        panePhone.setDisable(true);
                    }
                    else if(newValue == "Phone"){
                        paneBook.setVisible(false);
                        paneBook.setDisable(true);
                        paneAddHome.setVisible(false);
                        paneAddHome.setDisable(true);
                        paneElectronic.setVisible(false);
                        paneElectronic.setDisable(true);
                        paneComputer.setVisible(false);
                        paneComputer.setDisable(true);
                        panePhone.setVisible(true);
                        panePhone.setDisable(false);
                    }
                    else{
                        paneBook.setVisible(false);
                        paneBook.setDisable(true);
                        paneAddHome.setVisible(false);
                        paneAddHome.setDisable(true);
                        paneElectronic.setVisible(false);
                        paneElectronic.setDisable(true);
                        paneComputer.setVisible(false);
                        paneComputer.setDisable(true);
                        panePhone.setVisible(false);
                        panePhone.setDisable(true);
                    }
                }
            }
        });

        //These should be initialized once connected to server
        //this.listFinalizedOrders.setItems(FXCollections.observableArrayList());
        //this.listCategories.setItems((FXCollections.observableArrayList()));
    }
    /**=======================================================
     * Server Login
     ========================================================*/
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

    /**=======================================================
     * Admin Login
     ========================================================*/
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

    /**=======================================================
     * User Management
     ========================================================*/

    public void addUser(ActionEvent actionEvent) {
        String username = textAddUserName.getText();
        String email = textAddUserEmail.getText();
        String password = textAddUserPassword.getText();

        //should be noted, I haven't tested if getValue is the right way to retrieve this...
        if(secUserType.getValue().equals("Customer")){
            //add customer
        }
        else if(secUserType.getValue().equals("Admin")){
            //add admin
        }

    }


    /**=======================================================
     * Order Report
     ========================================================*/

    public void returnOrderReport(ActionEvent actionEvent) {
        String startDate = String.valueOf(dateStart.getValue());
        String endDate = String.valueOf(dateStart.getValue());
    }


    /**=======================================================
     * Category Management
     ========================================================*/

    public void setDefaultCategory(ActionEvent actionEvent) {
        String defaultCategory = textfieldDefaultCategory.getText();
    }

    public void addCategory(ActionEvent actionEvent) {
        String addCategoryStr = textfieldNewCategory.getText();

    }

    public void removeCategory(ActionEvent actionEvent) {
        String removeCategoryStr = textfieldRemoveCategory.getText();
    }

    /**=======================================================
     * Product Management
     ========================================================*/

    /** Sets Strings based on their respected textfields */
    private void setProductStrings(){
        this.productName = this.textProductName.getText();
        this.productId = this.textfieldProductID.getText();
        this.productBrand = this.textfieldProductBrand.getText();
        this.productIncorpDate = this.dateIncorporated.getValue(); //might need to conver to string
        this.productDesc = this.textAreaProductDesc.getText();
        this.productCategory = this.textSetCategory.getText();
    }

    public void addBook(ActionEvent actionEvent) {
        //string names: productName, productId, productBrand, productIncorpDate, productDesc, productCategory
        this.setProductStrings();

        String title = this.textfieldAuthor.getText();
        LocalDate publishDate = this.datePublication.getValue();
        String numOfPages = this.textfieldPages.getText();
    }

    public void addHome(ActionEvent actionEvent) {
        //string names: productName, productId, productBrand, productIncorpDate, productDesc, productCategory
        this.setProductStrings();

        String location = this.textfieldLocation.getText();
    }

    public void addElectronic(ActionEvent actionEvent) {
        //string names: productName, productId, productBrand, productIncorpDate, productDesc, productCategory
        this.setProductStrings();

        String serialNum = this.textfieldSerialNum.getText();
        String warranty = this.textfieldWarranty.getText();
    }

    public void addComputer(ActionEvent actionEvent) {
        //string names: productName, productId, productBrand, productIncorpDate, productDesc, productCategory
        this.setProductStrings();
        String serialNum = this.textfieldSerialNumComp.getText();
        String warranty = this.textfieldWarrantyComp.getText();
        ArrayList<String> specs = new ArrayList<>();
        //process arraylist here. Specs are separated by a comma. I can do this later.
    }

    public void addPhone(ActionEvent actionEvent) {
        //string names: productName, productId, productBrand, productIncorpDate, productDesc, productCategory
        this.setProductStrings();
        String serialNum = this.textfieldSerialNumPhone.getText();
        String warranty = this.textfieldWarrantyPhone.getText();
        String imei = this.textfieldImei.getText();
        String os = this.textfieldOS.getText();
    }

    public void addCategoryToProduct(ActionEvent actionEvent) {
        String category = (String) secProductCategoryAdd.getValue();
        //grab current selected product
    }

    public void removeCategoryFromProduct(ActionEvent actionEvent) {
        String category = (String) secProductCategoryRemove.getValue();
        //get current selected product.

    }

    /**=======================================================
     * MISC.
     ========================================================*/

    public void exitApplication(ActionEvent actionEvent) {
        Stage stage = (Stage) this.btnExit.getScene().getWindow();
        //TODO: Close client safely in exitApplication
        stage.close();
    }


    public void saveChanges(ActionEvent actionEvent) {
    }

    public void terminateServer(ActionEvent actionEvent) {
    }
}
