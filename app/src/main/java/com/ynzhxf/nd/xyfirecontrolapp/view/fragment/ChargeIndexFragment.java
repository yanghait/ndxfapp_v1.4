package com.ynzhxf.nd.xyfirecontrolapp.view.fragment;


import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.ChargeIndexBarChartBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.ICompententAreaPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl.NodeBasePersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.ui.CustomViewPager;
import com.ynzhxf.nd.xyfirecontrolapp.ui.MyMarkView;
import com.ynzhxf.nd.xyfirecontrolapp.ui.SpecialLoadHeader;
import com.ynzhxf.nd.xyfirecontrolapp.ui.XValuesFormatter;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.NewsListActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.PLVideoTextureHomeActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.charge.ChargeIndexPageSelectedActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.compentent.CompententAreaActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.compentent.CompententAreaCoutryActivity;
import com.ynzhxf.nd.xyfirecontrolapp.widget.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * author hbzhou
 * date 2019/5/17 10:06
 */
public class ChargeIndexFragment extends BaseFragment implements View.OnClickListener, ICompententAreaPersenter.ICompententAreaView {
    //布局容器
    public Context context;

    private BarChart chart;

    private TextView mEventCountOne;
    private TextView mEventCountTwo;
    private TextView mEventCountThree;

    private TextView mEventCountFour;
    private TextView mEventCountFive;
    private TextView mEventCountSix;

