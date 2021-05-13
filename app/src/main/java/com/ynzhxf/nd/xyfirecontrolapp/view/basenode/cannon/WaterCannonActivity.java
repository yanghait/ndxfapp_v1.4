package com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.bean.WaterCannonInfo;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter.WaterCannonPresenter;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.view.IWaterCannonView;
import com.ynzhxf.nd.xyfirecontrolapp.widget.HorizontalCirScaleView;
import com.ynzhxf.nd.xyfirecontrolapp.widget.VerticalCirScaleView;
import com.ynzhxf.nd.xyfirecontrolapp.widget.WaterCannonOCView;

import java.util.ArrayList;
import java.util.List;

import static com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter.WaterCannonPresenter.CloseSkyLight;
import static com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter.WaterCannonPresenter.Down;
import static com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter.WaterCannonPresenter.Left;
import static com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter.WaterCannonPresenter.OpenSkyLight;
import static com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter.WaterCannonPresenter.ResetWaterCannon;
import static com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter.WaterCannonPresenter.StopDown;
import static com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter.WaterCannonPresenter.StopLeft;
import static com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter.WaterCannonPresenter.StopUp;
import static com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter.WaterCannonPresenter.Up;
import static com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter.WaterCannonPresenter.WaterColumn;
import static com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter.WaterCannonPresenter.WaterMist;


public class WaterCannonActivity extends BaseActivity implements IWaterCannonView {

    WaterCannonPresenter waterCannonPresenter;

    WaterCannonInfo waterCannonInfo;

    LinearLayout water_cannon_OC_layout;
    WaterCannonOCView waterCannonOCView;
    TextView waterCannonOC_txt;
    LinearLayout water_cannon_open_btn;
    int angle = 0;

    LinearLayout water_cannon_control_layout;

    LinearLayout water_valveState_layout;
    ImageView water_valveState_img;

    LinearLayout water_cannonState_layout;
    LinearLayout water_reset_layout;

    TextView water_cannon_horizontal_txt;
    HorizontalCirScaleView water_cannon_horizontalView;

    TextView water_cannon_vertical_txt;
    VerticalCirScaleView water_cannon_verticalView;

    ImageView cannon_up_btn, cannon_down_btn, cannon_left_btn, cannon_right_btn;

    RadioGroup water_state_rg;

    private float verticalAngle = 0;

    Handler handler = new Handler();
    private Runnable openTask, closeTask, getDataTask;

