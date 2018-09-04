package com.study.onedx.pweather.gson;

import com.google.gson.annotations.SerializedName;

public class Update {

    @SerializedName("loc")
    public String locationTime;

    @SerializedName("utc")
    public String UTCTime;
}
