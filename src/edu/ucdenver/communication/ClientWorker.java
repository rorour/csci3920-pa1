package edu.ucdenver.communication;
import edu.ucdenver.company.*;

import java.io.*;
import java.net.Socket;

public class ClientWorker implements Runnable {
    //same company object used by every clientworker
    private static Company company;
    private Socket clientConnection;
    private int id;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private boolean keepRunningClient;

    public ClientWorker(Socket clientConnection, int id) {
        this.clientConnection = clientConnection;
        this.id = id;
    }

    public static void setCompany(Company company){
        ClientWorker.company = company;
    }

    private void sendMessage(String message){}

    private void processClientRequest() throws IOException{}

    private void closeClientConnection(){}

    private void displayMessage(String message){}

    @Override
    public void run() {
        //handle client interactions
        //todo make sure all necessary company functions are synchronized
        //close client connection

        try {
            //get client login info
            input = new ObjectInputStream(clientConnection.getInputStream());
            output = new ObjectOutputStream(clientConnection.getOutputStream());
            User currentUser = null;

            //send confirmation to client
            output.writeObject(new String("Connected to server"));

            //attempt login
            try {
                String email = (String)input.readObject();
                String password = (String)input.readObject();
                currentUser = company.loginUser(email, password);
            } catch (IllegalArgumentException iae){
                //user not found
                System.out.println(iae.getMessage());
            } catch (ClassNotFoundException e){
                e.printStackTrace();
            }

            //get client commands
            //check user permission
            //update company

            //sample action - add product to catalog
            Product p = null;
            try {
                p = (Product) input.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.printf("Client %d Sent Object:" + p, this.id);
            company.addProduct(p);

            //disconnect client
            clientConnection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
