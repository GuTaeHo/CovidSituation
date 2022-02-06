package com.cosiguk.covidsituation.network.response;

import com.cosiguk.covidsituation.network.responsecity.Body;
import com.cosiguk.covidsituation.network.responsecity.Header;
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
