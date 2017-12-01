package com.kade.lyx.ask_for_leave.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.kade.lyx.ask_for_leave.utils.UtilTools;
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
    private AlertDialog alertDialog;
    private ImageView head;
    private TextView device_no_tv;//显示设备号
    private SharedPreferences sp;
    private String Device_no;//设备号


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
        map.put("fid", "1");//2017/10/18 10:27 新增 传默认值1，默认审核流程为1(多余参数)
    }

    private LinkedHashMap<String, String> putMapData_AD() {

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(ConstantPool.PARAM_NAME, "GetADAddress");
        return map;

    }

    private void initView() {
        createDialog();
        sp = getSharedPreferences(ConstantPool.LOGIN_ID, MODE_PRIVATE);//获取SharedPreferences用于储存id和用户名密码
        Device_no = sp.getString(ConstantPool.DEVICE_NO, "000000");
        device_no_tv = (TextView) findViewById(R.id.device_no_tv);
        device_no_tv.setText(Device_no);//初始化设备号
        head = (ImageView) findViewById(R.id.head);
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
            }).execute(mUrl);
        }

        afleave_eTime = (TextView) findViewById(R.id.afleave_eTime);
        eTime = UtilTools.getCurTime().substring(0, 10) + " 23:59:59";
        afleave_eTime.setText("选择结束时间 " + eTime);//默认请假结束时间今天的23.59分
        afleave_sTime = (TextView) findViewById(R.id.afleave_sTime);
        sTime = UtilTools.getCurTime();
        afleave_sTime.setText("选择开始时间 " + sTime);//请假默认开始时间为当前时间
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
            }).execute(mUrl);

        }

    }

    private void setTimeClickListener() {
//        iv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                RxPermissions rxPermissions = new RxPermissions(MainActivity.this);
////                String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
////                rxPermissions.request(permissions)
////                        .subscribe(new Action1<Boolean>() {
////                            @Override
////                            public void call(Boolean aBoolean) {
////                                if (aBoolean) {
////                                    takePhoto();
////                                }
////                            }
////                        });
//            }
//        });

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
                    alertDialog.show();
                } else {
                    ToastUtil.showToast(getApplicationContext(), "时间选择有误，请检查！");
                }
            }
        } else {
            ToastUtil.showToast(getApplicationContext(), "请先获取账号信息！");

        }
    }

    /**
     * 提交请假接口申请
     */
    private void submitApply() {
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

            }).execute(mUrl);
        } else {
            ToastUtil.showToast(MainActivity.this, getString(R.string.no_network_tips));
        }
    }

    public void afl_back(View view) {
        startActivity(new Intent(this, LoginActivity.class));
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
                    basic_class.setText("部门/班级： " + student.getDname());
                    basic_id.setText("编号： " + student.getCnumber());
                    //  头像图片
//                    Glide.with(MainActivity.this).load(student.getPic()).fitCenter()
//                            .placeholder(R.drawable.defout_head).crossFade().into(head);
                    if (!TextUtils.isEmpty(student.getPic())) {
                        head.setImageBitmap(onDecodeClicked(student.getPic()));
                    }
                } else {
                    ToastUtil.showToast(getApplicationContext(), getString(R.string.connect_failed_tips));
                }
            }
        }).execute(mUrl);


    }

    public Bitmap onDecodeClicked(String code) {
        byte[] decode = Base64.decode(code, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        return bitmap;
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

    /**
     * 创建再次提交框
     */
    private void createDialog() {
        alertDialog = new AlertDialog.Builder(this).create();
        View view = LayoutInflater.from(this).inflate(R.layout.submit_again, null);
        view.findViewById(R.id.sa_cancle_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        view.findViewById(R.id.sa_sure_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitApply();
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(view);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
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
}
