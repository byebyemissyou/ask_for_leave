package com.kade.lyx.ask_for_leave.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuBuilder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.kade.lyx.ask_for_leave.BasicActivity;
import com.kade.lyx.ask_for_leave.R;
import com.kade.lyx.ask_for_leave.adapter.LeaveAdapter;
import com.kade.lyx.ask_for_leave.entity.ConstantPool;
import com.kade.lyx.ask_for_leave.entity.Student;
import com.kade.lyx.ask_for_leave.network.Request_Task;
import com.kade.lyx.ask_for_leave.utils.DateRangePickerFragment;
import com.kade.lyx.ask_for_leave.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import me.kareluo.ui.OptionMenu;
import me.kareluo.ui.OptionMenuView;
import me.kareluo.ui.PopupMenuView;
import me.kareluo.ui.PopupView;

public class LeaveCheckActivity extends BasicActivity implements DateRangePickerFragment.OnDateRangeSelectedListener {
    private AlertDialog.Builder builder_check;
    private ListView check_list;
    private EditText leave_time, leave_time2;
    private MaterialSpinner leave_type;
    private String type = "", json_result, menuTitle;
    private String s_year = "", s_mon, s_d, e_year, e_mon, e_d;
    private String defaultStart, defaultEnd;
    private String leaveId, checkType = "";
    private DateRangePickerFragment dateRangePickerFragment;
    private List<Student.LeaveDetails> leaveDetailsList;
    private LeaveAdapter adapter;
    private LinkedList<String> list_ti;
    private LinkedList<String> list_tn;
    private double exitTime = 0;

