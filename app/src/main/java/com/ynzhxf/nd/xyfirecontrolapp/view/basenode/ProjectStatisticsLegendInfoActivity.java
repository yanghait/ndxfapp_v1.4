package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;


import android.graphics.Color;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.StatisticsListErrorInfoBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;

/**
 * author hbzhou
 * date 2019/2/25 16:33
 */
public class ProjectStatisticsLegendInfoActivity extends ProjectStatisticsListErrorInfo implements OnDateSetListener {


    //时间选择控件初始时间
    public Calendar initStartTime = Calendar.getInstance();

    //时间选择控件结束时间
    public Calendar initEndTime = Calendar.getInstance();

    //开始时间毫秒
    public long startLongTime;

    //结束时间的毫秒数
    public long endLongTime;

    protected TextView mStartTime;

    protected TextView mEndTime;

    private String startTime;

    private String endTime;


    TextView mEventName1;
    TextView mEventCount1;

    TextView mEventName2;
    TextView mEventCount2;

    private LinearLayout layout2;

    public int selectFlag = 0;

    private int totalCount = 0;

    @Override
    protected void initDataForDetailsError() {
        //

        setBarTitle("事件记录详情");
        RelativeLayout layout1 = findViewById(R.id.statistics_details_layout1);

        LinearLayout errorLayout = findViewById(R.id.error_location);

        errorLayout.setVisibility(View.GONE);

        layout2 = findViewById(R.id.statistics_details_layout2);

        ImageButton down1 = findViewById(R.id.im_right_down1);

        ImageButton down2 = findViewById(R.id.im_right_down2);

        mEventName1 = findViewById(R.id.event_name1);
        mEventCount1 = findViewById(R.id.event_count1);

        mEventName2 = findViewById(R.id.event_name2);
        mEventCount2 = findViewById(R.id.event_count2);


        mStartTime = findViewById(R.id.txt_start_time);

        mEndTime = findViewById(R.id.txt_end_time);

        View bg1 = findViewById(R.id.error_bg1);
        View bg2 = findViewById(R.id.error_bg2);

        bg1.setVisibility(View.VISIBLE);
        bg2.setVisibility(View.VISIBLE);


        // down1.setVisibility(View.GONE);

        //down2.setVisibility(View.GONE);

        //layout2.setVisibility(View.VISIBLE);

        layout1.setVisibility(View.VISIBLE);

        layout2.setVisibility(View.GONE);

        startTime = getIntent().getStringExtra("timeStr");

        endTime = startTime;

        startTime = startTime + " " + "00:00:00";

        endTime = endTime + " " + "23:59:59";

        mStartTime.setText(startTime);

        mEndTime.setText(endTime);
        // 初始化时间参数
        initStartTime.add(Calendar.DAY_OF_MONTH, -31);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

        try {
            startLongTime = formatter.parse(startTime).getTime();
            endLongTime = formatter.parse(endTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        initOnClick();
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
    protected void initDataInfo() {
        //
        if (StringUtils.isEmpty(baseNodeID)) {
            dialog.dismiss();
            showNoDataView();
            refreshLayout.finishLoadMore();
            return;
        } else {
            hideNoDataView();
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("baseNodeID", baseNodeID);
        params.put("areaName", "");
        params.put("EventTypeID", eventTypeID);
        params.put("startTimeStr", startTime);
        params.put("endTimeStr", endTime);
        params.put("nowPage", String.valueOf(position));

        LogUtils.showLoge("baseNodeID", baseNodeID + "areaName" + areaName + "~~~" + startTime + "~~~" + endTime);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_STATISTICS_LEGEND_CLICK_LIST)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.dismiss();
                        refreshLayout.finishLoadMore();
                        //LogUtils.showLoge("获取统计详情点击详情0000---", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dialog.dismiss();
                        //LogUtils.showLoge("获取统计饼图实例详情5658---", response);

                        com.blankj.utilcode.util.LogUtils.json(response);

                        refreshLayout.finishLoadMore();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
//                            if (jsonObject.getBoolean("success")) {

                           // LogUtils.showLoge("extra1300---", jsonObject.getString("extra"));

                            JSONObject json = jsonObject.getJSONObject("data");

                            if (position == 1) {
                                totalCount = json.getInt("total");
                            }

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

                            if (errorBeanList.size() < totalCount) {
                                errorBeanList.addAll(errorInfoList);
                            }
                            CommonAdapter adapter = new CommonAdapter<StatisticsListErrorInfoBean>(ProjectStatisticsLegendInfoActivity.this, R.layout.item_statistics_legend_info, errorBeanList) {
                                @Override
                                protected void convert(ViewHolder holder, StatisticsListErrorInfoBean errorInfoBean, int position) {
                                    TextView mItemTime1 = holder.getView(R.id.error_time1);
                                    TextView mItemTime2 = holder.getView(R.id.error_time2);

                                    LinearLayout mErrorLocation = holder.getView(R.id.error_layout);

                                    mErrorLocation.setVisibility(View.VISIBLE);

                                    TextView mLocation = holder.getView(R.id.error_location);

                                    mLocation.setText(errorInfoBean.getAreaName());

                                    //mErrorLocation.setText(errorInfoBean.getAreaName());

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

                                    mItemMessage.setText("提示:".concat(errorInfoBean.getMessage()));

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

        errorBeanList.clear();

        initDataInfo();

    }
}
