package com.cosiguk.covidsituation.network;

import com.cosiguk.covidsituation.model.Hospital;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import lombok.Getter;

@Getter
public class ResponseHospital {
    @SerializedName("totalCount")
    public String totalCount;

    @SerializedName("data")
    public ArrayList<Hospital> data;
}
