package com.cosiguk.covidsituation.network.response;

import com.cosiguk.covidsituation.model.News;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

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

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }

    public ArrayList<News> getItems() {
        return items;
    }

    public void setItems(ArrayList<News> items) {
        this.items = items;
    }
}
