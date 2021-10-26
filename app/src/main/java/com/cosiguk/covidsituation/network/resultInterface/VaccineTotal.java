package com.cosiguk.covidsituation.network.resultInterface;

import com.cosiguk.covidsituation.network.responseVaccine.Items;

import java.util.ArrayList;

public interface VaccineTotal {
    void success(ArrayList<Items> items);
    void request(ArrayList<Items> items);
    void fail(String message);
}
