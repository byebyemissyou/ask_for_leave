package com.kade.lyx.ask_for_leave.network;

import android.os.AsyncTask;

import com.kade.lyx.ask_for_leave.entity.ConstantPool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by lyx on 2016/10/13 0013.
 */

public class Request_Task extends AsyncTask<String, Void, String> {
    private CallBack callBack;
    private Map<String, String> paramsValue;
    private String re_info;

    public Request_Task(Map<String, String> paramsValue, CallBack callBack) {
        this.callBack = callBack;
        this.paramsValue = paramsValue;
    }



    private String sendPOSTRequest(String path, Map<String, String> params) {
        InputStream is = null;
        ByteArrayOutputStream bos = null;

        try {

            StringBuilder sb = new StringBuilder();

            if(params!=null && params.size()!=0){

                for (Map.Entry<String, String> entry : params.entrySet()) {

                    sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "utf-8"));

                    sb.append("&");

                }

                //删除最后一个
                sb.deleteCharAt(sb.length()-1);

            }

            byte[] entity = sb.toString().getBytes();


            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(10000);

            conn.setRequestMethod("POST");

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(entity.length));

            OutputStream os = conn.getOutputStream();

            //以POST方式发送请求体

            os.write(entity);




            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                is = conn.getInputStream();
                bos = new ByteArrayOutputStream();

                int len;
                byte[] buff  = new byte[1024];

                while ((len =is.read(buff))!=-1){

                    bos.write(buff,0,len);
                    bos.flush();
                }
                re_info = new String(bos.toByteArray());

                return re_info;

            }

        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }finally {

            try {
                if (is!=null){
                    is.close();
                }
                if (bos!=null){
                    bos.close();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return ConstantPool.RESULT_FAILED;

    }


    public interface CallBack {

        void doResult(String data);

    }



    @Override
    protected String doInBackground(String... params) {

        String re = sendPOSTRequest(params[0], paramsValue);

        return re;

    }

    @Override
    protected void onPostExecute(String data) {
        super.onPostExecute(data);
            callBack.doResult(data);



    }


}
