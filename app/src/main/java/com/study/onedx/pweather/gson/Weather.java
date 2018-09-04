package com.study.onedx.pweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {

    @SerializedName("status")
    public String status;

    public Basic basic;

    public Now now;

    public Update update;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

    @SerializedName("hourly")
    public List<Hour> hourList;

    @SerializedName("lifestyle")
    public List<LifeStyle> lifeStyleList;
}
