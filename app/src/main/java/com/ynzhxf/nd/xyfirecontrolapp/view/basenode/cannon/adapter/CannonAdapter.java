package com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.bean.WaterCannonInfo;

import java.util.ArrayList;
import java.util.List;

public class CannonAdapter extends RecyclerView.Adapter {

    private Context mContext;

    private List<WaterCannonInfo> waterCannonInfoList = new ArrayList<>();

    public CannonAdapter(Context context) {
        mContext = context;
    }

    public void update(List<WaterCannonInfo> list) {
        waterCannonInfoList = list;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.more_cannon_item, parent, false);
        WaterCannonViewHolder waterCannonViewHolder = new WaterCannonViewHolder(convertView);
        return waterCannonViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        WaterCannonViewHolder viewHolder = (WaterCannonViewHolder) holder;
        WaterCannonInfo info = waterCannonInfoList.get(position);
        viewHolder.more_cannon_txt.setText(info.getName());
    }

    @Override
    public int getItemCount() {
        return waterCannonInfoList.size();
    }

    public class WaterCannonViewHolder extends RecyclerView.ViewHolder {
        TextView more_cannon_txt;


        public WaterCannonViewHolder(@NonNull View itemView) {
            super(itemView);
            more_cannon_txt = itemView.findViewById(R.id.more_cannon_txt);
        }
    }
}
