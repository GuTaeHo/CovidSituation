package com.cosiguk.covidsituation.network.resultInterface;

import com.cosiguk.covidsituation.network.response.ResponseBoard;
import com.cosiguk.covidsituation.network.response.ResponseBoardData;

public interface BoardListener {
    void success(ResponseBoard item);

    void fail(String message);
}
