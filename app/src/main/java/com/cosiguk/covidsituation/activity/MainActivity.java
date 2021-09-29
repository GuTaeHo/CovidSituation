package com.cosiguk.covidsituation.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.databinding.ActivityMainBinding;
import com.cosiguk.covidsituation.databinding.CommonToolbarBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private CommonToolbarBinding commonToolbarBinding;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        commonToolbarBinding = DataBindingUtil.bind(binding.commonToolbar.toolbar);
        width = Resources.getSystem().getDisplayMetrics().widthPixels;
        initLayout();
    }

    private void initLayout() {
        // 사이드바 너비 지정
        initSideBarWidth();
        initCommonActionBarLayout(commonToolbarBinding, "코로나 상황판", true);
        initListener();
    }

    private void initSideBarWidth() {
        // 모든 뷰에 영향을 미치는 layout 관련 속성을 가져옴 (width, height 등)
        ViewGroup.LayoutParams params = binding.navView.getLayoutParams();
        // 아래의 코드는 다음과 같음 android:layout_width="현재 화면 너비"
        params.width = width;
        // 뷰에 레이아웃 적용
        binding.navView.setLayoutParams(params);
    }

    private void initListener() {
        initDrawerToggle();
    };

    // 사이드바 리스너 등록
    private void initDrawerToggle() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.loDrawer, commonToolbarBinding.toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        binding.loDrawer.addDrawerListener(actionBarDrawerToggle);
    }

    @Override
    public void onBackPressed() {
        if (binding.loDrawer.isDrawerOpen(GravityCompat.START)) {
            binding.loDrawer.closeDrawer(GravityCompat.START);
        } else {
            backPressCloseHandler.onBackPressed();
        }
    }
}