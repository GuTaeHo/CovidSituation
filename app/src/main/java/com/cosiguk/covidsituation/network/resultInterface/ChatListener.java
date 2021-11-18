package com.cosiguk.covidsituation.network.resultInterface;

import com.cosiguk.covidsituation.model.Board;

import java.util.ArrayList;

public interface ChatListener {
    void success(ArrayList<Board> boards);
    void fail(String msg);
}
