package com.cosiguk.covidsituation.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

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

public class NewsFragment extends Fragment {
    private FragmentNewsBinding binding;
    private ArrayList<News> newsList;

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

        requestNews();
        initRefreshListener();
        return binding.getRoot();
    }

    private void requestNews() {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("X-Naver-Client-Id", BuildConfig.CLIENT_ID);
        headerMap.put("X-Naver-Client-Secret", BuildConfig.CLIENT_SECRET);

        HashMap<String, Object> queries = new HashMap<>();
        queries.put("query", "코로나");
        queries.put("sort", "sim");
        queries.put("display", 100);

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
        // 뉴스 리스트 저장
        this.newsList = newsList;
        // 뉴스 날짜 초기화 (커스텀 날짜)
        for (int i = 0; i < this.newsList.size(); i++) {
            this.newsList.get(i).setPubDate(ConvertUtil.convertDateBar(this.newsList.get(i).getPubDate()));
        }
        // 뉴스 리스트 정렬 (날짜, 시간 순)
        this.newsList.sort(new NewsComparator());
    }

    private void initRefreshListener() {
        binding.loSwipe.setOnRefreshListener(()->{
            requestNews();

            BasicUtil.showSnackBar(
                    getActivity(),
                    requireActivity().getWindow().getDecorView().getRootView(),
                    "새로고침 완료");
            // 새로 고침 완료
            binding.loSwipe.setRefreshing(false);
        });
    }

    private void initNewsLayout() {
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        NewsAdapter adapter = new NewsAdapter(newsList);
        adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                ActivityUtil.startUrlActivity(requireActivity(), newsList.get(pos).getLink());
            }
        });
        binding.recyclerview.setAdapter(adapter);
    }
}