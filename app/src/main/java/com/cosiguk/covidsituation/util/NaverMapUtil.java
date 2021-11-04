package com.cosiguk.covidsituation.util;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cosiguk.covidsituation.R;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Align;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

import java.util.ArrayList;
import java.util.HashMap;

public class NaverMapUtil {
    private static ArrayList<Marker> markers;
    private static ArrayList<InfoWindow> windows;
    private static FragmentTransaction fragmentTransaction;
    private static FragmentManager fragmentManager;
    private static MapFragment mapView;

    public static final String LAT = "lat";
    public static final String LNG = "lng";
    public static final String CAP = "caption";
    public static final String SUB_CAP = "sub_caption";
    public static final String PHONE_NUM = "phone_num";
    public static final String DISTANCE = "distance";

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
        CameraUpdate cameraUpdate =
                CameraUpdate.scrollAndZoomTo(new LatLng(loc.getLatitude(), loc.getLongitude()), 10);
        map.moveCamera(cameraUpdate);
    }

    public static void initUI(NaverMap map) {
        UiSettings settings = map.getUiSettings();
        settings.setZoomControlEnabled(false);
    }

    public static void removeMarkers() {
        if (markers != null) {
            markers.forEach(marker -> marker.setMap(null));
        }
    }

    public static void setMarkersAndCaptions(Context context, HashMap<String, String> map) {
        if (markers == null) {
            markers = new ArrayList<>();
            windows = new ArrayList<>();
        }
        Marker marker = new Marker();
        InfoWindow infoWindow = new InfoWindow();
        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(context) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return (CharSequence)"전화번호 : " + map.get(PHONE_NUM)+"\n거리 : " + String.format("%.2f", Float.parseFloat(map.get(DISTANCE))) + "km";
            }
        });
        marker.setPosition(new LatLng(Double.parseDouble(map.get(LAT)), Double.parseDouble(map.get(LNG))));
        marker.setCaptionAligns(Align.Top);
        marker.setCaptionTextSize(10);
        marker.setCaptionRequestedWidth(200);
        marker.setCaptionMinZoom(11);
        marker.setCaptionMaxZoom(18);
        marker.setSubCaptionText(map.get(SUB_CAP));
        marker.setSubCaptionColor(Color.rgb(77, 121, 255));
        marker.setSubCaptionHaloColor(Color.rgb(230, 236, 255));
        marker.setSubCaptionTextSize(8);
        marker.setHideCollidedSymbols(true);
        marker.setCaptionText(map.get(CAP));
        marker.setOnClickListener(overlay -> {
            if (marker.getInfoWindow() == null) {
                // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                infoWindow.open(marker);
            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                infoWindow.close();
            }
            return true;
        });
        markers.add(marker);
        windows.add(infoWindow);
    }

    // 마커들 초기화
    public static void setMarkers(NaverMap map) {
        if (markers != null) {
            markers.forEach(marker -> {
                marker.setWidth(100);
                marker.setHeight(130);
                marker.setMap(map);
            });
        }
    }

    public static void initOnMapClickListener(NaverMap map) {
        // 지도를 클릭하면 정보 창을 닫음
        map.setOnMapClickListener((coord, point) -> {
            windows.forEach(InfoWindow::close);
        });
    }

    public static void setCurrentMarker(NaverMap map, Location loc) {
        if (map != null) {
            Marker marker = new Marker();
            marker.setWidth(110);
            marker.setHeight(145);
            marker.setPosition(new LatLng(loc.getLatitude(), loc.getLongitude()));
            marker.setIcon(OverlayImage.fromResource(R.drawable.ic_android_studio));
            marker.setMap(map);
        }
    }

    public static void setZoom(NaverMap map, int maxLevel, int minLevel) {
        map.setMaxZoom(maxLevel);
        map.setMinZoom(minLevel);
    }
}
