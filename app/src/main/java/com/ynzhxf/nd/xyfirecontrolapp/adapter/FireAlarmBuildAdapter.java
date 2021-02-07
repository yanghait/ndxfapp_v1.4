package com.ynzhxf.nd.xyfirecontrolapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireAlarmBuildFloorCountBean;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;

import java.util.List;

/**
 * 火灾报警建筑物列表适配器
 * Created by nd on 2018-07-25.
 */

public class FireAlarmBuildAdapter extends RecyclerView.Adapter<FireAlarmBuildAdapter.ViewHolder>{
    private List<FireAlarmBuildFloorCountBean> fireAlarmList;
    private IFireAlarmBuildClick fireAlarmBuildClick;

    public interface IFireAlarmBuildClick{
        void fireAlarmBuildClick(FireAlarmBuildFloorCountBean query);
    }


    public FireAlarmBuildAdapter(List<FireAlarmBuildFloorCountBean> fireAlarmList, IFireAlarmBuildClick fireAlarmBuildClick){
        this.fireAlarmList = fireAlarmList;
        this.fireAlarmBuildClick= fireAlarmBuildClick;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ViewGroup mParent;
        View contentView;
        TextView txtName ;
        TextView txtFloorCount;
        TextView mCirclePointBuild;
        TextView txtTotalCount,txtNormalCount,txtActionCount , txtFaultCount , txtCloseCount , txtManageCount,txtOpenCount,txtFeedCount,txtFireCount,txtOutlineCount;


        public ViewHolder(View view,ViewGroup parent){
            super(view);
            contentView = view.findViewById(R.id.ly_contant);

            mParent = parent;
            txtName = view.findViewById(R.id.txt_name);

            txtFloorCount = view.findViewById(R.id.txt_floor_count);

            txtTotalCount  = view.findViewById(R.id.txt_total_count);
            txtNormalCount = view.findViewById(R.id.txt_normal);
            txtActionCount = view.findViewById(R.id.txt_action);
            txtFaultCount = view.findViewById(R.id.txt_fault);
            txtCloseCount = view.findViewById(R.id.txt_close);
            txtManageCount = view.findViewById(R.id.txt_manage);
            txtOpenCount = view.findViewById(R.id.txt_open);
            txtFeedCount = view.findViewById(R.id.txt_feed);
            txtFireCount = view.findViewById(R.id.txt_fire);
            txtOutlineCount = view.findViewById(R.id.txt_outline);
            mCirclePointBuild = view.findViewById(R.id.circle_point_build);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fire_alarm_build,parent,false);
        View query = view.findViewById(R.id.ly_contant);
        query.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(HelperTool.isFastClick()) return;
                fireAlarmBuildClick.fireAlarmBuildClick((FireAlarmBuildFloorCountBean)v.getTag());

            }
        });
        ViewHolder holder = new ViewHolder(view,parent);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FireAlarmBuildFloorCountBean query = fireAlarmList.get(position);
        holder.contentView.setTag(query);
        holder.txtName.setText(query.getBuildName());

        holder.txtFloorCount.setText(query.getFloorCount()+"");


        holder.txtTotalCount.setText(query.getEquipentCount()+"");
        holder.txtNormalCount.setText(query.getNormalCount()+"");
        holder.txtActionCount.setText(query.getActionCount()+"");
        holder.txtFaultCount.setText(query.getFaultCount()+"");
        holder.txtCloseCount.setText(query.getCloseCount()+"");
        holder.txtManageCount.setText(query.getManageCount()+"");
        holder.txtOpenCount.setText(query.getOpenCount()+"");
        holder.txtFeedCount.setText(query.getFeedCount()+"");
        holder.txtFireCount.setText(query.getFireCount()+"");
        holder.txtOutlineCount.setText(query.getOutLineCount()+"");


        if (query.getNormalCount() < query.getEquipentCount()) {
            holder.mCirclePointBuild.setBackground(holder.mParent.getContext().getResources().getDrawable(R.drawable.solid_point_red));
        } else {
            holder.mCirclePointBuild.setBackground(holder.mParent.getContext().getResources().getDrawable(R.drawable.circle_point_blue));
        }
    }

    @Override
    public int getItemCount() {
        return fireAlarmList.size();
    }
}

