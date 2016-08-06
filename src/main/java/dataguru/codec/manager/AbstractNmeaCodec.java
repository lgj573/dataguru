package dataguru.codec.manager;

import dataguru.codec.message.AbstractNmeaObject;
import dataguru.codec.message.ParameterNmeaObject;

import java.util.List;

public abstract class AbstractNmeaCodec {

    public abstract String decode(String content);
    public abstract List<String> encode(AbstractNmeaObject obj);


}
