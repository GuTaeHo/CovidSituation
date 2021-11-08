package com.cosiguk.covidsituation.model;

import com.google.gson.annotations.SerializedName;

public class Board {
    @SerializedName("id")
    private String id;
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
    @SerializedName("recommend")
    private int recommend;
    @SerializedName("deprecate")
    private int deprecate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
}
