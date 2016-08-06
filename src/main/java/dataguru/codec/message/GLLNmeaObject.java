package dataguru.codec.message;

import dataguru.codec.annotation.SentenceField;

/*
例：$GPGLL,4250.5589,S,14718.5084,E,092204.999,A*2D
字段0：$GPGLL，语句ID，表明该语句为Geographic Position（GLL）地理定位信息
字段1：纬度ddmm.mmmm，度分格式（前导位数不足则补0）
字段2：纬度N（北纬）或S（南纬）
字段3：经度dddmm.mmmm，度分格式（前导位数不足则补0）
字段4：经度E（东经）或W（西经）
字段5：UTC时间，hhmmss.sss格式
字段6：状态，A=定位，V=未定位
字段7：校验值
 */
public class GLLNmeaObject extends ParameterNmeaObject{
    @SentenceField(order = 1, fieldType = "latitude")
    public float lat = 0.0f;
    @SentenceField(order = 3, fieldType = "longitude")
    public float lon = 0.0f;
    @SentenceField(order = 5, fieldType = "float")
    public float time = 0.0f;

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

    public GLLNmeaObject(){
        this.objType = "GLL";
    }
}
