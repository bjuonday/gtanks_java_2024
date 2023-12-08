package amalgama;

import amalgama.database.Connector;
import amalgama.network.netty.NettyServer;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        ConfigLoader.loadRanks();
        ConfigLoader.loadStartup();
        Connector.checkConnection();
        NettyServer server = new NettyServer(5000);
        server.start();
    }
}