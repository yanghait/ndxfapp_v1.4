package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.MyExpandableAdapterOne;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.MyExpandableAdapterTwo;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireAlarmStatisticsDetailBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireStatisticsDetailEventBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.ui.CustomerExpandableListView;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;


/**
 * author hbzhou
 * date 2019/1/15 12:08
 * 项目统计详情
 */
public class ProjectStatisticsDetailsActivity extends BaseActivity {

    private CustomerExpandableListView expanableOne;

    private CustomerExpandableListView expanableTwo;

    private List<String> timeList = new ArrayList<>();

    private List<List<FireAlarmStatisticsDetailBean>> chartList = new ArrayList<>();

    private List<String> timeList1 = new ArrayList<>();

    private String num;

    private ProgressDialog dialog;

    private String projectId = "";

    private String projectSystemID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_project_statistics_details);
        super.onCreate(savedInstanceState);
        setBarTitle(getIntent().getStringExtra("title"));

        expanableOne = findViewById(R.id.statistics_detail_list1);

        expanableTwo = findViewById(R.id.statistics_detail_list2);

        expanableOne.setGroupIndicator(null);
        expanableTwo.setGroupIndicator(null);

        num = getIntent().getStringExtra("num");

        projectId = getIntent().getStringExtra("projectId");

        projectSystemID = getIntent().getStringExtra("projectSystemID");

        dialog = showProgress(this, "加载中...", false);

        initStatisticsDetails(getIntent().getStringExtra("projectSystemID"));
    }

    private void initStatisticsDetails(String systemId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("projectSystemID", systemId);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_PROJECT_STATISTICS_DETAILS)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showLong(e.getMessage());
                        dialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dialog.dismiss();
                        //LogUtils.showLoge("系统详情页返回1212---", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                JSONObject json = jsonObject.getJSONObject("data");

                                List<List<String>> arrayList = new Gson().fromJson(json.getJSONArray("everydayEvent").toString(),
                                        new TypeToken<List<List<String>>>() {
                                        }.getType());
                                if (arrayList == null || arrayList.size() == 0) {
                                    showNoDataView();
                                    //ToastUtils.showLong("暂无数据!");
                                    return;
                                } else {
                                    hideNoDataView();
                                }

                                List<Long> timeInteger = new ArrayList<>();
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                                if (arrayList != null && arrayList.size() > 0) {
                                    for (int i = 0; i < arrayList.size(); i++) {
                                        try {
                                            Date date = format.parse(arrayList.get(i).get(0));
                                            for (int j = 0; j < timeInteger.size(); j++) {
                                                if (timeInteger.get(j) == date.getTime()) {
                                                    break;
                                                }
                                                if (j == timeInteger.size() - 1) {
                                                    timeInteger.add(date.getTime());
                                                }
                                            }
                                            if (timeInteger.size() == 0) {
                                                timeInteger.add(date.getTime());
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                Collections.sort(timeInteger);

                                for (int i = 0; i < timeInteger.size(); i++) {
                                    timeList.add(HelperTool.MillTimeToYearMonth(timeInteger.get(i)));
                                }

                                if (arrayList != null && arrayList.size() > 0) {
                                    for (int i = 0; i < timeList.size(); i++) {
                                        List<FireAlarmStatisticsDetailBean> list = new ArrayList<>();
                                        for (int n = 0; n < arrayList.size(); n++) {
                                            if (timeList.get(i).equals(arrayList.get(n).get(0))) {
                                                FireAlarmStatisticsDetailBean bean = new FireAlarmStatisticsDetailBean();

                                                bean.setItem1(arrayList.get(n).get(0));
                                                bean.setItem2(arrayList.get(n).get(1));
                                                bean.setItem3(arrayList.get(n).get(2));
                                                list.add(bean);
                                            }
                                        }
                                        chartList.add(list);
                                    }
                                }

                                // 测试多个事件时显示效果
//                                for (int i = 0; i < chartList.size(); i++) {
//                                    List<FireAlarmStatisticsDetailBean> list = chartList.get(i);
//
//                                    if (list != null && list.size() > 0) {
//                                        if (list.get(0).getItem1().equals("2019-01-24")) {
//                                            FireAlarmStatisticsDetailBean bean = new FireAlarmStatisticsDetailBean();
//                                            bean.setItem1("2019-01-24");
//                                            bean.setItem2("10");
//                                            bean.setItem3("5");
//                                            list.add(bean);
//                                            FireAlarmStatisticsDetailBean bean1 = new FireAlarmStatisticsDetailBean();
//                                            bean1.setItem1("2019-01-24");
//                                            bean1.setItem2("1000");
//                                            bean1.setItem3("6");
//                                            list.add(bean1);
//                                            FireAlarmStatisticsDetailBean bean2 = new FireAlarmStatisticsDetailBean();
//                                            bean2.setItem1("2019-01-24");
//                                            bean2.setItem2("200");
//                                            bean2.setItem3("1");
//                                            list.add(bean2);
//                                            break;
//                                        }
//                                    }
//                                }

                                expanableOne.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                                    @Override
                                    public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {

                                        boolean isSelected = expandableListView.isGroupExpanded(i);

                                        if (isSelected) {
                                            expandableListView.collapseGroup(i);
                                        } else {
                                            expandableListView.expandGroup(i);
                                        }
                                        // 让饼状图列表只可展开一个
                                        for (int j = 0; j < timeList.size(); j++) {
                                            if (j == i) {
                                                continue;
                                            }
                                            if (expandableListView.isGroupExpanded(j)) {
                                                expandableListView.collapseGroup(j);
                                            }
                                        }
                                        return true;
                                    }
                                });

                                expanableOne.setAdapter(new MyExpandableAdapterOne(ProjectStatisticsDetailsActivity.this,
                                        timeList, chartList, projectSystemID));

                                expanableOne.expandGroup(0);


                                // 处理前十条或前十五条 火灾报警与其他给水系统返回数据格式不同

                                final List<FireStatisticsDetailEventBean> topTenList;
                                if (!StringUtils.isEmpty(num) && "11".equals(num)) {
                                    topTenList = new Gson().fromJson(json.getJSONArray("systemEventTop10").toString(),
                                            new TypeToken<List<FireStatisticsDetailEventBean>>() {
                                            }.getType());
                                } else {
                                    topTenList = new ArrayList<>();

                                    List<List<String>> topTenList1 = new Gson().fromJson(json.getJSONArray("systemEventTop10").toString(),
                                            new TypeToken<List<List<String>>>() {
                                            }.getType());

                                    FireStatisticsDetailEventBean bean = new FireStatisticsDetailEventBean();
                                    bean.setEventCount(topTenList1);

                                    topTenList.add(bean);
                                }

                                if (topTenList == null || topTenList.size() == 0) {
                                    showNoDataView();
                                    ToastUtils.showLong("暂无数据!");
                                    return;
                                } else {
                                    hideNoDataView();
                                }

                                for (int i = 0; i < topTenList.size(); i++) {
                                    timeList1.add(topTenList.get(i).getHostNum() + "号报警主机");
                                }
//                                timeList1.add("2" + "号报警主机");
//
//                                FireStatisticsDetailEventBean bean = topTenList.get(0);
//
//                                topTenList.add(bean);

                                MyExpandableAdapterTwo adapterTwo = new MyExpandableAdapterTwo(ProjectStatisticsDetailsActivity.this, timeList1,
                                        null, topTenList, num);

                                expanableTwo.setAdapter(adapterTwo);

                                if (!StringUtils.isEmpty(num) && !"11".equals(num)) {

                                    expanableTwo.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                                        @Override
                                        public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                                            return true;
                                        }
                                    });
                                } else {
                                    expanableTwo.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                                        @Override
                                        public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {

                                            boolean isSelected = expandableListView.isGroupExpanded(i);

                                            if (isSelected) {
                                                expandableListView.collapseGroup(i);
                                            } else {
                                                expandableListView.expandGroup(i);
                                            }
                                            // 让饼状图列表只可展开一个
                                            for (int j = 0; j < topTenList.size(); j++) {
                                                if (j == i) {
                                                    continue;
                                                }
                                                if (expandableListView.isGroupExpanded(j)) {
                                                    expandableListView.collapseGroup(j);
                                                }
                                            }
                                            return true;
                                        }
                                    });

                                }

                                expanableTwo.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                                    @Override
                                    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                                        Intent intent = new Intent(ProjectStatisticsDetailsActivity.this, ProjectStatisticsDetailsErrorActivity.class);

                                        if (!StringUtils.isEmpty(num) && "11".equals(num)) {
                                            intent.putExtra("ID", projectSystemID);
                                        } else {
                                            intent.putExtra("ID", topTenList.get(i).getEventCount().get(i1).get(0));
                                        }

                                        intent.putExtra("Name", topTenList.get(i).getEventCount().get(i1).get(1));

                                        intent.putExtra("EventId", "");

                                        //intent.putExtra("projectId",projectId);

                                        //LogUtils.showLoge("007", topTenList.get(i).getEventCount().get(0).get(0));

                                        startActivity(intent);

                                        return true;
                                    }
                                });


                                expanableTwo.expandGroup(0);

                            } else {
                                ToastUtils.showLong(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            ToastUtils.showLong(e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
    }
}
