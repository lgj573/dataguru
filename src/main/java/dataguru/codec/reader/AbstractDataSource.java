package dataguru.codec.reader;


import dataguru.codec.manager.Buffer;
import dataguru.codec.manager.CodeManager;
import dataguru.codec.message.Consumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractDataSource extends Thread {
    private List<Consumer> list = new ArrayList<Consumer>();
    private final AtomicLong linesRead = new AtomicLong();

    private CodeManager manager=new CodeManager();
    /**
     * The main read loop to use if sentences in stream
     *
     * @param stream
     *            the generic input stream to read from
     * @throws java.io.IOException
     */
    protected void readLoop(InputStream stream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));

        String line;
        Buffer buffer=new Buffer();
        while ((line = br.readLine()) != null) {
            manager.decode(line);
        }
        while (( line = manager.readLine()) != null) {
            distribute(line);
        }
    }
    public void setPacketHandler(Consumer consumer ) {
        list.add(consumer);
    }
    protected void distribute(String line){
        linesRead.incrementAndGet();
         for (Consumer consumer : list)
        {
            consumer.accept(line);
        }
    }
    public long getNumberOfLinesRead() {
        return linesRead.get();
    }
}
