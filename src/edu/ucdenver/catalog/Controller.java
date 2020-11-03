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

//TODO this controller also needs to safely exit out of application
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
    public Button btnSeeAll;
    public ListView listSearchedProducts;
    public TextField textSearch;
    public Tab tabActiveOrder;
    public Tab tabPastOrders;
    public Button btnCancel;
    public ListView listFinalizedProducts;
    public TextArea textAreafinalProducts;


    private ArrayList<Category> localCategories;
    private ArrayList<Product> localProducts;
    private Product selectedProduct;
    private Category selectedCategory;
    private Order selectedOrder;
    private Customer currentCustomer;
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

//    public void shutdown(){
//        System.out.println("Shutting down Admin App.");
//        if (this.serverConnection != null){
//            try {
//                output.writeObject("close client");
//                input.close();
//                output.close();
//                serverConnection.close();
//                System.out.println("Connection to server closed.");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

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


        //ChangeListener, oldValue and newValue need to change to the object
        this.listProducts.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
            @Override
            public void changed(ObservableValue observable, Product oldValue, Product newValue) {
                if (newValue != null) {
                    selectedProduct = newValue; //for ordering

                    textareaDescription.setText(productDesc(newValue));
                    //imageProduct.setImage(newValue.getImage()); //make an if/else statement based if there is a picture or not
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
                    //imageProduct1.setImage(newValue.getImage()); //make an if/else statement based if there is a picture or not
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
                    //imageOrder.setImage(newValue.getImage());
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

    //=======================================================
    // Customer Login
    //========================================================*/

    public void loginUser(ActionEvent actionEvent) throws IOException {
        String email = "charlie@customer.com"; //textfieldEmail.getText();
        String password = "456pw"; //passfieldPassword.getText();
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

        }
    }

    //=======================================================
    // Browse and Search
    //========================================================*/
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


        //If no existing open order, creates new one.
        //adds product to open order.

    }
    //TODO Fix: The server/Order class is the problem. Not this implementation.
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
            //listOrders.setItems(FXCollections.observableArrayList());
//            output.writeObject("order management");
//            output.flush();
//
//            output.writeObject("list order products");
//            output.flush();
//
//            ArrayList<Order> orders = (ArrayList<Order>) input.readObject();
//            listOrders.setItems(FXCollections.observableArrayList(orders));
            alert = new Alert(Alert.AlertType.CONFIRMATION, serverMessage);
            alert.showAndWait();
        } catch (IOException | ClassNotFoundException e) {
            alert = new Alert(Alert.AlertType.ERROR, serverMessage);
        }
    }

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

    public void exitApplication(ActionEvent actionEvent) {
        Stage stage = (Stage) this.btnExit.getScene().getWindow();
        //TODO: Close client safely in exitApplication
        stage.close();
    }

    //=======================================================
    // GUI Methods
    //========================================================*/

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
