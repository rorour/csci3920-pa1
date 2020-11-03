package edu.ucdenver.communication;
import edu.ucdenver.company.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.Collections;

public class ClientWorker implements Runnable {
    //same company object used by every clientworker
    private static Company company;
    private Socket clientConnection;
    private int id;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private boolean keepRunningClient;
    private User currentUser;
    private ServerSocket serverSocket;
    private boolean terminateServer = false;


    public ClientWorker(Socket clientConnection, int id, ServerSocket serverSocket) {
        this.clientConnection = clientConnection;
        this.id = id;
        this.serverSocket = serverSocket;
    }

    public static void setCompany(Company company){
        ClientWorker.company = company;
    }

    public static Company getCompany() {
        return company;
    }

    private void sendMessage(String message){
        try {
            output.writeObject(new String(message + "\n"));
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void processClientRequest() throws IOException{}

    private void closeClientConnection(){}

    private void displayMessage(String message){}

    //returns whether to terminate server
    private boolean adminCommands() throws IOException, ClassNotFoundException {
        String command = null;
        boolean keepRunningClient = true;
        boolean terminateServer = false;

        while (keepRunningClient){
            try {
                command = (String)input.readObject();
                switch (command){
                    case "create new user":
                        try {
                            adminNewUser();
                            sendMessage("0|User successfully added");
                        } catch (IllegalArgumentException iae){
                            sendMessage("1|" + iae.getMessage());
                        }

                        break;
                    case "product management":
                        adminProductManagement();
                        break;
                    case "category management":
                        adminCategoryManagement();
                        break;
                    case "order report":
                        adminOrderReport();
                        break;
                    case "order report by date":
                        adminOrderReportByDate();
                        break;
                    case "terminate":
                        terminateServer = true;
                        keepRunningClient = false;
                        break;
                    case "close client":
                        keepRunningClient = false;
                        break;
                    default:
                        sendMessage("1|Unknown command " + command);
                        break;
                }
            } catch (IOException | ClassNotFoundException e){
                sendMessage("1|Error: " + e.getMessage());
            }
        }
        return terminateServer;
    }

    private void adminProductManagement() throws IOException, ClassNotFoundException {
        String command = null;
        command = (String)input.readObject();
        Product p = null;
        Category c = null;
        switch(command){
            case "add product":
                try {
                    p = (Product)input.readObject();
                    company.addProduct(p);
                    sendMessage("0|Product added successfully");
                } catch (IOException | ClassNotFoundException e) {
                    sendMessage("1|Error on server side during add");
                }
                break;
            case "remove product":
                try {
                    p = (Product)input.readObject();
                    company.removeProduct(p);
                    sendMessage("0|Product removed successfully");
                } catch (IOException | ClassNotFoundException e) {
                    sendMessage("1|Error on server side during removal");
                }
                break;
            case "add category to product":
                p = (Product)input.readObject();
                c = (Category) input.readObject();
                company.addCategoryToProduct(p, c);
                break;
            case "remove category from product":
                p = (Product)input.readObject();
                c = (Category) input.readObject();
                company.removeCategoryFromProduct(p, c);
                break;
            case "list products":
                output.writeObject(company.getCatalog());
                output.flush();
                output.reset();

                System.out.println("List products called");

                for (Product prod : company.getCatalog())
                    System.out.println(prod);
                break;
            default:
                sendMessage("1|Unknown command " + command);
                break;
        }

    }

    private void adminNewUser() throws IOException, ClassNotFoundException {
        User newUser = (User)input.readObject();
        company.addUser(newUser);
    }

    private void adminCategoryManagement() throws IOException, ClassNotFoundException {
        String command = null;
        command = (String)input.readObject();
        Category c = null;
        switch (command){
            case "add category":
                c = (Category)input.readObject();
                company.addCategory(c);
                break;
            case "set default":
                c = (Category)input.readObject();
                company.setDefaultCategory(c);
                break;
            case "remove category":
                c = (Category)input.readObject();
                company.removeCategory(c);
                break;
            case "list categories":
                output.writeObject(company.getCategories());
                output.flush();
                output.reset();
                break;

        }

    }

    private void adminOrderReport() throws IOException {
        output.writeObject(company.getOrders());
        output.flush();
        //output.reset();
        //TODO: This was the problem. Apparently it doesn't like the reset? Dunno why.
    }

    private void adminOrderReportByDate() throws IOException, ClassNotFoundException {
        LocalDate date1 = (LocalDate)input.readObject();
        LocalDate date2 = (LocalDate)input.readObject();
        output.writeObject(company.listOrdersByDate(date1, date2));
        output.flush();
        output.reset();
    }

    private void customerCommands(){
        String command = null;
        boolean keepRunningClient = true;

        while (keepRunningClient && clientConnection != null){
            try {
                command = (String)input.readObject();
                switch (command){
                    case "browse":
                        customerBrowse();
                        break;
                    case "search":
                        customerSearch();
                        break;
                    case "order management":
                        customerOrder();
                        break;
                    case "list all products":
                        output.writeObject(company.getCatalog());
                        output.flush();
                        output.reset();
                        break;
                    case "list categories":
                        output.writeObject(company.getCategories());
                        output.flush();
                        output.reset();
                        break;
                    case "close client":
                        keepRunningClient = false;
                        break;
                    default:
                        sendMessage("1|Unknown command " + command);
                        break;
                }
            } catch (IOException | ClassNotFoundException e){
                sendMessage("1|Error: " + e.getMessage());
            }
        }
    }

    private void customerBrowse() throws IOException, ClassNotFoundException {
        Category c = (Category)input.readObject();
        output.writeObject(company.browseCategory(c));
        output.flush();

    }

    private void customerSearch() throws IOException, ClassNotFoundException {
        String str = (String) input.readObject();
        output.writeObject(company.searchProducts(str));
        output.flush();
        output.reset();
    }

    private void customerOrder() throws IOException, ClassNotFoundException {
        String command = null;
            command = (String) input.readObject();
            Product p = null;
            switch (command) {
                case "create order":
                    if(!company.hasOpenOrder((Customer) currentUser)){
                        company.createEmptyOrder((Customer) currentUser);
                    }
                    break;
                case "add product to order":
                    if(!company.hasOpenOrder((Customer) currentUser)){
                        company.createEmptyOrder((Customer) currentUser);
                    }
                    p = (Product)input.readObject();
                    company.addProductToOrder((Customer) currentUser,p);
                    break;
                case "remove product from order":
                    p = (Product)input.readObject();
                    try{
                        company.removeProductFromOrder((Customer) currentUser, p);
                        sendMessage("0| Product successfully removed");
                    }
                    catch(IllegalArgumentException iae){
                        sendMessage("1|" + iae);
                    }
                    //todo: product not being removed from database even on server side, might be my method in Order class
                    break;
                case "list order products":
                    output.writeObject(company.listOrderProducts((Customer) currentUser));
                    output.flush();
                    output.reset();
                    break;
                case "finalized order":
                    try{
                        company.finalizeOrder((Customer) currentUser);
                        sendMessage("0| Order Successfully Finalized");
                    }
                    catch(IllegalArgumentException iae){
                        sendMessage("1| Error on server side during finalizing order: " + iae);

                    }

                    break;
                case "past orders":
                    output.writeObject(company.listCustomerFinalizedProducts((Customer) currentUser));
                    output.flush();
                    output.reset();
                    break;
                case "cancel order":
                    try{
                        company.cancelOrder((Customer) currentUser);
                        sendMessage("0|Order Canceled Successfully");
                    } catch(IllegalArgumentException e){
                        sendMessage("1|Error on server side during removal");
                    }
                    break;
                case "order status":
                    output.writeObject(company.hasOpenOrder((Customer) currentUser));
                    output.flush();
                    output.reset();
                default:
                    break;
            }
    }

    /**
     * reads 2 strings from client, attempts to log in
     * sends client message starting with 0 if successful, 1 if error
     * @return true/false depending on if login successful
     */
    private boolean attemptLogin(String email){
        try {
            String password = (String)input.readObject();
            this.currentUser = company.loginUser(email, password);
            sendMessage("0|Logged in as " + this.currentUser.getDisplayName());
            return true;
        } catch (IllegalArgumentException iae){
            //user not found
            sendMessage("1|" + iae.getMessage());
            System.out.println(iae.getMessage());
        } catch (ClassNotFoundException | IOException e){
            sendMessage("1|Error during login attempt");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * interacts with client - requires login, then handles commands from client
     */
    @Override
    public void run() {
        //handle client interactions
        //todo make sure all necessary company functions are synchronized
        //close client connection
        try {
            //get streams
            input = new ObjectInputStream(clientConnection.getInputStream());
            output = new ObjectOutputStream(clientConnection.getOutputStream());

            //send confirmation to client
            sendMessage("0|Connected to server");

            //attempt login
            boolean loggedIn = false;
            while (!loggedIn){

                //make sure that close command was not sent before login
                String command = (String)input.readObject();
                if (!command.equals("close client")){

                    loggedIn = attemptLogin(command);

                    //check user permission & get client commands
                    if (currentUser.getAccessLevel().equals("admin")){
                        terminateServer = adminCommands();
                    } else if (currentUser.getAccessLevel().equals("customer")){
                        customerCommands();
                    } else {
                        throw new IllegalArgumentException("Unknown user permission");
                    }

                } else
                    break;
            }

            //disconnect client
            System.out.printf("Closing connection #%d\n", this.id);
            clientConnection.close();

        } catch (IOException | IllegalArgumentException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        if (terminateServer){
            //shut down server if command was received
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
