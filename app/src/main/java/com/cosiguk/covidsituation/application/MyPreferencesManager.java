package com.cosiguk.covidsituation.application;

import android.content.Context;
import android.content.SharedPreferences;

import com.cosiguk.covidsituation.model.Notice;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class MyPreferencesManager {
    private static MyPreferencesManager INSTANCE;

    private final String preferenceName;
    private final SharedPreferences.Editor editor;
    private final Context context;
    private final Gson gson;

    private static final String PROVINCES = "provinces";
    private static final String ID = "user_id";
    private static final String IS_AUTO_LOGIN = "autoLogin";
    private static final String IS_FIRST_LAUNCH = "firstLaunch";
    private static final String NOTICES = "notices";
    private static final String NEW_NOTICES = "new_notices";

    private MyPreferencesManager(Context context) {
        this.context = context;
        preferenceName = context.getPackageName();
        // 패키지명으로된 sharedPreferences 생성, 다른 앱에서 접근할 수 없도록 PRIVATE 로 설정
        SharedPreferences sharedPreferences = this.context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        // 값 조회, 저장을 위해서 edit() 메서드 사용
        editor = sharedPreferences.edit();
        // 객체를 직렬, 역직렬화 하기 위한 Gson 객체 초기화
        gson = new GsonBuilder().create();
    }

    // synchronized 키워드를 사용해 getInstance() 메소드를 임계구역으로 설정해
    // 한번에 한 스레드만 접근이 가능하도록 설정
    public static synchronized MyPreferencesManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MyPreferencesManager(context);
        }
        return INSTANCE;
    }

    // 앱 최초 실행 여부 반환
    public boolean isFirstLaunch() {
        return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE).getBoolean(IS_FIRST_LAUNCH, true);
    }

    public void setFirstLaunch(boolean b) {
        if (b) {
            editor.putBoolean(IS_FIRST_LAUNCH, true);
        } else {
            editor.putBoolean(IS_FIRST_LAUNCH, false);
        }

        editor.apply();
    }

    public boolean isAutoLogin() {
        return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE).getBoolean(IS_AUTO_LOGIN, false);
    }

    public void setAutoLogin(boolean b) {
        if (b) {
            editor.putBoolean(IS_AUTO_LOGIN, true);
        } else {
            editor.putBoolean(IS_AUTO_LOGIN, false);
        }

        editor.apply();
    }

    public void setId(String id) {
        editor.putString(ID, id);
        editor.apply();
    }

    public String getId() {
        return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE).getString(ID, "");
    }

    // 공지사항 리스트 저장
    public void setNotices(ArrayList<Notice> notices) {
        // 기존 정보 삭제
        editor.remove(NOTICES).apply();
        editor.putString(NOTICES, gson.toJson(notices));
        editor.apply();
    }

    // 공지사항 추가
    public void addNotice(Notice notice) {
        editor.putString(NOTICES, gson.toJson(notice));
        editor.apply();
    }

    // 공지사항 초기화
    public void initNotices() {
        editor.remove(NOTICES);
        editor.apply();
    }
    
    // 새 공지 저장
    public void setNewNotices() {
        
    }

    public void initNewNotices() {

    }

    public void getNewNotices() {

    }

    // 공지사항 리스트 반환
    public ArrayList<Notice> getNotices() {
        String noticesString = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE).getString(NOTICES, "");

        // string 형태의 리스트객체를 Array<Notice> 형태로 변환 하여 반환
        return gson.fromJson(noticesString, new TypeToken<ArrayList<Notice>>() {}.getType());
    }
}
