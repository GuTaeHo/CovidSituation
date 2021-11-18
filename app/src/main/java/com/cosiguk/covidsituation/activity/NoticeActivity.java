package com.cosiguk.covidsituation.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.adapter.NoticeAdapter;
import com.cosiguk.covidsituation.databinding.ActivityNoticeBinding;
import com.cosiguk.covidsituation.databinding.CloseToolbarBinding;
import com.cosiguk.covidsituation.dialog.NoticeDialog;
import com.cosiguk.covidsituation.model.Notice;
import com.cosiguk.covidsituation.network.resultInterface.NoticeListener;
import com.cosiguk.covidsituation.util.AnimationUtil;

import java.util.ArrayList;

public class NoticeActivity extends BaseActivity {
    private static final int RECYCLER_VIEW_TOP = 0;
    private ActivityNoticeBinding binding;
    private CloseToolbarBinding closeToolbarBinding;
    private ArrayList<Notice> notices;
    private NoticeAdapter adapter;
    // 상태 저장
    private Parcelable recyclerViewState;
    // 최상단 이동 버튼
    private boolean buttonState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(NoticeActivity.this, R.layout.activity_notice);
        closeToolbarBinding = DataBindingUtil.bind(binding.closeToolbar.toolbar);

        initValues();
        initLayout();
        requestNotice();
        initEvent();
    }

    private void initValues() {
        buttonState = false;
        adapter = new NoticeAdapter();
    }

    private void initLayout() {
        setStatusColor(getColor(R.color.status_white));
        closeToolbarBinding.toolbarTitle.setText(getResources().getString(R.string.side_contents_notice_title));
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(NoticeActivity.this));
    }

    private void requestNotice() {
        showProgressDialog(NoticeActivity.this, getResources().getString(R.string.progress_search));
        networkPresenter
                .notice(new NoticeListener() {
                    @Override
                    public void success(ArrayList<Notice> list) {
                        setNoticeList(list);
                        initNoticeLayout();
                        dismissProgressDialog();
                        // 새로 고침 완료
                        binding.loSwipe.setRefreshing(false);
                    }

                    @Override
                    public void fail(String message) {
                        new NoticeDialog(NoticeActivity.this)
                                .setMsg(message)
                                .show();
                        // 새로 고침 완료
                        binding.loSwipe.setRefreshing(false);
                        dismissProgressDialog();
                    }
                });
    }

    private void setNoticeList(ArrayList<Notice> list) {
        adapter.addAll(list);
    }

    private void initEvent() {
        binding.tvButton.setOnClickListener(v -> {
            binding.recyclerview.smoothScrollToPosition(RECYCLER_VIEW_TOP);
        });
        closeToolbarBinding.ivLeave.setOnClickListener(v -> {
            setStatusDefaultColor();
            finish();
        });
        initRefreshListener();
    }

    private void initNoticeLayout() {
        // 스크롤 상태 저장
        recyclerViewState = binding.recyclerview.getLayoutManager().onSaveInstanceState();
        binding.recyclerview.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            // 버튼 상태 변환
            if (!binding.recyclerview.canScrollVertically(-1)) {
                AnimationUtil.setAnimationGone(binding.tvButton, 0.0f, 100);
            } else {
                AnimationUtil.setAnimationVisible(binding.tvButton, 1.0f, 100);
            }
        });
        binding.recyclerview.setAdapter(adapter);
        // 스크롤 상태 복구
        binding.recyclerview.getLayoutManager().onRestoreInstanceState(recyclerViewState);
    }

    private void initRefreshListener() {
        binding.loSwipe.setOnRefreshListener(()->{
            // 리스트 초기화
            adapter.setItemListEmpty();
            requestNotice();
        });
    }

    @Override
    public void onBackPressed() {
        setStatusDefaultColor();
        finish();
    }
}