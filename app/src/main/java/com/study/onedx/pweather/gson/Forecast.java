package com.study.onedx.pweather.gson;

import com.google.gson.annotations.SerializedName;

public class Forecast {

    //预报日期
    @SerializedName("date")
    public String date;

    //白天天气状况描述
    @SerializedName("cond_txt_d")
    public String dayCondition;

    //晚间天气状况描述
    @SerializedName("cond_txt_n")
    public String nightCondition;

    //相对湿度
    @SerializedName("hum")
    public String humidity;

    //降水量
    @SerializedName("pcpn")
    public String Precipitation;

    //降水概率
    @SerializedName("pop")
    public String pcpnProbability;

    //大气压强
    @SerializedName("pres")
    public String atmosphericPressure;

    //最高温度
    @SerializedName("tmp_max")
    public String maxTemperature;

    //最低温度
    @SerializedName("tmp_min")
    public String minTemperature;

    //紫外线强度指数
    @SerializedName("uv_index")
    public String UVIntensity;

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
