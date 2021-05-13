package com.ynzhxf.nd.xyfirecontrolapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.bean.SmartPowerInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmartPowerAdapter extends RecyclerView.Adapter {

    private Context mContext;

    private OnItemClickListener onItemClickListener;

    private Map<Integer, Boolean> openItemMap = new HashMap<Integer, Boolean>();

    List<SmartPowerInfo> smartPowerInfoList = new ArrayList<>();

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public SmartPowerAdapter(Context context) {
        this.mContext = context;
    }

    public void update(List<SmartPowerInfo> data) {
        smartPowerInfoList = data;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.smart_power_cannon, parent, false);
        SmartPowerViewHolder waterCannonViewHolder = new SmartPowerViewHolder(view);
        return waterCannonViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SmartPowerViewHolder viewHolder = (SmartPowerViewHolder) holder;
        SmartPowerInfo smartPowerInfo = smartPowerInfoList.get(position);
        viewHolder.smart_powerNo_txt.setText(smartPowerInfo.getName());
        viewHolder.smart_power_switch.setChecked(smartPowerInfo.isSwitchStatus());
        viewHolder.smart_powerState_txt.setText(smartPowerInfo.getSwitchStatus());
        viewHolder.smart_onlineState_txt.setText(smartPowerInfo.getOnlineStatus());
        viewHolder.smart_voltage_txt.setText(smartPowerInfo.getVoltage());
        viewHolder.smart_electric_txt.setText(smartPowerInfo.getCurrent());
        viewHolder.smart_power_txt.setText(smartPowerInfo.getPower());
        viewHolder.smart_leakage_txt.setText(smartPowerInfo.getLeakageCurrent());
        viewHolder.smart_total_txt.setText(smartPowerInfo.getTotalElectricity());
        viewHolder.smart_temperature_txt.setText(smartPowerInfo.getTemperature());

        if (smartPowerInfo.getId() != null && !smartPowerInfo.getId().equals("")) {
            viewHolder.smart_power_switch.setVisibility(View.VISIBLE);
            viewHolder.smart_powerNo_txt.setTextColor(Color.parseColor("#3E7BFC"));
            viewHolder.smart_powerNo_txt.setBackgroundResource(R.drawable.shape_bg_blue_right_rectangle);
        } else {
            viewHolder.smart_powerNo_txt.setTextColor(Color.parseColor("#08CDBB"));
            viewHolder.smart_powerNo_txt.setBackgroundResource(R.drawable.shape_bg_green_right_rectangle);
            viewHolder.smart_power_switch.setVisibility(View.GONE);
        }

        if (smartPowerInfo.getVmSmartPowerChildren() != null && smartPowerInfo.getVmSmartPowerChildren().size() > 0) {
            viewHolder.smart_powerNum_txt.setText(smartPowerInfo.getVmSmartPowerChildren().size() + "");
            viewHolder.power_open_layout.setVisibility(View.VISIBLE);
            SmartPowerAdapter smartPowerAdapter = new SmartPowerAdapter(mContext);
            viewHolder.power_sub_reycler.setAdapter(smartPowerAdapter);
            smartPowerAdapter.update(smartPowerInfo.getVmSmartPowerChildren());
        } else {
            viewHolder.smart_powerNum_txt.setText("");
            viewHolder.power_open_layout.setVisibility(View.GONE);
        }
        viewHolder.smart_power_switch.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(smartPowerInfo.getId());
            }
        });
        viewHolder.power_open_layout.setOnClickListener(v -> {
            if (openItemMap.containsKey(position) && openItemMap.get(position)) {
                openItemMap.put(position, false);
            } else {
                openItemMap.put(position, true);
            }
            this.notifyDataSetChanged();
        });
        if (openItemMap.containsKey(position) && openItemMap.get(position)) {
            viewHolder.power_open_img.setImageResource(R.drawable.icon_item_close);
            viewHolder.power_sub_reycler.setVisibility(View.VISIBLE);
        } else {
            viewHolder.power_open_img.setImageResource(R.drawable.icon_item_open);
            viewHolder.power_sub_reycler.setVisibility(View.GONE);
        }

    }


    public class SmartPowerViewHolder extends RecyclerView.ViewHolder {

        TextView smart_powerNo_txt;

        Switch smart_power_switch;

        TextView smart_powerState_txt;

        TextView smart_onlineState_txt;

        TextView smart_powerNum_txt;

        TextView smart_voltage_txt;

        TextView smart_electric_txt;

        TextView smart_power_txt;

        TextView smart_leakage_txt;

        TextView smart_total_txt;

        TextView smart_temperature_txt;

        LinearLayout power_open_layout;

        ImageView power_open_img;

        RecyclerView power_sub_reycler;

        public SmartPowerViewHolder(@NonNull View itemView) {
            super(itemView);
            smart_powerNo_txt = itemView.findViewById(R.id.smart_powerNo_txt);
            smart_power_switch = itemView.findViewById(R.id.smart_power_switch);
            smart_powerState_txt = itemView.findViewById(R.id.smart_powerState_txt);
            smart_onlineState_txt = itemView.findViewById(R.id.smart_onlineState_txt);
            smart_powerNum_txt = itemView.findViewById(R.id.smart_powerNum_txt);
            smart_voltage_txt = itemView.findViewById(R.id.smart_voltage_txt);
            smart_electric_txt = itemView.findViewById(R.id.smart_electric_txt);
            smart_power_txt = itemView.findViewById(R.id.smart_power_txt);
            smart_leakage_txt = itemView.findViewById(R.id.smart_leakage_txt);
            smart_total_txt = itemView.findViewById(R.id.smart_total_txt);
            smart_temperature_txt = itemView.findViewById(R.id.smart_temperature_txt);

            power_open_layout = itemView.findViewById(R.id.power_open_layout);
            power_open_img = itemView.findViewById(R.id.power_open_img);
            power_sub_reycler = itemView.findViewById(R.id.power_sub_reycler);
            power_sub_reycler.setLayoutManager(new LinearLayoutManager(mContext));
        }
    }

    @Override
    public int getItemCount() {
        return smartPowerInfoList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String id);
    }
}
