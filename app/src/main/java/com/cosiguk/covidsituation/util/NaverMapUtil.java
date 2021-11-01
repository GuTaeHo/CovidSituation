package com.cosiguk.covidsituation.util;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.cosiguk.covidsituation.R;
import com.naver.maps.map.MapFragment;

public class NaverMapUtil {
    private static FragmentManager fm;
    private static MapFragment mapFragment;

    // 프래그먼트 관리자 반환
    public static FragmentManager getManager(Fragment fragment) {
        if (fm != null) {
            return fm;
        }
        return fragment.getChildFragmentManager();
    }

    // 맵 객체 반환
    public static MapFragment getInstance(Fragment fragment) {
        if (fm == null) {
            fm = fragment.getChildFragmentManager();
        }
        mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        return mapFragment;
    }
}
