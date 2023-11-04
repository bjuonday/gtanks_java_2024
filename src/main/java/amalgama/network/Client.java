package amalgama.network;

import amalgama.database.User;

import java.net.Socket;

public class Client {
    private final Socket socket;
    public User userData = null;
    public boolean authorized = false;
    public String currentBattleId = null;

    public Client(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }
}
