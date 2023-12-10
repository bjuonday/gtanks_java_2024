package amalgama.network.netty;

import amalgama.Global;
import amalgama.lobby.GarageManager;
import amalgama.network.*;
import amalgama.network.secure.ViolRegService;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;

public class TransferProtocol {
    private static final String DELIMITER = "end~";
    private final int KEY = 1;
    private ChannelHandlerContext context;
    private Channel channel;
    private SystemHandler systemHandler;
    private LobbyHandler lobbyHandler;
    private GarageHandler garageHandler;
    private AuthHandler authHandler;
    private LobbyChatHandler lobbyChatHandler;
    private BattleHandler battleHandler;
    private ProfileHandler profileHandler;
    public final ViolRegService vrs;
    public Client client;

    public TransferProtocol(ChannelHandlerContext ctx) {
        this.context = ctx;
        this.channel = ctx.getChannel();
        this.client = new Client();
        this.vrs = new ViolRegService(this);
        this.systemHandler = new SystemHandler(this);
        this.lobbyHandler = new LobbyHandler(this);
        this.garageHandler = new GarageHandler(this);
        this.authHandler = new AuthHandler(this);
        this.lobbyChatHandler = new LobbyChatHandler(this);
        this.battleHandler = new BattleHandler(this);
        this.profileHandler = new ProfileHandler(this);
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
        Command cmd = new Command(data);
        System.out.println("[Netty] Receive: " + cmd.args.length + ", " + cmd.src);
        if (cmd.args.length <= 1)
            return;

        switch (cmd.type) {
            case SYSTEM -> systemHandler.handle(cmd);
            case AUTH, REGISTRATION -> authHandler.handle(cmd);
            case LOBBY -> lobbyHandler.handle(cmd);
            case GARAGE -> garageHandler.handle(cmd);
            case LOBBY_CHAT -> lobbyChatHandler.handle(cmd);
            case BATTLE -> battleHandler.handle(cmd);
            case PROFILE -> profileHandler.handle(cmd);
            default -> {
                System.out.println("[Netty] Unknown type: " + cmd.args[0]);
            }
        }
    }

    public void onDisconnect() {
        Global.clients.remove(client.userData.getLogin());
    }

    public void close() {
        onDisconnect();
        channel.close();
    }

    public void send(Type type, String... args) {
        StringBuffer sb = new StringBuffer();
        sb.append(type.toString().toLowerCase());
        sb.append(";");
        sb.append(String.join(";", args));
        write(sb.toString());
    }

    public static void broadcast(String id, Type type, String... args) {
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
