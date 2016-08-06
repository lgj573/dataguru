package dataguru.codec.akka;


import akka.actor.*;
import dataguru.codec.message.Consumer;

import java.util.ArrayList;
import java.util.List;

// 开始一个Reader
public class TcpReader {
    String host;
    int port;
    private List<Consumer> list = new ArrayList<Consumer>();

    public TcpReader(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void setPacketHandler(Consumer consumer) {
        list.add(consumer);
    }

    public void start() {
        // Create an Akka system
        ActorSystem system = ActorSystem.create("TcpReaderSystem");

        // create the result listener, which will print the result and shutdown the system
        final ActorRef listener = system.actorOf(new Props(Listener.class), "listener");

        // create the master
        ActorRef master = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new TcpConnection(host,port, listener);
            }
        }), "master");

        // start the calculation
        master.tell(new Decode());
    }
    public   class Listener extends UntypedActor {
        public void onReceive(Object message) {
            if (message instanceof Result) {
                Result result = (Result) message;
                for (Consumer consumer : list)
                {
                    consumer.accept(result.getMessage());
                }
            } else {
                unhandled(message);
            }
        }
    }
}
