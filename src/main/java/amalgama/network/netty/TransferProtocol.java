package amalgama.network.netty;

import amalgama.network.*;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;

public class TransferProtocol {
    private static final String DELIMETER = "end~";
    private final int KEY = 1;
    private ChannelHandlerContext context;
    private Channel channel;
    private boolean cryptActive;

    public TransferProtocol(ChannelHandlerContext ctx) {
        this.context = ctx;
        this.channel = ctx.getChannel();
    }

    public void decrypt(String data) {
        StringBuffer sb = new StringBuffer(data);
        int pos = sb.indexOf(DELIMETER);
        if (pos == -1)
            return;
        sb.setLength(pos);
        if (cryptActive) {
            String decrypted = _d_1(sb.substring(1), Character.getNumericValue(sb.charAt(0)));
            submit(decrypted);
        }
        submit(sb.toString());
    }

    private void submit(String data) {
        Handler handler;
        Command cmd = new Command(data);
        System.out.println("[Netty] Receive: " + cmd.args.length + ", " + cmd.src);

//        switch (cmd.type) {
//            case SYSTEM -> handler = new SystemHandler(this);
//            case AUTH, REGISTRATION -> handler = new AuthHandler(this);
//            case LOBBY -> handler = new LobbyHandler(this);
//            case GARAGE -> handler = new GarageHandler(this);
//            case LOBBY_CHAT -> handler = new LobbyChatHandler(this);
//            case BATTLE -> handler = new BattleHandler(this);
//            case PROFILE -> handler = new ProfileHandler(this);
//            default -> {
//                System.out.println("Unknown type: " + cmd.args[0]);
//                return;
//            }
//        }

//        handler.handle(cmd);
    }

    public void onDisconnect() {

    }

    public void close() {
        channel.close();
    }

    private String _d_1(String data, int offset) {
        char[] array = data.toCharArray();
        for (int i = 0; i < array.length; i++)
            array[i] -= (char) (offset + KEY);
        return new String(array);
    }
}
