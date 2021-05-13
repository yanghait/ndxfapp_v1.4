package com.ynzhxf.nd.xyfirecontrolapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.bean.WaterCannonInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaterCannonAdapter extends RecyclerView.Adapter {

    private Context mContext;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private List<WaterCannonInfo> waterCannonInfoList = new ArrayList<>();

    public List<WaterCannonInfo> getWaterCannonInfoList() {
        return waterCannonInfoList;
    }

    private Map<Integer, Boolean> selectMap = new HashMap<>();

    public Map<Integer, Boolean> getSelectMap() {
        return selectMap;
    }

    public WaterCannonAdapter(Context context) {
        this.mContext = context;

    }

    public void updateList(List<WaterCannonInfo> list) {
        waterCannonInfoList = list;
        for (int i = 0; i < waterCannonInfoList.size(); i++) {
            if (!selectMap.containsKey(i))
                selectMap.put(i, false);
        }
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_water_cannon, parent, false);
        WaterCannonViewHolder waterCannonViewHolder = new WaterCannonViewHolder(view);
        return waterCannonViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        WaterCannonViewHolder viewHolder = (WaterCannonViewHolder) holder;
        WaterCannonInfo waterCannonInfo = waterCannonInfoList.get(position);
        viewHolder.water_cannonNo_txt.setText(waterCannonInfo.getName());

        if (waterCannonInfo.getSkyLightStatus() == 0) {
            viewHolder.cannon_lidState_txt.setText("关闭");
            viewHolder.cannon_lidState_txt.setTextColor(Color.parseColor("#ff6d6d"));
        } else {
            viewHolder.cannon_lidState_txt.setText("开启");
            viewHolder.cannon_lidState_txt.setTextColor(Color.parseColor("#70E8B0"));
        }
        if (waterCannonInfo.getSkyLightStatus() == 0) {
            viewHolder.cannon_valveState_txt.setText("关闭");
            viewHolder.cannon_valveState_txt.setTextColor(Color.parseColor("#ff6d6d"));
        } else {
            viewHolder.cannon_valveState_txt.setText("开启");
            viewHolder.cannon_valveState_txt.setTextColor(Color.parseColor("#70E8B0"));
        }


        viewHolder.cannon_planeNum_txt.setText(waterCannonInfo.getFlatAngle());
        viewHolder.cannon_elevationNum_txt.setText(waterCannonInfo.getElevationAngle());
        viewHolder.water_cannonNo_check_layout.setOnClickListener(v -> {
            if (selectMap.get(position)) {
                selectMap.put(position, false);
            } else {
                selectMap.put(position, true);
            }
            this.notifyDataSetChanged();
        });
        viewHolder.water_cannonNo_checkbox.setOnClickListener(v -> {
            if (selectMap.get(position)) {
                selectMap.put(position, false);
            } else {
                selectMap.put(position, true);
            }
            this.notifyDataSetChanged();
        });
        if (selectMap.get(position)) {
            viewHolder.water_cannonNo_checkbox.setChecked(true);
        } else {
            viewHolder.water_cannonNo_checkbox.setChecked(false);
        }
        viewHolder.water_cannon_layout.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(waterCannonInfo);
            }
        });
    }

    public class WaterCannonViewHolder extends AreaAdapter.ViewHolder {
        LinearLayout water_cannon_layout;
        TextView water_cannonNo_txt;
        LinearLayout water_cannonNo_check_layout;
        CheckBox water_cannonNo_checkbox;
        TextView cannon_lidState_txt;
        TextView cannon_valveState_txt;
        TextView cannon_planeNum_txt;
        TextView cannon_elevationNum_txt;


        public WaterCannonViewHolder(View view) {
            super(view);
            water_cannon_layout = view.findViewById(R.id.water_cannon_layout);
            water_cannonNo_txt = view.findViewById(R.id.water_cannonNo_txt);
            water_cannonNo_check_layout = view.findViewById(R.id.water_cannonNo_check_layout);
            water_cannonNo_checkbox = view.findViewById(R.id.water_cannonNo_checkbox);
            cannon_lidState_txt = view.findViewById(R.id.cannon_lidState_txt);
            cannon_valveState_txt = view.findViewById(R.id.cannon_valveState_txt);
            cannon_planeNum_txt = view.findViewById(R.id.cannon_planeNum_txt);
            cannon_elevationNum_txt = view.findViewById(R.id.cannon_elevationNum_txt);
        }
    }

    @Override
    public int getItemCount() {
        return waterCannonInfoList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(WaterCannonInfo id);
    }
}
