package com.kade.lyx.ask_for_leave.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jude.rollviewpager.RollPagerView;
import com.kade.lyx.ask_for_leave.BasicActivity;
import com.kade.lyx.ask_for_leave.R;
import com.kade.lyx.ask_for_leave.adapter.BannerAdapter;
import com.kade.lyx.ask_for_leave.entity.ConstantPool;
import com.kade.lyx.ask_for_leave.entity.Student;
import com.kade.lyx.ask_for_leave.network.Request_Task;
import com.kade.lyx.ask_for_leave.utils.ToastUtil;
import com.kade.lyx.ask_for_leave.utils.utils_parse_json.ParseJson_AD;
import com.kade.lyx.ask_for_leave.utils.utils_parse_json.ParseJson_BasicInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends BasicActivity {
    private static final int REQUEST_ORIGINAL = 100;
    private TextView afleave_sTime, afleave_eTime;
    private MaterialSpinner afleave_type;
    private LinkedHashMap<String, String> map;
    private EditText afleave_reason;
    private String content;
    private String sTime, eTime;
    private Student student;
    private TextView cname, basic_class, basic_id, sex;
    private String cid = "";
    private ProgressDialog dialog, dialog2;
    private RollPagerView imageShow;
    private List<String> urlList;
    private Student student2;
    private static final String TAG = "Main_Test";
    private double exitTime = 0;
    private LinkedList<String> list_ti;
    private ArrayList<String> list_tn;
    private String type = "";
    private ImageView iv;
    private Bitmap bitmap;


    //比较两个时间的前后，如果结束时间超过开始时间返回true，结束时间未超过开始时间则返回false
    private boolean compareTime(String start, String end) {

        java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Calendar st = java.util.Calendar.getInstance();
        java.util.Calendar ed = java.util.Calendar.getInstance();

        try {

            st.setTime(df.parse(start));
            ed.setTime(df.parse(end));

        } catch (java.text.ParseException e) {

            System.err.println("格式不正确");

        }

        int result = st.compareTo(ed);

        if (result == 0)
            return false;
        else if (result < 0)
            return true;
        else
            return false;

    }

    //获取待审核请假类型请求
    private LinkedHashMap<String, String> putMapData_type() {
        LinkedHashMap<String, String> map_type = new LinkedHashMap<>();
        map_type.put(ConstantPool.PARAM_NAME, "GetLeaveType");
        return map_type;

    }

    private void parseJson_leaveType(String json) {

        try {
            JSONObject object = new JSONObject(json);
            JSONArray jsonArray = object.getJSONArray("data");
            list_tn = new ArrayList<>();
            list_ti = new LinkedList<>();
            list_tn.add("全部类型");
            list_ti.add("");
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject type_object = (JSONObject) jsonArray.get(i);

                list_tn.add(type_object.getString("name"));
                list_ti.add(type_object.getString("id"));

            }

            afleave_type.setItems(list_tn);

            afleave_type.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                    type = list_ti.get(position);

                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void putMapData() {

        map = new LinkedHashMap<>();
        map.put(ConstantPool.PARAM_NAME, "AddLeaveInfo");
        map.put(ConstantPool.UID, cid);
        map.put("type", type);
        map.put("starttime", sTime);
        map.put("endtime", eTime);
        map.put("reason", content);

    }

    private LinkedHashMap<String, String> putMapData_AD() {

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(ConstantPool.PARAM_NAME, "GetADAddress");
        return map;

    }

    private void initView() {

        cid = getIntent().getStringExtra("cid");
        cname = (TextView) findViewById(R.id.cname);
        iv = (ImageView) findViewById(R.id.iv);
        basic_class = (TextView) findViewById(R.id.basic_class);
        basic_id = (TextView) findViewById(R.id.basic_id);
        imageShow = (RollPagerView) findViewById(R.id.imageShow);
        imageShow.setPlayDelay(3000);
        imageShow.setAnimationDurtion(500);
        urlList = new ArrayList<>();
        if (isNetworkConnected()) {
            new Request_Task(putMapData_type(), new Request_Task.CallBack() {
                @Override
                public void doResult(String data) {

                    if (!data.equals(ConstantPool.RESULT_FAILED)) {

                        parseJson_leaveType(data);

                    } else {

                        ToastUtil.showToast(getApplicationContext(), getString(R.string.connect_failed_tips));
                    }

                }
            }).execute(ConstantPool.URL);
        }

        afleave_eTime = (TextView) findViewById(R.id.afleave_eTime);
        afleave_sTime = (TextView) findViewById(R.id.afleave_sTime);
        afleave_reason = (EditText) findViewById(R.id.afleave_reason);
        afleave_type = (MaterialSpinner) findViewById(R.id.afleave_type);
        sex = (TextView) findViewById(R.id.sex);

        dialog = new ProgressDialog(this);
        dialog2 = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle("提示");
        dialog.setMessage("正在提交...");

        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setTitle("提示");
        dialog2.setMessage("正在查询...");

        //强行重新设置Md_Spinner的内边距
        afleave_type.setTextSize(17.0f);
        afleave_type.setPadding(20, 1, 20, 1);

        setInfo();

        if (isNetworkConnected()) {

            new Request_Task(putMapData_AD(), new Request_Task.CallBack() {
                @Override
                public void doResult(String data) {
                    student2 = new Student();

                    //有数据返回成功才开始解析
                    if (!data.equals(ConstantPool.RESULT_FAILED)) {
                        ParseJson_AD.parse(data, student2);

                        for (int i = 0; i < student2.getAddressList().size(); i++) {

                            urlList.add(student2.getAddressList().get(i).getName());
                        }

                        imageShow.setAdapter(new BannerAdapter(getApplicationContext(), imageShow, urlList));
                    }
                }
            }).execute(ConstantPool.URL);

        }

    }

    private void setTimeClickListener() {
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                RxPermissions rxPermissions = new RxPermissions(MainActivity.this);
//                String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//                rxPermissions.request(permissions)
//                        .subscribe(new Action1<Boolean>() {
//                            @Override
//                            public void call(Boolean aBoolean) {
//                                if (aBoolean) {
//                                    takePhoto();
//                                }
//                            }
//                        });
            }
        });

        afleave_sTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickDialog dialog = new DatePickDialog(MainActivity.this);
                //设置上下年分限制
                dialog.setYearLimt(15);
                //设置标题
                dialog.setTitle("选择请假开始时间");
                //设置类型
                dialog.setType(DateType.TYPE_ALL);
                //设置消息体的显示格式，日期格式
                dialog.setMessageFormat("yyyy-MM-dd HH:mm:ss");

                //设置选择回调

                dialog.setOnChangeLisener(null);
                //设置点击确定按钮回调
                dialog.setOnSureLisener(new OnSureLisener() {
                    @Override
                    public void onSure(Date date) {

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String str = sdf.format(date);
                        afleave_sTime.setText("选择开始时间    " + str);
                        sTime = str;
                    }
                });
                dialog.show();
            }
        });

        afleave_eTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickDialog dialog = new DatePickDialog(MainActivity.this);
                //设置上下年分限制
                dialog.setYearLimt(15);
                //设置标题
                dialog.setTitle("选择请假结束时间");
                //设置类型
                dialog.setType(DateType.TYPE_ALL);
                //设置消息体的显示格式，日期格式
                dialog.setMessageFormat("yyyy-MM-dd HH:mm");

                //设置选择回调
                dialog.setOnChangeLisener(null);
                //设置点击确定按钮回调
                dialog.setOnSureLisener(new OnSureLisener() {
                    @Override
                    public void onSure(Date date) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String str = sdf.format(date);
                        afleave_eTime.setText("选择结束时间    " + str);
                        eTime = str;


                    }
                });

                dialog.show();
            }
        });
    }

    public void afl_submit(View view) {

        content = afleave_reason.getText().toString();
        if (!cid.equals("")) {
//            if(bitmap == null){
//                ToastUtil.showToast(MainActivity.this, "请上传头像");
//            }
            if (afleave_type.getText().toString().equals("全部类型") || afleave_sTime.getText().toString().equals("选择开始时间") || afleave_eTime.getText().toString().equals("选择结束时间") || content.equals("")) {
                ToastUtil.showToast(MainActivity.this, "请选择类型&时间或补充请假理由");
            } else {
                if (compareTime(sTime, eTime)) {
                    if (isNetworkConnected()) {

                        dialog.show();
                        putMapData();

                        new Request_Task(map, new Request_Task.CallBack() {
                            @Override
                            public void doResult(String data) {
                                dialog.dismiss();
                                if (!data.equals(ConstantPool.RESULT_FAILED)) {
                                    try {
                                        JSONObject object = new JSONObject(data);
                                        String result = object.getString("result");
                                        if (result.equals("0")) {
                                            ToastUtil.showToast(MainActivity.this.getApplicationContext(), "提交成功！");
                                        } else {
                                            ToastUtil.showToast(MainActivity.this.getApplicationContext(), "请假时间段冲突，提交失败！");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    afleave_reason.setText(content);
                                    ToastUtil.showToast(MainActivity.this.getApplicationContext(), getString(R.string.connect_failed_tips));
                                }
                            }

                        }).execute(ConstantPool.URL);
                    } else {
                        ToastUtil.showToast(MainActivity.this, getString(R.string.no_network_tips));
                    }
                } else {
                    ToastUtil.showToast(getApplicationContext(), "时间选择有误，请检查！");
                }
            }
        } else {
            ToastUtil.showToast(getApplicationContext(), "请先获取账号信息！");

        }
    }



    private void setInfo() {

        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        map.put(ConstantPool.PARAM_NAME, "GetClient");
        map.put(ConstantPool.UID, cid);

        student = new Student();
        new Request_Task(map, new Request_Task.CallBack() {
            @Override
            public void doResult(String data) {
                ParseJson_BasicInfo.parse(data, student);

                if (!data.equals(ConstantPool.RESULT_FAILED)) {
                    cname.setText("姓名： " + student.getName());
                    sex.setText("性别： " + student.getSex());
                    basic_class.setText("单位： " + student.getDname());
                    basic_id.setText("编号： " + student.getCnumber());
                } else {
                    ToastUtil.showToast(getApplicationContext(), getString(R.string.connect_failed_tips));
                }
            }
        }).execute(ConstantPool.URL);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_taobao);
        initView();
        setTimeClickListener();
    }

    public void l_submit(View view) {
        Intent in = new Intent(this, LeaveActivity.class);
        if (!cid.equals("")) {
            in.putStringArrayListExtra("type", list_tn);
            in.putExtra("cid", cid);
            startActivity(in);
        } else {
            ToastUtil.showToast(getApplicationContext(), "请先获取账号信息！");
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
