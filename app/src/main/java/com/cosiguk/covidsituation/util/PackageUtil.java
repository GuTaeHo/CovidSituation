package com.cosiguk.covidsituation.util;

import android.content.Context;
import android.content.pm.PackageInfo;

public class PackageUtil {
    public static String getAppVersion(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packageInfo.versionName;
    }
}
