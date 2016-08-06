package dataguru.codec.manager;


import dataguru.codec.message.AbstractNmeaObject;
import dataguru.codec.message.GSVNmeaObject;
import dataguru.codec.message.gps.GpsSatellite;
import dataguru.codec.util.StringUtil;

import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

public class GSVNmeaCodec extends ParameterNmeaCodec {
    private static final String DELIMITER = ",";
    final private Vector<GpsSatellite> tempSatellites = new Vector<GpsSatellite>();
    private final Vector satellites;
    private short totalMessages;
    private short svs;

    private static GSVNmeaCodec codec=null;
    public GSVNmeaCodec(){
        this.satellites = new Vector();
        this.totalMessages = 0;
        this.svs = 0;
    }
    public static GSVNmeaCodec getInstance(){
        if(codec==null){
            codec = new GSVNmeaCodec();
        }
        return codec;
    }
    @Override
    public String decode(String content) {

        String[] values = StringUtil.split(content, DELIMITER);

        final short totalMessages = StringUtil.parseShort(values[1], (short) 0);
        final short cyclePos = StringUtil.parseShort(values[2], (short) 0);
        final short svs = StringUtil.parseShort(values[3], (short) 0);


        int index = 4;
        while (index + 3 < values.length) {
            short satelliteNumber = StringUtil.parseShort(values[index++], GpsSatellite.UNKNOWN);
            short elevation = StringUtil.parseShort(values[index++], GpsSatellite.UNKNOWN);
            short azimuth = StringUtil.parseShort(values[index++], GpsSatellite.UNKNOWN);
            short satelliteSnr = StringUtil.parseShort(values[index++], GpsSatellite.UNKNOWN);
            if (satelliteNumber != GpsSatellite.UNKNOWN) {
                final GpsSatellite sat = new GpsSatellite(satelliteNumber, satelliteSnr, elevation, azimuth);
                this.tempSatellites.addElement(sat);
            }
        }
        if (cyclePos == totalMessages) {// message end
            // New cycle started, copy over last cycles satellites and blank.
            GSVNmeaObject position = new GSVNmeaObject();
            position.totalMessages = totalMessages;
            position.svs = svs;
            position.satellites=tempSatellites;
            return position.toString();
        }else{
            return null;
        }
    }
//    private void copyLastCycleSatellitesAndClear() {
//        if (this.tempSatellites == null) {
//            return;
//        }
//        Enumeration e = tempSatellites.elements();
//        this.satellites.removeAllElements();
//        while (e.hasMoreElements()) {
//            this.satellites.addElement(e.nextElement());
//        }
//        this.tempSatellites.removeAllElements();
//    }
    @Override
    public List<String> encode(AbstractNmeaObject obj) {
        return null;
    }

}