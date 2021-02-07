package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.AuditOrderBean;
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

public class OwnerAuditOrderActivity extends BaseActivity {
    private TextView tv_type;
    private TextView tv_point;
    private TextView tv_method;
    private TextView tv_content;

    private RatingBar ratingBar;
    private EditText ed_content;

    private Button btn_back;
    private Button btn_confirm;

    private int score = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_owner_audit_order);
        super.onCreate(savedInstanceState);
        setBarTitle("工单审核");

        tv_type = findViewById(R.id.tv_type);
        tv_method = findViewById(R.id.tv_method);
        tv_content = findViewById(R.id.tv_content);
        tv_point = findViewById(R.id.tv_point);
        ratingBar = findViewById(R.id.rate_bar);
        ed_content = findViewById(R.id.ed_content);
        btn_back = findViewById(R.id.btn_order_back);
        btn_confirm = findViewById(R.id.btn_order_confirm);

        initAuditData(getIntent().getStringExtra("id"));
        initView(getIntent().getStringExtra("id"));

        LogUtils.showLoge("获取工单审核数据009---", "~~~~" + getIntent().getStringExtra("id") + "~~~" + HelperTool.getToken());
    }

    private void initView(final String id) {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                score = (int) v;
                LogUtils.showLoge("星级分数123---", String.valueOf(score));
            }
        });
        ratingBar.setRating(1.0f);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDataConfirm(id, 0);
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDataConfirm(id, 1);
            }
        });
    }

    private void initDataConfirm(String id, final int isSubmit) {
        if (TextUtils.isEmpty(ed_content.getText().toString().trim())) {
            HelperView.Toast(this, "评语不能为空!");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("workOrderId", id);
        params.put("evaluateLevel", String.valueOf(score));
        params.put("evaluateComment", ed_content.getText().toString().trim());
        params.put("isSubmit", String.valueOf(isSubmit));
        showProgressDig("提示", "加载中...", false);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.UEL_OWNER_EVALUATE_ORDER))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        hideProgressDig();
                        HelperView.Toast(OwnerAuditOrderActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        hideProgressDig();
                        LogUtils.showLoge("评价工单结果0520---", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            boolean isOK = json.getBoolean("success");
                            Intent intent = new Intent();

                            if (isOK && isSubmit == 0) {
                                HelperView.Toast(OwnerAuditOrderActivity.this, "提交返工成功!");
                                intent.putExtra("ResultType", 1);
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            } else if (isOK && isSubmit == 1) {
                                HelperView.Toast(OwnerAuditOrderActivity.this, "评价成功!");
                                intent.putExtra("ResultType", 2);
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            } else {
                                HelperView.Toast(OwnerAuditOrderActivity.this, json.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(OwnerAuditOrderActivity.this, e.getMessage());
                        }
                    }
                });
    }

    private void initAuditData(final String id) {
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("workOrderId", id);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_OWNER_AUDIT_ORDER))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        HelperView.Toast(OwnerAuditOrderActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id1) {
                        //LogUtils.showLoge("获取工单审核数据4567---", response + "~~~~" + id + "~~~" + HelperTool.getToken());
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.getBoolean("success")) {
                                JSONObject obj = json.getJSONObject("data");
                                AuditOrderBean bean = new Gson().fromJson(obj.toString(), AuditOrderBean.class);
                                tv_type.setText(bean.getFaultType());
                                tv_point.setText(bean.getFaultPlace());
                                tv_method.setText(bean.getRepairMethod());
                                tv_content.setText(bean.getRepairContent());
                            } else {
                                HelperView.Toast(OwnerAuditOrderActivity.this, json.getString("message"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(OwnerAuditOrderActivity.this, e.getMessage());
                        }
                    }
                });
    }
}
