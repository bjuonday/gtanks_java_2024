package amalgama.network;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    private ServerSocket socket;
    public void start() throws IOException {
        socket = new ServerSocket(5000);

        while (!socket.isClosed()) {
            new Thread(new NetworkEx(new Client(socket.accept())))
                    .start();
        }
    }
}
