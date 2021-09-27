package com.cosiguk.covidsituation.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ProgressDialogManager {
    // static으로 선언되어있기 때문에 객체 생성없이 다른(상속받은) 클래스에서 바로 사용이 가능
    public static ProgressDialog showSingle(Context context, ProgressDialog progressDialog, String title, String message) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(context, title, message);
        } else {
            // 기존 다이얼로그를 재사용
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);

            // 다이얼로그 출력
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }

        return progressDialog;
    }

    // 다이얼로그 삭제
    public static void dismiss(DialogInterface d) {
        if (d == null)
            return;

        // 다이얼로그 종류에 맞게 종료
        try {
            if (d instanceof AlertDialog) {
                if (((AlertDialog) d).isShowing())
                    ((AlertDialog) d).dismiss();

                return;
            }

            if (d instanceof ProgressDialog) {
                if (((ProgressDialog) d).isShowing())
                    ((ProgressDialog) d).dismiss();

                return;
            }

            if (d instanceof Dialog) {
                if (((Dialog) d).isShowing())
                    ((Dialog) d).dismiss();

                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
