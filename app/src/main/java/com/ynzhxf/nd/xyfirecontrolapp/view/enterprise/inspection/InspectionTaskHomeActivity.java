package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.MultiItemTypeAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectStateNumBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionCreateTaskBackBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionTaskHomeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionTaskHomeInputBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.inspection.ITaskInspectionHomePresenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.inspection.impl.InspectionTaskPresenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.ui.MyLoadMenuBackHeader;
import com.ynzhxf.nd.xyfirecontrolapp.ui.NormalDividerItemDecoration;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * 巡检任务首页
 */
public class InspectionTaskHomeActivity extends BaseActivity implements ITaskInspectionHomePresenter.ITaskInspectionHomeListView, OnDateSetListener {

    protected String projectId;

    protected ITaskInspectionHomePresenter presenter;

    protected RefreshLayout refreshLayout;

    protected RecyclerView mRecyclerView;

    protected CommonAdapter adapter;
    protected List<InspectionTaskHomeBean> homeBeanList = new ArrayList<>();

    protected InspectionTaskHomeInputBean inputBean;

    protected int currentPosition = 1;


    //时间选择控件初始时间
    public Calendar initStartTime = Calendar.getInstance();
    //时间选择控件结束时间
    public Calendar initEndTime = Calendar.getInstance();

    //开始时间毫秒
    public long startLongTime;

    //结束时间的毫秒数
    public long endLongTime;

    public int selectFlag = 0;

    protected TextView tv_start_time;
    protected TextView tv_end_time;

    protected LinearLayout mCreateTask;

    protected DrawerLayout mDrawerLayout;

    protected LinearLayout mRightMenu;

    protected List<InspectionCreateTaskBackBean> taskTypeList = new ArrayList<>();

    protected String selectedTypeId = "";
    protected String selectedState = "";

    protected ProgressDialog dialog;

    protected int isRefresh = 1;


    protected TextView mNumOne;
    protected TextView mNumTwo;
    protected TextView mNumThree;
    protected TextView mNumFour;

    protected LinearLayout mAllTask;
    protected LinearLayout mTaskOne;
    protected LinearLayout mTaskTwo;
    protected LinearLayout mTaskThree;

    protected View mUnderLine1;
    protected View mUnderLine2;
    protected View mUnderLine3;
    protected View mUnderLine4;

    protected List<View> mLineList = new ArrayList<>();

