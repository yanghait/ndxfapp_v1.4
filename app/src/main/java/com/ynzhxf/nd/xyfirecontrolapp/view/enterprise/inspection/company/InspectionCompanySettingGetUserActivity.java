package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.company;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionSetPersonBackBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.InspectionSettingGetUserListActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;


/**
 * author hbzhou
 * date 2019/1/23 09:32
 */
public class InspectionCompanySettingGetUserActivity extends InspectionSettingGetUserListActivity {

    @Override
    protected void setPersonData(int position, String id) {

        Intent intent = new Intent();
        intent.putExtra("Name", personBeanList.get(position).getName());
        intent.putExtra("inspectId", personBeanList.get(position).getID());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void initGetData(String query) {

        searchView.setVisibility(View.GONE);

        final ProgressDialog dialog = showProgress(this, "加载中...", false);
        if (StringUtils.isEmpty(getIntent().getStringExtra("projectId"))) {
            ToastUtils.showLong("未发现项目ID！");
            dialog.dismiss();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("projectId", getIntent().getStringExtra("projectId"));

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

                                List<InspectionSetPersonBackBean> personList = new Gson().fromJson(jsonObject1.getJSONArray("lsUser").toString(),
                                        new TypeToken<List<InspectionSetPersonBackBean>>() {
                                        }.getType());

                                if (personList == null || personList.size() == 0) {
                                    return;
                                }

                                personBeanList.clear();

                                personBeanList.addAll(personList);

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
