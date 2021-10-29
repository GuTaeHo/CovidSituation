package com.cosiguk.covidsituation.util;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class ConvertUtil {
    public static int PREVIOUS_DAY = 86400000;

    // 현재 날짜
    public static String getCurrentFormatTime(int initMillisecond) {
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyyMMdd", Locale.KOREA);
        return formatter.format(System.currentTimeMillis() - initMillisecond);
    }

    // 하루 전 날짜
    public static String getYesterdayFormatTime(int initMillisecond) {
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyyMMdd", Locale.KOREA);
        return formatter.format(System.currentTimeMillis() - PREVIOUS_DAY - initMillisecond);
    }

    // 년, 월, 일 까지 반환 (ex : 2021.10.01)
    public static String convertDateDot(String date) {
        String year = date.substring(0,4);
        String month = date.substring(4,6);
        String day = date.substring(6,8);

        return year + "." + month + "." + day;
    }

    // 인자 형식 (2021-10-26 16:11:01)
    public static String convertBarDateToDot(String date) {
        // 공백 기준 분리
        String[] core = date.split("\\s");
        String[] dates = core[0].split("-");

        return dates[0] + "." + dates[1] + "." + dates[2];
    }

    // 백신 API 금일 날짜 요청
    public static String currentDateBar(int initMillisecond) {
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd", Locale.KOREA);
        return formatter.format(System.currentTimeMillis() - initMillisecond);
    }

    // 뉴스 API 날짜 변환
    public static String convertDateBar(String date) {
        SimpleDateFormat str2Date = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss", Locale.US);
        SimpleDateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        String toDate = null;
        try {
            // String to Date
            Date fromDate = str2Date.parse(date);
            assert fromDate != null;
            // Date to Custom Pattern
            toDate = toFormat.format(fromDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return toDate;
    }

    public static String convertCommaSeparator(int value) {
        DecimalFormat formatter = new DecimalFormat("###,###");

        return formatter.format(value);
    }

    // 숫자 쉼표(,) 구분자를 넣어 반환
    public static String convertSignCommaSeparator(int value) {
        DecimalFormat formatter = new DecimalFormat("###,###");

        if (value < 0) {
            return formatter.format(value);
        }
        return "+"+formatter.format(value);
    }

    // 문자 분리, 위치 지정
    public static String splitString(String data, int start) {
        String[] frg = data.split("\\s");
        StringBuilder result = new StringBuilder();

        for (int i = start; i < frg.length; i++) {
            result.append(frg[i]);
            result.append(" ");
        }

        return String.valueOf(result);
    }
}
