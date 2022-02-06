package com.cosiguk.covidsituation.network.responsecity;

import com.cosiguk.covidsituation.model.City;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

@Xml
public class Items {
    @Element(name = "item")
    public List<City> item;

    public List<City> getItem() {
        return item;
    }

    public void setItem(List<City> item) {
        this.item = item;
    }
}
