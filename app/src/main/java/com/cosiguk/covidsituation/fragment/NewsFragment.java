package com.cosiguk.covidsituation.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
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
import com.cosiguk.covidsituation.util.BasicUtil;
import com.cosiguk.covidsituation.util.ConvertUtil;
import com.cosiguk.covidsituation.util.NewsComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class NewsFragment extends Fragment {
    private FragmentNewsBinding binding;
    private NewsAdapter adapter;
    private List<String> list;

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
        index = 1;
        // 어댑터 생성
        adapter = new NewsAdapter();
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

        MyApplication
                .networkPresenter
                .news(headerMap, queries, new NewsListener() {
                    @Override
                    public void success(ArrayList<News> list) {
                        setNewsList(list);
                        initNewsLayout();
                    }

                    @Override
                    public void fail(String message) {
                        new NoticeDialog(getActivity())
                                .setMsg(message)
                                .show();
                    }
                });
    }

    private void setNewsList(ArrayList<News> newsList) {
        // 날짜 형식 변환
        newsList.forEach(item -> {
            item.setPubDate(ConvertUtil.convertDateBar(item.getPubDate()));
        });
        Log.d("debuggingList", "addAll 실행");
        // 뉴스 리스트 저장
        adapter.addAll(newsList);
        Log.d("debuggingList", "sort 실행");
        // 뉴스 리스트 정렬 (날짜, 시간 순)
        // adapter.sort();
    }

    private void initNewsLayout() {
        Log.d("debuggingList", "initNewsLayout 실행");
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                // 스크롤이 끝에 도달했는지 확인
                if (!binding.recyclerview.canScrollVertically(1)) {
                    Log.d("debuggingList", "onScrollChange 실행");
                    // 검색 위치 조정
                    index = index + 20;
                    if (index <= 100) {
                        Log.d("scroll", index+", "+adapter.getItemCount());
                        requestNews(index);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                binding.recyclerview.scrollToPosition(adapter.getItemCount() - 11);
                            }
                        }, 200);
                    }
                }
            }
        });

        adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                ActivityUtil.startUrlActivity(requireActivity(), adapter.getList().get(pos).getLink());
            }
        });
        binding.recyclerview.setAdapter(adapter);
    }

    private void initRefreshListener() {
        binding.loSwipe.setOnRefreshListener(()->{
            // 리스트 초기화
            adapter.setItemListEmpty();
            // 인덱스 초기화
            index = 1;

            requestNews(index);

            BasicUtil.showSnackBar(
                    getActivity(),
                    requireActivity().getWindow().getDecorView().getRootView(),
                    "새로고침 완료");
            // 새로 고침 완료
            binding.loSwipe.setRefreshing(false);
        });
    }
}