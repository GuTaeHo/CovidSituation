package com.cosiguk.covidsituation.network.resultInterface;

import com.cosiguk.covidsituation.model.City;

import java.util.ArrayList;

public interface InfectionCityWeeks {
    void success(ArrayList<City> arrayList);
    void fail(String message);
}
