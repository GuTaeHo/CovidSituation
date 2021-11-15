package com.cosiguk.covidsituation.network.resultInterface;

import com.cosiguk.covidsituation.network.responseinfection.Body;

public interface TotalListener {
    void success(Body infection);
    // 요청 결과가 적절하지 않은 경우
    void request();

    void fail(String message);
}
