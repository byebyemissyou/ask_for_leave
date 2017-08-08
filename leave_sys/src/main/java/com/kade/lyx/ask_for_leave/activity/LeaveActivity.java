package com.kade.lyx.ask_for_leave.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.kade.lyx.ask_for_leave.BasicActivity;
import com.kade.lyx.ask_for_leave.R;
import com.kade.lyx.ask_for_leave.adapter.LeaveTypeAdapter;
import com.kade.lyx.ask_for_leave.adapter.StringTypeAdapter;
import com.kade.lyx.ask_for_leave.entity.ConstantPool;
import com.kade.lyx.ask_for_leave.entity.Student;
import com.kade.lyx.ask_for_leave.entity.TypeBean;
import com.kade.lyx.ask_for_leave.network.Request_Task;
import com.kade.lyx.ask_for_leave.utils.ToastUtil;
import com.kade.lyx.ask_for_leave.utils.utils_parse_json.ParseJson_Leave;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class LeaveActivity extends BasicActivity {

    private LinkedHashMap<String, String> map;
    private Student student;
    private List<Student.LeaveDetails> list;
    private LeaveTypeAdapter adapter;
    private static final String TAG = "LeaveActivity";
    private double exitTime = 0;

    private ArrayList<String> list_type;

    private RecyclerView rv_type_1;
    private RecyclerView rv_type_3;
    private StringTypeAdapter adapter1;
    private ArrayList<TypeBean> type1;
    private String cid;


    public void putMapData() {

        map = new LinkedHashMap<>();
        map.put(ConstantPool.PARAM_NAME, "GetLeaveDetailsByPage");
        map.put(ConstantPool.UID, getIntent().getStringExtra("cid"));
        map.put("type", "");
        map.put("startdate", "1990-01-01 00:00");
        map.put("enddate", "2020-01-01 00:00");
        map.put("pagesize", "1000");
        map.put("currpage", "0");

    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_new);

        list_type = getIntent().getStringArrayListExtra("type");
        cid = getIntent().getStringExtra("cid");
        if (list_type == null) {
            list_type = new ArrayList<>();
        }
        newList = new ArrayList<>();
        initViews();
        initListener();
        putMapData();
        student = new Student();


        new Request_Task(map, new Request_Task.CallBack() {
            @Override
            public void doResult(String data) {

                Log.i(TAG, "data = \n " + data);


                if (!data.equals(ConstantPool.RESULT_FAILED)) {
                    ParseJson_Leave.parse(data, student);


                    if (student.getLeaveDetailsList().size() != 0) {

                        rv_type_3.setLayoutManager(new LinearLayoutManager(LeaveActivity.this));
                        list = student.getLeaveDetailsList();
                        adapter = new LeaveTypeAdapter(list,LeaveActivity.this,cid);
                        rv_type_3.setAdapter(adapter);


                        ToastUtil.showToast(getApplicationContext(), "查询成功");

                    } else {

                        ToastUtil.showToast(getApplicationContext(), "没有请假数据！");

                    }

                } else {

                    ToastUtil.showToast(getApplicationContext(), getString(R.string.connect_failed_tips));

                }

            }
        }).execute(ConstantPool.URL);


    }

    private List<Student.LeaveDetails> newList;

    private void initListener() {
        adapter1.setOnItemClick(new StringTypeAdapter.OnItemClick() {
            @Override
            public void onClick(TextView textView, int pos, TypeBean data) {
                newList = new ArrayList<>();

                if (data.getName().equals("全部类型")) {
                    newList = list;
                } else {

                    if (null != list && null != newList) {

                        for (Student.LeaveDetails leaveDetails : list) {
                            if (leaveDetails.getTname().equals(data.getName())) {
                                newList.add(leaveDetails);
                            }
                        }

                    }


                }

                List<TypeBean> list1 = new ArrayList<>();
                for (int i = 0; i < type1.size(); i++) {
                    if (i == pos) {
                        TypeBean typeBean = type1.get(pos);
                        typeBean.setSelect(true);
                        list1.add(typeBean);
                    } else {
                        TypeBean typeBean = type1.get(i);
                        typeBean.setSelect(false);
                        list1.add(typeBean);
                    }
                }

                adapter1.setNewData(list1);

                if (null!=newList){

                    if (newList.size() == 0) {
                        ToastUtil.showToast(getApplicationContext(), "没有请假数据！");
                    }

                }

                if (null != adapter && null != newList) {

                    adapter.setNewData(newList);
                    rv_type_3.setAdapter(adapter);

                }


            }
        });

    }

    private void initViews() {
        rv_type_1 = (RecyclerView) findViewById(R.id.rv_type_1);
        rv_type_3 = (RecyclerView) findViewById(R.id.rv_type_3);

        type1 = new ArrayList<>();
        for (int i = 0; i < list_type.size(); i++) {
            TypeBean typeBean = null;
            if (i == 0) {
                typeBean = new TypeBean(list_type.get(i), true);
            } else {
                typeBean = new TypeBean(list_type.get(i), false);
            }
            type1.add(typeBean);
        }

        rv_type_1.setLayoutManager(new LinearLayoutManager(this));
        adapter1 = new StringTypeAdapter(type1);
        rv_type_1.setAdapter(adapter1);


    }


}
