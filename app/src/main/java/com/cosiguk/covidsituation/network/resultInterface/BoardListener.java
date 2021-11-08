package com.cosiguk.covidsituation.network.resultInterface;

import com.cosiguk.covidsituation.model.Board;
import com.cosiguk.covidsituation.model.City;

import java.util.ArrayList;
import java.util.List;

public interface BoardListener {
    void success(ArrayList<Board> items);

    void fail(String message);
}
