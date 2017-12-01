package com.kade.lyx.ask_for_leave.network;

import android.os.AsyncTask;
import android.util.Base64;

import com.kade.lyx.ask_for_leave.entity.ConstantPool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static android.util.Base64.NO_WRAP;

/**
 * Created by lyx on 2016/10/13 0013.
 */

public class Request_Task extends AsyncTask<String, Void, String> {
    private CallBack callBack;
    private Map<String, String> paramsValue;
    private String re_info;
    private String app_id;
    private String app_secret;

    public Request_Task(Map<String, String> paramsValue, CallBack callBack) {
        this.callBack = callBack;
        app_id = ConstantPool.APP_ID;
        app_secret = ConstantPool.COMMUNICATION_SECRET_KEY;
        if (paramsValue != null) {
            ecodeParam(paramsValue);
        } else {
            callBack.doResult("参数为空");
        }
    }

    public Request_Task(Map<String, String> paramsValue, CallBack callBack, boolean init) {
        this.callBack = callBack;
        app_id = ConstantPool.INIT_APP_ID;
        app_secret = ConstantPool.INIT_COMMUNICATION_SECRET_KEY;
        if (paramsValue != null) {
            ecodeParam(paramsValue);
        } else {
            callBack.doResult("参数为空");
        }
    }


    /**
     * 传参加密
     */
    private void ecodeParam(Map<String, String> data) {
        Map<String, String> _data = new HashMap<>();
        //添加appid
//        String _random_num = crateRandomNumber(ConstantPool.RANDOM_NUMBER_SUM);
        String _random_num = "12";
        String _method = data.get(ConstantPool.PARAM_NAME);
        String _no_urlcode_param = crateParam(data);
        String _param_data = "";
        try {
            _param_data = URLEncoder.encode(_no_urlcode_param, "UTF-8");//urlcode
            String _parem = "";
            String[] _s = _param_data.split("%");
            for (int i = 0; i < _s.length; i++) {
                if (i == 0) {
                    _parem = _s[i];
                } else {
                    _parem = _parem + "%" + _s[i].toLowerCase();
                }
            }
            _param_data = _parem;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        _data.put("appid", app_id);
        _data.put("nonce", _random_num);
        _data.put("method", _method);
        _data.put("data", _no_urlcode_param);
        _data.put("hash", crateHashParam(_no_urlcode_param, _random_num));
        this.paramsValue = _data;
    }

    private String crateHashParam(String no_urlcode_param, String random_num) {
        String _s = app_id + "|" + app_secret + "|" + no_urlcode_param + "|" + random_num;
        String _result = Base64.encodeToString(getMD5(_s).getBytes(), Base64.NO_WRAP);
        return _result;
    }

    /**
     * 参数base64加密
     */
    private String crateParam(Map<String, String> data) {
        String encodedString = "";
        if (data.containsKey(ConstantPool.PARAM_NAME)) {
            data.remove(ConstantPool.PARAM_NAME);
        }
        if (data.size() != 0) {
//            encodedString = Base64.encodeToString(JSON.toJSONString(data).getBytes(), NO_WRAP);
            encodedString = Base64.encodeToString(toJson(data).getBytes(), NO_WRAP);
        } else {
//            encodedString = Base64.encodeToString(JSON.toJSONString("{}").getBytes(), NO_WRAP);
            encodedString = "e30=";
        }
        return encodedString;
    }

    private String toJson(Map<String, String> map) {
        String _s = "";
        if (map != null && map.size() != 0) {
            _s = "{";
            for (String key : map.keySet()) {
                _s = _s + key + ":" + "\'" + map.get(key) + "\'" + ",";
            }
            _s = _s.substring(0, _s.length() - 1);
            _s = _s + "}";
        }
        return _s;
    }

    private String sendPOSTRequest(String path, Map<String, String> params) {
        InputStream is = null;
        ByteArrayOutputStream bos = null;

        try {

            StringBuilder sb = new StringBuilder();

            if (params != null && params.size() != 0) {

                for (Map.Entry<String, String> entry : params.entrySet()) {

                    sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "utf-8"));

                    sb.append("&");

                }

                //删除最后一个
                sb.deleteCharAt(sb.length() - 1);

            }

            byte[] entity = sb.toString().getBytes();


            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
                byte[] buff = new byte[1024];

                while ((len = is.read(buff)) != -1) {

                    bos.write(buff, 0, len);
                    bos.flush();
                }
                re_info = new String(bos.toByteArray());

                return re_info;

            }

        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } finally {

            try {
                if (is != null) {
                    is.close();
                }
                if (bos != null) {
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

    public String crateRandomNumber(int sum) {
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
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return result.substring(8, 24).toUpperCase();
    }
}
