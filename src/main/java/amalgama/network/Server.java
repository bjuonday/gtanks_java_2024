package amalgama.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;

@Deprecated(forRemoval = true)
public class Server extends Thread {
    private ServerSocketChannel channel;
    private static final int PORT = 5000;

    public Server() {
        setName("Server_thread");
        try {
            channel = ServerSocketChannel.open();
            channel.configureBlocking(true);
            channel.socket().bind(new InetSocketAddress(PORT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Server started.");
        try {
            Socket s = null;
            while (channel.isOpen()) {
                s = channel.socket().accept();
                s.setKeepAlive(true);
                onClientConnected(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onClientConnected(Socket s) {
//        System.out.println("New connection.");
//        Thread t = new Thread(new NetworkEx(new Client(s)));
//        t.setName("Client_thread_" + s.getInetAddress());
//        t.start();
    }
}
