package com.cosiguk.covidsituation.network.resultInterface;

import com.cosiguk.covidsituation.model.News;

import java.util.ArrayList;

public interface NewsListener {
    void success(ArrayList<News> list);
    void fail(String message);
}
