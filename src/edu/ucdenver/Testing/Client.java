package edu.ucdenver.Testing;
import edu.ucdenver.company.*;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;


public class Client {
    private final int serverPort;
    private final String serverIp;
    private boolean isConnected;

    private Socket serverConnection;

    public Client(){
        this("127.0.0.1", 10000);
    }

    public Client(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.isConnected = false;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void connect(){}

    private PrintWriter getOutputStream() throws IOException {return null;}

    private BufferedReader getInputStream() throws IOException{return null;}

    public void disconnect(){}

    public void getServerInitialResponse() throws IOException{};

    public String sendRequest(String request) throws IOException{return "";}

    public void displayMessage(String message){
        System.out.println(message);
    }

    public static void main(String[] args){
        //declare outside of try block so it can be referenced in finally{}
        Socket serverConnection = null;
//        PrintWriter output = null;
//        BufferedReader input = null;
        ObjectOutputStream output = null;
        ObjectInputStream input = null;

        //Socket() constructor takes IP and port of server to connect to
        try {
            serverConnection = new Socket("127.0.0.1", 10001);
            //forgetting to autoflush or flush() after sending output will cause errors
            output = new ObjectOutputStream(serverConnection.getOutputStream());
            input = new ObjectInputStream(serverConnection.getInputStream());

            //receive from server
            try {
                String serverMessage = (String)input.readObject();
                System.out.println("Server Message:" + serverMessage);

            output.writeObject(new String("charlie@customer.com"));
            output.flush();
            output.writeObject(new String("456pw"));
            output.flush();

                Thread.sleep(5000);

//                Product p = new Product("Teddy Bear", "1001", "Beanie Babies", "A stuffed brown bear.",
//                        LocalDate.of(2020, 10,24));
                System.out.println("Sending>>>>\n");
//                System.out.println(p);

//                output.writeObject(p);
                output.flush();

                //System.out.println("\n<<<<Waiting for response>>>>\n");

            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        } finally {
            try {
                input.close();
                output.close();
                serverConnection.close();

            } catch (IOException|NullPointerException e){
                e.printStackTrace();
            }
        }
    }
}
