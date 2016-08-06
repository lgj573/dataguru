package dataguru.codec.reader;


import dataguru.codec.akka.*;
import dataguru.codec.message.Consumer;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ReaderTest {
    @Test
    public void fileReaderTest() throws InterruptedException, IOException {
        AbstractDataSource reader = Readers.createReaderFromFile("src/test/resources/small_example.txt");
        reader.setPacketHandler(new Consumer() {
            @Override
            public void accept(String str) {
                System.out.println(str);
            }
        });
        reader.start();
        reader.join();
        assertEquals(5, reader.getNumberOfLinesRead());
    }
    @Test
    public void tcpReaderTest() throws InterruptedException, IOException {

//        AbstractDataSource reader = Readers.createReaderFromTCP("localhost",6789);
//        reader.setPacketHandler(new Consumer() {
//            @Override
//            public void accept(String str) {
//                System.out.println(str);
//            }
//        });
//        reader.start();
//        reader.join();
//        Thread.sleep(3000);
//        reader.interrupt();
    }

    @Test
    public void akkaTcpReaderTest() throws InterruptedException, IOException {

        dataguru.codec.akka.TcpReader reader = new dataguru.codec.akka.TcpReader("localhost", 6789);
        reader.setPacketHandler(new Consumer() {
            @Override
            public void accept(String str) {
                System.out.println(str);
            }
        });
        reader.start();
    }

}
