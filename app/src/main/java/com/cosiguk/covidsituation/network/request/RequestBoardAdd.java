package com.cosiguk.covidsituation.network.request;

import com.google.gson.annotations.SerializedName;

public class RequestBoardAdd {
    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("password")
    private String password;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
