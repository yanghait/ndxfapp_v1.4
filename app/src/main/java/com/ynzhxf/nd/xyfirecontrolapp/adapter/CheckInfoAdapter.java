package com.ynzhxf.nd.xyfirecontrolapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.CheckPointBean;

import java.util.ArrayList;
import java.util.List;

public class CheckInfoAdapter extends RecyclerView.Adapter {

    private Context mcontext;

    private List<CheckPointBean> checkPointBeanList = new ArrayList<>();

    private OnStateBtnClickListener onStateBtnClickListener;

    public void setOnStateBtnClickListener(OnStateBtnClickListener onStateBtnClickListener) {
        this.onStateBtnClickListener = onStateBtnClickListener;
    }

    public CheckInfoAdapter(Context context) {
        this.mcontext = context;
    }

    public void update(List<CheckPointBean> checkPointBeans) {
        checkPointBeanList = checkPointBeans;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_point_item, parent, false);
        CheckPointViewHolder checkPointViewHolder = new CheckPointViewHolder(view);
        return checkPointViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CheckPointViewHolder viewHolder = (CheckPointViewHolder) holder;
        CheckPointBean checkPointBean = checkPointBeanList.get(position);
        viewHolder.check_point_name_txt.setText(checkPointBean.getName());
        if (checkPointBean.getQrCode() != null && !checkPointBean.getQrCode().equals("")) {
            viewHolder.check_point_state_txt.setText("已注册");
            viewHolder.check_point_state_txt.setTextColor(mcontext.getResources().getColor(R.color.fire_close));
            viewHolder.check_point_state_btn.setText("修改");
        } else {
            viewHolder.check_point_state_txt.setText("未注册");
            viewHolder.check_point_state_txt.setTextColor(mcontext.getResources().getColor(R.color.fire_fire));
            viewHolder.check_point_state_btn.setText("注册");
        }
        viewHolder.check_point_state_btn.setOnClickListener(v -> {
            if (onStateBtnClickListener != null) {
                onStateBtnClickListener.onStateBtnClick(position, checkPointBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return checkPointBeanList.size();
    }

    public void updateItem(int index, CheckPointBean checkPointBean) {
        checkPointBeanList.set(index, checkPointBean);
        this.notifyDataSetChanged();
    }


    public class CheckPointViewHolder extends RecyclerView.ViewHolder {

        TextView check_point_name_txt;

        TextView check_point_state_txt;

        Button check_point_state_btn;

        public CheckPointViewHolder(@NonNull View itemView) {
            super(itemView);
            check_point_name_txt = itemView.findViewById(R.id.check_point_name_txt);
            check_point_state_txt = itemView.findViewById(R.id.check_point_state_txt);
            check_point_state_btn = itemView.findViewById(R.id.check_point_state_btn);
        }
    }

    public interface OnStateBtnClickListener {
        void onStateBtnClick(int position, CheckPointBean checkPointBean);
    }
}
