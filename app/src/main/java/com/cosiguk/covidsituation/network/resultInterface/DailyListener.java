package com.cosiguk.covidsituation.network.resultInterface;

import com.cosiguk.covidsituation.model.Infection;

public interface DailyListener {
    void success(Infection infection);

    void fail(String message);
}
