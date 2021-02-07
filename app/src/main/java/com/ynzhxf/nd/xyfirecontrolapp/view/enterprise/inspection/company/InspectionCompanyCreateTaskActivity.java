package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.company;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.CompanyExpandableTaskAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.CompanyInspectionTaskTypeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionCreateTaskGroupBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.ui.CustomerExpandableListView;
import com.ynzhxf.nd.xyfirecontrolapp.util.DialogUtil;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;

/**
 * author hbzhou
 * date 2019/1/22 10:49
 */
public class InspectionCompanyCreateTaskActivity extends BaseActivity {

    private CustomerExpandableListView expandableListView;

    private String inspectTypeValue = "0";

    private MaterialSpinner spinner_list;

    private ProgressDialog dialog;

    private int spinnerPosition = 0;

    private CompanyExpandableTaskAdapter adapter;
    private String projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_company_create_task);
        super.onCreate(savedInstanceState);
        setBarTitle("创建任务");

        expandableListView = findViewById(R.id.company_create_task_expand);

        expandableListView.setGroupIndicator(null);

        spinner_list = findViewById(R.id.spinner_list);

        Button mConfirm = findViewById(R.id.create_inspect_task);

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initAddInspectTask();
            }
        });

        projectId = getIntent().getStringExtra("projectId");

        dialog = showProgress(this, "加载中...", false);

        initData(projectId);
    }

    // 遍历所有选中的巡检项
    private String getInspectorIds() {
        List<InspectionCreateTaskGroupBean> groupList = adapter.getGroupList();
        String itemIds = "";
        if (groupList != null) {
            for (int i = 0; i < groupList.size(); i++) {
                for (int j = 0; j < groupList.get(i).getChildren().size(); j++) {
                    if ("1".equals(groupList.get(i).getChildren().get(j).getCheckArr().get(0).getIsChecked())) {
                        itemIds = itemIds.concat(groupList.get(i).getChildren().get(j).getTreeId()) + ",";
                    }
                }
            }
        }
        if (!StringUtils.isEmpty(itemIds)) {
            return itemIds.substring(0, itemIds.length() - 1);
        }
        return null;
    }

    private void initAddInspectTask() {

        String itemIds;
        if (adapter == null) {
            ToastUtils.showLong("未获取到巡检项数据!");
            return;
        }
        if (StringUtils.isEmpty(getInspectorIds())) {
            itemIds = "";
        } else {
            itemIds = getInspectorIds();
        }

        if (StringUtils.isEmpty(itemIds)) {
            ToastUtils.showLong("请至少选择一个巡检项!");
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("projectId", getIntent().getStringExtra("projectId"));
        params.put("inspectTypeValue", inspectTypeValue);
        params.put("Token", HelperTool.getToken());
        params.put("inspectItemIds", itemIds);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_COMPANY_INSPECT_ADD_TASK)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        //ToastUtils.showLong(e.getMessage());
                        LogUtils.aTag(e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.aTag("巡检创建任务结果1212---", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                DialogUtil.showErrorMessage(InspectionCompanyCreateTaskActivity.this, "创建巡检任务成功!",
                                        new DialogUtil.IComfirmListener() {
                                            @Override
                                            public void onConfirm() {
                                                setResult(Activity.RESULT_OK);
                                                finish();
                                            }
                                        });
                            } else {
                                ToastUtils.showLong(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initData(final String projectId) {

        HashMap<String, String> params = new HashMap<>();
        params.put("projectId", projectId);
        params.put("inspectTypeValue", inspectTypeValue);
        params.put("Token", HelperTool.getToken());
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_COMPANY_ADD_TASK_GET_DATA)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        //ToastUtils.showLong(e.getMessage());
                        LogUtils.aTag(e.getMessage());
                        dialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("配置任务列表返回1212---", response);

                        dialog.dismiss();

                        LogUtils.json(response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                JSONObject json = jsonObject1.getJSONObject("SystemItemTree");
                                List<InspectionCreateTaskGroupBean> groupList = new Gson().fromJson(json.getJSONArray("dataTree").toString(),
                                        new TypeToken<List<InspectionCreateTaskGroupBean>>() {
                                        }.getType());
                                if (groupList == null || groupList.size() == 0) {
                                    ToastUtils.showLong("未获取到巡检项!");
                                    return;
                                }

                                final List<CompanyInspectionTaskTypeBean> typeBeanList = new Gson().fromJson(jsonObject1.getJSONArray("lsTaskType").toString(),
                                        new TypeToken<List<CompanyInspectionTaskTypeBean>>() {
                                        }.getType());

                                spinner_list.setItems(getListString(typeBeanList));

                                // inspectTypeValue = String.valueOf(typeBeanList.get(0).getValue());

                                spinner_list.setSelectedIndex(spinnerPosition);

                                spinner_list.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                                        inspectTypeValue = String.valueOf(typeBeanList.get(position).getValue());
                                        initData(projectId);
                                    }
                                });

                                adapter = new CompanyExpandableTaskAdapter(InspectionCompanyCreateTaskActivity.this, groupList);

                                expandableListView.setAdapter(adapter);

                                expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                                    @Override
                                    public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                                        InspectionCreateTaskGroupBean.ChildrenBean bean = adapter.getGroupList().get(i).getChildren().get(i1);

                                        switch (bean.getCheckArr().get(0).getIsChecked()) {
                                            case "0":
                                                bean.getCheckArr().get(0).setIsChecked("1");

                                                if (adapter.isAllSelectedData(true, adapter.getGroupList().get(i).getChildren())) {
                                                    adapter.getGroupList().get(i).getCheckArr().get(0).setIsChecked("1");
                                                } else {
                                                    adapter.getGroupList().get(i).getCheckArr().get(0).setIsChecked("2");
                                                }
                                                break;
                                            case "1":
                                                bean.getCheckArr().get(0).setIsChecked("0");

                                                if (adapter.isAllSelectedData(false, adapter.getGroupList().get(i).getChildren())) {
                                                    adapter.getGroupList().get(i).getCheckArr().get(0).setIsChecked("0");
                                                } else {
                                                    adapter.getGroupList().get(i).getCheckArr().get(0).setIsChecked("2");
                                                }
                                                break;
                                        }
                                        adapter.notifyDataSetChanged();

                                        return true;
                                    }
                                });
                            } else {
                                ToastUtils.showLong(jsonObject.getString("message"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public List<String> getListString(List<CompanyInspectionTaskTypeBean> listBeans) {
        List<String> data = new LinkedList<>();
        for (int i = 0; i < listBeans.size(); i++) {
            data.add(listBeans.get(i).getName());
            if (inspectTypeValue.equals(String.valueOf(listBeans.get(i).getValue()))) {
                spinnerPosition = i;
            }
        }
        return data;
    }
}
