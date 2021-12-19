package com.cosiguk.covidsituation.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ActivityUtil {
    public static final String DATA = "data";
    // 액티비티 응답 결과 사용
    public static final String RESPONSE_OK = "response_ok";
    public static final String RESPONSE_CANCEL = "response_cancel";
    

    // 액티비티 스택을 모두 지우고 단일 액티비티 실행
    public static void startNewActivity(Context context, Class<?> c) {
        Intent intent = new Intent(context, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
    // Url 액티비티 실행 (웹브라우저 호출)
    public static void startUrlActivity(Context context, String url) {
        // url을 파싱해 적절한 액티비티 호출
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }
    // 기존 태스크에 액티비티 추가
    public static void startSingleActivity(Context context, Class<?> c) {
        Intent intent = new Intent(context, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static void startSingleActivityExtra(Context context, Class<?> c, String data) {
        Intent intent = new Intent(context, c);
        intent.putExtra(DATA, data);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
