package com.kade.lyx.ask_for_leave.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kade.lyx.ask_for_leave.R;
import com.kade.lyx.ask_for_leave.activity.DetialActivity;
import com.kade.lyx.ask_for_leave.entity.ConstantPool;
import com.kade.lyx.ask_for_leave.entity.Student;
import com.kade.lyx.ask_for_leave.network.Request_Task;
import com.kade.lyx.ask_for_leave.utils.ToastUtil;
import com.kade.lyx.ask_for_leave.utils.sharedpreferences.UnableClearSharepreferen;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by zh on 2017/7/19.
 */

public class LeaveTypeAdapter extends RecyclerView.Adapter<LeaveTypeAdapter.TypeVH> {
    private List<Student.LeaveDetails> list;
    private Activity context;
    private AlertDialog.Builder builder;
    private String cid;

    public LeaveTypeAdapter(List<Student.LeaveDetails> list, Activity context, String cid) {
        this.list = list;
        this.context = context;
        this.cid = cid;
    }

    public void setNewData(List<Student.LeaveDetails> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    private LinkedHashMap<String, String> putMapData(String l_id) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(ConstantPool.PARAM_NAME, "CancelLeave");
        map.put("id", cid);
        map.put("lid", l_id);

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
        String state = "";
        builder = new AlertDialog.Builder(context);

        switch (details.getState()) {

            case "0":
                state = "未审核";
                break;
            case "1":
                state = "审核通过";
                break;
            case "2":
                state = "审核未通过";
                break;
            case "3": //2017/10/18 10:27 和罗成确认新增状态
                state = "审核中";
                break;
            default:
                state = "未知";
        }

        if (null == details.getCname()) {

            vh.leave_name.setVisibility(View.GONE);

        } else {

            vh.leave_name.setVisibility(View.VISIBLE);
            vh.leave_name.setText("请假人：" + details.getCname());
        }

//        if (state.equals("未审核")) {
//
//            vh.leave_quit.setVisibility(View.VISIBLE);
//
//        } else {
//
//            vh.leave_quit.setVisibility(View.GONE);
//
//        }

        if (details.getState().equals("0")) {
            vh.leave_quit.setText("取消请假");
        } else {
            vh.leave_quit.setText("查询详情");
        }

        vh.leave_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getState().equals("0")) {
                    builder.setTitle("确定取消该请假吗？").setMessage("删除此条请假信息").
                            setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();

//                                ToastUtil.showToast(context,"position = " +position +"\n lid = "+list.get(position).getLid());

                                    //需要在这里再确定position否则位置不对
                                    new Request_Task(putMapData(list.get(position).getLid()), new Request_Task.CallBack() {
                                        @Override
                                        public void doResult(String data) {

                                            try {
                                                JSONObject object = new JSONObject(data);

                                                if (object.getString("result").equals("0")) {

                                                    ToastUtil.showToast(context, "请假取消成功");

                                                    //重启Activity以刷新数据 （更新整个页面所有的数据方法比较困难，暂时用重启activity处理）
                                                    reStartActivity();

                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).execute(UnableClearSharepreferen.getInstance().getServerAddress(context));


                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                        }
                    }).show();

                } else {
                    Intent intent = new Intent(context, DetialActivity.class);
                    intent.putExtra("lid", list.get(position).getLid());
                    intent.putExtra("cid", cid);
                    intent.putExtra("status", list.get(position).getState());
                    context.startActivity(intent);
                }
            }
        });

        vh.leave_type.setText(state);
        vh.leave_tname.setText(details.getTname());
        vh.leave_addtime.setText("申请时间：" + details.getAddtime());
        vh.leave_reason.setText("请假原因：" + details.getReason());
        vh.leave_timeS.setText("开始时间：" + details.getStarttime());
        vh.leave_timeE.setText("结束时间：" + details.getEndtime());
    }

    private void reStartActivity() {
        Intent intent = context.getIntent();
        context.finish();
        context.startActivity(intent);
    }


    @Override
    public int getItemCount() {

        return list.size();
    }

    class TypeVH extends RecyclerView.ViewHolder {

        TextView leave_type;
        TextView leave_tname;
        TextView leave_addtime;
        TextView leave_reason;
        TextView leave_timeS;
        TextView leave_timeE;
        TextView leave_name;
        Button leave_quit;


        private TypeVH(View itemView) {
            super(itemView);
            leave_type = (TextView) itemView.findViewById(R.id.leave_state);
            leave_tname = (TextView) itemView.findViewById(R.id.leave_tname);
            leave_addtime = (TextView) itemView.findViewById(R.id.leave_addtime);
            leave_reason = (TextView) itemView.findViewById(R.id.leave_reason);
            leave_timeS = (TextView) itemView.findViewById(R.id.leave_timeS);
            leave_timeE = (TextView) itemView.findViewById(R.id.leave_timeE);
            leave_name = (TextView) itemView.findViewById(R.id.leave_name);
            leave_quit = (Button) itemView.findViewById(R.id.leave_quit);


        }
    }
}
