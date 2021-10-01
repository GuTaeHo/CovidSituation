package com.cosiguk.covidsituation.network;


import com.cosiguk.covidsituation.network.resultInterface.BoardListListener;
import com.cosiguk.covidsituation.network.resultInterface.DailyListener;

// 액티비티에서 사용하는 통신 기능을 반드시 구현하도록 하는 인터페이스 선언
public interface NetworkPresenterInterface {
    // 일일 현황
    void daily(String serviceKey, DailyListener listener);

    // 시,도 별 현황
    void boardList(String serviceKey, BoardListListener listener);
}
