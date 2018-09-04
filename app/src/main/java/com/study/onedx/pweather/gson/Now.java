package com.study.onedx.pweather.gson;

import com.google.gson.annotations.SerializedName;

public class Now {
    //天气状况描述
    @SerializedName("cond_txt")
    public String Condition;

    //体感温度，默认单位：摄氏度
    @SerializedName("fl")
    public String fl;

    //相对湿度
    @SerializedName("hum")
    public String humidity;

    //降水量
    @SerializedName("pcpn")
    public String Precipitation;

    //大气压强
    @SerializedName("pres")
    public String atmosphericPressure;

    //温度
    @SerializedName("tmp")
    public String temperature;

    //能见度，单位：公里
    @SerializedName("vis")
    public String visibility;

    //风向360角度
    @SerializedName("wind_deg")
    public String windAngle;

    //风向
    @SerializedName("wind_dir")
    public String windDir;

    //风力
    @SerializedName("wind_sc")
    public String windPower;

    //风速，公里/小时
    @SerializedName("wind_spd")
    public String windSpeed;
}
