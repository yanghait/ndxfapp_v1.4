package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.OwnerImpowerInputBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.OwnerImpowerListBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class HistoryAlarmCreateOrderActivity extends OwnerCreateOrderActivity {

    private int selectPosition = 0;

    @Override
    public void doMainImpowerList(OwnerImpowerInputBean bean) {

    }

    @Override
    public void initSystemListId() {
        String baseNodeId = getIntent().getStringExtra("baseNodeId");
        initGetOrderDetails(baseNodeId);
        String content = getIntent().getStringExtra("content");
        ed_content.setText(content);
    }


    private void initGetOrderDetails(String baseNodeId) {
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("projectId", projectId);
        params.put("baseNodeId", baseNodeId);
        LogUtils.showLoge("获取授权系统列表1313---", projectId + "~~~~" + baseNodeId);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_OWNER_GET_SYSTEM_LIST))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        //HelperView.Toast(HistoryAlarmCreateOrderActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.showLoge("授权系统列表1212--", response);

                        try {
                            JSONObject json = new JSONObject(response);
                            JSONObject object = json.getJSONObject("data");
                            if (object == null) {
                                HelperView.Toast(HistoryAlarmCreateOrderActivity.this, "未发现工单系统!");
                                return;
                            } else {
                                ID = object.getString("SystemAuthedId");

                                final List<OwnerImpowerListBean> systemList = new Gson().fromJson(object.getJSONArray("lsProjectSystems").toString(),
                                        new TypeToken<List<OwnerImpowerListBean>>() {
                                        }.getType());

                                if (systemList != null && systemList.size() > 0) {
                                    spinner_list.setItems(getListStringForHistory(systemList));

                                    spinner_list.setSelectedIndex(selectPosition);

                                    spinner_list.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                                            ID = systemList.get(position).getID();
                                        }
                                    });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public List<String> getListStringForHistory(List<OwnerImpowerListBean> listBeans) {
        List<String> data = new LinkedList<>();
        for (int i = 0; i < listBeans.size(); i++) {
            data.add(listBeans.get(i).getProjectSystemName());
            if (listBeans.get(i).getID().equals(ID)) {
                selectPosition = i;
            }
        }
        return data;
    }
}
