package com.cosiguk.covidsituation.fragment;

import android.location.Location;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cosiguk.covidsituation.BuildConfig;
import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.application.MyApplication;
import com.cosiguk.covidsituation.databinding.FragmentVaccineBinding;
import com.cosiguk.covidsituation.dialog.NoticeDialog;
import com.cosiguk.covidsituation.network.responseVaccine.Items;
import com.cosiguk.covidsituation.network.resultInterface.VaccineTotal;
import com.cosiguk.covidsituation.util.BasicUtil;
import com.cosiguk.covidsituation.util.ConvertUtil;
import com.cosiguk.covidsituation.util.LocationUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class VaccineFragment extends Fragment {
    private FragmentVaccineBinding binding;
    private Items items;
    // 현재 위치
    private Location location;
    // 재귀 방지
    private int blockLoop;

    public VaccineFragment() {
    }

    public static VaccineFragment newInstance(String param1, String param2) {
        VaccineFragment fragment = new VaccineFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vaccine, container, false);
        blockLoop = 0;
        checkPermission();
        initRefreshListener();
        requestVaccinate(0);
        return binding.getRoot();
    }

    private void checkPermission() {
        if (LocationUtil.getPosition(getActivity()) != null){
            // 현재 위치 저장
            location = LocationUtil.getPosition(getActivity());
            Log.d("location", location.getAccuracy() + ", " + location.getLatitude() + ", " + location.getLongitude());
        } else {
            Log.d("location", "위치 정보가 null 입니다");
            BasicUtil.showSnackBar(getActivity(),
                    getActivity().getWindow().getDecorView().getRootView(),
                    "위치 권한이 허용되지 않아 진료소 목록을 불러올 수 없습니다"
            );
        }
    }

    // 백신 현황 요청
    private void requestVaccinate(int initMillisecond) {
        if (blockLoop < 2) {
            HashMap<String, String> map = new HashMap<>();
            map.put("serviceKey", BuildConfig.PUBLIC_SERVICE_KEY);
            map.put("perPage", "1");
            map.put("cond[baseDate::GTE]", ConvertUtil.currentDateBar(initMillisecond));

            request(map);
        }
    }

    private void request(HashMap<String, String> map) {
        MyApplication
                .networkPresenter
                .vaccineTotal(map, new VaccineTotal() {
                    @Override
                    public void success(ArrayList<Items> items) {
                        setVaccineItems(items.get(0));
                        initLayout();
                        Log.d("totalFirst", items.get(0).getTotalFirstCnt()+"");
                    }

                    @Override
                    public void request(ArrayList<Items> items) {
                        Log.d("vaccinate", "reRequestVaccinates");
                        requestVaccinate(ConvertUtil.PREVIOUS_DAY);
                    }

                    @Override
                    public void fail(String message) {
                        new NoticeDialog(getActivity())
                                .setMsg(message)
                                .show();
                    }
                });
    }

    private void setVaccineItems(Items items) {
        this.items = items;
    }

    private void initLayout() {
        // 당일 누적
        binding.tvFirstTotal.setText(ConvertUtil.convertCommaSeparator(items.getTotalFirstCnt()));
        binding.tvFirstDaily.setText(ConvertUtil.convertCommaSeparator(items.getFirstCnt()));
        binding.tvFirstYesterday.setText(ConvertUtil.convertCommaSeparator(items.getAccumulatedFirstCnt()));
        // 당일 실적
        binding.tvSecondTotal.setText(ConvertUtil.convertCommaSeparator(items.getTotalSecondCnt()));
        binding.tvSecondDaily.setText(ConvertUtil.convertCommaSeparator(items.getSecondCnt()));
        binding.tvSecondYesterday.setText(ConvertUtil.convertCommaSeparator(items.getAccumulatedSecondCnt()));
        // 전일 누적
        binding.tvThirdTotal.setText(ConvertUtil.convertCommaSeparator(items.getTotalThirdCnt()));
        binding.tvThirdDaily.setText(ConvertUtil.convertCommaSeparator(items.getThirdCnt()));
        binding.tvThirdYesterday.setText(ConvertUtil.convertCommaSeparator(items.getAccumulatedThirdCnt()));
        // 날짜 출력
        binding.tvDate.setText(ConvertUtil.convertBarDateToDot(items.getBaseDate()) + " 기준");
    }

    private void initRefreshListener() {
        binding.loSwipe.setOnRefreshListener(()->{

            BasicUtil.showSnackBar(
                    getActivity(),
                    getActivity().getWindow().getDecorView().getRootView(),
                    "새로고침 완료");
            // 새로 고침 완료
            binding.loSwipe.setRefreshing(false);
        });
    }
}