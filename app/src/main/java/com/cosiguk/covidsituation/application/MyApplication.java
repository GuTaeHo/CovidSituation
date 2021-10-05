package com.cosiguk.covidsituation.application;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.cosiguk.covidsituation.network.NetworkPresenter;


public class MyApplication extends Application {
    public static NetworkPresenter networkPresenter;

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        networkPresenter = new NetworkPresenter();
    }
}
