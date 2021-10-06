package com.cosiguk.covidsituation.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.adapter.CityAdapter;
import com.cosiguk.covidsituation.application.MyApplication;
import com.cosiguk.covidsituation.databinding.FragmentSituationBoardBinding;
import com.cosiguk.covidsituation.dialog.NoticeDialog;
import com.cosiguk.covidsituation.model.ItemCity;
import com.cosiguk.covidsituation.model.ItemTotal;
import com.cosiguk.covidsituation.network.RetrofitPublicClient;
import com.cosiguk.covidsituation.network.resultInterface.BoardListListener;
import com.cosiguk.covidsituation.network.resultInterface.TotalListener;
import com.cosiguk.covidsituation.util.ConvertUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class SituationBoardFragment extends Fragment {
    private static final String FRAG_TAG = "fragmentTag";

    private FragmentSituationBoardBinding binding;
    // 전체 확진 정보
    private ItemTotal itemTotal;
    // 전일 확진 정보
    private ItemTotal yesterdayItemTotal;
    // 시, 도 확진 정보 리스트
    private List<ItemCity> itemCityArrayList;
    // 재귀 요청 방지
    int maxCount;

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

        requestSituation(0);
        initRefreshListener();
        return binding.getRoot();
    }

    // 확진 현황 요청
    private void requestSituation(int initMillisecond) {
        if (maxCount < 2) {
            HashMap<String, String> map = new HashMap<>();
            map.put("ServiceKey", RetrofitPublicClient.SERVICE_KEY);
            map.put("startCreateDt", ConvertUtil.getYesterdayFormatTime(initMillisecond));
            map.put("endCreateDt", ConvertUtil.getCurrentFormatTime(initMillisecond));

            requestTotal(map);
        }
        ++maxCount;
    }

    // 전체 확진 정보 API 요청
    private void requestTotal(HashMap<String, String> map) {
        MyApplication.networkPresenter
                .total(map, new TotalListener() {
                    @Override
                    public void success(List<ItemTotal> items) {
                        // 작일 정보는 리스트의 두 번째에 포함되어 있음
                        setYesterdayItems(items.get(1));
                        // 금일 정보는 리스트의 첫 번째에 포함되어 있음
                        setDailyItems(items.get(0));
                        Log.d("TTAAGG","success() 호출, 시작 날짜 : " + map.get("startCreateDt") + ", 끝 날짜 : "+map.get("endCreateDt"));
                        // 레이아웃 업데이트
                        initDailyLayout();
                        initTotalLayout();
                        // 도시 정보 요청
                        requestCity(map.get("endCreateDt"));
                    }

                    @Override
                    // API 정보 갱신 전에 날짜가 변경 될 경우 호출
                    public void reRequest(List<ItemTotal> itemTotal) {
                        Log.d("TTAAGG","reRequest() 호출, 시작 날짜 : " + map.get("startCreateDt") + ", 끝 날짜 : "+map.get("endCreateDt"));
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
        map.put("ServiceKey", RetrofitPublicClient.SERVICE_KEY);
        map.put("startCreateDt", day);
        map.put("endCreateDt", day);

        MyApplication.networkPresenter
                .boardList(map, new BoardListListener() {
                    @Override
                    public void success(List<ItemCity> items) {
                        setCityItemList(items);
                        // 레이아웃 업데이트
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


    // 금일 정보 초기화
    public void setDailyItems(ItemTotal responseDailyTotal) {
        itemTotal = responseDailyTotal;
    }
    // 작일 정보 초기화
    public void setYesterdayItems(ItemTotal responseYesterdayTotal) {
        yesterdayItemTotal = responseYesterdayTotal;
    }
    // 시, 도 정보 초기화 (확진자 순으로 정렬)
    public void setCityItemList(List<ItemCity> items) {
        itemCityArrayList = items;

        // 내림차순 (확진자 기준)
        itemCityArrayList.sort(new Comparator<ItemCity>() {
            @Override
            public int compare(ItemCity o1, ItemCity o2) {
                return o2.getIncDec() - o1.getIncDec();
            }
        });
    }

    private void initRefreshListener() {
        binding.loSwipe.setOnRefreshListener(()->{
            // 새로고침
            maxCount = 0;
            requestSituation(0);

            // 새로 고침 완료
            binding.loSwipe.setRefreshing(false);
        });
    }

    private void initDailyLayout() {
        binding.tvDailyContetns.setText(String.format("(%s 기준)", ConvertUtil.covertDateDot(itemTotal.getStateDt())));
        // 일일 확진자
        binding.tvDailyInfect.setText(String.format("확진환자 (%s)", ConvertUtil.convertCommaSeparator(itemTotal.getDecideCnt() - yesterdayItemTotal.getDecideCnt())));
        // 일일 완치자
        binding.tvDailyCare.setText(String.format("격리해제 (%s)", ConvertUtil.convertCommaSeparator(itemTotal.getClearCnt() - yesterdayItemTotal.getClearCnt())));
        // 사망자
        binding.tvDailyDeath.setText(String.format("사망자 (%s)", ConvertUtil.convertCommaSeparator(itemTotal.getDeathCnt()-yesterdayItemTotal.getDeathCnt())));
    }

    private void initTotalLayout() {
        // 현황 날짜
        binding.tvTotalContents.setText(String.format("(%s 기준)", ConvertUtil.covertDateDot(itemTotal.getStateDt())));
        // 총 확진자
        binding.tvTotalInfect.setText(ConvertUtil.convertCommaSeparator(itemTotal.getDecideCnt()));
        binding.tvTotalInfectCompare.setText(String.format("(%s)", ConvertUtil.convertSignCommaSeparator(itemTotal.getDecideCnt() - yesterdayItemTotal.getDecideCnt())));
        // 총 완치자
        binding.tvTotalCure.setText(ConvertUtil.convertCommaSeparator(itemTotal.getClearCnt()));
        binding.tvTotalCureCompare.setText(String.format("(%s)", ConvertUtil.convertSignCommaSeparator(itemTotal.getClearCnt() - yesterdayItemTotal.getClearCnt())));
        // 총 사망자
        binding.tvTotalDeath.setText(ConvertUtil.convertCommaSeparator(itemTotal.getDeathCnt()));
        binding.tvTotalDeathCompare.setText(String.format("(%s)", ConvertUtil.convertSignCommaSeparator(itemTotal.getDeathCnt() - yesterdayItemTotal.getDeathCnt())));
        // 누적 검사 수
        binding.tvTotalExam.setText(ConvertUtil.convertCommaSeparator(itemTotal.getAccExamCnt()));
        binding.tvTotalExamCompare.setText(String.format("(%s)", ConvertUtil.convertSignCommaSeparator(itemTotal.getAccExamCnt() - yesterdayItemTotal.getAccExamCnt())));
        // 검사 진행 중
        binding.tvTotalCheck.setText(ConvertUtil.convertCommaSeparator(itemTotal.getExamCnt()));
        binding.tvTotalCheckCompare.setText(String.format("(%s)", ConvertUtil.convertSignCommaSeparator(itemTotal.getExamCnt() - yesterdayItemTotal.getExamCnt())));
        // 결과 음성 수
        binding.tvTotalNegative.setText(ConvertUtil.convertCommaSeparator(itemTotal.getResutlNegCnt()));
        binding.tvTotalNegativeCompare.setText(String.format("(%s)", ConvertUtil.convertSignCommaSeparator(itemTotal.getResutlNegCnt() - yesterdayItemTotal.getResutlNegCnt())));
    }

    private void initCityLayout() {
        binding.recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        CityAdapter adapter = new CityAdapter(itemCityArrayList);
        binding.recyclerview.setAdapter(adapter);
    }
}