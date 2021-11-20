package com.cosiguk.covidsituation.network.response;

import com.cosiguk.covidsituation.model.Board;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseBoardData {
    @SerializedName("boardList")
    private ArrayList<Board> boardList;

    @SerializedName("totalCount")
    private int totalCount;

    @SerializedName("totalPage")
    private int totalPage;

    @SerializedName("requestPage")
    private int requestPage;

    public ArrayList<Board> getBoardList() {
        if (boardList == null) {
            boardList = new ArrayList<>();
        }
        return boardList;
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

