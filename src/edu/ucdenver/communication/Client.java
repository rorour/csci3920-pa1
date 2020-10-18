package edu.ucdenver.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**TODO: implement Client class methods and place in its own package(?)
 *
 */
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
}
