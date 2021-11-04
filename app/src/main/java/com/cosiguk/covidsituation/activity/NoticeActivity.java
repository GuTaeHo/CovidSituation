package com.cosiguk.covidsituation.activity;

import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.databinding.ActivityNoticeBinding;
import com.cosiguk.covidsituation.databinding.CloseToolbarBinding;

public class NoticeActivity extends BaseActivity {
    private ActivityNoticeBinding binding;
    private CloseToolbarBinding closeToolbarBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(NoticeActivity.this, R.layout.activity_notice);
        closeToolbarBinding = DataBindingUtil.bind(binding.closeToolbar.toolbar);

        initLayout();
        initEvent();
    }

    private void initLayout() {
        closeToolbarBinding.toolbar.setBackground(ResourcesCompat.getDrawable(getResources(), R.color.app_background, null));
        closeToolbarBinding.toolbarTitle.setText(getResources().getString(R.string.side_contents_notice_title));
    }

    private void initEvent() {
        closeToolbarBinding.ivLeave.setOnClickListener(v -> finish());
    }
}