package com.cosiguk.covidsituation.network.response;

import com.cosiguk.covidsituation.model.Board;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseBoardDetail {
    @SerializedName("account")
    private Board data;

    public Board getData() {
        if (data == null) {
            data = new Board();
        }
        return data;
    }
}
