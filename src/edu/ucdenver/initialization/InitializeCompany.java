package edu.ucdenver.initialization;

import edu.ucdenver.company.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;

//initialize company & write to file so Server can be initialized
public class InitializeCompany {
    static String fileName = "initialize.txt";

    public static void main(String[] args){
        Company company = new Company("LR Company");

        //create products
        LocalDate today = LocalDate.now();
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

        //create & assign categories
        Category c1 = new Category("Furniture", "100", "Home furnishings.");
        Category c2 = new Category("Electronics", "101", "All electronic items.");
        Category c3 = new Category("Computers", "102", "All computers.");
        Category c4 = new Category("Books", "103", "All books.");
        Category c5 = new Category("Paperbacks", "104", "Paperback books.");
        Category c6 = new Category("Cell Phones", "105", "Cellular phones.");
        p2.addCategory(c2);
        p3.addCategory(c2);
        p3.addCategory(c3);
        p4.addCategory(c2);
        p4.addCategory(c6);
        p5.addCategory(c1);
        p6.addCategory(c4);
        p6.addCategory(c5);

        //add categories
        company.addCategory(c1);
        company.addCategory(c2);
        company.addCategory(c3);
        company.addCategory(c4);
        company.addCategory(c5);
        company.addCategory(c6);

        //add products
        company.addProduct(p2);
        company.addProduct(p3);
        company.addProduct(p4);
        company.addProduct(p5);
        company.addProduct(p6);

        //add users
        company.addUser(new Administrator("Alice Admin", "alice@admin.com", "pw123"));
        company.addUser(new Customer("Charlie Customer", "charlie@customer.com", "456pw"));


        //temp login user
        Customer tempCustomer = (Customer) company.loginUser("charlie@customer.com", "456pw");
        //add orders
        company.createEmptyOrder(tempCustomer);
        company.addProductToOrder(tempCustomer, p2);
        company.addProductToOrder(tempCustomer, p3);
        company.finalizeOrder(tempCustomer);

        company.testFinalizedOrders(); //for testing purposes only

        //add photos
        for (Product p : company.getCatalog()){
            p.setPhoto(String.format("src/edu/ucdenver/initialization/%s.jpg", p.getId()));
        }

        //write to file
        try{
            FileOutputStream fileOut = new FileOutputStream(InitializeCompany.fileName);
            ObjectOutputStream output = new ObjectOutputStream(fileOut);
            output.writeObject(company);
            output.flush();
            System.out.printf("Wrote company to file %s\n", InitializeCompany.fileName);
            output.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
