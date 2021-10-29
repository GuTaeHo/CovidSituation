package com.cosiguk.covidsituation.network.responseInfection;

import com.cosiguk.covidsituation.model.City;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

import lombok.Data;

@Xml
@Data
public class Items {
    @Element(name = "item")
    public List<City> item;
}
