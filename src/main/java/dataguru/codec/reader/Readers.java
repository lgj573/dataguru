package dataguru.codec.reader;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

import static java.util.Objects.requireNonNull;

public class Readers {

    public static AbstractDataSource createReaderFromTCP(String host,int port) throws IOException {
        return new TcpReader(host,port);
    }
    public static AbstractDataSource createReaderFromFile(String filename) throws IOException {
        return new StreamReader(createFileInputStream(requireNonNull(filename)));
    }
    static InputStream createFileInputStream(String filename) throws IOException {
        return new FileInputStream(filename);
    }
}
