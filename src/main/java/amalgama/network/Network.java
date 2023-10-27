package amalgama.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public abstract class Network {
    protected Client client;
    protected SocketChannel channel;
    protected boolean connected = true;
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

    public void send(String data) throws IOException {
        String req = data + DELIMETER;
        channel.write(ByteBuffer.wrap(req.getBytes()));
    }

    public void send(Type type, String... args) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(type.toString().toLowerCase());
        sb.append(";");
        sb.append(String.join(";", args));
        sb.append(DELIMETER);
        send(sb.toString());
    }

    public void disconnect() throws IOException {
        if (connected) {
            channel.close();
            connected = false;
        }
    }

    protected String decrypt(String data) {
        int key = Character.getNumericValue(data.charAt(0));
        char[] _array = new char[data.length() - 1];
        for (int i = 1; i < data.length(); i++) {
            _array[i - 1] = (char) (data.codePointAt(i) - (key + KEY));
        }
        return new String(_array);
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
