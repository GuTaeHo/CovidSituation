package com.cosiguk.covidsituation.model;

import com.google.gson.annotations.SerializedName;

public class Board {
    @SerializedName("title")
    private String title;
    @SerializedName("id")
    private int id;
    @SerializedName("password")
    private String password;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("content")
    private String content;
    @SerializedName("createdDate")
    private String createdDate;
    @SerializedName("modifiedDate")
    private String modifiedDate;
    @SerializedName("hit")
    private String hit;
    @SerializedName("recommend")
    private int recommend;
    @SerializedName("deprecate")
    private int deprecate;
    @SerializedName("review_count")
    private int reviewCount;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getHit() {
        return hit;
    }

    public void setHit(String hit) {
        this.hit = hit;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public int getDeprecate() {
        return deprecate;
    }

    public void setDeprecate(int deprecate) {
        this.deprecate = deprecate;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }
}
