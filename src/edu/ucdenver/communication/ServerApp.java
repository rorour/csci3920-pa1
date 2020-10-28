package edu.ucdenver.communication;
import edu.ucdenver.company.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerApp {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String fileName = "initialize.txt";
        boolean startServer = false;
        boolean companyInitialized = false;
        ObjectOutputStream output;
        ObjectInputStream input;
        Company company;

        //initialize company from file
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
                        input = new ObjectInputStream(fileIn);
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

        //start server
        Server server = new Server();
        //
        server.setCompany(company);
        server.startServer();
        //accept client connections

        //get client login info
        //get client commands
        //update company
        //disconnect client
        //write company to file
        //shut down server
    }
}
