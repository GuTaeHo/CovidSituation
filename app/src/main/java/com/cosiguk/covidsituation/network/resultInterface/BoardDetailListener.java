package com.cosiguk.covidsituation.network.resultInterface;

import com.cosiguk.covidsituation.model.Board;

public interface BoardDetailListener {
    void success(Board board);
    void fail(String message);
}
