package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.MultiItemTypeAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.ProjectRealDataBackBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.LabelNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IProjectRealAlarmPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl.NodeBasePersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.ui.NormalDividerItemDecoration;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * 项目实时报警数据详情页面
 */
public class ProjectRealAlarmActivity extends BaseActivity implements IProjectRealAlarmPersenter.IProjectRealAlarmView, View.OnClickListener {

    //报警数据缓存列表
    private List<LabelNodeBean> dataList;

    //实时报警列表
    private RecyclerView rvList;

    private CommonAdapter adapter;//
    //RealAlarmLabelAdapter

    //项目数据
    private ProjectNodeBean projectNodeBean;

    private IProjectRealAlarmPersenter persenter;

    //是否继续请求
    private boolean isRun = true;

    //是否暂停更新
    private boolean isPause = false;


    //距离上一次请求的时间
    private int lastRequestCount = 0;

    private ProgressDialog dialog;

    private String eventName = "";

    private int[] eventCount = new int[9];

    private TableLayout mTabLayout;

    private TextView mEventSum1;
    private TextView mEventSum2;
    private TextView mEventSum3;

    private TextView mEventSum4;
    private TextView mEventSum5;
    private TextView mEventSum6;

    private TextView mEventSum7;
    private TextView mEventSum8;
    private TextView mEventSum9;//txt_layout_event9


    private LinearLayout mEventLayout1;
    private LinearLayout mEventLayout2;
    private LinearLayout mEventLayout3;

    private LinearLayout mEventLayout4;
    private LinearLayout mEventLayout5;
    private LinearLayout mEventLayout6;

    private LinearLayout mEventLayout7;
    private LinearLayout mEventLayout8;
    private LinearLayout mEventLayout9;

    private String ProSysID = "";

    private boolean isFromRealData = false;

    private NestedScrollView mNestedView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_project_real_alarm);
        super.onCreate(savedInstanceState);
        setBarTitle("实时报警");
        Object queryObj = getIntent().getSerializableExtra("data");
        if (queryObj == null) {
            HelperView.Toast(this, "未找到项目！");
            return;
        }
        dialog = showProgress(this, "加载中...", false);
        eventName = getIntent().getStringExtra("EventName");
        mTabLayout = findViewById(R.id.real_alarm_tab_layout);

        View mSpace = findViewById(R.id.space_line);

        mEventSum1 = findViewById(R.id.txt_event1);
        mEventSum2 = findViewById(R.id.txt_event2);
        mEventSum3 = findViewById(R.id.txt_event3);

        mEventSum4 = findViewById(R.id.txt_event4);
        mEventSum5 = findViewById(R.id.txt_event5);
        mEventSum6 = findViewById(R.id.txt_event6);

        mEventSum7 = findViewById(R.id.txt_event7);
        mEventSum8 = findViewById(R.id.txt_event8);
        mEventSum9 = findViewById(R.id.txt_event9);


        mEventLayout1 = findViewById(R.id.txt_layout_event1);
        mEventLayout2 = findViewById(R.id.txt_layout_event2);
        mEventLayout3 = findViewById(R.id.txt_layout_event3);

        mEventLayout4 = findViewById(R.id.txt_layout_event4);
        mEventLayout5 = findViewById(R.id.txt_layout_event5);
        mEventLayout6 = findViewById(R.id.txt_layout_event6);

        mEventLayout7 = findViewById(R.id.txt_layout_event7);
        mEventLayout8 = findViewById(R.id.txt_layout_event8);
        mEventLayout9 = findViewById(R.id.txt_layout_event9);


        mEventLayout1.setOnClickListener(this);
        mEventLayout2.setOnClickListener(this);
        mEventLayout3.setOnClickListener(this);
        mEventLayout4.setOnClickListener(this);

        mEventLayout5.setOnClickListener(this);
        mEventLayout6.setOnClickListener(this);
        mEventLayout7.setOnClickListener(this);
        mEventLayout8.setOnClickListener(this);
        mEventLayout9.setOnClickListener(this);

        mEventLayout4.setVisibility(View.GONE);
        mEventLayout6.setVisibility(View.GONE);
        mEventLayout8.setVisibility(View.GONE);
        mEventLayout9.setVisibility(View.GONE);


        projectNodeBean = (ProjectNodeBean) queryObj;
        dataList = new ArrayList<>();
        persenter = NodeBasePersenterFactory.getProjectRealAlarmPersenter(this);
        addPersenter(persenter);
        rvList = findViewById(R.id.rv_list);

        mNestedView = findViewById(R.id.nested_scroll);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(manager);

        NormalDividerItemDecoration div = new NormalDividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        div.setDrawable(getResources().getDrawable(R.drawable.divider_shape));
        rvList.addItemDecoration(div);

