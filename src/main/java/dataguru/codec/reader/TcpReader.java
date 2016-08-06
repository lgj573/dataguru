package dataguru.codec.reader;


import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;

// 从TCP读取数据
public class TcpReader extends AbstractDataSource {
    ServerSocket serverSocket;

    @Before
    public void initialize()
    {
        IMocksControl control = EasyMock.createControl();
        java.sql.Connection mockConnection = control.createMock(Connection.class);
    }

    public TcpReader(String host, int port) throws IOException {
        super();
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        while (true) {
            Socket connectionSocket = null;
            try {
                connectionSocket = serverSocket.accept();

//                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
//                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

                // handle incoming connection from connectionSocket

                // 当取到新数据，开始一个新线程去处理
                new TCPHandler(connectionSocket.getInputStream(), connectionSocket.getOutputStream()).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class TCPHandler extends Thread {
        InputStream inputStream;
        OutputStream outputStream;

        public TCPHandler(InputStream input, OutputStream output) {
            inputStream = input;
            outputStream = output;
        }

        // 调用通用的codec 处理程序
        @Override
        public void run() {
            super.run();
            try {
                readLoop(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
