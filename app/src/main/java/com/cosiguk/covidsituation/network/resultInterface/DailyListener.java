package com.cosiguk.covidsituation.network.resultInterface;

import com.cosiguk.covidsituation.model.ItemTotal;

public interface DailyListener {
    void success(ItemTotal itemTotal);

    void fail(String message);
}