//        adapter = new RealAlarmLabelAdapter(this, dataList);
//        rvList.setAdapter(adapter);

        adapter = new CommonAdapter<LabelNodeBean>(this, R.layout.item_real_alarm_list, dataList) {
            @Override
            protected void convert(ViewHolder holder, LabelNodeBean labelNodeBean, int position) {

                TextView mEventTypeName = holder.getView(R.id.event_type_name);
                TextView mEventTime = holder.getView(R.id.event_time);
                TextView mEventCurrentValue = holder.getView(R.id.event_current_value);
                TextView mEventAlarmValue = holder.getView(R.id.event_alarm_value);
                TextView mEventAlarmPoint = holder.getView(R.id.event_alarm_point);

                mEventTypeName.setText(labelNodeBean.getEventType().getName());
                mEventCurrentValue.setText(labelNodeBean.getTranslateValue());
                mEventTime.setText(labelNodeBean.getEventTimeStr());
                mEventAlarmValue.setText(labelNodeBean.getAlarmValue());
                mEventAlarmPoint.setText(labelNodeBean.getAreaName());
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
            }
        };
        rvList.setAdapter(adapter);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(ProjectRealAlarmActivity.this, ProjectRealAlarmDetailActivity.class);
                intent.putExtra("title", dataList.get(position).getAreaName());
                intent.putExtra("text1", dataList.get(position).getTranslateValue());
                intent.putExtra("text2", dataList.get(position).getAlarmValue());

                if (StringUtils.isEmpty(dataList.get(position).getAlalogOffset())) {
                    intent.putExtra("text3", "-");
                } else {
                    intent.putExtra("text3", dataList.get(position).getAlalogOffset());
                }

                if (StringUtils.isEmpty(dataList.get(position).getStandarLower()) || StringUtils.isEmpty(dataList.get(position).getStandardHight())) {
                    intent.putExtra("text4", "-");
                } else {
                    intent.putExtra("text4", String.valueOf(dataList.get(position).getStandarLower() + dataList.get(position).getUnit().getName() + "-"
                            + dataList.get(position).getStandardHight() + dataList.get(position).getUnit().getName()));
                }
                String eventTypeName = dataList.get(position).getEventType().getName();
                intent.putExtra("text5", eventTypeName);

                intent.putExtra("text6", dataList.get(position).getEventTimeStr());
                intent.putExtra("text7", dataList.get(position).getAlarmMessage());

                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        isFromRealData = getIntent().getBooleanExtra("isFromRealData", false);
        if (!isFromRealData) {
            persenter.doProjectRealAlarm(projectNodeBean.getID());
        }

        TextView historicalAlarm = findViewById(R.id.historical_title);
        historicalAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectRealAlarmActivity.this, ProjectHistoryAlarmActivity.class);
                intent.putExtra("data", projectNodeBean);
                startActivity(intent);
            }
        });

