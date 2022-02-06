package com.cosiguk.covidsituation.network.response;

import com.cosiguk.covidsituation.model.Hospital;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseHospital {
    @SerializedName("totalCount")
    public String totalCount;

    @SerializedName("data")
    public ArrayList<Hospital> data;

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public ArrayList<Hospital> getData() {
        return data;
    }

    public void setData(ArrayList<Hospital> data) {
        this.data = data;
    }
}
