package edu.ucdenver.company;

import java.time.LocalDate;
import java.util.ArrayList;

public class TestCustomerOrders {
    public static void main(String[] args) {
        Company company = new Company("Shop");

        /* [TESTS COMPLETED]
        [x] adding users and checking name and email availability
        [x] logging into users
        [X] open customer order
        [X] add to open order
        [X] delete to order
        [X] list order w/ no repeat products
        [x] cancel order
        [X] finalize order
        [X] show finalized order list from one User
        [X] show all orders from a specified time frame
         */


//        company.addCustomer("Lora", "lora@gmail.com", "testing");
//        company.addCustomer("David", "david@gmail.com", "74dfa");
//        company.addCustomer("Lisa", "lisa@yahoo.com", "f7asdf1f");

        Home p5 = new Home("Couch", "1005", "IKEA", "A gray fabric couch.",
                LocalDate.of(2020,6,5), "Living Room");
        Book p6 = new Book("The Illustrated Man", "1006", "Penguin",
                "A paperback science fiction book.", LocalDate.of(1998,5,7), "Ray Bradbury",
                LocalDate.of(1960,01,01), 150);
        Electronic p7 = new Electronic("Teddy Bear", "1000", "Beanie Babies", "A stuffed brown bear.",
                LocalDate.of(2011,10,10), "CWHIBA", 1);

        User user = new Customer("Test", "test@customer.com", "hi");
        company.addUser(user);
        User currentUser = null;
        try {
            currentUser = (Customer) company.loginUser("test@customer.com", "hi");
            currentUser.setLoggedIn(true);
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }

        if (currentUser != null && currentUser.getLoggedIn()) {
            System.out.println("Success!");

            try{
                company.createEmptyOrder((Customer) currentUser);
            }catch(IllegalArgumentException iae){
                System.out.println(iae);
            }
            //testing to see if it allows duplicate open orders
            try{
                company.createEmptyOrder((Customer) currentUser);
            }catch(IllegalArgumentException iae){
                System.out.println(iae);
            }
            //testing add to order
            company.addProductToOrder((Customer) currentUser, p5);
            company.addProductToOrder((Customer) currentUser,p6);
            company.addProductToOrder((Customer) currentUser,p6);

            //Trying to cancel order here

            company.cancelOrder((Customer) currentUser);

            //opening new order and testing the finalized order lists
            try{
                System.out.println("Opening new Empty Order");
                company.createEmptyOrder((Customer) currentUser);
            }catch(IllegalArgumentException iae){
                System.out.println(iae);
            }

            company.addProductToOrder((Customer) currentUser, p5);
            company.addProductToOrder((Customer) currentUser,p6);
            company.addProductToOrder((Customer) currentUser,p6);

            System.out.println(company.listOrderProducts((Customer) currentUser));

            company.finalizeOrder((Customer) currentUser);
            System.out.println(company.listOrderReport((Customer) currentUser));

            try{
                System.out.println("Opening new Empty Order");
                company.createEmptyOrder((Customer) currentUser);
            }catch(IllegalArgumentException iae){
                System.out.println(iae);
            }

            company.addProductToOrder((Customer) currentUser, p5);
            company.addProductToOrder((Customer) currentUser,p6);
            company.addProductToOrder((Customer) currentUser,p6);
            company.addProductToOrder((Customer) currentUser, p7);

            System.out.println(company.listOrderProducts((Customer) currentUser));
            System.out.println("Removing : " + p6);
            company.removeProductFromOrder((Customer) currentUser, p6);

            System.out.println(company.listOrderProducts((Customer) currentUser));

            System.out.println("Removing : " + p7);
            company.removeProductFromOrder((Customer) currentUser, p7);

            company.finalizeOrder((Customer) currentUser);
            System.out.println(company.listOrderReport((Customer) currentUser));

            company.testFinalizedOrders();
            //System.out.println(company.getOrders());
            ArrayList<Order> orders = company.listOrdersByDate(LocalDate.of(2020, 1, 1),LocalDate.of(2020,4, 1));


            System.out.println("Orders finalized between Jan 1st to April 4th:\n");
            for(Order o : orders ){
                System.out.println(o);

            }


//
//            System.out.println(company.listOrderProducts(currentUser));
//            System.out.println("Removing all instances of product " + p6.getName());
//            try{
//                company.removeProductFromOrder(currentUser, p6);
//            }
//            catch(IllegalArgumentException iae){
//                System.out.println(iae);
//            }
//
//            System.out.println(company.listOrderProducts(currentUser));
//
//            System.out.println("Finalizing Order:\n");
//            try{
//                company.finalizeOrder(currentUser);
//            }
//            catch(IllegalArgumentException iae){
//                System.out.println(iae);
//            }
//            System.out.println(company.listOrderReport(currentUser));
//
//            //Testing errors
//            try{
//                company.removeProductFromOrder(currentUser,p5);
//            }
//            catch(IllegalArgumentException iae){
//                System.out.println(iae);
//            }

        }//logged in



    }
}