//        adapter.setOnItemClickListener(new RealAlarmLabelAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, RealAlarmLabelAdapter.BaseLabelViewHolder holder, int position) {
//
//                if (position < 0) {
//                    ToastUtils.showLong("未发现趋势记录!");
//                    return;
//                }
//
//                if (!"主机".equals(dataList.get(position).getOPCTagName()) && !"探头".equals(dataList.get(position).getOPCTagName())) {
//                    Intent intent = new Intent(ProjectRealAlarmActivity.this, LabelInfoRecordActivity.class);
//                    intent.putExtra("data", dataList.get(position));
//                    startActivity(intent);
//                } else if ("主机".equals(dataList.get(position).getOPCTagName()) && "1".equals(dataList.get(position).getUnitID())) {
//
//                    Intent intent = new Intent(ProjectRealAlarmActivity.this, FireAlarmHistoryInfoActivity.class);
//                    intent.putExtra("projectSystemID", dataList.get(position).getNodeTypeID());
//                    intent.putExtra("hostID", dataList.get(position).getBoolControlID());
//                    intent.putExtra("resource", "1");
//                    intent.putExtra("positionName", dataList.get(position).getAreaName());
//                    intent.putExtra("userID", dataList.get(position).getParentID());
//                    startActivity(intent);
//                } else if ("探头".equals(dataList.get(position).getOPCTagName()) && "2".equals(dataList.get(position).getUnitID())) {
//                    Intent intent = new Intent(ProjectRealAlarmActivity.this, FireAlarmHistoryInfoActivity.class);
//                    intent.putExtra("projectSystemID", dataList.get(position).getNodeTypeID());
//                    intent.putExtra("hostID", dataList.get(position).getBoolControlID());
//                    intent.putExtra("resource", "2");
//                    intent.putExtra("positionName", dataList.get(position).getAreaName());
//                    intent.putExtra("userID", dataList.get(position).getParentID());
//                    startActivity(intent);
//                }
//            }
//
//            @Override
//            public boolean onItemLongClick(View view, RealAlarmLabelAdapter.BaseLabelViewHolder holder, int position) {
//                return false;
//            }
//        });

        if (!StringUtils.isEmpty(eventName)) {
            mTabLayout.setVisibility(View.GONE);
            setBarTitle(eventName);

            mSpace.setVisibility(View.GONE);
        } else {
            mSpace.setVisibility(View.VISIBLE);
        }

        if (isFromRealData) {

            ProSysID = getIntent().getStringExtra("ProSysID");

            initDataTest();
        }

        mNestedView.setOnTouchListener(new View.OnTouchListener() {
            private int lastY = 0;
            private int touchEventId = -9983761;
            @SuppressLint("HandlerLeak")
            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    View scroller = (View) msg.obj;

                    if (msg.what == touchEventId) {
                        if (lastY == scroller.getScrollY()) {
                            //停止了，此处你的操作业务
                            isPause = false;
                        } else {
                            lastY = scroller.getScrollY();
                            handler.sendMessageDelayed(handler.obtainMessage(touchEventId, scroller), 5);
                        }
                    }
                }
            };


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventAction = event.getAction();
                switch (eventAction) {
                    case MotionEvent.ACTION_UP:
                        handler.sendMessageDelayed(handler.obtainMessage(touchEventId, v), 5);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        isPause = true;
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void initDataTest() {

        if (StringUtils.isEmpty(ProSysID)) {
            ToastUtils.showLong("暂无数据!");
            dialog.dismiss();
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("proID", projectNodeBean.getID());
        params.put("ProSysID", ProSysID);

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_GET_REAL_DATA_FOR_SYSTEM_LIST_ONE)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        ResultBean<List<LabelNodeBean>, String> result = new ResultBean<>();

                        ResultBean<ProjectRealDataBackBean, String> result1 = new Gson().fromJson(response,
                                new TypeToken<ResultBean<ProjectRealDataBackBean, String>>() {
                                }.getType());

                        result.setSuccess(result1.isSuccess());

                        result.setData(result1.getData().getLableList());

                        callBackProjectRealAlarm(result);
                    }
                });

    }

    @Override
    public void callBackError(ResultBean<String, String> resultBean, String action) {
        super.callBackError(resultBean, action);
        dialog.dismiss();
    }

    /**
     * 报警数据请求回调
     *
     * @param result
     */
    @Override
    public void callBackProjectRealAlarm(ResultBean<List<LabelNodeBean>, String> result) {

        if (result.isSuccess()) {
            final List<LabelNodeBean> queryList = result.getData();
            if (result.getData() == null || result.getData().size() == 0) {
                showNoDataView();
                mTabLayout.setVisibility(View.GONE);
                dialog.dismiss();
                return;
            } else {
                if (StringUtils.isEmpty(eventName)) {
                    mTabLayout.setVisibility(View.VISIBLE);
                }
                hideNoDataView();
            }

            dataList.clear();

            dataList.addAll(queryList);

            getEventCount();

            new UpdataThread().start();

            if (!isPause) {
                adapter.notifyDataSetChanged();
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            }, 2000);

        } else {
            dialog.dismiss();
            dataList.clear();
            adapter.notifyDataSetChanged();
            HelperView.Toast(this, "暂无数据,请稍后再试!");
        }
    }

    private void getEventCount() {
        if (StringUtils.isEmpty(eventName)) {
            eventCount[0] = dataList.size();

            for (int i = 0; i < eventCount.length; i++) {
                if (i > 0) {
                    eventCount[i] = 0;
                }
            }
            for (int i = 0; i < dataList.size(); i++) {
                switch (dataList.get(i).getEventType().getName()) {
                    case "报警事件":
                        eventCount[1]++;
                        break;
                    case "故障事件":
                        eventCount[2]++;
                        break;
                    case "恢复事件":
                        eventCount[3]++;
                        break;
                    case "运行事件":
                        eventCount[4]++;
                        break;
                    case "通讯事件":
                        eventCount[5]++;
                        break;


                    case "控制事件":
                        eventCount[6]++;
                        break;
                    case "无事件":
                        eventCount[7]++;
                        break;
                    case "其他":
                        eventCount[8]++;
                        break;
                }
            }
            mEventSum1.setText(String.valueOf(eventCount[0]));
            mEventSum2.setText(String.valueOf(eventCount[1]));
            mEventSum3.setText(String.valueOf(eventCount[2]));
            mEventSum4.setText(String.valueOf(eventCount[3]));

            mEventSum5.setText(String.valueOf(eventCount[4]));
            mEventSum6.setText(String.valueOf(eventCount[5]));
            mEventSum7.setText(String.valueOf(eventCount[6]));
            mEventSum8.setText(String.valueOf(eventCount[7]));
            mEventSum9.setText(String.valueOf(eventCount[8]));
        } else {

            if ("全部事件".equals(eventName)) {
                return;
            }
            // 获取某一种事件类型并显示
            List<LabelNodeBean> eventList = new ArrayList<>();

            for (int i = 0; i < dataList.size(); i++) {
                if (eventName.equals(dataList.get(i).getEventType().getName())) {
                    eventList.add(dataList.get(i));
                }
            }

            dataList.clear();

            dataList.addAll(eventList);

            if (dataList.size() == 0) {
                mTabLayout.setVisibility(View.GONE);
                showNoDataView();
            } else {
                hideNoDataView();
            }
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, ProjectRealAlarmActivity.class);
        intent.putExtra("data", projectNodeBean);
        intent.putExtra("isFromRealData", isFromRealData);
        intent.putExtra("ProSysID", ProSysID);
        switch (view.getId()) {
            case R.id.txt_layout_event1:
                intent.putExtra("EventName", "全部事件");
                break;
            case R.id.txt_layout_event2:
                intent.putExtra("EventName", "报警事件");
                break;
            case R.id.txt_layout_event3:
                intent.putExtra("EventName", "故障事件");
                break;

            case R.id.txt_layout_event4:
                intent.putExtra("EventName", "恢复事件");
                break;
            case R.id.txt_layout_event5:
                intent.putExtra("EventName", "运行事件");
                break;
            case R.id.txt_layout_event6:
                intent.putExtra("EventName", "通讯事件");
                break;

            case R.id.txt_layout_event7:
                intent.putExtra("EventName", "控制事件");
                break;
            case R.id.txt_layout_event8:
                intent.putExtra("EventName", "无事件");
                break;
            case R.id.txt_layout_event9:
                intent.putExtra("EventName", "其他");
                break;
        }
        startActivity(intent);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x128) {

                getEventCount();

                adapter.notifyDataSetChanged();
            }
        }
    };

    /**
     * 对比本次数据请求和上一次数据请求的差异
     *
     * @param newDatas
     */
    private void canstrastData(List<LabelNodeBean> newDatas) {
        List<LabelNodeBean> needRemove = new ArrayList<>();//需要移除的列表
        List<LabelNodeBean> needAdd = new ArrayList<>();//需要添加的列表
        for (int i = 0; i < newDatas.size(); i++) {//判断需要添加进去的并更新对象
            LabelNodeBean newObj = newDatas.get(i);
            boolean isExist = false;//是否存在
            for (int j = 0; j < dataList.size(); j++) {
                LabelNodeBean oldObj = newDatas.get(j);
                if (oldObj.getID().equals(newObj.getID())) {
                    isExist = true;
                    dataList.set(j, newObj);
                    continue;
                }
            }
            if (!isExist) {
                needAdd.add(newObj);
            }
        }
        for (int i = 0; i < dataList.size(); i++) {
            LabelNodeBean oldObj = dataList.get(i);
            boolean isExist = false;//是否存在
            for (int j = 0; j < newDatas.size(); j++) {
                LabelNodeBean newObj = newDatas.get(j);
                if (oldObj.getID().equals(newObj.getID())) {
                    isExist = true;
                    continue;
                }
            }
            if (!isExist) {
                needRemove.add(oldObj);
            }
        }
        //dataList.addAll(needAdd);
        //dataList.removeAll(needRemove);

        dataList.clear();

        dataList.addAll(newDatas);

        Log.i("实时报警", "实时报警请求完成");

    }


    private class UpdataThread extends Thread {
        @Override
        public void run() {
            try {
                if (isRun) {
                    while (lastRequestCount < 5000) {
                        if (!isPause) {
                            lastRequestCount += 300;
                        }
                        Thread.sleep(300);
                    }
                    lastRequestCount = 0;
                    Log.e(TAG, "实时数据请求");

                    if (!isFromRealData) {
                        persenter.doProjectRealAlarm(projectNodeBean.getID());
                    } else {
                        initDataTest();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRun = false;
    }
}
