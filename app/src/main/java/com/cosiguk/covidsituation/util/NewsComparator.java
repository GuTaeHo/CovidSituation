package com.cosiguk.covidsituation.util;

import com.cosiguk.covidsituation.model.News;

import java.util.Comparator;

public class NewsComparator implements Comparator<News> {
    @Override
    public int compare(News f1, News f2) {
        return f2.getPubDate().compareTo(f1.getPubDate());
    }
}
