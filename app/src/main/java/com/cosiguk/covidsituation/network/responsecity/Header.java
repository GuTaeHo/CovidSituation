package com.cosiguk.covidsituation.network.responsecity;

import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

import lombok.Data;

@Xml(name = "header")
@Data
public class Header {
    @PropertyElement
    public int resultCode;
    @PropertyElement
    public String resultMsg;
}
