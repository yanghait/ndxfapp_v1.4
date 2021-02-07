package com.ynzhxf.nd.xyfirecontrolapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.PieChartLabelBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireAlarmStatisticsDetailBean;
import com.ynzhxf.nd.xyfirecontrolapp.ui.CustomGridView;

import java.util.ArrayList;
import java.util.List;


/**
 * author hbzhou
 * date 2019/1/15 13:14
 */
public class MyExpandableAdapterOne extends BaseExpandableListAdapter {

    protected List<String> groupArray;
    protected List<List<FireAlarmStatisticsDetailBean>> childArray;
    protected Context mContext;

    private String projectSysID;

    public MyExpandableAdapterOne(Context mContext, List<String> groupArray, List<List<FireAlarmStatisticsDetailBean>> childArray, String projectSysID) {
        this.mContext = mContext;
        this.groupArray = groupArray;
        this.childArray = childArray;
        this.projectSysID = projectSysID;
    }

    @Override
    public int getGroupCount() {
        return groupArray.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return groupArray.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return childArray.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        GroupHolder holder = null;
        if (view == null) {
            holder = new GroupHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_expandlist_one_group, null);
            holder.groupName = view.findViewById(R.id.item_group_time);
            holder.arrow = view.findViewById(R.id.right_arrow);
            view.setTag(holder);
        } else {
            holder = (GroupHolder) view.getTag();
        }
        View mLine = view.findViewById(R.id.expandable_one_line);
        //判断是否已经打开列表
        if (isExpanded) {
            mLine.setVisibility(View.INVISIBLE);
            holder.arrow.setImageDrawable(mContext.getResources().getDrawable(R.drawable.down_new));
        } else {
            mLine.setVisibility(View.VISIBLE);
            holder.arrow.setImageDrawable(mContext.getResources().getDrawable(R.drawable.front_new));
        }
        holder.groupName.setText(groupArray.get(groupPosition));

        //LogUtils.showLoge("点击了父列表1212---", String.valueOf(groupPosition));

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        ChildHolder holder = null;
        if (view == null) {
            holder = new ChildHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_expandlist_one_child, null);
            holder.pieChart = view.findViewById(R.id.mPieChart);
            holder.mPieLayout = view.findViewById(R.id.popupWindow_layout);
            holder.gridView = view.findViewById(R.id.mPieChart_grid_view);
            view.setTag(holder);
        } else {
            holder = (ChildHolder) view.getTag();
        }

        initPieChart(groupArray.get(i), holder.gridView, holder.mPieLayout, holder.pieChart, childArray.get(i));

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    class GroupHolder {
        public TextView groupName;
        public ImageView arrow;
        public View line;
        public TextView noData;
    }

    class ChildHolder {
        public TextView childName;
        public TextView childNum;
        public TextView childCount;
        public PieChart pieChart;

        public LinearLayout mPieLayout;

        public CustomGridView gridView;

        public View exLine,exSpace;
    }

    private void initPieChart(final String timeStr, final CustomGridView gridView, final LinearLayout mPieLayout, final PieChart mPieChart, List<FireAlarmStatisticsDetailBean> beanList) {
        //饼状图
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(20, 0, 20, 5);

        mPieChart.setDragDecelerationFrictionCoef(0.95f);
        //设置中间文件
        mPieChart.setCenterText(generateCenterSpannableText(beanList));

        mPieChart.setCenterTextSize(20f);

        mPieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                PieEntry entry = (PieEntry) e;
//                LogUtils.showLoge("选中某一块1212---", String.valueOf(entry.getData() + "~~~" + entry.getLabel() + "~~~"
//                        + entry.getValue() + "~~~" + e.getX() + "~~~" + e.getY()));

                TextView mContent = mPieLayout.findViewById(R.id.piechart_popup);

                String label = entry.getLabel();
                label = label.replace(" ", "");
                label = label.replace(":", ": ");

                mContent.setText(String.valueOf(label + " (" + e.getY() + "%" + ")"));

                mPieLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected() {

                mPieLayout.setVisibility(View.GONE);
            }
        });
        //mPieChart.setCenterTextTypeface(mContext.g);

