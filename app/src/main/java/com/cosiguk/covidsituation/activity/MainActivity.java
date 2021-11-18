package com.cosiguk.covidsituation.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.databinding.ActivityMainBinding;
import com.cosiguk.covidsituation.databinding.CommonToolbarBinding;
import com.cosiguk.covidsituation.fragment.BoardFragment;
import com.cosiguk.covidsituation.fragment.NewsFragment;
import com.cosiguk.covidsituation.fragment.SituationBoardFragment;
import com.cosiguk.covidsituation.fragment.VaccineFragment;
import com.cosiguk.covidsituation.util.HandlerUtil;
import com.cosiguk.covidsituation.util.PackageUtil;

public class MainActivity extends BaseActivity implements BoardFragment.OnStatusColorUpdateListener {
    private ActivityMainBinding binding;
    private CommonToolbarBinding commonToolbarBinding;
    public Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        commonToolbarBinding = DataBindingUtil.bind(binding.commonToolbar.toolbar);

        initLayout();
        initListener();
    }

    private void initLayout() {
        // 툴바 초기화
        initCommonActionBarLayout(commonToolbarBinding, "코로나 상황판", true);
        // 하단바 초기화
        initNavigation();
        // 사이드바 초기화
        initSideBarWidth();
        initDrawerVersion();
    }

    private void initSideBarWidth() {
        // 모든 뷰에 영향을 미치는 layout 관련 속성을 가져옴 (width, height 등)
        ViewGroup.LayoutParams params = binding.sideView.getLayoutParams();
        // 아래의 코드는 다음과 같음 android:layout_width="현재 화면 너비 * 0.7"
        params.width = (int) (Resources.getSystem().getDisplayMetrics().widthPixels * 0.7);
        // 뷰에 레이아웃 적용
        binding.sideView.setLayoutParams(params);
    }

    private void initListener() {
        initDrawerToggle();
        initDrawerListener();
        initDrawerCloseButtonListener();
    };

    private void initDrawerVersion() {
        binding.commonSidebar.tvVersion.setText(PackageUtil.getAppVersion(MainActivity.this));
    }

    private void initDrawerListener() {
        binding.commonSidebar.loIntro.setOnClickListener(v -> {
            // Close Sidebar
            binding.loDrawer.closeDrawer(GravityCompat.START);
            HandlerUtil.activityDelay(MainActivity.this, IntroActivity.class, 200);
        });
        binding.commonSidebar.loNotice.setOnClickListener(v -> {
            // Close Sidebar
            binding.loDrawer.closeDrawer(GravityCompat.START);
            HandlerUtil.activityDelay(MainActivity.this, NoticeActivity.class, 200);
        });
    }

    private void initNavigation() {
        binding.navView.setOnItemSelectedListener(item -> {
            actionBarTitleChange(item.getItemId());
            bottomNavigate(item.getItemId());
            return true;
        });
        // 최초 프래그먼트 설정
        binding.navView.setSelectedItemId(R.id.nv_situation_board);
    }

    private void bottomNavigate(int id) {
        String tag = String.valueOf(id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        fragment = fragmentManager.findFragmentByTag(tag);
        // 미 생성된 프래그먼트 객체 생성
        if (fragment == null) {
            if (id == R.id.nv_situation_board) {
                // 사용자 목록을 전달하고 프래그먼트 생성
                fragment = SituationBoardFragment.newInstance();
            } else if (id == R.id.nv_news) {
                fragment = NewsFragment.newInstance();
            } else if (id == R.id.nv_vaccine) {
                fragment = VaccineFragment.newInstance();
            } else {
                fragment = new BoardFragment();
            }
            // 트랜잭션에 생성된 프래그먼트 추가
            fragmentTransaction.add(R.id.fragment, fragment, tag);
        } else {
            // 이미 생성된 프래그먼트는 바로 표시
            fragmentTransaction.show(fragment);
        }
        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commitNow();
    }

    private void actionBarTitleChange(int id) {
        if (id == R.id.nv_situation_board) {
            setCommonActionBarTitle(commonToolbarBinding, getResources().getString(R.string.location_situation_board));
        } else if (id == R.id.nv_news) {
            setCommonActionBarTitle(commonToolbarBinding, getResources().getString(R.string.location_news));
        } else if (id == R.id.nv_vaccine) {
            setCommonActionBarTitle(commonToolbarBinding, getResources().getString(R.string.location_vaccine));
        } else {
            setCommonActionBarTitle(commonToolbarBinding, getResources().getString(R.string.location_board));
        }
    }

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

    private void initDrawerCloseButtonListener() {
        binding.commonSidebar.ivNavClose.setOnClickListener(v -> binding.loDrawer.closeDrawer(GravityCompat.START));
    }

    // 프래그먼트에서 전송받은 상태바 색상
    @Override
    public void onUpdate(int color) {
        setStatusColor(color);
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