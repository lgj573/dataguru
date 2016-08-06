package dataguru.codec.reader;

import dataguru.queue.ProducerConsumer;
import org.junit.Test;

public class QueueTest {
    @Test
    public void test(){
        final ProducerConsumer<String> queue = new  ProducerConsumer<String>(8);
        Thread producer= new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<50;i++){
                    if(queue.put("item "+i))
                        System.out.println("put item "+i+" success");
                    else
                        System.out.println("put item "+i+" false");
                }
            }
        });
        Thread consumer= new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<100;i++){
                    String string = queue.take();
                    if(string!=null)
                        System.out.println("take : "+string);
                    else
                        System.out.println("take fail");
                }
            }
        });
        producer.start();
        consumer.start();
    }
}
