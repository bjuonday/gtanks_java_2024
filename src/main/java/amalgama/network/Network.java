package amalgama.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public abstract class Network {
    public Client client;
    protected SocketChannel channel;
    protected boolean connected = true;
    protected boolean loaded = false;
    protected final String DELIMETER = "end~";
    protected int KEY = 1;
    protected int nBytes;

    public Network(Client client) {
        this.client = client;
        this.channel = this.client.getSocket().getChannel();
        try {
            this.channel.configureBlocking(true);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public String stringifySocket() {
        return client.getSocket().getInetAddress().toString().substring(1) + ":" + this.client.getSocket().getPort();
    }

    private void write(String data) throws IOException {
        String req = data + DELIMETER;
        channel.write(ByteBuffer.wrap(req.getBytes()));
    }

    public void send(String data) throws IOException {
        write(data);
    }

    public void send(Type type, String... args) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(type.toString().toLowerCase());
        sb.append(";");
        sb.append(String.join(";", args));
        write(sb.toString());
    }

    public void disconnect() throws IOException {
        if (connected) {
            channel.close();
            connected = false;
        }
    }

    protected String decrypt(String data, int offset) {
        char[] array = data.toCharArray();
        for (int i = 0; i < array.length; i++)
            array[i] -= offset + KEY;
        return new String(array);
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
