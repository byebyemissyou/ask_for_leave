package com.kade.lyx.ask_for_leave.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.kade.lyx.ask_for_leave.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zzg on 2017/10/24 0024.
 * function :
 * note     :
 */

public class UtilTools {
    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurTime() {
        Date now = new Date();
        // SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmm");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(now);
        // String time = dateFormat.format(now);
        return time;
    }

    /**
     * 生成随机数
     *
     * @param sum 生成随机数个数
     * @return
     */
    public static String crateRandomNumber(int sum) {
        if (sum == 0) {
            return "0";
        }
        StringBuilder _data = new StringBuilder();
        for (int i = 0; i < sum; i++) {
            double _random_number = Math.random();
            String _s = (int) (_random_number * 10) + "";
            if (_s.equals("10")) {
                _s = "1";
            }
            _data.append(_s);
        }
        return _data.toString();
    }

    public static String getMD5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
//            System.out.println("MD5(" + sourceStr + ",32) = " + result);
//            System.out.println("MD5(" + sourceStr + ",16) = " + result.substring(8, 24));
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return result.substring(8, 24).toUpperCase();
    }

    /**
     * 判断手机网络
     *
     * @param activity
     * @return
     */
    public static boolean hasInternet(Context activity) {
        ConnectivityManager manager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null || !info.isAvailable()) {
            return false;
        }
        if (info.isRoaming()) {
            return true;
        }
        return true;
    }

    /**
     * 检查网络环境并提示用户
     *
     * @return
     */
    public static void showNetWorkWarring(final Context context) {
        Dialog dialog =
                new AlertDialog.Builder(context).setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.prompt).setMessage(R.string.NetWork_prompt)
                        .setNegativeButton(R.string.label_cancel, null)
                        .setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent mIntent = new Intent(Settings.ACTION_SETTINGS);
                                mIntent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                                context.startActivity(mIntent);
                            }
                        }).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
