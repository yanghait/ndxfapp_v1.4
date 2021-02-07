package com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;

import java.util.List;

/**
 * author hbzhou
 * date 2019/2/26 09:49
 */
public abstract class SpecialCommonAdapter<T> extends CommonAdapter<T> {

    public SpecialCommonAdapter(Context context, int layoutId, List<T> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void setListener(ViewGroup parent, final ViewHolder viewHolder, int viewType) {

        if (!isEnabled(viewType)) return;
        View specialView = viewHolder.getConvertView().findViewById(R.id.special_view);
        if (specialView == null) {
            return;
        }
        specialView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(v, viewHolder, position);
                }
            }
        });

        specialView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    return mOnItemClickListener.onItemLongClick(v, viewHolder, position);
                }
                return false;
            }
        });
    }
}