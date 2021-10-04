package com.cosiguk.covidsituation.network.resultInterface;

import com.cosiguk.covidsituation.model.ItemTotal;
import com.cosiguk.covidsituation.network.responsetotal.Items;

import java.util.List;

public interface TotalListener {
    void success(List<ItemTotal> itemTotal);

    void fail(String message);
}
