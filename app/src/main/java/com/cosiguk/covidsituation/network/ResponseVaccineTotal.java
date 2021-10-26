package com.cosiguk.covidsituation.network;

import com.cosiguk.covidsituation.network.responseVaccine.Items;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import lombok.Data;

@Data
public class ResponseVaccineTotal {
    @SerializedName("currentCount")
    public String currentCount;

    @SerializedName("data")
    public ArrayList<Items> data;
}
