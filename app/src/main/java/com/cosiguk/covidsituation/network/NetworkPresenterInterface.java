package com.cosiguk.covidsituation.network;


import com.cosiguk.covidsituation.network.resultInterface.BoardListListener;
import com.cosiguk.covidsituation.network.resultInterface.HospitalListener;
import com.cosiguk.covidsituation.network.resultInterface.NewsListener;
import com.cosiguk.covidsituation.network.resultInterface.NoticeListener;
import com.cosiguk.covidsituation.network.resultInterface.TotalListener;
import com.cosiguk.covidsituation.network.resultInterface.VaccineListener;
import com.cosiguk.covidsituation.network.resultInterface.VersionListener;

import java.util.HashMap;

public interface NetworkPresenterInterface {
    // 버전 체크
    void version(VersionListener listener);

    // 공지 사항
    void notice(NoticeListener listener);

    // 전체 현황
    void total(HashMap<String, String> requestQuery, TotalListener listener);

    // 시,도 별 현황
    void boardList(HashMap<String, String> requestBoardList, BoardListListener listener);

    // 뉴스
    void news(HashMap<String, String> headers, HashMap<String, Object> requestNews, NewsListener listener);

    // 백신 접종 현황
    void vaccineTotal(HashMap<String, String> requestQuery, VaccineListener listener);

    // 진료소 현황
    void hospital(HashMap<String, String> requestQuery, HospitalListener listener);
}
