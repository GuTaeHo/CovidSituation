package com.cosiguk.covidsituation.network.response;

import com.cosiguk.covidsituation.network.responseinfection.Body;
import com.cosiguk.covidsituation.network.responseinfection.Header;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import lombok.Data;

@Xml(name = "response")
@Data
public class ResponseInfection {
    @Element
    public Header header;
    @Element
    public Body body;
}
