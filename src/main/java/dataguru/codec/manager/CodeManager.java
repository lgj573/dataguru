package dataguru.codec.manager;

import dataguru.codec.message.ParameterNmeaObject;
import dataguru.codec.message.VDMNmeaObject;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CodeManager {

    private ArrayList<String> buffer = new ArrayList<String>();
    private ArrayList<ParameterNmeaObject> positions = new ArrayList<ParameterNmeaObject>();
    private ArrayList<VDMNmeaObject> vdms = new ArrayList<VDMNmeaObject>();
    public void decode(String content ){


        if(content.startsWith("$")) {
            String nmea = content.substring(1);
            String[] tokens = nmea.split(",");
            String type = tokens[0];
            //TODO check crc
            ParameterNmeaCodec codec = CodecFactory.getParameterNmeaCodec(type);
            if(codec!=null) {
                positions.add(codec.position);
                buffer.add(codec.decode(content));
            }
        }else if(content.startsWith("!")) {
            String nmea = content.substring(1);
            String[] tokens = nmea.split(",");
            String type = tokens[0];
            //TODO check crc
            VDMNmeaCodec codec=CodecFactory.getVDMNmeaCodec(type);
            if(codec!=null) {
                vdms.add(codec.vdmNmeaObject);
                buffer.add(codec.decode(content));
            }
        }
    }
    public List<String> encode(String content){
        return new ArrayList<String>();
    }

    public String readAll() {
        return StringUtils.join(buffer,",");
    }
    public String readLine() {
        if(buffer.size()>0){
           String ret = buffer.get(buffer.size()-1);
            buffer.remove(buffer.size()-1);
            return ret;
        }
        return null;
    }
    public ParameterNmeaObject readPosition() {
        if(positions.size()>0){
            ParameterNmeaObject ret = positions.get(positions.size()-1);
            positions.remove(positions.size()-1);
            return ret;
        }
        return null;
    }
    public VDMNmeaObject readVDM() {
        if(vdms.size()>0){
            VDMNmeaObject ret = vdms.get(vdms.size()-1);
            vdms.remove(vdms.size()-1);
            return ret;
        }
        return null;
    }
}
