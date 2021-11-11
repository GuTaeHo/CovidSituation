package com.cosiguk.covidsituation.network.resultInterface;

public interface BoardRecommendListener {
    void success(int code);
    void fail(String message);
}
