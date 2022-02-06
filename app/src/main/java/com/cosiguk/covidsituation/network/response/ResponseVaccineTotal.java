package com.cosiguk.covidsituation.network.response;

import com.cosiguk.covidsituation.model.Vaccine;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseVaccineTotal {
    @SerializedName("currentCount")
    public String currentCount;

    @SerializedName("data")
    public ArrayList<Vaccine> data;

    public String getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(String currentCount) {
        this.currentCount = currentCount;
    }

    public ArrayList<Vaccine> getData() {
        return data;
    }

    public void setData(ArrayList<Vaccine> data) {
        this.data = data;
    }
}
