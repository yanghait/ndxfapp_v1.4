package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.charge;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.charge.ChargeExtraContentBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.charge.ChargePieChartsAreaBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.charge.ChargePieChartsDataBean;
import com.ynzhxf.nd.xyfirecontrolapp.network.RetrofitUtils;
import com.ynzhxf.nd.xyfirecontrolapp.util.AreaPercentFormatter;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.PieChartUtil;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * author hbzhou
 * date 2019/10/29 17:29
 * 主管部门实时报警饼状统计图列表
 */
public class ChargeCurrentAlarmActivity extends BaseActivity {

    private PieChart chart;
    private PieChart chartText;
    private GridView gridView;

    private ProgressDialog dialog;
    private TextView mAreaTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_current_alarm_charts);
        super.onCreate(savedInstanceState);
        setBarTitle("实时报警");
        chart = findViewById(R.id.mPieChart);
        chartText = findViewById(R.id.mPieChart_text);
        gridView = findViewById(R.id.mPieChart_grid_view);
        mAreaTitle = findViewById(R.id.title_name);

        dialog = showProgress(this, "加载中...", false);
        initAllData();
        initCharts();
    }

    private void initAllData() {
        RetrofitUtils.getInstance().getChargeCurrentPieData(HelperTool.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultBean<ChargePieChartsDataBean, ChargeExtraContentBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResultBean<ChargePieChartsDataBean, ChargeExtraContentBean> resultBean) {
                        dialog.dismiss();
                        if (resultBean.isSuccess()) {
                            // 处理行业饼状统计图
                            if (resultBean.getData() != null && resultBean.getData().getLsBusinessTypeRealAlarmCount() != null && resultBean.getData().getLsBusinessTypeRealAlarmCount().size() > 0) {
                                PieChartUtil.initTradePieChart(ChargeCurrentAlarmActivity.this, resultBean.getData().getBusinessTypeRealAlarmSum(), gridView, chartText, resultBean.getData().getLsBusinessTypeRealAlarmCount(), false);
                            }
                            // 处理区域饼状统计图
                            if (resultBean.getData() != null && resultBean.getData().getLsAreaRealAlarmCount() != null && resultBean.getData().getLsAreaRealAlarmCount().size() > 0) {
                                setData(chart, resultBean.getData().getLsAreaRealAlarmCount(), resultBean.getData().getAreaRealAlarmSum());
                                if (resultBean.getExtra() != null && resultBean.getExtra().getLevel() == 3) {
                                    mAreaTitle.setText("各事件实时报警占比");
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.eTag(e.getMessage());
                        dialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                    }
                });
    }

    private void initCharts() {

        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        //tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        //chart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
        //chart.setCenterText(generateCenterSpannableText());

        chart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        chart.setDrawHoleEnabled(false);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        //chart.setOnChartValueSelectedListener(this);

        //seekBarX.setProgress(4);
        //seekBarY.setProgress(100);

        chart.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);
    }

    private void setData(PieChart chart, List<ChargePieChartsAreaBean> areaBeans, int sum) {

        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < areaBeans.size(); i++) {
            entries.add(new PieEntry(areaBeans.get(i).getStatisticsCount(), areaBeans.get(i).getAreaName()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        //dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : PieChartUtil.MATERIAL_COLORS_TRADE)
            colors.add(c);

//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);


        dataSet.setValueLinePart1OffsetPercentage(80f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setValueLineWidth(0.5f);//折线的粗细
        dataSet.setValueLineColor(getResources().getColor(R.color.gray));//折线颜色
        //dataSet.setUsingSliceColorAsValueLineColor(true);

        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        //数据与区块之间的用于指示的折线样式
        dataSet.setValueLinePart1OffsetPercentage(80f);//折线中第一段起始位置相对于区块的偏移量, 数值越大, 折线距离区块越远
        dataSet.setValueLinePart1Length(0.2f);//折线中第一段长度占比
        dataSet.setValueLinePart2Length(0.6f);//折线中第二段长度最大占比
        dataSet.setValueLineWidth(0.5f);//折线的粗细
        dataSet.setValueLineColor(getResources().getColor(R.color.gray));//折线颜色

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new AreaPercentFormatter(chart, sum));
        data.setValueTextSize(11f);
        data.setValueTextColor(getResources().getColor(R.color.gray));
        //data.setValueTypeface(tf);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }

}
