package com.cosiguk.covidsituation.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.adapter.ChatAdapter;
import com.cosiguk.covidsituation.databinding.ActivityBoardBinding;
import com.cosiguk.covidsituation.databinding.CloseToolbarBinding;
import com.cosiguk.covidsituation.dialog.NoticeDialog;
import com.cosiguk.covidsituation.model.Board;
import com.cosiguk.covidsituation.network.Response;
import com.cosiguk.covidsituation.network.resultInterface.BoardDeprecateListener;
import com.cosiguk.covidsituation.network.resultInterface.BoardDetailListener;
import com.cosiguk.covidsituation.network.resultInterface.BoardRecommendListener;
import com.cosiguk.covidsituation.network.resultInterface.BoardReportListener;
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
    // 게시글 작성 유저 ID
    private int userID;
    private Animation shake;
    // 게시글 스피너 INDEX
    private static final int REPORT = 0;
    private static final int UPDATE = 1;
    private static final int DELETE = 2;
    // 스피너 최초 실행 시 아이템 클릭 체크
    private int firstCheck;

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
        firstCheck = 0;
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
                .detailBoard(userID, new BoardDetailListener() {
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

    // 게시글 좋아요
    private void requestBoardRecommend() {
        networkPresenter
                .recommendBoard(userID, new BoardRecommendListener() {
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
                .deprecateBoard(userID, new BoardDeprecateListener() {
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

    // 게시글 신고
    private void requestBoardReport(int boardID) {
        networkPresenter
                .reportBoard(boardID, new BoardReportListener() {
                    @Override
                    public void success(Response<Object> response) {
                        new NoticeDialog(BoardActivity.this)
                                .setMsg("신고가 완료되었습니다")
                                .setShowNegativeButton(false)
                                .show();
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
                R.array.spinner_chat_array, R.layout.spinner_board);
        // drop down 레이아웃 설정
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (++firstCheck == 1) {
                    return;
                }

                Log.d("환장","하겠네 position -> " + position);

                switch (position) {
                    case REPORT:
                        new NoticeDialog(BoardActivity.this)
                                .setMsg("이 게시글을 신고하시겠습니까?")
                                .setNoticeDialogCallbackListener(new NoticeDialog.NoticeDialogCallbackListener() {
                                    @Override
                                    public void positive() {
                                        requestBoardReport(item.getId());
                                    }

                                    @Override
                                    public void negative() {

                                    }
                                }).show();
                        break;
                    case UPDATE:
                        break;
                    case DELETE:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
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