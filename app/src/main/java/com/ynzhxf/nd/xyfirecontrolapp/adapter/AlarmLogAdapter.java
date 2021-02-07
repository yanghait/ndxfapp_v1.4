package com.ynzhxf.nd.xyfirecontrolapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.AlarmLogBean;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.HistoryAlarmCreateOrderActivity;

import java.util.List;

/**
 * 报警记录数据适配器
 * Created by nd on 2018-07-11.
 */

public class AlarmLogAdapter extends RecyclerView.Adapter<AlarmLogAdapter.ViewHolder> {
    private List<AlarmLogBean> alarmList;

    private Activity activity;

    public AlarmLogAdapter(Activity activity, List<AlarmLogBean> alarmList) {
        this.alarmList = alarmList;
        this.activity = activity;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtAlarmTime, txtAlarmPosition, txtAlarmValue, txtAlarmSub, txtAlarmMessage;

        Button sponsorOrder;

        public ViewHolder(View view) {
            super(view);
            txtAlarmTime = view.findViewById(R.id.txt_alarm_time);
            txtAlarmPosition = view.findViewById(R.id.txt_alarm_position);
            txtAlarmValue = view.findViewById(R.id.txt_alarm_value);
            txtAlarmSub = view.findViewById(R.id.txt_alarm_sub);
            txtAlarmMessage = view.findViewById(R.id.txt_alarm_message);
            sponsorOrder = view.findViewById(R.id.sponsor_work);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm_log, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AlarmLogBean queryBean = alarmList.get(position);
        holder.txtAlarmTime.setText(queryBean.getEventTimeStr());
        holder.txtAlarmPosition.setText(queryBean.getAreaName());
        holder.txtAlarmValue.setText(queryBean.getAlarmValue());
        holder.txtAlarmSub.setText(queryBean.getSubconditionName());
        holder.txtAlarmMessage.setText(queryBean.getMessage());

        int loginType = SPUtils.getInstance().getInt("LoginType");
        if (loginType == 3) {

            if (queryBean.getEventType() != null && ("报警事件".equals(queryBean.getEventType().getName()) || "故障事件".equals(queryBean.getEventType().getName()) ||
                    "运行事件".equals(queryBean.getEventType().getName()))) {

                if (!StringUtils.isEmpty(queryBean.getAlarmValue()) && "主机复位".equals(queryBean.getAlarmValue())) {
                    holder.sponsorOrder.setVisibility(View.INVISIBLE);
                } else {
                    holder.sponsorOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String createOrderContent = queryBean.getSubconditionName() + "\n" + queryBean.getAreaName() + "\n" + queryBean.getAlarmValue() +
                                    "\n" + queryBean.getEventTimeStr() + "\n" + queryBean.getMessage();
                            Intent intent = new Intent(activity, HistoryAlarmCreateOrderActivity.class);
                            intent.putExtra("projectId", queryBean.getProjectID());
                            intent.putExtra("baseNodeId", queryBean.getBaseNodeID());
                            intent.putExtra("content", createOrderContent);
                            if (StringUtils.isEmpty(queryBean.getProjectID()) || StringUtils.isEmpty(queryBean.getBaseNodeID()) || StringUtils.isEmpty(createOrderContent)) {
                                HelperView.Toast(activity, "未获取到报警信息,无法发起工单!");
                                return;
                            }
                            activity.startActivity(intent);
                        }
                    });
                }
            } else {
                holder.sponsorOrder.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.sponsorOrder.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return alarmList.size();
    }
}
