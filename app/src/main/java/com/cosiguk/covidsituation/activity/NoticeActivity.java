package com.cosiguk.covidsituation.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.adapter.NewsAdapter;
import com.cosiguk.covidsituation.adapter.NoticeAdapter;
import com.cosiguk.covidsituation.application.MyApplication;
import com.cosiguk.covidsituation.databinding.ActivityNoticeBinding;
import com.cosiguk.covidsituation.databinding.CloseToolbarBinding;
import com.cosiguk.covidsituation.dialog.NoticeDialog;
import com.cosiguk.covidsituation.model.Notice;
import com.cosiguk.covidsituation.network.resultInterface.NoticeListener;
import com.cosiguk.covidsituation.util.ActivityUtil;

import java.util.ArrayList;

public class NoticeActivity extends BaseActivity {
    private ActivityNoticeBinding binding;
    private CloseToolbarBinding closeToolbarBinding;
    private ArrayList<Notice> notices;
    private NoticeAdapter adapter;
    // 상태 저장
    private Parcelable recyclerViewState;
    // 검색 위치
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(NoticeActivity.this, R.layout.activity_notice);
        closeToolbarBinding = DataBindingUtil.bind(binding.closeToolbar.toolbar);

        initValues();
        initLayout();
        requestNotice(index);
        initEvent();
    }

    private void initValues() {
        index = 1;
        adapter = new NoticeAdapter();
    }

    private void initLayout() {
        closeToolbarBinding.toolbar.setBackground(ResourcesCompat.getDrawable(getResources(), R.color.app_background, null));
        closeToolbarBinding.toolbarTitle.setText(getResources().getString(R.string.side_contents_notice_title));
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(NoticeActivity.this));
    }

    private void requestNotice(int index) {
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
        closeToolbarBinding.ivLeave.setOnClickListener(v -> finish());
        initRefreshListener();
    }

    private void initNoticeLayout() {
        // 스크롤 상태 저장
        recyclerViewState = binding.recyclerview.getLayoutManager().onSaveInstanceState();
        binding.recyclerview.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            // 스크롤이 끝에 도달했는지 확인
            if (!binding.recyclerview.canScrollVertically(1)) {
                Log.d("onStage()", adapter.getItemCount() + "");
//                    // 검색 위치 조정
//                    index = index + 20;
//                    if (index <= 100) {
//                        requestNotice(index);
//                    }
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
            // 인덱스 초기화
            index = 1;
            requestNotice(index);
        });
    }
}