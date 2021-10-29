package com.cosiguk.covidsituation;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.cosiguk.covidsituation.activity.BaseActivity;
import com.cosiguk.covidsituation.databinding.ActivityIntroBinding;

public class IntroActivity extends BaseActivity {
    private ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(IntroActivity.this, R.layout.activity_intro);
    }
}