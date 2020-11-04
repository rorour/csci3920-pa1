package edu.ucdenver.admin;

import edu.ucdenver.company.*;
import edu.ucdenver.company.Product;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class Controller {
    public Pane paneConnectServer;
    public TextField textfieldServer;
    public TextField textfieldPort;
    public Button btnConnect;
    public Pane paneLogin;
    public TextField textfieldEmail;
    public PasswordField passfieldPassword;
    public Button btnLogin;
    public Pane paneAdmin;
    public ComboBox secUserType;
    public Button btnAddUser;
    public Button btnAddBook;
    public Button btnExit;
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
    public ListView listProductForAddCategory;
    public ComboBox secProductCategoryAdd;
    public Button btnAddProductCategory;
    public TextArea textAProductCategories;
    public ListView listProductForRemoveCategory;
    public ComboBox secProductCategoryRemove;
    public Button btnRemoveProductCategory;
    public TextArea textAProductCategories2;
    public Button btnSave;
    public Button btnTerminate;
    public Button btnRemoveProduct;
    public ListView listProductToRemove;

    public Socket serverConnection;// = null;
    public ObjectOutputStream output;// = null;
    public ObjectInputStream input;// = null;
    public TextField textCategoryId;
    public TextField textCategoryDesc;
    public Tab tabAddRemove;
    public Tab tabRemoveCfromP;
    public Tab tabAddCtoP;
    public Tab tabRemoveProduct;
    public Tab tabOrderReport;
    public ComboBox secNewDefault;
    public ComboBox secCategoryAddProduct;
    public TextArea txtAreaDefaultCategory;
    public ComboBox secRemoveCategory;
    public TextArea textAreaOrderDetails;
    public Tab tabProduct;
    public ImageView imageProduct;
    public TextArea textAreaProductDetails;
    public ImageView imgProductPhoto;

    private String productName;
    private String productId;
    private String productBrand;
    private LocalDate productIncorpDate;
    private String productDesc;
    private String productCategory;
    private String clientMessage;
    private String serverMessage;
    private Alert alert;

    private ArrayList<Order> localOrders;
    private ArrayList<Product> localProducts;
    private ArrayList<Category> localCategory;

    private boolean loggedIn;


    private Product selectedProduct;
    //TODO: Fix GUI in Scene Builder

    public Controller() {
        this.output = null;
        this.input = null;
        this.serverConnection = null;
        this.secUserType = new ComboBox<>();
        this.listCategories = new ListView<>();
        this.listFinalizedOrders = new ListView<>();
        this.secProductType = new ComboBox<>();
        this.listProductForAddCategory = new ListView<>();
        this.listProductForRemoveCategory = new ListView<>();
        this.listProductToRemove = new ListView<>();
        this.secProductCategoryAdd = new ComboBox<>();
        this.secProductCategoryRemove = new ComboBox<>();

        this.localOrders = new ArrayList<>();
        this.localCategory = new ArrayList<>();
        this.localProducts = new ArrayList<>();

        this.selectedProduct = null;

        clientMessage = null;
        serverMessage = null;
        alert = null;

        loggedIn = false;



    }

    public void shutdown(){
        System.out.println("Shutting down Admin App.");
        if (this.serverConnection != null){
            try {
                output.writeObject("close client");
                input.close();
                output.close();
                serverConnection.close();
                System.out.println("Connection to server closed.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void initialize() {
        //set default visibility for panes
        if(!loggedIn){
            openServerPane();
            closeLoginPane();
            closeAdminPane();
        }




        //initialize currentdefault
        secUserType.setItems(FXCollections.observableArrayList("Customer", "Admin"));
        secProductType.setItems(FXCollections.observableArrayList("Book", "Home", "Electronic", "Computer", "Phone"));

        //Adds more input options depending if admin is adding a book, home, electronic etc.
        secProductType.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observable, String oldValue, String newValue) {
               // System.out.println("OldValue: " + oldValue + "   NewValue: " + newValue);
                if (newValue != null) {
                    toggleProductOptions(newValue);
                }
            }

        });

        //Sets Product details
        //TODO: haven't tested this yet
        listProductToRemove.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
            @Override
            public void changed(ObservableValue observable, Product oldValue, Product newValue) {
                if(newValue != null){
                    selectedProduct = newValue;
                    textAreaProductDetails.setText(toggleDescription(newValue)); //could also be newValue
                    //NEW displaying image code!
                    BufferedImage bi = new BufferedImage(
                            selectedProduct.getPhoto().getIconWidth(),
                            selectedProduct.getPhoto().getIconHeight(),
                            BufferedImage.TYPE_INT_RGB);
                    Graphics g = bi.createGraphics();
                    selectedProduct.getPhoto().paintIcon(null, g, 0,0);
                    g.dispose();
                    WritableImage wi = new WritableImage(selectedProduct.getPhoto().getIconWidth(), selectedProduct.getPhoto().getIconHeight());
                    SwingFXUtils.toFXImage(bi, wi);
                    imgProductPhoto.setImage(wi);
                    //end displaying image
                } else{
                    //todo: clear image here just in case nothing is selected
                }
            }
        });

        listProductForAddCategory.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
            @Override
            public void changed(ObservableValue observable, Product oldValue, Product newValue) {
                selectedProduct = newValue;
            }
        });
        //remove category from list
        listProductForRemoveCategory.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
            @Override
            public void changed(ObservableValue observable, Product oldValue, Product newValue) {
                selectedProduct = newValue;
                //Changes combobox based on the selected items categories
                secProductCategoryRemove.setItems(FXCollections.observableArrayList(newValue.getCategories()));
            }
        });

        listFinalizedOrders.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Order>() {
            @Override
            public void changed(ObservableValue observable, Order oldValue, Order newValue) {
                if(newValue != null){
                    textAreaOrderDetails.setText(orderConvertToString(newValue));
                }
                else {
                    textAreaOrderDetails.setText("");
                }
            }
        });


    }


    //=======================================================
    //Server Login
    //========================================================*/
    public void connectToServer(ActionEvent actionEvent) {
//connects to server
        //TODO get rid of debugging
        String ip = "127.0.0.1"; //textfieldServer.getText();
        int port = 10001; //Integer.parseInt(textfieldPort.getText());
        alert = null;

        try {
            serverConnection = new Socket(ip, port);
            output = new ObjectOutputStream(serverConnection.getOutputStream());
            input = new ObjectInputStream(serverConnection.getInputStream());
        } catch (IOException e) {
            alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }

        if (serverConnection.isConnected()) {
            alert = null;
            serverMessage = null;
            try {
                serverMessage = (String) input.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            alert = new Alert(Alert.AlertType.CONFIRMATION, serverMessage);
            alert.show();

            //Setting up Login Pane
            closeServerPane();
            openLoginPane();
            closeAdminPane();
        }
    }

    //=======================================================
    // Admin Login
    //========================================================*/
    public void loginUser(ActionEvent actionEvent) {
        String email = "alice@admin.com"; //textfieldEmail.getText();
        String password = "pw123"; //passfieldPassword.getText();
        alert = null;
        serverMessage = null;
        boolean loggedIn = false;

        try {
            output.writeObject(email);
            output.flush();
            output.writeObject(password);
            output.flush();
            serverMessage = (String) input.readObject();
            alert = new Alert(Alert.AlertType.CONFIRMATION, serverMessage);
            alert.showAndWait();
            if (serverMessage.charAt(0) == '0') {
                this.loggedIn = true;

            }
        } catch (IOException | ClassNotFoundException e) {
            alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }

        if (this.loggedIn) {
            //Setting up Management Pane
            closeLoginPane();
            closeServerPane();
            openAdminPane();



            //updates lists and comboboxes with initial values
            try {
                updateAllProductLists();
                updateAllCategoryLists();

                //updateOrderLists();

            } catch (IOException | ClassNotFoundException e) {

            }
        }
    }

    //=======================================================
    //          User Management
    //========================================================

    public void addUser(ActionEvent actionEvent) {
        String username = null;
        String email = null;
        String password = null;
        try {
            username = textAddUserName.getText();
            email = textAddUserEmail.getText();
            password = textAddUserPassword.getText();
        } catch (IllegalArgumentException|NullPointerException iae){
            alert = new Alert(Alert.AlertType.ERROR, "Fields may not have been filled out correctly");
            alert.show();
        }

        clientMessage = "create new user";

        try {
            if (secUserType.getValue().equals("Customer")) {
                output.writeObject(clientMessage);
                output.flush();
                output.writeObject(new Customer(username, email, password));
                output.flush();
                serverMessage = (String)input.readObject();
                alert = new Alert(Alert.AlertType.CONFIRMATION, serverMessage);
                alert.showAndWait();
                cleanUsertext();
            } else if (secUserType.getValue().equals("Admin")) {
                output.writeObject(clientMessage);
                output.flush();
                output.writeObject(new Administrator(username, email, password));
                output.flush();
                serverMessage = (String)input.readObject();
                alert = new Alert(Alert.AlertType.CONFIRMATION, serverMessage);
                alert.showAndWait();
                cleanUsertext();
            } else {
                alert = new Alert(Alert.AlertType.ERROR, "User type not selected");
            }
        } catch (IllegalArgumentException | NullPointerException | IOException | ClassNotFoundException e) {
            alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }

    }

    private void cleanUsertext() {
        textAddUserName.clear();
        textAddUserEmail.clear();
        textAddUserPassword.clear();
    }

     //=======================================================
     // Order Report
     //========================================================

    public void returnOrderReport(ActionEvent actionEvent) {
        LocalDate startDate = dateStart.getValue();
        LocalDate endDate = dateEnd.getValue();
        if(dateStart.getValue() != null && dateEnd.getValue() != null
                && !listFinalizedOrders.getSelectionModel().isEmpty()){
            try {
                output.writeObject("order report by date");
                output.flush();

                output.writeObject(startDate);
                output.flush();

                output.writeObject(endDate);
                output.flush();


                ArrayList<Order> o = (ArrayList<Order>) input.readObject();
                localOrders = null;
                localOrders = o;
                listFinalizedOrders.setItems(FXCollections.observableArrayList(localOrders));


            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


    }


    //=======================================================
    //      Category Management
    //========================================================*/

    public void setDefaultCategory(ActionEvent actionEvent) {
        Category c = (Category) secRemoveCategory.getValue();

        if(!secRemoveCategory.getSelectionModel().isEmpty()){
            try {
                output.writeObject("category management");
                output.flush();
                output.writeObject("set default");
                output.flush();

                output.writeObject(c);
                output.flush();

                //serverMessage = (String)input.readObject();
                alert = new Alert(Alert.AlertType.CONFIRMATION, "Success");
                alert.showAndWait();
            } catch (IOException e) {
                alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            }
        } else {
            alert = new Alert(Alert.AlertType.ERROR, "No Category was Selected");
            alert.show();
        }

    }
    //TODO needs to make sure you can't add
    public void addCategory(ActionEvent actionEvent) {
        String name = textfieldNewCategory.getText();
        String id = textCategoryId.getText();
        String desc = textCategoryDesc.getText();
        try {
            output.writeObject("category management");
            output.flush();
            output.writeObject("add category");
            output.flush();

            output.writeObject(new Category(name, id, desc));
            output.flush();

            updateAllCategoryLists();
            //serverMessage = (String)input.readObject();
            alert = new Alert(Alert.AlertType.CONFIRMATION, "Success");
            alert.showAndWait();


        } catch (IOException | ClassNotFoundException e) {
            alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
        }

    }

    //TODO removeCategory
    public void removeCategory(ActionEvent actionEvent) {
        Category c = (Category) secRemoveCategory.getValue();
        if(!secRemoveCategory.getSelectionModel().isEmpty()){
            try {
                output.writeObject("category management");
                output.flush();
                output.writeObject("remove category");
                output.flush();

                output.writeObject(c);
                output.flush();

                //serverMessage = (String)input.readObject();
                alert = new Alert(Alert.AlertType.CONFIRMATION, "Success");
                alert.showAndWait();

                updateAllCategoryLists();
            } catch (IOException | ClassNotFoundException e) {
                alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            }
        }
        else {
            alert = new Alert(Alert.AlertType.ERROR, "No Category was Selected");
            alert.show();
        }

    }

    //=======================================================
    // Product Management
    // ========================================================*/
    /**
     * Sets Strings based on their respected textfields
     */
    private void setProductStrings() {
        try {
            this.productName = this.textProductName.getText();
            this.productId = this.textfieldProductID.getText();
            this.productBrand = this.textfieldProductBrand.getText();
            this.productIncorpDate = this.dateIncorporated.getValue(); //might need to convert to string
            this.productDesc = this.textAreaProductDesc.getText();
            //todo add category from drop down - i'm commenting this out for now
            //this.productCategory = this.textSetCategory.getText();
            this.productCategory = null;
        } catch (IllegalArgumentException iae){
            alert = new Alert(Alert.AlertType.ERROR, "Fields may not have been filled out correctly\n"
                    + "Error thrown: " + iae.getMessage());
            alert.show();
        }
    }
    //TODO: Revise Confirmation/Error messages for each

    /** Sends message to server to add Product into company
     * @param p Product
     */
    private void productMessage(Product p) {
        try {
            output.writeObject("product management");
            output.flush();

            output.writeObject("add product");
            output.flush();

            output.writeObject(p);
            output.flush();

            serverMessage = (String)input.readObject();

            alert = new Alert(Alert.AlertType.CONFIRMATION, serverMessage);
            alert.showAndWait();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }

    }


    public void addBook(ActionEvent actionEvent) {
        //string names: productName, productId, productBrand, productIncorpDate, productDesc, productCategory
        this.setProductStrings();

        try {
            String author = this.textfieldAuthor.getText();
            LocalDate publishDate = this.datePublication.getValue();
            int numOfPages = Integer.parseInt(this.textfieldPages.getText());

            Product p = new Book(this.productName, this.productId, this.productBrand, this.productDesc,
                    this.productIncorpDate, author, publishDate, numOfPages);

            productMessage(p);
        } catch (IllegalArgumentException iae){
            alert = new Alert(Alert.AlertType.ERROR, "Fields may not have been filled out correctly\n"
                    + "Error thrown: " +  iae.getMessage());
            alert.show();
        }
    }

    public void addHome(ActionEvent actionEvent) {
        //string names: productName, productId, productBrand, productIncorpDate, productDesc, productCategory
        this.setProductStrings();

        try {
            String location = this.textfieldLocation.getText();

            Product p = new Home(this.productName, this.productId, this.productBrand, this.productDesc,
                    this.productIncorpDate, location);

            productMessage(p);
        } catch (IllegalArgumentException iae){
        alert = new Alert(Alert.AlertType.ERROR, "Fields may not have been filled out correctly\n"
                + "Error thrown: " + iae.getMessage());
        alert.show();
    }
    }

    public void addElectronic(ActionEvent actionEvent) {
        //string names: productName, productId, productBrand, productIncorpDate, productDesc, productCategory
        this.setProductStrings();
        try {
            String serialNum = this.textfieldSerialNum.getText();
            int warranty = Integer.parseInt(this.textfieldWarranty.getText());

            Product p = new Electronic(this.productName, this.productId, this.productBrand, this.productDesc,
                    this.productIncorpDate, serialNum, warranty);

            productMessage(p);
        } catch (IllegalArgumentException iae){
            alert = new Alert(Alert.AlertType.ERROR, "Fields may not have been filled out correctly\n"
                    + "Error thrown: " + iae.getMessage());
            alert.show();
        }
    }

    public void addComputer(ActionEvent actionEvent) {
        //string names: productName, productId, productBrand, productIncorpDate, productDesc, productCategory
        this.setProductStrings();
        try {
            String serialNum = this.textfieldSerialNum.getText();
            int warranty = Integer.parseInt(this.textfieldWarranty.getText());
            String specStr = this.textfieldSpecs.getText();
            ArrayList<String> specs = new ArrayList<>();

            Computer p = new Computer(this.productName, this.productId, this.productBrand, this.productDesc,
                    this.productIncorpDate, serialNum, warranty);


            //Creates an array list of given string, separated by them by commas
            if (!specStr.isEmpty()) {
                String[] specParts = specStr.split(",");
                specs = new ArrayList<>(Arrays.asList(specParts));
                for (String s : specs) {
                    p.addSpec(s);
                }
            }

            productMessage(p);
        } catch (IllegalArgumentException iae){
            alert = new Alert(Alert.AlertType.ERROR, "Fields may not have been filled out correctly\n"
                    + "Error thrown: " + iae.getMessage());
            alert.show();
        }

    }

    public void addPhone(ActionEvent actionEvent) {
        //string names: productName, productId, productBrand, productIncorpDate, productDesc, productCategory
        this.setProductStrings();
        try {
            String serialNum = this.textfieldSerialNum.getText();
            int warranty = Integer.parseInt(this.textfieldWarranty.getText());
            String imei = this.textfieldImei.getText();
            String os = this.textfieldOS.getText();

            Product p = new CellPhone(this.productName, this.productId, this.productBrand, this.productDesc,
                    this.productIncorpDate, serialNum, warranty, imei, os);

            productMessage(p);
        } catch (IllegalArgumentException iae){
            alert = new Alert(Alert.AlertType.ERROR, "Fields may not have been filled out correctly\n"
                    + "Error thrown: " + iae.getMessage());
            alert.show();
        }
    }

    public void removeProduct(ActionEvent actionEvent) {
        Product p = null;
        try {
            p = selectedProduct;
        } catch (Error e){
            alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }

        if (p == null){
            alert = new Alert(Alert.AlertType.ERROR, "selected product is null");
            alert.show();
        } else {
            alert = new Alert(Alert.AlertType.INFORMATION, "attempting to remove product " + p.getName());
            alert.show();
        }

        try {
            output.writeObject("product management");
            output.flush();

            output.writeObject("remove product");
            output.flush();

            output.writeObject(p);
            output.flush();

            serverMessage = (String) input.readObject();
            alert = new Alert(Alert.AlertType.INFORMATION, serverMessage);
            alert.show();
            updateAllProductLists();
            textAreaProductDetails.setText("");//clears details
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

//        try {
//            updateAllProductLists();
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//

    }

    public void addCategoryToProduct(ActionEvent actionEvent) {
        Category c = (Category) secProductCategoryAdd.getValue();
        Product p = selectedProduct;
        Alert alert;
        try {
            output.writeObject("product management");
            output.flush();

            output.writeObject("add category to product");
            output.flush();

            output.writeObject(p);
            output.flush();
            output.writeObject(c);
            output.flush();
            //serverMessage = (String)input.readObject();
            alert = new Alert(Alert.AlertType.CONFIRMATION, "Success" );
            alert.showAndWait();
            
            updateAllProductLists();
        } catch (IOException | ClassNotFoundException e) {
            alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }
    }


    public void removeCategoryFromProduct(ActionEvent actionEvent) {
        Category c = (Category) secProductCategoryRemove.getValue();
        Product p = selectedProduct;

        Alert alert;
        if(!secProductCategoryRemove.getSelectionModel().isEmpty()){
            try {
                output.writeObject("product management");
                output.flush();

                output.writeObject("remove category from product");
                output.flush();

                output.writeObject(p);
                output.flush();
                output.writeObject(c);
                output.flush();
                //serverMessage = (String)input.readObject();
                alert = new Alert(Alert.AlertType.CONFIRMATION, "Success");
                alert.show();
            } catch (IOException e) {
                alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                alert.show();
            }
        } else {
            alert = new Alert(Alert.AlertType.ERROR, "Category not selected");
            alert.show();
        }


    }


    //=======================================================
    // Server Exit / Save / Terminate.
    //========================================================

    public void terminateServer(ActionEvent actionEvent) {
        String clientMessage = "terminate";
        try {
            output.writeObject(new String(clientMessage));
            output.flush();
            //serverMessage = (String)input.readObject();
            alert = new Alert(Alert.AlertType.CONFIRMATION, "Server Saved and Terminated");
            alert.showAndWait();
        } catch (IOException e) {
            alert = new Alert(Alert.AlertType.ERROR, "Something went wrong");
        }

    }


    //=======================================================
    // GUI Methods
    //========================================================
    //TODO
    private String toggleDescription(Product p) {
        StringBuilder str = new StringBuilder();
        //reason book is separate is because list order
        str.append("Name: " + p.getName() + "\n" +
                "Id: " + p.getId() + "\n" +
                "Brand: " + p.getBrand() + "\n" +
                "Incorporation Date: " + p.getIncorporatedDate() + "\n" +
                "Categories: ");
        for(Category c : p.getCategories()){
            str.append(c.getName() + " ");
        }
        str.append("\n");
        if(p.productType().equals("Book")){
            Book b =  (Book)p;
            str.append("Author: " + b.getAuthorName() + "\n" +
                    "Publishing Date: " + b.getPublicationDate() + "\n" +
                    "Number of Pages: " + b.getNumOfPages() + "\n");
        }
        else if(p.productType().equals("Home")){
            Home h = (Home)p;
            str.append("Location: " + h.getLocation() + "\n");

        }
        else if(p.productType().equals("Electronic")){
            Electronic e = (Electronic) p;
            str.append("Serial no.: " + e.getSerialNum() + "\n" +
                    "Warranty Period: " + e.getWarrantyPeriod() + " years\n"); //TODO if we change warranty to be a string, we can get rid of this in the string

        }
        else if(p.productType().equals("Computer")){
            Computer computer = (Computer) p;
            str.append("Serial no.: " + computer.getSerialNum() + "\n" +
                    "Warranty Period: " + computer.getWarrantyPeriod() + " years\n");
            str.append("Specs: ");
            for(String s : computer.getSpecs()){
                str.append(s + "  ");
            }
            str.append("\n");
        }
        else if(p.productType().equals("CellPhone")){
            CellPhone phone = (CellPhone) p;
            str.append("Serial no.: " + phone.getSerialNum() + "\n" +
                    "Warranty Period: " + phone.getWarrantyPeriod() + " years\n");
            str.append("IMEI: " + phone.getImei() + "\n +" +
                    "OS: " + phone.getOs());
        }
        return str.toString();
    }
    private void toggleProductOptions(String newValue) {
        if (newValue == "Book") {
            openBookPane();
            closeHomePane();
            closeElectronicPane();
            closeComputerPane();
            closePhonePane();

        } else if (newValue == "Home") {
            openHomePane();
            closeBookPane();
            closeElectronicPane();
            closeComputerPane();
            closePhonePane();
        } else if (newValue == "Electronic") {
            openElectronicPane();
            closeBookPane();
            closeHomePane();
            closeComputerPane();
            closePhonePane();
        } else if (newValue == "Computer") {
            openComputerPane();
            closeBookPane();
            closeHomePane();
            closeElectronicPane();
            closePhonePane();
        } else if (newValue == "Phone") {
            openPhonePane();
            closeBookPane();
            closeHomePane();
            closeElectronicPane();
            closeComputerPane();
        } else {
            closeBookPane();
            closeHomePane();
            closeElectronicPane();
            closeComputerPane();
            closePhonePane();
        }
    }

    private void openServerPane() {
        paneConnectServer.setVisible(true);
        paneConnectServer.setDisable(false);
    }
    private void closeServerPane() {
        paneConnectServer.setVisible(false);
        paneConnectServer.setDisable(true);
    }
    private void openLoginPane() {
        paneLogin.setVisible(true);
        paneLogin.setDisable(false);
    }
    private void closeLoginPane() {
        paneLogin.setVisible(false);
        paneLogin.setDisable(true);
    }
    private void openAdminPane() {
        paneAdmin.setVisible(true);
        paneAdmin.setDisable(false);
    }
    private void closeAdminPane() {
        paneAdmin.setVisible(false);
        paneAdmin.setDisable(true);
    }

    private void openBookPane() {
        paneBook.setVisible(true);
        paneBook.setDisable(false);
    }
    private void closeBookPane() {
        paneBook.setVisible(false);
        paneBook.setDisable(true);
    }
    private void openHomePane() {
        paneAddHome.setVisible(true);
        paneAddHome.setDisable(false);
    }
    private void closeHomePane() {
        paneAddHome.setVisible(false);
        paneAddHome.setDisable(true);
    }
    private void openElectronicPane() {
        paneElectronic.setVisible(true);
        paneElectronic.setDisable(false);
    }
    private void closeElectronicPane() {
        paneElectronic.setVisible(false);
        paneElectronic.setDisable(true);
    }
    private void openComputerPane() {
        paneComputer.setVisible(true);
        paneComputer.setDisable(false);
    }
    private void closeComputerPane() {
        paneComputer.setVisible(false);
        paneComputer.setDisable(true);
    }
    private void openPhonePane() {
        panePhone.setVisible(true);
        panePhone.setDisable(false);
    }
    private void closePhonePane() {
        panePhone.setVisible(false);
        panePhone.setDisable(true);
    }


    public void updateCategoryList(Event event) {
        if(this.tabAddRemove.isSelected()){
//            secProductCategoryAdd.setItems(FXCollections.observableArrayList(localCategory));
//            secNewDefault.setItems(FXCollections.observableArrayList(localCategory));
//            secRemoveCategory.setItems(FXCollections.observableArrayList(localCategory));
//            listCategories.setItems(FXCollections.observableArrayList(localCategory));
        }

    }


    public void updateRemoveCfromP(Event event) {
        if(this.tabRemoveCfromP.isSelected()){
            try {
                output.writeObject("product management");
                output.flush();
                output.writeObject("list products");
                output.flush();
                ArrayList<Product> p = (ArrayList<Product>) input.readObject();
                localCategory = null;
                localProducts = p;

                //listProductForAddCategory.setItems(FXCollections.observableArrayList(p));
                updateAllProductLists();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateAddCtoP(Event event) {
        if(this.tabAddCtoP.isSelected()){
            try {
                //adding products to list
                output.writeObject("product management");
                output.flush();
                output.writeObject("list products");
                output.flush();
                ArrayList<Product> p = (ArrayList<Product>) input.readObject();
                localCategory = null;
                localProducts = p;

                //adding categories to combobox
                output.writeObject("category management");
                output.flush();
                output.writeObject("list categories");
                output.flush();
                ArrayList<Category> c = (ArrayList<Category>) input.readObject();

                secProductCategoryAdd.setItems(FXCollections.observableArrayList(c));

                listProductForAddCategory.setItems(FXCollections.observableArrayList(p));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateOrders(Event event) throws IOException, ClassNotFoundException {
        if(this.tabOrderReport.isSelected()){
            output.writeObject("order report");
            output.flush();
            //Redundant but necessary in order to get the list to update.
            ArrayList<Order> o = (ArrayList<Order>) input.readObject();
            localOrders = null;
            localOrders = o;

            listFinalizedOrders.setItems(FXCollections.observableArrayList(o));
            listFinalizedOrders.getSelectionModel().select(0); //needs to be selected in order to
        }
    }


    // : cleanup. Lets update each list individually, so we can get rid of this
    public void updateAllProductLists() throws IOException, ClassNotFoundException {
        //Initializing lists with objects

        //testing grabbing arraylists
        output.writeObject("product management");
        output.flush();
        output.writeObject("list products");
        output.flush();
        ArrayList<Product> p = (ArrayList<Product>) input.readObject();
        localCategory = null;
        localProducts = p;

        //update all the lists
        listProductForAddCategory.setItems(FXCollections.observableArrayList(p));
        listProductForRemoveCategory.setItems(FXCollections.observableArrayList(p));
        listProductToRemove.setItems(FXCollections.observableArrayList(p));

    }

    //TODO Cleanup.
    public void updateAllCategoryLists() throws IOException, ClassNotFoundException {
        output.writeObject("category management");
        output.flush();
        output.writeObject("list categories");
        output.flush();
        ArrayList<Category> c = (ArrayList<Category>) input.readObject();

        secProductCategoryAdd.setItems(FXCollections.observableArrayList(c));
        secCategoryAddProduct.setItems(FXCollections.observableArrayList(c));
        secNewDefault.setItems(FXCollections.observableArrayList(c));
        secRemoveCategory.setItems(FXCollections.observableArrayList(c));
    }

    public void updateTabRemoveProduct(Event event) {
        if(this.tabRemoveProduct.isSelected()){
            try{
                output.writeObject("product management");
                output.flush();
                output.writeObject("list products");
                output.flush();
                ArrayList<Product> newProducts = (ArrayList<Product>) input.readObject();
                listProductToRemove.setItems(FXCollections.observableArrayList(newProducts));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private String orderConvertToString(Order order){
        StringBuilder sb = new StringBuilder();
        for(Product p : order.getProducts()){
            sb.append(String.format("%s %s %s%n", p.getName(), p.getId(), p.getBrand()));
        }
        return sb.toString();
    }
}
