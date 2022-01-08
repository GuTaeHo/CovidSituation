package com.cosiguk.covidsituation.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.databinding.DataBindingUtil;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.databinding.ActivityCityDetailBinding;
import com.cosiguk.covidsituation.databinding.SpinnerToolbarBinding;
import com.cosiguk.covidsituation.dialog.NoticeDialog;
import com.cosiguk.covidsituation.model.City;
import com.cosiguk.covidsituation.network.resultInterface.InfectionCityWeeks;
import com.cosiguk.covidsituation.util.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

public class CityDetailActivity extends BaseActivity {
    private ActivityCityDetailBinding binding;
    private SpinnerToolbarBinding spinnerToolbarBinding;
    private List<String> cityList;
    // 7일치 감염자 현황 리스트
    private ArrayList<City> cityInfectionWeeks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_city_detail);
        spinnerToolbarBinding = DataBindingUtil.bind(binding.spinnerToolbar.toolbar);

        initValues();
        initLayout();
        initEvent();
        requestInfectionCityWeeks();
    }

    private void initValues() {
        setStatusBarColor(getColor(R.color.status_white));
        // 현재 위치 (도시)
        String currentCity = getIntent().getStringExtra(ActivityUtil.DATA);
        cityList = SplashActivity.getCityList();
        // 현 도시를 최상단으로
        cityList.remove(currentCity);
        cityList.add(0, currentCity);
    }

    private void initLayout() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cityList);

        // ArrayAdapter.createFromResource(this,
        //    R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerToolbar.spinner.setAdapter(adapter);
    }

    private void initEvent() {
        spinnerToolbarBinding.ivLeave.setOnClickListener(view -> {
            finish();
        });

        // 스피너 클릭 이벤트
        binding.spinnerToolbar.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void requestInfectionCityWeeks() {
        showProgressDialog(this, "불러오는 중...");
        networkPresenter
                .infectionCityWeeks(new InfectionCityWeeks() {
                    @Override
                    public void success(ArrayList<City> arrayList) {
                        dismissProgressDialog();
                    }

                    @Override
                    public void fail(String message) {
                        dismissProgressDialog();
                        new NoticeDialog(CityDetailActivity.this)
                                .setMsg(message)
                                .show();
                    }
                });
    }
}