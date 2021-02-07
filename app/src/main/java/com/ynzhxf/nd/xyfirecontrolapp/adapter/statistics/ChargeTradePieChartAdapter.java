package com.ynzhxf.nd.xyfirecontrolapp.adapter.statistics;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.charge.ChargePieChartsTradeBean;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.charge.ChargeChartsProjectActivity;

import java.util.List;


/**
 * author hbzhou
 * date 2019/10/30 10:11
 */
public class ChargeTradePieChartAdapter extends BaseAdapter {

    private Context context;
    private List<ChargePieChartsTradeBean> chartsTradeBeans;
    private boolean isNeedClick;

    public ChargeTradePieChartAdapter(Context context, List<ChargePieChartsTradeBean> chartsTradeBeans, boolean isNeedClick) {
        this.context = context;
        this.chartsTradeBeans = chartsTradeBeans;
        this.isNeedClick = isNeedClick;
    }

    @Override
    public int getCount() {
        return chartsTradeBeans.size();
    }

    @Override
    public Object getItem(int i) {
        return chartsTradeBeans.get(i);
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
            view = LayoutInflater.from(context).inflate(R.layout.item_pie_chart_com, null);
            viewHolder.color = view.findViewById(R.id.item_pie_color);
            viewHolder.content = view.findViewById(R.id.item_pie_content);
            viewHolder.mLayout = view.findViewById(R.id.pie_layout);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.color.setBackgroundColor(chartsTradeBeans.get(position).getColor());
        viewHolder.content.setText(chartsTradeBeans.get(position).getLabel());

        viewHolder.mLayout.setGravity(Gravity.START);
        viewHolder.mLayout.requestLayout();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNeedClick) {
                    goToChargeChartsHistoryAlarmOrder(chartsTradeBeans.get(position).getBusinessTypeId(), chartsTradeBeans.get(position).getBusinessTypeName());
                } else {
                    ToastUtils.showLong("无详情数据!");
                }
            }
        });
        return view;
    }

    private void goToChargeChartsHistoryAlarmOrder(String tradeTypeId, String title) {
        Intent intent = new Intent(context, ChargeChartsProjectActivity.class);
        intent.putExtra("typeId", tradeTypeId);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    class ViewHolder {
        LinearLayout mLayout;
        TextView color;
        TextView content;
    }
}
