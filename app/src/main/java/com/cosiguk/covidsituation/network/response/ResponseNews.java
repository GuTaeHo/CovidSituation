package com.cosiguk.covidsituation.network.response;

import com.cosiguk.covidsituation.model.News;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import lombok.Data;

@Data
public class ResponseNews {
    @SerializedName("lastBuildDate")
    private String lastBuildDate;

    @SerializedName("total")
    private String total;

    @SerializedName("start")
    private int start;

    @SerializedName("display")
    private int display;

    @SerializedName("items")
    private ArrayList<News> items;
}
