package com.cosiguk.covidsituation.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.cosiguk.covidsituation.R;
import com.google.android.material.snackbar.Snackbar;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class BasicUtil {
    public static String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showSnackBar(Context context, View view, String message) {
        Snackbar.make(context, view, message, Snackbar.LENGTH_SHORT).show();
    }

    // 통합 권한 체크
    public static void initPermissions(Context context, String[] permissions) {
        TedPermission
                .with(context)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Log.d("permission_location", "Granted");
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Log.d("permission_location", "Denied");
                    }
                })
                .setDeniedMessage(context.getString(R.string.location_denied_msg))
                .setGotoSettingButtonText(R.string.location_goto_setting)
                .setPermissions(permissions)
                .check();
    }

    public static boolean checkPermission(Context context, String permission) {
        int permissionResult = ActivityCompat.checkSelfPermission(context, permission);
        return permissionResult == PackageManager.PERMISSION_GRANTED;
    }
}
