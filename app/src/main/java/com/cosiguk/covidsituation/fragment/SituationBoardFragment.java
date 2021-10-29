package com.cosiguk.covidsituation.fragment;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cosiguk.covidsituation.BuildConfig;
import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.adapter.CityAdapter;
import com.cosiguk.covidsituation.application.MyApplication;
import com.cosiguk.covidsituation.databinding.FragmentSituationBoardBinding;
import com.cosiguk.covidsituation.dialog.NoticeDialog;
import com.cosiguk.covidsituation.model.City;
import com.cosiguk.covidsituation.model.Infection;
import com.cosiguk.covidsituation.network.resultInterface.BoardListListener;
import com.cosiguk.covidsituation.network.resultInterface.TotalListener;
import com.cosiguk.covidsituation.util.BasicUtil;
import com.cosiguk.covidsituation.util.ConvertUtil;
import com.cosiguk.covidsituation.util.LocationUtil;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class SituationBoardFragment extends Fragment {
    private static final String FRAG_TAG = "fragmentTag";

    private FragmentSituationBoardBinding binding;
    // 전체 확진 정보
    private Infection infection;
    // 전일 확진 정보
    private Infection yesterdayInfection;
    // 시, 도 확진 정보 리스트
    private List<City> cityArrayList;
    // 재귀 요청 방지
    int maxCount;
    // 현재 도시
    City city;
    
    public SituationBoardFragment() {}

    public static SituationBoardFragment newInstance(String fragmentTag) {
        SituationBoardFragment fragment = new SituationBoardFragment();
        Bundle args = new Bundle();
        args.putString(FRAG_TAG, fragmentTag);
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_situation_board, container, false);
        maxCount = 0;
        checkPermission();
        requestSituation(0);
        initRefreshListener();
        return binding.getRoot();
    }

    private void checkPermission() {
        BasicUtil.initPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
    }

    // 확진 현황 요청
    private void requestSituation(int initMillisecond) {
        if (maxCount < 2) {
            HashMap<String, String> map = new HashMap<>();
            map.put("ServiceKey", BuildConfig.PUBLIC_SERVICE_KEY);
            map.put("startCreateDt", ConvertUtil.getYesterdayFormatTime(initMillisecond));
            map.put("endCreateDt", ConvertUtil.getCurrentFormatTime(initMillisecond));

            requestTotal(map);
        }
        ++maxCount;
    }

    // 전체 확진 정보 API 요청
    private void requestTotal(HashMap<String, String> map) {
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
                    // API 정보 갱신 전에 날짜가 변경 될 경우 호출
                    public void reRequest(List<Infection> infection) {
                        requestSituation(ConvertUtil.PREVIOUS_DAY);
                    }

                    @Override
                    public void fail(String message) {
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

        MyApplication.getNetworkPresenterInstance()
                .boardList(map, new BoardListListener() {
                    @Override
                    public void success(List<City> items) {
                        setCityItemList(items);
                        // 레이아웃 업데이트
                        initDailyLayout();
                        initTotalLayout();
                        initCityLayout();
                    }

                    @Override
                    public void fail(String message) {
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
        cityArrayList.sort(new Comparator<City>() {
            @Override
            public int compare(City o1, City o2) {
                return o2.getDefCnt() - o1.getDefCnt();
            }
        });
        // 현재 주소
        String currentAddress = getCurrentAddress();
        Log.d("currentAddress", currentAddress);

        cityArrayList.forEach(new Consumer<City>() {
            @Override
            public void accept(City item) {
                if (item.getGubun().equals(currentAddress)) {
                    city = item;
                }
            }
        });
        cityArrayList.remove(city);
        cityArrayList.add(1, city);
    }

    private void initRefreshListener() {
        binding.loSwipe.setOnRefreshListener(()->{
            // 카운터 초기화
            maxCount = 0;
            requestSituation(0);

            BasicUtil.showSnackBar(
                    getActivity(),
                    getActivity().getWindow().getDecorView().getRootView(),
                    "새로고침 완료");
            // 새로 고침 완료
            binding.loSwipe.setRefreshing(false);
        });
    }

    private void initDailyLayout() {
        binding.tvDailyContetns.setText(String.format("%s 기준",
                ConvertUtil.convertDateDot(infection.getStateDt())));
        // 일일 확진자
        binding.tvDailyInfect.setText(String.format("확진환자 (%s)",
                ConvertUtil.convertCommaSeparator(infection.getDecideCnt() - yesterdayInfection.getDecideCnt())));
        // 일일 완치자
        binding.tvDailyCare.setText(String.format("격리해제 (%s)",
                ConvertUtil.convertCommaSeparator(infection.getClearCnt() - yesterdayInfection.getClearCnt())));
        // 사망자
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
        // 총 완치자
        binding.tvTotalCure.setText(ConvertUtil.convertCommaSeparator(infection.getClearCnt()));
        binding.tvTotalCureCompare.setText(String.format("(%s)",
                ConvertUtil.convertSignCommaSeparator(infection.getClearCnt() - yesterdayInfection.getClearCnt())));
        // 총 사망자
        binding.tvTotalDeath.setText(ConvertUtil.convertCommaSeparator(infection.getDeathCnt()));
        binding.tvTotalDeathCompare.setText(String.format("(%s)",
                ConvertUtil.convertSignCommaSeparator(infection.getDeathCnt() - yesterdayInfection.getDeathCnt())));
        // 누적 검사 수
        binding.tvTotalExam.setText(ConvertUtil.convertCommaSeparator(infection.getAccExamCnt()));
        binding.tvTotalExamCompare.setText(String.format("(%s)",
                ConvertUtil.convertSignCommaSeparator(infection.getAccExamCnt() - yesterdayInfection.getAccExamCnt())));
        // 검사 진행 중
        binding.tvTotalCheck.setText(ConvertUtil.convertCommaSeparator(infection.getExamCnt()));
        binding.tvTotalCheckCompare.setText(String.format("(%s)",
                ConvertUtil.convertSignCommaSeparator(infection.getExamCnt() - yesterdayInfection.getExamCnt())));
        // 결과 음성 수
        binding.tvTotalNegative.setText(ConvertUtil.convertCommaSeparator(infection.getResutlNegCnt()));
        binding.tvTotalNegativeCompare.setText(String.format("(%s)",
                ConvertUtil.convertSignCommaSeparator(infection.getResutlNegCnt() - yesterdayInfection.getResutlNegCnt())));
    }

    private void initCityLayout() {
        binding.recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        CityAdapter adapter = new CityAdapter(cityArrayList);
        binding.recyclerview.setAdapter(adapter);
    }

    // 위치 OFF -> 기본 위치 : 서울
    private String getCurrentAddress() {
        if (LocationUtil.isLocationStatus(getActivity())) {
            Location location = LocationUtil.getLocation(getActivity());
            String address = LocationUtil.getCoordinateToAddress(getActivity(), location);
            Log.d("address", address);
            String[] addresses = address.split("\\s");
            return addressStringConvert(addresses[1]);
        } else {
            // base location
            return getActivity().getResources().getString(R.string.location_base);
        }
    }

    // 특정 문자 변환
    private String addressStringConvert(String address) {
        switch (address) {
            case "경상북도":
                address = "경북";
                break;
            case "경상남도":
                address = "경남";
                break;
            case "충청남도":
                address = "충남";
                break;
            case "충청북도":
                address = "충북";
                break;
            case "전라북도":
                address = "전북";
                break;
            case "전라남도":
                address = "전남";
                break;
            default:
                break;
        }

        return address.substring(0,2);
    }
}