package com.cosiguk.covidsituation.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.databinding.DataBindingUtil;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.databinding.ActivityCityDetailBinding;
import com.cosiguk.covidsituation.databinding.SpinnerToolbarBinding;
import com.cosiguk.covidsituation.util.ActivityUtil;

public class CityDetailActivity extends BaseActivity {
    private ActivityCityDetailBinding binding;
    private SpinnerToolbarBinding spinnerToolbarBinding;
    private String currentCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_city_detail);
        spinnerToolbarBinding = DataBindingUtil.bind(binding.spinnerToolbar.toolbar);

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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerToolbar.spinner.setAdapter(adapter);
    }

    private void initEvent() {
        spinnerToolbarBinding.ivLeave.setOnClickListener(view -> {
            finish();
        });
    }
}