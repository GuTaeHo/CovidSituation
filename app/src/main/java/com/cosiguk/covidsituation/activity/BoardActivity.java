package com.cosiguk.covidsituation.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.databinding.ActivityBoardBinding;
import com.cosiguk.covidsituation.databinding.CloseToolbarBinding;
import com.cosiguk.covidsituation.dialog.NoticeDialog;
import com.cosiguk.covidsituation.model.Board;
import com.cosiguk.covidsituation.network.resultInterface.BoardDeprecateListener;
import com.cosiguk.covidsituation.network.resultInterface.BoardDetailListener;
import com.cosiguk.covidsituation.network.resultInterface.BoardRecommendListener;
import com.cosiguk.covidsituation.util.ActivityUtil;

public class BoardActivity extends BaseActivity {
    private final int SUCCESS = 0;
    private ActivityBoardBinding binding;
    private CloseToolbarBinding commonToolbarBinding;
    private Board item;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(BoardActivity.this, R.layout.activity_board);
        commonToolbarBinding = DataBindingUtil.bind(binding.closeToolbar.toolbar);

        initValue();
        initEvent();
        requestBoard();
    }

    private void initValue() {
        userID = getIntent().getIntExtra(ActivityUtil.NOTICE_ID, -1);
    }

    private void initLayout() {
        commonToolbarBinding.toolbarTitle.setText(getResources().getString(R.string.bd_toolbar));
        commonToolbarBinding.toolbar.setBackgroundColor(getResources().getColor(R.color.app_background, null));
        binding.tvTitle.setText(item.getTitle());
        binding.tvNickname.setText(item.getNickname());
        binding.tvDate.setText(item.getCreatedDate());
        binding.tvHit.setText(String.format("조회수 %s", item.getHit()));
        binding.tvContent.setText(item.getContent());
        binding.tvRecommendCount.setText(String.format("%s", item.getRecommend()));
        binding.tvDeprecateCount.setText(String.format("%s", item.getDeprecate()));
    }

    private void initEvent() {
        commonToolbarBinding.ivLeave.setOnClickListener(v -> {
            setResult(Activity.RESULT_OK);
            finish();
        });
        binding.loRecommend.setOnClickListener(v -> requestLike());
        binding.loDeprecate.setOnClickListener(v -> requestUnLike());
    }

    // 인터넷 연결이 끊어졌을 경우 레이아웃 초기화
    private void setEmptyLayout() {
        commonToolbarBinding.toolbarTitle.setText(getResources().getString(R.string.bd_toolbar));
        commonToolbarBinding.toolbar.setBackgroundColor(getResources().getColor(R.color.app_background, null));
        binding.loBoardDetail.setVisibility(View.GONE);
        binding.tvNetworkError.setVisibility(View.VISIBLE);
    }

    private void requestBoard() {
        showProgressDialog(BoardActivity.this, getResources().getString(R.string.progress_board_detail));
        networkPresenter
                .boardDetail(userID, new BoardDetailListener() {
                    @Override
                    public void success(Board board) {
                        setBoardItem(board);
                        initLayout();
                        dismissProgressDialog();
                    }

                    @Override
                    public void fail(String message) {
                        dismissProgressDialog();
                        setEmptyLayout();
                    }
                });
    }

    private void requestLike() {
        networkPresenter
                .boardRecommend(userID, new BoardRecommendListener() {
                    @Override
                    public void success(int code) {
                        if (code == SUCCESS) {
                            binding.loRecommend.setBackground(AppCompatResources.getDrawable(BoardActivity.this, R.drawable.view_round_positive_clicked));
                            binding.tvRecommendCount.setText(String.format("%s", item.getRecommend() + 1));
                        }
                    }

                    @Override
                    public void fail(String message) {
                        new NoticeDialog(BoardActivity.this)
                                .setMsg(message)
                                .setShowNegativeButton(false)
                                .show();
                    }
                });
    }

    private void requestUnLike() {
        networkPresenter
                .boardDeprecate(userID, new BoardDeprecateListener() {
                    @Override
                    public void success(int code) {
                        if (code == SUCCESS) {
                            binding.loDeprecate.setBackground(AppCompatResources.getDrawable(BoardActivity.this, R.drawable.view_round_negative_clicked));
                            binding.tvDeprecateCount.setText(String.format("%s", item.getDeprecate() + 1));
                        }
                    }

                    @Override
                    public void fail(String message) {
                        new NoticeDialog(BoardActivity.this)
                                .setMsg(message)
                                .setShowNegativeButton(false)
                                .show();
                    }
                });
    }

    private void setBoardItem(Board board) {
        item = board;
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        finish();
    }
}