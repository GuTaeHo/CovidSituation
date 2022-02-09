package com.cosiguk.covidsituation.dialog;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.databinding.DialogInputBinding;

public class InputDialog extends AlertDialog {
    private DialogInputBinding binding;
    private Context context;
    private CharSequence message;
    private CharSequence hint;
    private String positiveString = "확인";
    private String negativeString = "취소";
    private boolean isShowNegativeButton = true;
    private boolean isBackPressButton = true;

    private InputDialogCallbackListener inputDialogCallbackListener;

    public InputDialog(Context context) {
        super(context);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.context = context;
    }

    public InputDialog setShowNegativeButton(boolean b) {
        this.isShowNegativeButton = b;
        return this;
    }

    public InputDialog setBackPressButton(boolean b) {
        this.isBackPressButton = b;
        return this;
    }

    public InputDialog setMsg(CharSequence message) {
        this.message = message;
        return this;
    }

    public InputDialog setHint(CharSequence hint) {
        this.hint = hint;
        return this;
    }

    public InputDialog setInputDialogCallbackListener(InputDialogCallbackListener inputDialogCallbackListener) {
        this.inputDialogCallbackListener = inputDialogCallbackListener;
        return this;
    }

    public InputDialog setPositiveMsg(String message) {
        this.positiveString = message;
        return this;
    }

    public InputDialog setNegativeMsg(String message) {
        this.negativeString = message;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_input, null, false);
        setContentView(binding.getRoot());

        // 다이얼로그 영역 밖 터치시 다이얼로그가 사라지지 않도록 설정
        setCanceledOnTouchOutside(false);
        // editText 포커싱 초기화 (가상 키보드 작동)
        this.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        binding.tvMessage.setText(message);
        binding.etUserInput.setHint(hint);
        binding.tvPositive.setText(positiveString);
        binding.tvNegative.setText(negativeString);

        binding.loPositive.setOnClickListener(v -> {
            String userInput = binding.etUserInput.getText().toString().trim();

            if ("".equals(userInput)) {
                binding.tvMessage.setText("비밀번호를 입력해주세요");
                return;
            } else if (inputDialogCallbackListener != null) {
                inputDialogCallbackListener.positive(userInput);
            }

            dismiss();
        });

        binding.loNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputDialogCallbackListener != null) {
                    inputDialogCallbackListener.negative();
                }

                dismiss();
            }
        });

        binding.loNegative.setVisibility(isShowNegativeButton ? View.VISIBLE : View.GONE);
    }

    public interface InputDialogCallbackListener {
        void positive(String value);

        void negative();
    }

    @Override
    public void onBackPressed() {
        if (isBackPressButton) {
            super.onBackPressed();
        }
    }
}
