package com.kade.lyx.ask_for_leave.utils.utils_parse_json;

import com.kade.lyx.ask_for_leave.entity.Student;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lyx on 2016/10/14 0014.
 */

public class ParseJson_BasicInfo {
    private static String json_result;


    public static String parse(String jsonString, Student student) {

        try{

            JSONObject jsonObject = new JSONObject(jsonString);
            json_result = jsonObject.getString("result");
            JSONObject object_data = jsonObject.getJSONObject("data");


                student.setName(object_data.getString("name"));
                student.setSex(object_data.getString("sex"));
                student.setCnumber(object_data.getString("cnumber"));
                student.setDname(object_data.getString("dname"));
                student.setPic(object_data.getString("Pic"));






        }catch (JSONException e){


            e.getMessage();

        }

        return String.valueOf(json_result);
    }



}
