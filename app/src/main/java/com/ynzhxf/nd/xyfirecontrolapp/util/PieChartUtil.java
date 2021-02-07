package com.ynzhxf.nd.xyfirecontrolapp.util;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.GridView;
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
import com.ynzhxf.nd.xyfirecontrolapp.adapter.CompanyPieChartAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.statistics.ChargeTradePieChartAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.charge.ChargePieChartsTradeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.PieChartLabelBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.MainOwnerMarkCountBean;
import com.ynzhxf.nd.xyfirecontrolapp.ui.CustomGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * author hbzhou
 * date 2019/6/26 10:35
 * 饼状图数据处理
 */
public class PieChartUtil {

    public static void initPieChart(final Context mContext, final int num, final CustomGridView gridView, final LinearLayout mPieLayout, final PieChart mPieChart, List<MainOwnerMarkCountBean> beanList) {

        //计算饼图总数
//        int num = 0;
//        int state1 = 0;
//        int state2 = 0;
//        for (int i = 0; i < beanList.size(); i++) {
//            switch (beanList.get(i).getWorkOrderState()) {
//                case 0:
//                    num = beanList.get(i).getCount();
//                    break;
//                case 70:
//                    state1 = beanList.get(i).getCount();
//                    break;
//                case 90:
//                    state2 = beanList.get(i).getCount();
//                    break;
//            }
//        }
//        num = num - (state1 + state2);

        //饼状图
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(20, 0, 20, 5);

        mPieChart.setDragDecelerationFrictionCoef(0.95f);
        //设置中间文件
        mPieChart.setCenterText(generateCenterSpannableText(num));

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

        for (int i = 0; i < beanList.size(); i++) {
            if (beanList.get(i).getWorkOrderState() != 0 && beanList.get(i).getWorkOrderState() != 70 && beanList.get(i).getWorkOrderState() != 90) {
                float per = beanList.get(i).getCount() * 1.0f / num * 100;
                int per1 = Math.round(per);
                switch (beanList.get(i).getWorkOrderState()) {
                    case 10:
                        entries.add(new PieEntry(per1, "待确认"));
                        break;
                    case 20:
                        entries.add(new PieEntry(per1, "待维修"));
                        break;
                    case 30:
                        entries.add(new PieEntry(per1, "维修中"));
                        break;
                    case 40:
                        entries.add(new PieEntry(per1, "已挂起"));
                        break;
                    case 50:
                        entries.add(new PieEntry(per1, "待审核"));
                        break;
                    case 60:
                        entries.add(new PieEntry(per1, "已返工"));
                        break;
                    case 80:
                        entries.add(new PieEntry(per1, "申请终止"));
                        break;
                }
            }
        }

        //设置数据
        setData(mContext, mPieChart, entries, beanList);

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

            bean.setLabel(entries.get(i).getLabel() + " " + beanList.get(i).getCount());

//            int state = (i + 1) * 10;
//
//            if (state == 70) {
//                bean.setEventTypeID("80");
//            } else {
            bean.setEventTypeID(String.valueOf(beanList.get(i).getWorkOrderState()));
//            }
            labelBeans.add(bean);
        }

        gridView.setAdapter(new CompanyPieChartAdapter(mContext, labelBeans));

        // l.setStackSpace(ScreenUtil.dp2px(mContext,300));
        //l.setYEntrySpace(ScreenUtil.dp2px(mContext,220));
//        l.setXEntrySpace(30f);

        // 输入标签样式
//        mPieChart.setEntryLabelColor(Color.WHITE);
//        mPieChart.setEntryLabelTextSize(12f);