    private ArrayList<BaseFragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "精准治理", "火灾风险最高", "设备诊断异常"
    };

    private final List<String> mBeanList = new ArrayList<>();

    private ProgressDialog progressDialog;

    private LinearLayout menuNetproject;
    private LinearLayout menuNews;
    private LinearLayout videoConference;
    private LinearLayout fireGis;

    //区域数据请求
    public ICompententAreaPersenter compententAreaPersenter;

    private RefreshLayout mRefreshLayout;
    private Banner mBanner;
    private TextView mSearchButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_charge_index, container, false);
        chart = view.findViewById(R.id.charge_bar_chart);
        mEventCountOne = view.findViewById(R.id.charge_event_one);
        mEventCountTwo = view.findViewById(R.id.charge_event_two);
        mEventCountThree = view.findViewById(R.id.charge_event_three);
        mEventCountFour = view.findViewById(R.id.charge_event_four);
        mEventCountFive = view.findViewById(R.id.charge_event_five);
        mEventCountSix = view.findViewById(R.id.charge_event_six);

        menuNetproject = view.findViewById(R.id.menu_net_pro);
        menuNews = view.findViewById(R.id.menu_news);
        videoConference = view.findViewById(R.id.video_conference);
        fireGis = view.findViewById(R.id.fire_gis);
        mBanner = view.findViewById(R.id.charge_banner);
        mSearchButton = view.findViewById(R.id.charge_search_button);

        mRefreshLayout = view.findViewById(R.id.charge_refreshLayout);
        mRefreshLayout.setRefreshHeader(new SpecialLoadHeader(getActivity()));

        RelativeLayout mClickLayout1 = view.findViewById(R.id.click_layout1);
        RelativeLayout mClickLayout2 = view.findViewById(R.id.click_layout2);
        RelativeLayout mClickLayout3 = view.findViewById(R.id.click_layout3);

        RelativeLayout mClickLayout4 = view.findViewById(R.id.click_layout4);
        RelativeLayout mClickLayout5 = view.findViewById(R.id.click_layout5);
        RelativeLayout mClickLayout6 = view.findViewById(R.id.click_layout6);
        mClickLayout1.setOnClickListener(this);
        mClickLayout2.setOnClickListener(this);
        mClickLayout3.setOnClickListener(this);
        mClickLayout4.setOnClickListener(this);
        mClickLayout5.setOnClickListener(this);
        mClickLayout6.setOnClickListener(this);
        compententAreaPersenter = NodeBasePersenterFactory.getCompententAreaPersenterImpl(this);
        addPersenter(compententAreaPersenter);
        initProjectListView(view);
        initTopViewClick();
        initRefreshLayout();
        initTopBannerView();
        return view;
    }

    /**
     * 处理顶部banner和搜索框
     */
    private void initTopBannerView() {
        List<Integer> images2 = new ArrayList<>();
        images2.add(R.drawable.charge_banner1);
        images2.add(R.drawable.charge_banner2);
        images2.add(R.drawable.charge_banner3);
        //Integer[] images1={R.mipmap.home_icon2,R.mipmap.device_icon2,R.mipmap.application_icon2};
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(images2);
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.Default);
        //设置标题集合（当banner样式有显示title时）
        //homeBanner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
        // 点击顶部搜索框
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = showProgress(getActivity(), "加载中...", false);
                compententAreaPersenter.doCompententArea();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
    }

    private void initRefreshLayout() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000);

                getRightMenuData();

                initBarChart();

                initEventCount();
            }
        });
    }

    private void initTopViewClick() {
        //联网项目
        menuNetproject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showProgressDig(true);
                progressDialog = showProgress(getActivity(), "加载中...", false);
                compententAreaPersenter.doCompententArea();
            }
        });
        //通知公告
        menuNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewsListActivity.class);
                startActivity(intent);
            }
        });

        // 视频会议
        videoConference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //HelperView.Toast(getActivity(), "开发中,敬请期待!");
                Intent intent = new Intent(context, PLVideoTextureHomeActivity.class);
                startActivity(intent);
            }
        });

        // 消防GIS
        fireGis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelperView.Toast(getActivity(), "开发中,敬请期待!");
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (mBeanList.size() == 0) {
            ToastUtils.showLong("未发现事件统计内容!");
            return;
        }
        Intent intent;
        switch (view.getId()) {
            case R.id.click_layout1:
                intent = new Intent(context, ChargeIndexPageSelectedActivity.class);
                intent.putExtra("RightSelectedText", mBeanList.get(0));
                intent.putExtra("RightSelectedPosition", 0);
                startActivity(intent);
                break;
            case R.id.click_layout2:
                intent = new Intent(context, ChargeIndexPageSelectedActivity.class);
                intent.putExtra("RightSelectedText", mBeanList.get(1));
                intent.putExtra("RightSelectedPosition", 1);
                startActivity(intent);
                break;
            case R.id.click_layout3:
                intent = new Intent(context, ChargeIndexPageSelectedActivity.class);
                intent.putExtra("RightSelectedText", mBeanList.get(2));
                intent.putExtra("RightSelectedPosition", 2);
                startActivity(intent);
                break;
            case R.id.click_layout4:
                intent = new Intent(context, ChargeIndexPageSelectedActivity.class);
                intent.putExtra("RightSelectedText", mBeanList.get(3));
                intent.putExtra("RightSelectedPosition", 3);
                startActivity(intent);
                break;
            case R.id.click_layout5:
                intent = new Intent(context, ChargeIndexPageSelectedActivity.class);
                intent.putExtra("RightSelectedText", mBeanList.get(4));
                intent.putExtra("RightSelectedPosition", 4);
                startActivity(intent);
                break;
            case R.id.click_layout6:
                intent = new Intent(context, ChargeIndexPageSelectedActivity.class);
                intent.putExtra("RightSelectedText", mBeanList.get(5));
                intent.putExtra("RightSelectedPosition", 5);
                startActivity(intent);
                break;
        }
    }

    private void getRightMenuData() {
        HashMap<String, String> params = new HashMap<>();
        if (StringUtils.isEmpty(HelperTool.getToken())) {
            return;
        }
        params.put("Token", HelperTool.getToken());
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_CHARGE_INDEX_PROJECT_TYPE)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                final List<String> beanList = new Gson().fromJson(jsonObject.getJSONArray("data").toString(),
                                        new TypeToken<List<String>>() {
                                        }.getType());
                                if (beanList == null || beanList.size() == 0) {
                                    return;
                                }
                                mBeanList.addAll(beanList);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        progressDialog = showProgress(getActivity(), "加载中...", false);

        getRightMenuData();

        initBarChart();

        initEventCount();
    }

    private void initProjectListView(View view) {
        // 处理底部项目列表
        for (int i = 0; i < 3; i++) {
            mFragments.add(ChargeChildrenIndexFragment.newInstance(String.valueOf(i)));
        }

        //LogUtils.showLoge("sum000---",mFragments.size()+"---");

        CustomViewPager viewPager = view.findViewById(R.id.view_pager);
        MyPagerAdapter mAdapter = new MyPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(1);

        SlidingTabLayout tabLayout = view.findViewById(R.id.slide_tab_layout);
        tabLayout.setViewPager(viewPager);
        tabLayout.setCurrentTab(0);
    }

    /**
     * 获取事件统计数量
     */
    private void initEventCount() {

        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_CHARGE_INDEX_GET_SIX_DATA_COUNT)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showLong("获取事件统计出错!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("首页事件统计数量000", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                JSONObject json = jsonObject.getJSONObject("data");

                                try {
                                    initValueAnimator(Integer.parseInt(json.getString("ProjectOnlineSum")), mEventCountOne);
                                    initValueAnimator(Integer.parseInt(json.getString("EquipAbnormalCount")), mEventCountTwo);
                                    initValueAnimator(Integer.parseInt(json.getString("HighRiskProjectCount")), mEventCountThree);

                                    initValueAnimator(Integer.parseInt(json.getString("RealAlarmCount")), mEventCountFour);
                                    initValueAnimator(Integer.parseInt(json.getString("Recent24HourCount")), mEventCountFive);
                                    initValueAnimator(Integer.parseInt(json.getString("ProjectOfflineCount")), mEventCountSix);
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

//                                mEventCountOne.setText(json.getString("ProjectOnlineSum"));
//                                mEventCountTwo.setText(json.getString("EquipAbnormalCount"));
//                                mEventCountThree.setText(json.getString("HighRiskProjectCount"));
//
//                                mEventCountFour.setText(json.getString("RealAlarmCount"));
//                                mEventCountFive.setText(json.getString("Recent24HourCount"));
//                                mEventCountSix.setText(json.getString("ProjectOfflineCount"));

                            } else {
                                ToastUtils.showLong("获取事件统计失败!");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initValueAnimator(final int sumCount, final TextView mContentText) {
        if (sumCount == 0) {
            return;
        }
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, sumCount);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(3000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                mContentText.setText(String.valueOf(value));
            }
        });
        valueAnimator.start();
    }

    public static ChargeIndexFragment newInstance() {
        return new ChargeIndexFragment();
    }

    private void initBarChart() {
        //chart.setOnChartValueSelectedListener(this);
        chart.getDescription().setEnabled(false);

//        chart.setDrawBorders(true);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawBarShadow(false);

        chart.setDrawGridBackground(false);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkView mv = new MyMarkView(getActivity(), R.layout.custom_mark_view);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart

//        seekBarX.setProgress(10);
//        seekBarY.setProgress(100);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        //l.setTypeface(tfLight);
        l.setYOffset(0f);
        l.setXOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);
        l.setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        //xAxis.setTypeface(tfLight);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setTextSize(8f);

        // 设置底部横向标题并且不画竖线
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });
//        xAxis.setAxisMinimum(1f);
//        xAxis.setAxisMaximum(6f);

        YAxis leftAxis = chart.getAxisLeft();
        //leftAxis.setTypeface(tfLight);
        leftAxis.setValueFormatter(new DefaultValueFormatter(0));
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setAxisMaximum(100f);
        leftAxis.setLabelCount(11);

        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        //leftAxis.enableAxisLineDashedLine(10f,10f,0f);

        chart.getAxisRight().setEnabled(false);

        getBarChartData();

        // initBarChartData();
    }

    private void getBarChartData() {

        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_CHARGE_INDEX_GET_BAR_CHART_DATA)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.showLoge("获取统计图数据009---", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.showLoge("获取统计图数据010---", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                JSONObject json = jsonObject.getJSONObject("data");

                                ChargeIndexBarChartBean barChartData = new Gson().fromJson(json.toString(),
                                        ChargeIndexBarChartBean.class);

                                if (barChartData == null || barChartData.getLsAreaProject() == null || barChartData.getLsAreaProject().size() == 0) {
                                    ToastUtils.showLong("未获取到图表数据!");
                                    return;
                                }

                                List<Float> value1 = new ArrayList<>();
                                List<Float> value2 = new ArrayList<>();
                                List<Float> value3 = new ArrayList<>();

                                try {
                                    for (String str : barChartData.getLsLowEquipDetectCount()) {
                                        value1.add(Float.parseFloat(str));
                                    }
//                                value1.add(34f);
//                                value1.add(101f);

                                    for (String str : barChartData.getLsHighRiskCount()) {
                                        value2.add(Float.parseFloat(str));
                                    }
                                    for (String str : barChartData.getLsAlarmCount()) {
                                        value3.add(Float.parseFloat(str));
                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                    ToastUtils.showLong(e.getMessage());
                                }
//                                value3.add(18f);
//                                value3.add(56f);

                                initBarChartData(barChartData.getLsAreaProject(), value1, value2, value3);

                            } else {
                                ToastUtils.showLong("未获取到统计图数据,请稍后再试!");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 获取组合统计图数据 处理并显示
     */
    private void initBarChartData(final List<String> titleName, List<Float> value1, List<Float> value2, List<Float> value3) {
        float groupSpace = 0.19f;
        float barSpace = 0.07f; // x4 DataSet
        float barWidth = 0.20f; // x4 DataSet

//        float groupSpace = 0.08f;
//        float barSpace = 0.03f; // x4 DataSet
//        float barWidth = 0.2f; // x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

        //(0.25+ 0.05) * 3 + 0.1 = 1.00

        ArrayList<BarEntry> values1 = new ArrayList<>();
        ArrayList<BarEntry> values2 = new ArrayList<>();
        ArrayList<BarEntry> values3 = new ArrayList<>();
        //ArrayList<BarEntry> values4 = new ArrayList<>();

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(10);
        chart.getXAxis().setLabelCount(titleName.size());
        chart.getXAxis().setValueFormatter(new XValuesFormatter(titleName));

        for (int i = 1; i < titleName.size() + 1; i++) {
            values1.add(new BarEntry(i, value1.get(i - 1)));
            values2.add(new BarEntry(i, value2.get(i - 1)));
            values3.add(new BarEntry(i, value3.get(i - 1)));
        }

        BarDataSet set1, set2, set3, set4;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {

            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) chart.getData().getDataSetByIndex(1);
            set3 = (BarDataSet) chart.getData().getDataSetByIndex(2);
            //set4 = (BarDataSet) chart.getData().getDataSetByIndex(3);
            set1.setValues(values1);
            set2.setValues(values2);
            set3.setValues(values3);


            //set4.setValues(values4);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            // create 4 DataSets
            set1 = new BarDataSet(values1, "Company A");
            set1.setColor(Color.parseColor("#FFB107"));
            set2 = new BarDataSet(values2, "Company B");
            set2.setColor(Color.parseColor("#fb5f2f"));
            set3 = new BarDataSet(values3, "Company C");
            set3.setColor(Color.parseColor("#1890FF"));
            //set4 = new BarDataSet(values4, "Company D");
            //set4.setColor(Color.rgb(255, 102, 0));

            BarData data = new BarData(set1, set2, set3/*, set4*/);
            data.setValueFormatter(new LargeValueFormatter());
            //data.setValueTypeface(tfLight);

            chart.setData(data);
        }

        set1.setDrawValues(false);
        set2.setDrawValues(false);
        set3.setDrawValues(false);

        //chart.setAutoScaleMinMaxEnabled(true);

        // specify the width each bar should have
        chart.getBarData().setBarWidth(barWidth);

        // restrict the x-axis range
        chart.getXAxis().setAxisMinimum(1);

        chart.setDragEnabled(true);
        chart.setScaleYEnabled(false);
        // 让分组统计图超过一屏幕
        if (titleName.size() >= 7) {
            float scaleX = 2f + (titleName.size() - 7) * 0.5f;
            chart.setScaleMinima(scaleX, 0);
        }
        chart.setPinchZoom(false);
        chart.animateXY(2000, 2000);

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        chart.getXAxis().setAxisMaximum(titleName.size() + 1);
        chart.groupBars(1, groupSpace, barSpace);
        chart.invalidate();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void callBackError(ResultBean<String, String> resultBean, String code) {
        super.callBackError(resultBean, code);
        progressDialog.dismiss();
    }

    @Override
    public void callBackCompententArea(ResultBean<List<ProjectNodeBean>, String[]> result) {
        progressDialog.dismiss();
        try {
            if (result.isSuccess()) {
                if (result.getData().size() == 0) {
                    Toast.makeText(context, "账号异常！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = null;
                ProjectNodeBean queryObj = result.getData().get(0);
                if (queryObj.getNodeLevel() < 3) {
                    intent = new Intent(context, CompententAreaActivity.class);
                } else {
                    intent = new Intent(context, CompententAreaCoutryActivity.class);
                    intent.putExtra("code", 1);
                }
                intent.putExtra("data", result);
                startActivity(intent);
            } else {
                Toast.makeText(context, "获取联网项目失败！", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            HelperView.Toast(context, "联网项目失败:" + e.getMessage());
        }
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {

        private MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
