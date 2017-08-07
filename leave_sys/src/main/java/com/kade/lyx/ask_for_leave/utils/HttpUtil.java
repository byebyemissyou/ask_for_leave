package com.kade.lyx.ask_for_leave.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by zh on 2017/7/20.
 */

public class HttpUtil {

    private static final Handler handler = new Handler(Looper.getMainLooper());

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain
                            .request()
                            .newBuilder()
                            .build();
                    return chain.proceed(request);
                }
            })
            .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
            .writeTimeout(10, TimeUnit.SECONDS)//设置写的超时时间
            .connectTimeout(15, TimeUnit.SECONDS)//设置连接超时时间
            .build();


    /**
     * 头像上传
     */
    public static void updateHeader(String id,
                                 File file, final UIDataListener listener) {
        String url = "shopappajx/shopAppCouponAction_saveCoupon.htm";
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("couponImage", file);
        uploadImgAndParameter(map, url, listener);
    }

    //上传图片共有方法
    private synchronized final static void uploadImgAndParameter(Map<String, Object> map, String url,
                                                                 final UIDataListener listener) {

        // mImgUrls为存放图片的url集合
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getValue() != null) {
                    if (entry.getValue() instanceof File) {
                        File f = (File) entry.getValue();
                        builder.addFormDataPart(entry.getKey(), f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));
                    } else {
                        builder.addFormDataPart(entry.getKey(), entry.getValue().toString());
                    }
                }

            }
        }
        //创建RequestBody
        RequestBody body = builder.build();

//        MultipartBody requestBody = builder.build();
        //构建Request请求
        final Request request = new Request.Builder()
                .url(url)//地址
                .post(body)//添加请求体
//                .post(requestBody)//添加请求体
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {//判断是否成功
                    final String data = response.body().string();//string()仅可调用一次。否则报IllegalStateException: closed异常
                    Log.i("file1", "上传照片成功-->" + data);
                    onSuccess(listener, data);
                    call.cancel();//上传成功取消请求释放内存
                }
            }
            @Override
            public void onFailure(Call call, final IOException e) {

                Log.i("file2", "上传失败-->" + e.getMessage());
                String msg = e.getMessage();
                if (msg == null || msg.equals("timeout")) {
                    onError(listener, "网络不稳定请求超时!");
                } else {
                    onError(listener, e.getMessage());
                }
                call.cancel();//上传失败取消请求释放内存
            }
        });
    }

    private final static void onSuccess(final UIDataListener listener, final String data) {
        handler.post(new Runnable() {
            public void run() {
                // 需要在主线程的操作。
                listener.onSuccess(data);
            }
        });
    }

    private final static void onError(final UIDataListener listener, final String msg) {
        if (null != listener) {
            handler.post(new Runnable() {
                public void run() {
                    // 需要在主线程的操作。
                    listener.onFailure(msg);
                }
            });
        }
    }

    public interface UIDataListener {
        //网络请求成功
        void onSuccess(String data);

        //网络请求失败
        void onFailure(String errorMassage);
    }
}
