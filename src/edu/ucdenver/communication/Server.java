package edu.ucdenver.communication;

import edu.ucdenver.company.Company;
import edu.ucdenver.company.Product;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

//note: this should be in its own package later
public class Server {
    private int port;
    private int backlog;
    private int connectionCounter;
    ServerSocket serverSocket;
    private Company company;

    public Server() {
        this(10001, 10);
    }

    public Server(int port, int backlog){
        this.port = port;
        this.backlog = backlog;
        this.connectionCounter = 0;
    }
    public void startServer() throws IOException {
        this.serverSocket = new ServerSocket(this.port, this.backlog);

    }
    public Socket waitForClientConnection() throws IOException {
        System.out.println("Waiting for a connection...");
        Socket clientConnection = serverSocket.accept();
        System.out.printf("Connection %d accepted from %s", ++this.connectionCounter,
                clientConnection.getInetAddress().getHostName());
        return clientConnection;
    }
    public void setCompany(Company company){
        this.company = company;
    }



    public void runServer(){
        try{
            startServer();
        while(true) {
            Socket clientConnection = null;
            ObjectOutputStream output = null;
            ObjectInputStream input = null;
            try {
                clientConnection = waitForClientConnection();
                output = new ObjectOutputStream(clientConnection.getOutputStream());
                input = new ObjectInputStream(clientConnection.getInputStream());
                //sleep for 3 seconds
                Thread.sleep(3000);

                //send text to client. if autoflush were off, we would add output.flush() after.
                output.writeObject("Connected to server");
                output.flush();

                //receive text from client
//                    String clientMessage = (String)input.readObject();
//                    System.out.println("Client Message:" + clientMessage);

                Product p = (Product) input.readObject();
                System.out.println("Client Sent Object:" + p);


            } catch (InterruptedException | NullPointerException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    input.close();
                    output.close();
                    //this is separate from the server socket that client connects to and closes, so it needs to be closed as well.
                    clientConnection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }//end while loop
    }
    catch(IOException ioe){
        System.out.println("Cannot open the server.");
        ioe.printStackTrace();
    }
    }

}
