package com.cosiguk.covidsituation.util;

import android.view.View;

public class ViewUtil {

    public static int getAbsoluteHeight(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        return location[1] + view.getHeight();
    }
}
