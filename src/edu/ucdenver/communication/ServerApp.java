package edu.ucdenver.communication;
import edu.ucdenver.company.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApp {
    static private Company company = null;
    static private ServerSocket serverSocket = null;
    static private int connectionCounter = 0;
    static private ObjectOutputStream output;
    static private ObjectInputStream input;

    /**
     * initializes company, either from file or new empty Company
     */
    private static void initializeCompany(){
        Scanner sc = new Scanner(System.in);
        String fileName = "initialize.txt";
        boolean startServer = false;
        boolean companyInitialized = false;

        while (!startServer){
            System.out.printf("\nMenu: \nA) Load Data from File \nB) Start Server\n");
            String menuChoice = sc.nextLine();
            switch(menuChoice){
                case "A":
                case "a":
                    //get file name or use default
                    System.out.printf("Enter file name to load company from, or enter \"Y\" to load from " +
                            "default file \"%s\"\n", fileName);
                    String newFileName = sc.nextLine();
                    if (!newFileName.equals("Y") && !newFileName.equals("y")){
                        fileName = newFileName;
                    }
                    //read from file
                    try {
                        FileInputStream fileIn = new FileInputStream(fileName);
                        ServerApp.input = new ObjectInputStream(fileIn);
                        company = (Company)input.readObject();
                        System.out.printf("Company \"%s\" initialized from file.\n", company.getName());
                        input.close();
                    } catch (IOException ioe) {
                        System.out.printf("ERROR: Could not read from file %s\n", fileName);
                        System.out.printf("Error thrown : %s\n", ioe.getMessage());
                        break;
                    } catch (ClassNotFoundException e){
                        System.out.printf("Class not found\n");
                        e.printStackTrace();
                        break;
                    }
                    companyInitialized = true;
                    break;
                case "B":
                case "b":
                    startServer = true;
                    break;
                default:
                    System.out.println("Unknown menu choice.\n");
                    break;
            }
        }
        //check if company is initialized; initialize if necessary
        if (!companyInitialized){
            System.out.println("Company has not been initialized yet. Please name the company:");
            String companyName = sc.nextLine();
            company = new Company(companyName);
            Administrator a = new Administrator("Default Admin", "admin@admin.com", "pw");
            company.addUser(a);
            System.out.printf("Empty company \"%s\" has been created. Admin login is \"%s\" with password \"%s\"\n",
                    company.getName(), a.getEmail(), a.getPassword());
        }
    }

    /**
     * listens for new client connection.
     * @return the new connection, or null if ServerSocket closed while listening.
     */
    private static Socket waitForClientConnection() {
        System.out.println("Waiting for a connection...");
        Socket clientConnection = null;
        try {
            clientConnection = serverSocket.accept();
            System.out.printf("Connection %d accepted from %s\n", ++connectionCounter,
                    clientConnection.getInetAddress().getHostName());
        } catch (IOException e){
            System.out.println("Stopped waiting for connection.");
        }
        return clientConnection;
    }

    /**
     * write the updated company to file "savedcompany.txt".
     */
    private static void saveCompany() throws IOException {
        String fileName = "savedcompany.txt";
        FileOutputStream fileOut = new FileOutputStream(fileName);
        ServerApp.output = new ObjectOutputStream(fileOut);
        output.writeObject(company);
        output.flush();
        System.out.printf("Wrote company to file %s\n", fileName);
        output.close();
    }

    /**
     * starts server, creates threads for client connections, and shuts down once ServerSocket is closed.
     */
    private static void runServer() throws IOException {
        int port = 10001;
        int backlog = 10;
        Socket clientConnection = null;

        //ClientWorker is now in charge of company
        ClientWorker.setCompany(ServerApp.company);

        //start server
        ServerApp.serverSocket = new ServerSocket(port, backlog);
        ExecutorService executorService = Executors.newCachedThreadPool();

        //accept client connections
        while(!serverSocket.isClosed()){
            clientConnection = waitForClientConnection();
            if (clientConnection != null){
                //create new thread for client
                ClientWorker cw = new ClientWorker(clientConnection, connectionCounter, serverSocket);
                executorService.execute(cw);
            }
        }
        //serverSocket has been closed
        System.out.println("Server shutting down.");

        //write company to file
        ServerApp.company = ClientWorker.getCompany();
        ServerApp.saveCompany();

        //shut down all client connections
        executorService.shutdown();
    }

    /**
     * runs server app
     */
    public static void main(String[] args){
        System.out.println("\n========================SERVER APP========================");
        initializeCompany();
        try {
            runServer();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
