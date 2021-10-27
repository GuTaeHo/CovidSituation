package com.cosiguk.covidsituation.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocationUtil {

    // 현재 위치 정보를 포함한 객체 반환
    public static Location getLocation(Context context) {
        Location location = null;

        if (BasicUtil.checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
            LocationManager locationManager = (LocationManager) context
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
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public static String getCoordinateToAddress(Context context, Location location) {
        final Geocoder geocoder = new Geocoder(context);

        List<Address> list = null;
        try {
            list = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (list != null) {
            if (list.size() == 0) {
                return "list 에 아무런 값도 들어있지 않습니다";
            } else {
                return list.get(0).getAddressLine(0);
            }
        } else {
            return "list 가 null 입니다.";
        }
    }
}
