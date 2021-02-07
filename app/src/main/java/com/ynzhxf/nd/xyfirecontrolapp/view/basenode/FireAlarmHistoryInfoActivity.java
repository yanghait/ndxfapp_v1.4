package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.StatisticsListErrorInfoBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;


/**
 * author hbzhou
 * date 2019/1/14 20:15
 * 火灾探头历史记录
 */
public class FireAlarmHistoryInfoActivity extends BaseActivity implements OnDateSetListener {

    protected TextView title;

    protected RecyclerView mRecyclerView;

    protected String projectSystemID;

    protected String hostID;

    protected String userID;

    protected String positionName;

    protected String resource;

    protected ProgressDialog dialog;


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

    protected String startTime = "";

    protected String endTime = "";

    public int selectFlag = 0;

    protected RefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_alarm_history_info);
        super.onCreate(savedInstanceState);
        setBarTitle("历史趋势");

        title = findViewById(R.id.alarm_history_title);

        mRecyclerView = findViewById(R.id.alarm_history_recycler);

        mStartTime = findViewById(R.id.txt_start_time);
        mEndTime = findViewById(R.id.txt_end_time);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
        if (intent != null) {
            projectSystemID = intent.getStringExtra("projectSystemID");
            hostID = intent.getStringExtra("hostID");
            userID = intent.getStringExtra("userID");
            positionName = intent.getStringExtra("positionName");
            resource = intent.getStringExtra("resource");
        }

        if (!StringUtils.isEmpty(positionName)) {
            title.setText(positionName);
        }
        dialog = showProgress(this, "加载中...", false);

        initSelectTime();

        initImProjectInfo();

        initOnClick();

        initRefreshLayout();

    }

    protected void initRefreshLayout() {
    }

    protected void initImProjectInfo() {
//        if (!StringUtils.isEmpty(resource) && "1".equals(resource)) {
//
            if (getIntent().getBooleanExtra("isFromAlarm", false)) {
                setBarTitle("主机状态");
            }
//
//            if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
//                // 默认加载七天
//                initStartTime.add(Calendar.DAY_OF_MONTH, -31);
//                //initEndTime.add(Calendar.YEAR, 1);
//
//                Calendar queryTime = Calendar.getInstance();
//                endLongTime = queryTime.getTimeInMillis();
//                queryTime.add(Calendar.DAY_OF_MONTH, -7);
//                startLongTime = queryTime.getTimeInMillis();
//                mStartTime.setText(HelperTool.MillTimeToStringDate(startLongTime).concat(" "));
//                mEndTime.setText(HelperTool.MillTimeToStringDate(endLongTime).concat(" "));
//
//                startTime = HelperTool.MillTimeToStringDateDetail(startLongTime);
//                endTime = HelperTool.MillTimeToStringDateDetail(endLongTime);
//            }
//            initAlarmHistoryInfoNew();
//        } else {

            if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
                // 默认加载24小时
                initStartTime.add(Calendar.DAY_OF_MONTH, -31);
                //initEndTime.add(Calendar.YEAR, 1);

                Calendar queryTime = Calendar.getInstance();
                endLongTime = queryTime.getTimeInMillis();
                queryTime.add(Calendar.DAY_OF_MONTH, -1);
                startLongTime = queryTime.getTimeInMillis();
                mStartTime.setText(HelperTool.MillTimeToStringDate(startLongTime).concat(" "));
                mEndTime.setText(HelperTool.MillTimeToStringDate(endLongTime).concat(" "));

                startTime = HelperTool.MillTimeToStringDateDetail(startLongTime);
                endTime = HelperTool.MillTimeToStringDateDetail(endLongTime);
            }
            initAlarmHistoryInfo();
        //}
    }

    private void initAlarmHistoryInfoNew() {

        LogUtils.showLoge("历史记录请求参数0909111---", projectSystemID + "~~~" + hostID + "~~~" + resource + "~~~" + startTime + "~~~" +
                endTime);

        if (StringUtils.isEmpty(hostID) || StringUtils.isEmpty(projectSystemID)) {
            ToastUtils.showLong("未获取到探头信息!");
            dialog.dismiss();
            showNoDataView();
            return;
        }else{
            hideNoDataView();
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("proSysID", projectSystemID);
        params.put("hostNum", hostID);
        params.put("resource", resource);
        params.put("userID", userID);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_FIRE_ALARM_COMPUTER_INFO)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.dismiss();
                        showNoDataView();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dialog.dismiss();
                        LogUtils.showLoge("火灾报警主机和探头报警记录0909---", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getBoolean("success")) {
                                List<List<String>> historyLogList = new Gson().fromJson(jsonObject.getJSONArray("data").toString(),
                                        new TypeToken<List<List<String>>>() {
                                        }.getType());

                                if (historyLogList == null || historyLogList.size() == 0) {
                                    showNoDataView();
                                    return;
                                } else {
                                    hideNoDataView();
                                }

                                List<List<String>> historySortList = new ArrayList<>();

                                for (int i = historyLogList.size() - 1; i >= 0; i--) {
                                    historySortList.add(historyLogList.get(i));
                                }

                                List<StatisticsListErrorInfoBean> errorInfoList = new ArrayList<>();

                                for (int i = 0; i < historySortList.size(); i++) {
                                    StatisticsListErrorInfoBean bean = new StatisticsListErrorInfoBean();
                                    if (historySortList.get(i).size() > 5) {

                                        bean.setAlarmValue(historySortList.get(i).get(1));

                                        bean.setEventName(historySortList.get(i).get(5));

                                        bean.setMessage(historySortList.get(i).get(4));

                                        bean.setReviewStateStr(historySortList.get(i).get(3));

                                        bean.setEventTimeStr(historySortList.get(i).get(0));
                                    }
                                    errorInfoList.add(bean);
                                }

                                CommonAdapter adapter = new CommonAdapter<StatisticsListErrorInfoBean>(FireAlarmHistoryInfoActivity.this, R.layout.item_statistics_error_info, errorInfoList) {
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


                                        mItemType.setText(String.valueOf("(" + errorInfoBean.getEventName() + ")"));

                                        mItemMessage.setText(errorInfoBean.getMessage());

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
                                        } catch (Exception e) {
                                            ToastUtils.showLong(e.getMessage());
                                            e.printStackTrace();
                                        }

                                    }
                                };

                                mRecyclerView.setAdapter(adapter);
                            } else {
                                ToastUtils.showLong(jsonObject.getString("message"));
                                showNoDataView();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showLong(e.getMessage());
                        }
                    }
                });
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


