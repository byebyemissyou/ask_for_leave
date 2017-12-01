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

import com.kade.lyx.ask_for_leave.BasicActivity;
import com.kade.lyx.ask_for_leave.R;
import com.kade.lyx.ask_for_leave.entity.ConstantPool;
import com.kade.lyx.ask_for_leave.utils.ToastUtil;
import com.kade.lyx.ask_for_leave.utils.sharedpreferences.UnableClearSharepreferen;

public class SettingConfigActivity extends BasicActivity {
    private UnableClearSharepreferen mUs;
    private TextView device_no_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_config);
        inint();
    }

    private EditText as_user_number, as_user_password, as_user_new_number, as_user_new_password, as_servers_addr_et;

    private void inint() {
        mUs = UnableClearSharepreferen.getInstance();
        SharedPreferences sp = getSharedPreferences(ConstantPool.LOGIN_ID, MODE_PRIVATE);
        device_no_tv = (TextView) findViewById(R.id.device_no_tv);
        device_no_tv.setText(sp.getString(ConstantPool.DEVICE_NO, "000000"));
        as_user_number = (EditText) findViewById(R.id.as_user_number);
        as_user_password = (EditText) findViewById(R.id.as_user_password);
        as_user_new_number = (EditText) findViewById(R.id.as_user_new_number);
        as_user_new_password = (EditText) findViewById(R.id.as_user_new_password);
        as_servers_addr_et = (EditText) findViewById(R.id.as_servers_addr_et);
        as_servers_addr_et.setText(mUs.getServerAddress(this));

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.as_apply_amount_tv: {
                if (!TextUtils.isEmpty(as_user_new_number.getText())
                        && !TextUtils.isEmpty(as_user_new_password.getText())
                        && !TextUtils.isEmpty(as_user_number.getText())
                        && !TextUtils.isEmpty(as_user_password.getText())) {
                    if (as_user_number.getText().equals(mUs.getManagerNumber(SettingConfigActivity.this))
                            && as_user_password.getText().equals(mUs.getManagerPassword(SettingConfigActivity.this))) {
                        if (as_user_password.getText().length() > 5 && as_user_new_number.getText().length() > 5) {
                            mUs.setManagerNumber(SettingConfigActivity.this, as_user_new_number.getText().toString());
                            mUs.setManagerPassword(SettingConfigActivity.this, as_user_new_password.getText().toString());
                            ToastUtil.showToast(SettingConfigActivity.this, "账号修改成功");
                        } else {
                            ToastUtil.showToast(SettingConfigActivity.this, "账号密码至少六位字符");
                        }
                    }
                } else {
                    ToastUtil.showToast(SettingConfigActivity.this, "请完善填写的信息");
                }
                break;
            }
            case R.id.as_apply_addr_tv: {
                if (!TextUtils.isEmpty(as_servers_addr_et.getText())) {
                    mUs.setServerAddress(SettingConfigActivity.this, as_servers_addr_et.getText().toString());
                    ToastUtil.showToast(SettingConfigActivity.this, "服务器地址修改成功");
                } else {
                    ToastUtil.showToast(SettingConfigActivity.this, "请完善填写的信息");
                }
                break;
            }
            case R.id.as_back_iv: {
                finish();
                break;
            }
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
