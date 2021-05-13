package com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.bean.SmlSystemInfo;

import java.util.ArrayList;
import java.util.List;

public class SmlSysAdapter extends RecyclerView.Adapter {

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private List<SmlSystemInfo> smlSystemInfos = new ArrayList<>();

    public SmlSysAdapter() {

    }

    public void update(List<SmlSystemInfo> data) {
        smlSystemInfos = data;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project_system_list, parent, false);
        SysViewholder sysViewholder = new SysViewholder(view);
        return sysViewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SysViewholder viewholder = (SysViewholder) holder;
        SmlSystemInfo smlSystemInfo = smlSystemInfos.get(position);
        viewholder.txtName.setText(smlSystemInfo.getProjectSystemName());
        viewholder.mAlarmCount.setText(smlSystemInfo.getAlarmCount() + "");
        viewholder.mMoitorCount.setText(smlSystemInfo.getTagCount() + "");
        if (smlSystemInfo.getProjectSystemTypeID().equals("11000")) {
            viewholder.imageView.setImageResource(R.mipmap.icon_cannon_sys);
        } else if (smlSystemInfo.getProjectSystemTypeID().equals("665")) {
            viewholder.imageView.setImageResource(R.mipmap.icon_smart_power_sys);
        } else {
            viewholder.imageView.setImageResource(R.mipmap.ic_launcher_logo);
        }
        viewholder.contentView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(smlSystemInfo.getProjectSystemTypeID(),smlSystemInfo.getProjectSystemId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return smlSystemInfos.size();
    }

    public class SysViewholder extends RecyclerView.ViewHolder {
        View contentView, alarmLayout;
        ImageView imageView;
        TextView txtName, txtDescribe, mAlarmCount, mMoitorCount;

        public SysViewholder(@NonNull View view) {
            super(view);
            contentView = view.findViewById(R.id.container);
            alarmLayout = view.findViewById(R.id.alarm_real_layout);
            imageView = view.findViewById(R.id.im_img);
            txtName = (TextView) view.findViewById(R.id.txt_name);
            txtDescribe = view.findViewById(R.id.txt_describe);

            mAlarmCount = view.findViewById(R.id.alarm_count);
            mMoitorCount = view.findViewById(R.id.monitor_count);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String typeId,String sysId);
    }
}
