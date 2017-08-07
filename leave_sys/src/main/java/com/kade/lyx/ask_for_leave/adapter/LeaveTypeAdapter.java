package com.kade.lyx.ask_for_leave.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kade.lyx.ask_for_leave.R;
import com.kade.lyx.ask_for_leave.entity.ConstantPool;
import com.kade.lyx.ask_for_leave.entity.Student;
import com.kade.lyx.ask_for_leave.network.Request_Task;
import com.kade.lyx.ask_for_leave.utils.ToastUtil;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by zh on 2017/7/19.
 */

public class LeaveTypeAdapter extends RecyclerView.Adapter<LeaveTypeAdapter.TypeVH> {
    private List<Student.LeaveDetails> list;
    private Context context;
    private AlertDialog.Builder builder;

    public LeaveTypeAdapter(List<Student.LeaveDetails> list,Context context){
        this.list = list;
        this.context = context;
    }

    public void setNewData(List<Student.LeaveDetails> list){
        this.list = list;
        notifyDataSetChanged();
    }


    private LinkedHashMap<String,String> putMapData(){
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put(ConstantPool.PARAM_NAME,"Login");
        map.put("cnumber", "888888");
        map.put("pwd", "000000");

        return map;

    }

    @Override
    public TypeVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leave, parent, false);
        return new TypeVH(inflate);
    }

    @Override
    public void onBindViewHolder(TypeVH vh, final int position) {
        Student.LeaveDetails details = list.get(position);
        String state = "" ;
        builder = new  AlertDialog.Builder(context);
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

        if (state.equals("未审核")){

            vh.leave_quit.setVisibility(View.VISIBLE);

        }else {

            vh.leave_quit.setVisibility(View.GONE);

        }

        vh.leave_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {






                builder.setTitle("请求网络").setMessage("返回data").
                        setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                                new Request_Task(putMapData(), new Request_Task.CallBack() {
                                    @Override
                                    public void doResult(String data) {

                                        ToastUtil.showToast(context,"返回data  = "+data);


                                    }
                                }).execute(ConstantPool.URL);



                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                }).show();





            }
        });


        vh.leave_type.setText(state);
        vh.leave_tname.setText(details.getTname());
        vh.leave_addtime.setText("申请时间："+details.getAddtime());
        vh.leave_reason.setText("请假原因："+details.getReason());
        vh.leave_timeS.setText("开始时间："+details.getStarttime());
        vh.leave_timeE.setText("结束时间："+details.getEndtime());



    }

    @Override
    public int getItemCount() {

            return list.size();
    }

    class TypeVH extends RecyclerView.ViewHolder{
        TextView leave_type;
        TextView leave_tname;
        TextView leave_addtime;
        TextView leave_reason;
        TextView leave_timeS;
        TextView leave_timeE;
        TextView leave_name;
        Button leave_quit;

        public TypeVH(View itemView) {
            super(itemView);

            leave_type = (TextView) itemView.findViewById(R.id.leave_state);
            leave_tname = (TextView) itemView.findViewById(R.id.leave_tname);
            leave_addtime = (TextView) itemView.findViewById(R.id.leave_addtime);
            leave_reason = (TextView) itemView.findViewById(R.id.leave_reason);
            leave_timeS = (TextView) itemView.findViewById(R.id.leave_timeS);
            leave_timeE = (TextView) itemView.findViewById(R.id.leave_timeE);
            leave_name = (TextView) itemView.findViewById(R.id.leave_name);
            leave_quit = (Button)itemView.findViewById(R.id.leave_quit);

        }
    }
}