    protected int selectStatePosition = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.sliding_menu_inspect);
        super.onCreate(savedInstanceState);

        setBarTitle("巡检任务");

        refreshLayout = findViewById(R.id.inspection_home_refreshLayout);

        mRecyclerView = findViewById(R.id.inspection_home_recyclerView);

        tv_start_time = findViewById(R.id.inspection_home_left_time);
        tv_end_time = findViewById(R.id.inspection_home_right_time);

        mCreateTask = findViewById(R.id.inspection_home_create_tasks);

        // 各个状态数
        mNumOne = findViewById(R.id.task_home_num1);
        mNumTwo = findViewById(R.id.task_home_num2);
        mNumThree = findViewById(R.id.task_home_num3);
        mNumFour = findViewById(R.id.task_home_num4);

        // 点击布局
        mAllTask = findViewById(R.id.task_home_btn1);
        mTaskOne = findViewById(R.id.task_home_btn2);
        mTaskTwo = findViewById(R.id.task_home_btn3);
        mTaskThree = findViewById(R.id.task_home_btn4);

        mUnderLine1 = findViewById(R.id.task_home_line1);
        mUnderLine2 = findViewById(R.id.task_home_line2);
        mUnderLine3 = findViewById(R.id.task_home_line3);
        mUnderLine4 = findViewById(R.id.task_home_line4);
        mLineList.add(mUnderLine1);
        mLineList.add(mUnderLine2);
        mLineList.add(mUnderLine3);
        mLineList.add(mUnderLine4);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        NormalDividerItemDecoration div = new NormalDividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        div.setDrawable(getResources().getDrawable(R.drawable.divider_shape_inspection));
        mRecyclerView.addItemDecoration(div);

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        MyLoadMenuBackHeader myLoadHeader = new MyLoadMenuBackHeader(this);
        refreshLayout.setRefreshHeader(myLoadHeader);

        projectId = getIntent().getStringExtra("projectId");

        SPUtils.getInstance().put("updateHomeItem", false);

        dialog = showProgress(this, "加载中,请稍后...", false);

        initRefreshListener();

        initSelectTime();

        doRequestData();

        initOnClick();

        initData();

        initDrawLayout();

        initTabLayout();
    }

    protected void initInspectStateNum() {
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("projectId", projectId);
        params.put("startTime", HelperTool.MillTimeToStringDate(startLongTime));
        params.put("endTime", HelperTool.MillTimeToStringDate(endLongTime));
        params.put("inspectTypeId", selectedTypeId);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_INSPECT_HOME_STATE_NUM_FOR_OWNER))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("输出巡检首页个状态shuliang1212---", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (!jsonObject.getBoolean("success")) {
                                return;
                            }

                            List<InspectStateNumBean> stateNumBeanList = new Gson().fromJson(
                                    jsonObject.getJSONArray("data").toString(), new TypeToken<List<InspectStateNumBean>>() {
                                    }.getType()
                            );
                            if (stateNumBeanList != null && stateNumBeanList.size() > 0) {
                                for (int i = 0; i < stateNumBeanList.size(); i++) {
                                    switch (stateNumBeanList.get(i).getInspectTaskState()) {
                                        case -1:
                                            mNumOne.setText(String.valueOf(stateNumBeanList.get(i).getCount()));
                                            break;
                                        case 0:
                                            mNumTwo.setText(String.valueOf(stateNumBeanList.get(i).getCount()));
                                            break;
                                        case 10:
                                            mNumThree.setText(String.valueOf(stateNumBeanList.get(i).getCount()));
                                            break;
                                        case 20:
                                            mNumFour.setText(String.valueOf(stateNumBeanList.get(i).getCount()));
                                            break;
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    protected void initTabLayout() {

        mUnderLine1.setVisibility(View.VISIBLE);
        setUnSelectedLine(0);

        mAllTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectStatePosition != 1) {
                    dialog.show();
                    mUnderLine1.setVisibility(View.VISIBLE);
                    setUnSelectedLine(0);
                    setSelected("");
                    selectStatePosition = 1;
                }
            }
        });

        mTaskOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectStatePosition != 2) {
                    dialog.show();
                    mUnderLine2.setVisibility(View.VISIBLE);
                    setUnSelectedLine(1);
                    setSelected("0");
                    selectStatePosition = 2;
                }
            }
        });

        mTaskTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectStatePosition != 3) {
                    dialog.show();
                    mUnderLine3.setVisibility(View.VISIBLE);
                    setUnSelectedLine(2);
                    setSelected("10");
                    selectStatePosition = 3;
                }
            }
        });

        mTaskThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectStatePosition != 4) {
                    dialog.show();
                    mUnderLine4.setVisibility(View.VISIBLE);
                    setUnSelectedLine(3);
                    setSelected("20");
                    selectStatePosition = 4;
                }
            }
        });
    }

    protected void setSelected(String state) {
        selectedState = state;
        currentPosition = 1;
        inputBean.setState(selectedState);
        inputBean.setToken(HelperTool.getToken());
        inputBean.setProjectId(projectId);
        inputBean.setPageSize("20");
        currentPosition = 1;
        inputBean.setPageIndex(String.valueOf(currentPosition));

        homeBeanList.clear();

        presenter.doTaskInspectionHomeList(inputBean);
    }

    protected void setUnSelectedLine(int position) {
        for (int i = 0; i < mLineList.size(); i++) {
            if (i == position) {
                continue;
            }
            mLineList.get(i).setVisibility(View.INVISIBLE);
        }
    }

    protected void initDrawLayout() {

        TextView selectButton = findViewById(R.id.inspection_home_screen);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(mRightMenu);
            }
        });

        mDrawerLayout = findViewById(R.id.inspect_layout);
        mDrawerLayout.addDrawerListener(mSimpleDrawerListener);
        mRightMenu = findViewById(R.id.inspect_right_menu);

        Button resetButton = findViewById(R.id.sliding_reset);

        Button confirmButton = findViewById(R.id.sliding_confirm);

        final CheckBox typeBox1 = findViewById(R.id.sliding_type1);
        final CheckBox typeBox2 = findViewById(R.id.sliding_type2);
        final CheckBox stateBox1 = findViewById(R.id.sliding_state1);
        final CheckBox stateBox2 = findViewById(R.id.sliding_state2);
        final CheckBox stateBox3 = findViewById(R.id.sliding_state3);

        initTaskType(typeBox1, typeBox2);

        typeBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    typeBox2.setChecked(false);
                    selectedTypeId = taskTypeList.get(0).getID();
                }
            }
        });
        typeBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    typeBox1.setChecked(false);
                    selectedTypeId = taskTypeList.get(1).getID();
                }
            }
        });
        stateBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    selectedState = "0";
                    stateBox2.setChecked(false);
                    stateBox3.setChecked(false);
                }
            }
        });
        stateBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    selectedState = "10";
                    stateBox1.setChecked(false);
                    stateBox3.setChecked(false);
                }
            }
        });
        stateBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    selectedState = "20";
                    stateBox1.setChecked(false);
                    stateBox2.setChecked(false);
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeBox1.setChecked(false);
                typeBox2.setChecked(false);

                stateBox1.setChecked(false);
                stateBox2.setChecked(false);
                stateBox3.setChecked(false);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawer(mRightMenu);

                if (!typeBox1.isChecked() && !typeBox2.isChecked()) {
                    selectedTypeId = "";
                    inputBean.setInspectTypeId("");
                } else {
                    inputBean.setInspectTypeId(selectedTypeId);
                }
