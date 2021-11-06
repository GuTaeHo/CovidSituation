package com.cosiguk.covidsituation.network.resultInterface;

import com.cosiguk.covidsituation.model.Notice;

import java.util.ArrayList;

public interface NoticeListener {
    void success(ArrayList<Notice> list);
    void fail(String message);
}
