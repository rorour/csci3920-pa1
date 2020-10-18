package edu.ucdenver.communication;
import edu.ucdenver.company.*;
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
}
