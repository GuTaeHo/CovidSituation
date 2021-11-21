package com.cosiguk.covidsituation.network.response;

import com.cosiguk.covidsituation.model.Board;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseBoard {
    @SerializedName("data")
    private ArrayList<Board> data;

    @SerializedName("totalCount")
    private int totalCount;

    @SerializedName("totalPage")
    private int totalPage;

    @SerializedName("requestPage")
    private int requestPage;

    public ArrayList<Board> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getRequestPage() {
        return requestPage;
    }
}

