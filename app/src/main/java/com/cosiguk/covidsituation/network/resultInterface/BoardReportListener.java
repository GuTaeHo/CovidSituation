package com.cosiguk.covidsituation.network.resultInterface;

import com.cosiguk.covidsituation.network.Response;

public interface BoardReportListener {
    void success(Response<Object> item);

    void fail(String message);
}
