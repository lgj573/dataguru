package dataguru.codec.akka;


import java.io.InputStream;
import java.io.OutputStream;

//传给decode的数据流
public class Work {
    InputStream inputStream;
    OutputStream outputStream;

    public Work(InputStream input, OutputStream output) {
        inputStream = input;
        outputStream = output;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }
}
