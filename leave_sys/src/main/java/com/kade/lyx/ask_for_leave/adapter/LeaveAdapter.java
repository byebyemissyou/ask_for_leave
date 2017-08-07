package com.kade.lyx.ask_for_leave.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kade.lyx.ask_for_leave.R;
import com.kade.lyx.ask_for_leave.entity.Student;

import java.util.List;

/**
 * Created by Administrator on 2017/1/9 0009.
 */

public class LeaveAdapter extends BaseAdapter {
    private Context context;
    private List<Student.LeaveDetails> list;

    public LeaveAdapter(Context context, List<Student.LeaveDetails> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LeaveAdapter.ViewHolder vh;

        if (convertView == null) {
            vh = new LeaveAdapter.ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_leave, null);

            vh.leave_type = (TextView) convertView.findViewById(R.id.leave_state);
            vh.leave_tname = (TextView) convertView.findViewById(R.id.leave_tname);
            vh.leave_addtime = (TextView) convertView.findViewById(R.id.leave_addtime);
            vh.leave_reason = (TextView) convertView.findViewById(R.id.leave_reason);
            vh.leave_timeS = (TextView) convertView.findViewById(R.id.leave_timeS);
            vh.leave_timeE = (TextView) convertView.findViewById(R.id.leave_timeE);
            vh.leave_name = (TextView) convertView.findViewById(R.id.leave_name);


            convertView.setTag(vh);

        } else {

            vh = (LeaveAdapter.ViewHolder) convertView.getTag();

        }

        Student.LeaveDetails details = list.get(position);
        String state = "" ;
        switch (details.getState()){

            case "0":
                state = "未审核";
                break;
            case "1":
                state = "已审核";
                break;
            case "2":
                state = "审核未通过";
                break;
            default:
                state = "未知";
        }

        if (null==details.getCname()){

            vh.leave_name.setVisibility(View.GONE);

        }else {

            vh.leave_name.setVisibility(View.VISIBLE);
            vh.leave_name.setText("请假人："+details.getCname());
        }

        vh.leave_type.setText(state);
        vh.leave_tname.setText(details.getTname());
        vh.leave_addtime.setText("申请时间："+details.getAddtime());
        vh.leave_reason.setText("请假原因："+details.getReason());
        vh.leave_timeS.setText("开始时间："+details.getStarttime());
        vh.leave_timeE.setText("结束时间："+details.getEndtime());

        return convertView;

    }


    private class ViewHolder {

        TextView  leave_type, leave_tname, leave_addtime,leave_reason,leave_timeS,leave_timeE,leave_name;

    }

    public void setData(List<Student.LeaveDetails> newPeopleList){
        this.list = newPeopleList;
    }


}
