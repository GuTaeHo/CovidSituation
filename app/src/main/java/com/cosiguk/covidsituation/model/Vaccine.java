package com.cosiguk.covidsituation.model;

import com.google.gson.annotations.SerializedName;

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

    public int getAccumulatedFirstCnt() {
        return accumulatedFirstCnt;
    }

    public void setAccumulatedFirstCnt(int accumulatedFirstCnt) {
        this.accumulatedFirstCnt = accumulatedFirstCnt;
    }

    public int getAccumulatedSecondCnt() {
        return accumulatedSecondCnt;
    }

    public void setAccumulatedSecondCnt(int accumulatedSecondCnt) {
        this.accumulatedSecondCnt = accumulatedSecondCnt;
    }

    public int getAccumulatedThirdCnt() {
        return accumulatedThirdCnt;
    }

    public void setAccumulatedThirdCnt(int accumulatedThirdCnt) {
        this.accumulatedThirdCnt = accumulatedThirdCnt;
    }

    public int getFirstCnt() {
        return firstCnt;
    }

    public void setFirstCnt(int firstCnt) {
        this.firstCnt = firstCnt;
    }

    public int getSecondCnt() {
        return secondCnt;
    }

    public void setSecondCnt(int secondCnt) {
        this.secondCnt = secondCnt;
    }

    public int getThirdCnt() {
        return thirdCnt;
    }

    public void setThirdCnt(int thirdCnt) {
        this.thirdCnt = thirdCnt;
    }

    public int getTotalFirstCnt() {
        return totalFirstCnt;
    }

    public void setTotalFirstCnt(int totalFirstCnt) {
        this.totalFirstCnt = totalFirstCnt;
    }

    public int getTotalSecondCnt() {
        return totalSecondCnt;
    }

    public void setTotalSecondCnt(int totalSecondCnt) {
        this.totalSecondCnt = totalSecondCnt;
    }

    public int getTotalThirdCnt() {
        return totalThirdCnt;
    }

    public void setTotalThirdCnt(int totalThirdCnt) {
        this.totalThirdCnt = totalThirdCnt;
    }

    public String getBaseDate() {
        return baseDate;
    }

    public void setBaseDate(String baseDate) {
        this.baseDate = baseDate;
    }
}
