package dataguru.codec.zeromq;


import dataguru.codec.manager.Buffer;
import dataguru.codec.manager.CodeManager;
import dataguru.codec.message.Consumer;
import dataguru.codec.message.ParameterNmeaObject;
import dataguru.codec.message.VDMNmeaObject;
import dataguru.codec.reader.AbstractDataSource;
import org.zeromq.ZMQ;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

// 开始一个Reader
public class TcpReader extends AbstractDataSource {
    private CodeManager manager = new CodeManager();

    private List<Consumer> list = new ArrayList<Consumer>();
    ServerSocket serverSocket;

    public void setPacketHandler(Consumer consumer) {
        list.add(consumer);
    }

    public TcpReader(String host, int port) throws IOException {
        super();
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        // 通过subscriber 读取解码结果
        new Subscriber().start();
        while (true) {
            Socket connectionSocket = null;
            try {
                connectionSocket = serverSocket.accept();
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

    // NMEA数据解析
    protected void readLoop(InputStream stream) throws IOException {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));

            String line;
            ParameterNmeaObject position;
            VDMNmeaObject vdm;
            Buffer buffer = new Buffer();
            while ((line = br.readLine()) != null) {
                manager.decode(line);
            }
            // 用Zeromq 发布消息
            while ((position = manager.readPosition()) != null) {
                publish(ParameterNmeaObjectToProto(position));
            }
            while ((vdm = manager.readVDM()) != null) {
                publish(VDMNmeaObjectToProto(vdm));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 简单语句用 protocol buffer编码
    public byte[] ParameterNmeaObjectToProto(ParameterNmeaObject position){
        NmeaMessage.ParameterNmeaMessage.Builder msgBuilder = NmeaMessage.ParameterNmeaMessage.newBuilder();
        msgBuilder.setAltitude(position.getAltitude());
        msgBuilder.setDir(position.getDir());
        msgBuilder.setFixed(position.isFixed() ? 1 : 0);
        msgBuilder.setLatitude(position.getLat());
        msgBuilder.setLongitude(position.getLon());
        msgBuilder.setQuality(position.getQuality());
        msgBuilder.setTime(position.getTime());
        msgBuilder.setType(position.getObjType());
        msgBuilder.setVelocity(position.getVelocity());
        NmeaMessage.ParameterNmeaMessage message = msgBuilder.build();
        return message.toByteArray();
    }
    // 封装语句用 protocol buffer编码
    public byte[] VDMNmeaObjectToProto(VDMNmeaObject vdm){
        NmeaMessage.VDMNmeaMessage.Builder msgBuilder = NmeaMessage.VDMNmeaMessage.newBuilder();
        msgBuilder.setMsgType(vdm.msgType);
        msgBuilder.setChannel(vdm.channel);
        msgBuilder.setNumMsgParts(vdm.numMsgParts);
        msgBuilder.setSeqMsgID(vdm.seqMsgID);
        msgBuilder.setMsgData(vdm.msgData);
        msgBuilder.setMsgBinary(vdm.msgBinary);
        msgBuilder.setChecksum(vdm.checksum);
        NmeaMessage.VDMNmeaMessage message = msgBuilder.build();
        return message.toByteArray();
    }

    // NMEA数据解析之后，发布解析后的数据到zeromq
    public void publish(byte[] bytes) throws InterruptedException {
        ZMQ.Context context = ZMQ.context(1);

        //  Socket to talk to clients
        ZMQ.Socket responder = context.socket(ZMQ.REP);
        responder.bind("tcp://*:5555");

        responder.send(bytes, 0);

        responder.close();
        context.term();
    }

    // subscriber 读取解码后的数据
    class Subscriber extends Thread {

        @Override
        public void run() {
            super.run();
            ZMQ.Context context = ZMQ.context(1);
            //  Socket to talk to server

            ZMQ.Socket requester = context.socket(ZMQ.REQ);
            requester.connect("tcp://localhost:5555");
            while (!Thread.currentThread().isInterrupted()) {
                byte[] reply = requester.recv(0);
                distribute(new String(reply));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            requester.close();
            context.term();
        }
    }
}
