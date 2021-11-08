package com.cosiguk.covidsituation.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.databinding.FragmentBoardBinding;

public class BoardFragment extends Fragment {
    private FragmentBoardBinding binding;

    public BoardFragment() {
    }

    public static BoardFragment newInstance() {
        BoardFragment fragment = new BoardFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board, container, false);
        return binding.getRoot();
    }
}