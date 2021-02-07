package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.AlarmLogAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.PagingBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.AlarmLogBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IProjectHistoryAlarmPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl.NodeBasePersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 项目历史报警数据
 */
public class ProjectHistoryAlarmActivity extends BaseActivity implements OnDateSetListener, IProjectHistoryAlarmPersenter.IProjectHistoryAlarmView {

    //项目对象
    private ProjectNodeBean projectNodeBean;
    //报警数据列表
    private List<AlarmLogBean> datalist;

    //当前页码
    private int pageSize = 1;
    //总数
    private int totalCount = 0;
    //分页总数
    private int totalPage = 1;
    //每页数量
    private int pageCount = 15;

    private TextView txtAlarmToatalCount;

    //开始时间控件
    private TextView txtStart;
    //开始时间毫秒
    private long startLongTime;

    //结束时间控件
    private TextView txtEnd;
    //结束时间的毫秒数
    private long endLongTime;

    private IProjectHistoryAlarmPersenter persenter;

    //时间选择控件初始时间
    private Calendar initStartTime = Calendar.getInstance();
    //时间选择控件结束时间
    private Calendar initEndTime = Calendar.getInstance();
    //上拉刷新控件
    private SmartRefreshLayout refreshLayout;

    private RecyclerView recyclerView;
    private AlarmLogAdapter adapter;

    private ProgressDialog dialog;


    /**
     * 开始时间和结束时间选择标识  0 是开始   1是结束
     */
    private int select = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_project_history_alarm);
        super.onCreate(savedInstanceState);
        Object queryObj = getIntent().getSerializableExtra("data");
        if (queryObj == null) {
            HelperView.Toast(this, "未找到项目！");
            return;
        }
        setBarTitle("历史报警");
        projectNodeBean = (ProjectNodeBean) queryObj;
        persenter = NodeBasePersenterFactory.getProjectHistoryAlarmPersenterImpl(this);
        addPersenter(persenter);
        datalist = new ArrayList<>();
        txtAlarmToatalCount = findViewById(R.id.txt_alarm_total_count);
        txtStart = findViewById(R.id.txt_start_time);
        txtEnd = findViewById(R.id.txt_end_time);
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.rv_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new AlarmLogAdapter(this, datalist);
        recyclerView.setAdapter(adapter);

        //showProgressDig(false);
        dialog = showProgress(this, "加载中,请稍后...", false);
        init();
        doReRequestData();
    }

    private void init() {
        initStartTime.add(Calendar.YEAR, -1);
        initEndTime.add(Calendar.YEAR, 1);
        Calendar queryTime = Calendar.getInstance();
        endLongTime = queryTime.getTimeInMillis();
        // 从业主首页入口进来显示24小时事件
        if (getIntent().getBooleanExtra("OneDay", false)) {
            queryTime.add(Calendar.DAY_OF_MONTH, -1);
            startLongTime = queryTime.getTimeInMillis();
        } else {
            queryTime.add(Calendar.DAY_OF_MONTH, -10);
            startLongTime = queryTime.getTimeInMillis();
        }

        //上拉加载
        //refreshLayout.setRefreshHeader(new MaterialHeader(this));
        // refreshLayout.getRefreshHeader().setPrimaryColors(getResources().getColor(R.color.fire_fire));
        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshLayout.finishRefresh(2000);
                doReRequestData();
            }

        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(2000);
                if (pageSize > totalPage) {
                    HelperView.Toast(ProjectHistoryAlarmActivity.this, "没有更多数据！");
                    refreshLayout.finishLoadMore(true);
                    return;
                }
                persenter.doProjectHistoryAlarm(pageSize, pageCount, projectNodeBean.getID(), HelperTool.MillTimeToStringTime(startLongTime), HelperTool.MillTimeToStringTime(endLongTime));
            }
        });

        txtStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = 0;
                showTimeSelect();
            }
        });

        txtEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = 1;
                showTimeSelect();
            }
        });
        setTextViewTime();
    }

    private void setTextViewTime() {
        txtStart.setText(HelperTool.MillTimeToStringTime(startLongTime));
        txtEnd.setText(HelperTool.MillTimeToStringTime(endLongTime));
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        if (select == 0) {
            if (millseconds >= endLongTime) {
                HelperView.Toast(this, "开始时间不能大于等于结束时间!");
                return;
            }
            startLongTime = millseconds;
        } else {
            if (millseconds <= startLongTime) {
                HelperView.Toast(this, "结束时间不能小于等于结束时间!");
                return;
            }
            endLongTime = millseconds;

        }
        //showProgressDig(false);
//        if (dialog.isShowing()) {
//            dialog.dismiss();
//        }

        dialog.show();
        setTextViewTime();
        doReRequestData();

    }

    /**
     * 重新请求分页数据
     */
    private void doReRequestData() {
        datalist.clear();
        pageSize = 1;
        totalCount = 0;
        totalPage = 1;
        persenter.doProjectHistoryAlarm(pageSize, pageCount, projectNodeBean.getID(), HelperTool.MillTimeToStringTime(startLongTime), HelperTool.MillTimeToStringTime(endLongTime));
    }

    private void showTimeSelect() {

        String queryTitle = "开始时间选择";
        if (select == 1) {
            queryTitle = "结束时间选择";
        }
        long selectTime = startLongTime;
        if (select == 1) {
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
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.tool_bar))//当前文本颜色
                .setWheelItemTextSize(14)//字体大小
                .build();

        mDialogAll.show(getSupportFragmentManager(), "ALL");
    }

    @Override
    public void callBackProjectHistoryAlarm(ResultBean<PagingBean<AlarmLogBean>, String> result) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        if (result.isSuccess() && (result.getData() == null || result.getData().getRows() == null ||
                result.getData().getRows().size() == 0)) {
            showNoDataView();
            return;
        } else {
            hideNoDataView();
        }
        try {
            //refreshLayout.finishLoadMore(true);
            //refreshLayout.finishRefresh(true);
            if (result.isSuccess()) {
                int queryStart = datalist.size();
                if (queryStart > 0) {
                    queryStart -= 1;
                }
                datalist.addAll(result.getData().getRows());
                pageSize++;
                totalCount = result.getData().getTotal();
                totalPage = result.getData().getTotalPageCount();
                adapter.notifyDataSetChanged();
                txtAlarmToatalCount.setText(totalCount + "个");
            } else {
                HelperView.Toast(this, result.getMessage());
            }
        } catch (Exception e) {
            HelperView.Toast(this, "历史报警失败:" + e.getMessage());
        }

        //hideProgressDig();
    }

    @Override
    public void callBackError(ResultBean<String, String> resultBean, String code) {
        super.callBackError(resultBean, code);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        // refreshLayout.finishLoadMore(true);
        //refreshLayout.finishRefresh(true);
    }
}
