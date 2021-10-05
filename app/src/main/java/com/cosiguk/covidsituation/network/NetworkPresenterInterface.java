package com.cosiguk.covidsituation.network;


import com.cosiguk.covidsituation.network.resultInterface.BoardListListener;
import com.cosiguk.covidsituation.network.resultInterface.TotalListener;

import java.util.HashMap;

public interface NetworkPresenterInterface {
    // 전체 현황
    void total(HashMap<String, String> requestQuery, TotalListener listener);

    // 시,도 별 현황
    void boardList(HashMap<String, String> requestBoardList, BoardListListener listener);
}
