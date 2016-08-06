package dataguru.codec.manager;

import dataguru.codec.annotation.SentenceField;
import dataguru.codec.message.AbstractNmeaObject;
import dataguru.codec.message.ParameterNmeaObject;

import java.lang.reflect.Field;
import java.util.List;

public abstract class ParameterNmeaCodec extends AbstractNmeaCodec {
    ParameterNmeaObject position;

    void setByAnnotation(String[] tokens){
        try {
            Class rt_class = position.getClass();
            Field field[] = rt_class.getDeclaredFields();
            for(Field field1:field){
                if(field1.isAnnotationPresent(SentenceField.class)){
                    Field f = rt_class.getDeclaredField(field1.getName());
                    SentenceField ann=(SentenceField)f.getAnnotation(SentenceField.class);
                    int order = ann.order();
                    String type = ann.fieldType();

                    if("float".equals(type)){
                        f.setFloat(position,Float.parseFloat(tokens[order]));
                    }else if("int".equals(type)){
                        f.setInt(position,Integer.parseInt(tokens[order]));
                    }else if("latitude".equals(type)){
                        f.setFloat(position,Latitude2Decimal(tokens[order], tokens[order+1]));
                    }else if("longitude".equals(type)){
                        f.setFloat(position,Latitude2Decimal(tokens[order], tokens[order+1]));
                    }
                }
            }
        } catch (  IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
    static float Latitude2Decimal(String lat, String NS) {
        float med = Float.parseFloat(lat.substring(2))/60.0f;
        med +=  Float.parseFloat(lat.substring(0, 2));
        if(NS.startsWith("S")) {
            med = -med;
        }
        return med;
    }
    static float Longitude2Decimal(String lon, String WE) {
        float med = Float.parseFloat(lon.substring(3))/60.0f;
        med +=  Float.parseFloat(lon.substring(0, 3));
        if(WE.startsWith("W")) {
            med = -med;
        }
        return med;
    }
}
