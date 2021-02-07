package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.file;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.MultiItemTypeAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.SearchProjectBean;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.company.CompanySearchProjectActivity;

import java.util.List;

public class FileShareSearchProjectActivity extends CompanySearchProjectActivity {

    @Override
    public void initStartData() {
        //

        adapter = new CommonAdapter<SearchProjectBean>(FileShareSearchProjectActivity.this, R.layout.item_search_project_main, listBean) {
            @Override
            protected void convert(ViewHolder holder, SearchProjectBean searchProjectBean, int position) {
                ((TextView) holder.getView(R.id.search_item_text)).setText(listBean.get(position).getName());
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

    @Override
    public void initOnSearchOk(List<SearchProjectBean> list) {
        // 处理搜索结果显示
        listBean.clear();
        listBean.addAll(list);
        adapter.notifyDataSetChanged();
    }
}
