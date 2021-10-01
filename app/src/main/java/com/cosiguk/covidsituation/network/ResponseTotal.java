package com.cosiguk.covidsituation.network;

import com.cosiguk.covidsituation.network.responsetotal.Body;
import com.cosiguk.covidsituation.network.responsetotal.Header;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import lombok.Data;

@Xml(name = "response")
@Data
public class ResponseTotal {
    @Element
    public Header header;
    @Element
    public Body body;
}
