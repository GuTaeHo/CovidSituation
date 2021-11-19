package com.cosiguk.covidsituation.activity;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.adapter.ChatAdapter;
import com.cosiguk.covidsituation.databinding.ActivityBoardBinding;
import com.cosiguk.covidsituation.databinding.CloseToolbarBinding;
import com.cosiguk.covidsituation.dialog.NoticeDialog;
import com.cosiguk.covidsituation.model.Board;
import com.cosiguk.covidsituation.network.resultInterface.BoardDeprecateListener;
import com.cosiguk.covidsituation.network.resultInterface.BoardDetailListener;
import com.cosiguk.covidsituation.network.resultInterface.BoardRecommendListener;
import com.cosiguk.covidsituation.network.resultInterface.ChatAddListener;
import com.cosiguk.covidsituation.network.resultInterface.ChatListener;
import com.cosiguk.covidsituation.util.ActivityUtil;
import com.cosiguk.covidsituation.util.PatternUtil;
import com.cosiguk.covidsituation.util.ViewUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class BoardActivity extends BaseActivity {
    private final int SUCCESS = 0;
    private ActivityBoardBinding binding;
    private CloseToolbarBinding commonToolbarBinding;
    private ChatAdapter adapter;
    private Board item;
    private int userID;
    private Animation shake;

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
        adapter = new ChatAdapter(BoardActivity.this);
        shake = AnimationUtils.loadAnimation(this, R.anim.anim_shake);
    }

    private void initLayout() {
        setStatusColor(getColor(R.color.status_white));
        commonToolbarBinding.toolbarTitle.setText(getResources().getString(R.string.bd_toolbar));
        binding.tvTitle.setText(item.getTitle());
        binding.tvNickname.setText(item.getNickname());
        binding.tvDate.setText(item.getCreatedDate());
        binding.tvHit.setText(String.format("조회수 %s", item.getHit()));
        binding.tvContent.setText(item.getContent());
        binding.tvRecommendCount.setText(String.format("%s", item.getRecommend()));
        binding.tvDeprecateCount.setText(String.format("%s", item.getDeprecate()));
        initTextChat();
        initSpinner(binding.spinner);
        initChat();
    }

    private void initEvent() {
        commonToolbarBinding.ivLeave.setOnClickListener(v -> {
            setStatusDefaultColor();
            setResult(Activity.RESULT_OK);
            finish();
        });
        binding.contentContainer.setOnClickListener(v -> hideKeyboard());
        binding.loRecommend.setOnClickListener(v -> requestLike());
        binding.loDeprecate.setOnClickListener(v -> requestUnLike());
        binding.tvSendComment.setOnClickListener(v -> sendComment());
    }

    // 인터넷 연결이 끊어졌을 경우 레이아웃 초기화
    private void setEmptyLayout() {
        setStatusColor(getColor(R.color.status_white));
        commonToolbarBinding.toolbarTitle.setText(getResources().getString(R.string.bd_toolbar));
        binding.loBoardDetail.setVisibility(View.GONE);
        binding.recyclerview.setVisibility(View.GONE);
        binding.tvNetworkError.setVisibility(View.VISIBLE);
    }

    private void sendComment() {
        String nickName = binding.etNickname.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String content = binding.etContent.getText().toString().trim();

        if ("".equals(nickName)) {
            binding.etNickname.startAnimation(shake);
            binding.etNickname.requestFocus();
            showKeyboard();
            return;
        }

        if ("".equals(password)) {
            binding.etPassword.startAnimation(shake);
            binding.etPassword.requestFocus();
            showKeyboard();
            return;
        }

        if ("".equals(content)) {
            binding.etContent.startAnimation(shake);
            binding.etContent.requestFocus();
            showKeyboard();
            return;
        }

        if (content.length() > 100) {
            binding.etContent.startAnimation(shake);
            binding.etContent.requestFocus();
            showToastPosition(getString(R.string.validate_content_comment), ViewUtil.getAbsoluteHeight(binding.etContent));
            showKeyboard();
            return;
        }

        if (PatternUtil.isNickNamePattern(nickName)) {
            binding.etNickname.startAnimation(shake);
            binding.etNickname.requestFocus();
            showToastPosition(getString(R.string.validate_nickname), ViewUtil.getAbsoluteHeight(binding.etNickname));
            showKeyboard();
            return;
        }

        if (PatternUtil.isPassWordPattern(password)) {
            binding.etPassword.startAnimation(shake);
            binding.etPassword.requestFocus();
            showToastPosition(getString(R.string.validate_password), ViewUtil.getAbsoluteHeight(binding.etPassword));
            showKeyboard();
            return;
        }

        RequestBody nickNameBody = RequestBody.create(MediaType.parse("multipart/form-data"), nickName);
        RequestBody passwordBody = RequestBody.create(MediaType.parse("multipart/form-data"), password);
        RequestBody contentBody = RequestBody.create(MediaType.parse("multipart/form-data"), content);

        HashMap<String, RequestBody> requestMap = new HashMap<>();
        requestMap.put("nickname", nickNameBody);
        requestMap.put("password", passwordBody);
        requestMap.put("content", contentBody);

        requestChatAdd(requestMap);
    }

    private void requestBoard() {
        showProgressDialog(BoardActivity.this, getResources().getString(R.string.progress_board_detail));
        networkPresenter
                .boardDetail(userID, new BoardDetailListener() {
                    @Override
                    public void success(Board board) {
                        setBoardItem(board);
                        requestChat();
                    }

                    @Override
                    public void fail(String message) {
                        dismissProgressDialog();
                        setEmptyLayout();
                    }
                });
    }

    private void requestChat() {
        networkPresenter
                .chatList(userID, new ChatListener() {
                    @Override
                    public void success(ArrayList<Board> boards) {
                        adapter.addItems(boards);
                        initLayout();
                        dismissProgressDialog();
                    }

                    @Override
                    public void fail(String msg) {
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

    private void requestChatAdd(HashMap<String, RequestBody> request) {
        networkPresenter
                .chatAdd(userID, request, new ChatAddListener() {
                    @Override
                    public void success() {
                        adapter.clear();
                        removeChatText();
                        requestBoard();
                    }

                    @Override
                    public void fail(String msg) {
                        new NoticeDialog(BoardActivity.this)
                                .setMsg(msg)
                                .show();
                    }
                });
    }

    private void setBoardItem(Board board) {
        item = board;
    }

    private void initTextChat() {
        if (adapter.getItemCount() != 0) {
            binding.tvChatTitle.setText(String.format(Locale.KOREA,"댓글 %d개", adapter.getItemCount()));
        }
    }

    private void initSpinner(Spinner spinner) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void initChat() {
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(BoardActivity.this));
        binding.recyclerview.setAdapter(adapter);
    }

    // 채팅 완료 후 텍스트 삭제
    private void removeChatText() {
        binding.etNickname.setText("");
        binding.etPassword.setText("");
        binding.etContent.setText("");
    }

    @Override
    public void onBackPressed() {
        setStatusDefaultColor();
        setResult(Activity.RESULT_OK);
        finish();
    }
}