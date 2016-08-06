package dataguru.codec.message;

import dataguru.codec.message.gps.GpsSatellite;

import java.util.Vector;

/**
 * <h2>$GPGSV</h2>
 * <p>
 * GPS Satellites in view
 *
 * <pre>
 *      eg. $GPGSV,3,1,11,03,03,111,00,04,15,270,00,06,01,010,00,13,06,292,00*74
 *      $GPGSV,3,2,11,14,25,170,00,16,57,208,39,18,67,296,40,19,40,246,00*74
 *      $GPGSV,3,3,11,22,42,067,42,24,14,311,43,27,05,244,00,,,,*4D
 * <br>
 *      $GPGSV,1,1,13,02,02,213,,03,-3,000,,11,00,121,,14,13,172,05*67
 * <br>
 *      1    = Total number of messages of this type in this cycle
 *      2    = Message number
 *      3    = Total number of SVs in view
 *      4    = SV PRN number
 *      5    = Elevation in degrees, 90 maximum
 *      6    = Azimuth, degrees from true north, 000 to 359
 *      7    = SNR, 00-99 dB (null when not tracking)
 *      8-11 = Information about second SV, same as field 4-7
 *      12-15= Information about third SV, same as field 4-7
 *      16-19= Information about fourth SV, same as field 4-7
 * </pre>
 *
 * <hr size=1>
 *
 * <u>Satellite information</u><br/>
 * $GPGSV,2,1,08,01,40,083,46,02,17,308,41,12,07,344,39,14,22,228,45*75<br/>
 * <br/> Where: GSV Satellites in view 2 Number of sentences for full gps 1
 * sentence 1 of 2 08 Number of satellites in view 01 Satellite PRN number
 * 40 Elevation, degrees 083 Azimuth, degrees 46 SNR - higher is better for
 * up to 4 satellites per sentence *75 the checksum gps, always begins with *
 */
public class GSVNmeaObject extends AbstractNmeaObject {
    public int totalMessages = 0;
    public int svs = 0;
    public Vector<GpsSatellite> satellites;
    public GSVNmeaObject() {
        this.objType = "GSV";
        this.satellites = new Vector<GpsSatellite>();
    }
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for(GpsSatellite s: satellites){
            sb.append(String.format(" number %d, snr %d,elevation %d,azimuth %d",s.getNumber(),s.getSnr(),s.getElevation(),s.getAzimuth()));
        }
        return String.format("POSITION:type:%s totalMessages: %d, svs: %d,%s  ",objType, totalMessages, svs,sb.toString() );
    }
}
