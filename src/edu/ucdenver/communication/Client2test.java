package edu.ucdenver.communication;

import edu.ucdenver.company.Administrator;
import edu.ucdenver.company.Electronic;
import edu.ucdenver.company.Product;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Scanner;


/**TODO: implement Client class methods and place in its own package(?)
 *
 */
public class Client2test {
    private final int serverPort;
    private final String serverIp;
    private boolean isConnected;

    private Socket serverConnection;

    public Client2test(){
        this("127.0.0.1", 10000);
    }

    public Client2test(String serverIp, int serverPort) {
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

                String clientMessage = null;
                Scanner sc = new Scanner(System.in);

                boolean loggedIn = false;
                //login
                while (!loggedIn){
                    System.out.println("Enter email: ");
                    clientMessage = sc.nextLine();
                    output.writeObject(new String(clientMessage));
                    output.flush();

                    System.out.println("Enter password: ");
                    clientMessage = sc.nextLine();
                    output.writeObject(new String(clientMessage));
                    output.flush();

                    //get confirmation from server
                    serverMessage = (String)input.readObject();
                    System.out.println("Server Message:" + serverMessage);
                    if (serverMessage.charAt(0) == '0'){
                        loggedIn = true;
                    }


                }

                System.out.println("Enter command(product management): ");
                clientMessage = sc.nextLine();
                output.writeObject(new String(clientMessage));
                output.flush();

                System.out.println("Enter command(add product): ");
                clientMessage = sc.nextLine();
                output.writeObject(new String(clientMessage));
                output.flush();

                Product p = new Electronic("Barbie Doll", "1010", "Mattel", "A Barbie doll.",
                        LocalDate.of(2020, 10,24), "023902139120", 5);
                output.writeObject(p);
                output.flush();

                clientMessage = "terminate";
                output.writeObject(new String(clientMessage));
                output.flush();

//                System.out.println("Enter command(create new user): ");
//                clientMessage = sc.nextLine();
//                output.writeObject(new String(clientMessage));
//                output.flush();
//
//                Administrator admin2 = new Administrator("Second Admin", "admin2@admin.com", "pw");
//                output.writeObject(admin2);
//                output.flush();
//
//                clientMessage = "terminate";
//                output.writeObject(new String(clientMessage));
//                output.flush();



//                while (!clientMessage.equals("exit")){
//
//                }


//            Thread.sleep(1000);

//                System.out.println("Sending>>>>\n");
//                System.out.println(p);
//
//                output.writeObject(p);
//                output.flush();

                //System.out.println("\n<<<<Waiting for response>>>>\n");

            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
//            catch (InterruptedException e) {
//                e.printStackTrace();
//            }
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
