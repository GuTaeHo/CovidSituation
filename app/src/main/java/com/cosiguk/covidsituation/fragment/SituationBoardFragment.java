package com.cosiguk.covidsituation.fragment;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.cosiguk.covidsituation.BuildConfig;
import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.activity.CityDetailActivity;
import com.cosiguk.covidsituation.activity.SplashActivity;
import com.cosiguk.covidsituation.adapter.CityAdapter;
import com.cosiguk.covidsituation.application.MyApplication;
import com.cosiguk.covidsituation.databinding.FragmentSituationBoardBinding;
import com.cosiguk.covidsituation.dialog.NoticeDialog;
import com.cosiguk.covidsituation.model.City;
import com.cosiguk.covidsituation.model.Infection;
import com.cosiguk.covidsituation.network.resultInterface.SituationBoardListener;
import com.cosiguk.covidsituation.network.resultInterface.TotalListener;
import com.cosiguk.covidsituation.util.ActivityUtil;
import com.cosiguk.covidsituation.util.BasicUtil;
import com.cosiguk.covidsituation.util.ConvertUtil;
import com.cosiguk.covidsituation.util.LocationUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SituationBoardFragment extends Fragment {

    private FragmentSituationBoardBinding binding;
    private CityAdapter adapter;
    // 전체 확진 정보
    private Infection infection;
    // 전일 확진 정보
    private Infection yesterdayInfection;
    // 시, 도 확진 정보 리스트
    private List<City> cityArrayList;
    // 현재 도시
    private City city;
    // 요청 횟수
    private int requestCount;
    
    public SituationBoardFragment() {}

    public static SituationBoardFragment newInstance() {
        return new SituationBoardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_situation_board, container, false);
        checkPermission();
        initAdapter();
        requestSituation(0);
        return binding.getRoot();
    }

    private void checkPermission() {
        BasicUtil.initPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
    }

    private void initAdapter() {
        adapter = new CityAdapter(getActivity());
    }

    // 확진 현황 요청
    private void requestSituation(int initMillisecond) {
        HashMap<String, String> map = new HashMap<>();
        map.put("ServiceKey", BuildConfig.PUBLIC_SERVICE_KEY);
        map.put("startCreateDt", ConvertUtil.getYesterdayFormatTime(initMillisecond));
        map.put("endCreateDt", ConvertUtil.getCurrentFormatTime(initMillisecond));

        requestTotal(map);
    }

    // 전체 확진 정보 API 요청
    private void requestTotal(HashMap<String, String> map) {
        MyApplication.showProgressDialog(getActivity(), getResources().getString(R.string.progress_total));
        MyApplication.getNetworkPresenterInstance()
                .total(map, new TotalListener() {
                    @Override
                    public void success(List<Infection> items) {
                        // 작일 정보는 리스트의 두 번째에 포함되어 있음
                        setYesterdayItems(items.get(1));
                        // 금일 정보는 리스트의 첫 번째에 포함되어 있음
                        setDailyItems(items.get(0));
                        // 도시 정보 요청
                        requestCity(map.get("endCreateDt"));
                    }

                    @Override
                    public void request() {
                        if (requestCount == 0) {
                            requestSituation(ConvertUtil.PREVIOUS_DAY);
                            requestCount += 1;
                        } else {
                            requestSituation(ConvertUtil.BEFORE_TWO_DAYS);
                        }
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

    // 시, 도 확진 정보 API 요청
    private void requestCity(String day) {
        // 요청에 성공한 날짜를 기준으로 시, 도 확진자 요청
        HashMap<String, String> map = new HashMap<>();
        map.put("ServiceKey", BuildConfig.PUBLIC_SERVICE_KEY);
        map.put("startCreateDt", day);
        map.put("endCreateDt", day);

        MyApplication.showProgressDialog(getActivity(), getResources().getString(R.string.progress_city));
        MyApplication.getNetworkPresenterInstance()
                .situationBoardList(map, new SituationBoardListener() {
                    @Override
                    public void success(List<City> items) {
                        setCityItemList(items);
                        // 레이아웃 업데이트
                        initDailyLayout();
                        initTotalLayout();
                        initCityLayout();
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

    public void setDailyItems(Infection responseDailyTotal) {
        infection = responseDailyTotal;
    }
    public void setYesterdayItems(Infection responseYesterdayTotal) {
        yesterdayInfection = responseYesterdayTotal;
    }
    // 시, 도 정보 초기화 (확진자 순으로 정렬)
    public void setCityItemList(List<City> items) {
        cityArrayList = items;

        // 내림차순 (확진자 기준)
        cityArrayList.sort((o1, o2) -> o2.getDefCnt() - o1.getDefCnt());
        // 현재 주소
        String currentAddress = getCurrentAddress();

        // 현재 위치에 관련된 정보만 추출하여 저장
        cityArrayList.forEach(item -> {
            if (item.getGubun().equals(currentAddress)) {
                city = item;
            }
        });
        cityArrayList.remove(city);
        cityArrayList.add(1, city);
        // 아이템 각각에 클릭 이벤트 구현
        adapter.setOnItemClickListener((view, position) -> {
            ActivityUtil.startSingleActivityExtra(
                    getActivity(),
                    CityDetailActivity.class,
                    adapter.getItem(position).getGubun());
        });
        adapter.addItems((ArrayList<City>) cityArrayList);
    }

    private void initDailyLayout() {
        binding.tvDailyContetns.setText(String.format("%s 기준",
                ConvertUtil.convertDateDot(infection.getStateDt())));
        // 일일 확진자
        binding.tvDailyInfect.setText(String.format("확진환자 (%s)",
                ConvertUtil.convertCommaSeparator(infection.getDecideCnt() - yesterdayInfection.getDecideCnt())));
        // 일일 사망자
        binding.tvDailyDeath.setText(String.format("사망자 (%s)",
                ConvertUtil.convertCommaSeparator(infection.getDeathCnt()- yesterdayInfection.getDeathCnt())));
    }

    private void initTotalLayout() {
        // 현황 날짜
        binding.tvTotalContents.setText(String.format("%s 기준",
                ConvertUtil.convertDateDot(infection.getStateDt())));
        // 총 확진자
        binding.tvTotalInfect.setText(ConvertUtil.convertCommaSeparator(infection.getDecideCnt()));
        binding.tvTotalInfectCompare.setText(String.format("(%s)",
                ConvertUtil.convertSignCommaSeparator(infection.getDecideCnt() - yesterdayInfection.getDecideCnt())));
        // 총 사망자
        binding.tvTotalDeath.setText(ConvertUtil.convertCommaSeparator(infection.getDeathCnt()));
        binding.tvTotalDeathCompare.setText(String.format("(%s)",
                ConvertUtil.convertSignCommaSeparator(infection.getDeathCnt() - yesterdayInfection.getDeathCnt())));
        }

    private void initCityLayout() {
        binding.tvListTitleIntro.setText("현 위치 : " + getCurrentAddressShort());
        binding.recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        binding.recyclerview.setAdapter(adapter);
    }

    // 위치 OFF -> 기본 위치 : 서울
    private String getCurrentAddress() {
        if (LocationUtil.isConnect(getActivity())) {
            try {
                Location location = LocationUtil.getLastLocation(getActivity());
                String address = LocationUtil.getCoordinateToAddress(getActivity(), location);
                String[] addresses = address.split("\\s");
                return addressConvert(addresses[1]);
            } catch (NullPointerException e) {
                return getActivity().getResources().getString(R.string.location_base);
            }
        } else {
            // base location
            return getActivity().getResources().getString(R.string.location_base);
        }
    }

    // 지오코딩 결과 간략화하여 반환
    private String getCurrentAddressShort() {
        if (LocationUtil.isConnect(getActivity())) {
            try {
                Location location = LocationUtil.getLastLocation(getActivity());
                String address = LocationUtil.getCoordinateToAddress(getActivity(), location);
                String[] addresses = address.split("\\s");
                // ex -> 대구광역시 남구
                return addresses[1] + " " + addresses[2];
            } catch (NullPointerException e) {
                return getActivity().getResources().getString(R.string.location_base);
            }
        } else {
            // base location
            return getActivity().getResources().getString(R.string.location_base);
        }
    }

    // 주소 변환
    private String addressConvert(String address) {
        if (SplashActivity.getProvince(address) != null) {
            // ex -> "대구"
            return SplashActivity.getProvince(address);
        } else {
            // ex -> "서울"
            return SplashActivity.getProvince(getResources().getString(R.string.location_base));
        }
    }
}