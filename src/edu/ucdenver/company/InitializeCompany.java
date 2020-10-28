package edu.ucdenver.company;

import java.time.LocalDate;

//initialize company & write to file so Server can be initialized
public class InitializeCompany {
    String fileName = "initialize.txt";

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

        //add categories
        p2.addCategory(c2);
        p3.addCategory(c2);
        p3.addCategory(c3);
        p4.addCategory(c2);
        p4.addCategory(c6);
        p5.addCategory(c1);
        p6.addCategory(c4);
        p6.addCategory(c5);

        //add products
        company.addProduct(p2);
        company.addProduct(p3);
        company.addProduct(p4);
        company.addProduct(p5);
        company.addProduct(p6);

        //add users
        Administrator a1 = new Administrator("Alice Admin", "alice@admin.com", "pw123");
        //Customer c1 = new Customer();
        //company.add

        //add orders?
    }
}
