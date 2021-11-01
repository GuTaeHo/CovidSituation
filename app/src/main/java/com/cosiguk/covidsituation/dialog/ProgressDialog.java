package com.cosiguk.covidsituation.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.databinding.CommonProgressBinding;

public class ProgressDialog extends Dialog {
    private CommonProgressBinding binding;
    private String message;
    private Context context;

    public ProgressDialog(@NonNull Context context) {
        super(context);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.context = context;
    }

    public ProgressDialog setMessage(String message) {
        if (binding != null) {
            binding.tvMessage.setText(message);
        }
        this.message = message;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.common_progress, null, false);
        setContentView(binding.getRoot());
        // 영역 밖 터치시 다이얼로그 사라지지 않도록 설정
        setCanceledOnTouchOutside(false);
        binding.tvMessage.setText(message);
    }
}
