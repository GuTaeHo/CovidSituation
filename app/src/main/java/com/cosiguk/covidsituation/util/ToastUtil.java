package com.cosiguk.covidsituation.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cosiguk.covidsituation.R;

public class ToastUtil {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showDarkToast(Context context, String message) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.toast_dark, null);

        TextView textView = view.findViewById(R.id.tv_toast_content);
        textView.setText(message);

        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    public static void showToastPosition(Context context, String message, int height) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.toast_dark, null);
        TextView textView = view.findViewById(R.id.tv_toast_content);
        textView.setText(message);
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.setGravity(Gravity.TOP, 0, height);
        toast.show();
    }

    public static void showToastCenter(Context context, String message) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.toast_dark, null);
        TextView textView = view.findViewById(R.id.tv_toast_content);
        textView.setText(message);
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
