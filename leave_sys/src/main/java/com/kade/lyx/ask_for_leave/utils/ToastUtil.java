package com.kade.lyx.ask_for_leave.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by lyx on 2016/10/9 0009.
 */
public class ToastUtil {

    private static Toast toast;
    //本工具类主要用于在Toast消失前不产生重复的Toast，以减小资源开销，提升体验
    public static void showToast(Context context, String content) {

        if (toast == null) {

            toast = Toast.makeText(context, content, Toast.LENGTH_LONG);


        } else {

            toast.setText(content);

        }

        toast.show();
    }



}
