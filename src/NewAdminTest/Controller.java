package NewAdminTest;

import edu.ucdenver.company.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;

public class Controller {
    public Socket serverConnection;// = null;
    public ObjectOutputStream output;// = null;
    public ObjectInputStream input;// = null;
    private boolean loggedIn;
    public Alert alert;
    private String clientMessage;
    private String serverMessage;
    public ListView listProduct;
    private ObservableList<Product> observableList;
    private boolean firstBootup;

    private ArrayList<Product> localProduct;
    private Product selectedProduct;

    public Controller(){
        this.output = null;
        this.input = null;
        this.serverConnection = null;
        listProduct = new ListView<>();
        loggedIn = false;
        observableList = FXCollections.observableArrayList();
        firstBootup = true;
        localProduct = new ArrayList<>();
    }

    public void initialize(){
        if(!loggedIn){
            connectToServer();
            loginUser();
        }

        listProduct.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
            @Override
            public void changed(ObservableValue observable, Product oldValue, Product newValue) {
                if(newValue != null){
                    selectedProduct = newValue;
                } else {
                    selectedProduct = null;
                }

            }
        });



    }

    public void connectToServer() {
//connects to server
        // get rid of debugging
        String ip = "127.0.0.1"; //textfieldServer.getText();
        int port = 10001; //Integer.parseInt(textfieldPort.getText());
        alert = null;

        try {
            serverConnection = new Socket(ip, port);
            output = new ObjectOutputStream(serverConnection.getOutputStream());
            input = new ObjectInputStream(serverConnection.getInputStream());
        } catch (IOException e) {
//            alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
//            alert.show();
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
//            alert = new Alert(Alert.AlertType.CONFIRMATION, serverMessage);
//            alert.show();

        }
    }

    public void loginUser() {
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
//            alert = new Alert(Alert.AlertType.CONFIRMATION, serverMessage);
//            alert.showAndWait();
            if (serverMessage.charAt(0) == '0') {
                this.loggedIn = true;

            }
        } catch (IOException | ClassNotFoundException e) {
//            alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
//            alert.show();
        }

        if (this.loggedIn) {

            try {
                //testing grabbing arraylists
                output.writeObject("product management");
                output.flush();
                output.writeObject("list products");
                output.flush();
                //TODO (Note) okay, I tested it, this is redundant, but is absolutely necessary to get it to work
                ArrayList<Product> p = null;
                p = (ArrayList<Product>) input.readObject();
                localProduct = p;
                listProduct.setItems(FXCollections.observableArrayList(p));


            } catch (IOException | ClassNotFoundException e) {

            }
        }
        firstBootup = false;
    }



    public void addProduct(ActionEvent actionEvent) {
        Computer c = new Computer("Windows 10 Laptop", "1005", "Lenovo",
                "It's a laptop", LocalDate.of(2019,5,10),
                "CWE12598D1", 2);

        c.addSpec("8GB RAM");
        c.addSpec("600GB");
        Category c3 = new Category("Computers", "102", "All computers.");

        c.addCategory(c3);

        try {
            output.writeObject("product management");
            output.flush();

            output.writeObject("add product");
            output.flush();

            output.writeObject(c);
            output.flush();

            serverMessage = (String)input.readObject();
            if (serverMessage.charAt(0) == '0') {
                localProduct.add(c);
            }

            listProduct.setItems(FXCollections.observableArrayList(localProduct));



        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }



    public void removeProduct(ActionEvent actionEvent) {
        try {
            if(selectedProduct != null){
                output.writeObject("product management");
                output.flush();

                output.writeObject("remove product");
                output.flush();

                output.writeObject(selectedProduct);
                output.flush();

                serverMessage = (String)input.readObject();
                if (serverMessage.charAt(0) == '0') {
                    localProduct.remove(selectedProduct);
                    listProduct.setItems(FXCollections.observableArrayList(localProduct));
                }

            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void update(){}

    public void exitApp(ActionEvent actionEvent) {
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
}