    private static final String TAG = "LeaveCheckTest";
    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_check);
        check_list = (ListView) findViewById(R.id.check_list);
        leave_time = (EditText) findViewById(R.id.leave_time);
        leave_time2 = (EditText) findViewById(R.id.leave_time2);
        leave_type = (MaterialSpinner) findViewById(R.id.leave_type);
        leave_type.setTextSize(12.0f);
        leave_type.setPadding(50, 1, 25, 1);

        dateRangePickerFragment = DateRangePickerFragment.newInstance(LeaveCheckActivity.this, false, R.layout.date_range_picker);

        //获取请假类型
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
        if (android.os.Build.VERSION.SDK_INT >= 19) {

            dateRangePickerFragment.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog);
        }

        setThisMonth();
        leave_time.setText(defaultStart);
        leave_time2.setText(defaultEnd);
        leave_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dateRangePickerFragment.show(getSupportFragmentManager(), "datePicker");

            }
        });

        leave_time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dateRangePickerFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });


    }

    //重置数据
    private void resetData() {

        if (leaveDetailsList != null) {

            if (leaveDetailsList.size() != 0) {
                //清空currList
                leaveDetailsList.clear();
                adapter.notifyDataSetChanged();
            }


        }


    }

    private void setThisMonth() {

        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.getTime();
        defaultStart = dateFormater.format(cal.getTime()) + "";

        cal.set(Calendar.DAY_OF_MONTH,
                cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        defaultEnd = dateFormater.format(cal.getTime()) + "";
    }

    private LinkedHashMap<String, String> putQueryMap() {

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(ConstantPool.PARAM_NAME, "GetNeedAuditLeaveByPage");
        map.put(ConstantPool.UID, getIntent().getStringExtra("cid"));
        map.put("type", type);
        if (s_year.equals("")) {

            map.put("startdate", defaultStart);
            map.put("enddate", defaultEnd);


        } else {

            map.put("startdate", s_year + "-" + s_mon + "-" + s_d);
            map.put("enddate", e_year + "-" + e_mon + "-" + e_d);


        }
        map.put("pagesize", "1000");
        map.put("currpage", "1");

        return map;


    }

    //获取待审核请假类型请求
    private LinkedHashMap<String, String> putMapData_type() {
        LinkedHashMap<String, String> map_type = new LinkedHashMap<>();
        map_type.put(ConstantPool.PARAM_NAME, "GetLeaveType");
        return map_type;

    }

    //提交审核结果请求
    private LinkedHashMap<String, String> putCheckMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(ConstantPool.PARAM_NAME, "AuditLeave");
        map.put("lid", leaveId);
        map.put("type", checkType);
        return map;
    }


    //解析请假类型并设置下拉菜单的点击事件
    private void parseJson_leaveType(String json) {

        try {

            JSONObject object = new JSONObject(json);
            JSONArray jsonArray = object.getJSONArray("data");
            list_tn = new LinkedList<>();
            list_ti = new LinkedList<>();
            list_tn.add("全部类型");
            list_ti.add("");
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject type_object = (JSONObject) jsonArray.get(i);

                list_tn.add(type_object.getString("name"));
                list_ti.add(type_object.getString("id"));


            }

            leave_type.setItems(list_tn);

            leave_type.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                    type = list_ti.get(position);
                    resetData();


                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //解析提交结果
    private String parseJson(String json) {
        try {


            JSONObject object = new JSONObject(json);
            return object.getString("result");


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return "";
    }

    public void queryLeaveCheckList(View view) {

        if (isNetworkConnected()) {

            queryCheckList();

        } else {

            ToastUtil.showToast(getApplicationContext(), getString(R.string.no_network_tips));

        }

    }

    private String parseCheckListData(String jsonString, Student student) {

        try {

            JSONObject jsonObject = new JSONObject(jsonString);
            json_result = jsonObject.getString("result");
            student.setTotal(jsonObject.getString("total"));
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            List<Student.LeaveDetails> leaveDetailses = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                Student.LeaveDetails leaveDetails = student.new LeaveDetails();
                JSONObject leave_object = (JSONObject) jsonArray.get(i);
                leaveDetails.setId(leave_object.getString("id"));
                leaveDetails.setCname(leave_object.getString("cname"));
                leaveDetails.setTname(leave_object.getString("tname"));
                leaveDetails.setStarttime(leave_object.getString("starttime"));
                leaveDetails.setEndtime(leave_object.getString("endtime"));
                leaveDetails.setState(leave_object.getString("state"));
                leaveDetails.setReason(leave_object.getString("reason"));
                leaveDetails.setAddtime(leave_object.getString("addtime"));

                leaveDetailses.add(leaveDetails);

            }

            student.setLeaveDetailsList(leaveDetailses);


        } catch (JSONException e) {


            e.getMessage();

        }

        return String.valueOf(json_result);
    }




    private void queryCheckList() {

        new Request_Task(putQueryMap(), new Request_Task.CallBack() {
            @Override
            public void doResult(String data) {
                Log.i(TAG, "GetNeedAuditLeaveByPage.data =  " + data);


                if (!data.equals(ConstantPool.RESULT_FAILED)) {
                    builder_check = new AlertDialog.Builder(LeaveCheckActivity.this);
                    builder_check.setTitle("提交审核"); //设置标题
                    builder_check.setIcon(R.mipmap.quit_new);//设置图标，图片id即可

                    student = new Student();

                    parseCheckListData(data, student);
                    leaveDetailsList = student.getLeaveDetailsList();

                    if (student.getLeaveDetailsList().size() != 0) {
                        adapter = new LeaveAdapter(getApplicationContext(), leaveDetailsList);
                        check_list.setAdapter(adapter);

                        check_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                leaveId = student.getLeaveDetailsList().get(position).getId();
                                PopupMenuView menuView = new PopupMenuView(LeaveCheckActivity.this.getApplicationContext(),
                                        R.menu.popmenu, new MenuBuilder(LeaveCheckActivity.this.getApplicationContext()));
                                menuView.setOnMenuClickListener(new OptionMenuView.OnOptionMenuClickListener() {
                                    @Override
                                    public boolean onOptionMenuClick(int position_pop, final OptionMenu menu) {
                                        menuTitle = String.valueOf(menu.getTitle());
                                        builder_check.setMessage("确认将此请假的状态从未审核更改为" + menuTitle + "吗？"); //设置内容
                                        switch (menuTitle) {
                                            case "审核通过":
                                                checkType = "1";
                                                break;
                                            case "审核不通过":
                                                checkType = "2";
                                                break;
                                        }

                                        builder_check.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(final DialogInterface dialog, int which) {


                                                new Request_Task(putCheckMap(), new Request_Task.CallBack() {
                                                    @Override
                                                    public void doResult(String data) {
                                                        dialog.dismiss();

                                                        if (parseJson(data).equals("0")) {

                                                            ToastUtil.showToast(getApplicationContext(), "审核状态更改成功！");

                                                            queryCheckList();


                                                        } else {

                                                            ToastUtil.showToast(getApplicationContext(), "更改失败 ！");

                                                        }


                                                    }
                                                }).execute(ConstantPool.URL);


                                            }

                                        });
                                        builder_check.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();

                                            }
                                        });

                                        //参数都设置完成了，创建并显示出来
                                        builder_check.create().show();


                                        return true;
                                    }
                                });

                                menuView.setSites(PopupView.SITE_TOP);
                                menuView.show(view);


                            }
                        });

                    } else {
                        leaveDetailsList.clear();
                        adapter.setData(leaveDetailsList);
                        adapter.notifyDataSetChanged();
                        ToastUtil.showToast(getApplicationContext(), "没有待审核的数据!");



                    }


                } else {

                    ToastUtil.showToast(getApplicationContext(), getString(R.string.connect_failed_tips));

                }


            }
        }).execute(ConstantPool.URL);
    }



    @Override
    public void onDateRangeSelected(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear) {
        s_year = String.valueOf(startYear);
        e_year = String.valueOf(endYear);
        if (startMonth + 1 < 10) {
            s_mon = String.valueOf("0" + (startMonth + 1));
        } else {
            s_mon = String.valueOf((startMonth + 1));
        }
        if (startDay < 10) {
            s_d = String.valueOf("0" + startDay);
        } else {
            s_d = String.valueOf(startDay);
        }
        if (endDay < 10) {
            e_d = String.valueOf("0" + endDay);
        } else {
            e_d = String.valueOf(endDay);
        }
        if (endMonth + 1 < 10) {
            e_mon = String.valueOf("0" + (endMonth + 1));
        } else {
            e_mon = String.valueOf((endMonth + 1));
        }
        if (startYear > endYear) {
            s_year = "";
            ToastUtil.showToast(getApplicationContext(), "请正确选择年！！！");
        } else if (startYear == endYear && startMonth > endMonth) {
            s_year = "";
            ToastUtil.showToast(getApplicationContext(), "请正确选择月！！！");
        } else if (startYear == endYear && startMonth == endMonth && startDay > endDay) {
            s_year = "";
            ToastUtil.showToast(getApplicationContext(), "请正确选择日！！！");
        } else {
            leave_time.setText(s_year + "-" + s_mon + "-" + s_d.substring(0, 2));
            leave_time2.setText(e_year + "-" + e_mon + "-" + e_d.substring(0, 2));
            resetData();
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
