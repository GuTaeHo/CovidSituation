package com.cosiguk.covidsituation.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.activity.BoardActivity;
import com.cosiguk.covidsituation.adapter.BaseRecyclerViewAdapter;
import com.cosiguk.covidsituation.adapter.BoardListAdapter;
import com.cosiguk.covidsituation.application.MyApplication;
import com.cosiguk.covidsituation.databinding.FragmentBoardBinding;
import com.cosiguk.covidsituation.dialog.NoticeDialog;
import com.cosiguk.covidsituation.model.Board;
import com.cosiguk.covidsituation.network.resultInterface.BoardListener;
import com.cosiguk.covidsituation.util.ActivityUtil;

import java.util.ArrayList;

public class BoardFragment extends Fragment {
    private FragmentBoardBinding binding;
    private BoardListAdapter adapter;
    private Context context;
    private ActivityResultLauncher<Intent> resultLauncher;

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

        initLayout();
        resultActivityLauncher();
        initRefreshListener();
        requestBoard();
        return binding.getRoot();
    }

    private void requestBoard() {
        MyApplication.showProgressDialog(getActivity(), getResources().getString(R.string.progress_search));
        MyApplication
                .getNetworkPresenterInstance()
                .boardList(new BoardListener() {
                    @Override
                    public void success(ArrayList<Board> items) {
                        adapter.addItems(items);
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

    private void initLayout() {
        context = getActivity();
        adapter = new BoardListAdapter(context);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // 스크롤이 끝에 도달했는지 확인
                if (!binding.recyclerview.canScrollVertically(1)) {
                    Log.d("onStage()", adapter.getItemCount() + "");
                }
            }
        });
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), BoardActivity.class);
                intent.putExtra(ActivityUtil.NOTICE_ID, adapter.getItem(position).getId());
                resultLauncher.launch(intent);
            }
        });
        binding.recyclerview.setAdapter(adapter);
    }

    private void initRefreshListener() {
        binding.loSwipe.setOnRefreshListener(()->{
            adapter.clear();
            requestBoard();
        });
    }

    private void resultActivityLauncher() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContract<Intent, Object>() {
                    @NonNull
                    @Override
                    public Intent createIntent(@NonNull Context context, Intent input) {
                        input.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        return input;
                    }

                    @Override
                    public Object parseResult(int resultCode, @Nullable Intent intent) {
                        if (resultCode == Activity.RESULT_OK) {
                            return ActivityUtil.RESPONSE_OK;
                        } else
                            return ActivityUtil.RESPONSE_CANCEL;
                    }
                },
                new ActivityResultCallback<Object>() {
                    @Override
                    public void onActivityResult(Object result) {
                        if (result.equals(ActivityUtil.RESPONSE_OK)) {
                            adapter.clear();
                            requestBoard();
                        }
                    }
                });
    }
}