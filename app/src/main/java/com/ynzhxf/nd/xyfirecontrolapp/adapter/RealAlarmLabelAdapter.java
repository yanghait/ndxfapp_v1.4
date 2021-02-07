package com.ynzhxf.nd.xyfirecontrolapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.LabelNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.ui.CircularDraw;

import java.util.List;

/**
 * 设备标签结构列表数据适配器
 * <p>
 * Created by nd on 2018-07-23.
 */

public class RealAlarmLabelAdapter extends RecyclerView.Adapter<RealAlarmLabelAdapter.BaseLabelViewHolder> {

    //模拟量有偏差
    private static final int LABEL_ANALOGO = 1;


    //其他类型
    private static final int LABEL_OTHRE = 2;

    private List<LabelNodeBean> labelList;

    protected OnItemClickListener mOnItemClickListener;

    private Context mContext;


    public RealAlarmLabelAdapter(Context mContext, List<LabelNodeBean> labelList) {
        this.labelList = labelList;
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        LabelNodeBean query = labelList.get(position);
        if (query.getTagValueType() != null && query.getAlalogOffset() != null) {
            return LABEL_ANALOGO;
        }
        return LABEL_OTHRE;
    }


    public class BaseLabelViewHolder extends ViewHolder {
        public TextView txtName, txtAlarmValue, txtNowValue, txtAlarmMessage, txtAlarmTime, txtAlarmType, txtEventType;

        public BaseLabelViewHolder(Context mContext, View view) {
            super(mContext, view);
            txtName = view.findViewById(R.id.txt_name);
            txtAlarmValue = view.findViewById(R.id.txt_alarm_value);
            txtNowValue = view.findViewById(R.id.txt_now_value);
            txtAlarmMessage = view.findViewById(R.id.txt_alarm_message);
            txtAlarmTime = view.findViewById(R.id.txt_alarm_time);

            txtAlarmType = view.findViewById(R.id.txt_alarm_type);
            txtEventType = view.findViewById(R.id.txt_event_type);
        }
    }


    /**
     * 标签模拟量有偏差率
     */
    public class ViewHolderLabelAnalogo extends BaseLabelViewHolder {
        TextView txtStandar, txtOffset;
        CircularDraw cdDraw;

        public ViewHolderLabelAnalogo(Context mContext, View view) {
            super(mContext, view);
            txtOffset = view.findViewById(R.id.txt_offset);
            txtStandar = view.findViewById(R.id.txt_standar_value);
            cdDraw = view.findViewById(R.id.cd_offeset);
        }
    }


    @Override
    public BaseLabelViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        LayoutInflater layout = LayoutInflater.from(parent.getContext());
        View view = null;
        BaseLabelViewHolder holder = null;
        switch (viewType) {
            case LABEL_ANALOGO:
                view = layout.inflate(R.layout.item_project_real_alarm_analog, parent, false);
                holder = new ViewHolderLabelAnalogo(mContext, view);
                break;
            default:
                view = layout.inflate(R.layout.item_project_real_alarm_other, parent, false);
                holder = new BaseLabelViewHolder(mContext, view);
        }
        setListener(parent, holder, viewType);
        return holder;
    }

    protected void setListener(final ViewGroup parent, final BaseLabelViewHolder viewHolder, int viewType) {
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(v, viewHolder, position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
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

    public interface OnItemClickListener {
        void onItemClick(View view, BaseLabelViewHolder holder, int position);

        boolean onItemLongClick(View view, BaseLabelViewHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(BaseLabelViewHolder holder, int position) {
        LabelNodeBean queryObj = this.labelList.get(position);
        BaseLabelViewHolder queryBaseHolder = (BaseLabelViewHolder) holder;
        queryBaseHolder.txtName.setText(queryObj.getAreaName());
        queryBaseHolder.txtAlarmValue.setText(queryObj.getAlarmValue());
        queryBaseHolder.txtNowValue.setText(queryObj.getTranslateValue());
        queryBaseHolder.txtAlarmMessage.setText(queryObj.getAlarmMessage());
        queryBaseHolder.txtAlarmTime.setText(queryObj.getEventTimeStr());
        queryBaseHolder.txtAlarmType.setText(queryObj.getAlarmTypeStr());
        queryBaseHolder.txtEventType.setText(queryObj.getEventType().getName());
        if (holder instanceof BaseLabelViewHolder) {//开关量
            BaseLabelViewHolder queryHolder = queryBaseHolder;
        }
        if (holder instanceof ViewHolderLabelAnalogo) {//模拟量
            ViewHolderLabelAnalogo queryHolder = (ViewHolderLabelAnalogo) holder;
            queryHolder.txtStandar.setText(queryObj.getStandarLower() + queryObj.getUnit().getName() + "-" + queryObj.getStandardHight() + queryObj.getUnit().getName());
            queryHolder.txtOffset.setText(queryObj.getAlalogOffset());
            queryHolder.cdDraw.loadData(queryObj.getAlalogOffset());
            queryBaseHolder.txtAlarmValue.setTextColor(Color.parseColor("#3498DB"));
            return;
        }
    }

    @Override
    public int getItemCount() {
        return labelList.size();
    }

}
