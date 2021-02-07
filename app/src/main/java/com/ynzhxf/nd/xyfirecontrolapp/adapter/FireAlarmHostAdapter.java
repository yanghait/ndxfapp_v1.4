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
 * Created by nd on 2018-07-25.
 */

public class FireAlarmHostAdapter extends RecyclerView.Adapter<FireAlarmHostAdapter.ViewHolder> {
    private List<FireAlarmBuildFloorCountBean> fireAlarmList;
    private IFireAlarmHostClick fireAlarmHostClick;
    private int errorColor;
    private int normalColor;

    public interface IFireAlarmHostClick {
        void fireAlarmHostClick(FireAlarmBuildFloorCountBean query);

        void fireAlarmDeviceClick(FireAlarmBuildFloorCountBean query);

        void fireAlarmDevicePortClick(FireAlarmBuildFloorCountBean query);
    }


    public FireAlarmHostAdapter(List<FireAlarmBuildFloorCountBean> fireAlarmList, IFireAlarmHostClick fireAlarmHostClick, int errorColor, int normalColor) {
        this.fireAlarmList = fireAlarmList;
        this.fireAlarmHostClick = fireAlarmHostClick;
        this.errorColor = errorColor;
        this.normalColor = normalColor;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View contentView, deviceView, mDevicePort, mSpaceLine;
        TextView txtName;
        TextView txtHostState, txtPortState;
        TextView txtTotalCount, txtNormalCount, txtActionCount, txtFaultCount, txtCloseCount, txtManageCount, txtOpenCount, txtFeedCount, txtFireCount, txtOutlineCount;

        TextView mCirclePoint;

        ViewGroup mParent;

        public ViewHolder(View view, ViewGroup parent) {
            super(view);

            mParent = parent;

            mCirclePoint = view.findViewById(R.id.item_circle_point);

            contentView = view.findViewById(R.id.ly_contant);

            mSpaceLine = view.findViewById(R.id.item_space_line);

            deviceView = view.findViewById(R.id.device_layout);

            mDevicePort = view.findViewById(R.id.device_port_layout);
            txtName = view.findViewById(R.id.txt_name);

            txtHostState = view.findViewById(R.id.txt_host_state);
            txtPortState = view.findViewById(R.id.txt_port_state);

            txtTotalCount = view.findViewById(R.id.txt_total_count);
            txtNormalCount = view.findViewById(R.id.txt_normal);
            txtActionCount = view.findViewById(R.id.txt_action);
            txtFaultCount = view.findViewById(R.id.txt_fault);
            txtCloseCount = view.findViewById(R.id.txt_close);
            txtManageCount = view.findViewById(R.id.txt_manage);
            txtOpenCount = view.findViewById(R.id.txt_open);
            txtFeedCount = view.findViewById(R.id.txt_feed);
            txtFireCount = view.findViewById(R.id.txt_fire);
            txtOutlineCount = view.findViewById(R.id.txt_outline);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fire_alarm_host, parent, false);
        View query = view.findViewById(R.id.ly_contant);
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HelperTool.isFastClick()) return;
                fireAlarmHostClick.fireAlarmHostClick((FireAlarmBuildFloorCountBean) v.getTag());

            }
        });

        View deviceLayout = view.findViewById(R.id.device_layout);
        deviceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fireAlarmHostClick.fireAlarmDeviceClick((FireAlarmBuildFloorCountBean) view.getTag());
            }
        });

        View devicePort = view.findViewById(R.id.device_port_layout);
        devicePort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fireAlarmHostClick.fireAlarmDevicePortClick((FireAlarmBuildFloorCountBean) view.getTag());
            }
        });

        ViewHolder holder = new ViewHolder(view, parent);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FireAlarmBuildFloorCountBean query = fireAlarmList.get(position);
        holder.contentView.setTag(query);
        holder.deviceView.setTag(query);
        holder.mDevicePort.setTag(query);
        holder.txtName.setText(query.getFireHostName());

        holder.txtHostState.setText(query.getHostStateStr());
        holder.txtPortState.setText(query.getHostPortStateStr());

//        if (query.getHostState() == 0) {
//            holder.txtHostState.setTextColor(normalColor);
//            holder.txtPortState.setTextColor(normalColor);
//        } else {
//            holder.txtHostState.setTextColor(errorColor);
//            holder.txtPortState.setTextColor(errorColor);
//        }

        if ("正常".equals(query.getHostStateStr())) {
            holder.txtHostState.setTextColor(normalColor);
        } else {
            holder.txtHostState.setTextColor(errorColor);
        }

        if ("正常".equals(query.getHostPortStateStr())) {
            holder.txtPortState.setTextColor(normalColor);
        } else {
            holder.txtPortState.setTextColor(errorColor);
        }


        holder.txtTotalCount.setText(query.getEquipentCount() + "");
        holder.txtNormalCount.setText(query.getNormalCount() + "");
        holder.txtActionCount.setText(query.getActionCount() + "");
        holder.txtFaultCount.setText(query.getFaultCount() + "");
        holder.txtCloseCount.setText(query.getCloseCount() + "");
        holder.txtManageCount.setText(query.getManageCount() + "");
        holder.txtOpenCount.setText(query.getOpenCount() + "");
        holder.txtFeedCount.setText(query.getFeedCount() + "");
        holder.txtFireCount.setText(query.getFireCount() + "");
        holder.txtOutlineCount.setText(query.getOutLineCount() + "");

        //LogUtils.showLoge("输出正常和全部数量0909---", query.getNormalCount() + "~~~" + query.getEquipentCount());

        if (query.getNormalCount() < query.getEquipentCount()) {
            holder.mCirclePoint.setBackground(holder.mParent.getContext().getResources().getDrawable(R.drawable.solid_point_red));
        } else {
            holder.mCirclePoint.setBackground(holder.mParent.getContext().getResources().getDrawable(R.drawable.circle_point_blue));
        }
    }

    @Override
    public int getItemCount() {
        return fireAlarmList.size();
    }
}

