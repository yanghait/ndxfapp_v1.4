package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.company;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.MultiItemTypeAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionSystemListBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.ui.MyLoadHeader;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;


/**
 * author hbzhou
 * date 2019/1/23 16:39
 */
public class InspectionCompanySystemListActivity extends BaseActivity {

    private String taskId;

    private RecyclerView mRecyclerView;

    private String projectId;

    private ProgressDialog dialog;

    private RefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_company_inspection_system_list);
        super.onCreate(savedInstanceState);
        setBarTitle("巡检系统");

        mRecyclerView = findViewById(R.id.company_inspect_system_recycler);

        refreshLayout = findViewById(R.id.inspection_system_refreshLayout);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration div = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        div.setDrawable(getResources().getDrawable(R.drawable.item_div_system_list));
        mRecyclerView.addItemDecoration(div);

        MyLoadHeader myLoadHeader = new MyLoadHeader(this);
        refreshLayout.setRefreshHeader(myLoadHeader);

        taskId = getIntent().getStringExtra("taskId");

        projectId = getIntent().getStringExtra("projectId");

        dialog = showProgress(this, "加载中...", false);

        initRefreshLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initRefreshLayout() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                dialog.show();
                initData();
            }
        });
    }

    private void initData() {

        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("taskId", taskId);
        //LogUtils.aTag("打印巡检任务ID", taskId);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_COMPANY_INSPECT_GET_SYSTEM_LIST)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                       // ToastUtils.showLong(e.getMessage());
                        LogUtils.aTag(e.getMessage());
                        dialog.dismiss();
                        refreshLayout.finishRefresh();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.aTag("巡检系统列表1212---", response);
                        dialog.dismiss();
                        refreshLayout.finishRefresh();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                //JSONObject json = jsonObject.getJSONObject("data");

                                final List<InspectionSystemListBean> systemListBeans = new Gson().fromJson(jsonObject.getJSONArray("data").toString(),
                                        new TypeToken<List<InspectionSystemListBean>>() {
                                        }.getType());

                                if (systemListBeans == null || systemListBeans.size() == 0) {
                                    ToastUtils.showLong("暂无数据!");
                                    showNoDataView();
                                    return;
                                } else {
                                    hideNoDataView();
                                }

                                CommonAdapter adapter = new CommonAdapter<InspectionSystemListBean>(InspectionCompanySystemListActivity.this,
                                        R.layout.item_company_system_list, systemListBeans) {
                                    @Override
                                    protected void convert(ViewHolder holder, InspectionSystemListBean bean, int position) {
                                        TextView mTitle = holder.getView(R.id.item_title);

                                        TextView mState = holder.getView(R.id.item_state);

                                        mState.setText(bean.getStateShow());

                                        mTitle.setText(bean.getName());

                                        switch (bean.getState()) {
                                            case 0:
                                                mState.setTextColor(getResources().getColor(R.color.fire_fire));
                                                break;
                                            case 10:
                                                mState.setTextColor(getResources().getColor(R.color.inspection_list_btn));
                                                break;
                                        }
                                    }
                                };

                                adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                                        Intent intent = new Intent(InspectionCompanySystemListActivity.this, InspectionCompanyItemActivity.class);
                                        intent.putExtra("taskId", taskId);
                                        intent.putExtra("systemId", systemListBeans.get(position).getID());
                                        intent.putExtra("projectId", projectId);
                                        intent.putExtra("systemName", systemListBeans.get(position).getName());

                                        startActivity(intent);
                                    }

                                    @Override
                                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                                        return false;
                                    }
                                });

                                mRecyclerView.setAdapter(adapter);

                            } else {
                                ToastUtils.showLong(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
}
