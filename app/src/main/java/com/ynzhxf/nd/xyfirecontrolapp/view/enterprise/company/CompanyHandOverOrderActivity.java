package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.company;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.CompanyHandOverGetUserInfoBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.DialogUtil;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class CompanyHandOverOrderActivity extends BaseActivity {
    private TextView content;
    private Button confirm;
    private MaterialSpinner spinner_list;

    private String ID;
    private String workOrderid;

    private EditText mHandOverContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_hand_over_order);
        super.onCreate(savedInstanceState);
        setBarTitle("移交工单");
        content = findViewById(R.id.tv_hand_over_receive);
        confirm = findViewById(R.id.hand_over_confirm);

        spinner_list = findViewById(R.id.hand_over_material_spinner);

        mHandOverContent = findViewById(R.id.hand_over_content);

        workOrderid = getIntent().getStringExtra("ID");

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initConfirm();
            }
        });
        initDataGetUserId();
    }

    private void initConfirm() {
        String handContent = mHandOverContent.getText().toString();
        if (StringUtils.isEmpty(ID)) {
            return;
        }
        if (StringUtils.isEmpty(handContent)) {
            ToastUtils.showLong("请输入移交原因!");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("workOrderId", workOrderid);
        params.put("userId", ID);
        params.put("remark", handContent);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_COMPANY_HAND_OVER_CONFIRM))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        HelperView.Toast(CompanyHandOverOrderActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("移交结果321---", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            boolean isOK = json.getBoolean("success");
                            if (isOK) {
                                DialogUtil.showDialogMessage(CompanyHandOverOrderActivity.this, "移交成功!", new DialogUtil.IComfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        setResult(Activity.RESULT_OK);
                                        finish();
                                    }
                                });
                            } else {
                                HelperView.Toast(CompanyHandOverOrderActivity.this, json.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(CompanyHandOverOrderActivity.this, e.getMessage());
                        }
                    }
                });
    }

    private void initDataGetUserId() {
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_COMPANY_HAND_OVER_GET_USER))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        HelperView.Toast(CompanyHandOverOrderActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("打印用户信息123--", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray jsonArray = json.getJSONArray("lsUserExceptSelf");
                            JSONObject cur = json.getJSONObject("curUser");
                            content.setText(cur.getString("Name"));
                            //LogUtils.showLoge("ID1616---", cur.getString("ID"));
                            final List<CompanyHandOverGetUserInfoBean> beanList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<CompanyHandOverGetUserInfoBean>>() {
                            }.getType());
                            List<String> list = new LinkedList<>();
                            if (beanList != null && beanList.size() > 0) {
                                for (int i = 0; i < beanList.size(); i++) {
                                    list.add(beanList.get(i).getName());
                                }
                                ID = beanList.get(0).getID();

                                //LogUtils.showLoge("ID1515---", HelperTool.getToken());

                                spinner_list.setItems(list);

                                spinner_list.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                                        ID = beanList.get(position).getID();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(CompanyHandOverOrderActivity.this, e.getMessage());
                        }
                    }
                });
    }
}
