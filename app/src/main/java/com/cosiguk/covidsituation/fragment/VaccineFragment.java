package com.cosiguk.covidsituation.fragment;

import android.location.Location;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cosiguk.covidsituation.BuildConfig;
import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.adapter.CityAdapter;
import com.cosiguk.covidsituation.adapter.HospitalAdapter;
import com.cosiguk.covidsituation.application.MyApplication;
import com.cosiguk.covidsituation.databinding.FragmentVaccineBinding;
import com.cosiguk.covidsituation.dialog.NoticeDialog;
import com.cosiguk.covidsituation.model.Hospital;
import com.cosiguk.covidsituation.model.Vaccine;
import com.cosiguk.covidsituation.network.resultInterface.HospitalListener;
import com.cosiguk.covidsituation.network.resultInterface.VaccineListener;
import com.cosiguk.covidsituation.util.BasicUtil;
import com.cosiguk.covidsituation.util.ConvertUtil;
import com.cosiguk.covidsituation.util.LocationUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class VaccineFragment extends Fragment {
    private FragmentVaccineBinding binding;
    private HospitalAdapter adapter;
    // 백신 정보
    private Vaccine vaccine;
    // 진료소 정보
    private ArrayList<Hospital> hospitals;
    // 현재 위치(위, 경도)
    private Location location;
    // 현재 위치(주소)
    private String[] addresses;
    // 재귀 방지
    private int blockLoop;

    public VaccineFragment() {
    }

    public static VaccineFragment newInstance() {
        return new VaccineFragment();
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
        getLocation();
        initRefreshListener();
        requestVaccine(0);
        return binding.getRoot();
    }

    private void getLocation() {
        if (LocationUtil.isLocationStatus(getActivity())) {
            if (LocationUtil.getLocation(getActivity()) != null){
                // 현재 위치 저장
                location = LocationUtil.getLocation(getActivity());
                String address = LocationUtil.getCoordinateToAddress(getActivity(), location);
                addresses = address.split("\\s");

            } else {
                BasicUtil.showSnackBar(getActivity(),
                        getActivity().getWindow().getDecorView().getRootView(),
                        "위치정보를 알 수 없습니다. 위치를 재설정합니다"
                );
                location = LocationUtil.setBaseLocation();
                String address = LocationUtil
                        .getCoordinateToAddress(getActivity(), location);
                addresses = address.split("\\s");
            }
        } else {
            BasicUtil.showSnackBar(getActivity(),
                    getActivity().getWindow().getDecorView().getRootView(),
                    "\"위치\"기능이 비활성화상태입니다. 위치를 재설정합니다");

            location = LocationUtil.setBaseLocation();
            String address = LocationUtil
                    .getCoordinateToAddress(getActivity(), location);
            addresses = address.split("\\s");
        }
    }

    // 백신 현황 요청
    private void requestVaccine(int initMillisecond) {
        if (blockLoop < 2) {
            HashMap<String, String> map = new HashMap<>();
            map.put("serviceKey", BuildConfig.PUBLIC_SERVICE_KEY);
            map.put("perPage", "1");
            map.put("cond[baseDate::GTE]", ConvertUtil.currentDateBar(initMillisecond));

            MyApplication
                    .getNetworkPresenterInstance()
                    .vaccineTotal(map, new VaccineListener() {
                        @Override
                        public void success(ArrayList<Vaccine> items) {
                            setVaccineItem(items.get(0));
                            requestHospital();
                        }

                        @Override
                        public void request(ArrayList<Vaccine> items) {
                            requestVaccine(ConvertUtil.PREVIOUS_DAY);
                        }

                        @Override
                        public void fail(String message) {
                            new NoticeDialog(getActivity())
                                    .setMsg(message)
                                    .show();
                        }
                    });
        }
    }

    private void requestHospital() {
        HashMap<String, String> map = new HashMap<>();
        map.put("serviceKey", BuildConfig.PUBLIC_SERVICE_KEY);
        map.put("page", "1");
        map.put("perPage", "300");

        MyApplication
                .getNetworkPresenterInstance()
                .hospital(map, new HospitalListener() {
                    @Override
                    public void success(ArrayList<Hospital> list) {
                        setHospitalItems(list);
                        initLayout();
                    }

                    @Override
                    public void fail(String message) {
                        new NoticeDialog(getActivity())
                                .setMsg(message)
                                .show();
                    }
                });
    }

    private void setVaccineItem(Vaccine item) {
        this.vaccine = item;
    }

    // API 응답 리스트 -> 현재 주소 일치 아이템 저장
    private void setHospitalItems(ArrayList<Hospital> items) {
        ArrayList<Hospital> hospitals = new ArrayList<>();

        if (addresses[1] != null) {
            items.forEach(item -> {
                if (item.getSido().equals(addresses[1])) {
                    float distance = LocationUtil.computeDistance(
                            location.getLatitude(),
                            location.getLongitude(),
                            Double.parseDouble(item.getLat()),
                            Double.parseDouble(item.getLng()));
                    // 현재 아이템에 현재 위치와 거리 차이 추가
                    item.setDistance(distance);
                    // 일치 아이템 추가
                    hospitals.add(item);
                }
            });
        }
        this.hospitals = hospitals;
    }

    private void initLayout() {
        // 당일 누적
        binding.tvFirstTotal.setText(ConvertUtil.convertCommaSeparator(vaccine.getTotalFirstCnt()));
        binding.tvFirstDaily.setText(ConvertUtil.convertSignCommaSeparator(vaccine.getFirstCnt()));
        binding.tvFirstYesterday.setText(ConvertUtil.convertCommaSeparator(vaccine.getAccumulatedFirstCnt()));
        // 당일 실적
        binding.tvSecondTotal.setText(ConvertUtil.convertCommaSeparator(vaccine.getTotalSecondCnt()));
        binding.tvSecondDaily.setText(ConvertUtil.convertSignCommaSeparator(vaccine.getSecondCnt()));
        binding.tvSecondYesterday.setText(ConvertUtil.convertCommaSeparator(vaccine.getAccumulatedSecondCnt()));
        // 전일 누적
        binding.tvThirdTotal.setText(ConvertUtil.convertCommaSeparator(vaccine.getTotalThirdCnt()));
        binding.tvThirdDaily.setText(ConvertUtil.convertSignCommaSeparator(vaccine.getThirdCnt()));
        binding.tvThirdYesterday.setText(ConvertUtil.convertCommaSeparator(vaccine.getAccumulatedThirdCnt()));
        // 날짜 출력
        binding.tvDate.setText(ConvertUtil.convertBarDateToDot(vaccine.getBaseDate()) + " 기준");

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HospitalAdapter(hospitals);
        binding.recyclerview.setAdapter(adapter);
    }

    private void initRefreshListener() {
        binding.loSwipe.setOnRefreshListener(()->{
            // 요청 초기화
            blockLoop = 0;
            requestVaccine(0);
            refreshSuccess();
        });
    }

    private void refreshSuccess() {
        BasicUtil.showSnackBar(
                getActivity(),
                getActivity().getWindow().getDecorView().getRootView(),
                "새로고침 완료");
        // 새로 고침 완료
        binding.loSwipe.setRefreshing(false);
    }
}