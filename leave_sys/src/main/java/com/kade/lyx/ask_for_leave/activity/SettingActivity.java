package com.kade.lyx.ask_for_leave.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.kade.lyx.ask_for_leave.BasicActivity;
import com.kade.lyx.ask_for_leave.R;
import com.kade.lyx.ask_for_leave.entity.ConstantPool;
import com.kade.lyx.ask_for_leave.network.Request_Task;
import com.kade.lyx.ask_for_leave.sample.UtileTools;
import com.kade.lyx.ask_for_leave.update.CheckUpdateEntity;
import com.kade.lyx.ask_for_leave.utils.ToastUtil;
import com.kade.lyx.ask_for_leave.utils.utils_parse_json.ParseUpdata;

import java.util.LinkedHashMap;

public class SettingActivity extends BasicActivity {

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
    }

    private void init() {
        TextView device_no_tv = (TextView) findViewById(R.id.device_no_tv);
        SharedPreferences sp = getSharedPreferences(ConstantPool.LOGIN_ID, MODE_PRIVATE);
        device_no_tv.setText(sp.getString(ConstantPool.DEVICE_NO, "000000"));
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setTitle("更新提示");
        builder.setMessage("APP版本更新，需要设备能访问到外部网络");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("开始更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (UtileTools.hasInternet(SettingActivity.this)) {
                    upDateApp();
                } else {
                    ToastUtil.showToast(SettingActivity.this, "当前网络异常");
                }
            }
        });
        dialog = builder.create();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.as_addr_tv: {
                startActivity(new Intent(SettingActivity.this, SettingConfigActivity.class));
                break;
            }
            case R.id.as_device_tv: {
                startActivity(new Intent(SettingActivity.this, SettingDeviceNoActivity.class));
                break;
            }
            case R.id.as_setting_tv: {//跳转系统设置方便用户设置声音网络这些（如果厂商不在系统设置页面做返回按钮，就显示出下面的虚拟按键）
                //跳转系统设置页面代码
                Intent mIntent = new Intent(Settings.ACTION_SETTINGS);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                startActivity(mIntent);
                break;
            }
            case R.id.as_updata_tv: {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        ToastUtil.showToast(SettingActivity.this, "该版本已是最新版本");
//                    }
//                }, 1000);
//
                upDateApp();
                break;
            }
            case R.id.as_back_iv: {
                finish();
                break;
            }
        }
    }

    /**
     * 获取更新情况
     */
    private void upDateApp() {
        LinkedHashMap<String, String> map_type = new LinkedHashMap<>();
        map_type.put(ConstantPool.PARAM_NAME, "GetLastVersion");
        map_type.put("sname", ConstantPool.APP_NAME);
        new Request_Task(map_type, new Request_Task.CallBack() {
            @Override
            public void doResult(String data) {
                if (!TextUtils.isEmpty(data) && !data.equals(ConstantPool.RESULT_FAILED)) {
                    CheckUpdateEntity mCheckUpdateEntity = new CheckUpdateEntity();
                    ParseUpdata.parse(data, mCheckUpdateEntity);
                    if (UtileTools.getVerCode(SettingActivity.this) < Integer.parseInt(mCheckUpdateEntity.getVer())) {
                        dialog.show();//1129
                    } else {
                        ToastUtil.showToast(SettingActivity.this, "当前版本已经是最新版本");
                    }
                } else {
                    ToastUtil.showToast(SettingActivity.this, "无更新信息");
                }
            }
        }).execute(ConstantPool.UPDATA_URL);
    }
}