//        mPieChart.setDrawHoleEnabled(true);
//        mPieChart.setHoleColor(Color.WHITE);

//        mPieChart.setTransparentCircleColor(Color.WHITE);
//        mPieChart.setTransparentCircleAlpha(110);

        mPieChart.setHoleRadius(58f);
        mPieChart.setTransparentCircleRadius(61f);

        mPieChart.setDrawCenterText(true);

        mPieChart.setRotationAngle(0);
        //mPieChart.setCenterTextTypeface(Typeface.DEFAULT);

        // 触摸旋转
        //mPieChart.setRotationEnabled(true);
        //mPieChart.setHighlightPerTapEnabled(true);

        //变化监听
        //mPieChart.setOnChartValueSelectedListener(mContext);


        //模拟数据
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
//        entries.add(new PieEntry(40, "优秀"));
//        entries.add(new PieEntry(20, "满分"));
//        entries.add(new PieEntry(30, "及格"));
//        entries.add(new PieEntry(10, "不及格"));

        int num = 0;
        for (int i = 0; i < beanList.size(); i++) {
            try {
                num = num + Integer.parseInt(beanList.get(i).getItem2());

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }


        for (int i = 0; i < beanList.size(); i++) {
            String title = "";
            switch (beanList.get(i).getItem3()) {
                case "0":
                    title = "无事件";
                    break;
                case "1":
                    title = "控制事件";
                    break;
                case "2":
                    title = "运行事件";
                    break;
                case "3":
                    title = "报警事件";
                    break;
                case "4":
                    title = "故障事件";
                    break;
                case "5":
                    title = "恢复事件";
                    break;
                case "6":
                    title = "通讯事件";
                    break;
                case "10":
                    title = "其他";
                    break;
            }

            try {
                int count = Integer.parseInt(beanList.get(i).getItem2());

                float per = count * 1.0f / num * 100;

                int per1 = Math.round(per);

                String label = title + " " + count;

                //LogUtils.showLoge("文本长度1212--", String.valueOf(label.length()));


//                if (!StringUtils.isEmpty(label)) {
//                    if (label.length() < 10) {
//                        switch (10 - label.length()) {
//                            case 1:
//                                entries.add(new PieEntry(per1, title + ":" + " " + count));
//                                break;
//                            case 2:
//                                entries.add(new PieEntry(per1, title + ":" + "  " + count));
//                                break;
//                            case 3:
//                                entries.add(new PieEntry(per1, title + ":" + "    " + count));
//                                break;
//                            case 4:
//                                entries.add(new PieEntry(per1, title + ":" + "      " + count));
//                                break;
//                        }
//                    } else {
                entries.add(new PieEntry(per1, title + " " + " " + count));
//                    }
//                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        //设置数据
        setData(mPieChart, entries, beanList);

        mPieChart.animateY(1400, Easing.EaseInOutQuad);


        Legend l = mPieChart.getLegend();
        l.setEnabled(false);
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);//top
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);//right
        l.setWordWrapEnabled(true);
        l.setTextSize(12f);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);//vertical
//        l.setDrawInside(false);
        l.setXEntrySpace(7f);
//        l.setYEntrySpace(7f);
        l.setYOffset(10f);
        l.setMaxSizePercent(1f);

        l.setXOffset(11f);

        List<PieChartLabelBean> labelBeans = new ArrayList<>();

        for (int i = 0; i < entries.size(); i++) {
            PieChartLabelBean bean = new PieChartLabelBean();

            bean.setColor(MATERIAL_COLORS[i]);

            bean.setLabel(entries.get(i).getLabel());

            bean.setEventTypeID(beanList.get(i).getItem3());

//            if(i==0){
//                bean.setLabel(entries.get(i).getLabel()+"1234");
//            }

            labelBeans.add(bean);
        }

        gridView.setAdapter(new PieChartGridAdapter(mContext, labelBeans, projectSysID, timeStr));

        // l.setStackSpace(ScreenUtil.dp2px(mContext,300));
        //l.setYEntrySpace(ScreenUtil.dp2px(mContext,220));
