package com.cosiguk.covidsituation.fragmentpager;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.databinding.PagerVaccineBinding;

public class VaccineFragment extends Fragment {

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
        return inflater.inflate(R.layout.pager_vaccine, container, false);
    }
}