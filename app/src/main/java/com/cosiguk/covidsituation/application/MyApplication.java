package com.cosiguk.covidsituation.application;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.cosiguk.covidsituation.network.NetworkPresenter;


public class MyApplication extends Application {

    private MyApplication() {}

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    // 내부클래스에서 static 변수를 선언해야하는 경우 static 내부 클래스를 선언해야만 한다.
    private static class InitMyApplication {
        // 클래스 로딩 시점에서 생성
        private static final NetworkPresenter networkPresenter = new NetworkPresenter();
    }

    public static NetworkPresenter getNetworkPresenterInstance() {
        return InitMyApplication.networkPresenter;
    }
}
