package com.ynzhxf.nd.xyfirecontrolapp.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ynzhxf.nd.xyfirecontrolapp.GloblePlantformDatas;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.DangerProjectAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.count.IUserMaxEventCountPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.count.impl.CountPersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.ICompententAreaPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IDangeroursUserProjectPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl.NodeBasePersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.ui.MyLoadHeader;
import com.ynzhxf.nd.xyfirecontrolapp.ui.MyXFormatter;
import com.ynzhxf.nd.xyfirecontrolapp.ui.RingDraw;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.NewsListActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.compentent.CompententAreaActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.compentent.CompententAreaCoutryActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主管部门 首页
 * Created by nd on 2018-07-11.
 */

public class CompetentIndexFragment extends BaseFragment implements IDangeroursUserProjectPersenter.IDangeroursUserProjectView, IUserMaxEventCountPersenter.IUserMaxEventCountView, ICompententAreaPersenter.ICompententAreaView {
    /**
     * 组织架构名称
     */
    public TextView txtOrgName;

    /**
     * 事件总数
     */
    private RingDraw txtEventTotal;

    /**
     * 实时报警的数量
     */
    private TextView txtRealAlarmCount;

    //精准治理列表
    public RecyclerView precisionManagementView;

    //统计图表
    public HorizontalBarChart mChart;

    //布局容器
    public Context context;
    //刷新控件
    private RefreshLayout refreshLayout;

    //联网项目菜单
    public LinearLayout menuNetproject;

    //通知公告菜单
    public LinearLayout menuNews;

    // 视频会议
    public LinearLayout videoConference;

    // 消防GIS
    public LinearLayout fireGis;

    // 查看更多
    public TextView view_More;

    /**
     * 全局缓存数据
     */
    public GloblePlantformDatas datas;

    /**
     * 精准治理数据请求
     */
    public IDangeroursUserProjectPersenter dangeroursUserProjectPersenter;
    //用户统计数据请求
    public IUserMaxEventCountPersenter userMaxEventCountPersenter;
    //区域数据请求
    public ICompententAreaPersenter compententAreaPersenter;

    private List<String> eventNameList;
    private List<Integer> eventCountList;
    // 精准治理图标
    public ImageView accurate_control;

    //精准治理文字
    public TextView accurate_control_text;

