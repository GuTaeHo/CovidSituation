package com.cosiguk.covidsituation.network.responseinfection;

import com.cosiguk.covidsituation.model.Infection;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

@Xml
public class Items {
    @Element(name = "item")
    public List<Infection> item;

    public List<Infection> getItem() {
        return item;
    }

    public void setItem(List<Infection> item) {
        this.item = item;
    }
}
