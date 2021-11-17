package com.cosiguk.covidsituation.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class LocationUtil {
    private static LocationManager locationManager;
    private static Location location;
    private static int UPDATE_MILLISECOND = 5000;
    private static int UPDATE_METER = 10;

    // 현재 위치 정보를 포함한 객체 반환
    @SuppressLint("MissingPermission")
    public static Location getLastLocation(Context context) {
        Location location = null;

        if (BasicUtil.isGranted(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
            locationManager = (LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);

            List<String> providers = locationManager.getProviders(true);
            setLocationUpdateEvent();

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

    // 위치기능 활성화 체크
    public static boolean isConnect(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // 좌표 -> 주소
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

    @SuppressLint("MissingPermission")
    private static void setLocationUpdateEvent() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                UPDATE_MILLISECOND,
                UPDATE_METER,
                gpsLocationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                UPDATE_MILLISECOND,
                UPDATE_METER,
                gpsLocationListener);
    }

    private static final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            Log.d("locationEvent", "위치 업데이트 : " + location.getLongitude() + ", " + location.getLatitude());
            setLocation(location);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };

    // Util 클래스의 location 변수에 현재 위치 값 저장
    private static void setLocation(Location loc) {
        location = loc;
    }

    public static Location getLocation() {
        if (location == null) {
            setBaseLocation();
            return location;
        }
        return location;
    }

    // 기본 위치 (서울)
    public static Location setBaseLocation() {
        Location location = new Location("dummy provider");
        location.setLatitude(37.5511565);
        location.setLongitude(126.9693536);
        return location;
    }

    /**
     * 두 지점 간의 거리
     * @param lat1 지점 1 위도
     * @param lon1 지점 1 경도
     * @param lat2 지점 2 위도
     * @param lon2 지점 2 경도
     */
    public static float computeDistance(double lat1, double lon1, double lat2, double lon2) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        dist = dist * 1.609344;

        return (float)dist;
    }

    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}
