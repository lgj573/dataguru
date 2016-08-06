package dataguru.codec.manager;

import dataguru.codec.message.AbstractNmeaObject;
import dataguru.codec.message.GGANmeaObject;
import dataguru.codec.message.GLLNmeaObject;

import java.util.List;

public class GLLNmeaCodec extends ParameterNmeaCodec {
    @Override
    public String decode(String content) {
        position=new GLLNmeaObject();
        String[] tokens=content.split(",");
        setByAnnotation(tokens);
//        position.lat = Latitude2Decimal(tokens[1], tokens[2]);
//        position.lon = Longitude2Decimal(tokens[3], tokens[4]);
//        position.time = Float.parseFloat(tokens[5]);
        return position.toString();
    }

    @Override
    public List<String> encode(AbstractNmeaObject obj) {
        return null;
    }
}
