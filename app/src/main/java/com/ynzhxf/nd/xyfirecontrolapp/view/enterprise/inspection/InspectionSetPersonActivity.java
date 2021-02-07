package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import com.ynzhxf.nd.xyfirecontrolapp.util.DialogUtil;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class InspectionSetPersonActivity extends InspectionResponsiblePersonActivity {


    @Override
    protected void setPersonData(final int position, String id) {
        //

        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("ID", areaId);
        params.put("inspectorId", id);
        final ProgressDialog dialog = showProgress(this, "设置中...", false);

        OkHttpUtils.post()
                .url(url)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.dismiss();
                        HelperView.Toast(InspectionSetPersonActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dialog.dismiss();
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.getBoolean("success")) {

                                DialogUtil.showErrorMessage(InspectionSetPersonActivity.this, "设置负责人成功!", new DialogUtil.IComfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        Intent intent = new Intent();
                                        intent.putExtra("Name", personBeanList.get(position).getName());
                                        intent.putExtra("position", setPosition);
                                        setResult(Activity.RESULT_OK, intent);
                                        finish();
                                    }
                                });

                            } else {
                                HelperView.Toast(InspectionSetPersonActivity.this, json.getString("message"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(InspectionSetPersonActivity.this, e.getMessage());
                        }
                    }
                });
    }
}
