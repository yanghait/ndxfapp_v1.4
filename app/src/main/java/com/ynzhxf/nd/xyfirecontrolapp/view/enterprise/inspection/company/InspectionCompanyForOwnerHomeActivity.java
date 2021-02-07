package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.company;


import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.CompanyInspectionTaskTypeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectStateNumBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionTaskHomeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionTaskHomeInputBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.inspection.impl.InspectionTaskPresenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.InspectionRecordsActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * author hbzhou
 * date 2019/4/8 15:55
 */
public class InspectionCompanyForOwnerHomeActivity extends InspectionHomeCompanyActivity {


    @Override
    protected void initData() {
        //super.initData();
    }

    @Override
    protected void doRequestData() {
        //
        presenter = InspectionTaskPresenterFactory.getTaskInspectionHomePresenterImpl(this);

        inputBean = new InspectionTaskHomeInputBean();

        inputBean.setToken(HelperTool.getToken());
        inputBean.setState(selectedState);
        inputBean.setProjectId(projectId);
        inputBean.setPageSize("10");
        inputBean.setCompany(true);
        inputBean.setCompanyForOwner(true);
        inputBean.setStartTime(HelperTool.MillTimeToStringDate(startLongTime));
        inputBean.setEndTime(HelperTool.MillTimeToStringDate(endLongTime));
        currentPosition = 1;
        inputBean.setPageIndex(String.valueOf(currentPosition));

        presenter.doTaskInspectionHomeList(inputBean);

        setBarTitle("维保方巡检任务");
    }

    @Override
    protected void initInspectStateNum() {

        //LogUtils.showLoge("更新个状态数量1212---", "~~~");

        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("projectId", projectId);
        params.put("inspectFrequencyId", selectedTypeId);
        params.put("startTime", HelperTool.MillTimeToStringDate(startLongTime));
        params.put("endTime", HelperTool.MillTimeToStringDate(endLongTime));
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_INSPECT_HOME_STATE_NUM_FOR_HOME))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        //LogUtils.showLoge("数量返回9898---", response);

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

    @Override
    protected void initTaskType(CheckBox typeBox1, CheckBox typeBox2) {
        HashMap<String, String> params = new HashMap<>();

        params.put("Token", HelperTool.getToken());

        final ProgressDialog dialog = showProgress(this, "加载中,请稍后...", false);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_COMPANY_INSPECTION_TYPE_LIST_FOR_OWNER)
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
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.getBoolean("success")) {

                                final List<CompanyInspectionTaskTypeBean> beanList = new Gson().fromJson(json.getJSONArray("data").toString(),
                                        new TypeToken<List<CompanyInspectionTaskTypeBean>>() {
                                        }.getType());

                                if (beanList == null || beanList.size() == 0) {
                                    HelperView.Toast(InspectionCompanyForOwnerHomeActivity.this, "未获取到任务类型!");
                                    return;
                                }

                                typeBeanList.addAll(beanList);

                                for (int i = 0; i < typeBeanList.size(); i++) {
                                    checkBoxList.get(i).setText(typeBeanList.get(i).getName());
                                }
                                if (typeBeanList.size() >= 3) {
                                    checkBoxList.get(2).setVisibility(View.VISIBLE);
                                }

                                if (typeBeanList.size() > 3) {
                                    mLayout2.setVisibility(View.VISIBLE);
                                }

                                if (typeBeanList.size() > 6) {
                                    mLayout3.setVisibility(View.VISIBLE);
                                }


                                switch (typeBeanList.size()) {
                                    case 1:
                                        checkBoxList.get(1).setVisibility(View.INVISIBLE);
                                        checkBoxList.get(2).setVisibility(View.INVISIBLE);
                                        break;
                                    case 2:
                                        checkBoxList.get(2).setVisibility(View.INVISIBLE);
                                        break;
                                    case 3:
                                        break;
                                    case 4:
                                        checkBoxList.get(4).setVisibility(View.INVISIBLE);
                                        checkBoxList.get(5).setVisibility(View.INVISIBLE);
                                        break;

                                    case 5:
                                        checkBoxList.get(5).setVisibility(View.INVISIBLE);
                                        break;
                                    case 6:
                                        break;
                                    case 7:
                                        break;
                                }

                            } else {
                                HelperView.Toast(InspectionCompanyForOwnerHomeActivity.this, json.getString("message"));
                            }
                        } catch (JSONException e) {
                            HelperView.Toast(InspectionCompanyForOwnerHomeActivity.this, e.getMessage());
                        }
                    }
                });
    }

    @Override
    protected void initInspectCompany(TextView headName, InspectionTaskHomeBean homeBean) {
        headName.setText(String.valueOf("维保公司:" + homeBean.getCompanyName()));
    }

    @Override
    protected void goToInspectItemList(int position) {

        InspectionTaskHomeBean homeBean = homeBeanList.get(position);

        Intent intent = new Intent(InspectionCompanyForOwnerHomeActivity.this, InspectionRecordsActivity.class);

        intent.putExtra("taskId", homeBean.getID());

        intent.putExtra("bean", homeBean);

        intent.putExtra("isCompany", true);

        intent.putExtra("isCompanyForOwner", true);

        startActivity(intent);
    }

    @Override
    protected void goToInspectRecords(InspectionTaskHomeBean homeBean) {
        Intent intent = new Intent(InspectionCompanyForOwnerHomeActivity.this, InspectionRecordsActivity.class);

        intent.putExtra("taskId", homeBean.getID());

        intent.putExtra("bean", homeBean);

        intent.putExtra("isCompany", true);

        intent.putExtra("isCompanyForOwner", true);

        startActivity(intent);
    }
}
