package com.cosiguk.covidsituation.network.response;

import com.cosiguk.covidsituation.model.Board;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseBoard {
    @SerializedName("data")
    private ArrayList<Board> data;

    public ArrayList<Board> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data;
    }
}
