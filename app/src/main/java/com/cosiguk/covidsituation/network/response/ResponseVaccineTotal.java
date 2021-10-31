package com.cosiguk.covidsituation.network.response;

import com.cosiguk.covidsituation.model.Vaccine;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import lombok.Data;

@Data
public class ResponseVaccineTotal {
    @SerializedName("currentCount")
    public String currentCount;

    @SerializedName("data")
    public ArrayList<Vaccine> data;
}
