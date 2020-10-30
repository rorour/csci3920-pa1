package edu.ucdenver.communication;
import edu.ucdenver.company.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

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

        while (keepRunningClient && clientConnection != null){
            try {
                command = (String)input.readObject();
                switch (command){
                    case "create new user":
                        adminNewUser();
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
                    case "add product":
                        //sample action - add product to catalog
                        Product p = null;
                        try {
                            p = (Product) input.readObject();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        System.out.printf("Client %d Sent Object:" + p, this.id);
                        company.addProduct(p);
                        break;
                    case "terminate":
                        terminateServer = true;
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
                p = (Product)input.readObject();
                company.addProduct(p);
                break;
            case "remove product":
                p = (Product)input.readObject();
                company.removeProduct(p);
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
        }

    }

    private void adminOrderReport(){
        //todo implement this
    }


    private void customerCommands(){
        String command = null;
        boolean keepRunningClient = true;

        while (keepRunningClient && clientConnection != null){
            try {
                command = (String)input.readObject();
                switch (command){
                    //todo add customer commands
                    default:
                        sendMessage("1|Unknown command " + command);
                        break;
                }
            } catch (IOException | ClassNotFoundException e){
                sendMessage("1|Error: " + e.getMessage());
            }
        }

    }



    /**
     * reads 2 strings from client, attempts to log in
     * sends client message starting with 0 if successful, 1 if error
     * @return true/false depending on if login successful
     */
    private boolean attemptLogin(){
        try {
            String email = (String)input.readObject();
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
                loggedIn = attemptLogin();
            }

            //check user permission & get client commands
            if (currentUser.getAccessLevel().equals("admin")){
                terminateServer = adminCommands();
            } else if (currentUser.getAccessLevel().equals("customer")){
                customerCommands();
            } else {
                throw new IllegalArgumentException("Unknown user permission");
            }

            //disconnect client
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
