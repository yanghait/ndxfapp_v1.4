package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection;


import android.app.ProgressDialog;

import com.blankj.utilcode.util.ToastUtils;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * author hbzhou
 * date 2019/1/14 18:05
 */
public class InspectionUtils {

    public static void initData(String projectId, final ProgressDialog dialog, final OnShowSettingInspectCallBack callBack) {
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("nodeId", projectId);
        if (dialog != null) {
            dialog.show();
        }
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE + URLConstant.URL_INSPECTION_GET_CREATE_TASK_STATE)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showLong(e.getMessage());
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        callBack.OnResult(1);
                        callBack.OnCompanyResult(1);
                        callBack.onShowRealVideo(false);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        LogUtils.showLoge("是否显示自定义创建任务5000---", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.getBoolean("success")) {

                                JSONObject data = json.getJSONObject("data");

                                int isShow = data.getInt("FireInspectUserType");
                                int isCompanyShow = data.getInt("FireInspectMaintenceUserType");
                                callBack.onShowRealVideo(data.getBoolean("RealVideoShow"));
                                callBack.OnResult(isShow);
                                callBack.OnCompanyResult(isCompanyShow);

                            } else {
                                callBack.OnResult(1);
                                callBack.OnCompanyResult(1);
                                callBack.onShowRealVideo(false);
                                //ToastUtils.showLong(json.getString("message"));
                            }
                        } catch (JSONException e) {
                            callBack.OnResult(1);
                            callBack.OnCompanyResult(1);
                            callBack.onShowRealVideo(false);
                            e.printStackTrace();
                        }
                    }
                });
    }

    public interface OnShowSettingInspectCallBack {
        void OnResult(int type);

        void OnCompanyResult(int type);

        void onShowRealVideo(boolean isShow);
    }
}
