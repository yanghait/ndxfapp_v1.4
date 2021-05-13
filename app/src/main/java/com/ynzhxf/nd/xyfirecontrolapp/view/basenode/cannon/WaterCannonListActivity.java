package com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.WaterCannonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.tool.utils.Utils;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.bean.WaterCannonInfo;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter.WaterCannonListPrenenter;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.view.IWaterCannonListview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaterCannonListActivity extends BaseActivity implements IWaterCannonListview {

    WaterCannonListPrenenter waterCannonListPrenenter;

    private String SystemId = "";

    RecyclerView water_cannon_recycler;

    WaterCannonAdapter waterCannonAdapter;


    Handler handler = new Handler();
    private Runnable getDataTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_water_cannon_list);
        waterCannonListPrenenter = new WaterCannonListPrenenter(this);
        super.onCreate(savedInstanceState);
        if (getIntent().hasExtra("systemId"))
            SystemId = getIntent().getStringExtra("systemId");
        initTitle();
        initLayout();
        initHandler();
    }

    private void initHandler() {
        getDataTask = new Runnable() {
            @Override
            public void run() {
                waterCannonListPrenenter.getLableBySystemId(SystemId);
                handler.postDelayed(this, 2000);
            }
        };
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        } else {
        }
    }


    private void initTitle() {
        ImageView app_title_left_btn = this.findViewById(R.id.app_title_left_btn);
        app_title_left_btn.setOnClickListener(v -> {
            finish();
        });
        TextView app_title_txt = this.findViewById(R.id.app_title_txt);
        app_title_txt.setText("消防水炮列表");
        ImageView app_title_right_img = this.findViewById(R.id.app_title_right_img);
        app_title_right_img.setVisibility(View.VISIBLE);
        app_title_right_img.setImageResource(R.mipmap.icon_cannon_control);
        app_title_right_img.setOnClickListener(v -> {
            Map<Integer, Boolean> map = waterCannonAdapter.getSelectMap();
            ArrayList<WaterCannonInfo> cannonInfoList = new ArrayList<>();
            for (Integer key : map.keySet()) {
                if (map.get(key)) {
                    cannonInfoList.add(waterCannonAdapter.getWaterCannonInfoList().get(key));
                }
            }
            if (cannonInfoList.size() > 0) {
                Intent intent = new Intent(WaterCannonListActivity.this, MoreCannonControlActivity.class);
                intent.putExtra("cannonList", cannonInfoList);
                startActivity(intent);
            } else {
                Utils.showToastTips(this, "请选择至少一个设备!");
            }
        });
    }

    private void initLayout() {
        water_cannon_recycler = this.findViewById(R.id.water_cannon_recycler);
        water_cannon_recycler.setLayoutManager(new LinearLayoutManager(this));
        waterCannonAdapter = new WaterCannonAdapter(this);
        water_cannon_recycler.setAdapter(waterCannonAdapter);

        waterCannonAdapter.setOnItemClickListener(info -> {
            Intent intent = new Intent(WaterCannonListActivity.this, WaterCannonActivity.class);
            intent.putExtra("waterCannonInfo", info);
            startActivity(intent);
        });
    }


    @Override
    public void getWaterCannonListSuccess(List<WaterCannonInfo> data) {
        waterCannonAdapter.updateList(data);
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