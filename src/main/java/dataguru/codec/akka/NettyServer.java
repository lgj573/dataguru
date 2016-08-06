package dataguru.codec.akka;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;

// Netty服务器，把VDM发给客户端
public class NettyServer {
    final static int port = 8080;
    // 把所有连接加入到这个组，信息统一发送到这个组
    public static ChannelGroup allConnected = new DefaultChannelGroup("all-connected");
    public static void main(String[] args) throws InterruptedException {
        NettyServer server = new NettyServer();
        server.config(port);
        server.start();
        while(true){
            // 发送信息到客户端
            ChannelBuffer cb = ChannelBuffers.wrappedBuffer("!AIVDM,1,1,,B,402=481uaUcf;OQ55JS9ITi025Jp,0*2B\r\n".getBytes(Charset.forName("UTF-8")));
            allConnected.write(cb);
            Thread.sleep(2000);
        }
    }

    ServerBootstrap bootstrap;
    Channel parentChannel;
    InetSocketAddress localAddress;
    MyChannelHandler channelHandler = new MyChannelHandler();

    // 初始化服务器
    NettyServer() {
        bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        bootstrap.setOption("reuseAddress", true);
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.soLinger", 2);
        bootstrap.getPipeline().addLast("servercnfactory", channelHandler);
    }

    void config(int port) {
        this.localAddress = new InetSocketAddress(port);
    }

    // 启动服务器
    void start() {
        parentChannel = bootstrap.bind(localAddress);
    }

    // 自定义channel handler
    class MyChannelHandler extends SimpleChannelHandler {

        //断开连接 移除channel
        @Override
        public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
                throws Exception {
            System.out.println("Channel closed " + e);
            allConnected.remove(e.getChannel());
        }
        //建立连接 加入channel
        @Override
        public void channelConnected(ChannelHandlerContext ctx,
                                     ChannelStateEvent e) throws Exception {
            System.out.println("Channel connected " + e);
            allConnected.add(e.getChannel());
        }

        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
                throws Exception {
            try {
                System.out.println("New message " + e.toString() + " from " + ctx.getChannel());
                processMessage(e);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }

        private void processMessage(MessageEvent e) {
            Channel ch = e.getChannel();
            ch.write(e.getMessage());
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
//            super.exceptionCaught(ctx, e);
            System.out.println("== exceptionCaught");
        }
    }
}
