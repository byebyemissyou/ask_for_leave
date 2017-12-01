package com.kade.lyx.ask_for_leave.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kade.lyx.ask_for_leave.BasicActivity;
import com.kade.lyx.ask_for_leave.R;
import com.kade.lyx.ask_for_leave.entity.ConstantPool;
import com.kade.lyx.ask_for_leave.network.Request_Task;
import com.kade.lyx.ask_for_leave.utils.ToastUtil;
import com.kade.lyx.ask_for_leave.utils.UtilTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

public class SettingDeviceNoActivity extends BasicActivity {

    private EditText editText;
    private String old_no;
    private TextView asd_cancle_tv, asd_sure_tv;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_device_no);
        init();
    }

    private void init() {
        SharedPreferences sp = getSharedPreferences(ConstantPool.LOGIN_ID, MODE_PRIVATE);//获取SharedPreferences用于储存id和用户名密码
        editor = sp.edit();
        old_no = sp.getString(ConstantPool.DEVICE_NO, "");
        editText = (EditText) findViewById(R.id.asd_device_et);
        editText.setText(old_no);
        asd_cancle_tv = (TextView) findViewById(R.id.asd_cancle_tv);
        asd_sure_tv = (TextView) findViewById(R.id.asd_sure_tv);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.asd_cancle_tv: {
                finish();
                break;
            }
            case R.id.asd_sure_tv: {
                String device_no = "";
                if (!TextUtils.isEmpty(editText.getText())) {
                    if (editText.getText().length() < 6) {
                        for (int i = 0; i < (6 - editText.length()); i++) {
                            device_no = device_no + "0";
                        }
                        device_no = device_no + editText.getText();
                    } else {
                        device_no = editText.getText().toString();
                    }
                    setDeviceId(device_no);
                } else {
                    Toast.makeText(SettingDeviceNoActivity.this, "请输入设备号", Toast.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.asdn_back_iv: {
                finish();
                break;
            }
        }
    }

    /**
     * 设置设备的设备号
     */
    private void setDeviceId(String deviceId) {
        asd_sure_tv.setEnabled(false);
        asd_cancle_tv.setEnabled(false);
        if (UtilTools.hasInternet(this)) {
            LinkedHashMap _paramsValue = new LinkedHashMap<>();
            _paramsValue.put(ConstantPool.PARAM_NAME, "SetDeviceNumber");
            _paramsValue.put("dsn", getImei());
            _paramsValue.put("dno", deviceId);
            _paramsValue.put("type", "1");//大屏请假为1 考勤为2
            new Request_Task(_paramsValue, new Request_Task.CallBack() {

                @Override
                public void doResult(String data) {
                    asd_sure_tv.setEnabled(true);
                    asd_cancle_tv.setEnabled(true);
                    if (!data.equals(ConstantPool.RESULT_FAILED)) {
                        try {
                            JSONObject object = new JSONObject(data);
                            if (object.getString("result").equals(ConstantPool.RESULT_OK)) {
                                editor.putString(ConstantPool.DEVICE_NO, object.getString("data"));
                                editor.commit();
                                Toast.makeText(SettingDeviceNoActivity.this, "修改完成", Toast.LENGTH_LONG).show();
                                finish();
                            } else if (object.getString("result").equals("10004")) {
                                Toast.makeText(SettingDeviceNoActivity.this, "设备编号已占用", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (TextUtils.isEmpty(data)) {
                        ToastUtil.showToast(SettingDeviceNoActivity.this, "服务器异常");
                    }
                }
            }).execute(mUrl);
        } else {
            Toast.makeText(SettingDeviceNoActivity.this, "当前网络异常", Toast.LENGTH_LONG).show();
        }
    }

    //************************解决点击其他区域键盘自动收回**************************************//

    // 获取点击事件

    @Override

    public boolean dispatchTouchEvent(MotionEvent ev) {

        // TODO Auto-generated method stub

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            View view = getCurrentFocus();

            if (isHideInput(view, ev)) {

                HideSoftInput(view.getWindowToken());

            }

        }

        return super.dispatchTouchEvent(ev);

    }

    // 判定是否需要隐藏

    private boolean isHideInput(View v, MotionEvent ev) {

        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    // 隐藏软键盘

    private void HideSoftInput(IBinder token) {

        if (token != null) {

            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            manager.hideSoftInputFromWindow(token,

                    InputMethodManager.HIDE_NOT_ALWAYS);

        }

    }

    //************************解决点击其他区域键盘自动收回**************************************//
}
