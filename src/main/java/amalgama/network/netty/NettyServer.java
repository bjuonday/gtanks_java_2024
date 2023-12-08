package amalgama.network.netty;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class NettyServer {
    private final ServerBootstrap networkServer;
    private final String address;
    private final int port;

    public NettyServer(String address, int port) {
        this.address = address;
        this.port = port;
        ExecutorService bossExec = new OrderedMemoryAwareThreadPoolExecutor(1, 400000000, 2000000000, 60, TimeUnit.SECONDS);
        ExecutorService ioExec = new OrderedMemoryAwareThreadPoolExecutor(4, 400000000, 2000000000, 60, TimeUnit.SECONDS);
        networkServer = new ServerBootstrap(new NioServerSocketChannelFactory(bossExec, ioExec, 4));
        networkServer.setOption("backlog", 500);
        networkServer.setOption("child.tcpNpDelay", true);
        networkServer.setOption("child.keepAlive", true);
        networkServer.setPipelineFactory(new PipelineFactory());
    }

    public void start() {
        Channel channel = networkServer.bind(new InetSocketAddress(address, port));
        System.out.println("[Netty] Server available.");
    }

    public void stop() {
        networkServer.releaseExternalResources();
    }
}
