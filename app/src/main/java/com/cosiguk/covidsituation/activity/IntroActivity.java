package com.cosiguk.covidsituation.activity;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.viewpager2.widget.ViewPager2;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.adapter.ViewPagerAdapter;
import com.cosiguk.covidsituation.databinding.ActivityIntroBinding;
import com.cosiguk.covidsituation.databinding.CloseToolbarBinding;

public class IntroActivity extends BaseActivity {
    public static final int NUM_PAGES = 5;
    private ViewPagerAdapter adapter;
    private ActivityIntroBinding binding;
    private CloseToolbarBinding closeToolbarBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(IntroActivity.this, R.layout.activity_intro);
        closeToolbarBinding = DataBindingUtil.bind(binding.introToolbar.toolbar);

        initLayout();
        initEvent();
    }

    private void initLayout() {
        setStatusColor(getColor(R.color.status_white));
        closeToolbarBinding.toolbarTitle.setText("");
        adapter = new ViewPagerAdapter(IntroActivity.this, NUM_PAGES);
        binding.pager.setAdapter(adapter);

        // 인디케이터 등록
        binding.indicator.setViewPager(binding.pager);
        binding.indicator.createIndicators(NUM_PAGES, 0);

        binding.pager.setOffscreenPageLimit(3);
    }

    private void initEvent() {
        binding.pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                if (positionOffsetPixels == 0) {
                    binding.pager.setCurrentItem(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                switch (position) {
                    case 0 :
                        binding.tvPrevious.setVisibility(View.INVISIBLE);
                        binding.tvNext.setVisibility(View.VISIBLE);
                        break;
                    case 1 :
                    case 2 :
                    case 3 :
                        binding.tvPrevious.setVisibility(View.VISIBLE);
                        binding.tvNext.setVisibility(View.VISIBLE);
                        binding.tvNext.setText(getResources().getString(R.string.pager_next));
                        break;
                    case 4 :
                        binding.tvNext.setText(getResources().getString(R.string.pager_close));
                        break;
                }
            }
        });
        // 버튼 클릭 리스너
        binding.tvNext.setOnClickListener(v -> nextPage());
        binding.tvPrevious.setOnClickListener(v -> previousPage());
        closeToolbarBinding.ivLeave.setOnClickListener(v -> {
            setStatusColor(getColor(R.color.status_bar));
            finish();
        });
    }

    private void nextPage() {
        int currentPages = binding.pager.getCurrentItem();
        if (currentPages == 4) {
            finish();
        } else {
            binding.pager.setCurrentItem(currentPages + 1, true);
        }
    }

    private void previousPage() {
        int currentPages = binding.pager.getCurrentItem();
        binding.pager.setCurrentItem(currentPages - 1, true);
    }

    @Override
    public void onBackPressed() {
        setStatusColor(getColor(R.color.status_bar));
        finish();
    }
}