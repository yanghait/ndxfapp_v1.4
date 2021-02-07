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
 * Created by nd on 2018-07-11.
 */

public class FireAlarmFloorAdapter extends RecyclerView.Adapter<FireAlarmFloorAdapter.ViewHolder> {
    private List<FireAlarmBuildFloorCountBean> fireAlarmList;
    private IFireAlarmFloorClick fireAlarmFloorClick;


    public interface IFireAlarmFloorClick {
        void fireAlarmFloorClick(FireAlarmBuildFloorCountBean query);
    }


    public FireAlarmFloorAdapter(List<FireAlarmBuildFloorCountBean> fireAlarmList, IFireAlarmFloorClick fireAlarmFloorClick) {
        this.fireAlarmList = fireAlarmList;
        this.fireAlarmFloorClick = fireAlarmFloorClick;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;

        public ViewHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.txt_name);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fire_floor, parent, false);
        View query = view.findViewById(R.id.txt_name);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HelperTool.isFastClick()) return;
                fireAlarmFloorClick.fireAlarmFloorClick((FireAlarmBuildFloorCountBean) v.getTag());

            }
        });
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FireAlarmBuildFloorCountBean query = fireAlarmList.get(position);
        holder.txtName.setTag(query);
        holder.txtName.setText(String.valueOf(query.getFloor() + "层"));
        holder.txtName.setTextColor(query.getFloorCount());
    }

    @Override
    public int getItemCount() {
        return fireAlarmList.size();
    }
}
