package com.cosiguk.covidsituation.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.databinding.ActivityBoardBinding;

public class BoardActivity extends BaseActivity {
    private ActivityBoardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(BoardActivity.this, R.layout.activity_board);
    }
}