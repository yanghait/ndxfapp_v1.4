package com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.adapter.CannonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.bean.WaterCannonInfo;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter.MoreWaterCannonPresenter;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.view.IMoreWaterCannonView;

import java.util.ArrayList;
import java.util.List;

import static com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter.MoreWaterCannonPresenter.CloseSkyLight;
import static com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter.MoreWaterCannonPresenter.CloseWaterValve;
import static com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter.MoreWaterCannonPresenter.OpenSkyLight;
import static com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter.MoreWaterCannonPresenter.OpenWaterValve;
import static com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter.MoreWaterCannonPresenter.ResetWaterCannon;
import static com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter.MoreWaterCannonPresenter.WaterColumn;
import static com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter.MoreWaterCannonPresenter.WaterMist;

public class MoreCannonControlActivity extends BaseActivity implements IMoreWaterCannonView {

    MoreWaterCannonPresenter moreWaterCannonPresenter;

    RecyclerView more_cannon_recycle;
    CannonAdapter cannonAdapter;

    LinearLayout cannon_OC_layout;
    ImageView cannon_OC_img;
    TextView cannon_OC_txt;

    LinearLayout water_valve_layout;
    ImageView water_valve_img;
    TextView water_valve_txt;

    LinearLayout water_reset_layout;
    ImageView water_reset_img;
    TextView water_reset_txt;

    RadioGroup water_state_rg;

    private List<WaterCannonInfo> cannonInfoList;
    private List<String> equipList = new ArrayList<>();

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_more_cannon_control);
        super.onCreate(savedInstanceState);
        moreWaterCannonPresenter = new MoreWaterCannonPresenter(this);
        if (getIntent().hasExtra("cannonList")) {
            cannonInfoList = (List<WaterCannonInfo>) getIntent().getSerializableExtra("cannonList");
            for (WaterCannonInfo info : cannonInfoList) {
                equipList.add(info.getId());
            }

        }
        initTitle();
        initLayout();
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

        });
    }

    private void initLayout() {
        more_cannon_recycle = this.findViewById(R.id.more_cannon_recycle);
        cannonAdapter = new CannonAdapter(this);
        more_cannon_recycle.setLayoutManager(new GridLayoutManager(this, 4));
        more_cannon_recycle.setAdapter(cannonAdapter);
        cannonAdapter.update(cannonInfoList);

        cannon_OC_layout = this.findViewById(R.id.cannon_OC_layout);
        cannon_OC_img = this.findViewById(R.id.cannon_OC_img);
        cannon_OC_txt = this.findViewById(R.id.cannon_OC_txt);

        water_valve_layout = this.findViewById(R.id.water_valve_layout);
        water_valve_img = this.findViewById(R.id.water_valve_img);
        water_valve_txt = this.findViewById(R.id.water_valve_txt);

        water_reset_layout = this.findViewById(R.id.water_reset_layout);
        water_reset_img = this.findViewById(R.id.water_reset_img);
        water_reset_txt = this.findViewById(R.id.water_reset_txt);

        water_state_rg = this.findViewById(R.id.water_state_rg);

        setListener();
    }

    private void setListener() {
        cannon_OC_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sentToCannon(OpenSkyLight);
                sentToCannon(CloseSkyLight);
            }
        });
        water_valve_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sentToCannon(OpenWaterValve);
                sentToCannon(CloseWaterValve);
            }
        });
        water_reset_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sentToCannon(ResetWaterCannon);
            }
        });
        water_state_rg.setOnCheckedChangeListener((checjButton, id) -> {
            switch (id) {
                case R.id.water_column_rbtn:
                    sentToCannon(WaterColumn);
                    break;
                case R.id.water_mist_rbtn:
                    sentToCannon(WaterMist);
                    break;
            }
        });
    }

    public void sentToCannon(int type) {
        dialog = showProgress(this, "加载中...", false);
        moreWaterCannonPresenter.setWaterCannonCommend(equipList, type);
    }

    @Override
    public void sendCmdToCannonSuccess() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void getWaterCannonDateSuccess() {

    }
}