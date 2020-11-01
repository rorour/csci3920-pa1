package edu.ucdenver.company;

import javafx.scene.control.Cell;

import java.io.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

public class TestingProducts {
    public static void main(String[] args){
        //create products

        LocalDate today = LocalDate.of(2020,10,24);
//        Product p1 = new Product("Teddy Bear", "1001", "Beanie Babies", "A stuffed brown bear.",
//                today);
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
//        Product p7 = new Product("Teddy Bear", "1000", "Beanie Babies", "A stuffed brown bear.",
//                today);
        Book p8 = new Book("The Illustrated Man", "1006", "Pearson",
                "A paperback science fiction book.", today, "R. Bradbury",
                LocalDate.of(1960,01,01), 150);

        //create categories
        Category c1 = new Category("Furniture", "100", "Home furnishings.");
        Category c2 = new Category("Electronics", "101", "All electronic items.");
        Category c3 = new Category("Computers", "102", "All computers.");
        Category c4 = new Category("Books", "103", "All books.");
        Category c5 = new Category("Paperbacks", "104", "Paperback books.");
        Category c6 = new Category("Textbooks", "105", "All textbooks.");
        Category c7 = new Category("Furniture", "100", "Stuff");

        //add category to product
        p6.addCategory(c4);
        p6.addCategory(c5);
        p3.addCategory(c3);
        Company myCompany = new Company("My Company Name");
//        myCompany.addProduct(p1);
        myCompany.addProduct(p2);
        myCompany.addProduct(p3);
        myCompany.addProduct(p4);
        myCompany.addProduct(p5);
        myCompany.addProduct(p6);
//        myCompany.addProduct(p7);
        //myCompany.addProduct(p8);




//        System.out.println("Printing Catalog:");
//        for (Product p : myCompany.getCatalog()){
//            System.out.println(p);
//        }
//
//        Collections.sort(myCompany.getCatalog(), new ProductComparator());
//
//        System.out.println("Printing SORTED Catalog:");
//        for (Product p : myCompany.getCatalog()){
//            System.out.println(p);
//        }
//
//        System.out.println("Removing product from catalog:");
//        myCompany.removeProduct(new Product("iPhone X", "1004", "idk", "", today));
//        for (Product p : myCompany.getCatalog()){
//            System.out.println(p);
//        }

//        System.out.println("Checking if products are equal:");
//        boolean e;
//        e = p1.equals(p2);
//        System.out.println("Expected 0, result " + e);
//        e = p5.equals(p6);
//        System.out.println("Expected 0, result " + e);
//        e = p6.equals(p8);
//        System.out.println("Expected 1, result " + e);
//        e = p3.equals(p3);
//        System.out.println("Expected 1, result " + e);

//        //create categories
//        Category c1 = new Category("Furniture", "100", "Home furnishings.");
//        Category c2 = new Category("Electronics", "101", "All electronic items.");
//        Category c3 = new Category("Computers", "102", "All computers.");
//        Category c4 = new Category("Books", "103", "All books.");
//        Category c5 = new Category("Paperbacks", "104", "Paperback books.");
//        Category c6 = new Category("Textbooks", "105", "All textbooks.");
//        Category c7 = new Category("Furniture", "100", "Stuff");

        //add categories to company
        myCompany.addCategory(c1);
        myCompany.addCategory(c2);
        myCompany.addCategory(c3);
        myCompany.addCategory(c4);
        myCompany.addCategory(c5);
        myCompany.addCategory(c6);
        myCompany.addCategory(c7);

//        System.out.println("Printing Categories:");
//        for (Category c : myCompany.getCategories()){
//            System.out.println(c);
//        }
//
//        Collections.sort(myCompany.getCategories(), new CategoryComparator());
//
//        System.out.println("Printing SORTED Categories:");
//        for (Category c : myCompany.getCategories()){
//            System.out.println(c);
//        }
//
//        System.out.println("Checking if categories are equal:");
//        boolean e;
//        e = c1.equals(c2);
//        System.out.println("Expected 0, result " + e);
//        e = c5.equals(c6);
//        System.out.println("Expected 0, result " + e);
//        e = c1.equals(c7);
//        System.out.println("Expected 1, result " + e);
//        e = c3.equals(c3);
//        System.out.println("Expected 1, result " + e);

//        add category to product
        p6.addCategory(c4);
        p6.addCategory(c5);

//        //print products in category 4
//        System.out.println("Products in category 4:");
//        System.out.println(myCompany.browseCategory(c4));
//
//        //print products with no assigned categories
//        System.out.println("Products with no category");
//        for (Product p : myCompany.getCatalog()){
//            if (p.getCategories().size() == 0){
//                System.out.println(p);
//            }
//        }

        //print all categories
        for (Category c : myCompany.getCategories()){
            System.out.printf("%s:\n", c.getName());
            System.out.println(myCompany.browseCategory(c));
        }

        myCompany.removeCategory(c4);
        myCompany.removeCategory(c3);
        System.out.println("After removing Computers & Books");
        for (Category c : myCompany.getCategories()){
            System.out.printf("%s:\n", c.getName());
            System.out.println(myCompany.browseCategory(c));
        }


        //testing reading/writing objects to/from file

        try {
            FileOutputStream fileOut = new FileOutputStream("myfile.txt");
            ObjectOutputStream output = new ObjectOutputStream(fileOut);
            ObjectInputStream input = null;
            output.writeObject(p4);
            output.flush();
            System.out.println("Wrote object to file.");
            output.close();
            FileInputStream fileIn = new FileInputStream("myfile.txt");
            input = new ObjectInputStream(fileIn);
            CellPhone newPhone = (CellPhone)input.readObject();
            System.out.println("Read from file" + newPhone);
            input.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
