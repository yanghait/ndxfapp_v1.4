package com.ynzhxf.nd.xyfirecontrolapp.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.NewsListAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.PagingBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.NewsBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.common.INewsListPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.common.INewsURLPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.common.impl.CommonPersenterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 通知公告（新闻）
 */
public class NewsListActivity extends BaseActivity implements INewsURLPersenter.INewsURLView, INewsListPersenter.INewsListView, NewsListAdapter.NewsItemOnClick {
    //新闻数据集合
    private List<NewsBean> dataList;

    //列表容器
    private RecyclerView recyclerView;
    //当前页码
    private int pageSize = 1;

    //总页数
    private int pageTotalCount = 1;

    //数据适配器
    private NewsListAdapter adapter;

    //下拉刷新控件
    private SmartRefreshLayout refreshLayout;

    INewsURLPersenter urlPersenter;
    INewsListPersenter listPersenter;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_news_list);
        super.onCreate(savedInstanceState);
        setBarTitle("通知公告");
        dataList = new ArrayList<>();

        urlPersenter = CommonPersenterFactory.getNewsURLPersenter(this);
        addPersenter(urlPersenter);
        listPersenter = CommonPersenterFactory.getNewListPersenter(this);
        addPersenter(listPersenter);
        recyclerView = findViewById(R.id.rv_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new NewsListAdapter(dataList, this);
        recyclerView.setAdapter(adapter);
        refreshLayout = findViewById(R.id.refreshLayout);
        //showProgressDig(true, "获取中.....");
        dialog = showProgress(this, "加载中...", false);
        init();
        listPersenter.doNewsList(pageSize);
    }


    private void init() {
        //设置加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(20000);
                if (pageSize > pageTotalCount) {
                    refreshLayout.finishLoadMore(true);
                    return;
                }
                listPersenter.doNewsList(pageSize);
            }
        });
    }

    /**
     * 获取新闻远程加载地址并对点击量加1
     *
     * @param result
     */
    @Override
    public void callBackNewsURL(ResultBean<String, String> result) {
        dialog.dismiss();
        if (result.isSuccess()) {
            Intent intent = new Intent(this, NewsInfoActivity.class);
            intent.putExtra("data", result.getExtra());
            startActivity(intent);
        } else {
            HelperView.Toast(this, result.getMessage());
        }
        hideProgressDig();
    }

    /**
     * 获取新闻列表
     *
     * @param result
     */
    @Override
    public void callBackNewsList(ResultBean<PagingBean<NewsBean>, String> result) {
        refreshLayout.finishLoadMore(true);
        dialog.dismiss();
        if (result == null || result.getData() == null || result.getData().getRows() == null ||
                result.getData().getRows().size() == 0) {
            //HelperView.Toast(this, "暂无数据,请稍后再试!");
            showNoDataView();
            return;
        } else {
            hideNoDataView();
        }
        try {
            if (result.isSuccess()) {
                pageSize++;
                pageTotalCount = result.getData().getTotalPageCount();
                dataList.addAll(result.getData().getRows());
                adapter.notifyDataSetChanged();
            } else {
                HelperView.Toast(this, result.getMessage());
            }
        } catch (Exception e) {
            HelperView.Toast(this, "新闻列表失败：" + e.getMessage());
        }

        hideProgressDig();
    }

    @Override
    public void callBackError(ResultBean<String, String> resultBean, String code) {
        super.callBackError(resultBean, code);
        dialog.dismiss();
        refreshLayout.finishLoadMore(true);
    }

    /**
     * 点击新闻列表项
     *
     * @param newsBean
     */
    @Override
    public void NewsItemOnClick(NewsBean newsBean) {
        //showProgressDig(false);
        dialog.show();
        urlPersenter.doNewsURL(newsBean.getNewsID());
    }
}
