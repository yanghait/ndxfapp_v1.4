package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.company;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.MultiItemTypeAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.SearchProjectBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class CompanySearchProjectActivity extends BaseActivity {

    public RecyclerView recyclerView;
    public SearchView searchView;

    public CommonAdapter adapter;

    public List<SearchProjectBean> listBean = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_search_project);
        createToolBar((Toolbar) findViewById(R.id.toolbar));
        setBarTitle("项目");

        recyclerView = findViewById(R.id.search_list_view);

        searchView = findViewById(R.id.search_view);

        initRecyclerView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                initGetData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                initChangeText(newText);
                return true;
            }
        });
        searchView.findViewById(androidx.appcompat.R.id.search_plate).setBackground(null);
        searchView.findViewById(androidx.appcompat.R.id.submit_area).setBackground(null);
        searchView.onActionViewExpanded();
        initStartData();
    }

    protected void initChangeText(String newText) {

    }

    public void initStartData() {
        SearchProjectBean searchBean = new SearchProjectBean();
        searchBean.setID(null);
        searchBean.setName("全部项目");
        listBean.add(0, searchBean);
        adapter = new CommonAdapter<SearchProjectBean>(CompanySearchProjectActivity.this, R.layout.item_search_project_main, listBean) {
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
                initGotoOnclick(position);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setAdapter(adapter);

        initGetData("");
    }

    public void initGotoOnclick(int position) {
        Intent intent = new Intent();
        intent.putExtra("ID", listBean.get(position).getID());
        intent.putExtra("Name", listBean.get(position).getName());
        setResult(Activity.RESULT_OK, intent);
        finish();

    }

    protected void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

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
                        HelperView.Toast(CompanySearchProjectActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray jsonArray = json.getJSONArray("data");
                            final List<SearchProjectBean> list = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<SearchProjectBean>>() {
                            }.getType());
                            if (list != null && list.size() > 0) {
                                initOnSearchOk(list);
                            } else {
                                HelperView.Toast(CompanySearchProjectActivity.this, "未搜索到项目!");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(CompanySearchProjectActivity.this, e.getMessage());
                        }
                    }
                });
    }

    public void initOnSearchOk(List<SearchProjectBean> list) {
        listBean.clear();
        SearchProjectBean searchBean = new SearchProjectBean();
        searchBean.setID(null);
        searchBean.setName("全部项目");
        listBean.add(0, searchBean);
        listBean.addAll(list);
        adapter.notifyDataSetChanged();
    }
}
