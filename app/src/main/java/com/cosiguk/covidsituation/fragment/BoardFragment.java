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

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.adapter.BoardListAdapter;
import com.cosiguk.covidsituation.application.MyApplication;
import com.cosiguk.covidsituation.databinding.FragmentBoardBinding;
import com.cosiguk.covidsituation.dialog.NoticeDialog;
import com.cosiguk.covidsituation.model.Board;
import com.cosiguk.covidsituation.network.resultInterface.BoardListener;

import java.util.ArrayList;

public class BoardFragment extends Fragment {
    private FragmentBoardBinding binding;
    private ArrayList<Board> boards;
    private BoardListAdapter adapter;
    private Parcelable recyclerViewState;

    public BoardFragment() {}

    public static BoardFragment newInstance() {
        return new BoardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board, container, false);
        initValue();
        requestNews();
        initRefreshListener();
        return binding.getRoot();
    }

    private void initValue() {
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BoardListAdapter(getActivity(), new ArrayList<>());
    }

    private void requestNews() {
        MyApplication.showProgressDialog(getActivity(), getResources().getString(R.string.progress_search));
        MyApplication
                .getNetworkPresenterInstance()
                .boardList(new BoardListener() {
                    @Override
                    public void success(ArrayList<Board> items) {
                        setBoardList(items);
                        initBoardLayout();
                        binding.loSwipe.setRefreshing(false);
                        MyApplication.dismissProgressDialog();
                    }

                    @Override
                    public void fail(String message) {
                        MyApplication.dismissProgressDialog();
                        binding.loSwipe.setRefreshing(false);
                        new NoticeDialog(getActivity())
                                .setMsg(message)
                                .show();
                    }
                });
    }

    private void setBoardList(ArrayList<Board> boardList) {
        // 뉴스 리스트 저장
        adapter.addAll(boardList);
    }

    private void initBoardLayout() {
        // 스크롤 상태 저장
        recyclerViewState = binding.recyclerview.getLayoutManager().onSaveInstanceState();
        binding.recyclerview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // 스크롤이 끝에 도달했는지 확인
                if (!binding.recyclerview.canScrollVertically(1)) {
                    Log.d("onStage()", adapter.getItemCount() + "");
                }
            }
        });
        binding.recyclerview.setAdapter(adapter);
        // 스크롤 상태 복구
        binding.recyclerview.getLayoutManager().onRestoreInstanceState(recyclerViewState);
    }

    private void initRefreshListener() {
        binding.loSwipe.setOnRefreshListener(()->{
            adapter.setItemListEmpty();
            requestNews();
        });
    }
}