package com.cosiguk.covidsituation.network.resultInterface;

import com.cosiguk.covidsituation.model.ItemTotal;
import com.cosiguk.covidsituation.network.responsetotal.Items;

public interface DailyListener {
    void success(ItemTotal itemTotal);

    void fail(String message);
}
