package com.cosiguk.covidsituation.application;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferencesManager {
    private static MyPreferencesManager INSTANCE;

    private final String preferenceName;
    private final SharedPreferences.Editor editor;
    private final Context context;

    private static final int PRIVATE_MODE = 0;

    // 로그인 정보
    private static final String PROVINCES = "provinces";
    private static final String ID = "user_id";
    private static final String IS_AUTO_LOGIN = "autoLogin";

    private MyPreferencesManager(Context context) {
        this.context = context;
        preferenceName = context.getPackageName();
        // 패키지명으로된 sharedPreferences 생성, 다른 앱에서 접근할 수 없도록 PRIVATE 로 설정
        SharedPreferences sharedPreferences = this.context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        // 값 조회, 저장을 위해서 edit() 메서드 사용
        editor = sharedPreferences.edit();
    }

    // synchronized 키워드를 사용해 getInstance() 메소드를 임계구역으로 설정해
    // 한번에 한 스레드만 접근이 가능하도록 설정
    public static synchronized MyPreferencesManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MyPreferencesManager(context);
        }
        return INSTANCE;
    }

    /*                   로그인 정보                   */
    // IS_AUTO_LOGIN 키의 값을 반환 (기본값 : false)
    public boolean isAutoLogin() {
        return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE).getBoolean(IS_AUTO_LOGIN, false);
    }
    public void setAutoLogin(boolean b) {
        if (b) {
            editor.putBoolean(IS_AUTO_LOGIN, true);
        } else {
            editor.putBoolean(IS_AUTO_LOGIN, false);
        }

        // sharedPreference 객체에 설정한 값 저장 [apply() 또는 commit() 등을 사용]
        editor.apply();
    }

    public void setId(String id) {
        editor.putString(ID, id);
        editor.apply();
    }

    public String getId() {
        return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE).getString(ID, "");
    }
}
