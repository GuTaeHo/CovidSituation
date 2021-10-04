package com.cosiguk.covidsituation.network.responsetotal;

import com.cosiguk.covidsituation.model.ItemTotal;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Xml
@Data
public class Items {
    @Element(name = "item")
    public List<ItemTotal> item;
}
