package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.charge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.model.GradientColor;
import com.github.mikephil.charting.utils.Utils;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.charge.ChargeChartsHistoryAlarmListBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.charge.ChargeChartsListBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.charge.ChargeExtraContentBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.charge.ChargePieChartsTradeBean;
import com.ynzhxf.nd.xyfirecontrolapp.network.RetrofitUtils;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.HistoryAlarmAreaFormatter;
import com.ynzhxf.nd.xyfirecontrolapp.util.PieChartUtil;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * author hbzhou
 * date 2019/10/30 13:34
 * 主管部门历史报警统计图列表
 */
public class ChargeChartsHistoryActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private RadioGroup radioGroup;
    private int radioButtonId;

    private ProgressDialog dialog;

    private int level = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_charge_charts_history);
        super.onCreate(savedInstanceState);
        setBarTitle("历史报警");
        recyclerView = findViewById(R.id.charts_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        radioGroup = findViewById(R.id.charts_radio_group);

        dialog = showProgress(this, "加载中...", false);
        radioButtonId = R.id.radio_button1;
        initRadioGroup();
        initChartsHistoryAlarm("7");
    }

    /**
     * 处理RadioButton 时间筛选
     */
    private void initRadioGroup() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton1 = findViewById(R.id.radio_button1);
                RadioButton radioButton2 = findViewById(R.id.radio_button2);
                RadioButton radioButton3 = findViewById(R.id.radio_button3);
                if (radioButtonId == i) {
                    return;
                } else {
                    radioButtonId = i;
                }
                switch (i) {
                    case R.id.radio_button1:

                        //ToastUtils.showLong("000");
                        radioButton1.setTextColor(getResources().getColor(R.color.white));
                        radioButton2.setTextColor(getResources().getColor(R.color.charts_radio_select));
                        radioButton3.setTextColor(getResources().getColor(R.color.charts_radio_select));

                        initChartsHistoryAlarm("7");
                        break;
                    case R.id.radio_button2:
                        //ToastUtils.showLong("111");
                        radioButton1.setTextColor(getResources().getColor(R.color.charts_radio_select));
                        radioButton2.setTextColor(getResources().getColor(R.color.white));
                        radioButton3.setTextColor(getResources().getColor(R.color.charts_radio_select));

                        initChartsHistoryAlarm("30");
                        break;
                    case R.id.radio_button3:
                        //ToastUtils.showLong("222");
                        radioButton1.setTextColor(getResources().getColor(R.color.charts_radio_select));
                        radioButton2.setTextColor(getResources().getColor(R.color.charts_radio_select));
                        radioButton3.setTextColor(getResources().getColor(R.color.white));

                        initChartsHistoryAlarm("90");
                        break;
                }
            }
        });
    }

    private void initChartsHistoryAlarm(String days) {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        //LogUtils.showLoge("传参---007009---",days);
        RetrofitUtils.getInstance().getChargeChartsHistoryAlarmData(HelperTool.getToken(), days)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultBean<ChargeChartsHistoryAlarmListBean, ChargeExtraContentBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResultBean<ChargeChartsHistoryAlarmListBean, ChargeExtraContentBean> resultBean) {
                        dialog.dismiss();
                        if (resultBean.isSuccess() && resultBean.getData() != null) {
                            initData(resultBean.getData());
                            if (resultBean.getExtra() != null) {
                                level = resultBean.getExtra().getLevel();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                    }
                });
    }

    private void initData(ChargeChartsHistoryAlarmListBean alarmListBean) {
        List<ChargeChartsListBean> listBeans = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ChargeChartsListBean bean = new ChargeChartsListBean();
            bean.setModeltype(i + 1);
            bean.setHistoryAlarmListBean(alarmListBean);
            listBeans.add(bean);
        }
        recyclerView.setAdapter(new MultipleItemAdapter(listBeans));
    }

    private class MultipleItemAdapter extends BaseMultiItemQuickAdapter<ChargeChartsListBean, BaseViewHolder> {
        /**
         * Same as QuickAdapter#QuickAdapter(Context,int) but with
         * some initialization data.
         *
         * @param data A new list is created out of this one to avoid mutable list
         */
        private MultipleItemAdapter(List<ChargeChartsListBean> data) {
            super(data);
            addItemType(1, R.layout.item_charts_history_alarm_one);
            addItemType(2, R.layout.item_charts_history_alarm_two);
            addItemType(3, R.layout.item_charts_history_alarm_three);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, ChargeChartsListBean item) {
            switch (item.getItemType()) {
                case 1:
                    LineChart lineChart = helper.getView(R.id.chart1);
                    TextView historyAlarmCount = helper.getView(R.id.history_alarm_count);
                    historyAlarmCount.setText("报警总数:".concat(String.valueOf(item.getHistoryAlarmListBean().getHistoryAlarmSum())));
                    int count = item.getHistoryAlarmListBean().getLsHistoryAlarmTrend().size();
                    int range = 0;
                    for (int i = 0; i < item.getHistoryAlarmListBean().getLsHistoryAlarmTrend().size(); i++) {
                        if (item.getHistoryAlarmListBean().getLsHistoryAlarmTrend().get(i).getStatisticsValue() > range) {
                            range = item.getHistoryAlarmListBean().getLsHistoryAlarmTrend().get(i).getStatisticsValue();
                        }
                    }
                    initCubicLineCharts(lineChart, count, range, item.getHistoryAlarmListBean());
                    break;
                case 2:
                    BarChart barChart = helper.getView(R.id.bar_chart);
                    TextView mBarTitle = helper.getView(R.id.bar_title);
                    if (level == 3) {
                        mBarTitle.setText("事件历史报警");
                    } else {
                        mBarTitle.setText("区域历史报警");
                    }
                    initBarCharts(barChart, item.getHistoryAlarmListBean());
                    break;
                case 3:
                    PieChart pieChart = helper.getView(R.id.pie_chart);
                    GridView gridView = helper.getView(R.id.pieChart_grid_view);
                    List<ChargePieChartsTradeBean> tradeBeanList = new ArrayList<>();
                    for (int i = 0; i < item.getHistoryAlarmListBean().getLsBusinessTypeHistoryAlarm().size(); i++) {
                        ChargeChartsHistoryAlarmListBean.LsBusinessTypeHistoryAlarmBean alarmBean = item.getHistoryAlarmListBean().getLsBusinessTypeHistoryAlarm().get(i);
                        ChargePieChartsTradeBean tradeBean = new ChargePieChartsTradeBean();
                        tradeBean.setBusinessTypeId(alarmBean.getBusinessTypeId());
                        tradeBean.setBusinessTypeName(alarmBean.getBusinessTypeName());
                        // --->todo 行业数量和百分比使用随机数
                        tradeBean.setStatisticsCount(alarmBean.getStatisticsCount());//(int) (Math.random() * (50 + 1))
                        tradeBean.setStatisticsValue(alarmBean.getStatisticsValue());
                        tradeBeanList.add(tradeBean);
                    }
                    PieChartUtil.initTradePieChart(ChargeChartsHistoryActivity.this, item.getHistoryAlarmListBean().getHistoryAlarmSum(), gridView, pieChart, tradeBeanList, true);
                    break;
            }
        }
    }

    private void initBarCharts(BarChart chart, final ChargeChartsHistoryAlarmListBean alarmBean) {
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(false);

        chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawGridBackground(false);
        // chart.setDrawYLabels(false);

        final List<String> citys = new ArrayList<>();
        for (int i = 0; i < alarmBean.getLsAreaHistoryAlarm().size(); i++) {
            citys.add(alarmBean.getLsAreaHistoryAlarm().get(i).getAreaName());
        }

        ValueFormatter xAxisFormatter = new HistoryAlarmAreaFormatter(citys);
        //new DayAxisValueFormatter(chart);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(tfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                BarEntry barEntry = (BarEntry) e;
                //ToastUtils.showLong(barEntry.getX() + "~~~~");

                String clickAreaName = citys.get(Math.round(barEntry.getX()) - 1);
                List<ChargeChartsHistoryAlarmListBean.LsHistoryAlarmProjectBean> projectBeans = new ArrayList<>();
                // 如果主管部门是区级或者县级 点击事件柱状图显示所有项目
                if (level == 3) {
                    projectBeans.addAll(alarmBean.getLsProjectWithAreaBusiness());
                } else {
                    for (int i = 0; i < alarmBean.getLsProjectWithAreaBusiness().size(); i++) {
                        ChargeChartsHistoryAlarmListBean.LsHistoryAlarmProjectBean bean1 = alarmBean.getLsProjectWithAreaBusiness().get(i);
                        if (!StringUtils.isEmpty(bean1.getAddress()) && bean1.getAddress().contains(clickAreaName)) {
                            projectBeans.add(bean1);
                        }
                    }
                }

                Intent intent = new Intent(ChargeChartsHistoryActivity.this, ChargeChartsProjectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) projectBeans);
                intent.putExtras(bundle);
                intent.putExtra("title", clickAreaName);
                startActivity(intent);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        //ValueFormatter custom = new MyValueFormatter("$");

        YAxis leftAxis = chart.getAxisLeft();
        //leftAxis.setTypeface(tfLight);
        leftAxis.setLabelCount(8, false);
        //leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
        rightAxis.setDrawGridLines(false);
        //rightAxis.setTypeface(tfLight);
        rightAxis.setLabelCount(8, false);
        //rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        l.setEnabled(false);

        float start = 1f;

        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = (int) start; i < start + alarmBean.getLsAreaHistoryAlarm().size(); i++) {
            //float val = (float) (Math.random() * (range + 1));
            float val = alarmBean.getLsAreaHistoryAlarm().get(i - 1).getStatisticsValue();

//            if (Math.random() * 100 < 25) {
//                values.add(new BarEntry(i, val, getResources().getDrawable(R.drawable.star)));
//            } else {
            values.add(new BarEntry(i, val));
//            }
        }

        BarDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(values, "");

            set1.setDrawIcons(false);

