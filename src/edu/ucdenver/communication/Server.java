package edu.ucdenver.communication;
import edu.ucdenver.company.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**TODO: implement Server class methods
 *
 */
//note: this should be in its own package later
public class Server {
    private int port;
    private int backlog;
    private int connectionCounter;
    private Company company;


    public Server() {
    }

    public Server(int port, int backlog){}

    public Socket waitForClientConnection(){return null;}

    private void runServer(){}

    //todo replace this later
    public static void main(String[] args){
        try{
            //create server socket, the constructor will bind and listen
            //takes port number and backlog(size of queue that port has)
            //choose port number and backlog size; use try/catch because ServerSocket throws exception
            ServerSocket serverSocket = new ServerSocket(10001, 5);
            Socket clientConnection = null;
            ObjectOutputStream output = null;
            ObjectInputStream input = null;

            while(true) {
                try {
                    System.out.println("Waiting for a connection...");

                    //program will pause here until a connection is made.
                    //accept() returns new socket through which connection will continue
                    clientConnection = serverSocket.accept();
                    System.out.println("Connection accepted from " + clientConnection.getInetAddress().getHostName());

                    System.out.println("Getting data streams");
                    //PrintWriter turns stream into printable string
                    //autoflush parameter clears stream
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
