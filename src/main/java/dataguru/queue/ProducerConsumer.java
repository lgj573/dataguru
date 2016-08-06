package dataguru.queue;


import java.util.concurrent.atomic.AtomicInteger;

public class ProducerConsumer<T> {
     // 队列最大容量
    private AtomicInteger maxSize;
    //当前队列长度，利用原子变量，其他线程不会读到脏数据
    private AtomicInteger size;

    private final T[] items;

    // 初始化队列
    public ProducerConsumer(int maxSize) {
        this.maxSize = new AtomicInteger(maxSize);
        this.size=new AtomicInteger(0);
        items = (T[]) new Object[maxSize];
    }

    public ProducerConsumer() {
        this(10);
    }

    // 同步加入对象，利用原子变量设置当前长度
    public synchronized boolean put(T t)  {
        if (size.get() >= maxSize.get()) return false;
        items[size.getAndIncrement()] = t;
        return true;
    }

    public synchronized T take(){
        if(size.get()<1)return null;
        return items[size.decrementAndGet()];
    }

    public int getCapacity() {
        return maxSize.get();
    }

    public int size() {
        return this.size.get();
    }
    // 测试代码
    public static void main(String[] args) throws InterruptedException {
        final ProducerConsumer<String> queue = new  ProducerConsumer<String>(50);
        Thread producer[]=new Thread[10];
        for(int i=0;i<10;i++){
            producer[i]= new Thread(new Runnable() {
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
        }
        Thread consumer[]=new Thread[10];
        for(int i=0;i<10;i++){
            consumer[i]= new Thread(new Runnable() {
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
        }
        for(int i=0;i<10;i++){
            producer[i].start();
            consumer[i].start();
        }
    }
}
