package dataguru.codec.manager;


public class CodecFactory {
    public static ParameterNmeaCodec getParameterNmeaCodec(String name){

        if("GPGGA".equals(name)){
            return new GGANmeaCodec();
        }else if("GPGLL".equals(name)){
            return new GLLNmeaCodec();
        }else if("GPRMC".equals(name)){
            return new RMCNmeaCodec();
        }else if("GPGSV".equals(name)){
            return GSVNmeaCodec.getInstance();
        }else{
            return null;
        }
    }
    public static VDMNmeaCodec getVDMNmeaCodec(String name){

        if("AIVDM".equals(name)){
            return new AIVDMNmeaCodec();
        }else{
            return null;
        }
    }
}
