package dataguru.codec.akka;


import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinRouter;
import org.apache.commons.io.IOUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// 从tcp读取数据
public class TcpConnection extends UntypedActor {
    private final String host;
    private final int port;
    private final ActorRef listener;
    private final ActorRef workerRouter;

    public TcpConnection(String host, int port, ActorRef listener) {
        this.host = host;
        this.port = port;
        this.listener = listener;
        workerRouter = this.getContext().actorOf(new Props(CodecManager.class).withRouter(new RoundRobinRouter(1)),
                "workerRouter");
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Decode) {
            //NettyClient
            NettyClient client = new NettyClient();
            client.config(host, port).setChannelHandler(new SimpleChannelUpstreamHandler() {
                @Override
                public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
                        throws Exception {
                    try {
                        ChannelBuffer buf = (ChannelBuffer) e.getMessage();
                        byte[] bytes = buf.array();
                        String string = new String(bytes);
                        workerRouter.tell(new Work(IOUtils.toInputStream(string, "UTF-8"), null), getSelf());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        throw ex;
                    }
                }
            }).start();
            /**
             * old ServerSocket connection
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                try {
                    Socket connectionSocket = serverSocket.accept();
                    workerRouter.tell(new Work(connectionSocket.getInputStream(), connectionSocket.getOutputStream()), getSelf());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
             */
        } else if (message instanceof Result) {
            Result result = (Result) message;
            String resultMessage = result.getMessage();
            listener.tell(new Result(resultMessage), getSelf());
        } else {
            unhandled(message);
        }
    }
}
