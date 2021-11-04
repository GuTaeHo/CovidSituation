package com.cosiguk.covidsituation.fragment;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cosiguk.covidsituation.BuildConfig;
import com.cosiguk.covidsituation.R;
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
import com.cosiguk.covidsituation.util.NaverMapUtil;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class VaccineFragment extends Fragment implements Comparator<Hospital>, OnMapReadyCallback {
    private FragmentVaccineBinding binding;
    private HospitalAdapter adapter;
    // 지도 프래그먼트 관리 객체
    private FragmentManager fragmentManager;
    private MapFragment mapView;
    // 백신 정보
    private Vaccine vaccine;
    // 진료소 정보
    private ArrayList<Hospital> hospitals;
    // 현재 위치(위, 경도)
    private Location location;
    // 재귀 방지
    private int blockLoop;

    public VaccineFragment() {}

    public static VaccineFragment newInstance() {
        return new VaccineFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vaccine, container, false);
        blockLoop = 0;
        getLocation();
        requestVaccine(0);
        return binding.getRoot();
    }

    private void getLocation() {
        if (LocationUtil.isConnect(getActivity())) {
            if (LocationUtil.getLocation(getActivity()) != null)
                location = LocationUtil.getLocation(getActivity());
            else {
                location = LocationUtil.setBaseLocation();
                BasicUtil.showToast(getActivity(), getString(R.string.vc_location_unknown));
            }
        } else {
            BasicUtil.showToast(getActivity(), getString(R.string.vc_location_off));
            location = LocationUtil.setBaseLocation();
        }
    }

    // 백신 현황 요청
    private void requestVaccine(int initMillisecond) {
        if (blockLoop < 2) {
            HashMap<String, String> map = new HashMap<>();
            map.put("serviceKey", BuildConfig.PUBLIC_SERVICE_KEY);
            map.put("perPage", "1");
            map.put("cond[baseDate::GTE]", ConvertUtil.currentDateBar(initMillisecond));

            MyApplication.showProgressDialog(getActivity(), getResources().getString(R.string.progress_vaccine));
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
                            MyApplication.dismissProgressDialog();
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
                        initListener();
                        MyApplication.dismissProgressDialog();
                    }

                    @Override
                    public void fail(String message) {
                        MyApplication.dismissProgressDialog();
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

        items.forEach(item -> {
            float distance = LocationUtil.computeDistance(
                    location.getLatitude(),
                    location.getLongitude(),
                    Double.parseDouble(item.getLat()),
                    Double.parseDouble(item.getLng()));

            if (distance <= 15) {
                item.setDistance(distance);
                hospitals.add(item);

                HashMap<String, String> map = new HashMap<>();
                map.put(NaverMapUtil.LAT, item.getLat());
                map.put(NaverMapUtil.LNG, item.getLng());
                map.put(NaverMapUtil.CAP, item.getFacilityName());
                map.put(NaverMapUtil.SUB_CAP, ConvertUtil.splitString(item.getCenterName(), 1));
                map.put(NaverMapUtil.PHONE_NUM, item.getPhoneNumber());
                map.put(NaverMapUtil.DISTANCE, String.valueOf(item.getDistance()));

                // 마커 초기화
                NaverMapUtil.setMarkersAndCaptions(getContext(), map);
            }
        });
    
        // 가까운 병원 순 정렬
        hospitals.sort(VaccineFragment.this);

        this.hospitals = hospitals;
    }

    @Override
    public int compare(Hospital o1, Hospital o2) {
        return o1.getDistance().intValue() - o2.getDistance().intValue();
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

    private void initListener() {
        setToggleListener();
    }

    private void setToggleListener() {
        binding.swToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            initMapInstance();

            if (isChecked) {
                binding.recyclerview.setVisibility(View.GONE);
                binding.map.setVisibility(View.VISIBLE);
            } else {
                binding.recyclerview.setVisibility(View.VISIBLE);
                binding.map.setVisibility(View.GONE);
            }
        });
    }

    // 맵 객체 초기화
    // mapView 는 지도를 보여주는 역할만 담당.
    // 지도와 상호작용하기위해선 getMapAsync 메서드의 콜백으로 넘어온 naverMap 객체를 이용
    private void initMapInstance() {
        mapView = NaverMapUtil.getInstance(VaccineFragment.this);
        mapView.getMapAsync(this);
    }

    // 맵 객체가 준비되면 호출
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        NaverMapUtil.setCurrentMarker(naverMap, location);
        // 인터페이스 초기화
        NaverMapUtil.initUI(naverMap);
        // 카메라 이동
        NaverMapUtil.moveCamera(naverMap, location);
        // 줌 레벨 설정
        NaverMapUtil.setZoom(naverMap, 17,6);
        // 마커 출력
        NaverMapUtil.setMarkers(naverMap);
        // 지도 클릭 리스너 등록
        NaverMapUtil.initOnMapClickListener(naverMap);
    }
}