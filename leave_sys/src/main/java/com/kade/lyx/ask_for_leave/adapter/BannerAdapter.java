package com.kade.lyx.ask_for_leave.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class BannerAdapter extends LoopPagerAdapter {
    private Context context;
    private ImageView view;
    private List<String> urlLists;


    public BannerAdapter(Context context, RollPagerView viewPager ,List<String> urlLists) {
        super(viewPager);
        this.context = context;
        this.urlLists = urlLists;
    }


    @Override
    public int getRealCount() {
        return urlLists.size();
    }

    @Override
    public View getView(ViewGroup container, int position) {
        view = new ImageView(container.getContext());
        Picasso.with(context).load(urlLists.get(position)).into(view);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

}