    public ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        datas = GloblePlantformDatas.getInstance();
        final View view = inflater.inflate(R.layout.fragment_compentent_index, null, false);
        view_More = view.findViewById(R.id.pro_more);
        precisionManagementView = (RecyclerView) view.findViewById(R.id.pro_recision);
        mChart = (HorizontalBarChart) view.findViewById(R.id.chart_total);
        txtOrgName = (TextView) view.findViewById(R.id.orgName);
        txtEventTotal = view.findViewById(R.id.event_total);
        txtRealAlarmCount = (TextView) view.findViewById(R.id.real_alarm_count);
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.charge_refreshLayout);
        menuNetproject = view.findViewById(R.id.menu_net_pro);
        menuNews = view.findViewById(R.id.menu_news);
        fireGis = view.findViewById(R.id.fire_gis);
        videoConference = view.findViewById(R.id.video_conference);
        accurate_control = view.findViewById(R.id.accurate_control);
        accurate_control.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.circle_flag));
        accurate_control_text = view.findViewById(R.id.accurate_control_text);
        eventNameList = new ArrayList<>();
        eventCountList = new ArrayList<>();
        dangeroursUserProjectPersenter = NodeBasePersenterFactory.getDangeroursUserProjectPersenterImpl(this);
        userMaxEventCountPersenter = CountPersenterFactory.getUserMaxEventCountPersenterImpl(this);
        compententAreaPersenter = NodeBasePersenterFactory.getCompententAreaPersenterImpl(this);
        addPersenter(dangeroursUserProjectPersenter);
        addPersenter(userMaxEventCountPersenter);
        addPersenter(compententAreaPersenter);
        //下拉刷新
        refreshLayout.setRefreshHeader(new MyLoadHeader(getActivity()));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                init(view);
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }
        });

        dialog = showProgress(getActivity(), "加载中...", false);

        init(view);
        return view;
    }

    public static CompetentIndexFragment newInstance() {
        return new CompetentIndexFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    /**
     * 初始化界面
     */
    public void init(View view) {

        //联网项目
        menuNetproject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showProgressDig(true);
                dialog = showProgress(getActivity(), "加载中...", false);
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
                HelperView.Toast(getActivity(), "开发中,敬请期待!");
            }
        });

        // 消防GIS
        fireGis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelperView.Toast(getActivity(), "开发中,敬请期待!");
            }
        });

        // 无查看更多
        view_More.setVisibility(View.INVISIBLE);

        //组织架构名称
        txtOrgName.setText(datas.getLoginInfoBean().getOrgName());

        dangeroursUserProjectPersenter.doDangeroursUserProject();
        userMaxEventCountPersenter.doUserMaxEventCount();

        //设置相关属性
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setContentDescription("事件统计");
        mChart.getDescription().setEnabled(false);
        mChart.setNoDataText("加载中....");
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        mChart.getAxisRight().setEnabled(false);  //不绘制右侧轴线
    }


    /**
     * 加载图表数据
     */
    private void loadChart() {

        XAxis xl = mChart.getXAxis();
        xl.setValueFormatter(new MyXFormatter(eventNameList));
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        //xl.setGranularity(1f);
        //xl.setAxisMinimum(0f);
        xl.setLabelCount(eventCountList.size());

        YAxis yl = mChart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f);
        int[] a = new int[eventCountList.size()];
        for (int i = 0; i < a.length; i++) {
            a[i] = eventCountList.get(i);
        }
        setData(eventCountList.size(), a);
        mChart.setFitBars(true);
        mChart.animateXY(2000, 2000);
        Legend legend = mChart.getLegend();
        legend.setEnabled(false);  //不显示图例
    }


    private void setData(int count, int[] val) {

        float barWidth = 0.5f;        //每个彩色数据条的宽度

        float spaceForBar = 1.0f;//彩色条间距

        //LogUtils.showLoge("输出每个报警数1313--",val[0]+"~~~~"+val[1]+"~~~"+val[2]);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < count; i++) {
            yVals1.add(new BarEntry(i*spaceForBar, val[i]));
        }
        BarDataSet set1;
        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "事件统计");

            set1.setDrawIcons(false);
            set1.setColors(getColors());//new int[]{Color.rgb(99, 67, 72), Color.rgb(14, 181, 136), Color.rgb(255, 222, 173), Color.rgb(105, 105, 105)}
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(barWidth);
            mChart.setData(data);
        }
    }

    // 分析每个统计条的颜色
    private int[] getColors() {

        int[] colors = new int[eventNameList.size()];

        for (int i = 0; i < eventNameList.size(); i++) {
            switch (eventNameList.get(i)) {
                case "无事件":
                    colors[i] = Color.rgb(47, 129, 247);
                    break;
                case "控制事件":
                    colors[i] = Color.rgb(201, 142, 208);
                    break;
                case "运行事件":
                    colors[i] = Color.rgb(98, 191, 252);
                    break;
                case "报警事件":
                    colors[i] = Color.rgb(252, 89, 90);
                    break;
                case "故障事件":
                    colors[i] = Color.rgb(255, 198, 85);
                    break;
                case "恢复事件":
                    colors[i] = Color.rgb(55, 214, 182);
                    break;
                case "通讯事件":
                    colors[i] = Color.rgb(114, 146, 249);
                    break;
                case "其他事件":
                    colors[i] = Color.rgb(206, 137, 142);
                    break;
            }
        }
        return colors;
    }

    /**
     * 加载最危险项目前十回调
     *
     * @param result
     */
    @Override
    public void callBackDangeroursUserProject(ResultBean<List<ProjectNodeBean>, String> result) {
        dialog.dismiss();
        try {
            if (result.isSuccess()) {
                LinearLayoutManager manager = new LinearLayoutManager(context);
                precisionManagementView.setLayoutManager(manager);
                DangerProjectAdapter adapter = new DangerProjectAdapter(result.getData());
                precisionManagementView.setAdapter(adapter);
            } else {
                String msg = "精准治理" + result.getMessage();
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                Log.e(TAG, msg);
            }
        } catch (Exception e) {
            HelperView.Toast(context, "精准治理失败：" + e.getMessage());
        }


    }

    /**
     * 加载用户统计信息回调
     *
     * @param data
     */
    @Override
    public void callBackUserMaxEventCount(Map<String, String> data) {
        dialog.dismiss();
        try {

            eventCountList.clear();
            eventNameList.clear();

            if (data.size() <= 0) {
                String msg = "统计信息异常";
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                Log.e(TAG, msg);
                return;
            }
            boolean needLoad = false;
            Map<String, Integer> countTotal = new HashMap<>();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                int queryValue = Integer.parseInt(entry.getValue());
                if (queryValue == 0) continue;
                boolean needAdd = true;
                switch (entry.getKey()) {
                    case "NONE_EVENT"://无事件
                        eventNameList.add("无事件");
                        break;
                    case "CONTROL_EVENT"://控制事件
                        eventNameList.add("控制事件");
                        break;
                    case "RUN_EVENT"://运行事件
                        eventNameList.add("运行事件");
                        break;
                    case "ALARM_EVENT": //报警事件
                        eventNameList.add("报警事件");
                        break;
                    case "FAULT_EVENT"://故障事件
                        eventNameList.add("故障事件");
                        break;
                    case "FAULT_RECOVERY"://故障恢复事件
                        eventNameList.add("恢复事件");
                        break;
                    case "SYSTEM_COMMUNICATION"://通讯事件
                        eventNameList.add("通讯事件");
                        break;
                    case "OTHER_EVENT"://其它事件
                        eventNameList.add("其它事件");
                        break;
                    default:
                        needAdd = false;
                }
                if (needAdd) {
                    eventCountList.add(queryValue);
                    needLoad = true;
                }
            }
            if (needLoad) {
                loadChart();
            } else {
                mChart.setNoDataText("无记录！");
                mChart.invalidate();
            }
            //refreshLayout.finishRefresh(true);
            txtEventTotal.setAgle(0, 270, Integer.parseInt(data.get("total")));
            txtRealAlarmCount.setText(data.get("realCount") + "");

        } catch (Exception e) {
            HelperView.Toast(context, "统计事件类型失败:" + e.getMessage());
        }
        hideProgressDig();

    }


    @Override
    public void callBackError(ResultBean<String, String> resultBean, String code) {
        super.callBackError(resultBean, code);
        dialog.dismiss();
    }

    @Override
    public void callBackCompententArea(ResultBean<List<ProjectNodeBean>, String[]> result) {
        dialog.dismiss();
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
}
