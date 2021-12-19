package com.cosiguk.covidsituation.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.adapter.BaseRecyclerViewAdapter;
import com.cosiguk.covidsituation.adapter.ChatAdapter;
import com.cosiguk.covidsituation.databinding.ActivityBoardBinding;
import com.cosiguk.covidsituation.databinding.CloseToolbarBinding;
import com.cosiguk.covidsituation.dialog.NoticeDialog;
import com.cosiguk.covidsituation.model.Board;
import com.cosiguk.covidsituation.network.resultInterface.BoardDeprecateListener;
import com.cosiguk.covidsituation.network.resultInterface.BoardDetailListener;
import com.cosiguk.covidsituation.network.resultInterface.BoardRecommendListener;
import com.cosiguk.covidsituation.network.resultInterface.ChatAddListener;
import com.cosiguk.covidsituation.network.resultInterface.ChatDeprecateListener;
import com.cosiguk.covidsituation.network.resultInterface.ChatListener;
import com.cosiguk.covidsituation.network.resultInterface.ChatRecommendListener;
import com.cosiguk.covidsituation.util.ActivityUtil;
import com.cosiguk.covidsituation.util.PatternUtil;
import com.cosiguk.covidsituation.util.ToastUtil;
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
        userID = getIntent().getIntExtra(ActivityUtil.DATA, -1);
        adapter = new ChatAdapter(BoardActivity.this);
        shake = AnimationUtils.loadAnimation(this, R.anim.anim_shake);
    }

    private void initLayout() {
        setStatusBarColor(getColor(R.color.status_white));
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
            setStatusBarDefaultColor();
            setResult(Activity.RESULT_OK);
            finish();
        });
        binding.contentContainer.setOnClickListener(v -> hideKeyboard());
        binding.loRecommend.setOnClickListener(v -> requestBoardRecommend());
        binding.loDeprecate.setOnClickListener(v -> requestBoardDeprecate());
        binding.tvSendComment.setOnClickListener(v -> sendComment());
    }

    // 인터넷 연결이 끊어졌을 경우 레이아웃 초기화
    private void setEmptyLayout() {
        setStatusBarColor(getColor(R.color.status_white));
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

        RequestBody nickNameBody = RequestBody.create(nickName, MediaType.parse("multipart/form-data"));
        RequestBody passwordBody = RequestBody.create(password, MediaType.parse("multipart/form-data"));
        RequestBody contentBody = RequestBody.create(content, MediaType.parse("multipart/form-data"));

        HashMap<String, RequestBody> requestMap = new HashMap<>();
        requestMap.put("nickname", nickNameBody);
        requestMap.put("password", passwordBody);
        requestMap.put("content", contentBody);

        requestChatAdd(requestMap);
    }

    // 게시글 요청
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

    // 댓글 요청
    private void requestChat() {
        networkPresenter
                .chatList(userID, new ChatListener() {
                    @Override
                    public void success(ArrayList<Board> boards) {
                        adapter.addItems(boards);
                        // setChatClickEvent();
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

    // 게시글 좋아요
    private void requestBoardRecommend() {
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

    // 게시글 싫어요
    private void requestBoardDeprecate() {
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

    // 댓글 등록
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

    // 댓글 좋아요
    public void requestChatRecommend(int chatID) {
        networkPresenter
                .chatRecommend(chatID, new ChatRecommendListener() {
                    @Override
                    public void success() {
                        adapter.clear();
                        requestBoard();
                    }

                    @Override
                    public void fail(String msg) {
                        ToastUtil.showToastCenter(BoardActivity.this, msg);
                    }
                });
    }

    // 댓글 싫어요
    public void requestChatDeprecate(int chatID) {
        networkPresenter
                .chatDeprecate(chatID, new ChatDeprecateListener() {
                    @Override
                    public void success() {
                        adapter.clear();
                        requestBoard();
                    }

                    @Override
                    public void fail(String msg) {
                        ToastUtil.showToastCenter(BoardActivity.this, msg);
                    }
                });

    }

    // 댓글 신고
    public void requestChatReport(int chatID) {

    }

    // 댓글 삭제
    public void requestChatDelete(int chatID) {

    }

    // 게시글은 어댑터 사용 X, 액티비티 자체에서 값 설정
    private void setBoardItem(Board board) {
        item = board;
    }

    private void initTextChat() {
        if (adapter.getItemCount() != 0) {
            binding.tvChatTitle.setText(String.format(Locale.KOREA,"댓글 %d개", adapter.getItemCount()));
        }
    }

    private void initSpinner(Spinner spinner) {
        // 어댑터 초기화 (context, 스트링 아이템, 초기 레이아웃)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, R.layout.spinner_board);
        // drop down 레이아웃 설정
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(adapter);
    }

    // 댓글 클릭 이벤트 정의
    private void setChatClickEvent() {

        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onItemClick(View view, int position) {
                Log.d("adapterClicked", "view : " + view.toString() + ", position : " + position);
                // 클릭된 아이템의 id 획득
                int chatID = adapter.getItem(position).getId();
                switch (view.getId()) {
                    case R.id.lo_recommend:
                        requestChatRecommend(chatID);
                        break;
                    case R.id.lo_deprecate:
                        requestChatDeprecate(chatID);
                        break;
                    case R.id.tv_report:
                        requestChatReport(chatID);
                        break;
                    case R.id.tv_delete:
                        requestChatDelete(chatID);
                        break;
                    case R.id.container:
                        showToast("컨테이너 클릭됨, ID : " + chatID);
                        break;
                    default:
                        showToast("이벤트 설정중 문제가 생겼습니다...");
                        break;
                }
            }
        });
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
        setStatusBarDefaultColor();
        setResult(Activity.RESULT_OK);
        finish();
    }
}