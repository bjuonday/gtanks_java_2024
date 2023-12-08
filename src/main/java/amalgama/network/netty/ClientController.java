package amalgama.network.netty;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.HashMap;

public class ClientController implements Runnable {
    private Thread t = new Thread(this);
    private HashMap<ChannelHandlerContext, TransferProtocol> connections;

    public ClientController() {
        this.t.setName("thread_NettyController");
        this.t.start();
    }

    public void newConnection(ChannelHandlerContext ctx) {
        connections.put(ctx, new TransferProtocol(ctx));
    }

    public void disconnect(ChannelHandlerContext ctx) {
        if (connections.containsKey(ctx)) {
            connections.get(ctx).onDisconnect();
            connections.remove(ctx);
        }
    }

    public void acceptMessage(ChannelHandlerContext ctx, MessageEvent e) {
        if (connections.containsKey(ctx)) {
            connections.get(ctx).decrypt((String)e.getMessage());
        }
    }

    @Override
    public void run() { }
}
