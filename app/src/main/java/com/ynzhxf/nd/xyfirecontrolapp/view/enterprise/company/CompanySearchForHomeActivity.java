package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.company;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.MultiItemTypeAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.SearchProjectBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.CompanyProjectInfoActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class CompanySearchForHomeActivity extends CompanySearchProjectActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initStartData() {
        adapter = new CommonAdapter<SearchProjectBean>(CompanySearchForHomeActivity.this, R.layout.item_search_project_main, listBean) {
            @Override
            protected void convert(ViewHolder holder, SearchProjectBean searchProjectBean, int position) {
                ((TextView) holder.getView(R.id.search_item_text)).setText(listBean.get(position).getName());
                TextView mAddress = holder.getView(R.id.search_item_text_address);
                if (!StringUtils.isEmpty(listBean.get(position).getAddress())) {
                    mAddress.setVisibility(View.VISIBLE);
                    mAddress.setText(listBean.get(position).getAddress());
                } else {
                    mAddress.setVisibility(View.GONE);
                }
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                finish();

                Intent intent = new Intent(CompanySearchForHomeActivity.this, CompanyProjectInfoActivity.class);
                ProjectNodeBean nodeBean = new ProjectNodeBean();
                nodeBean.setID(listBean.get(position).getID());
                intent.putExtra("projectData", nodeBean);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setAdapter(adapter);
        initGetData("");
    }

    @Override
    public void initGetData(String query) {
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("keyword", query);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_COMPANY_SEARCH_PROJECT))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        HelperView.Toast(CompanySearchForHomeActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray jsonArray = json.getJSONArray("data");
                            final List<SearchProjectBean> list = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<SearchProjectBean>>() {
                            }.getType());
                            if (list != null && list.size() > 0) {
                                listBean.clear();
                                listBean.addAll(list);
                                adapter.notifyDataSetChanged();
                            } else {
                                HelperView.Toast(CompanySearchForHomeActivity.this, "未搜索到项目!");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(CompanySearchForHomeActivity.this, e.getMessage());
                        }
                    }
                });
    }
}
