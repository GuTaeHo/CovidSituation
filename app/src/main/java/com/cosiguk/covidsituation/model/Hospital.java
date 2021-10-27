package com.cosiguk.covidsituation.model;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Hospital {
    @SerializedName("address")
    public String address;

    @SerializedName("centerName")
    public String centerName;

    @SerializedName("facilityName")
    public String facilityName;

    @SerializedName("lat")
    public String lat;

    @SerializedName("lng")
    public String lng;

    @SerializedName("phoneNumber")
    public String phoneNumber;

    @SerializedName("sido")
    public String sido;

    @SerializedName("zipCode")
    public String zipCode;
}
