package com.cosiguk.covidsituation.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.cosiguk.covidsituation.R;
import com.cosiguk.covidsituation.databinding.CommonToolbarBinding;
import com.google.android.material.snackbar.Snackbar;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class BasicUtil {
    public static String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    // 버전 비교 결과 코드
    public static final int VERSION_ERROR = -1;     // 버전정보 조회 에러
    public static final int VERSION_LATEST = 0;     // 최신버전
    public static final int VERSION_LOW_MAJOR = 1;  // 메이저 버전 낮음
    public static final int VERSION_LOW_MINOR = 2;  // 마이너 버전 낮음
    public static final int VERSION_LOW_PATCH = 3;  // 패치 버전 낮음

    public static int stringToInt(String s) {
        return Integer.parseInt(s);
    }

    public static int isVersionValid(String deviceVersion, String serverVersion) {
        try {
            // .(닷)을 기준으로 deviceVersion 의 문자열을 잘라 각각의 배열 인덱스에 할당
            String[] deviceVersionArr = deviceVersion.split("\\.", -1);
            String[] serverVersionArr = serverVersion.split("\\.", -1);

            // 1.0
            int deviceMajor = stringToInt(deviceVersionArr[0]);
            int deviceMiner = stringToInt(deviceVersionArr[1]);
            int devicePatch;
            try {
                // 세 번째 인덱스에 대한 인덱스 공간이 확보가 안될 가능성이 있기 때문에 (예 : 버전이 1.0 인 경우)
                // try catch 로 예외 처리 (NullPointerException)
                devicePatch = stringToInt(deviceVersionArr[2]);
            } catch (Exception e) {
                // 예외일 경우 0을 저장 (예 : 버전이 2.7 인 경우 => 2.7.0 형태로 만들기 위함)
                devicePatch = 0;
            }

            int serverMajor = stringToInt(serverVersionArr[0]);
            int serverMiner = stringToInt(serverVersionArr[1]);
            int serverPatch;
            try {
                serverPatch = stringToInt(serverVersionArr[2]);
            } catch (Exception e) {
                serverPatch = 0;
            }


            // 서버와 클라이언트간의 버전이 다를 경우 if문 실행
            if (serverMajor > deviceMajor) {
                return VERSION_LOW_MAJOR;
            } else if (serverMajor == deviceMajor) {
                if (serverMiner > deviceMiner) {
                    return VERSION_LOW_MINOR;
                } else if (serverMiner == deviceMiner) {
                    if (serverPatch > devicePatch) {
                        return VERSION_LOW_PATCH;
                    }
                }
            }

            // 서버와 클라이언트간 버전이 같을 경우 최신버전에 해당하는 0을 리턴
            return VERSION_LATEST;
        } catch (Exception e) {
            return VERSION_ERROR;
        }
    }

    // 디버깅 여부 반환
    public static boolean isDebugable(Context context) {
        boolean isDebug = false;
        // 패키지 매니저를 시스템에서 획득
        PackageManager packageManager = context.getPackageManager();

        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            // 디버깅모드라면 true 반환
            isDebug = (0 != (applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return isDebug;
    }

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

    public static boolean isGranted(Context context, String permission) {
        int permissionResult = ActivityCompat.checkSelfPermission(context, permission);
        return permissionResult == PackageManager.PERMISSION_GRANTED;
    }
}
