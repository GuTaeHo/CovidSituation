package com.cosiguk.covidsituation.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ActivityUtil {
    // Url 액티비티 실행 (웹브라우저 호출)
    public static void startUrlActivity(Context context, String url) {
        // url을 파싱해 적절한 액티비티 호출
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }
}
