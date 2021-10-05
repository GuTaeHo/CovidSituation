package com.cosiguk.covidsituation.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ConvertUtil {
    public static int PREVIOUS_DAY = 8640000;

    // 현재 날짜
    public static String getCurrentFormatTime(int initMillisecond) {
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyyMMdd", Locale.KOREA);
        return formatter.format (System.currentTimeMillis() - initMillisecond);
    }

    // 하루 전 날짜
    public static String getYesterdayFormatTime(int initMillisecond) {
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyyMMdd", Locale.KOREA);
        return formatter.format(System.currentTimeMillis() - 86400000 - initMillisecond);
    }

    // 년, 월, 일 까지 반환 (ex : 2021.10.01)
    public static String covertDateDot(String date) {
        String year = date.substring(0,4);
        String month = date.substring(4,6);
        String day = date.substring(6,8);

        return year + "." + month + "." + day;
    }

    // 숫자 쉼표(,) 구분자를 넣어 반환
    public static String convertCommaSeparator(int value) {
        DecimalFormat formatter = new DecimalFormat("###,###");

        return formatter.format(value);
    }
}
