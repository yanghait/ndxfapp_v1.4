package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;


import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jzxiang.pickerview.TimePickerDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.FireMaintanceListBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
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
 * author hbzhou
 * date 2019/1/30 17:52
 */
public class ProjectInfoMessageDataActivity extends FireAlarmHistoryInfoActivity {

    private String ID;

    private TextView mCount1;

    private TextView mCount2;
    private TextView mCount3;
    private TextView mCount4;

    private String name = "";

    private int labelType;

    private int position = 1;

    private List<FireMaintanceListBean> fireBeansList = new ArrayList<>();

    private boolean isFromAlarmPort = false;

    private TextView mNameCount1;

    private TextView mNameCount2;
    private TextView mNameCount3;
    private TextView mNameCount4;

    @Override
    protected void initImProjectInfo() {

        LinearLayout mHistoryLayout = findViewById(R.id.history_layout);
        mHistoryLayout.setVisibility(View.GONE);

        LinearLayout mImLayout = findViewById(R.id.im_layout);

        mImLayout.setVisibility(View.VISIBLE);

        TextView mTitle = findViewById(R.id.im_title);

        mCount1 = findViewById(R.id.im_count1);
        mCount2 = findViewById(R.id.im_count2);
        mCount3 = findViewById(R.id.im_count3);
        mCount4 = findViewById(R.id.im_count4);

        mNameCount1 = findViewById(R.id.im_name_count1);
        mNameCount2 = findViewById(R.id.im_name_count2);
        mNameCount3 = findViewById(R.id.im_name_count3);
        mNameCount4 = findViewById(R.id.im_name_count4);

        //initData();

        ID = getIntent().getStringExtra("ID");

        name = getIntent().getStringExtra("Name");

        labelType = getIntent().getIntExtra("labelType", 1);

        isFromAlarmPort = getIntent().getBooleanExtra("isFromAlarmPort", false);

        switch (labelType) {
            case 1:
                setBarTitle("消防接管");
                mNameCount2.setText("已接管");
                mNameCount1.setText("未接管");
                break;
            case 2:
                setBarTitle("通讯状态");
                mNameCount1.setText("通讯正常");
                mNameCount2.setText("通讯异常");
                break;
            case 3:
                setBarTitle("维保状态");
                mNameCount2.setText("未维修");
                mNameCount1.setText("维修中");
                break;
            case 4:
                mImLayout.setVisibility(View.GONE);
                mHistoryLayout.setVisibility(View.VISIBLE);

                TextView mTitleName = findViewById(R.id.alarm_history_title);
                mTitleName.setText(name);
                setBarTitle("串口连接状态");
                mNameCount1.setText("正常");
                mNameCount2.setText("异常");
                break;
        }

        mTitle.setText(name);

        doRequestDataInfo();

    }

    @Override
    public void initSelectTime() {

        initStartTime.add(Calendar.DAY_OF_MONTH, -31);
        //initEndTime.add(Calendar.YEAR, 1);

        Calendar queryTime = Calendar.getInstance();
        endLongTime = queryTime.getTimeInMillis();
        queryTime.add(Calendar.DAY_OF_MONTH, -7);
        startLongTime = queryTime.getTimeInMillis();
        mStartTime.setText(HelperTool.MillTimeToStringDate(startLongTime).concat(" "));
        mEndTime.setText(HelperTool.MillTimeToStringDate(endLongTime).concat(" "));

        startTime = HelperTool.MillTimeToStringDateDetail(startLongTime);
        endTime = HelperTool.MillTimeToStringDateDetail(endLongTime);
    }

