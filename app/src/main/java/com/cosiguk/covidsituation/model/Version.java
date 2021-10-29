package com.cosiguk.covidsituation.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Version {
    @SerializedName("android")
    public String android;

    @SerializedName("ios")
    public String ios;
}
