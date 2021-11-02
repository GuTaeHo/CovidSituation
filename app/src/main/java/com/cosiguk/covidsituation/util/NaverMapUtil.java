package com.cosiguk.covidsituation.util;

import android.location.Location;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cosiguk.covidsituation.R;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.Marker;

import java.util.ArrayList;
import java.util.function.Consumer;

public class NaverMapUtil {
    private static ArrayList<Marker> markers;
    private static FragmentTransaction fragmentTransaction;
    private static FragmentManager fragmentManager;
    private static MapFragment mapView;

    // 맵 객체 반환
    public static MapFragment getInstance(Fragment fragment) {
        if (fragmentManager == null) {
            fragmentManager = fragment.getChildFragmentManager();
        }
        mapView = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        if (mapView == null) {
            mapView = MapFragment.newInstance();
            fragmentManager.beginTransaction().add(R.id.map, mapView).commit();
        }
        return mapView;
    }

    public static void moveCamera(NaverMap map, Location loc) {
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(loc.getLatitude(), loc.getLongitude()));
        map.moveCamera(cameraUpdate);
    }

    public static void initMarkers(Marker marker) {
        if (markers == null) {
            markers = new ArrayList<>();
        }
        markers.add(marker);
    }

    public static void removeMarkers() {
        if (markers != null) {
            markers.clear();
        }
    }

    public static void setMarker(NaverMap map) {
        if (markers != null) {
            markers.forEach(new Consumer<Marker>() {
                @Override
                public void accept(Marker marker) {
                    marker.setMap(map);
                }
            });
        }
    }

    public static void setZoom(NaverMap map, int maxLevel, int minLevel) {
        map.setMaxZoom(maxLevel);
        map.setMinZoom(minLevel);
    }
}
