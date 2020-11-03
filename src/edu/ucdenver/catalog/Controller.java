package edu.ucdenver.catalog;

import edu.ucdenver.company.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
    public ListView listCategories;
    public ListView listProducts;
    public ImageView imageProduct;
    public Tab tabBrowse;
    public Tab tabSearch;
    public ListView listProducts1;
    public Button btnAddToCart1;
    public TextArea textareaDescription1;
    public ImageView imageProduct1;

    private ArrayList<Category> localCategory;
    private ArrayList<Product> localProduct;
    private Product selectedProduct;
    private Category selectedCategory;
    private Order selectedOrder;
    private Customer currentCustomer;
    private Alert alert;

    public Controller() {
        listProducts = new ListView<>();
        listCategories = new ListView<>();
        listFinalizedOrderList = new ListView<>();
        listOrders = new ListView<>();
    }

    public void initialize() {
        //set default visibility
        paneConnectServer.setVisible(true);
        paneConnectServer.setDisable(false);
        paneLogin.setVisible(false);
        paneLogin.setDisable(true);
        paneCatalog.setVisible(false);
        paneCatalog.setDisable(false);

        //ChangeListener, oldValue and newValue need to change to the object
        this.listProducts.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
            @Override
            public void changed(ObservableValue observable, Product oldValue, Product newValue) {
                if (newValue != null) {
                    selectedProduct = newValue; //for ordering

                    textareaDescription.setText(productDesc(newValue));

                    //change image based on selected product's image and description
                    //imageProduct.setImage(new Image("image location")); //
                    //textareaDescription.setText("newValue Product info here");
                    //Another thing is that "add to cart" button needs to know what is the new value too
                    //maybe a temporary product or string initialized globally to keep track
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
    }

    public void connectToServer(ActionEvent actionEvent) {
        //connects to server
        //TODO get rid of debug
        String ip = "127.0.0.1";//textFieldServer.getText();
        int port = 10001; //Integer.parseInt(textFieldPort.getText());
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

    public void loginUser(ActionEvent actionEvent) {
        String email = "charlie@customer.com"; //textFieldEmail.getText();
        String password = "456pw"; //passfieldPassword.getText();
        Alert alert = null;
        String servermessage;
        boolean loggedIn = false;

        try {
            output.writeObject(email);
            output.flush();
            output.writeObject(password);
            output.flush();
            servermessage = (String) input.readObject();
            alert = new Alert(Alert.AlertType.CONFIRMATION, servermessage);
            alert.show();
            if (servermessage.charAt(0) == '0') {
                loggedIn = true;
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

        }
    }

    private boolean checkOrderStatus() throws IOException {
        output.writeObject("order management");
        output.flush();

        output.writeObject("order status");
        output.flush();
        return input.readBoolean();
    }

    public void addToCart(ActionEvent actionEvent) {
        Product p = selectedProduct;
        try {
            //creates an open order in case there isn't already one
            if (!checkOrderStatus()) {
                output.writeObject("order management");
                output.flush();

                output.writeObject("create order");
                output.flush();
            }
            output.writeObject("order management");
            output.flush();

            output.writeObject("add product to order");
            output.flush();

            output.writeObject(p);
            output.flush();
            //note: getting errors, (same as before, just need to remember how to fix them lol
            ArrayList<Order> orders = (ArrayList<Order>) input.readObject();
            listOrders.setItems(FXCollections.observableArrayList(orders));

            alert = new Alert(Alert.AlertType.CONFIRMATION, p.getName() + " was added Successfully");
            alert.show();
        } catch (IOException | ClassNotFoundException e) {
            alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
            e.printStackTrace();
        }

    }

public void searchProduct(ActionEvent actionEvent){
        String search=this.textfieldSearch.getText();
        //returns results into
        //this.listProducts.setItems(FXCollections.observableArrayList("list here"));
        //if no results back, then
        //this.listProducts.setItems(FXCollections.observableArrayList("No Search Found"));
        }

public void removeFromOrder(ActionEvent actionEvent){
        //get selected product from listOrders
        //listOrders.getSelectionModel().getSelectedItem();
        //removes all products that match
        }

public void finalizeOrder(ActionEvent actionEvent){
        //takes current open order and finalizes it

        }

public void exitApplication(ActionEvent actionEvent){
        Stage stage=(Stage)this.btnExit.getScene().getWindow();
        //TODO: Close client safely in exitApplication
        stage.close();
        }

private ArrayList<Product> browseCategories(Category c){
        ArrayList<Product> p=null;
        try{
        output.writeObject("browse");
        output.flush();
        output.writeObject(c);
        output.flush();
        p=(ArrayList<Product>)input.readObject();
        return p;
        }
        catch(IOException|ClassNotFoundException e){
        e.printStackTrace();
        }
        return p;
        }

//I need to finish this.
private String productDesc(Product p){
        StringBuilder str=new StringBuilder();
        //reason book is separate is because list order
        if(p.productType().equals("Book")){
        Book b=(Book)p;
        str.append(p.getName());
        str.append(b.getAuthorName());
        str.append(b.getPublicationDate());
        str.append(b.getNumOfPages());
        }else{

        str.append(p.getBrand());
        str.append(p.getDescription());
        if(p.productType().equals("Home")){

        }
        else if(p.productType().equals("Electronic")){

        }
        else if(p.productType().equals("Computer")){

        }
        else if(p.productType().equals("CellPhone")){

        }
        }

        return str.toString();
        }

public void updateCatalog(Event event){
        }

public void updateSearchTab(Event event){
        }


        }
