package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.StatisticsListErrorInfoBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;


/**
 * author hbzhou
 * date 2019/1/30 13:29
 */
public class ProjectStatisticsListErrorInfo extends BaseActivity {


    protected String baseNodeID;

    protected String areaName;

    protected String eventTypeID;

    protected ProgressDialog dialog;

    protected String eventTypeName = "";

    protected RecyclerView mRecyclerView;

    protected RefreshLayout refreshLayout;

    protected int position = 1;

    protected List<StatisticsListErrorInfoBean> errorBeanList = new ArrayList<>();

    protected TextView mHistoryTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_project_statistics_error_info);
        super.onCreate(savedInstanceState);
        setBarTitle("最多故障点");

        mRecyclerView = findViewById(R.id.recyclerView);

        refreshLayout = findViewById(R.id.refreshLayout);

        mHistoryTitle = findViewById(R.id.alarm_history_title);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        MyLoadHeader myLoadHeader = new MyLoadHeader(this);
//        refreshLayout.setRefreshHeader(myLoadHeader);

        refreshLayout.setEnableAutoLoadMore(true);

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

                position++;

                //dialog.show();
                //LogUtils.showLoge("上拉加载1212---", "1313");
                initDataInfo();

            }
        });


        baseNodeID = getIntent().getStringExtra("ID");

        areaName = getIntent().getStringExtra("Name");

        eventTypeID = getIntent().getStringExtra("EventId");

        if (!StringUtils.isEmpty(areaName)) {
            mHistoryTitle.setText(areaName);
        } else {
            mHistoryTitle.setText("故障点");
        }

        dialog = showProgress(this, "加载中...", false);

        initDataForDetailsError();

        initDataInfo();
    }

    protected void initDataForDetailsError() {
    }

    protected void initDataInfo() {

        if (StringUtils.isEmpty(baseNodeID) || StringUtils.isEmpty(areaName)) {
            dialog.dismiss();
            ToastUtils.showLong("未获取到详情信息");
            showNoDataView();
            refreshLayout.finishLoadMore();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("baseNodeID", baseNodeID);
        params.put("areaName", areaName);
        params.put("eventTypeID", "");
        params.put("nowPage", String.valueOf(position));

        LogUtils.showLoge("baseNodeID", baseNodeID + "areaName" + areaName);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_GET_STATISTICS_ALARM_ALL_ERROR)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.dismiss();
                        refreshLayout.finishLoadMore();
                        LogUtils.showLoge("获取统计详情点击详情0000---", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dialog.dismiss();
                        LogUtils.showLoge("获取统计详情点击详情5658---", response);

                        com.blankj.utilcode.util.LogUtils.json(response);

                        refreshLayout.finishLoadMore();


                        try {
                            JSONObject jsonObject = new JSONObject(response);
//                            if (jsonObject.getBoolean("success")) {

                            LogUtils.showLoge("extra1300---", jsonObject.getString("extra"));

                            JSONObject json = jsonObject.getJSONObject("data");

                            List<StatisticsListErrorInfoBean> errorInfoList = new Gson().fromJson(json.getJSONArray("rows").toString(),
                                    new TypeToken<List<StatisticsListErrorInfoBean>>() {
                                    }.getType());

                            if (JSONObject.NULL != jsonObject.get("extra")) {
                                List<List<String>> extraList = new Gson().fromJson(jsonObject.getJSONArray("extra").toString(),
                                        new TypeToken<List<List<String>>>() {
                                        }.getType());
                                if (extraList != null && extraList.size() > 0) {
                                    if (!StringUtils.isEmpty(extraList.get(0).get(3))) {
                                        eventTypeName = extraList.get(0).get(3);
                                    }
                                }

                                initEventCount(extraList);
                            }

                            if (errorBeanList.size() == 0 && (errorInfoList == null || errorInfoList.size() == 0)) {
                                showNoDataView();
                                return;
                            } else {
                                hideNoDataView();
                            }
                            if (errorBeanList.size() > 0 && (errorInfoList == null || errorInfoList.size() == 0)) {
                                ToastUtils.showLong("暂无更多记录!");
                            }

                            errorBeanList.addAll(errorInfoList);
                            CommonAdapter adapter = new CommonAdapter<StatisticsListErrorInfoBean>(ProjectStatisticsListErrorInfo.this, R.layout.item_statistics_error_info, errorBeanList) {
                                @Override
                                protected void convert(ViewHolder holder, StatisticsListErrorInfoBean errorInfoBean, int position) {
                                    TextView mItemTime1 = holder.getView(R.id.error_time1);
                                    TextView mItemTime2 = holder.getView(R.id.error_time2);

                                    TextView mItemName = holder.getView(R.id.error_name);

                                    TextView mItemType = holder.getView(R.id.error_type);

                                    TextView mItemMessage = holder.getView(R.id.error_message);

                                    TextView mItemActionType = holder.getView(R.id.error_action_type);

                                    LinearLayout mLine = holder.getView(R.id.line1);

                                    if (position == 0) {
                                        mLine.setVisibility(View.INVISIBLE);
                                    } else {
                                        mLine.setVisibility(View.VISIBLE);
                                    }

                                    if ("正常".equals(errorInfoBean.getAlarmValue())) {
                                        mItemName.setTextColor(Color.parseColor("#16A085"));
                                    } else {
                                        mItemName.setTextColor(Color.parseColor("#E74C3C"));
                                    }

                                    mItemName.setText(errorInfoBean.getAlarmValue());


                                    mItemType.setText(String.valueOf("(" + errorInfoBean.getEventType().getName() + ")"));

                                    mItemMessage.setText(String.valueOf("提示:" + errorInfoBean.getMessage()));

                                    mItemActionType.setText(errorInfoBean.getReviewStateStr());


                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

                                    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd~HH:mm:ss", Locale.CHINA);

                                    try {
                                        Date date = formatter.parse(errorInfoBean.getEventTimeStr());

                                        String timeStr = formatter1.format(date);

                                        if (!StringUtils.isEmpty(timeStr) && timeStr.contains("~")) {
                                            String[] strings = timeStr.split("~");

                                            if (strings.length >= 2) {
                                                mItemTime1.setText(strings[0]);
                                                mItemTime2.setText(strings[1]);
                                            }
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }
                            };

                            mRecyclerView.setAdapter(adapter);

//                            } else {
//                                ToastUtils.showLong(jsonObject.getString("message"));
//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            ToastUtils.showLong(e.getMessage());
                        }

                    }
                });
    }

    protected void initEventCount(List<List<String>> extraList) {

    }
}
