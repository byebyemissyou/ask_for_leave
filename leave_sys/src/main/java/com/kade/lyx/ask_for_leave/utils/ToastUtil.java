package com.kade.lyx.ask_for_leave.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kade.lyx.ask_for_leave.R;

/**
 * Created by zzg
 */
public class ToastUtil {

    private static Toast toast;
    private static TextView msg_tv;

    //本工具类主要用于在Toast消失前不产生重复的Toast，以减小资源开销，提升体验
    public static void showToast(Context context, String content) {

        if (toast == null) {
            toast = new Toast(context);
            toast.setGravity(Gravity.CENTER, 0, 0);
            View view = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
            msg_tv = (TextView) view.findViewById(R.id.msg_tv);
            msg_tv.setText(content);
            toast.setView(view);
//            toast = Toast.makeText(context);
        } else {
            msg_tv.setText(content);
        }

        toast.show();
    }


}
