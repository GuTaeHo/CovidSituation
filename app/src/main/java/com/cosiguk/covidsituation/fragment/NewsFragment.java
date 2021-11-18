package com.cosiguk.covidsituation.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cosiguk.covidsituation.BuildConfig;
import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.adapter.NewsAdapter;
import com.cosiguk.covidsituation.application.MyApplication;
import com.cosiguk.covidsituation.databinding.FragmentNewsBinding;
import com.cosiguk.covidsituation.dialog.NoticeDialog;
import com.cosiguk.covidsituation.model.News;
import com.cosiguk.covidsituation.network.resultInterface.NewsListener;
import com.cosiguk.covidsituation.util.ActivityUtil;
import com.cosiguk.covidsituation.util.ConvertUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewsFragment extends Fragment {
    private FragmentNewsBinding binding;
    private NewsAdapter adapter;
    private List<String> list;
    private Parcelable recyclerViewState;
    // 프로그래스 디스플레이 상태
    private boolean displayProgress;
    // 뉴스 인덱스
    int index;

    public NewsFragment() {}

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false);
        initValue();
        // API 검색 시작 위치 초기화
        requestNews(index);
        initRefreshListener();
        return binding.getRoot();
    }

    private void initValue() {
        displayProgress = true;
        index = 1;
        // 어댑터 생성
        adapter = new NewsAdapter();
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void requestNews(int index) {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Naver-Client-Id", BuildConfig.CLIENT_ID);
        headerMap.put("X-Naver-Client-Secret", BuildConfig.CLIENT_SECRET);

        HashMap<String, Object> queries = new HashMap<>();
        queries.put("query", "코로나");
        queries.put("sort", "sim");
        queries.put("display", 20);
        queries.put("start", index);

        if (displayProgress) MyApplication.showProgressDialog(getActivity(), getResources().getString(R.string.progress_search));
        MyApplication
                .getNetworkPresenterInstance()
                .news(headerMap, queries, new NewsListener() {
                    @Override
                    public void success(ArrayList<News> list) {
                        // 새로 고침 완료
                        binding.loSwipe.setRefreshing(false);
                        setNewsList(list);
                        initNewsLayout();
                        if (displayProgress) {
                            MyApplication.dismissProgressDialog();
                            displayProgress = false;
                        };
                    }

                    @Override
                    public void fail(String message) {
                        // 새로 고침 완료
                        binding.loSwipe.setRefreshing(false);
                        new NoticeDialog(getActivity())
                                .setMsg(message)
                                .show();
                        if (displayProgress) {
                            MyApplication.dismissProgressDialog();
                            displayProgress = false;
                        };
                    }
                });
    }

    private void setNewsList(ArrayList<News> newsList) {
        // 날짜 형식 변환
        newsList.forEach(item -> {
            item.setPubDate(ConvertUtil.convertDateBar(item.getPubDate()));
        });
        // 뉴스 리스트 저장
        adapter.addAll(newsList);
    }

    private void initNewsLayout() {
        // 스크롤 상태 저장
        recyclerViewState = binding.recyclerview.getLayoutManager().onSaveInstanceState();
        binding.recyclerview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                // 스크롤이 끝에 도달했는지 확인
                if (!binding.recyclerview.canScrollVertically(1)) {
                    Log.d("onStage()", adapter.getItemCount() + "");
                    // 검색 위치 조정
                    index = index + 20;
                    if (index <= 100) {
                        requestNews(index);
                    }
                }
            }
        });

        adapter.setOnItemClickListener((v, pos) -> ActivityUtil.startUrlActivity(requireActivity(), adapter.getList().get(pos).getLink()));
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
            requestNews(index);
        });
    }
}