package com.study.onedx.pweather.gson;

import com.google.gson.annotations.SerializedName;

public class Hour {
    //天气状况描述
    @SerializedName("cond_txt")
    public String Condition;

    //相对湿度
    @SerializedName("hum")
    public String humidity;

    //降水概率
    @SerializedName("pop")
    public String pcpnProbability;

    //大气压强
    @SerializedName("pres")
    public String atmosphericPressure;

    //预报时间
    @SerializedName("time")
    public String time;

    //温度
    @SerializedName("tmp")
    public String temperature;

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
