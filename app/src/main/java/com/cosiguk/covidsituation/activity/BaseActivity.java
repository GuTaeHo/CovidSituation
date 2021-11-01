package com.cosiguk.covidsituation.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.application.MyPreferencesManager;
import com.cosiguk.covidsituation.databinding.CommonToolbarBinding;
import com.cosiguk.covidsituation.dialog.ProgressDialog;
import com.cosiguk.covidsituation.network.NetworkPresenter;
import com.cosiguk.covidsituation.util.BackPressCloseHandler;
import com.cosiguk.covidsituation.util.ProgressDialogManager;


public class BaseActivity extends AppCompatActivity {
    protected MyPreferencesManager preferencesManager;
    protected NetworkPresenter networkPresenter;
    protected ProgressDialog progressDialog;
    // protected ProgressDialog progressDialog;
    protected BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferencesManager = MyPreferencesManager.getInstance(this);
        networkPresenter = new NetworkPresenter();
        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    protected void initCommonActionBarLayout(CommonToolbarBinding commonToolbarBinding, String title, boolean setBackButton) {
        // 액티비티에 액션바 부착
        setSupportActionBar(commonToolbarBinding.toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            // 뒤로가기 설정
            actionBar.setDisplayHomeAsUpEnabled(setBackButton);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
            // 앱바 타이틀 숨김
            actionBar.setDisplayShowTitleEnabled(false);
            commonToolbarBinding.toolbarTitle.setText(title);
        }
    }

    protected void setCommonActionBarTitle(CommonToolbarBinding commonToolbarBinding, String title) {
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            commonToolbarBinding.toolbarTitle.setText(title);
        }
    };

    // 다이얼로그 출력
    public void showProgressDialog(Context context, String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
        }
        progressDialog.setMessage(message).show();
    }

    // 다이얼로그 삭제
    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /*
    protected void showProgressDialog(String message) {
        progressDialog = ProgressDialogManager.showSingle(this, progressDialog, "", message);
    }

    protected void dismissProgressDialog() {
        ProgressDialogManager.dismiss(progressDialog);
    }
     */

    public void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
