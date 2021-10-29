package com.cosiguk.covidsituation.network;

import com.cosiguk.covidsituation.network.responseInfection.Body;
import com.cosiguk.covidsituation.network.responseInfection.Header;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import lombok.Data;

@Xml(name = "response")
@Data
public class ResponseCity {
    @Element
    public Header header;
    @Element
    public Body body;
}
