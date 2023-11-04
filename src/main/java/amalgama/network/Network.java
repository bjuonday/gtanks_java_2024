package amalgama.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public abstract class Network {
    private static List<Network> instances = new ArrayList<>();
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
            instances.add(this);
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

    public void broadcast(String data) throws IOException {
        for (Network n : instances) {
            n.send(data);
        }
    }

    public void broadcast(Type type, String... args) throws IOException {
        for (Network n : instances) {
            n.send(type, args);
        }
    }

    public void broadcast_Lobby(String data) throws IOException {
        for (Network n : instances) {
            if (n.client.currentBattleId == null) {
                n.send(data);
            }
        }
    }

    public void broadcast_Lobby(Type type, String... args) throws IOException {
        for (Network n : instances) {
            if (n.client.currentBattleId == null) {
                n.send(type, args);
            }
        }
    }

    public int countOfInstances() {
        return instances.size();
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
