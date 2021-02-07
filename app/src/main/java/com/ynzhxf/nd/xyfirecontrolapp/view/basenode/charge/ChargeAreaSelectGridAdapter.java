package com.ynzhxf.nd.xyfirecontrolapp.view.basenode.charge;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.ChargeIndexAreaDataBean;
import com.ynzhxf.nd.xyfirecontrolapp.ui.AreaSelectedPopupWindow;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


/**
 * author hbzhou
 * date 2019/5/30 17:29
 * 处理recycler view 地区列表
 */
public class ChargeAreaSelectGridAdapter extends BaseAdapter {

    private List<ChargeIndexAreaDataBean.ChildrenNodesBeanX.ChildrenNodesBean> beanList;
    private Context context;
    private int position;

    public ChargeAreaSelectGridAdapter(Context context, int position, List<ChargeIndexAreaDataBean.ChildrenNodesBeanX.ChildrenNodesBean> beanList) {
        this.context = context;
        this.position = position;
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

    class ViewHolder {
        TextView mAreaName;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_area_select_popup_one, null);
            viewHolder.mAreaName = view.findViewById(R.id.area_item_title_one);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if ((i + 1) % 3 == 1) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(viewHolder.mAreaName.getLayoutParams());
            layoutParams.gravity = Gravity.START;
            viewHolder.mAreaName.setLayoutParams(layoutParams);
            viewHolder.mAreaName.requestLayout();
        } else if ((i + 1) % 3 == 2) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(viewHolder.mAreaName.getLayoutParams());
            layoutParams.gravity = Gravity.CENTER;
            viewHolder.mAreaName.setLayoutParams(layoutParams);
            viewHolder.mAreaName.requestLayout();
        } else if ((i + 1) % 3 == 0) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(viewHolder.mAreaName.getLayoutParams());
            layoutParams.gravity = Gravity.END;
            viewHolder.mAreaName.setLayoutParams(layoutParams);
            viewHolder.mAreaName.requestLayout();
        }
        viewHolder.mAreaName.setText(beanList.get(i).getName());
        viewHolder.mAreaName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChargeIndexAreaDataBean.ChildrenNodesBeanX childrenNodes = new ChargeIndexAreaDataBean.ChildrenNodesBeanX();
                childrenNodes.setID(beanList.get(i).getID());
                childrenNodes.setName(beanList.get(i).getName());
                EventBus.getDefault().post(childrenNodes);
                AreaSelectedPopupWindow.selectedPosition[0] = position;
                AreaSelectedPopupWindow.selectedPosition[1] = i + 1;
                notifyDataSetChanged();
            }
        });
        if (AreaSelectedPopupWindow.selectedPosition[0] == position && AreaSelectedPopupWindow.selectedPosition[1] == i + 1) {
            viewHolder.mAreaName.setTextColor(context.getResources().getColor(R.color.device_diagnose_orange));
        } else {
            viewHolder.mAreaName.setTextColor(context.getResources().getColor(R.color.global_gray_text_color));
        }
        return view;
    }
}
