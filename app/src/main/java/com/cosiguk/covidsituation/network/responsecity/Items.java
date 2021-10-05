package com.cosiguk.covidsituation.network.responsecity;

import com.cosiguk.covidsituation.model.ItemCity;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Xml
@Data
public class Items {
    @Element(name = "item")
    public List<ItemCity> item;
}