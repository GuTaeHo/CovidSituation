package com.cosiguk.covidsituation.model;

import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name = "item")
public class Infection {
    @PropertyElement(name = "accDefRate")
    public double accDefRate;
    @PropertyElement(name = "accExamCnt")
    public int accExamCnt;
    @PropertyElement(name = "accExamCompCnt")
    public int accExamCompCnt;
    @PropertyElement(name = "careCnt")
    public int careCnt;
    @PropertyElement(name = "clearCnt")
    public int clearCnt;
    @PropertyElement(name = "createDt")
    public String createDt;
    @PropertyElement(name = "deathCnt")
    public int deathCnt;
    @PropertyElement(name = "decideCnt")
    public int decideCnt;
    @PropertyElement(name = "examCnt")
    public int examCnt;
    @PropertyElement(name = "resutlNegCnt")
    public int resutlNegCnt;
    @PropertyElement(name = "seq")
    public int seq;
    @PropertyElement(name = "stateDt")
    public String stateDt;
    @PropertyElement(name = "stateTime")
    public String stateTime;
    @PropertyElement(name = "updateDt")
    public String updateDt;

    public double getAccDefRate() {
        return accDefRate;
    }

    public void setAccDefRate(double accDefRate) {
        this.accDefRate = accDefRate;
    }

    public int getAccExamCnt() {
        return accExamCnt;
    }

    public void setAccExamCnt(int accExamCnt) {
        this.accExamCnt = accExamCnt;
    }

    public int getAccExamCompCnt() {
        return accExamCompCnt;
    }

    public void setAccExamCompCnt(int accExamCompCnt) {
        this.accExamCompCnt = accExamCompCnt;
    }

    public int getCareCnt() {
        return careCnt;
    }

    public void setCareCnt(int careCnt) {
        this.careCnt = careCnt;
    }

    public int getClearCnt() {
        return clearCnt;
    }

    public void setClearCnt(int clearCnt) {
        this.clearCnt = clearCnt;
    }

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

    public int getDecideCnt() {
        return decideCnt;
    }

    public void setDecideCnt(int decideCnt) {
        this.decideCnt = decideCnt;
    }

    public int getExamCnt() {
        return examCnt;
    }

    public void setExamCnt(int examCnt) {
        this.examCnt = examCnt;
    }

    public int getResutlNegCnt() {
        return resutlNegCnt;
    }

    public void setResutlNegCnt(int resutlNegCnt) {
        this.resutlNegCnt = resutlNegCnt;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getStateDt() {
        return stateDt;
    }

    public void setStateDt(String stateDt) {
        this.stateDt = stateDt;
    }

    public String getStateTime() {
        return stateTime;
    }

    public void setStateTime(String stateTime) {
        this.stateTime = stateTime;
    }

    public String getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(String updateDt) {
        this.updateDt = updateDt;
    }
}
