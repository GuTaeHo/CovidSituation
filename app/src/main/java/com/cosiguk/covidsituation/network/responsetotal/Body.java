package com.cosiguk.covidsituation.network.responsetotal;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

import lombok.Data;

@Data
@Xml(name = "body")
public class Body {
    @Element
    public Items items;
    @PropertyElement
    public int numOfRows;
    @PropertyElement
    public int pageNo;
    @PropertyElement
    public int totalCount;
}
