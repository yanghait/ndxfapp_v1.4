package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.LabelNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.RealDataLogBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.count.ILabelInfoCountPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.count.impl.CountPersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.ILabelRecordTweentyHourPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl.NodeBasePersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.ui.LineChartMarkView;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 标签信息和记录详情页
 */
public class LabelInfoRecordActivity extends BaseActivity implements ILabelRecordTweentyHourPersenter.ILabelRecordTweentyHourView, ILabelInfoCountPersenter.ILabelInfoCountView,
        OnDateSetListener {
    private ILabelRecordTweentyHourPersenter persenterRecord;
    private ILabelInfoCountPersenter persenterLabelInfoCount;
    private LabelNodeBean labelNodeBean;
    private List<RealDataLogBean> dataList;
    private LineChart lineChart;
    //24小时记录数量统计
    private TextView txtTitle;

    private TextView mTxtTitleSum;
    //当前值
    private TextView txtNowValue;
    //是否配置报警
    private TextView txtAlarmType;
    //近三十天报警记录数量
    private TextView txtAlarmCount;
    //近三十天数据冻结数量
    private TextView txtValueCount;


    //时间选择控件初始时间
    public Calendar initStartTime = Calendar.getInstance();
    //时间选择控件结束时间
    public Calendar initEndTime = Calendar.getInstance();

    //开始时间毫秒
    public long startLongTime;

    //结束时间的毫秒数
    public long endLongTime;

    private TextView mStartTime;

    private TextView mEndTime;

    public int selectFlag = 0;

    private ProgressDialog dialog;

    private int timeHour = 24;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_label_info_record);
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Object queryObj = intent.getSerializableExtra("data");
        if (queryObj == null) {
            HelperView.Toast(this, "标签节点不存在!");
            return;
        }
        labelNodeBean = (LabelNodeBean) queryObj;
        setBarTitle(labelNodeBean.getName());

        mTxtTitleSum = findViewById(R.id.txt_title_sum);

        txtTitle = findViewById(R.id.txt_title);
        txtNowValue = findViewById(R.id.txt_now_value);
        txtAlarmType = findViewById(R.id.txt_alarm_setting);
        txtAlarmCount = findViewById(R.id.txt_alarm_count);
        txtValueCount = findViewById(R.id.txt_value_count);

        mStartTime = findViewById(R.id.txt_start_time);
        mEndTime = findViewById(R.id.txt_end_time);

        txtNowValue.setText(labelNodeBean.getTranslateValue());

        persenterRecord = NodeBasePersenterFactory.getILabelRecordTweentyHourPersenter(this);
        addPersenter(persenterRecord);
        persenterLabelInfoCount = CountPersenterFactory.getLabelInfoCountPersenterImpl(this);
        addPersenter(persenterLabelInfoCount);
        lineChart = findViewById(R.id.line_chart);
        initView();

        dialog = showProgress(this, "加载中...", false);

        persenterRecord.doLabelRecordTweentyHour(labelNodeBean.getID(), null, null);
        persenterLabelInfoCount.doLabelInfoCount(labelNodeBean.getID());


        initSelectTime();

        initOnClick();

        doRequestData("", "");
    }

    private void initOnClick() {
        mStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFlag = 0;
                showTimeSelect();

            }
        });

        mEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFlag = 1;
                showTimeSelect();
            }
        });
    }

    public void initSelectTime() {
        initStartTime.add(Calendar.DAY_OF_MONTH, -31);
        //initEndTime.add(Calendar.YEAR, 1);


        Calendar queryTime = Calendar.getInstance();
        endLongTime = queryTime.getTimeInMillis();
        queryTime.add(Calendar.DAY_OF_MONTH, -31);
        startLongTime = queryTime.getTimeInMillis();
        mStartTime.setText(HelperTool.MillTimeToStringDate(startLongTime).concat(" "));
        mEndTime.setText(HelperTool.MillTimeToStringDate(endLongTime).concat(" "));
    }

    public void showTimeSelect() {

        String queryTitle = "开始时间选择";
        if (selectFlag == 1) {
            queryTitle = "结束时间选择";
        }
        long selectTime = startLongTime;
        if (selectFlag == 1) {
            selectTime = endLongTime;
        }
        TimePickerDialog mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)//回调
                .setToolBarTextColor(getResources().getColor(R.color.fire_fire))
                .setCancelStringId("取消")//取消按钮
                .setSureStringId("确定")//确定按钮
                .setTitleStringId(queryTitle)//标题
                .setYearText("年")//Year
                .setMonthText("月")//Month
                .setDayText("日")//Day
                .setHourText("时")//Hour
                .setMinuteText("分")//Minute
                .setCyclic(false)//是否可循环
                .setMinMillseconds(initStartTime.getTimeInMillis())//最小日期和时间
                .setMaxMillseconds(initEndTime.getTimeInMillis())//最大日期和时间
                .setCurrentMillseconds(selectTime)
                .setThemeColor(getResources().getColor(R.color.tool_bar))
                .setType(Type.ALL)//类型
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))//未选中的文本颜色
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.tool_bar))//当前文本颜色timepicker_toolbar_bg
                .setWheelItemTextSize(14)//字体大小
                .build();

        mDialogAll.show(getSupportFragmentManager(), "ALL");
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millSeconds) {
        if (selectFlag == 0) {
            if (millSeconds >= endLongTime) {
                HelperView.Toast(this, "开始时间不能大于等于结束时间!");
                return;
            }
            startLongTime = millSeconds;
        } else {
            if (millSeconds <= startLongTime) {
                HelperView.Toast(this, "结束时间不能小于等于开始时间!");
                return;
            }
            endLongTime = millSeconds;
        }
        mStartTime.setText(HelperTool.MillTimeToStringDateDetail(startLongTime).concat(" "));
        mEndTime.setText(HelperTool.MillTimeToStringDateDetail(endLongTime).concat(" "));

        dialog.show();

        LogUtils.showLoge("输出输入参数信息1212---", HelperTool.MillTimeToStringDateDetail(startLongTime) + "~~~" + HelperTool.MillTimeToStringDateDetail(endLongTime));


        String startTime = HelperTool.MillTimeToStringDateDetail(startLongTime);

        String endTime = HelperTool.MillTimeToStringDateDetail(endLongTime);

        doRequestData(startTime, endTime);

        //Calendar.

        timeHour = (int) ((endLongTime - startLongTime) / 1000 / (60 * 60));


        persenterRecord.doLabelRecordTweentyHour(labelNodeBean.getID(), startTime, endTime);
    }

    protected void doRequestData(String startTime, String endTime) {
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());//
        params.put("labelID", labelNodeBean.getID());
        params.put("startTime", startTime);
        params.put("endTime", endTime);

