package edu.ucdenver.communication;
import edu.ucdenver.company.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApp {
    static private Company company = null;
    static private ServerSocket serverSocket = null;
    static private int connectionCounter = 0;
    static private ObjectOutputStream output;
    static private ObjectInputStream input;

    //make sure company gets initialized before running server
    private static void initializeCompany(){
        Scanner sc = new Scanner(System.in);
        String fileName = "initialize.txt";
        boolean startServer = false;
        boolean companyInitialized = false;

        while (!startServer){
            System.out.printf("Menu: \nA) Load Data from File \nB) Start Server\n");
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
                        System.out.printf("Company %s initialized from file.\n", company.getName());
                        input.close();
                    } catch (IOException ioe) {
                        System.out.printf("Could not read from file %s\n", fileName);
                        ioe.printStackTrace();
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
            System.out.println("Company has not been initialized yet. Please name the company: \n");
            String companyName = sc.nextLine();
            company = new Company(companyName);
        }
    }

    private static Socket waitForClientConnection() throws IOException {
        System.out.println("Waiting for a connection...");
        Socket clientConnection = serverSocket.accept();
        System.out.printf("Connection %d accepted from %s\n", ++connectionCounter,
                clientConnection.getInetAddress().getHostName());
        return clientConnection;
    }

    private static void runServer() throws IOException {
        int port = 10001;
        int backlog = 10;
        Socket clientConnection = null;

        //ClientWorker is now in charge of company
        ClientWorker.setCompany(ServerApp.company);

        //start server
        ServerApp.serverSocket = new ServerSocket(port, backlog);
        ExecutorService executorService = Executors.newCachedThreadPool();

        while(true){
            //accept client connection
            clientConnection = waitForClientConnection();

            //create new thread for client
            ClientWorker cw = new ClientWorker(clientConnection, connectionCounter);
            executorService.execute(cw);
        }
        //ServerApp.company = ClientWorker.getCompany();
        //write company to file
        //executorService.shutdown();
        //executorService.awaitTermination();
        //executorService.shutdownNow();
        //serverSocket.close();
        //shut down server
    }

    public static void main(String[] args){
        initializeCompany();
        try {
            runServer();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
