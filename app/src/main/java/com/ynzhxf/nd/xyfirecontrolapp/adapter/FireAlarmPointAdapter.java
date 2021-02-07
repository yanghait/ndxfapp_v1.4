package com.ynzhxf.nd.xyfirecontrolapp.adapter;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireAlarmPointBean;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;

import java.util.List;

/**
 * 火灾报警探头点数据适配器
 * Created by nd on 2018-07-26.
 */

public class FireAlarmPointAdapter extends RecyclerView.Adapter<FireAlarmPointAdapter.ViewHolder>{
    private List<FireAlarmPointBean> fireAlarmList;
    private IFireAlarmPointClick fireAlarmPointClick;

    private Drawable[] colors;


    public interface IFireAlarmPointClick{
        void fireAlarmFloorClick(FireAlarmPointBean query , View view);
    }


    public FireAlarmPointAdapter(List<FireAlarmPointBean> fireAlarmList, IFireAlarmPointClick fireAlarmPointClick , Drawable[] colors){
        this.fireAlarmList = fireAlarmList;
        this.fireAlarmPointClick= fireAlarmPointClick;
        this.colors = colors;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        View v_node ;

        public ViewHolder(View view){
            super(view);
            v_node = view.findViewById(R.id.v_node);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fire_point,parent,false);
        final View query = view.findViewById(R.id.v_node);
        query.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(HelperTool.isFastClick()) return;
                fireAlarmPointClick.fireAlarmFloorClick((FireAlarmPointBean)v.getTag(),query);

            }
        });
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FireAlarmPointBean query = fireAlarmList.get(position);
        holder.v_node.setTag(query);
        Drawable drawable = null;
        if(query.getState() >=0 && query.getState()<=7){
            drawable = colors[query.getState()];
        }else{
            drawable = colors[8];
        }
        holder.v_node.setBackground(drawable);
    }

    @Override
    public int getItemCount() {
        return fireAlarmList.size();
    }
}
