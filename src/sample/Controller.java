package sample;


import edu.ucdenver.company.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.LocalDate;

public class Controller {
    public TextField txtfield_loginUser;
    public TextField txtfield_loginPssd;
    public ListView lst_products;
    public ImageView imgview_product;
    public Tab tabEC;
    public Button btnLogin;
    public Label labellogged;
    private Company company;
    private User loggedInUser;

    private Image image;
    private final String noImg = "/sample/resources/noImg.png";

    //TODO: Just testing to see if Login and Images work. This is just a testing app.

    public Controller(){
        loggedInUser = null;
        company = testFillCompanyWValues();


    }

    public Company testFillCompanyWValues(){
        LocalDate today = LocalDate.of(2020,10,27);
        Product p1 = new Product("Teddy Bear", "1001", "Beanie Babies", "A stuffed brown bear.",
                today);
        Electronic p2 = new Electronic("Flat Screen TV", "1002", "Sony",
                "A 65\" flat screen television.", today, "9908213", 5);
        Computer p3 = new Computer("MacBook Pro", "1003", "Apple",
                "A laptop computer by Apple.", today, "01234134", 2);
        CellPhone p4 = new CellPhone("iPhone X", "1004", "Apple", "A smartphone.",
                today, "5637937493", 3, "0000000001010101", "iOS");
        Home p5 = new Home("Couch", "1005", "IKEA", "A gray fabric couch.", today,
                "Living Room");
        Book p6 = new Book("The Illustrated Man", "1006", "Penguin",
                "A paperback science fiction book.", today, "Ray Bradbury",
                LocalDate.of(1960,01,01), 150);
        Product p7 = new Product("Teddy Bear", "1000", "Beanie Babies", "A stuffed brown bear.",
                today);

        //create categories
        Category c1 = new Category("Furniture", "100", "Home furnishings.");
        Category c2 = new Category("Electronics", "101", "All electronic items.");
        Category c3 = new Category("Computers", "102", "All computers.");
        Category c4 = new Category("Books", "103", "All books.");
        Category c5 = new Category("Paperbacks", "104", "Paperback books.");
        Category c6 = new Category("Textbooks", "105", "All textbooks.");
        Category c7 = new Category("Furniture", "100", "Stuff");

        //adding images to product
        p1.setImageURL("teddybear", "jpg");
        p2.setImageURL("flatscreentv", "jpg");
        p3.setImageURL("MacBookPro", "jpg");


        //        add category to product
        p6.addCategory(c4);
        p6.addCategory(c5);
        p3.addCategory(c3);
        Company myCompany = new Company("My Company Name");
        myCompany.addProduct(p1);
        myCompany.addProduct(p2);
        myCompany.addProduct(p3);
        myCompany.addProduct(p4);
        myCompany.addProduct(p5);
        myCompany.addProduct(p6);
        myCompany.addProduct(p7);

        myCompany.addCustomer("John", "john@gmail.com", "testing");
        myCompany.addCustomer("Steve", "steve@gmail.com", "1234");
        myCompany.addAdmin("TheAdmin", "admin@company.com", "random");

        return myCompany;
    }

    public void initialize(){
        this.lst_products.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
            @Override
            public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {
                if(newValue != null){
                    //changes images here
                    if(!newValue.getImageURL().isEmpty()){
                        image = new Image(newValue.getImageURL());
                        imgview_product.setImage(image);
                    }
                    else{
                        imgview_product.setImage(new Image(noImg));
                    }
                    //lstEnrolledStudent.setItems(FXCollections.observableArrayList(newValue.getEnrolledStudents()));
                }
            }
        });
    }

    public void listProductUpdate(Event event) {
        if(this.tabEC.isSelected()){
            this.lst_products.setItems(FXCollections.observableArrayList(company.getCatalog()));
        }
    }

    private void cleanLogin(){
        this.txtfield_loginUser.setText("");
        this.txtfield_loginPssd.setText("");
    }


    public void loginUser(ActionEvent actionEvent) {
        try{
            this.loggedInUser = company.loginUser(this.txtfield_loginUser.getText(), this.txtfield_loginPssd.getText());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Logged in Successfully");
            alert.showAndWait();
            cleanLogin();
            labellogged.setText("Hello, " + this.loggedInUser.getDisplayName());
        }
        catch(IllegalArgumentException iae){
            Alert alert = new Alert(Alert.AlertType.ERROR, iae.getMessage());
            alert.showAndWait();
            cleanLogin();
        }


    }
}
