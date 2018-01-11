package com.kade.lyx.ask_for_leave.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kade.lyx.ask_for_leave.ExitApplication;
import com.kade.lyx.ask_for_leave.R;
import com.kade.lyx.ask_for_leave.entity.ConstantPool;
import com.kade.lyx.ask_for_leave.network.Request_Task;
import com.kade.lyx.ask_for_leave.sample.BaseCardActivity;
import com.kade.lyx.ask_for_leave.utils.ToastUtil;
import com.kade.lyx.ask_for_leave.utils.UtilTools;
import com.kade.lyx.ask_for_leave.utils.sharedpreferences.UnableClearSharepreferen;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

public class LoginActivity extends BaseCardActivity implements View.OnFocusChangeListener {
    private String acc, pass;
    private MaterialEditText user_name_check, user_password_check;
    private EditText user_name, user_password;
    private ProgressDialog dialog;
    private ImageView alt_del;
    private String cid = "";
    private boolean isSH = false;
    private double exitTime = 0;
    private LinearLayout ask_for_leave, activity_login;
    private static final String TAG = "LoginTest";
    private RelativeLayout contentrl;
    private TextView login_title_tv;
    private TextView device_no_tv;//设备号显示
    private RadioButton alt_check_rb, alt_leave_rb;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String mUrl;

    private LinkedHashMap<String, String> putLoginMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(ConstantPool.PARAM_NAME, "Login");
        map.put("cnumber", acc);
        map.put("pwd", pass);
//        map.put("fid", "1");
        return map;
    }

    private LinkedHashMap<String, String> putCardLoginMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(ConstantPool.PARAM_NAME, "CardLogin");
//        map.put(ConstantPool.PARAM_NAME, "02");
//        map.put("cnumber", acc);
        map.put("cardno", acc);
//        map.put("fid", "1");
        return map;
    }

    private LinkedHashMap<String, String> putMapData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(ConstantPool.PARAM_NAME, "IsAudit");
        map.put("cid", cid);
        return map;
    }

    private void initView() {
        sp = getSharedPreferences(ConstantPool.LOGIN_ID, MODE_PRIVATE);//获取SharedPreferences用于储存设备号
        editor = sp.edit();
        alt_del = (ImageView) findViewById(R.id.alt_del);
        contentrl = (RelativeLayout) findViewById(R.id.contentrl);
        user_name = (EditText) findViewById(R.id.user_name);
        user_name.setOnFocusChangeListener(this);
        user_name.addTextChangedListener(nameWatcher);
        user_password = (EditText) findViewById(R.id.user_password);
        user_password.setOnFocusChangeListener(this);
        user_name_check = (MaterialEditText) findViewById(R.id.user_name_check);
        user_name_check.setOnFocusChangeListener(this);
        user_password_check = (MaterialEditText) findViewById(R.id.user_password_check);
        user_password_check.setOnFocusChangeListener(this);
        ask_for_leave = (LinearLayout) findViewById(R.id.ask_for_leave);
        activity_login = (LinearLayout) findViewById(R.id.activity_login);
        login_title_tv = (TextView) findViewById(R.id.login_title_tv);
        alt_check_rb = (RadioButton) findViewById(R.id.alt_check_rb);
        alt_leave_rb = (RadioButton) findViewById(R.id.alt_leave_rb);
        device_no_tv = (TextView) findViewById(R.id.device_no_tv);
        try {
            device_no_tv.setText(sp.getString(ConstantPool.DEVICE_NO, ""));
        } catch (Exception e) {
            device_no_tv.setText("");
        }
        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("正在登陆...");
        dialog.setTitle("登陆");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_login_main);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUrl = UnableClearSharepreferen.getInstance().getServerAddress(this);
        sendEnd();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                starScanCardSoon();
            }
        }, 100);
        user_name.setText("");
        user_password.setText("");
        getDeviceId(0, "");
    }

    /**
     * 开始读卡
     */
    private void starScanCardSoon() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                packFindCardData();
            }
        }, 500);
    }

    /**
     * 延时读卡
     */
    private void starScanCard() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                packFindCardData();
            }
        }, 2000);

    }

    private String parseLoginJson(String json) {

        String res = "";
        try {

            JSONObject obj = new JSONObject(json);
            res = obj.getString("result");
            cid = obj.getString("data");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return res;
    }


    private String parseJson(String json) {
        try {


            JSONObject object = new JSONObject(json);
            return object.getString("result");


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return "";
    }

    //监听账号 输入情况
    TextWatcher nameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(s)) {
                alt_del.setVisibility(View.VISIBLE);
            } else {
                alt_del.setVisibility(View.GONE);
            }
        }
    };

    public void Cardlogin(String mac_number) {
        acc = mac_number;
        if (isNetworkConnected()) {
            dialog.show();
            new Request_Task(putCardLoginMap(), new Request_Task.CallBack() {
                @Override
                public void doResult(String data) {
                    if (!data.equals(ConstantPool.RESULT_FAILED)) {
                        if (!parseLoginJson(data).equals("0")) {
                            starScanCard();
                            ToastUtil.showToast(getApplicationContext(), "非本系统用户卡");
                            dialog.dismiss();
                        } else {
                            if (isSH) {
                                new Request_Task(putMapData(), new Request_Task.CallBack() {
                                    @Override
                                    public void doResult(String data) {

                                        dialog.dismiss();

                                        if (!data.equals(ConstantPool.RESULT_FAILED)) {

                                            if (parseJson(data).equals("1")) {

                                                ToastUtil.showToast(getApplicationContext(), "您属于可审核用户！");
                                                Intent in = new Intent(LoginActivity.this, LeaveCheckActivity.class);
                                                in.putExtra("cid", cid);
                                                startActivity(in);
                                            } else {
                                                starScanCard();
                                                ToastUtil.showToast(getApplicationContext(), "你的账号没有审核权限!");
                                            }
                                        } else {
                                            starScanCard();
                                            ToastUtil.showToast(getApplicationContext(), getString(R.string.connect_failed_tips));
                                        }
                                    }
                                }).execute(mUrl);
                            } else {
                                dialog.dismiss();
                                Intent in = new Intent(LoginActivity.this, MainActivity.class);
                                in.putExtra("cid", cid);
                                startActivity(in);
                                finish();
                            }
                        }
                    } else {

                        dialog.dismiss();
                        ToastUtil.showToast(getApplicationContext(), getString(R.string.connect_failed_tips));
                        starScanCard();
                    }


                }
            }).execute(mUrl);


        } else {

            ToastUtil.showToast(getApplicationContext(), getString(R.string.no_network_tips));
            starScanCard();
        }

    }

