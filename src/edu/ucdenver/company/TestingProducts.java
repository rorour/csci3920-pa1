package edu.ucdenver.company;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

public class TestingProducts {
    public static void main(String[] args){
        //create products

        LocalDate today = LocalDate.of(2020,10,24);
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

        Company myCompany = new Company("My Company Name");
        myCompany.addProduct(p1);
        myCompany.addProduct(p2);
        myCompany.addProduct(p3);
        myCompany.addProduct(p4);
        myCompany.addProduct(p5);
        myCompany.addProduct(p6);
        myCompany.addProduct(p7);

        System.out.println("Printing Catalog:");
        for (Product p : myCompany.getCatalog()){
            System.out.println(p);
        }

        Collections.sort(myCompany.getCatalog(), new ProductComparator());

        System.out.println("Printing SORTED Catalog:");
        for (Product p : myCompany.getCatalog()){
            System.out.println(p);
        }

    }
}