//        l.setXEntrySpace(30f);

        // 输入标签样式
//        mPieChart.setEntryLabelColor(Color.WHITE);
//        mPieChart.setEntryLabelTextSize(12f);

        for (IDataSet<?> set : mPieChart.getData().getDataSets())
            set.setDrawValues(set.isDrawValuesEnabled());
    }

    //设置中间文字
    private SpannableString generateCenterSpannableText(List<FireAlarmStatisticsDetailBean> beanList) {
        //原文：MPAndroidChart\ndeveloped by Philipp Jahoda

        int num = 0;
        for (int i = 0; i < beanList.size(); i++) {
            try {
                num = num + Integer.parseInt(beanList.get(i).getItem2());

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
//        SpannableString s = new SpannableString("总数 " + num + "");
//        s.setSpan(new RelativeSizeSpan(1.2f), 0, s.length(), 0);
//        s.setSpan(new RelativeSizeSpan(0.6f), 0, 2, 0);
//        s.setSpan(new StyleSpan(Typeface.BOLD), 0, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 2, 0);


        //s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        // s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);

        // s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);


        SpannableString s = new SpannableString(num + "\n" + "总数");
        s.setSpan(new RelativeSizeSpan(1.2f), 0, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(0.6f), s.length()-2, s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.BOLD), 0, s.length()-2, 0);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#a9a9a9")), s.length()-2, s.length(), 0);
        return s;
    }

    //设置数据
    private void setData(PieChart mPieChart, ArrayList<PieEntry> entries, List<FireAlarmStatisticsDetailBean> beanList) {
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(5f);

        initColors(beanList);
        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : MATERIAL_COLORS)
            colors.add(c);
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        dataSet.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);

        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        //数据与区块之间的用于指示的折线样式
        dataSet.setValueLinePart1OffsetPercentage(0.2f);//折线中第一段起始位置相对于区块的偏移量, 数值越大, 折线距离区块越远
        dataSet.setValueLinePart1Length(0.3f);//折线中第一段长度占比
        dataSet.setValueLinePart2Length(0.2f);//折线中第二段长度最大占比
        dataSet.setValueLineWidth(0.5f);//折线的粗细
        dataSet.setValueLineColor(mContext.getResources().getColor(R.color.gray));//折线颜色


        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(mPieChart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);
        mPieChart.setData(data);

        // 设置不显示圈边上文字
        mPieChart.setDrawEntryLabels(false);

        mPieChart.highlightValues(null);
        //刷新
        mPieChart.invalidate();
    }

    private void initColors(List<FireAlarmStatisticsDetailBean> beanList) {
        for (int i = 0; i < beanList.size(); i++) {
            switch (beanList.get(i).getItem3()) {
                case "0":
                    MATERIAL_COLORS[i] = rgb("#2f87f7");
                    break;
                case "1":
                    MATERIAL_COLORS[i] = rgb("#c98ed0");
                    break;
                case "2":
                    MATERIAL_COLORS[i] = rgb("#62bffc");
                    break;
                case "3":
                    MATERIAL_COLORS[i] = rgb("#fc595a");
                    break;
                case "4":
                    MATERIAL_COLORS[i] = rgb("#ffc655");
                    break;
                case "5":
                    MATERIAL_COLORS[i] = rgb("#37d6b6");
                    break;
                case "6":
                    MATERIAL_COLORS[i] = rgb("#7292f9");
                    break;
                case "10":
                    MATERIAL_COLORS[i] = rgb("#ce898e");
                    break;
            }
        }
    }

    private int[] MATERIAL_COLORS = {
            rgb("#fc595a"), rgb("#ffc655"), rgb("#37d6b6"), rgb("#62bffc"),
            rgb("#7292f9"), rgb("#c98ed0"), rgb("#2f81f7"), rgb("#ce898e"),
    };

    /**
     * Converts the given hex-color-string to rgb.
     *
     * @param hex
     * @return
     */
    private int rgb(String hex) {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return Color.rgb(r, g, b);
    }
}