//                if (!stateBox1.isChecked() && !stateBox2.isChecked() && !stateBox3.isChecked()) {
//                    selectedState = "";
//                    inputBean.setState("");
//                } else {
                inputBean.setState(selectedState);
//                }

                inputBean.setToken(HelperTool.getToken());
                inputBean.setProjectId(projectId);
                inputBean.setPageSize("20");
                currentPosition = 1;
                inputBean.setPageIndex(String.valueOf(currentPosition));

                homeBeanList.clear();

                presenter.doTaskInspectionHomeList(inputBean);

                initInspectStateNum();

                //LogUtils.showLoge("输入参数1515---", inputBean.toString());
            }
        });

    }

    protected DrawerLayout.SimpleDrawerListener mSimpleDrawerListener = new DrawerLayout.SimpleDrawerListener() {
        @Override
        public void onDrawerOpened(View drawerView) {
            //档DrawerLayout打开时，让整体DrawerLayout布局可以响应点击事件
            drawerView.setClickable(true);
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
        }
    };

    protected void initTaskType(final CheckBox typeBox1, final CheckBox typeBox2) {

        HashMap<String, String> params = new HashMap<>();

        params.put("Token", HelperTool.getToken());

        final ProgressDialog dialog = showProgress(this, "加载中,请稍后...", false);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_INSPECTION_GET_TASK_TYPE)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        //LogUtils.showLogd("获取自定义任务类型1515---", e.getMessage());
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        // LogUtils.showLoge("获取自定义任务类型1212---", response);
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.getBoolean("success")) {

                                final List<InspectionCreateTaskBackBean> beanList = new Gson().fromJson(json.getJSONArray("data").toString(),
                                        new TypeToken<List<InspectionCreateTaskBackBean>>() {
                                        }.getType());

                                if (beanList == null || beanList.size() == 0) {
                                    HelperView.Toast(InspectionTaskHomeActivity.this, "未获取到任务类型!");
                                    return;
                                }

                                taskTypeList.addAll(beanList);

                                if (taskTypeList.size() < 2) {
                                    return;
                                }

                                typeBox1.setText(taskTypeList.get(0).getName());

                                typeBox2.setText(taskTypeList.get(1).getName());

                            } else {
                                HelperView.Toast(InspectionTaskHomeActivity.this, json.getString("message"));
                            }
                        } catch (Exception e) {
                            HelperView.Toast(InspectionTaskHomeActivity.this, e.getMessage());
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDrawerLayout.removeDrawerListener(mSimpleDrawerListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SPUtils.getInstance().getBoolean("updateHomeItem")) {
            inputBean.setToken(HelperTool.getToken());
            inputBean.setState(selectedState);
            inputBean.setProjectId(projectId);
            inputBean.setPageSize(String.valueOf(homeBeanList.size()));
            currentPosition = 1;
            inputBean.setPageIndex(String.valueOf(currentPosition));

            inputBean.setInspectTypeId(selectedTypeId);
            inputBean.setStartTime(HelperTool.MillTimeToStringDate(startLongTime));
            inputBean.setEndTime(HelperTool.MillTimeToStringDate(endLongTime));

            presenter.doTaskInspectionHomeList(inputBean);

            //LogUtils.showLoge("获取数据1212---", "1212");

            SPUtils.getInstance().put("updateHomeItem", false);
        }

        initInspectStateNum();
    }

    protected void doRequestData() {
        presenter = InspectionTaskPresenterFactory.getTaskInspectionHomePresenterImpl(this);

        inputBean = new InspectionTaskHomeInputBean();

        inputBean.setToken(HelperTool.getToken());
        inputBean.setState(selectedState);
        inputBean.setProjectId(projectId);
        inputBean.setPageSize("20");
        currentPosition = 1;
        inputBean.setPageIndex(String.valueOf(currentPosition));
        inputBean.setInspectTypeId(selectedTypeId);
        inputBean.setStartTime(HelperTool.MillTimeToStringDate(startLongTime));
        inputBean.setEndTime(HelperTool.MillTimeToStringDate(endLongTime));

        //LogUtils.showLoge("输出业主巡检首页列表参数1212---", inputBean.toString());

        presenter.doTaskInspectionHomeList(inputBean);
    }

    protected void initData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("nodeId", projectId);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE + URLConstant.URL_INSPECTION_GET_CREATE_TASK_STATE)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        LogUtils.showLoge("是否显示自定义创建任务1212---", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.getBoolean("success")) {

                                JSONObject data = json.getJSONObject("data");

                                int isShow = data.getInt("FireInspectUserType");
                                if (isShow == 2 || isShow == 3) {
                                    mCreateTask.setVisibility(View.VISIBLE);
                                }

                            } else {
                                HelperView.Toast(InspectionTaskHomeActivity.this, json.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void initRefreshListener() {

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                currentPosition = 1;
                inputBean.setPageIndex(String.valueOf(currentPosition));
                //inputBean.setState("");
                homeBeanList.clear();
                presenter.doTaskInspectionHomeList(inputBean);
                isRefresh = 2;
                //refreshLayout.finishRefresh(2000);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                currentPosition++;
                inputBean.setPageIndex(String.valueOf(currentPosition));
                //inputBean.setState("");
                inputBean.setPageSize("20");
                presenter.doTaskInspectionHomeList(inputBean);
                isRefresh = 3;
                //refreshLayout.finishLoadMore(2000);
            }
        });
    }

    public void initSelectTime() {
        initStartTime.add(Calendar.YEAR, -1);
        initEndTime.add(Calendar.YEAR, 1);
        Calendar queryTime = Calendar.getInstance();
        endLongTime = queryTime.getTimeInMillis();
        queryTime.add(Calendar.YEAR, -1);
        startLongTime = queryTime.getTimeInMillis();
        tv_start_time.setText(HelperTool.MillTimeToStringDate(startLongTime).concat(" "));
        tv_end_time.setText(HelperTool.MillTimeToStringDate(endLongTime).concat(" "));
    }

    public void initOnClick() {
        tv_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFlag = 0;
                showTimeSelect();
            }
        });
        tv_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFlag = 1;
                showTimeSelect();
            }
        });

        mCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InspectionTaskHomeActivity.this, InspectionTypeCreateTaskActivity.class);

                intent.putExtra("projectId", projectId);

                startActivityForResult(intent, 20);
            }
        });

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
    public void callBackError(ResultBean<String, String> resultBean, String action) {
        super.callBackError(resultBean, action);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void callBackTaskInspectionHomeList(ResultBean<List<InspectionTaskHomeBean>, String> resultBean) {

        initDialogShow();

        if (resultBean == null || resultBean.getData() == null || resultBean.getData().size() == 0) {
            //HelperView.Toast(this, "暂无巡检任务!");
            if (homeBeanList.size() == 0) {
                showNoDataView();
            } else {
                hideNoDataView();
            }
            if (adapter != null && currentPosition == 1) {
                adapter.notifyDataSetChanged();
            }
            return;
        } else {
            hideNoDataView();
        }

        //LogUtils.showLoge("筛选返回数量1212---", String.valueOf("~~~~" + resultBean.getData().size()));

        if (currentPosition == 1) {
            homeBeanList.clear();
        }

        homeBeanList.addAll(resultBean.getData());

        if (adapter != null) {
            adapter.notifyDataSetChanged();
            return;
        }

        adapter = new CommonAdapter<InspectionTaskHomeBean>(this, R.layout.item_inspection_task_list, homeBeanList) {
            @Override
            protected void convert(ViewHolder holder, final InspectionTaskHomeBean homeBean, final int position) {
                TextView title = holder.getView(R.id.inspection_home_title);
                TextView stateName = holder.getView(R.id.inspection_home_state);
                TextView headName = holder.getView(R.id.inspection_home_head_name);
                TextView startTime = holder.getView(R.id.inspection_home_start_time);
                TextView endTime = holder.getView(R.id.inspection_home_end_time);

                TextView mInspectCount = holder.getView(R.id.inspect_count);

                Button mResult = holder.getView(R.id.item_state_result);

                Button setPerson = holder.getView(R.id.inspection_assigned_task_button);

                Button assignedTask = holder.getView(R.id.inspection_view_task_button);

                Button viewHistory = holder.getView(R.id.inspection_home_task_button);

                if (!StringUtils.isEmpty(homeBean.getStateCount())) {
                    mInspectCount.setText(homeBean.getStateCount());
                }

                title.setText(homeBean.getName());

                stateName.setText(homeBean.getStateShow());

                headName.setText(String.valueOf("负责人:" + homeBean.getChargeManName()));

                startTime.setText(String.valueOf("开始时间:" + homeBean.getStartTimeShow()));

                endTime.setText(String.valueOf("截止时间:" + homeBean.getEndTimeShow()));

                // 定义是否显示负责人或者显示维保公司
                initInspectCompany(headName, homeBean);
                // 查看记录
                viewHistory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Intent intent = new Intent(InspectionTaskHomeActivity.this, InspectionRecordsActivity.class);
//
//                        intent.putExtra("taskId", homeBean.getID());
//
//                        intent.putExtra("bean", homeBean);
//
//                        startActivity(intent);

                        goToInspectRecords(homeBean);
                    }
                });
                // 设置负责人
                setPerson.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(InspectionTaskHomeActivity.this, InspectionResponsiblePersonActivity.class);

                        intent.putExtra("position", position);

                        intent.putExtra("taskId", homeBean.getID());

                        startActivityForResult(intent, 25);
                    }
                });
                // 分配任务
                assignedTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(InspectionTaskHomeActivity.this, InspectionAssignedTasksActivity.class);

                        intent.putExtra("projectId", homeBean.getProjectId());

                        intent.putExtra("taskId", homeBean.getID());

                        startActivity(intent);
                    }
                });

                if (homeBean.getCurrentUserType() == 2) {
                    assignedTask.setVisibility(View.VISIBLE);
                } else if (homeBean.getCurrentUserType() == 3) {
                    assignedTask.setVisibility(View.VISIBLE);
                    setPerson.setVisibility(View.VISIBLE);
                } else {
                    assignedTask.setVisibility(View.INVISIBLE);
                    setPerson.setVisibility(View.INVISIBLE);
                }

                if (homeBean.getState() == 10) {
                    assignedTask.setVisibility(View.INVISIBLE);
                    setPerson.setVisibility(View.INVISIBLE);
                    mResult.setVisibility(View.VISIBLE);
                    stateName.setTextColor(getResources().getColor(R.color.inspection_list_btn));
                    if (homeBean.getResult() == 0) {
                        mResult.setText(homeBean.getResultShow());//异常
                        mResult.setBackground(getResources().getDrawable(R.drawable.inspection_item_btn_one));
                    } else if (homeBean.getResult() == 1) {
                        mResult.setText(homeBean.getResultShow());//正常
                        mResult.setBackground(getResources().getDrawable(R.drawable.inspection_item_btn));
                    }
                } else if (homeBean.getState() == 0) {
                    mResult.setVisibility(View.INVISIBLE);
                    stateName.setTextColor(getResources().getColor(R.color.fire_fire));
                } else if (homeBean.getState() == 20) {
                    mResult.setVisibility(View.INVISIBLE);
                    stateName.setTextColor(getResources().getColor(R.color.global_button_stroke_color));
                }
            }
        };

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, final int position) {

                InspectionTaskHomeBean homeBean = homeBeanList.get(position);

                if (homeBean.getState() == 10 || homeBean.getState() == 20) {
                    goToInspectRecords(homeBean);
                } else {
                    // String[] permissions = new String[]{Manifest.permission.CAMERA};
                    // 使用新的权限请求框架 更稳定兼容性更好
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        AndPermission.with(InspectionTaskHomeActivity.this)
                                .runtime()
                                .permission(Permission.CAMERA)
                                .onGranted(new Action<List<String>>() {
                                    @Override
                                    public void onAction(List<String> data) {
                                        goToInspectItemList(position);
                                    }
                                })
                                .onDenied(new Action<List<String>>() {
                                    @Override
                                    public void onAction(List<String> data) {
                                        ToastUtils.showLong("权限被拒绝,巡检功能无法使用!");
                                    }
                                })
                                .start();
                    } else {
                        goToInspectItemList(position);
                    }


                }


