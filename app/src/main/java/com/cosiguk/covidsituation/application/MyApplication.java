package com.cosiguk.covidsituation.application;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import androidx.appcompat.app.AppCompatDelegate;

import com.cosiguk.covidsituation.dialog.ProgressDialog;
import com.cosiguk.covidsituation.network.NetworkPresenter;
import com.cosiguk.covidsituation.util.ProgressDialogManager;

public class MyApplication extends Application {
    protected static ProgressDialog progressDialog;

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    // 내부클래스에서 static 변수를 선언해야하는 경우 static 내부 클래스를 선언해야함
    private static class InitMyApplication {
        // 클래스 로딩 시점에서 생성
        private static final NetworkPresenter networkPresenter = new NetworkPresenter();
    }

    public static NetworkPresenter getNetworkPresenterInstance() {
        return InitMyApplication.networkPresenter;
    }

    // 다이얼로그 출력
    public static void showProgressDialog(Context context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
        }
        // 모서리 배경제거
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();
    }

    // 다이얼로그 삭제
    public static void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
