package dataguru.codec.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public abstract class VDMNmeaObject extends AbstractNmeaObject{
//    public ActiveTargetsMap targetsMap;
    public String sentence;
    public int numMsgParts;
    public int msgPartNum;
    public int seqMsgID;
    public String channel;
    public String msgData;
    public String msgBinary;
    public String checksum;
    public int msgType;

    public String toString() {
        return String.format("VID:type:%s, msgParts:%d partNum: %d, seqMsgID: %d, channel: %s, msgData: %s, msgData: %s",objType, numMsgParts, msgPartNum, seqMsgID, channel, msgData, msgData);
    }
}
