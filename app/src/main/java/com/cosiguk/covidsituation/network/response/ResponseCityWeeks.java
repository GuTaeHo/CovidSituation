package com.cosiguk.covidsituation.network.response;

import com.cosiguk.covidsituation.model.City;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseCityWeeks {
    @SerializedName("data")
    private ArrayList<City> data;

    public ArrayList<City> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data;
    }

    public void setData(ArrayList<City> data) {
        this.data = data;
    }
}