//                if (EasyPermissions.hasPermissions(InspectionTaskHomeActivity.this, permissions)) {
//
//                    Intent intent = new Intent(InspectionTaskHomeActivity.this, InspectionItemListActivity.class);
//
//                    intent.putExtra("projectId", homeBeanList.get(position).getProjectId());
//
//                    intent.putExtra("inspectionTypeId", homeBeanList.get(position).getInspectTypeId());
//
//                    intent.putExtra("taskId", homeBeanList.get(position).getID());
//
//                    intent.putExtra("state",homeBeanList.get(position).getState());
//
//                    LogUtils.showLoge("打印状态1010---", String.valueOf(homeBeanList.get(position).getState()));
//
//                    startActivity(intent);
//                } else {
//                    requestPermissionsCallBack(InspectionTaskHomeActivity.this, "请求相机权限!", 31, permissions, new PermissionsUtil.IGrantCallBack() {
//                        @Override
//                        public void result(boolean Success, int requestCode) {
//                            if (Success && requestCode == 31) {
//                                Intent intent = new Intent(InspectionTaskHomeActivity.this, InspectionItemListActivity.class);
//
//                                intent.putExtra("projectId", homeBeanList.get(position).getProjectId());
//
//                                intent.putExtra("inspectionTypeId", homeBeanList.get(position).getInspectTypeId());
//
//                                intent.putExtra("taskId", homeBeanList.get(position).getID());
//
//                                intent.putExtra("state",homeBeanList.get(position).getState());
//                                LogUtils.showLoge("打印状态1111---", String.valueOf(homeBeanList.get(position).getState()));
//                                startActivity(intent);
//                            }
//                        }
//                    });
//                }


            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        mRecyclerView.setAdapter(adapter);
    }

    protected void initInspectCompany(TextView headName, InspectionTaskHomeBean homeBean) {
    }

    protected void initDialogShow() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        if (isRefresh == 2) {
            refreshLayout.finishRefresh();
        } else if (isRefresh == 3) {
            refreshLayout.finishLoadMore();
        }
    }

    /**
     * 跳转到巡检记录
     *
     * @param homeBean
     */
    protected void goToInspectRecords(InspectionTaskHomeBean homeBean) {
        Intent intent = new Intent(InspectionTaskHomeActivity.this, InspectionRecordsActivity.class);

        intent.putExtra("taskId", homeBean.getID());

        intent.putExtra("bean", homeBean);

        //intent.putExtra("isCompany", true);

        startActivity(intent);
    }

    /**
     * 跳转到巡检项列表
     *
     * @param position
     */
    protected void goToInspectItemList(int position) {
        Intent intent = new Intent(InspectionTaskHomeActivity.this, InspectionItemListActivity.class);

        intent.putExtra("projectId", homeBeanList.get(position).getProjectId());

        intent.putExtra("inspectionTypeId", homeBeanList.get(position).getInspectTypeId());

        intent.putExtra("taskId", homeBeanList.get(position).getID());

        intent.putExtra("state", homeBeanList.get(position).getState());

        //LogUtils.showLoge("打印状态1010---", String.valueOf(homeBeanList.get(position).getState()));

        startActivity(intent);
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
        tv_start_time.setText(HelperTool.MillTimeToStringDate(startLongTime).concat(" "));
        tv_end_time.setText(HelperTool.MillTimeToStringDate(endLongTime).concat(" "));

        /*LogUtils.showLoge("输出选择时间1212---", HelperTool.MillTimeToStringDate(startLongTime).concat(" ") + "~~~~"
                + HelperTool.MillTimeToStringDate(endLongTime).concat(" "));*/
        dialog.show();

        isRefresh = 1;

        inputBean.setStartTime(HelperTool.MillTimeToStringDate(startLongTime));
        inputBean.setEndTime(HelperTool.MillTimeToStringDate(endLongTime));
        currentPosition = 1;
        inputBean.setPageIndex(String.valueOf(currentPosition));
        homeBeanList.clear();
        presenter.doTaskInspectionHomeList(inputBean);

        initInspectStateNum();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 20:
                    homeBeanList.clear();
                    currentPosition = 1;

                    // 当创建完任务后返回刷新列表时需要获取当前时间筛选 否则可能无法刷新出最新的一条
                    Calendar queryTime = Calendar.getInstance();
                    queryTime.add(Calendar.MINUTE, 2);
                    endLongTime = queryTime.getTimeInMillis();
                    inputBean.setEndTime(HelperTool.MillTimeToStringDate(endLongTime));
                    inputBean.setPageIndex(String.valueOf(currentPosition));

                    presenter.doTaskInspectionHomeList(inputBean);
                    break;
                case 25:
                    if (homeBeanList.size() > 0) {
                        InspectionTaskHomeBean bean = homeBeanList.get(data.getIntExtra("position", 0));
                        bean.setChargeManName(data.getStringExtra("Name"));
                    }
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}
