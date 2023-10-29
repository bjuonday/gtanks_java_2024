package amalgama.network;

import java.net.Socket;

public class Client {
    private final Socket socket;
    public boolean authorized = false;

    public Client(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }
}
