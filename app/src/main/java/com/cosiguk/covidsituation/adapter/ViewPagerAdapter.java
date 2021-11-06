package com.cosiguk.covidsituation.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.cosiguk.covidsituation.activity.IntroActivity;
import com.cosiguk.covidsituation.fragmentpager.BoardFragment;
import com.cosiguk.covidsituation.fragmentpager.IntroFragment;
import com.cosiguk.covidsituation.fragmentpager.NewsFragment;
import com.cosiguk.covidsituation.fragmentpager.SituationBoardFragment;
import com.cosiguk.covidsituation.fragmentpager.VaccineFragment;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private int pageCount;
    private ArrayList<Fragment> fragments;

    public ViewPagerAdapter(FragmentActivity fragment, int pageCount) {
        super(fragment);
        this.pageCount = pageCount;
        this.fragments = new ArrayList<>();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getCurrentPosition(position);

        switch (index) {
            case 0 :
                return IntroFragment.newInstance();
            case 1 :
                return SituationBoardFragment.newInstance();
            case 2 :
                return VaccineFragment.newInstance();
            case 3 :
                return NewsFragment.newInstance();
            case 4 :
                return BoardFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return IntroActivity.NUM_PAGES;
    }

    public int getCurrentPosition(int position) {
        return position % pageCount;
    }
}
