package edu.ucdenver.catalog;

import edu.ucdenver.company.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

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
    public ListView listOrders;
    public Button btnRemove;
    public Button btnFinalize;
    public TextArea textareaOrderDesc;
    public ListView listFinalizedOrderList;
    public Button btnExit;
    public Socket serverConnection = null;
    public ObjectOutputStream output = null;
    public ObjectInputStream input = null;
    public ListView listCategories;
    public ListView listProducts;
    public ImageView imageProduct;
    public Tab tabBrowse;
    public Tab tabSearch;
    public ListView listProducts1;
    public Button btnAddToCart1;
    public TextArea textareaDescription1;
    public ImageView imageProduct1;
    public Button btnSeeAll;
    public ListView listSearchedProducts;
    public TextField textSearch;
    public Tab tabActiveOrder;
    public Tab tabPastOrders;
    public Button btnCancel;
    public ListView listFinalizedProducts;
    public TextArea textAreafinalProducts;
    public ImageView imageBrowseProduct;
    public ImageView imageSearchProduct;
    public ImageView imageOrder;
    public Label labelWelcome;
    public Tab tabHome;

    private ArrayList<Category> localCategories;
    private ArrayList<Product> localProducts;
    private Product selectedProduct;
    private Category selectedCategory;
    private Order selectedOrder;
    private String customerName;
    private Alert alert;
    private boolean loggedIn;
    private String serverMessage;
    private Product selectedOrderProduct;

    public Controller() {
        listProducts = new ListView<>();
        listSearchedProducts = new ListView<>();
        listCategories = new ListView<>();
        listFinalizedOrderList = new ListView<>();

        listOrders = new ListView<>();
        textAreafinalProducts = null;
        selectedProduct = null;
        selectedOrderProduct = null;
        selectedOrder = null;
    }

    /**
     * called when window closed; terminates connection and tells server to close client connection
     */
    public void shutdown(){
        System.out.println("Shutting down Catalog App.");
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
        //set default visibility
        if (!loggedIn) {
            paneConnectServer.setVisible(true);
            paneConnectServer.setDisable(false);
            paneLogin.setVisible(false);
            paneLogin.setDisable(true);
            paneCatalog.setVisible(false);
            paneCatalog.setDisable(false);
        }


        this.listProducts.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
            @Override
            public void changed(ObservableValue observable, Product oldValue, Product newValue) {
                if (newValue != null) {
                    selectedProduct = newValue; //for ordering

                    textareaDescription.setText(productDesc(newValue));
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
                    imageBrowseProduct.setImage(wi);
                } else {
                    selectedProduct = null;
                }
            }
        });

        this.listSearchedProducts.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
            @Override
            public void changed(ObservableValue observable, Product oldValue, Product newValue) {
                if (newValue != null) {
                    selectedProduct = newValue; //for ordering

                    textareaDescription1.setText(productDesc(newValue));
                    BufferedImage bi = new BufferedImage(
                            selectedProduct.getPhoto().getIconWidth(),
                            selectedProduct.getPhoto().getIconHeight(),
                            BufferedImage.TYPE_INT_RGB);
                    Graphics g = bi.createGraphics();
                    selectedProduct.getPhoto().paintIcon(null, g, 0,0);
                    g.dispose();
                    WritableImage wi = new WritableImage(selectedProduct.getPhoto().getIconWidth(), selectedProduct.getPhoto().getIconHeight());
                    SwingFXUtils.toFXImage(bi, wi);
                    imageSearchProduct.setImage(wi);
                } else {
                    selectedProduct = null;
                }
            }
        });
        this.listCategories.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Category>() {
            @Override
            public void changed(ObservableValue observable, Category oldValue, Category newValue) {
                if (newValue != null) {
                    if (browseCategories(newValue) != null) {
                        listProducts.setItems(FXCollections.observableArrayList(browseCategories(newValue)));
                    } else {
                        listProducts.setItems(FXCollections.emptyObservableList());
                    }
                }
            }
        });

        this.listOrders.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
            @Override
            public void changed(ObservableValue observable, Product oldValue, Product newValue) {
                if(newValue != null){
                    selectedOrderProduct = newValue;
                    textareaOrderDesc.setText((productDesc(newValue)));
                    BufferedImage bi = new BufferedImage(
                            selectedProduct.getPhoto().getIconWidth(),
                            selectedProduct.getPhoto().getIconHeight(),
                            BufferedImage.TYPE_INT_RGB);
                    Graphics g = bi.createGraphics();
                    selectedProduct.getPhoto().paintIcon(null, g, 0,0);
                    g.dispose();
                    WritableImage wi = new WritableImage(selectedProduct.getPhoto().getIconWidth(), selectedProduct.getPhoto().getIconHeight());
                    SwingFXUtils.toFXImage(bi, wi);
                    imageOrder.setImage(wi);
                }
                else {
                    selectedOrderProduct = null;
                }
            }
        });

        this.listFinalizedOrderList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Order>() {
            @Override
            public void changed(ObservableValue observable, Order oldValue, Order newValue) {
                System.out.println(newValue);
                if(newValue != null){
                    listFinalizedProducts.setItems(FXCollections.observableArrayList(newValue.getProducts()));
                    System.out.println(newValue.getProducts());
                } else {
                    listFinalizedProducts.setItems(FXCollections.emptyObservableList());
                }
            }
        });
    }

    //=======================================================
    //Server Login
    //========================================================*/
    /**
     * connects to server
     */
    public void connectToServer(ActionEvent actionEvent) {
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

        if (serverConnection.isConnected()) {
            alert = null;
            String servermessage = null;
            try {
                servermessage = (String) input.readObject();
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

    //=======================================================
    // Customer Login
    //========================================================*/
    /**
     * log in user using email and password
     */
    public void loginUser(ActionEvent actionEvent) throws IOException {
        String email = "charlie@customer.com"; //textfieldEmail.getText();
        String password = "123456pw"; //passfieldPassword.getText();
        alert = null;
        serverMessage = null;
        //boolean loggedIn = false;

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

        if (loggedIn) {
            paneLogin.setVisible(false);
            paneLogin.setDisable(true);
            paneCatalog.setVisible(true);
            paneCatalog.setDisable(false);
            updateBrowseLists(); //needed because browse is the first tab opened.
            updateWelcomeMessage();
        }
    }

    /**
     * updates welcome message on Home tab
     */
    private void updateWelcomeMessage() {
        try {
            output.writeObject("get name");
            output.flush();
            customerName = (String)input.readObject();
        } catch (ClassNotFoundException | IOException | NullPointerException e){
            System.out.println(e.getMessage());
        }
        if (customerName != null && customerName.charAt(0) != '1'){
            labelWelcome.setText(String.format("Welcome to the catalog, %s!", customerName));
        } else {
            labelWelcome.setText("Welcome to the catalog!");
        }
    }

    //=======================================================
    // Browse and Search
    //========================================================*/
    /**
     * gets all products in catalog
     */
    private ArrayList<Product> browseCategories(Category c) {
        ArrayList<Product> p = null;
        try {
            output.writeObject("browse");
            output.flush();
            output.writeObject(c);
            output.flush();
            p = (ArrayList<Product>) input.readObject();
            return p;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return p;
    }


    //=======================================================
    // Order Management
    //========================================================*/
    /**
     * add product to open order.
     * If no existing open order, creates new one.
     */
    public void addToCart(ActionEvent actionEvent) {
        if(selectedProduct != null){
            Product p = selectedProduct;
            try {
                //creates an open order in case there isn't already one
                output.writeObject("order management");
                output.flush();

                output.writeObject("add product to order");
                output.flush();

                output.writeObject(p);
                output.flush();

                alert = new Alert(Alert.AlertType.CONFIRMATION, p.getName() + " was added Successfully");
                alert.show();
            } catch (IOException e) {
                alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                alert.show();
                e.printStackTrace();

            }
        }
    }
    /**
     * remove product from open order
     */
    public void removeFromOrder(ActionEvent actionEvent) {
        Product p = selectedProduct;
        try {
            output.writeObject("order management");
            output.flush();

            output.writeObject("remove product from order");
            output.flush();

            output.writeObject(p);
            output.flush();

            serverMessage = (String) input.readObject();

            //update list (will tab do it automatically?
            listOrders.setItems(FXCollections.observableArrayList());
            output.writeObject("order management");
            output.flush();

            output.writeObject("list order products");
            output.flush();

            ArrayList<Order> orders = (ArrayList<Order>) input.readObject();
            listOrders.setItems(FXCollections.observableArrayList(orders));
            alert = new Alert(Alert.AlertType.CONFIRMATION, serverMessage);
            alert.showAndWait();
        } catch (IOException | ClassNotFoundException e) {
            alert = new Alert(Alert.AlertType.ERROR, serverMessage);
        }
    }

    /**
     * cancel open order
     */
    public void cancelOrder(ActionEvent actionEvent) {
        try {
            output.writeObject("order management");
            output.flush();

            output.writeObject("cancel order");
            output.flush();

            serverMessage = (String) input.readObject();
            alert = new Alert(Alert.AlertType.CONFIRMATION, serverMessage);
            alert.showAndWait();

            clearOrderLists();

        } catch (IOException | ClassNotFoundException e) {
            alert = new Alert(Alert.AlertType.ERROR, serverMessage);
            alert.show();
        }
    }

    /**
     * finalize order; once this is done it can no longer be edited by customer
     */
    public void finalizeOrder(ActionEvent actionEvent) {
        //takes current open order and finalizes it
        try {
            output.writeObject("order management");
            output.flush();

            output.writeObject("finalized order");
            output.flush();

            serverMessage = (String) input.readObject();

            alert = new Alert(Alert.AlertType.CONFIRMATION, serverMessage);
            alert.showAndWait();

            clearOrderLists();
            
        } catch (IOException | ClassNotFoundException e) {
            alert = new Alert(Alert.AlertType.ERROR, serverMessage);
            alert.show();
        }


    }

    private void clearOrderLists(){
        listOrders.setItems(FXCollections.emptyObservableList()); //clears list
        textareaOrderDesc.setText("");
    }


    //=======================================================
    // GUI Methods
    //========================================================*/
    /**
     * below functions update tabs/text fields.
     */
    private String productDesc(Product p) {
        StringBuilder str = new StringBuilder();
        //reason book is separate is because list order
        if (p.productType().equals("Book")) {
            Book b = (Book) p;
            str.append("\"" + p.getName() + "\" by " + b.getAuthorName() + '\n' +
                    "Published: " + b.getPublicationDate() + '\n' +
                    "Pages: " + b.getNumOfPages() + '\n');
            str.append(p.getBrand() + '\n');
        } else {
            str.append(p.getName() + "from " + p.getBrand() + '\n');
            if (p.productType().equals("Home")) {
                Home h = (Home) p;
                str.append("Location: " + h.getLocation() + '\n');
            } else if (p.productType().equals("Electronic")) {
                Electronic e = (Electronic) p;
                str.append("Serial no.: " + e.getSerialNum() + "\n" +
                        "Warranty Period: " + e.getWarrantyPeriod() + " years\n");
            }
            else if(p.productType().equals("Computer")){
                Computer computer = (Computer) p;
                str.append("Warranty Period: " + computer.getWarrantyPeriod() + " years\n");
                str.append("Specs: ");
                if(!computer.getSpecs().isEmpty()){
                    //prints each spec
                    for(String s : computer.getSpecs()){
                        str.append(s + "  ");
                    }
                } else {
                    str.append("[Empty]");
                }

                str.append("\n");
            }
            else if(p.productType().equals("CellPhone")){
                CellPhone phone = (CellPhone) p;
                str.append("Warranty Period: " + phone.getWarrantyPeriod() + " years\n");
                str.append("IMEI: " + phone.getImei() + "\n +" +
                        "OS: " + phone.getOs());
            }
        }
        str.append(p.getDescription());

        return str.toString();
    }

    public void updateCatalogTab(Event event) {
        if (tabBrowse.isSelected() && loggedIn) {
            updateBrowseLists();
        }

    }

    public void updateSearchTab(Event event) {
    }


    private void updateBrowseLists() {
        try {
            output.writeObject("list all products");
            output.flush();
            ArrayList<Product> p = (ArrayList<Product>) input.readObject();

            output.writeObject("list categories");
            output.flush();

            ArrayList<Category> c = (ArrayList<Category>) input.readObject();

            listCategories.setItems(FXCollections.observableArrayList(c));
            listProducts.setItems(FXCollections.observableArrayList(p));

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void seeAllProducts(ActionEvent actionEvent) {
        updateBrowseLists();

    }
    /**
     * search product name and description for specified string
     */
    public void searchProduct(ActionEvent actionEvent) {
        alert = new Alert(Alert.AlertType.INFORMATION, textSearch.getText());
        String search = null;
        if (!textSearch.getText().isEmpty()) {
            search = textSearch.getText();
            try {
                output.writeObject("search");
                output.flush();
                output.writeObject(search);
                output.flush();

                ArrayList<Product> p = (ArrayList<Product>) input.readObject();
                if (!p.isEmpty()) {
                    listSearchedProducts.setItems(FXCollections.observableArrayList(p));
                } else {
                    listSearchedProducts.setItems(FXCollections.emptyObservableList()); //Clears list
                    alert = new Alert(Alert.AlertType.INFORMATION, "No match was found");
                    alert.show();
                }


            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            alert = new Alert(Alert.AlertType.ERROR, "Search box is empty");
            alert.show();
        }
    }

    public void updateOrderTab(Event event) {
        if(tabActiveOrder.isSelected()){
            try {
                output.writeObject("order management");
                output.flush();

                output.writeObject("list order products");
                output.flush();

                ArrayList<Order> orders = (ArrayList<Order>) input.readObject();
                listOrders.setItems(FXCollections.observableArrayList(orders));

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public void updatePastOrders(Event event) {
        if(tabPastOrders.isSelected()){
            try {
                output.writeObject("order management");
                output.flush();

                output.writeObject("past orders");
                output.flush();

                ArrayList o = (ArrayList) input.readObject();
                if(!o.isEmpty()){
                    listFinalizedOrderList.setItems(FXCollections.observableArrayList(o));
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private void updateOrderMessage(){
        if (loggedIn){
            try{
                output.writeObject("order management");
                output.flush();

                output.writeObject("list order products");
                output.flush();

                ArrayList<Order> orders = (ArrayList<Order>) input.readObject();
                listOrders.setItems(FXCollections.observableArrayList(orders));
            }
            catch(IOException | ClassNotFoundException e){

            }
        }

    }


    public void updateHomeTab(Event event) {
        if(tabHome.isSelected() && loggedIn){
            updateWelcomeMessage();
        }
    }
}
