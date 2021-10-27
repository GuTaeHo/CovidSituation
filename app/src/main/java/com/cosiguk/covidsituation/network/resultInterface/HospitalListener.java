package com.cosiguk.covidsituation.network.resultInterface;

import com.cosiguk.covidsituation.model.Hospital;

import java.util.ArrayList;

public interface HospitalListener {
    void success(ArrayList<Hospital> list);
    void fail(String message);
}
