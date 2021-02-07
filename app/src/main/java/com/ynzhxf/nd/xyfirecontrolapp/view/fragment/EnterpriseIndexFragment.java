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

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
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
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ynzhxf.nd.xyfirecontrolapp.GloblePlantformDatas;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.NormalProjectAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.PagingBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.NewsBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.common.INewsListPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.common.INewsURLPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.common.impl.CommonPersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.count.IUserMaxEventCountPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.count.impl.CountPersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IUserHasAuthoryProjectPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl.NodeBasePersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.ui.MyXFormatter;
import com.ynzhxf.nd.xyfirecontrolapp.ui.RingDraw;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.NewsInfoActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.NewsListActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.adapter.SaleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.bakumon.library.view.BulletinView;

/**
 * 业主首页
 * Created by nd on 2018-07-11.
 */

public class EnterpriseIndexFragment extends BaseFragment implements IUserHasAuthoryProjectPersenter.IUserHasAuthoryProjectView, IUserMaxEventCountPersenter.IUserMaxEventCountView, INewsListPersenter.INewsListView, INewsURLPersenter.INewsURLView {
    /**
     * 组织架构名称
     */
    private TextView txtOrgName;

    /**
     * 事件总数
     */
    private RingDraw txtEventTotal;

    /**
     * 实时报警的数量
     */
    private TextView txtRealAlarmCount;

    //联网项目列表
    private RecyclerView rvUserMaxProject;

    //统计图表
    private HorizontalBarChart mChart;

    //布局容器
    private Context context;

    //刷新控件
    private RefreshLayout refreshLayout;

    /**
     * 全局缓存数据
     */
    private GloblePlantformDatas datas;

    //用户统计数据请求
    private IUserMaxEventCountPersenter userMaxEventCountPersenter;

    //用户有权限的项目列表请求

    private IUserHasAuthoryProjectPersenter userHasAuthoryProjectPersenter;


    //项目列表数据适配器
    public NormalProjectAdapter projectAdapter;

    public List<ProjectNodeBean> proDataList;


    private List<String> eventNameList;
    private List<Integer> eventCountList;

    public TextView projectListTitle;
    public ImageView abnormal;

