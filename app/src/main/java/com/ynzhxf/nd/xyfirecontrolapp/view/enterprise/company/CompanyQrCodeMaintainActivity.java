package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.company;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.MaintenanceCompanyActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.OwnerMyWorkOrderActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class CompanyQrCodeMaintainActivity extends BaseActivity {

    private Button start_main;

    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_company_qrcode_maintain);
        super.onCreate(savedInstanceState);
        setBarTitle("扫码结果");
        start_main = findViewById(R.id.start_maintain);
        boolean isShow = getIntent().getBooleanExtra("isShow", false);
        flag = getIntent().getBooleanExtra("flag", false);
        if (isShow) {
            start_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToMaintainOrder(CompanyQrCodeMaintainActivity.this, getIntent().getStringExtra("projectId"), getIntent().getStringExtra("id"));
                }
            });
        } else {
            HelperView.Toast(this, "当前位置不在范围内!");
            start_main.setBackground(getResources().getDrawable(R.drawable.btn_maintain_bac));
            start_main.setClickable(false);
            start_main.setText("请在有效范围内扫码");
        }
    }

    private static ProgressDialog showProgressDigOne(Activity activity, String title, String message, boolean cancelable) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(cancelable);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
        return progressDialog;
    }

    private void goToMaintainOrder(final Activity activity, final String projectId, final String id) {
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("projectId", projectId);
        params.put("workOrderId", id);
        final ProgressDialog progressDialog = showProgressDigOne(activity, "提示", "加载中...", false);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_COMPANY_MAINTAIN_ORDER)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        progressDialog.dismiss();
                        HelperView.Toast(activity, e.getMessage());
                        start_main.setBackground(activity.getResources().getDrawable(R.drawable.btn_maintain_bac));
                        start_main.setClickable(false);
                        start_main.setText("请在有效范围内扫码");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.showLoge("开始维修123---", response);
                        progressDialog.dismiss();
                        try {
                            JSONObject json = new JSONObject(response);
                            boolean isOK = json.getBoolean("success");
                            if (isOK) {
                                if (!flag) {
                                    Intent intent = new Intent(activity, MaintenanceCompanyActivity.class);

                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                    activity.startActivity(intent);

                                    activity.finish();
                                } else {
                                    Intent intent = new Intent(activity, OwnerMyWorkOrderActivity.class);

                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                    intent.putExtra("isCompany", true);

                                    intent.putExtra("NoRemoveSPTime", true);

                                    intent.putExtra("state",3);

                                    activity.startActivity(intent);

                                    activity.finish();
                                }
                            } else {
                                start_main.setBackground(activity.getResources().getDrawable(R.drawable.btn_maintain_bac));
                                start_main.setClickable(false);
                                start_main.setText("请在有效范围内扫码");
                                HelperView.Toast(activity, json.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(activity, e.getMessage());
                        }
                    }
                });
    }
}
