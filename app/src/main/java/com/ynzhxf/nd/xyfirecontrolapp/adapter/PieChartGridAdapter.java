package com.ynzhxf.nd.xyfirecontrolapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.PieChartLabelBean;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.ProjectStatisticsLegendInfoActivity;

import java.util.List;


/**
 * author hbzhou
 * date 2019/2/25 14:50
 */
public class PieChartGridAdapter extends BaseAdapter {

    private Context mContext;

    private List<PieChartLabelBean> labels;

    private String projectSysID;

    private String timeStr;

    public PieChartGridAdapter(Context mContext, List<PieChartLabelBean> labels, String projectSysID, String timeStr) {
        this.mContext = mContext;
        this.labels = labels;
        this.projectSysID = projectSysID;
        this.timeStr = timeStr;
    }

    @Override
    public int getCount() {
        return labels.size();
    }

    @Override
    public Object getItem(int i) {
        return labels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_pie_chart_grid, null);
            viewHolder.color = view.findViewById(R.id.item_pie_color);
            viewHolder.content = view.findViewById(R.id.item_pie_content);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.color.setBackgroundColor(labels.get(position).getColor());
        viewHolder.content.setText(labels.get(position).getLabel());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProjectStatisticsLegendInfoActivity.class);

                intent.putExtra("ID", projectSysID);

                intent.putExtra("Name", "");

                intent.putExtra("EventId", labels.get(position).getEventTypeID());

                intent.putExtra("timeStr", timeStr);

                mContext.startActivity(intent);
            }
        });
        return view;
    }

    class ViewHolder {
        TextView color;

        TextView content;
    }
}