    private List<String> equipList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_water_cannon);
        super.onCreate(savedInstanceState);
        setPORTRAIT(false);
        waterCannonPresenter = new WaterCannonPresenter(this);
        initTitle();
        initLayout();
        testTime();
        if (getIntent().hasExtra("waterCannonInfo")) {
            waterCannonInfo = (WaterCannonInfo) getIntent().getSerializableExtra("waterCannonInfo");
            equipList.add(waterCannonInfo.getId());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        handler.postDelayed(getDataTask, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(getDataTask);
    }

    private void initTitle() {
        ImageView app_title_left_btn = this.findViewById(R.id.app_title_left_btn);
        app_title_left_btn.setOnClickListener(v -> {
            finish();
        });
        TextView app_title_txt = this.findViewById(R.id.app_title_txt);
        app_title_txt.setText("消防水炮");
    }

    private void testTime() {
        openTask = new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (angle < 60) {
                    waterCannonOCView.setStartAngle(++angle);
                    waterCannonOC_txt.setText("消防水炮伪装开启中  " + angle + "°...");
                    handler.postDelayed(this, 200);
                } else {
                    waterCannonOC_txt.setText("消防水炮伪装已开启");
                    water_cannon_OC_layout.setVisibility(View.GONE);
                    handler.removeCallbacks(openTask);
                }
            }
        };
        closeTask = new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (angle > 0) {
                    waterCannonOCView.setStartAngle(--angle);
                    waterCannonOC_txt.setText("消防水炮伪装关闭中  " + angle + "°...");
                    handler.postDelayed(this, 200);
                } else {
                    waterCannonOC_txt.setText("消防水炮伪装已关闭");
                    handler.removeCallbacks(closeTask);
                }
            }
        };
        getDataTask = new Runnable() {
            @Override
            public void run() {
                waterCannonPresenter.getEquipmentStaus(waterCannonInfo.getId());
                handler.postDelayed(this, 2000);
            }
        };
    }

    private void initLayout() {
        water_cannon_OC_layout = this.findViewById(R.id.water_cannon_OC_layout);
        waterCannonOCView = this.findViewById(R.id.waterCannonOCView);
        waterCannonOC_txt = this.findViewById(R.id.waterCannonOC_txt);
        water_cannon_open_btn = this.findViewById(R.id.water_cannon_open_btn);
        water_cannon_open_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waterCannonPresenter.setWaterCannonCommend(equipList, OpenSkyLight);
                handler.postDelayed(openTask, 200);
            }
        });
        water_cannonState_layout = this.findViewById(R.id.water_cannonState_layout);//水炮状态（开启或关闭）
        water_cannonState_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waterCannonPresenter.setWaterCannonCommend(equipList, CloseSkyLight);
                water_cannon_OC_layout.setVisibility(View.VISIBLE);
                handler.postDelayed(closeTask, 200);
            }
        });
        water_cannon_control_layout = this.findViewById(R.id.water_cannon_control_layout);
        water_valveState_layout = this.findViewById(R.id.water_valveState_layout);//水阀状态（开启或关闭）
        water_valveState_img = this.findViewById(R.id.water_valveState_img);
        water_valveState_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        water_reset_layout = this.findViewById(R.id.water_cannon_control_layout);//复位按钮
        water_reset_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waterCannonPresenter.setWaterCannonCommend(equipList, ResetWaterCannon);
            }
        });

        water_cannon_horizontal_txt = this.findViewById(R.id.water_cannon_horizontal_txt);
        water_cannon_horizontalView = this.findViewById(R.id.water_cannon_horizontalView);

        water_cannon_vertical_txt = this.findViewById(R.id.water_cannon_vertical_txt);
        water_cannon_verticalView = this.findViewById(R.id.water_cannon_verticalView);

        cannon_left_btn = this.findViewById(R.id.cannon_left_btn);
        cannon_right_btn = this.findViewById(R.id.cannon_right_btn);
        cannon_up_btn = this.findViewById(R.id.cannon_up_btn);
        cannon_down_btn = this.findViewById(R.id.cannon_down_btn);

        cannon_left_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        waterCannonPresenter.setWaterCannonCommend(equipList, Left);
                        break;
                    case MotionEvent.ACTION_UP:
                        waterCannonPresenter.setWaterCannonCommend(equipList, StopLeft);
                        break;
                }
                return true;
            }
        });
        cannon_down_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        waterCannonPresenter.setWaterCannonCommend(equipList, Down);
                        break;
                    case MotionEvent.ACTION_UP:
                        waterCannonPresenter.setWaterCannonCommend(equipList, StopDown);
                        break;
                }
                return true;
            }
        });
        cannon_up_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        waterCannonPresenter.setWaterCannonCommend(equipList, Up);
                        break;
                    case MotionEvent.ACTION_UP:
                        waterCannonPresenter.setWaterCannonCommend(equipList, StopUp);
                        break;
                }
                return true;
            }
        });
        cannon_down_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        waterCannonPresenter.setWaterCannonCommend(equipList, Down);
                        break;
                    case MotionEvent.ACTION_UP:
                        waterCannonPresenter.setWaterCannonCommend(equipList, StopDown);
                        break;
                }
                return false;
            }
        });
        water_state_rg = this.findViewById(R.id.water_state_rg);
        water_state_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.water_column_rbtn:
                        waterCannonPresenter.setWaterCannonCommend(equipList, WaterColumn);
                        break;
                    case R.id.water_mist_rbtn:
                        waterCannonPresenter.setWaterCannonCommend(equipList, WaterMist);
                        break;
                }
            }
        });
    }

    @Override
    public void getWaterCannonDateSuccess() {

    }
}