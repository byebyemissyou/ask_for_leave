package com.kade.lyx.ask_for_leave.init;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kade.lyx.ask_for_leave.BasicActivity;
import com.kade.lyx.ask_for_leave.ExitApplication;
import com.kade.lyx.ask_for_leave.R;
import com.kade.lyx.ask_for_leave.activity.LoginActivity;
import com.kade.lyx.ask_for_leave.entity.ConstantPool;
import com.kade.lyx.ask_for_leave.network.Request_Task;
import com.kade.lyx.ask_for_leave.sample.UtileTools;
import com.kade.lyx.ask_for_leave.utils.ToastUtil;
import com.kade.lyx.ask_for_leave.utils.sharedpreferences.UnableClearSharepreferen;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;


/**
 * 设备出厂初始化
 */
public class InitialActivity extends BasicActivity {
    private ProgressDialog dialog;
    private InitialSharepreference initsp;
    private long exitTime = 0;
    private RelativeLayout ai_addr_rl, ai_initial_rl;
    private Button start;
    private EditText ai_addr_et, ai_user_new_number, ai_user_new_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_initial);
        init();
    }

    private void init() {
        ai_initial_rl = (RelativeLayout) findViewById(R.id.ai_initial_rl);
        ai_addr_et = (EditText) findViewById(R.id.ai_addr_et);
        ai_user_new_number = (EditText) findViewById(R.id.ai_user_new_number);
        ai_user_new_password = (EditText) findViewById(R.id.ai_user_new_password);
        ai_addr_et = (EditText) findViewById(R.id.ai_addr_et);
        ai_addr_rl = (RelativeLayout) findViewById(R.id.ai_addr_rl);
        initsp = new InitialSharepreference(this);
        dialog = new ProgressDialog(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (initsp.getInitialStatus()) {//判断是否初始化
//        if (true) {//判断是否初始化
            ai_initial_rl.setVisibility(View.GONE);
            ai_addr_rl.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(UnableClearSharepreferen.getInstance().getServerAddress(this))) {
            startActivity(new Intent(InitialActivity.this, LoginActivity.class));
            finish();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start: {//确定初始化
                if (!UtileTools.hasInternet(this)) {
                    UtileTools.showNetWorkWarring(this);
                } else {
                    initDivice();
                }
                break;
            }
            case R.id.ai_sure_tv: {//提交账号密码等设置
                if (!TextUtils.isEmpty(ai_addr_et.getText())
                        && !TextUtils.isEmpty(ai_user_new_number.getText())
                        && !TextUtils.isEmpty(ai_user_new_password.getText())) {
                    if (ai_user_new_number.getText().toString().length() >= 6
                            && ai_user_new_password.getText().toString().length() >= 6) {
                        //填充初始化的服务器地址和设置的账号和密码
                        UnableClearSharepreferen.getInstance().setServerAddress(InitialActivity.this, ai_addr_et.getText().toString());
                        UnableClearSharepreferen.getInstance().setManagerNumber(InitialActivity.this, ai_user_new_number.getText().toString());
                        UnableClearSharepreferen.getInstance().setManagerPassword(InitialActivity.this, ai_user_new_password.getText().toString());
                        startActivity(new Intent(InitialActivity.this, LoginActivity.class));
                        InitialActivity.this.finish();
                    } else {
                        ToastUtil.showToast(InitialActivity.this, "账号密码至少六位");
                    }
                } else {
                    ToastUtil.showToast(InitialActivity.this, "请填写完整信息");
                }
                break;
            }
            case R.id.ai_setting_ib: {
                //跳转系统设置页面代码 让初始化人员优先让设备连上网络
                Intent mIntent = new Intent(Settings.ACTION_SETTINGS);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                startActivity(mIntent);
                break;
            }
        }
    }

    private LinkedHashMap<String, String> paramsValue;

    private void initDivice() {
        dialog.show();
        paramsValue = new LinkedHashMap<>();
        paramsValue.put("dno", getImei());
        paramsValue.put("code", "21askforleave");
        paramsValue.put("param", "07");
        //初始化
        new Request_Task(paramsValue, new Request_Task.CallBack() {
            @Override
            public void doResult(String data) {
                dialog.dismiss();
                if (!data.equals("failed")) {
                    try {
                        JSONObject object = new JSONObject(data);
                        if (object.getInt("result") == 0) {
                            initsp.setInitialStatusTrue();
                            ai_initial_rl.setVisibility(View.GONE);
                            ai_addr_rl.setVisibility(View.VISIBLE);
                            Toast.makeText(InitialActivity.this, object.getString("data"), Toast.LENGTH_SHORT).show();
                        } else if (object.getInt("result") == 2) {
                            Toast.makeText(InitialActivity.this, object.getString("data"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(InitialActivity.this, "初始化失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(InitialActivity.this, "与服务器连接失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, false).execute(ConstantPool.INIT_URL);
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
}
