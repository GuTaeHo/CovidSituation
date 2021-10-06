package com.cosiguk.covidsituation.network.resultInterface;

import com.cosiguk.covidsituation.model.ItemTotal;
import com.cosiguk.covidsituation.network.responsetotal.Items;

import java.util.List;

public interface TotalListener {
    void success(List<ItemTotal> itemTotal);

    // 요청 결과가 적절하지 않은 경우
    void reRequest(List<ItemTotal> itemTotal);

    void fail(String message);
}
