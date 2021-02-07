package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.company;

import android.app.Activity;
import android.app.ProgressDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.OwnerFinishOrderActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;


/**
 * author hbzhou
 * date 2019/3/28 16:34
 */
public class CompanyHangUpActivity extends OwnerFinishOrderActivity {
    @Override
    protected void initOnClick() {

        setBarTitle("挂起工单");

        TextView mHangUpTitle = findViewById(R.id.tv_title);

        mHangUpTitle.setText("挂起原因");

        ed_content.setHint("请输入挂起原因");

        btn_confirm.setText("挂起");

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(ed_content.getText().toString().trim())) {
                    HelperView.Toast(CompanyHangUpActivity.this, "挂起原因不能为空!");
                    return;
                }
                initFinish();
            }
        });
    }

    @Override
    protected void initFinish() {

        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("workOrderId", ID);
        params.put("remark", ed_content.getText().toString().trim());

        final ProgressDialog progressDialog = showProgress(this, "挂起中...", false);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_COMPANY_HANG_UP_ORDER)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        progressDialog.dismiss();

                        try {
                            JSONObject json = new JSONObject(response);
                            boolean isOK = json.getBoolean("success");
                            if (isOK) {
                                ToastUtils.showLong("挂起成功!");
                                setResult(Activity.RESULT_OK);
                                finish();
                            } else {
                                ToastUtils.showLong(json.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
