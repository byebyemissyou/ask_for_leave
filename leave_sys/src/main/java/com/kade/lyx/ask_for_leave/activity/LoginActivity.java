package com.kade.lyx.ask_for_leave.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.kade.lyx.ask_for_leave.BasicActivity;
import com.kade.lyx.ask_for_leave.R;
import com.kade.lyx.ask_for_leave.entity.ConstantPool;
import com.kade.lyx.ask_for_leave.network.Request_Task;
import com.kade.lyx.ask_for_leave.utils.ToastUtil;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

public class LoginActivity extends BasicActivity implements CompoundButton.OnCheckedChangeListener {
    private String acc, pass;
    private MaterialEditText user_name, user_password;
    private ProgressDialog dialog;
    private String cid = "";
    private Switch switchOfLogin;
    private boolean isSH = false;
    private double exitTime = 0;
    private static final String TAG = "LoginTest";

    private LinkedHashMap<String, String> putLoginMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(ConstantPool.PARAM_NAME, "Login");
        map.put("cnumber", acc);
        map.put("pwd", pass);

        return map;
    }


    private LinkedHashMap<String, String> putMapData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(ConstantPool.PARAM_NAME, "IsAudit");
        map.put("cid", cid);


        return map;


    }

    private void initView() {



        user_name = (MaterialEditText) findViewById(R.id.user_name);
        user_password = (MaterialEditText) findViewById(R.id.user_password);
        switchOfLogin = (Switch) findViewById(R.id.switchOfLogin);
        switchOfLogin.setOnCheckedChangeListener(this);
        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("正在登陆...");
        dialog.setTitle("登陆");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_taobao);
        initView();

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


    public void login(View view) {


        if (!user_name.getText().toString().isEmpty() && !user_password.getText().toString().isEmpty()) {
            acc = user_name.getText().toString();
            pass = user_password.getText().toString();

            if (isNetworkConnected()) {


                dialog.show();
                new Request_Task(putLoginMap(), new Request_Task.CallBack() {
                    @Override
                    public void doResult(String data) {


                        if (!data.equals(ConstantPool.RESULT_FAILED)) {

                            if (!parseLoginJson(data).equals("0")) {

                                ToastUtil.showToast(getApplicationContext(), "用户名或密码错误");
                                dialog.dismiss();


                            } else {

                                if (isSH) {

                                    new Request_Task(putMapData(), new Request_Task.CallBack() {
                                        @Override
                                        public void doResult(String data) {

                                            dialog.dismiss();

                                            if (!data.equals(ConstantPool.RESULT_FAILED)) {

                                                if (parseJson(data).equals("1")) {

/*
                                                    ToastUtil.showToast(getApplicationContext(), "您属于可审核用户！");
                                                    Intent in = new Intent(LoginActivity.this, LeaveCheckActivity.class);
                                                    in.putExtra("cid", cid);
                                                    startActivity(in);
                                                    LoginActivity.this.finish();
*/


                                                } else {

                                                    ToastUtil.showToast(getApplicationContext(), "你的账号没有审核权限!");


                                                }

                                            } else {

                                                ToastUtil.showToast(getApplicationContext(), getString(R.string.connect_failed_tips));

                                            }
                                        }
                                    }).execute(ConstantPool.URL);


                                } else {

                                    dialog.dismiss();
                                    Intent in = new Intent(LoginActivity.this, MainActivity.class);
                                    in.putExtra("cid", cid);
                                    startActivity(in);
                                    LoginActivity.this.finish();


                                }


                            }

                        } else {

                            dialog.dismiss();
                            ToastUtil.showToast(getApplicationContext(), getString(R.string.connect_failed_tips));

                        }


                    }
                }).execute(ConstantPool.URL);


            } else {

                ToastUtil.showToast(getApplicationContext(), getString(R.string.no_network_tips));

            }


        } else {

            ToastUtil.showToast(getApplicationContext(), "请输入用户名或密码");


        }


    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {

            switchOfLogin.setText("正在使用审核端登录");
            isSH = true;

        } else {

            switchOfLogin.setText("点此切换为审核端登录");
            isSH = false;


        }


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();

            } else {
                this.finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
