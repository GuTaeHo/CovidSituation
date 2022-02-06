package com.cosiguk.covidsituation.network.response;

import com.cosiguk.covidsituation.network.responseinfection.Body;
import com.cosiguk.covidsituation.network.responseinfection.Header;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name = "response")
public class ResponseInfection {
    @Element
    public Header header;
    @Element
    public Body body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
