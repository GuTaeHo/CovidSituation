package com.cosiguk.covidsituation.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import java.util.List;

public class LocationUtil {

    // 현재 위치 정보를 포함한 객체 반환
    public static Location getPosition(Context context) {
        Location location = null;

        if (BasicUtil.checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
            LocationManager locationManager = (LocationManager)context
                    .getSystemService(Context.LOCATION_SERVICE);

            List<String> providers = locationManager.getProviders(true);

            // provides 객체의 모든 위치 제공자(GPS, NETWORK, PASSIVE)들로 부터 마지막 위치를 가져와 저장
            for (String provider : providers) {
                Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
                if (lastKnownLocation == null) {
                    continue;
                }

                if (location == null || lastKnownLocation.getAccuracy() < location.getAccuracy()) {
                    location = lastKnownLocation;
                }
            }
        } else {
            String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION};

            BasicUtil.initPermissions(context, permission);
        }
        return location;
    }

    // 위치 서비스 활성화 상태
    public static boolean isLocationStatus(Context context) {
        LocationManager locationManager = (LocationManager)context
                .getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