//    params.put("Token",HelperTool.getToken());
//        params.put("projectSystemID",projectSystemID);
//        params.put("hostID",hostID);
//        params.put("userID",userID);
//        params.put("startTime",startTime);
//        params.put("endTime",endTime);

    protected void initAlarmHistoryInfo() {

        if (StringUtils.isEmpty(hostID) || StringUtils.isEmpty(projectSystemID)) {
            ToastUtils.showLong("未获取到主机或探头信息!");
            dialog.dismiss();
            showNoDataView();
            return;
        }else{
            hideNoDataView();
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("proSysID", projectSystemID);
        params.put("hostNum", hostID);
        params.put("userID", userID);
        params.put("resource", resource);
        params.put("startTimeStr", startTime);
        params.put("endTimeStr", endTime);

//        LogUtils.showLoge("探头历史记录返回1212000---", startTime + "~~~" + endTime + "~~~" +
//                projectSystemID + "~~~" + hostID + "~~~" + userID + "~~~" + resource);

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_FIRE_ALARM_COMPUTER_INFO)//URL_FIRE_ALARM_COMPUTER_INFO
                .params(params)//URL_FIRE_ALARM_HISTORY_INFO
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dialog.dismiss();
                        //LogUtils.showLoge("探头历史记录返回1212---", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getBoolean("success")) {
                                List<List<String>> historyLogList = new Gson().fromJson(jsonObject.getJSONArray("data").toString(),
                                        new TypeToken<List<List<String>>>() {
                                        }.getType());

                                if (historyLogList == null || historyLogList.size() == 0) {
                                    showNoDataView();
                                    return;
                                } else {
                                    hideNoDataView();
                                }

                                List<List<String>> historySortList = new ArrayList<>();

                                for (int i = historyLogList.size() - 1; i >= 0; i--) {
                                    historySortList.add(historyLogList.get(i));
                                }

                                List<StatisticsListErrorInfoBean> errorInfoList = new ArrayList<>();

                                for (int i = 0; i < historySortList.size(); i++) {
                                    StatisticsListErrorInfoBean bean = new StatisticsListErrorInfoBean();
                                    if (historySortList.get(i).size() > 5) {

                                        bean.setAlarmValue(historySortList.get(i).get(1));

                                        bean.setEventName(historySortList.get(i).get(5));

                                        bean.setMessage(historySortList.get(i).get(4));

                                        bean.setReviewStateStr(historySortList.get(i).get(3));

                                        bean.setEventTimeStr(historySortList.get(i).get(0));
                                    }
                                    errorInfoList.add(bean);
                                }

                                CommonAdapter adapter = new CommonAdapter<StatisticsListErrorInfoBean>(FireAlarmHistoryInfoActivity.this, R.layout.item_statistics_error_info, errorInfoList) {
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


                                        mItemType.setText(String.valueOf("(" + errorInfoBean.getEventName() + ")"));

                                        mItemMessage.setText(errorInfoBean.getMessage());

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
                                        } catch (Exception e) {
                                            ToastUtils.showLong(e.getMessage());
                                            e.printStackTrace();
                                        }

                                    }
                                };

                                mRecyclerView.setAdapter(adapter);
                            } else {
                                ToastUtils.showLong(jsonObject.getString("message"));
                                showNoDataView();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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

        initImProjectInfo();
    }
}
