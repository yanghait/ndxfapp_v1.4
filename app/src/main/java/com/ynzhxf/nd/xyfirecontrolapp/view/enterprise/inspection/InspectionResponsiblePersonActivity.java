package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.MultiItemTypeAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionSetPersonBackBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.DialogUtil;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.PermissionsUtil;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.company.CompanySearchProjectActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class InspectionResponsiblePersonActivity extends CompanySearchProjectActivity {


    protected List<InspectionSetPersonBackBean> personBeanList = new ArrayList<>();

    protected int setPosition = 0;

    protected String taskId;

    protected boolean isSearchText = true;

    protected String url;

    protected String areaId;

    @Override
    public void initStartData() {

        setBarTitle("设置负责人");

        searchView.setQueryHint("输入名字查询");

        setPosition = getIntent().getIntExtra("position", 0);

        taskId = getIntent().getStringExtra("taskId");

        url = getIntent().getStringExtra("url");

        areaId = getIntent().getStringExtra("areaId");

        adapter = new CommonAdapter<InspectionSetPersonBackBean>(InspectionResponsiblePersonActivity.this, R.layout.item_set_person_layout, personBeanList) {
            @Override
            protected void convert(ViewHolder holder, final InspectionSetPersonBackBean bean, int position) {
                ((TextView) holder.getView(R.id.set_person_item_text)).setText(bean.getName());

                ImageButton callPhone = holder.getView(R.id.set_person_item_image);

                callPhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /**
                         * 拨打电话（直接拨打电话）
                         * @param phoneNum 电话号码
                         */
                        if (StringUtils.isEmpty(bean.getContactsPhone())) {
                            HelperView.Toast(InspectionResponsiblePersonActivity.this, "未发现联系人号码!");
                            return;
                        }
                        String[] permissions = new String[]{Manifest.permission.CALL_PHONE};

                        if (ContextCompat.checkSelfPermission(InspectionResponsiblePersonActivity.this, permissions[0])
                                != PackageManager.PERMISSION_GRANTED) {
                            requestPermissionsCallBack(InspectionResponsiblePersonActivity.this, "请求存储权限", 29, permissions, new PermissionsUtil.IGrantCallBack() {
                                @Override
                                public void result(boolean Success, int requestCode) {
                                    if (Success && requestCode == 29) {
                                        Intent intent1 = new Intent(Intent.ACTION_CALL);
                                        Uri data = Uri.parse("tel:" + bean.getContactsPhone());
                                        intent1.setData(data);
                                        startActivity(intent1);
                                    }
                                }
                            });
                            return;
                        } else {
                            Intent intent1 = new Intent(Intent.ACTION_CALL);
                            Uri data = Uri.parse("tel:" + bean.getContactsPhone());
                            intent1.setData(data);
                            startActivity(intent1);
                        }
                    }
                });
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                initGotoOnclick(position);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setAdapter(adapter);

        searchView.clearFocus();

        TextView title = findViewById(R.id.toolbar_title);

        title.setFocusable(true);

        title.setFocusableInTouchMode(true);

        title.requestFocus();

        initGetData("");
    }

    @Override
    protected void initChangeText(String newText) {
        if (StringUtils.isEmpty(newText)) {
            if (isSearchText) {
                isSearchText = false;
            } else {
                // 当搜索框为空的时候加载默认用户列表
                initGetData("");
            }
        }
    }

    @Override
    public void initGotoOnclick(int position) {
        setPersonData(position, personBeanList.get(position).getID());
    }

    protected void setPersonData(final int position, String id) {
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("ID", taskId);
        params.put("chargeManID", id);
        final ProgressDialog dialog = showProgress(this, "设置中...", false);

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_INSPECTION_SET_PERSON))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.dismiss();
                        HelperView.Toast(InspectionResponsiblePersonActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dialog.dismiss();
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.getBoolean("success")) {

                                DialogUtil.showErrorMessage(InspectionResponsiblePersonActivity.this, "设置负责人成功!", new DialogUtil.IComfirmListener() {
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
                                HelperView.Toast(InspectionResponsiblePersonActivity.this, json.getString("message"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(InspectionResponsiblePersonActivity.this, e.getMessage());
                        }
                    }
                });
    }

    @Override
    public void initGetData(String query) {

        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("keyword", query);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_INSPECTION_GET_RESPONSIBLE_PERSON))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        HelperView.Toast(InspectionResponsiblePersonActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        //LogUtils.showLoge("获取要设置的用户列表1212---", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.getBoolean("success")) {
                                JSONArray jsonArray = json.getJSONArray("data");
                                final List<InspectionSetPersonBackBean> list = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<InspectionSetPersonBackBean>>() {
                                }.getType());
                                if (list != null && list.size() > 0) {
                                    personBeanList.clear();
                                    personBeanList.addAll(list);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    HelperView.Toast(InspectionResponsiblePersonActivity.this, "未发现用户信息!");
                                }
                            } else {
                                HelperView.Toast(InspectionResponsiblePersonActivity.this, json.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(InspectionResponsiblePersonActivity.this, e.getMessage());
                        }
                    }
                });
    }
}
