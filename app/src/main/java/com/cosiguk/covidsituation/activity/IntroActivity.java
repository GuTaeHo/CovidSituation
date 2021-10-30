package com.cosiguk.covidsituation.activity;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.databinding.ActivityIntroBinding;

public class IntroActivity extends BaseActivity {
    private ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(IntroActivity.this, R.layout.activity_intro);
    }
}