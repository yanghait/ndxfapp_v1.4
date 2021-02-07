package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.company;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.CompanyExpandableTaskAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionCreateTaskGroupBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionSetPersonBackBean;
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
import java.util.List;

import okhttp3.Call;


/**
 * author hbzhou
 * date 2019/1/22 15:35
 */
public class InspectionCompanySettingActivity extends BaseActivity {

    private CustomerExpandableListView expandableListView;

    private Button mConfirm;

    private TextView mSetPerson;

    private String inspectorId;

    private ProgressDialog dialog;

    private String projectId;

    private String configId;

    private CompanyExpandableTaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_company_setting_task);
        super.onCreate(savedInstanceState);
        setBarTitle("设置");

        expandableListView = findViewById(R.id.company_inspect_setting_expand);

        expandableListView.setGroupIndicator(null);

        mSetPerson = findViewById(R.id.inspect_company_selected_person);

        dialog = showProgress(this, "加载中...", false);

        projectId = getIntent().getStringExtra("projectId");

        initData(getIntent().getStringExtra("projectId"));

        initOnClick();
    }

    private void initOnType() {

        HashMap<String, String> params = new HashMap<>();

        params.put("Token", HelperTool.getToken());

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_COMPANY_INSPECT_GET_TYPE)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                    }
                });
    }

    private void initOnClick() {
        mConfirm = findViewById(R.id.company_inspect_setting);

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                initSaveSettingData();
            }
        });

        mSetPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InspectionCompanySettingActivity.this, InspectionCompanySettingGetUserActivity.class);

                intent.putExtra("projectId", getIntent().getStringExtra("projectId"));

                startActivityForResult(intent, 20);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case 20:
                    mSetPerson.setText(data.getStringExtra("Name"));
                    inspectorId = data.getStringExtra("inspectId");
                    LogUtils.aTag("输出选择的负责人Id和Name1212---", mSetPerson.getText() + "---" + inspectorId);
                    break;
            }
        }
    }

    private void initSaveSettingData() {

        String itemIds = getInspectorIds();

        HashMap<String, String> params = new HashMap<>();

        if (StringUtils.isEmpty(projectId) || StringUtils.isEmpty(inspectorId) || StringUtils.isEmpty(itemIds)) {
            ToastUtils.showLong("请至少选择一个巡检负责人和巡检项!");
            dialog.dismiss();
            return;
        }

        params.put("projectId", projectId);

        params.put("inspectorId", inspectorId);

        params.put("inspectItemIds", itemIds);
        if (StringUtils.isEmpty(configId)) {
            params.put("configId", "");
        } else {
            params.put("configId", configId);
        }
        params.put("Token", HelperTool.getToken());

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_COMPANY_INSPECT_SETTING_SAVE)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.aTag(e.getMessage());
                        dialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.aTag("巡检设置保存!", response);
                        dialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                DialogUtil.showErrorMessage(InspectionCompanySettingActivity.this, "巡检设置保存成功!",
                                        new DialogUtil.IComfirmListener() {
                                            @Override
                                            public void onConfirm() {
                                                finish();
                                            }
                                        });
                            } else {
                                jsonObject.getString("message");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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


    private void initData(String projectId) {
        if (StringUtils.isEmpty(projectId)) {
            ToastUtils.showLong("未发现项目ID！");
            dialog.dismiss();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("projectId", projectId);

        params.put("Token", HelperTool.getToken());
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_COMPANY_INSPECT_SETTING_GET_INFO)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
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

                                configId = jsonObject1.getString("ConfigId");

                                inspectorId = jsonObject1.getString("InspectorId");

                                List<InspectionCreateTaskGroupBean> groupList = new Gson().fromJson(json.getJSONArray("dataTree").toString(),
                                        new TypeToken<List<InspectionCreateTaskGroupBean>>() {
                                        }.getType());
                                if (groupList == null || groupList.size() == 0) {
                                    ToastUtils.showLong("未获取到巡检项!");
                                    return;
                                }

                                List<InspectionSetPersonBackBean> personList = new Gson().fromJson(jsonObject1.getJSONArray("lsUser").toString(),
                                        new TypeToken<List<InspectionSetPersonBackBean>>() {
                                        }.getType());

                                if (personList == null || personList.size() == 0) {
                                    ToastUtils.showLong("未获取到巡检负责人!");
                                    return;
                                }

                                if (StringUtils.isEmpty(inspectorId)) {
                                    mSetPerson.setText(personList.get(0).getName());
                                    inspectorId = personList.get(0).getID();
                                } else {
                                    for (int i = 0; i < personList.size(); i++) {
                                        if (inspectorId.equals(personList.get(i).getID())) {
                                            mSetPerson.setText(personList.get(i).getName());
                                            break;
                                        }
                                    }
                                }

                                adapter = new CompanyExpandableTaskAdapter(InspectionCompanySettingActivity.this, groupList);

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

                                expandableListView.expandGroup(0);
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
