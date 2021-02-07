package com.ynzhxf.nd.xyfirecontrolapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.PieChartLabelBean;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.OwnerMyWorkOrderActivity;

import java.util.List;


/**
 * author hbzhou
 * date 2019/6/26 11:07
 */
public class CompanyPieChartAdapter extends BaseAdapter {
    private Context mContext;
    private List<PieChartLabelBean> beanList;

    public CompanyPieChartAdapter(Context mContext, List<PieChartLabelBean> beanList) {
        this.mContext = mContext;
        this.beanList = beanList;
    }

    @Override
    public int getCount() {
        return beanList.size();
    }

    @Override
    public Object getItem(int i) {
        return beanList.get(i);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_pie_chart_com, null);
            viewHolder.color = view.findViewById(R.id.item_pie_color);
            viewHolder.content = view.findViewById(R.id.item_pie_content);
            viewHolder.mLayout = view.findViewById(R.id.pie_layout);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.color.setBackgroundColor(beanList.get(position).getColor());
        viewHolder.content.setText(beanList.get(position).getLabel());

        viewHolder.mLayout.setGravity(Gravity.START);
        viewHolder.mLayout.requestLayout();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int state = 0;
                switch (beanList.get(position).getEventTypeID()) {
                    case "10":
                        state = 1;
                        break;
                    case "20":
                        state = 2;
                        break;
                    case "30":
                        state = 3;
                        break;
                    case "40":
                        state = 4;
                        break;
                    case "50":
                        state = 5;
                        break;
                    case "60":
                        state = 6;
                        break;

                    case "80":
                        state = 8;
                        break;
                }
                goToCompanyMyOrder(state);
            }
        });
        return view;
    }

    private void goToCompanyMyOrder(int state) {
        Intent intent = new Intent(mContext, OwnerMyWorkOrderActivity.class);
        intent.putExtra("state", state);
        intent.putExtra("isCompany", true);
        mContext.startActivity(intent);
    }

    class ViewHolder {
        LinearLayout mLayout;
        TextView color;
        TextView content;
    }
}
