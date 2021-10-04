package com.cosiguk.covidsituation.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.application.MyApplication;
import com.cosiguk.covidsituation.databinding.FragmentSituationBoardBinding;
import com.cosiguk.covidsituation.dialog.NoticeDialog;
import com.cosiguk.covidsituation.model.ItemTotal;
import com.cosiguk.covidsituation.network.RetrofitTotalClient;
import com.cosiguk.covidsituation.network.resultInterface.TotalListener;
import com.cosiguk.covidsituation.util.ConvertUtil;

import java.util.HashMap;
import java.util.List;

public class SituationBoardFragment extends Fragment {
    private FragmentSituationBoardBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    // 전체 확진 정보
    private ItemTotal itemTotal;
    // 전일 확진 정보
    private ItemTotal yesterdayItemTotal;

    public SituationBoardFragment() {
        // Required empty public constructor
    }

    public static SituationBoardFragment newInstance(String param1, String param2) {
        SituationBoardFragment fragment = new SituationBoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // 사용자 목록 초기화
    private void initList() {/*
        networkPresenter.boardList(RetrofitCityClient.SERVICE_KEY, new BoardListListener() {
            @Override
            public void success(Items items) {
            }

            @Override
            public void fail(String message) {

            }
        });
        */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_situation_board, container, false);

        initSituation();

        return binding.getRoot();
    }

    // 확진 현황 요청
    private void initSituation() {
        HashMap<String, String> map = new HashMap<>();
        map.put("ServiceKey", RetrofitTotalClient.SERVICE_KEY);
        map.put("startCreateDt", ConvertUtil.getYesterdayFormatTime());
        map.put("endCreateDt", ConvertUtil.getCurrentFormatTime());

        MyApplication.networkPresenter
                .total(map, new TotalListener() {
                    @Override
                    public void success(List<ItemTotal> items) {
                        // 금일 정보는 리스트의 첫 번째에 포함되어 있음
                        setDailyItems(items.get(0));
                        // 작일 정보는 리스트의 두 번째에 포함되어 있음
                        setYesterdayItems(items.get(1));
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

    public void setDailyItems(ItemTotal responseDailyTotal) {
        itemTotal = responseDailyTotal;
    }
    public void setYesterdayItems(ItemTotal responseYesterdayTotal) {
        yesterdayItemTotal = responseYesterdayTotal;
    }

    private void initLayout() {
        // 일일 현황
        binding.tvDailyTitle.setText(String.format("일일 확진 현황 (%s 기준)", ConvertUtil.covertDateDot(itemTotal.getStateDt())));
        // 일일 확진자
        binding.tvDailyInfect.setText(String.format("확진환자 (%s)", ConvertUtil.convertCommaSeparator(itemTotal.getDecideCnt() - yesterdayItemTotal.getDecideCnt())));
        // 일일 완치자
        binding.tvDailyCare.setText(String.format("격리해제 (%s)", ConvertUtil.convertCommaSeparator(itemTotal.getClearCnt() - yesterdayItemTotal.getClearCnt())));
        // 사망자
        binding.tvDailyDeath.setText(String.format("사망자 (%s)", ConvertUtil.convertCommaSeparator(itemTotal.getDeathCnt()-yesterdayItemTotal.getDeathCnt())));

        // 전체 현황
        // 현황 날짜
        binding.tvTotalTitle.setText(String.format("전체 감염 현황 (%s 기준)", ConvertUtil.covertDateDot(itemTotal.getStateDt())));
        // 총 확진자
        binding.tvTotalInfect.setText(ConvertUtil.convertCommaSeparator(itemTotal.getDecideCnt()));
        binding.tvTotalInfectCompare.setText(String.format("(+%s)", ConvertUtil.convertCommaSeparator(itemTotal.getDecideCnt() - yesterdayItemTotal.getDecideCnt())));
        // 총 완치자
        binding.tvTotalCure.setText(ConvertUtil.convertCommaSeparator(itemTotal.getClearCnt()));
        binding.tvTotalCureCompare.setText(String.format("(+%s)", ConvertUtil.convertCommaSeparator(itemTotal.getClearCnt())));
        // 총 사망자
        binding.tvTotalDeath.setText(ConvertUtil.convertCommaSeparator(itemTotal.getDeathCnt()));
        // 누적 검사 수
        binding.tvTotalExam.setText(ConvertUtil.convertCommaSeparator(itemTotal.getAccExamCnt()));
        // 검사 진행 중
        binding.tvTotalCheck.setText(ConvertUtil.convertCommaSeparator(itemTotal.getExamCnt()));
        // 결과 음성 수
        binding.tvTotalNegative.setText(ConvertUtil.convertCommaSeparator(itemTotal.getResutlNegCnt()));
    }
}