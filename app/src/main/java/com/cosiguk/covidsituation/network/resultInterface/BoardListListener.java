package com.cosiguk.covidsituation.network.resultInterface;

import com.cosiguk.covidsituation.network.responsecity.Items;

import java.util.ArrayList;

public interface BoardListListener {
    void success(Items items);

    void fail(String message);
}
