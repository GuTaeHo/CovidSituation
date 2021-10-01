package com.cosiguk.covidsituation.network.responsecity;

import com.cosiguk.covidsituation.model.Item;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;

import lombok.Data;

@Xml(name = "items")
@Data
public class Items {
    public ArrayList<Item> item;
}
