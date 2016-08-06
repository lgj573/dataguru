package dataguru.codec.manager;

import dataguru.codec.annotation.SentenceField;
import dataguru.codec.message.AbstractNmeaObject;
import dataguru.codec.message.GGANmeaObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

public class GGANmeaCodec extends ParameterNmeaCodec {

    @Override
    public String decode(String content) {
        position=new GGANmeaObject();
        String[] tokens=content.split(",");
        setByAnnotation(tokens);

//        position.time = Float.parseFloat(tokens[1]);
//        position.lat = Latitude2Decimal(tokens[2], tokens[3]);
//        position.lon = Longitude2Decimal(tokens[4], tokens[5]);
//        position.quality = Integer.parseInt(tokens[6]);
//        position.altitude = Float.parseFloat(tokens[9]);
        return position.toString();
    }

    @Override
    public List<String> encode(AbstractNmeaObject obj) {
        return null;
    }
}
