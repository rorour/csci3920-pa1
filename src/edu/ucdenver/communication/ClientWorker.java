package edu.ucdenver.communication;
import edu.ucdenver.company.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**TODO: implement CLientWorker class methods
 *
 */

public class ClientWorker implements Runnable{
    private Socket clientConnection;
    private Company company;
    private int id;
    private PrintWriter output;
    private BufferedReader input;
    private boolean keepRunningClient;

    public ClientWorker(Socket clientConnection, Company company, int id) {
        this.clientConnection = clientConnection;
        this.company = company;
        this.id = id;
    }

    private void getOutputStream(Socket clientConnection){}

    private void sendMessage(String message){}

    private void processClientRequest() throws IOException{}

    private void closeClientConnection(){}

    private void displayMessage(String message){}

    @Override
    public void run() {

    }
}