//            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            /*int startColor = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
            int endColor = ContextCompat.getColor(this, android.R.color.holo_blue_bright);
            set1.setGradientColor(startColor, endColor);*/

            int startColor1 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
            int startColor2 = ContextCompat.getColor(this, android.R.color.holo_blue_light);
            int startColor3 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
            int startColor4 = ContextCompat.getColor(this, android.R.color.holo_green_light);
            int startColor5 = ContextCompat.getColor(this, android.R.color.holo_red_light);
            int endColor1 = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
            int endColor2 = ContextCompat.getColor(this, android.R.color.holo_purple);
            int endColor3 = ContextCompat.getColor(this, android.R.color.holo_green_dark);
            int endColor4 = ContextCompat.getColor(this, android.R.color.holo_red_dark);
            int endColor5 = ContextCompat.getColor(this, android.R.color.holo_orange_dark);

            List<GradientColor> gradientColors = new ArrayList<>();
            gradientColors.add(new GradientColor(startColor1, endColor1));
            gradientColors.add(new GradientColor(startColor2, endColor2));
            gradientColors.add(new GradientColor(startColor3, endColor3));
            gradientColors.add(new GradientColor(startColor4, endColor4));
            gradientColors.add(new GradientColor(startColor5, endColor5));

            set1.setGradientColors(gradientColors);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            //data.setValueTypeface(tfLight);
            data.setBarWidth(0.5f);

            chart.setData(data);

            for (IDataSet set : chart.getData().getDataSets())
                set.setDrawValues(false);
            chart.invalidate();
        }
    }

    // 处理趋势统计图
    private void initCubicLineCharts(final LineChart chart, int count, int range, ChargeChartsHistoryAlarmListBean alarmListBean) {
        {   // // Chart Style // //
            // background color
            chart.setBackgroundColor(Color.WHITE);

            // disable description text
            chart.getDescription().setEnabled(false);

            // enable touch gestures
            chart.setTouchEnabled(true);

            // set listeners
            //chart.setOnChartValueSelectedListener(this);
            chart.setDrawGridBackground(false);

            // create marker to display box when values are selected
//            MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
//
//            // Set the marker to the chart
//            mv.setChartView(chart);
//            chart.setMarker(mv);

            // enable scaling and dragging
            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);
            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            chart.setPinchZoom(true);
        }

        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            // vertical grid lines
            //xAxis.enableGridDashedLine(10f, 10f, 0f);
            //xAxis.enableAxisLineDashedLine(10f,10f,0f);
            xAxis.setDrawGridLines(false);

            List<String> date = new ArrayList<>();

            for (int i = 0; i < alarmListBean.getLsHistoryAlarmTrend().size(); i++) {
                date.add(alarmListBean.getLsHistoryAlarmTrend().get(i).getDayShow());
            }

            xAxis.setValueFormatter(new HistoryAlarmAreaFormatter(date));
        }

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = chart.getAxisLeft();

            // disable dual axis (only use LEFT axis)
            chart.getAxisRight().setEnabled(false);

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f);
            //yAxis.enableAxisLineDashedLine(10f,10f,0f);
            //yAxis.disableGridDashedLine();
            //yAxis.disableAxisLineDashedLine();

            //yAxis.setDrawZeroLine(false);
            //yAxis.setGridColor(Color.WHITE);
            //yAxis.setDrawGridLines(false);

            // axis range
            if (range > 0) {
                yAxis.setAxisMaximum(range);
            } else {
                yAxis.setAxisMaximum(500);
            }

            yAxis.setAxisMinimum(0f);
        }

