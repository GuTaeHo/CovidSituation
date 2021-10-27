package com.cosiguk.covidsituation.model;

import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Xml(name = "item")
@Data
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
}
