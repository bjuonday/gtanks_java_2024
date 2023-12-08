package amalgama.network.netty;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

public class PipelineFactory implements ChannelPipelineFactory {

    @Override
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();
        pipeline.addLast("decoder", (ChannelHandler) new StringDecoder());
        pipeline.addLast("encoder", (ChannelHandler) new StringEncoder());
        pipeline.addLast("handler", (ChannelHandler) new ConnectionHandler());
        return pipeline;
    }
}
