package com.cosiguk.covidsituation.network.response;

import com.cosiguk.covidsituation.model.Notice;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseBoard {
    @SerializedName("data")
    private ArrayList<Notice> data;

    public ArrayList<Notice> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data;
    }
}
