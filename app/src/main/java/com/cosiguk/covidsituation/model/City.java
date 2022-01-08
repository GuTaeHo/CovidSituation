package com.cosiguk.covidsituation.model;

import com.google.gson.annotations.SerializedName;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

import lombok.Data;

@Xml(name = "item")
@Data
public class City {
    @SerializedName("createDt")
    @PropertyElement(name = "createDt")
    public String createDt;
    @SerializedName("deathcnt")
    @PropertyElement(name = "deathCnt")
    public int deathCnt;
    @SerializedName("defcnt")
    @PropertyElement(name = "defCnt")
    public int defCnt;
    @SerializedName("gubun")
    @PropertyElement(name = "gubun")
    public String gubun;
    @PropertyElement(name = "gubunCn")
    public String gubunCn;
    @PropertyElement(name = "gubunEn")
    public String gubunEn;
    @PropertyElement(name = "incDec")
    public int incDec;
    @PropertyElement(name = "isolClearCnt")
    public int isolClearCnt;
    @PropertyElement(name = "isolIngCnt")
    public int isolIngCnt;
    @PropertyElement(name = "localOccCnt")
    public int localOccCnt;
    @PropertyElement(name = "overFlowCnt")
    public int overFlowCnt;
    @PropertyElement(name = "seq")
    public int seq;
    @PropertyElement(name = "stdDay")
    public String stdDay;
    @PropertyElement(name = "updateDt")
    public String updateDt;
}
