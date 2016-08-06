package dataguru.codec.akka;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

//客户端读取消息
public class NettyClient {
    public String host = "127.0.0.1";
    public int port = 8080;

    public static void main(String[] args) {
        NettyClient client = new NettyClient();
        client.config("127.0.0.1", 8080).start();
    }

    ClientBootstrap bootstrap;
    // 使用默认的channelHandler
    ChannelHandler myHandler = new MyClientHandler();


    // 初始化客户端
    public NettyClient() {
        bootstrap = new ClientBootstrap(
                new NioClientSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(myHandler);
            }
        });
    }

    NettyClient config(String host, int port) {
        this.host = host;
        this.port = port;
        bootstrap.setOption("remoteAddress", new InetSocketAddress(this.host, this.port));
        return this;
    }
    // 设置 自定义的channelHandler
    public NettyClient setChannelHandler(ChannelHandler channelHandler){
        this.myHandler = channelHandler;
        return this;
    }

    // 建立连接
    void start() {
        bootstrap.connect();
    }

    class MyClientHandler extends SimpleChannelUpstreamHandler {
        @Override
        public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
                throws Exception {
            System.out.println("Client Channel closed " + e);
        }

        @Override
        public void channelConnected(ChannelHandlerContext ctx,
                                     ChannelStateEvent e) throws Exception {
            System.out.println("Client Channel connected " + e);
        }

        // 读取服务器消息
        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
                throws Exception {
            try {
                ChannelBuffer buf = (ChannelBuffer) e.getMessage();
                byte[] bytes = buf.array();
                System.out.println("Client reseived message : " + new String(bytes));
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }
    }
}