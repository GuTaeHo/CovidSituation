package com.cosiguk.covidsituation.activity;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.databinding.ActivityCityDetailBinding;
import com.cosiguk.covidsituation.databinding.CloseToolbarBinding;
import com.cosiguk.covidsituation.util.ActivityUtil;

public class CityDetailActivity extends BaseActivity {
    private ActivityCityDetailBinding binding;
    private CloseToolbarBinding closeToolbarBinding;
    private String currentCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_city_detail);
        closeToolbarBinding = DataBindingUtil.bind(binding.closeToolbar.toolbar);

        initValues();
        initLayout();
        initEvent();
    }

    private void initValues() {
        setStatusBarColor(getColor(R.color.status_white));
        // 현재 위치 (도시)
        currentCity = getIntent().getStringExtra(ActivityUtil.DATA);
    }

    private void initLayout() {
        closeToolbarBinding.toolbarTitle.setText(currentCity);
    }

    private void initEvent() {
        closeToolbarBinding.ivLeave.setOnClickListener(view -> {
            finish();
        });
    }
}