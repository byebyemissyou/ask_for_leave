package com.kade.lyx.ask_for_leave.utils.utils_parse_json;

import com.kade.lyx.ask_for_leave.entity.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class ParseJson_AD {

    private static String json_result;
    private static List<Student.ADAddress> addressList;


    public static String parse(String jsonString, Student student) {

        try{

            JSONObject jsonObject = new JSONObject(jsonString);
            json_result = jsonObject.getString("result");
            addressList = new ArrayList<>();
            JSONArray array = jsonObject.getJSONArray("data");

            for (int i = 0;i<array.length();i++){

                Student.ADAddress address = student.new ADAddress();
                JSONObject adObj =  (JSONObject) array.get(i);
                address.setName(adObj.getString("name"));

                addressList.add(address);

            }

            student.setAddressList(addressList);

        }catch (JSONException e){


            e.getMessage();

        }

        return String.valueOf(json_result);
    }






}