    @Override
    protected void initRefreshLayout() {
        //
        refreshLayout = findViewById(R.id.refreshLayout);

        refreshLayout.setEnableAutoLoadMore(true);

        refreshLayout.setEnableLoadMore(true);

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

                position++;

                doRequestDataInfo();

            }
        });
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
        mStartTime.setText(HelperTool.MillTimeToStringDate(startLongTime).concat(" "));
        mEndTime.setText(HelperTool.MillTimeToStringDate(endLongTime).concat(" "));

        dialog.show();

        //LogUtils.showLoge("输出输入参数信息1212---", HelperTool.MillTimeToStringDateDetail(startLongTime) + "~~~" + HelperTool.MillTimeToStringDateDetail(endLongTime));

        startTime = HelperTool.MillTimeToStringDateDetail(startLongTime);

        endTime = HelperTool.MillTimeToStringDateDetail(endLongTime);

        position = 1;

        fireBeansList.clear();

        doRequestDataInfo();

    }

    protected void doRequestDataInfo() {
        HashMap<String, String> params = new HashMap<>();

        String url = URLConstant.URL_BASE1 + URLConstant.URL_GET_ALL_STATE_INFO;

        if (isFromAlarmPort) {
            url = URLConstant.URL_BASE1 + URLConstant.URL_GET_ALL_PORT_STATE_INFO;
            params.put("projectSystemID", ID);
        } else {
            params.put("projectID", ID);
            params.put("labelType", String.valueOf(labelType));
        }

        params.put("Token", HelperTool.getToken());
        params.put("nowPage", String.valueOf(position));
        params.put("startTimeStr", startTime);
        params.put("endTimeStr", endTime);

        OkHttpUtils.post()
                .url(url)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.dismiss();

                        refreshLayout.finishLoadMore();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dialog.dismiss();

                        refreshLayout.finishLoadMore();
                        //com.blankj.utilcode.util.LogUtils.aTag("各种状态记录记录1212---", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getBoolean("success")) {
                                JSONObject json = jsonObject.getJSONObject("data");

                                List<FireMaintanceListBean> fireBeans = new Gson().fromJson(json.getJSONArray("rows").toString(),
                                        new TypeToken<List<FireMaintanceListBean>>() {
                                        }.getType());

                                if (fireBeansList.size() == 0 && (fireBeans == null || fireBeans.size() == 0)) {
                                    showNoDataView();
                                    return;
                                } else {
                                    hideNoDataView();
                                }

                                if (JSONObject.NULL != jsonObject.get("extra")) {
                                    List<List<String>> eventSum = new Gson().fromJson(jsonObject.getJSONArray("extra").toString(),
                                            new TypeToken<List<List<String>>>() {
                                            }.getType());
                                    if (eventSum != null && eventSum.size() > 0) {
                                        for (int i = 0; i < eventSum.size(); i++) {
                                            switch (i) {
                                                case 0:
                                                    mCount1.setVisibility(View.VISIBLE);
                                                    mNameCount1.setVisibility(View.VISIBLE);
                                                    if (eventSum.get(0).size() >= 2) {
                                                        mCount1.setText(eventSum.get(0).get(0));
                                                        mNameCount1.setText(eventSum.get(0).get(1));
                                                        String count = eventSum.get(0).get(1);
                                                        if (!StringUtils.isEmpty(count) && (count.contains("正常") || count.contains("未接管") ||
                                                                count.contains("未维修"))) {
                                                            mCount1.setTextColor(getResources().getColor(R.color.flat_greensea));
                                                        } else {
                                                            mCount1.setTextColor(getResources().getColor(R.color.flat_alizarin));
                                                        }
                                                    }
                                                    break;
                                                case 1:
                                                    mCount2.setVisibility(View.VISIBLE);
                                                    mNameCount2.setVisibility(View.VISIBLE);
                                                    if (eventSum.get(0).size() >= 2) {
                                                        mCount2.setText(eventSum.get(1).get(0));
                                                        mNameCount2.setText(eventSum.get(1).get(1));
                                                        String count = eventSum.get(1).get(1);
                                                        if (!StringUtils.isEmpty(count) && (count.contains("正常") || count.contains("未接管") ||
                                                                count.contains("未维修"))) {
                                                            mCount2.setTextColor(getResources().getColor(R.color.flat_greensea));
                                                        } else {
                                                            mCount2.setTextColor(getResources().getColor(R.color.flat_alizarin));
                                                        }
                                                    }
                                                    break;
                                                case 2:
                                                    mCount3.setVisibility(View.VISIBLE);
                                                    mNameCount3.setVisibility(View.VISIBLE);
                                                    if (eventSum.get(0).size() >= 2) {
                                                        mCount3.setText(eventSum.get(2).get(0));
                                                        mNameCount3.setText(eventSum.get(2).get(1));
                                                        String count = eventSum.get(2).get(1);
                                                        if (!StringUtils.isEmpty(count) && (count.contains("正常") || count.contains("未接管") ||
                                                                count.contains("未维修"))) {
                                                            mCount3.setTextColor(getResources().getColor(R.color.flat_greensea));
                                                        } else {
                                                            mCount3.setTextColor(getResources().getColor(R.color.flat_alizarin));
                                                        }
                                                    }
                                                    break;
                                                case 3:
                                                    mCount4.setVisibility(View.VISIBLE);
                                                    mNameCount4.setVisibility(View.VISIBLE);
                                                    if (eventSum.get(0).size() >= 2) {
                                                        mCount4.setText(eventSum.get(3).get(0));
                                                        mNameCount4.setText(eventSum.get(3).get(1));
                                                        String count = eventSum.get(3).get(1);
                                                        if (!StringUtils.isEmpty(count) && (count.contains("正常") || count.contains("未接管") ||
                                                                count.contains("未维修"))) {
                                                            mCount4.setTextColor(getResources().getColor(R.color.flat_greensea));
                                                        } else {
                                                            mCount4.setTextColor(getResources().getColor(R.color.flat_alizarin));
                                                        }
                                                    }
                                                    break;
                                            }
                                        }
                                    }
                                }

                                fireBeansList.addAll(fireBeans);

                                CommonAdapter adapter = new CommonAdapter<FireMaintanceListBean>(ProjectInfoMessageDataActivity.this, R.layout.item_alarm_history_info, fireBeansList) {
                                    @Override
                                    protected void convert(ViewHolder holder, FireMaintanceListBean bean, int position) {
                                        TextView dayAndMonth = holder.getView(R.id.item_alarm_time1);

                                        TextView years = holder.getView(R.id.item_alarm_time2);

                                        TextView detailTime = holder.getView(R.id.item_alarm_text);

                                        TextView mState = holder.getView(R.id.item_alarm_state1);

                                        TextView mStateOne = holder.getView(R.id.item_alarm_state);

                                        mState.setVisibility(View.INVISIBLE);

                                        LinearLayout line1 = holder.getView(R.id.line1);

                                        if (position == 0) {
                                            line1.setVisibility(View.INVISIBLE);
                                        } else {
                                            line1.setVisibility(View.VISIBLE);
                                        }

                                        String timeStr = bean.getEventTime().substring(bean.getEventTime().lastIndexOf("(") + 1, bean.getEventTime().lastIndexOf(")"));

                                        String okTime = HelperTool.MillTimeToDateForFireMain(Long.parseLong(timeStr));

                                        String[] strings = okTime.split("~");


                                        if (strings.length < 4) {
                                            ToastUtils.showLong("返回数据异常!");
                                            return;
                                        }
                                        years.setText(strings[0]);

                                        dayAndMonth.setText(String.valueOf(strings[1] + "." + strings[2]));

                                        detailTime.setText(strings[3]);

                                        mStateOne.setText(bean.getTrasentValue());

                                        if (labelType == 4) {
                                            mStateOne.setText(bean.getMessage());
                                        }

                                        if (!StringUtils.isEmpty(bean.getRealValue()) && "0".equals(bean.getRealValue())) {
                                            mStateOne.setTextColor(Color.parseColor("#16A085"));
                                        } else {
                                            mStateOne.setTextColor(Color.parseColor("#E74C3C"));
                                        }
                                    }
                                };
                                mRecyclerView.setAdapter(adapter);

                                adapter.notifyDataSetChanged();
                            } else {
                                ToastUtils.showLong(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
}
