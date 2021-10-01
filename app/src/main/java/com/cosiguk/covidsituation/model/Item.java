package com.cosiguk.covidsituation.model;

import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

import lombok.Getter;
import lombok.Setter;

@Xml
@Getter
@Setter
public class Item {
    @PropertyElement(name = "createDt")
    public String createDt;
    @PropertyElement(name = "deathCnt")
    public int deathCnt;
    @PropertyElement(name = "defCnt")
    public int defCnt;
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
    @PropertyElement(name = "qurRate")
    public double qurRate;
    @PropertyElement(name = "seq")
    public int seq;
    @PropertyElement(name = "stdDay")
    public String stdDay;
    @PropertyElement(name = "updateDt")
    public String updateDt;

}
