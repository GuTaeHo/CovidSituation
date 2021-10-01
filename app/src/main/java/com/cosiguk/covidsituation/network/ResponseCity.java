package com.cosiguk.covidsituation.network;

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

    /*
    public Header getHeader() {
        if (header != null) {
            return header;
        }
        return new Header();
    }

    public Body getBody() {
        if (body != null) {
            return body;
        }
        return new Body();
    }
    */
}
