package com.cosiguk.covidsituation.network.resultInterface;

import com.cosiguk.covidsituation.model.Vaccine;

import java.util.ArrayList;

public interface VaccineListener {
    void success(ArrayList<Vaccine> items);
    void request(ArrayList<Vaccine> items);
    void fail(String message);
}
