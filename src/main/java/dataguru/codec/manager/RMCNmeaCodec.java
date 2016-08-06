package dataguru.codec.manager;

import dataguru.codec.message.AbstractNmeaObject;
import dataguru.codec.message.GGANmeaObject;
import dataguru.codec.message.RMCNmeaObject;
import dataguru.codec.util.DateTime;

import java.util.Date;
import java.util.List;

public class RMCNmeaCodec extends ParameterNmeaCodec {
    @Override
    public String decode(String content) {

        position=new RMCNmeaObject();
        String[] tokens=content.split(",");
        setByAnnotation(tokens);

//        position.time = Float.parseFloat(tokens[1]);
//        position.lat = Latitude2Decimal(tokens[3], tokens[4]);
//        position.lon = Longitude2Decimal(tokens[5], tokens[6]);
//        position.velocity = Float.parseFloat(tokens[7]);
//        position.dir = Float.parseFloat(tokens[8]);
       return position.toString();
    }
    @Override
    public List<String> encode(AbstractNmeaObject obj) {
        return null;
    }
}
