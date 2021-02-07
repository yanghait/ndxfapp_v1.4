package com.ynzhxf.nd.xyfirecontrolapp.util;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.formatter.PercentFormatter;


/**
 * author hbzhou
 * date 2019/10/30 13:10
 * 自定义区域饼图折线显示内容
 */
public class AreaPercentFormatter extends PercentFormatter {
    private int sum;
    public AreaPercentFormatter(PieChart pieChart, int sum) {
        super(pieChart);
        this.sum = sum;
    }

    @Override
    public String getFormattedValue(float value) {
        return "(" + (Math.round(value * 0.01 * sum)) + "," + mFormat.format(value) + " %)";
    }
}
