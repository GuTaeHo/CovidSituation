package com.cosiguk.covidsituation.application;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import androidx.appcompat.app.AppCompatDelegate;

import com.cosiguk.covidsituation.BuildConfig;
import com.cosiguk.covidsituation.dialog.ProgressDialog;
import com.cosiguk.covidsituation.network.NetworkPresenter;
import com.cosiguk.covidsituation.util.NaverMapUtil;
import com.cosiguk.covidsituation.util.ProgressDialogManager;
import com.naver.maps.map.NaverMapSdk;

public class MyApplication extends Application {
    protected static ProgressDialog progressDialog;

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setServiceID();
    }

    // 내부클래스에서 static 변수를 선언해야하는 경우 static 내부 클래스를 선언해야함
    private static class InitMyApplication {
        // 클래스 로딩 시점에서 생성
        private static final NetworkPresenter networkPresenter = new NetworkPresenter();
    }

    public static NetworkPresenter getNetworkPresenterInstance() {
        return InitMyApplication.networkPresenter;
    }

    public static void showProgressDialog(Context context, String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
        }
        progressDialog.setMessage(message).show();
    }

    public static void setProgressDialogMessage(String message) {
        if (progressDialog != null) {
            progressDialog.setMessage(message);
        }
    }

    // 다이얼로그 삭제
    public static void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    // 네이버 Map API ID 등록
    public void setServiceID() {
        NaverMapSdk.getInstance(this).setClient(
                new NaverMapSdk.NaverCloudPlatformClient(BuildConfig.MAP_ID));
    }
}