        for (IDataSet<?> set : mPieChart.getData().getDataSets())
            set.setDrawValues(false);//set.isDrawValuesEnabled()
    }

    //设置中间文字
    private static SpannableString generateCenterSpannableText(int num) {
        //原文：MPAndroidChart\ndeveloped by Philipp Jahoda
        SpannableString s = new SpannableString(num + "\n" + "总数");
        s.setSpan(new RelativeSizeSpan(1.2f), 0, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(0.6f), s.length() - 2, s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.BOLD), 0, s.length() - 2, 0);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#a9a9a9")), s.length() - 2, s.length(), 0);
        return s;
    }

    //设置数据
    private static void setData(Context mContext, PieChart mPieChart, ArrayList<PieEntry> entries, List<MainOwnerMarkCountBean> beanList) {
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(5f);

        initColors(beanList);
        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : MATERIAL_COLORS)
            colors.add(c);
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

    private static void initColors(List<MainOwnerMarkCountBean> beanList) {
        for (int i = 0; i < beanList.size(); i++) {
            switch (beanList.get(i).getWorkOrderState()) {
                case 10:
                    MATERIAL_COLORS[i] = rgb("#FFC738");
                    break;
                case 20:
                    MATERIAL_COLORS[i] = rgb("#EF4561");
                    break;
                case 30:
                    MATERIAL_COLORS[i] = rgb("#30C78B");
                    break;
                case 40:
                    MATERIAL_COLORS[i] = rgb("#3D9DFE");
                    break;
                case 50:
                    MATERIAL_COLORS[i] = rgb("#24D5B4");
                    break;
                case 60:
                    MATERIAL_COLORS[i] = rgb("#FD7C65");
                    break;
                case 80:
                    MATERIAL_COLORS[i] = rgb("#6A7FF8");
                    break;
            }
        }
    }

    private static int[] MATERIAL_COLORS = {
            rgb("#fc595a"), rgb("#ffc655"), rgb("#37d6b6"), rgb("#62bffc"),
            rgb("#7292f9"), rgb("#c98ed0"), rgb("#2f81f7"), rgb("#ce898e"),
    };

    /**
     * Converts the given hex-color-string to rgb.
     *
     * @param hex
     * @return
     */
    private static int rgb(String hex) {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return Color.rgb(r, g, b);
    }

    public static void initTradePieChart(final Context mContext, final int num, final GridView gridView, final PieChart mPieChart, List<ChargePieChartsTradeBean> tradeBeanList, final boolean isNeedClick) {
        //饼状图
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(20, 0, 20, 5);

        mPieChart.setDragDecelerationFrictionCoef(0.95f);
        //设置中间文件
        mPieChart.setCenterText(generateCenterSpannableText(num));

        mPieChart.setCenterTextSize(20f);
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

        for (int i = 0; i < tradeBeanList.size(); i++) {
            float percent = tradeBeanList.get(i).getStatisticsCount() * 1.0f / num * 100f;
            entries.add(new PieEntry(Math.round(percent), tradeBeanList.get(i).getBusinessTypeName()));
        }

        //设置数据
        setTradeData(mContext, mPieChart, entries, tradeBeanList);

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


//        List<PieChartLabelBean> labelBeans = new ArrayList<>();
//        for (int i = 0; i < entries.size(); i++) {
//            PieChartLabelBean bean = new PieChartLabelBean();
//
//            bean.setColor(MATERIAL_COLORS[i]);
//
//            bean.setLabel(entries.get(i).getLabel() + " " + tradeBeanList.get(i).getTradeNum());
//
//            bean.setEventTypeID(String.valueOf(tradeBeanList.get(i).getTradeTypeId()));
//
//            labelBeans.add(bean);
//        }

        for (int i = 0; i < tradeBeanList.size(); i++) {
            ChargePieChartsTradeBean bean = tradeBeanList.get(i);
            // 假数据！！！！todo
            //bean.setStatisticsCount(bean.getStatisticsCount());
            bean.setColor(MATERIAL_COLORS_TRADE[i]);
            bean.setLabel(entries.get(i).getLabel() + " " + tradeBeanList.get(i).getStatisticsCount());
        }

        gridView.setAdapter(new ChargeTradePieChartAdapter(mContext, tradeBeanList, isNeedClick));

        // l.setStackSpace(ScreenUtil.dp2px(mContext,300));
        //l.setYEntrySpace(ScreenUtil.dp2px(mContext,220));
//        l.setXEntrySpace(30f);

        // 输入标签样式
//        mPieChart.setEntryLabelColor(Color.WHITE);
//        mPieChart.setEntryLabelTextSize(12f);

        for (IDataSet<?> set : mPieChart.getData().getDataSets())
            set.setDrawValues(set.isDrawValuesEnabled());
    }

    //设置数据
    private static void setTradeData(Context mContext, PieChart mPieChart, ArrayList<PieEntry> entries, List<ChargePieChartsTradeBean> beanList) {
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(5f);

        //initTradeColors(beanList);
        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : MATERIAL_COLORS_TRADE)
            colors.add(c);
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        dataSet.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);

        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        //数据与区块之间的用于指示的折线样式
//        dataSet.setValueLinePart1OffsetPercentage(0.2f);//折线中第一段起始位置相对于区块的偏移量, 数值越大, 折线距离区块越远
//        dataSet.setValueLinePart1Length(0.3f);//折线中第一段长度占比
//        dataSet.setValueLinePart2Length(0.2f);//折线中第二段长度最大占比
//        dataSet.setValueLineWidth(0.5f);//折线的粗细
//        dataSet.setValueLineColor(mContext.getResources().getColor(R.color.gray));//折线颜色
        dataSet.setDrawValues(false);


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

    public static int[] MATERIAL_COLORS_TRADE = {
            rgb("#FFC738"), rgb("#EF4561"), rgb("#4CC46F"), rgb("#3D9DFE"),
            rgb("#24D5B4"), rgb("#FD7C65"), rgb("#6A7FF8"), rgb("#BF80F8"),
            rgb("#63BFFC"), rgb("#A7EF57"), rgb("#FDEE75")
    };
}
