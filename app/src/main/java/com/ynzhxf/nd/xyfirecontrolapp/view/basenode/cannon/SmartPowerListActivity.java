package com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.SmartPowerAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.bean.SmartPowerInfo;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter.SmartPowerListPresenter;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.view.ISmartPowerListView;

import java.util.List;

public class SmartPowerListActivity extends BaseActivity implements ISmartPowerListView {

    SmartPowerListPresenter smartPowerListPresenter;

    RecyclerView smart_power_recycler;

    SmartPowerAdapter smartPowerAdapter;

    private String smartID = "";

    Handler handler = new Handler();
    private Runnable getDataTask;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_smart_power_list);
        super.onCreate(savedInstanceState);
        smartPowerListPresenter = new SmartPowerListPresenter(this);
        smartID = getIntent().getStringExtra("systemId");
        setBarTitle("智慧用电列表");
        initHandler();
        initLayout();
    }

    private void initHandler() {
        getDataTask = new Runnable() {
            @Override
            public void run() {
                smartPowerListPresenter.getLableBySystemId(smartID);
                handler.postDelayed(this, 2000);
            }
        };
    }

    private void initLayout() {
        smart_power_recycler = this.findViewById(R.id.smart_power_recycler);
        smart_power_recycler.setLayoutManager(new LinearLayoutManager(this));
        smartPowerAdapter = new SmartPowerAdapter(this);
        smart_power_recycler.setAdapter(smartPowerAdapter);

        smartPowerAdapter.setOnItemClickListener((String id) -> {
            dialog = showProgress(this, "设置中...", false);
            smartPowerListPresenter.writeLablevalue(id);
        });
    }

    @Override
    public void getSmartPowerListSuccess(List<SmartPowerInfo> data) {
        smartPowerAdapter.update(data);
    }

    @Override
    public void writeLableSuccess() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(getDataTask, 200);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(getDataTask);
    }
}