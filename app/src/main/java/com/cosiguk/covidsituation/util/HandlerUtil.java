package com.cosiguk.covidsituation.util;

import android.app.Activity;
import android.os.Handler;

public class HandlerUtil {
    public static void activityDelay(Activity activity, Class tClass, int delay) {
        Handler mHandler = new Handler();
        mHandler.postDelayed(() -> ActivityUtil.startSingleActivity(activity, tClass), delay);
    }
}
