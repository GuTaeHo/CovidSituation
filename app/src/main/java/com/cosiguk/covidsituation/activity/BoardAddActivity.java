package com.cosiguk.covidsituation.activity;

import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.databinding.ActivityBoardAddBinding;
import com.cosiguk.covidsituation.databinding.CloseToolbarBinding;
import com.cosiguk.covidsituation.dialog.NoticeDialog;
import com.cosiguk.covidsituation.network.resultInterface.BoardAddListener;
import com.cosiguk.covidsituation.util.ObserveUtil;
import com.cosiguk.covidsituation.util.PatternUtil;
import com.cosiguk.covidsituation.util.ViewUtil;

import java.util.HashMap;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;


public class BoardAddActivity extends BaseActivity {
    private ActivityBoardAddBinding binding;
    private CloseToolbarBinding closeToolbarBinding;
    private Animation shake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_add);
        closeToolbarBinding = DataBindingUtil.bind(binding.closeToolbar.toolbar);

        initLayout();
        initEvent();
    }

    private void initLayout() {
        setStatusColor(getColor(R.color.status_white));
        closeToolbarBinding.ivLeave.setBackgroundResource(R.drawable.ic_close_24);
        closeToolbarBinding.tvButton.setVisibility(View.VISIBLE);
        closeToolbarBinding.toolbarTitle.setText(getString(R.string.toolbar_title_board_add));
        shake = AnimationUtils.loadAnimation(this, R.anim.anim_shake);
    }

    private void initEvent() {
        binding.container.setOnClickListener(v -> hideKeyboard());
        closeToolbarBinding.ivLeave.setOnClickListener(v -> {
            setResult(Activity.RESULT_CANCELED);
            finish();
        });
        closeToolbarBinding.tvButton.setOnClickListener(v -> {
            inputCheck();
        });
        binding.etContent.addTextChangedListener(new ObserveUtil() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.tvTextCount.setText(String.format(Locale.KOREA, "%d / 250 자", s.length()));
            }
        });
    }

    private void inputCheck() {
        String title = binding.etTitle.getText().toString().trim();
        String content = binding.etContent.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String nickName = binding.etNickname.getText().toString().trim();

        if ("".equals(title)) {
            binding.etTitle.startAnimation(shake);
            binding.etTitle.requestFocus();
            showKeyboard();
            return;
        }

        if ("".equals(content)) {
            binding.etContent.startAnimation(shake);
            binding.etContent.requestFocus();
            showKeyboard();
            return;
        }

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

        if (title.length() > 25) {
            binding.etTitle.startAnimation(shake);
            binding.etTitle.requestFocus();
            showToastPosition(getString(R.string.validate_title), ViewUtil.getAbsoluteHeight(binding.etTitle));
            showKeyboard();
            return;
        }

        if (content.length() > 250) {
            binding.etContent.startAnimation(shake);
            binding.etContent.requestFocus();
            showToastPosition(getString(R.string.validate_content_board_add), ViewUtil.getAbsoluteHeight(binding.etContent));
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

        RequestBody titleBody = RequestBody.create(title, MediaType.parse("multipart/form-data"));
        RequestBody contentBody = RequestBody.create(content, MediaType.parse("multipart/form-data"));
        RequestBody nickNameBody = RequestBody.create(nickName, MediaType.parse("multipart/form-data"));
        RequestBody passwordBody = RequestBody.create(password, MediaType.parse("multipart/form-data"));

        HashMap<String, RequestBody> requestMap = new HashMap<>();
        requestMap.put("title", titleBody);
        requestMap.put("content", contentBody);
        requestMap.put("nickname", nickNameBody);
        requestMap.put("password", passwordBody);

        requestUpdate(requestMap);
    }

    private void requestUpdate(HashMap<String, RequestBody> requestMap) {
        networkPresenter
                .boardAdd(requestMap, new BoardAddListener() {
                    @Override
                    public void success() {
                        setResult(Activity.RESULT_OK);
                        finish();
                        showToast("게시글이 등록되었습니다");
                    }

                    @Override
                    public void fail(String msg) {
                        new NoticeDialog(BoardAddActivity.this)
                                .setMsg(msg)
                                .show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}