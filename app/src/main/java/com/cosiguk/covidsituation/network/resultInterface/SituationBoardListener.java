package com.cosiguk.covidsituation.network.resultInterface;

import com.cosiguk.covidsituation.model.City;

import java.util.List;

public interface SituationBoardListener {
    void success(List<City> items);

    void fail(String message);
}
