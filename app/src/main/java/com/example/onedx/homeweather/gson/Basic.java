package com.example.onedx.homeweather.gson;

import com.google.gson.annotations.SerializedName;

public class Basic {

    @SerializedName("location")
    public String cityName;

    @SerializedName("cid")
    public String cityId;

    @SerializedName("lon")
    public String longitude;

    @SerializedName("lat")
    public String latitude;

}
