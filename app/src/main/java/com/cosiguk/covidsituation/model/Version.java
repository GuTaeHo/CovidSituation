package com.cosiguk.covidsituation.model;

import com.google.gson.annotations.SerializedName;

public class Version {
    @SerializedName("android")
    public String android;

    @SerializedName("ios")
    public String ios;

    public String getAndroid() {
        if (android == null) {
            return "";
        }
        return android;
    }

    public void setAndroid(String android) {
        this.android = android;
    }

    public String getIos() {
        if (ios == null) {
            return "";
        }
        return ios;
    }

    public void setIos(String ios) {
        this.ios = ios;
    }
}
