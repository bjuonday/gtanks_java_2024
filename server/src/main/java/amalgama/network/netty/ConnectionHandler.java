package amalgama.network.netty;

import org.jboss.netty.channel.*;

public class ConnectionHandler extends SimpleChannelUpstreamHandler {
    private static ConnectionHandler instance;
    private final ClientController controller = new ClientController();
    private ConnectionHandler() { }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        controller.acceptMessage(ctx, e);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        Channel channel = ctx.getChannel();
        if (channel.isOpen())
            channel.close();
        System.out.println("[Netty] Exception: " + e.getCause().toString());
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        Channel channel = ctx.getChannel();
        // check for blacklist
        // closing if true
        controller.newConnection(ctx);
        System.out.println("[Netty] Client connected '" + channel.getRemoteAddress() + "' #" + channel.getId());
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        Channel channel = ctx.getChannel();
        controller.disconnect(ctx);
        System.out.println("[Netty] Connection closed '" + channel.getRemoteAddress() + "' #" + channel.getId());
    }

    public static ConnectionHandler getInstance() {
        if (instance == null)
            instance = new ConnectionHandler();
        return instance;
    }

    public ClientController getController() {
        return controller;
    }
}