//        HashMap<String, String> params = new HashMap<>();
//        params.put("Token", HelperTool.getToken());//
//        params.put("projectID", labelNodeBean.getID());
//        params.put("startTime", "");
//        params.put("endTime", "");

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_GET_STSTEM_HISTORY_TREND_INFO)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        com.blankj.utilcode.util.LogUtils.aTag("历史趋势记录9999---", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        com.blankj.utilcode.util.LogUtils.aTag("历史趋势记录1212---", response);
                    }
                });

    }

    private void initView() {
        loadChart();
    }

    private XAxis xAxis;                //X轴
    private YAxis leftYAxis;            //左侧Y轴
    private YAxis rightYaxis;           //右侧Y轴
    private LimitLine limitLine;        //限制线

    private void loadChart() {
        /***图表设置***/
        //是否展示网格线
        lineChart.setDrawGridBackground(false);
        //是否显示边界
        lineChart.setDrawBorders(true);
        //是否可以拖动
        lineChart.setDragEnabled(true);
        //是否有触摸事件
        lineChart.setTouchEnabled(true);
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.setDrawGridBackground(false);
        lineChart.setPinchZoom(false);
        //是否显示边界
        lineChart.setDrawBorders(false);
        //设置XY轴动画效果
        lineChart.animateY(2500);
        lineChart.animateX(1500);

        /***XY轴的设置***/
        xAxis = lineChart.getXAxis();
        leftYAxis = lineChart.getAxisLeft();
        leftYAxis.setXOffset(15);
        rightYaxis = lineChart.getAxisRight();
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        //保证Y轴从0开始，不然会上移一点
        leftYAxis.setAxisMinimum(0f);
        rightYaxis.setAxisMinimum(0f);
        xAxis.setDrawGridLines(false);
        rightYaxis.setDrawGridLines(false);
        leftYAxis.setDrawGridLines(true);
        leftYAxis.setStartAtZero(false);
        rightYaxis.setEnabled(false);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {

                // 测试Y轴时间返回

                int position = (int) value;
                if (position <= dataList.size() - 1) {
                    return dataList.get(position).getTimeStr();
                }
                return "";
            }
        });
    }

    /**
     * 曲线初始化设置 一个LineDataSet 代表一条曲线
     *
     * @param lineDataSet 线条
     * @param color       线条颜色
     * @param mode
     */
    private void initLineDataSet(LineDataSet lineDataSet, int color, LineDataSet.Mode mode) {
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(3f);
        //设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(10f);
        //设置折线图填充
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);
        if (mode == null) {
            //设置曲线展示为圆滑曲线（如果不设置则默认折线）
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        } else {
            lineDataSet.setMode(mode);
        }
    }

    /**
     * 展示曲线
     *
     * @param dataList 数据集合
     * @param name     曲线名称
     * @param color    曲线颜色
     */
    public void showLineChart(List<RealDataLogBean> dataList, String name, int color) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            RealDataLogBean data = dataList.get(i);
            float value = Float.parseFloat(data.getValue());
            Entry entry = new Entry(i, value);
            entries.add(entry);
        }
        // 每一个LineDataSet代表一条线
        LineDataSet lineDataSet = new LineDataSet(entries, name);
        initLineDataSet(lineDataSet, color, LineDataSet.Mode.LINEAR);
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
    }

    /**
     * 设置线条填充背景颜色
     *
     * @param drawable
     */
    public void setChartFillDrawable(Drawable drawable) {
        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            LineDataSet lineDataSet = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            //避免在 initLineDataSet()方法中 设置了 lineDataSet.setDrawFilled(false); 而无法实现效果
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillDrawable(drawable);
            lineChart.invalidate();
        }
    }

    /**
     * 设置 可以显示X Y 轴自定义值的 MarkerView
     */
    public void setMarkerView() {
        LineChartMarkView mv = new LineChartMarkView(this, xAxis.getValueFormatter());
        mv.setChartView(lineChart);
        lineChart.setMarker(mv);
        lineChart.invalidate();
    }


    /**
     * 获取实时数据记录记录回调
     *
     * @param result
     */
    @Override
    public void callBackLabelRecordTweentyHour(ResultBean<List<RealDataLogBean>, String> result) {
        dialog.dismiss();
        try {
            if (result.isSuccess()) {
                dataList = result.getData();

                //LogUtils.showLoge("输出返回历史趋势数量1111", String.valueOf(dataList.size()));

                if (timeHour > 24) {
                    int day = (timeHour / 24);
                    txtTitle.setText(String.valueOf("近" + day + "天数据记录总数 "));
                } else {
                    txtTitle.setText(String.valueOf("近" + timeHour + "小时数据记录总数 "));
                }
                mTxtTitleSum.setText(String.valueOf(dataList.size()));

                //LogUtils.showLoge("记录数7878000---",dataList.size()+"");

                setMarkerView();
                Drawable drawable = getResources().getDrawable(R.drawable.fade_blue);
                if (dataList.size() > 0) {
                    showLineChart(dataList, labelNodeBean.getName(), Color.parseColor("#f48c26"));
                    setChartFillDrawable(drawable);
                }

            } else {
                HelperView.Toast(this, "获取标签记录失败！");
            }
        } catch (Exception e) {
            HelperView.Toast(this, "图标失败:" + e.getMessage());
        }

    }

    @Override
    public void callBackError(ResultBean<String, String> resultBean, String action) {
        super.callBackError(resultBean, action);
        dialog.dismiss();
    }

    /**
     * 获取统计记录回调
     *
     * @param result
     */
    @Override
    public void callBackLabelInfoCount(Map<String, String> result) {
        try {
            if (result.size() > 0) {
                txtAlarmType.setText(result.get("alarmState"));
                txtValueCount.setText(result.get("realTotal"));
                txtAlarmCount.setText(result.get("alarmTotal"));
            } else {
                HelperView.Toast(this, "获取统计记录是失败！");
            }
        } catch (Exception e) {

        }

    }


}
