package amalgama.network.netty;

import amalgama.network.*;
import amalgama.network.secure.ViolRegService;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;

public class TransferProtocol {
    private static final String DELIMITER = "end~";
    private final int KEY = 1;
    private ChannelHandlerContext context;
    private Channel channel;
    public final ViolRegService vrs;
    public Client client;

    public TransferProtocol(ChannelHandlerContext ctx) {
        this.context = ctx;
        this.channel = ctx.getChannel();
        this.client = new Client();
        this.vrs = new ViolRegService(this);
    }

    public void decrypt(String data) {
        StringBuffer sb = new StringBuffer(data);
        int pos = sb.indexOf(DELIMITER);
        if (pos == -1)
            return;
        sb.setLength(pos);
        if (!client.encrypted) {
            String decrypted = _d_1(sb.substring(1), Character.getNumericValue(sb.charAt(0)));
            submit(decrypted);
            return;
        }
        submit(sb.toString());
    }

    private void submit(String data) {
        Handler handler;
        Command cmd = new Command(data);
        System.out.println("[Netty] Receive: " + cmd.args.length + ", " + cmd.src);

        switch (cmd.type) {
            case SYSTEM -> handler = new SystemHandler(this);
            case AUTH, REGISTRATION -> handler = new AuthHandler(this);
            case LOBBY -> handler = new LobbyHandler(this);
            case GARAGE -> handler = new GarageHandler(this);
            case LOBBY_CHAT -> handler = new LobbyChatHandler(this);
            case BATTLE -> handler = new BattleHandler(this);
            case PROFILE -> handler = new ProfileHandler(this);
            default -> {
                System.out.println("[Netty] Unknown type: " + cmd.args[0]);
                return;
            }
        }

        handler.handle(cmd);
    }

    public void onDisconnect() {

    }

    public void close() {
        channel.close();
    }

    public void send(Type type, String... args) {
        StringBuffer sb = new StringBuffer();
        sb.append(type.toString().toLowerCase());
        sb.append(";");
        sb.append(String.join(";", args));
        write(sb.toString());
    }

    public void broadcast(String id, Type type, String... args) {
        var connections = ConnectionHandler.getInstance().getController().getConnections();
        if (id.equalsIgnoreCase("lobby")) {
            for (var connection : connections) {
                if (connection == null) continue;
                if (connection.client.currentBattleId == null)
                    connection.send(type, args);
            }
        }
        else {
            for (var connection : connections) {
                if (connection == null) continue;
                if (connection.client.currentBattleId.equals(id))
                    connection.send(type, args);
            }
        }
    }

    private void write(String data) {
        StringBuffer sb = new StringBuffer(data);
        sb.append(DELIMITER);
        channel.write(sb.toString());
    }

    private String _d_1(String data, int offset) {
        char[] array = data.toCharArray();
        for (int i = 0; i < array.length; i++)
            array[i] -= (char) (offset + KEY);
        return new String(array);
    }
}
