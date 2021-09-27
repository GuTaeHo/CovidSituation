package com.cosiguk.covidsituation.activity;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.databinding.ActivityMainBinding;
import com.cosiguk.covidsituation.databinding.CommonToolbarBinding;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private CommonToolbarBinding commonToolbarBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        commonToolbarBinding = DataBindingUtil.bind(binding.commonToolbar.toolbar);

        initLayout();
    }

    private void initLayout() {
        initCommonActionBarLayout(commonToolbarBinding, "코로나 상황판", false);
    }
}