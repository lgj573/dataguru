package dataguru.codec.reader;

import java.io.IOException;
import java.io.InputStream;

import static java.util.Objects.requireNonNull;

public class StreamReader extends AbstractDataSource {
    private final InputStream stream;
    public StreamReader(InputStream fileInputStream) {
        super();
        this.stream = requireNonNull(fileInputStream);
    }
    @Override
    public void run() {
        try {
            readLoop(stream);
        } catch (IOException e) {
             e.printStackTrace();
        }
    }
}
