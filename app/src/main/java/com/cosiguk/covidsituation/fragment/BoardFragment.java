package com.cosiguk.covidsituation.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    private BaseRecyclerViewAdapter.OnItemClickListener reportListener;

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
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onItemClick(View view, int position) {
                Log.d("board", "clicked");
                ActivityUtil.startSingleActivityExtra(context, BoardActivity.class, adapter.getItem(position).getId());
                /*
                switch (view.getId()) {
                    case R.id.container:
                        Log.d("board", "container clicked : " + adapter.getItem(position).getTitle());
                        ActivityUtil.startSingleActivityExtra(context, BoardActivity.class, adapter.getItem(position).getId());
                        break;
                    case R.id.tv_board_like:
                        Log.d("board", "like clicked");
                        requestLike();
                        break;
                    case R.id.tv_board_unlike:
                        Log.d("board", "unlike clicked");
                        requestUnLike();
                        break;
                    case R.id.tv_board_report:
                        Log.d("board", "report clicked");
                        requestReport(adapter.getItem(position));
                        break;
                    case R.id.tv_board_delete:
                        Log.d("board", "delete clicked");
                        requestDelete();
                        break;
                    default:
                        Log.d("board", "default clicked");
                        break;
                }

                 */
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

    private void requestLike() {

    }

    private void requestUnLike() {

    }

    private void requestReport(Board item) {
        new NoticeDialog(context)
                .setMsg(getResources().getString(R.string.board_report_notice))
                .setPositiveMsg(getResources().getString(R.string.dialog_yes))
                .setNegativeMsg(getResources().getString(R.string.dialog_no))
                .setNoticeDialogCallbackListener(new NoticeDialog.NoticeDialogCallbackListener() {
                    @Override
                    public void positive() {
                        Toast.makeText(context, "게시글 : " + item.getTitle() + " 신고 완료", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void negative() {}
                }).show();
    }

    private void requestDelete() {

    }
}