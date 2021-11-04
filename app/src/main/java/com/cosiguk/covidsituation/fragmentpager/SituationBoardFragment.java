package com.cosiguk.covidsituation.fragmentpager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cosiguk.covidsituation.R;

public class SituationBoardFragment extends Fragment {

    public SituationBoardFragment() {
    }

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
        return inflater.inflate(R.layout.pager_situation_board, container, false);
    }
}