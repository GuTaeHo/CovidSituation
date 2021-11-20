package com.cosiguk.covidsituation.network;


import com.cosiguk.covidsituation.network.resultInterface.BoardAddListener;
import com.cosiguk.covidsituation.network.resultInterface.BoardDeprecateListener;
import com.cosiguk.covidsituation.network.resultInterface.BoardDetailListener;
import com.cosiguk.covidsituation.network.resultInterface.BoardListener;
import com.cosiguk.covidsituation.network.resultInterface.BoardRecommendListener;
import com.cosiguk.covidsituation.network.resultInterface.ChatAddListener;
import com.cosiguk.covidsituation.network.resultInterface.ChatListener;
import com.cosiguk.covidsituation.network.resultInterface.DeleteBoardListener;
import com.cosiguk.covidsituation.network.resultInterface.SituationBoardListener;
import com.cosiguk.covidsituation.network.resultInterface.HospitalListener;
import com.cosiguk.covidsituation.network.resultInterface.NewsListener;
import com.cosiguk.covidsituation.network.resultInterface.NoticeListener;
import com.cosiguk.covidsituation.network.resultInterface.TotalListener;
import com.cosiguk.covidsituation.network.resultInterface.VaccineListener;
import com.cosiguk.covidsituation.network.resultInterface.VersionListener;

import java.util.HashMap;

import okhttp3.RequestBody;

public interface NetworkPresenterInterface {
    // 버전 체크
    void version(VersionListener listener);

    // 공지 사항
    void notice(NoticeListener listener);

    // 전체 현황
    void total(HashMap<String, String> requestQuery, TotalListener listener);

    // 시,도 별 현황
    void situationBoardList(HashMap<String, String> requestBoardList, SituationBoardListener listener);

    // 뉴스 조회
    void news(HashMap<String, String> headers, HashMap<String, Object> requestNews, NewsListener listener);

    // 백신 접종 현황
    void vaccineTotal(HashMap<String, String> requestQuery, VaccineListener listener);

    // 진료소 현황
    void hospital(HashMap<String, String> requestQuery, HospitalListener listener);

    // 게시글 조회
    void boardList(HashMap<String, RequestBody> request, BoardListener listener);

    // 게시글 추가
    void boardAdd(HashMap<String, RequestBody> requestBoardAdd, BoardAddListener listener);

    // 게시글 상세
    void boardDetail(int boardID, BoardDetailListener listener);
    
    // 게시글 추천
    void boardRecommend(int boardID, BoardRecommendListener listener);

    // 게시글 비추천
    void boardDeprecate(int boardID, BoardDeprecateListener listener);

    // 게시글 삭제
    void deleteBoard(int boardID, DeleteBoardListener listener);

    // 댓글 조회
    void chatList(int boardID, ChatListener listener);

    // 댓글 추가
    void chatAdd(int boardID, HashMap<String, RequestBody> requestChatAdd, ChatAddListener listener);
}
