package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class OwnerFinishOrderActivity extends BaseActivity {
    protected EditText ed_content;
    protected String ID;

    protected boolean isFromCompany = false;

    protected Button btn_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_for_work_order);
        super.onCreate(savedInstanceState);
        setBarTitle("终止工单");
        ed_content = findViewById(R.id.ed_reason);
        btn_confirm = findViewById(R.id.btn_sure);
        ID = getIntent().getStringExtra("ID");
        isFromCompany = getIntent().getBooleanExtra("isFromCompany", false);
        initOnClick();
    }

    protected void initOnClick(){
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(ed_content.getText().toString().trim())) {
                    HelperView.Toast(OwnerFinishOrderActivity.this, "终止原因不能为空!");
                    return;
                }
                initFinish();
            }
        });
    }

    protected void initFinish() {
        String url = URLConstant.URL_OWNER_FINISH_ORDER;
        if (isFromCompany) {
            url = URLConstant.URL_COMPANY_TERMINATION_ORDER;
        }
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("workOrderId", ID);
        params.put("endReason", ed_content.getText().toString().trim());
        final ProgressDialog dialog = showProgress(this, "终止工单中...", false);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(url))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.dismiss();
                        HelperView.Toast(OwnerFinishOrderActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dialog.dismiss();
                        LogUtils.showLoge("终止工单1521---", response);
                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONObject json = new JSONObject(response);
                                if ((boolean) json.get("success")) {
                                    HelperView.Toast(OwnerFinishOrderActivity.this, "终止成功!");
                                    setResult(Activity.RESULT_OK);
                                    finish();
                                } else {
                                    HelperView.Toast(OwnerFinishOrderActivity.this, json.getString("message"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}
