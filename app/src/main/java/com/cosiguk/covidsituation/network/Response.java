package com.cosiguk.covidsituation.network;

import com.google.gson.annotations.SerializedName;

public class Response<T> {
    public static final int SUCCESS = 0;

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("error")
    private String error;

    @SerializedName("result_data")
    private T result_data;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public T getResultData() {
        return result_data;
    }
}