//    public void CardloginT(String mac_number) {
//        if (isNetworkConnected()) {
//            Intent in = new Intent(LoginActivity.this, MainActivity.class);
//            if (mac_number.equals("1701404874")) {
//                in.putExtra("cid", "87");
//            } else if (mac_number.equals("547874740")) {
//                in.putExtra("cid", "81");
//            } else if (mac_number.equals("3754280510")) {
//                in.putExtra("cid", "84");
//            }else {
//                ToastUtil.showToast(getApplicationContext(), "非本系统用户卡");
//            }
//            startActivity(in);
//            finish();
//        } else {
//            ToastUtil.showToast(getApplicationContext(), getString(R.string.no_network_tips));
//            starScanCard();
//        }
//    }

    private void loginHttp() {
        if (isNetworkConnected()) {
            dialog.show();
            new Request_Task(putLoginMap(), new Request_Task.CallBack() {
                @Override
                public void doResult(String data) {
                    if (!data.equals(ConstantPool.RESULT_FAILED)) {
                        if (!parseLoginJson(data).equals("0")) {
                            ToastUtil.showToast(getApplicationContext(), "账号或密码错误");
                            dialog.dismiss();
                        } else {
                            if (isSH) {
                                new Request_Task(putMapData(), new Request_Task.CallBack() {
                                    @Override
                                    public void doResult(String data) {
                                        dialog.dismiss();
                                        if (!data.equals(ConstantPool.RESULT_FAILED)) {
                                            if (parseJson(data).equals("1")) {
                                                ToastUtil.showToast(getApplicationContext(), "您属于可审核用户！");
                                                Intent in = new Intent(LoginActivity.this, LeaveCheckActivity.class);
                                                in.putExtra("cid", cid);
                                                startActivity(in);
                                            } else {
                                                ToastUtil.showToast(getApplicationContext(), "你的账号没有审核权限!");
                                            }
                                        } else {
                                            ToastUtil.showToast(getApplicationContext(), getString(R.string.connect_failed_tips));
                                        }
                                    }
                                }).execute(mUrl);
                            } else {
                                dialog.dismiss();
                                Intent in = new Intent(LoginActivity.this, MainActivity.class);
                                in.putExtra("cid", cid);
                                startActivity(in);
                                finish();
                            }
                        }
                    } else {
                        dialog.dismiss();
                        ToastUtil.showToast(getApplicationContext(), getString(R.string.connect_failed_tips));
                    }
                }
            }).execute(mUrl);
        } else {
            ToastUtil.showToast(getApplicationContext(), getString(R.string.no_network_tips));
        }
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switchOfLogin: {
                isSH = true;
                ask_for_leave.setVisibility(View.GONE);
                activity_login.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.switchOfLogin_check: {
                isSH = false;
                activity_login.setVisibility(View.GONE);
                ask_for_leave.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.button:
            case R.id.button_check: {
                if (!user_name.getText().toString().isEmpty() && !user_password.getText().toString().isEmpty()) {
                    acc = user_name.getText().toString();
                    pass = user_password.getText().toString();
                    if (acc.equals(UnableClearSharepreferen.getInstance().getManagerNumber(this))
                            && pass.equals(UnableClearSharepreferen.getInstance().getManagerPassword(this))) {
                        startActivity(new Intent(this, SettingActivity.class));//管理账号登录
                    } else {
                        getDeviceId(1, "");
//                        loginHttp();
                    }
                } else {
                    ToastUtil.showToast(getApplicationContext(), "请输入用户名或密码");
                }
                break;
            }
            case R.id.alt_check_rb: {//我要审核
                alt_check_rb.setTextColor(getResources().getColor(R.color.white));
                alt_check_rb.setBackgroundResource(R.color.login_choice);
                alt_leave_rb.setTextColor(getResources().getColor(R.color.black));
                alt_leave_rb.setBackgroundResource(R.color.white);
                login_title_tv.setText("审核系统登录");
                isSH = true;
                break;
            }
            case R.id.alt_leave_rb: {//我要请假
                alt_check_rb.setTextColor(getResources().getColor(R.color.black));
                alt_check_rb.setBackgroundResource(R.color.white);
                alt_leave_rb.setTextColor(getResources().getColor(R.color.white));
                alt_leave_rb.setBackgroundResource(R.color.login_choice);
                isSH = false;
                login_title_tv.setText("请假系统登录");
                break;
            }
            case R.id.alt_del: {//删除登录账号信息
                user_name.setText("");
                user_password.setText("");
                break;
            }
        }

    }

    /**
     * 获取当前设备的设备号（设备号是用户自己设置的设备号，设备唯一识别码是设备的硬件标识码）
     *
     * @param type   0只获取 1账号登录 2刷卡登录
     * @param cardno 物理卡号 账号登录传空
     */
    private void getDeviceId(final int type, final String cardno) {
        if (UtilTools.hasInternet(this)) {
            if (!TextUtils.isEmpty(mUrl)) {
                LinkedHashMap _paramsValue = new LinkedHashMap<>();
                _paramsValue.put(ConstantPool.PARAM_NAME, "GetDeviceNumber");
                _paramsValue.put("dsn", getImei());//传设备唯一识别码
                new Request_Task(_paramsValue, new Request_Task.CallBack() {

                    @Override
                    public void doResult(String data) {
                        if (!TextUtils.isEmpty(data)) {
                            if (!data.equals(ConstantPool.RESULT_FAILED)) {
                                try {
                                    JSONObject object = new JSONObject(data);
                                    if (object.getString("result").equals(ConstantPool.RESULT_OK)) {
                                        editor.putString(ConstantPool.DEVICE_NO, object.getString("data"));
                                        editor.commit();
                                        device_no_tv.setText(object.getString("data"));
                                    } else if (object.getString("result").equals("1") && object.getString("data") == "") {
                                        ToastUtil.showToast(LoginActivity.this, "请先设置设备号");
                                        startActivity(new Intent(LoginActivity.this, SettingDeviceNoActivity.class));
                                    }
                                } catch (JSONException e) {
                                    if (type == 2) {//当是刷卡登录时，如果获取设备号异常还是要再次启动寻卡
                                        starScanCard();
                                    }
                                    e.printStackTrace();
                                }
                                switch (type) {
                                    case 0: {//回到登录页面重新拉取设备号
                                        break;
                                    }
                                    case 1: {
                                        if (!TextUtils.isEmpty(device_no_tv.getText())) {
                                            loginHttp();
                                        } else {
                                            ToastUtil.showToast(LoginActivity.this, "请先设置设备号");
                                            startActivity(new Intent(LoginActivity.this, SettingDeviceNoActivity.class));
                                        }
                                        break;
                                    }
                                    case 2: {
                                        if (!TextUtils.isEmpty(device_no_tv.getText())) {
                                            Cardlogin(cardno);
                                        } else {
                                            ToastUtil.showToast(LoginActivity.this, "请先设置设备号");
                                            startActivity(new Intent(LoginActivity.this, SettingDeviceNoActivity.class));
                                        }
                                        break;
                                    }
                                }
                            } else {
                                if (type == 2) {//当是刷卡登录时，如果获取设备号异常还是要再次启动寻卡
                                    starScanCard();
                                }
                                ToastUtil.showToast(LoginActivity.this, "获取设备号异常");
                            }
                        } else {
                            ToastUtil.showToast(LoginActivity.this, "服务器异常");
                        }
                    }
                }).execute(mUrl);
            } else {
                ToastUtil.showToast(this, "接口地址错误，请重新配置");
            }
        } else {
            ToastUtil.showToast(LoginActivity.this, "无网络");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();

            } else {
                ExitApplication.getInstance().exit();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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

    /**
     * 扫卡数据返回
     *
     * @param data
     */
    @Override
    protected void onCardReceived(String data) {
        if (data.equals("no")) {
            starScanCardSoon();
        } else {
            getDeviceId(2, data);//1129
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
//            contentrl.s(0,-2,0,0);
        } else {

        }
    }
}
