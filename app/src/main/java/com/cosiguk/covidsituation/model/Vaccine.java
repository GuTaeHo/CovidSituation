package com.cosiguk.covidsituation.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Vaccine {
    // 전일 누적
    @SerializedName("accumulatedFirstCnt")
    public int accumulatedFirstCnt;

    @SerializedName("accumulatedSecondCnt")
    public int accumulatedSecondCnt;

    @SerializedName("accumulatedThirdCnt")
    public int accumulatedThirdCnt;

    // 당일 실적
    @SerializedName("firstCnt")
    public int firstCnt;

    @SerializedName("secondCnt")
    public int secondCnt;

    @SerializedName("thirdCnt")
    public int thirdCnt;

    // 전체 누적
    @SerializedName("totalFirstCnt")
    public int totalFirstCnt;

    @SerializedName("totalSecondCnt")
    public int totalSecondCnt;

    @SerializedName("totalThirdCnt")
    public int totalThirdCnt;

    @SerializedName("baseDate")
    public String baseDate;
}
