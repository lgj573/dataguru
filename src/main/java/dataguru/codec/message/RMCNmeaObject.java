package dataguru.codec.message;

import dataguru.codec.annotation.SentenceField;

/*
格 式： $GPRMC,<1>,<2>,<3>,<4>,<5>,<6>,<7>,<8>,<9>,<10>,<11>,<12>*hh<CR><LF>
$GPRMC,024813.640,A,3158.4608,N,11848.3737,E,10.05,324.27,150706,,,A*50
说 明：
字段 0：$GPRMC，语句ID，表明该语句为Recommended Minimum Specific GPS/TRANSIT Data（RMC）推荐最小定位信息
          字段 1：UTC时间，hhmmss.sss格式
          字段 2：状态，A=定位，V=未定位
          字段 3：纬度ddmm.mmmm，度分格式（前导位数不足则补0）
          字段 4：纬度N（北纬）或S（南纬）
          字段 5：经度dddmm.mmmm，度分格式（前导位数不足则补0）
          字段 6：经度E（东经）或W（西经）
          字段 7：速度，节，Knots（一节也是1.852千米／小时）
          字段 8：方位角，度（二维方向指向，相当于二维罗盘）
          字段 9：UTC日期，DDMMYY格式
          字段10：磁偏角，（000 - 180）度（前导位数不足则补0）
          字段11：磁偏角方向，E=东，W=西
          字段12：模式，A=自动，D=差分，E=估测，N=数据无效（3.0协议内容）
          字段13：校验值
 */
public class RMCNmeaObject extends ParameterNmeaObject{
    @SentenceField(order = 1, fieldType = "float")
    public float time = 0.0f;
    @SentenceField(order = 3, fieldType = "latitude")
    public float lat = 0.0f;
    @SentenceField(order = 5, fieldType = "longitude")
    public float lon = 0.0f;
    @SentenceField(order = 7, fieldType = "float")
    public float velocity = 0;
    @SentenceField(order = 8, fieldType = "float")
    public float dir = 0.0f;

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public float getDir() {
        return dir;
    }

    public void setDir(float dir) {
        this.dir = dir;
    }

    public RMCNmeaObject(){
        this.objType = "RMC";
    }
}
