package edu.ucdenver.catalog;

import edu.ucdenver.company.Category;
import edu.ucdenver.company.Customer;
import edu.ucdenver.company.Order;
import edu.ucdenver.company.Product;
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

    public Controller(){
        listProducts = new ListView<>();
        listCategories = new ListView<>();
        listFinalizedOrderList = new ListView<>();
        listOrders = new ListView<>();
    }

    public void initialize(){
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
                if(newValue != null){
                    selectedProduct = newValue;

                    //change image based on selected product's image and description
                    //imageProduct.setImage(new Image("image location")); //
                    //textareaDescription.setText("newValue Product info here");
                    //Another thing is that "add to cart" button needs to know what is the new value too
                    //maybe a temporary product or string initialized globally to keep track
                }
            }
        });
        this.listCategories.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
            @Override
            public void changed(ObservableValue observable, Product oldValue, Product newValue) {
                if(newValue != null){
                    //listProducts.setItems(FXCollections);
                    //shows products of specified category
                    //listProducts.setItems(FXCollections.observableArrayList(newValue));
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
            if(!checkOrderStatus()){
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

            //todo: updatelist with changes

            alert = new Alert(Alert.AlertType.CONFIRMATION, p.getName() + " was added Successfully");
            alert.show();
        } catch (IOException e) {
            alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
            e.printStackTrace();

        }

        //If no existing open order, creates new one.
        //adds product to open order.
        
    }

    public void searchProduct(ActionEvent actionEvent) {
        String search = this.textfieldSearch.getText();
        //returns results into
        //this.listProducts.setItems(FXCollections.observableArrayList("list here"));
        //if no results back, then
        //this.listProducts.setItems(FXCollections.observableArrayList("No Search Found"));
    }

    public void removeFromOrder(ActionEvent actionEvent) {
        //get selected product from listOrders
        //listOrders.getSelectionModel().getSelectedItem();
        //removes all products that match
    }

    public void finalizeOrder(ActionEvent actionEvent) {
        //takes current open order and finalizes it
        
    }

    public void exitApplication(ActionEvent actionEvent) {
        Stage stage = (Stage) this.btnExit.getScene().getWindow();
        //TODO: Close client safely in exitApplication
        stage.close();
    }

    public void updateCatalog(Event event) {
    }

    public void updateSearchTab(Event event) {
    }
}
