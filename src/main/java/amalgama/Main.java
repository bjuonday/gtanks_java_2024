package amalgama;

import amalgama.database.Connector;
import amalgama.network.Server;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        Connector.checkConnection();
        Server server = new Server();
        server.start();
    }
}