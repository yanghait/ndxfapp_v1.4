package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.DialogUtil;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class OwnerOrderOverDetailsActivity extends BaseActivity {

    private TextView tv_over_details;
    private boolean isFromCompany = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_owner_order_over);
        super.onCreate(savedInstanceState);
        setBarTitle("原因详情");
        tv_over_details = findViewById(R.id.over_details_content);
        Button btn_over_details = findViewById(R.id.btn_over_details_yes);
        final String id = getIntent().getStringExtra("ID");
        initData(id, URLConstant.URL_OWNER_ORDER_OVER_DETAILS, true);
        isFromCompany = getIntent().getBooleanExtra("isFromCompany", false);
        if (isFromCompany) {
            btn_over_details.setVisibility(View.GONE);
        }
        btn_over_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData(id, URLConstant.URL_OWNER_YES_ORDER_OVER, false);
            }
        });
    }

    private void initData(String id, String url, final boolean isDetails) {
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("workOrderId", id);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(url))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        HelperView.Toast(OwnerOrderOverDetailsActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("终止详情返回0909---", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            if (isDetails) {
                                String content = json.getString("data");
                                tv_over_details.setText(content);
                            } else {
                                boolean isResult = json.getBoolean("success");
                                if (isResult) {
                                    DialogUtil.showDialogMessage(OwnerOrderOverDetailsActivity.this, "终止成功!", new DialogUtil.IComfirmListener() {
                                        @Override
                                        public void onConfirm() {
                                            setResult(Activity.RESULT_OK);
                                            finish();
                                        }
                                    });
                                } else {
                                    HelperView.Toast(OwnerOrderOverDetailsActivity.this, "终止失败,请稍后再试!");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(OwnerOrderOverDetailsActivity.this, e.getMessage());
                        }
                    }
                });
    }
}
