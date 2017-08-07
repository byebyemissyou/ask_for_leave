package com.kade.lyx.ask_for_leave.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kade.lyx.ask_for_leave.R;
import com.kade.lyx.ask_for_leave.entity.TypeBean;

import java.util.List;

/**
 * Created by zh on 2017/7/19.
 */

public class StringTypeAdapter extends RecyclerView.Adapter<StringTypeAdapter.VH> {

    private List<TypeBean> list;

    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public StringTypeAdapter(List<TypeBean> list){
        this.list = list;
    }

    public void setNewData(List<TypeBean> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_string, parent, false);
        return new VH(inflate);
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        final TypeBean typeBean = list.get(position);
        holder.tv_string.setText(typeBean.getName());

        if(typeBean.isSelect()){
            holder.tv_string.setTextColor(Color.parseColor("#ffffff"));
            holder.tv_string.setBackgroundColor(Color.parseColor("#03A9F4"));
        }else{
            holder.tv_string.setTextColor(Color.parseColor("#000000"));
            holder.tv_string.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        holder.tv_string.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClick!=null){
                    onItemClick.onClick(holder.tv_string,position,typeBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class VH extends RecyclerView.ViewHolder{
        TextView tv_string;
        public VH(View itemView) {
            super(itemView);
            tv_string = (TextView) itemView.findViewById(R.id.tv_string);
        }
    }

    public interface OnItemClick{
        void onClick(TextView textView,int pos,TypeBean data);
    }
}
