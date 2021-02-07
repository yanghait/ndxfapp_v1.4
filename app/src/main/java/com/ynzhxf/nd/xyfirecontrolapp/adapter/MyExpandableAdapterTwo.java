package com.ynzhxf.nd.xyfirecontrolapp.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireAlarmStatisticsDetailBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireStatisticsDetailEventBean;

import java.util.List;

/**
 * author hbzhou
 * date 2019/1/15 18:22
 * 项目统计项目详情事件记录列表适配器
 */
public class MyExpandableAdapterTwo extends MyExpandableAdapterOne {

    private List<FireStatisticsDetailEventBean> beanList;

    private String num;

    public MyExpandableAdapterTwo(Context mContext, List<String> groupArray, List<List<FireAlarmStatisticsDetailBean>> childArray, List<FireStatisticsDetailEventBean> beanList, String num) {
        super(mContext, groupArray, childArray, null);
        this.beanList = beanList;
        this.num = num;
    }

    @Override
    public int getChildrenCount(int i) {
        return beanList.get(i).getEventCount().size();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        ChildHolder holder;
        if (view == null) {
            holder = new ChildHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_statistics_child_two, null);
            holder.childName = view.findViewById(R.id.child_two_title);
            holder.childNum = view.findViewById(R.id.child_two_num);
            holder.childCount = view.findViewById(R.id.child_two_count);
            holder.exLine = view.findViewById(R.id.expand_line);
            holder.exSpace = view.findViewById(R.id.expand_two_space);
            view.setTag(holder);
        } else {
            holder = (ChildHolder) view.getTag();
        }
        LinearLayout titleLayout = view.findViewById(R.id.layout_sta);
        TextView someSum = view.findViewById(R.id.layout_sta_sum);
        if (childPosition == 0 && !StringUtils.isEmpty(num) && "11".equals(num)) {
            titleLayout.setVisibility(View.VISIBLE);
            titleLayout.setClickable(false);
            View line = view.findViewById(R.id.layout_sta_line);
            //line.setVisibility(View.INVISIBLE);
            // 计算火灾报警记录每一列表总数
            try {
                int sum = 0;
                for (int i = 0; i < beanList.get(groupPosition).getEventCount().size(); i++) {
                    sum = sum + Integer.parseInt(beanList.get(groupPosition).getEventCount().get(i).get(2));
                }
                someSum.setText(String.valueOf(sum));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }


        } else {
            titleLayout.setVisibility(View.GONE);
        }

        if (!StringUtils.isEmpty(num) && "11".equals(num)) {

            if (beanList.get(groupPosition).getEventCount().size() > 0 && childPosition == beanList.get(groupPosition).getEventCount().size() - 1) {

                holder.exLine.setVisibility(View.GONE);

                holder.exSpace.setVisibility(View.VISIBLE);
            } else {
                holder.exLine.setVisibility(View.VISIBLE);

                holder.exSpace.setVisibility(View.GONE);
            }
        }


        String childName = beanList.get(groupPosition).getEventCount().get(childPosition).get(1);

        if (!StringUtils.isEmpty(childName) && childName.contains("-")) {
            String[] splitName = childName.split("-");
            if (splitName.length > 3) {
                String okName = "";
                for (int i = 0; i < splitName.length; i++) {
                    if (i > 2) {
                        okName = okName.concat(splitName[i]).concat("-");
                    }
                }
                holder.childName.setText(okName.substring(0, okName.length() - 1));
            }
        } else {
            holder.childName.setText(beanList.get(groupPosition).getEventCount().get(childPosition).get(1));
        }

        holder.childNum.setText(String.valueOf(childPosition + 1));

        holder.childCount.setText(beanList.get(groupPosition).getEventCount().get(childPosition).get(2));

        if (childPosition <= 2) {
            switch (childPosition) {
                case 0:
                    holder.childNum.setBackground(mContext.getResources().getDrawable(R.drawable.item_child_two_num));
                    holder.childCount.setTextColor(mContext.getResources().getColor(R.color.pie_event_color));
                    break;
                case 1:
                    holder.childNum.setBackground(mContext.getResources().getDrawable(R.drawable.item_child_shape_two));
                    holder.childCount.setTextColor(mContext.getResources().getColor(R.color.Expandable_item_two));
                    break;
                case 2:
                    holder.childNum.setBackground(mContext.getResources().getDrawable(R.drawable.item_child_shape_three));
                    holder.childCount.setTextColor(mContext.getResources().getColor(R.color.Expandable_item_three));
                    break;
            }
        } else {
            holder.childNum.setBackground(mContext.getResources().getDrawable(R.drawable.item_child_shape_four));
            holder.childCount.setTextColor(mContext.getResources().getColor(R.color.Expandable_item_four));
        }

        return view;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        GroupHolder holder;

        if (!StringUtils.isEmpty(num) && "11".equals(num)) {
            if (view == null) {
                holder = new GroupHolder();
                view = LayoutInflater.from(mContext).inflate(R.layout.item_statistics_group_two, null);
                holder.groupName = view.findViewById(R.id.item_group_two_title);
                holder.arrow = view.findViewById(R.id.right_arrow);
                holder.line = view.findViewById(R.id.line);
                holder.noData = view.findViewById(R.id.no_data_text);
                view.setTag(holder);
            } else {
                holder = (GroupHolder) view.getTag();
            }

            //判断是否已经打开列表
            if (isExpanded) {
                holder.line.setVisibility(View.GONE);

                if (beanList.get(groupPosition).getEventCount().size() == 0) {
                    holder.line.setVisibility(View.VISIBLE);
                    holder.noData.setVisibility(View.VISIBLE);
                } else {
                    holder.noData.setVisibility(View.GONE);
                }

                holder.arrow.setImageDrawable(mContext.getResources().getDrawable(R.drawable.down_new));
            } else {
                holder.line.setVisibility(View.VISIBLE);
                holder.noData.setVisibility(View.GONE);
                holder.arrow.setImageDrawable(mContext.getResources().getDrawable(R.drawable.front_new));
            }
            holder.groupName.setText(groupArray.get(groupPosition));

        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_statistics_group_two, null);

            LinearLayout alarmView = view.findViewById(R.id.alarm_view);
            View line = view.findViewById(R.id.line);

            LinearLayout layoutSta = view.findViewById(R.id.layout_sta);

            alarmView.setVisibility(View.GONE);
            line.setVisibility(View.GONE);

            TextView title = view.findViewById(R.id.alarm_title);

            title.setText("事件记录统计排名前10");

            layoutSta.setVisibility(View.VISIBLE);

            TextView someSum = view.findViewById(R.id.layout_sta_sum);
            // 计算火灾报警记录每一列表总数
            try {
                int sum = 0;
                for (int i = 0; i < beanList.get(groupPosition).getEventCount().size(); i++) {
                    sum = sum + Integer.parseInt(beanList.get(groupPosition).getEventCount().get(i).get(2));
                }
                someSum.setText(String.valueOf(sum));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
