package com.cosiguk.covidsituation.model;

import com.google.gson.annotations.SerializedName;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name = "item")
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

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

    public int getDeathCnt() {
        return deathCnt;
    }

    public void setDeathCnt(int deathCnt) {
        this.deathCnt = deathCnt;
    }

    public int getDefCnt() {
        return defCnt;
    }

    public void setDefCnt(int defCnt) {
        this.defCnt = defCnt;
    }

    public String getGubun() {
        return gubun;
    }

    public void setGubun(String gubun) {
        this.gubun = gubun;
    }

    public String getGubunCn() {
        return gubunCn;
    }

    public void setGubunCn(String gubunCn) {
        this.gubunCn = gubunCn;
    }

    public String getGubunEn() {
        return gubunEn;
    }

    public void setGubunEn(String gubunEn) {
        this.gubunEn = gubunEn;
    }

    public int getIncDec() {
        return incDec;
    }

    public void setIncDec(int incDec) {
        this.incDec = incDec;
    }

    public int getIsolClearCnt() {
        return isolClearCnt;
    }

    public void setIsolClearCnt(int isolClearCnt) {
        this.isolClearCnt = isolClearCnt;
    }

    public int getIsolIngCnt() {
        return isolIngCnt;
    }

    public void setIsolIngCnt(int isolIngCnt) {
        this.isolIngCnt = isolIngCnt;
    }

    public int getLocalOccCnt() {
        return localOccCnt;
    }

    public void setLocalOccCnt(int localOccCnt) {
        this.localOccCnt = localOccCnt;
    }

    public int getOverFlowCnt() {
        return overFlowCnt;
    }

    public void setOverFlowCnt(int overFlowCnt) {
        this.overFlowCnt = overFlowCnt;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getStdDay() {
        return stdDay;
    }

    public void setStdDay(String stdDay) {
        this.stdDay = stdDay;
    }

    public String getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(String updateDt) {
        this.updateDt = updateDt;
    }
}
