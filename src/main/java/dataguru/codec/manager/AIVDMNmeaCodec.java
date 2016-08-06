package dataguru.codec.manager;

import dataguru.codec.message.AIVDMNmeaObject;
import dataguru.codec.message.AbstractNmeaObject;

import java.util.List;

public class AIVDMNmeaCodec extends VDMNmeaCodec {
    @Override
    public String decode(String content) {
        vdmNmeaObject=new AIVDMNmeaObject();
        String[] temp = content.split(",");
        vdmNmeaObject.sentence = content;
        vdmNmeaObject.numMsgParts = Integer.parseInt(temp[1]);
        vdmNmeaObject.msgPartNum = Integer.parseInt(temp[2]);
        if (temp[3].length() > 0) {
            vdmNmeaObject.seqMsgID = Integer.parseInt(temp[3]);
        }
        vdmNmeaObject.channel = temp[4];
        vdmNmeaObject.msgData = temp[5];
        vdmNmeaObject.checksum = temp[6].substring(temp[6].indexOf("*") + 1);
        return vdmNmeaObject.toString();
    }

    @Override
    public List<String> encode(AbstractNmeaObject obj) {
        return null;
    }
}
