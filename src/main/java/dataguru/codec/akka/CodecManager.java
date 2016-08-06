package dataguru.codec.akka;

import akka.actor.UntypedActor;
import dataguru.codec.manager.Buffer;
import dataguru.codec.manager.CodeManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

// decode 消息
public class CodecManager extends UntypedActor {
    private CodeManager manager=new CodeManager();

    public String decode(InputStream stream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));

        String line;
        Buffer buffer=new Buffer();
        while ((line = br.readLine()) != null) {
            manager.decode(line);
        }
        return manager.readAll();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Work) {
            Work work = (Work) message;
            InputStream is = work.getInputStream();
            String result = decode(is);
            getSender().tell(new Result(result), getSelf());
        } else {
            unhandled(message);
        }
    }

}
