package com.cosiguk.covidsituation.fragment;

import android.app.FragmentBreadCrumbs;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.application.MyApplication;
import com.cosiguk.covidsituation.databinding.FragmentSituationBoardBinding;
import com.cosiguk.covidsituation.dialog.NoticeDialog;
import com.cosiguk.covidsituation.model.Item;
import com.cosiguk.covidsituation.model.ItemTotal;
import com.cosiguk.covidsituation.network.RetrofitCityClient;
import com.cosiguk.covidsituation.network.RetrofitTotalClient;
import com.cosiguk.covidsituation.network.responsetotal.Items;
import com.cosiguk.covidsituation.network.resultInterface.BoardListListener;
import com.cosiguk.covidsituation.network.resultInterface.DailyListener;

public class SituationBoardFragment extends Fragment {
    private FragmentSituationBoardBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    // 일일 확진 정보
    private ItemTotal itemTotal;

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
        initDailySituation();
    }

    private void initDailySituation() {
        MyApplication.networkPresenter
                .daily(RetrofitTotalClient.SERVICE_KEY, new DailyListener() {
                    @Override
                    public void success(ItemTotal items) {
                        setItems(items);
                    }
                    @Override
                    public void fail(String message) {
                        new NoticeDialog(getActivity())
                                .setMsg(message)
                                .show();
                    }
                });
    }

    public void setItems(ItemTotal responseItemTotal) {
        itemTotal = responseItemTotal;
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

        binding.tvDailyInfect.setText(itemTotal.getDecideCnt());
        initLayout();

        return binding.getRoot();
    }

    private void initLayout() {
        /*
        // 격리중 (치료중)
        binding.tvSeparate.setText("격리중 ("+itemTotal.careCnt + ")");
        // 일일 확진자
        binding.tvDailyInfect.setText("일일 확진자 ("+itemTotal.decideCnt+")");
        // 일일 완치자
        binding.tvDailyCare.setText("일일 완치자 ("+itemTotal.clearCnt+")");
         */
    }
}