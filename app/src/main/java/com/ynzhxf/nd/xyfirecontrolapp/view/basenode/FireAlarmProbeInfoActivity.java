package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;


import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.StatisticsListErrorInfoBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;

/**
 * author hbzhou
 * date 2019/3/18 15:00
 */
public class FireAlarmProbeInfoActivity extends FireAlarmHistoryInfoActivity {


    @Override
    protected void initAlarmHistoryInfo() {
        if (StringUtils.isEmpty(hostID) || StringUtils.isEmpty(projectSystemID)) {
            ToastUtils.showLong("未获取到探头信息!");
            dialog.dismiss();
            showNoDataView();
            return;
        } else {
            hideNoDataView();
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("projectSystemID", projectSystemID);
        params.put("hostID", hostID);
        params.put("userID", userID);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

//        LogUtils.showLoge("探头历史记录返回1212000---", startTime + "~~~" + endTime + "~~~" +
//                projectSystemID + "~~~" + hostID + "~~~" + userID + "~~~" + resource);

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_FIRE_ALARM_HISTORY_INFO)//URL_FIRE_ALARM_COMPUTER_INFO
                .params(params)
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
                                    if (historySortList.get(i).size() > 3) {

                                        bean.setAlarmValue(historySortList.get(i).get(1));

                                        bean.setEventName(initEventName(historySortList.get(i).get(2)));

                                        //bean.setMessage(historySortList.get(i).get(4));

                                        bean.setReviewStateStr(historySortList.get(i).get(3));

                                        bean.setEventTimeStr(historySortList.get(i).get(0));
                                    }
                                    errorInfoList.add(bean);
                                }

                                CommonAdapter adapter = new CommonAdapter<StatisticsListErrorInfoBean>(FireAlarmProbeInfoActivity.this, R.layout.item_statistics_error_info, errorInfoList) {
                                    @Override
                                    protected void convert(ViewHolder holder, StatisticsListErrorInfoBean errorInfoBean, int position) {
                                        TextView mItemTime1 = holder.getView(R.id.error_time1);
                                        TextView mItemTime2 = holder.getView(R.id.error_time2);

                                        TextView mItemName = holder.getView(R.id.error_name);

                                        TextView mItemType = holder.getView(R.id.error_type);

                                        TextView mItemMessage = holder.getView(R.id.error_message);

                                        mItemMessage.setVisibility(View.GONE);

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

                                        //mItemMessage.setText(errorInfoBean.getMessage());

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

    private String initEventName(String nameState) {

        String string = "";

        switch (nameState) {
            case "0":
                string = "无事件";
                break;

            case "1":

                string = "控制事件";
                break;

            case "2":

                string = "运行事件";
                break;

            case "3":

                string = "报警事件";
                break;

            case "4":

                string = "故障事件";
                break;

            case "5":

                string = "恢复事件";
                break;

            case "6":

                string = "通讯事件";
                break;

            case "10":
                string = "其他事件";
                break;
        }
        return string;

    }
}