//
//        {   // // Create Limit Lines // //
//            LimitLine llXAxis = new LimitLine(9f, "Index 10");
//            llXAxis.setLineWidth(4f);
//            llXAxis.enableDashedLine(10f, 10f, 0f);
//            llXAxis.setLabelPosition(LimitLabelPosition.RIGHT_BOTTOM);
//            llXAxis.setTextSize(10f);
//            llXAxis.setTypeface(tfRegular);
//
//            LimitLine ll1 = new LimitLine(150f, "Upper Limit");
//            ll1.setLineWidth(4f);
//            ll1.enableDashedLine(10f, 10f, 0f);
//            ll1.setLabelPosition(LimitLabelPosition.RIGHT_TOP);
//            ll1.setTextSize(10f);
//            ll1.setTypeface(tfRegular);
//
//            LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
//            ll2.setLineWidth(4f);
//            ll2.enableDashedLine(10f, 10f, 0f);
//            ll2.setLabelPosition(LimitLabelPosition.RIGHT_BOTTOM);
//            ll2.setTextSize(10f);
//            ll2.setTypeface(tfRegular);
//
//            // draw limit lines behind data instead of on top
//            yAxis.setDrawLimitLinesBehindData(true);
//            xAxis.setDrawLimitLinesBehindData(true);
//
//            // add limit lines
//            yAxis.addLimitLine(ll1);
//            yAxis.addLimitLine(ll2);
//            //xAxis.addLimitLine(llXAxis);
//        }

        // add data
//        seekBarX.setProgress(7);
//        seekBarY.setProgress(500);
//        setData(7, 500);

        // draw points over time
        chart.animateX(500);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // draw legend entries as lines
        l.setForm(Legend.LegendForm.EMPTY);

        ArrayList<Entry> values = new ArrayList<>();

        float start = 1f;
        for (int i = 1; i < start + count; i++) {

            float val = alarmListBean.getLsHistoryAlarmTrend().get(i - 1).getStatisticsValue();
            values.add(new Entry(i, val/*getResources().getDrawable(R.drawable.star)*/));
        }

        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "");

            set1.setDrawIcons(false);

            // draw dashed line
            //set1.enableDashedLine(10f, 5f, 0f);

            // black lines and points
            set1.setColor(Color.YELLOW);
            set1.setCircleColor(Color.BLACK);

            // line thickness and point size
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);

            // draw points as solid circles
            set1.setDrawCircleHole(false);

            // customize legend entry
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            // text size of values
            set1.setValueTextSize(9f);

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f);

            // set the filled area
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            // 初始化趋势图****
            set1.setDrawCircles(false);
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setDrawValues(false);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            chart.setData(data);
        }
    }
}
