package com.cosiguk.covidsituation.network.resultInterface;

import com.cosiguk.covidsituation.model.ItemCity;
import com.cosiguk.covidsituation.network.responsecity.Items;

import java.util.ArrayList;
import java.util.List;

public interface BoardListListener {
    void success(List<ItemCity> items);

    void fail(String message);
}
