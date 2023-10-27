package amalgama.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public abstract class Network {
    protected Client client;
    protected SocketChannel channel;
    protected boolean connected = true;
    protected final String DELIMETER = "end~";
    protected final int KEY = 2;
    protected int nBytes;

    public Network(Client client) {
        this.client = client;
        this.channel = this.client.getSocket().getChannel();
        try {
            this.channel.configureBlocking(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String stringifySocket() {
        return client.getSocket().getInetAddress().toString().substring(1) + ":" + this.client.getSocket().getPort();
    }

    public void send(String data) throws IOException {
        channel.write(ByteBuffer.wrap(data.getBytes()));
    }

    public void send(Type type, String... args) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(type.toString().toLowerCase());
        sb.append(";");
        sb.append(String.join(";", args));
        sb.append(DELIMETER);

        String req = sb.toString();
        channel.write(ByteBuffer.wrap(req.getBytes()));
    }

    public void disconnect() throws IOException {
        if (connected) {
            channel.close();
            connected = false;
        }
    }

    protected String decrypt(String data) {
        char[] a = data.toCharArray();
        for (int i = 0; i < a.length; i++)
            a[i] -= KEY;
        return new String(a);
    }

    protected String read() throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.clear();
        nBytes = channel.read(buf);
        if (nBytes > 0) {
            buf.flip();
            return new String(buf.array());
        }
        return "";
    }
}
