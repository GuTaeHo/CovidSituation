package com.cosiguk.covidsituation.model;

import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

import lombok.Data;

@Data
public class News {
    @SerializedName("title")
    private String title;

    @SerializedName("originallink")
    private String originallink;

    @SerializedName("link")
    private String link;

    @SerializedName("description")
    private String description;

    @SerializedName("pubDate")
    private String pubDate;
}
