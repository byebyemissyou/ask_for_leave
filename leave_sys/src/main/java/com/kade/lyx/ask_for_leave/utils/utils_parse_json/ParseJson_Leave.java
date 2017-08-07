package com.kade.lyx.ask_for_leave.utils.utils_parse_json;

import com.kade.lyx.ask_for_leave.entity.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/9 0009.
 */

public class ParseJson_Leave {
    private static String json_result;
    private static List<Student.LeaveDetails> leaveDetailses;


    public static String parse(String jsonString, Student student) {

        try{

            JSONObject jsonObject = new JSONObject(jsonString);
            json_result = jsonObject.getString("result");
            student.setTotal(jsonObject.getString("total"));
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            leaveDetailses = new ArrayList<>();

            for (int i=0;i<jsonArray.length();i++){
                Student.LeaveDetails leaveDetails = student.new LeaveDetails();
                JSONObject leave_object =  (JSONObject) jsonArray.get(i);
                leaveDetails.setTname(leave_object.getString("tname"));
                leaveDetails.setStarttime(leave_object.getString("starttime"));
                leaveDetails.setEndtime(leave_object.getString("endtime"));
                leaveDetails.setState(leave_object.getString("state"));
                leaveDetails.setReason(leave_object.getString("reason"));
                leaveDetails.setAddtime(leave_object.getString("addtime"));

                leaveDetailses.add(leaveDetails);

            }


            student.setLeaveDetailsList(leaveDetailses);



        }catch (JSONException e){


            e.getMessage();

        }

        return String.valueOf(json_result);
    }





}