    // 通告公告轮播组件
    private BulletinView bulletinView;
    // 获取新闻详情
    private INewsURLPersenter urlPresenter;
    // 请求新闻详情dialog
    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        datas = GloblePlantformDatas.getInstance();
        View view = inflater.inflate(R.layout.fragment_enterprise_index, null, false);
        rvUserMaxProject = (RecyclerView) view.findViewById(R.id.rv_pro_list);
        mChart = (HorizontalBarChart) view.findViewById(R.id.chart_total);
        txtOrgName = (TextView) view.findViewById(R.id.orgName);
        txtEventTotal = view.findViewById(R.id.event_total);
        txtRealAlarmCount = (TextView) view.findViewById(R.id.real_alarm_count);
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.refreshLayout);
        projectListTitle = view.findViewById(R.id.fragment_project_list_title);

        abnormal = view.findViewById(R.id.abnormal_project);
        abnormal.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.circle_flag));

        eventNameList = new ArrayList<>();
        eventCountList = new ArrayList<>();
        userMaxEventCountPersenter = CountPersenterFactory.getUserMaxEventCountPersenterImpl(this);
        addPersenter(userMaxEventCountPersenter);

        userHasAuthoryProjectPersenter = NodeBasePersenterFactory.getUserHasAuthoryProjectPersenterImpl(this);
        addPersenter(userHasAuthoryProjectPersenter);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvUserMaxProject.setLayoutManager(manager);
        proDataList = new ArrayList<>();
        projectAdapter = new NormalProjectAdapter(proDataList);
        rvUserMaxProject.setAdapter(projectAdapter);
        //下拉刷新
        //refreshLayout.setRefreshHeader(new MaterialHeader(context));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshLayout.finishRefresh(3000/*,false*/);//传入false表示刷新失败
                init();
            }
        });
        dialog = showProgress(getActivity(), "加载中,请稍后...", false);
        init();
        initNoticeTitle(view);
        return view;
    }

    public static EnterpriseIndexFragment newInstance() {
        return new EnterpriseIndexFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void callBackNewsURL(ResultBean<String, String> result) {
        dialog.dismiss();
        if (result.isSuccess()) {
            Intent intent = new Intent(getActivity(), NewsInfoActivity.class);
            intent.putExtra("data", result.getExtra());
            startActivity(intent);
        } else {
            HelperView.Toast(getActivity(), result.getMessage());
        }
    }

    @Override
    public void callBackNewsList(final ResultBean<PagingBean<NewsBean>, String> result) {
        if (result != null && result.getData() != null) {
            bulletinView.setAdapter(new SaleAdapter(getActivity(), result.getData().getRows()));

            bulletinView.setOnBulletinItemClickListener(new BulletinView.OnBulletinItemClickListener() {
                @Override
                public void onBulletinItemClick(int position) {
                    dialog = showProgress(getActivity(), "加载中,请稍后...", false);
                    urlPresenter.doNewsURL(result.getData().getRows().get(position).getNewsID());
                }
            });
        }
    }

    /**
     * 处理业主通知公告
     *
     * @param view
     */
    public void initNoticeTitle(View view) {
        INewsListPersenter listPresenter = CommonPersenterFactory.getNewListPersenter(this);
        listPresenter.doNewsList(1);
        addPersenter(listPresenter);
        urlPresenter = CommonPersenterFactory.getNewsURLPersenter(this);
        addPersenter(urlPresenter);

        LinearLayout ownerMessage = view.findViewById(R.id.owner_message);

        ownerMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewsListActivity.class);
                startActivity(intent);
            }
        });
        bulletinView = view.findViewById(R.id.bulletin_view);
    }

    /**
     * 初始化界面
     */
    private void init() {

        // 设置登录用户类型
        projectAdapter.setLoginType(SPUtils.getInstance().getInt("LoginType"));
        //组织架构名称
        txtOrgName.setText(datas.getLoginInfoBean().getOrgName());

        userMaxEventCountPersenter.doUserMaxEventCount();
        userHasAuthoryProjectPersenter.doUserHasAuthoryProject();

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

        xl.setLabelCount(eventCountList.size());

        YAxis yl = mChart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0);

        int[] a = new int[eventCountList.size()];
        for (int i = 0; i < a.length; i++) {
            a[i] = eventCountList.get(i);
        }
        setData(eventCountList.size(), a);
        mChart.setFitBars(true);
        mChart.animateXY(2000, 2000);
        Legend legend = mChart.getLegend();
        legend.setEnabled(false);  //不显示图例

//        for (int i = 0; i <eventNameList.size() ; i++) {
//            LogUtils.e("eventname000----"+eventNameList.get(i)+"~~~~"+eventCountList.get(i));
//        }
    }


    private void setData(int count, int[] val) {

        float barWidth = 0.5f;        //每个彩色数据条的宽度

        float spaceForBar = 1.0f;
//        if (count < 4) {
//            barWidth = 0.4f;
//        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < count; i++) {
            yVals1.add(new BarEntry(i * spaceForBar, val[i]));
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
     * 加载用户统计信息回调
     *
     * @param data
     */
    @Override
    public void callBackUserMaxEventCount(Map<String, String> data) {
        dialog.dismiss();
        eventCountList.clear();
        eventNameList.clear();
        boolean needLoad = false;
        if (data.size() <= 0) {
            String msg = "统计信息异常";
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            Log.e(TAG, msg);
            return;
        }
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
        txtEventTotal.setAgle(0, 270, Integer.parseInt(data.get("total")));
        txtRealAlarmCount.setText(data.get("realCount") + "");

    }


    @Override
    public void callBackError(ResultBean<String, String> resultBean, String code) {
        super.callBackError(resultBean, code);
        dialog.dismiss();
    }


    @Override
    public void callBackUserHasAuthoryProject(ResultBean<List<ProjectNodeBean>, String> resultBean) {
        dialog.dismiss();
        if (resultBean.isSuccess()) {
            proDataList.clear();
            proDataList.addAll(resultBean.getData());
            projectAdapter.notifyDataSetChanged();

            if (resultBean.getData().size() > 0) {
                ProjectNodeBean bean = resultBean.getData().get(0);
                SPUtils.getInstance().put("OwnerProjectId", bean.getID());
                SPUtils.getInstance().put("OwnerProjectName", bean.getName());
                SPUtils.getInstance().put("ProjectNum",resultBean.getData().size());
            }
        } else {
            HelperView.Toast(context, "项目列表：" + resultBean.getMessage());
        }
    }

}
