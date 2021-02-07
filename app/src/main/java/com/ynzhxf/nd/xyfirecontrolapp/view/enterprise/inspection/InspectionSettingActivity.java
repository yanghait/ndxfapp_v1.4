package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.suke.widget.SwitchButton;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.DialogUtil;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.Call;

/**
 * 巡检配置
 */
public class InspectionSettingActivity extends BaseActivity implements OnDateSetListener {

    private String projectId;

    private SwitchButton mSwitchButton;
    private SwitchButton mAssessSwitchButton;

    private TextView mStartTime;

    private TextView mEndTime;

    private TextView mInspectorMan;

    private int showFlag = 1;

    private Button mSettingConfirm;

    private String openTime;

    private String closeTime;

    private String inspectLeaderId;

    protected ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_inspection_setting);
        super.onCreate(savedInstanceState);

        setBarTitle("项目配置");

        projectId = getIntent().getStringExtra("projectId");

        mSwitchButton = findViewById(R.id.inspect_setting_switch);

        mAssessSwitchButton = findViewById(R.id.inspect_setting_assessment);

        mStartTime = findViewById(R.id.inspect_setting_start_time);

        mEndTime = findViewById(R.id.inspect_setting_end_time);

        mInspectorMan = findViewById(R.id.inspect_setting_person);

        mSettingConfirm = findViewById(R.id.inspect_setting_confirm);

        dialog = showProgress(this, "加载中,请稍后...", false);

        initOnClick();

        initSettingData();
    }

    private void initOnClick() {
        mStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFlag = 1;
                showTimePicker();
            }
        });

        mEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFlag = 2;
                showTimePicker();
            }
        });

        mSettingConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.showSelectMessage(InspectionSettingActivity.this, "确认提交?", new DialogUtil.IComfirmCancelListener() {
                    @Override
                    public void onConfirm() {
                        initConfirmSetting();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
        mInspectorMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InspectionSettingActivity.this, InspectionSettingGetUserListActivity.class);

                startActivityForResult(intent, 12);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == 12) {
                inspectLeaderId = data.getStringExtra("inspectId");
                mInspectorMan.setText(data.getStringExtra("Name"));
            }
        }
    }

    private void initConfirmSetting() {

        if (StringUtils.isEmpty(openTime) || StringUtils.isEmpty(closeTime) || StringUtils.isEmpty(inspectLeaderId)) {
            ToastUtils.showLong("请选择巡检营业时间和巡检负责人!");
            return;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.CHINA);

        try {
            Date startTime1 = formatter.parse(openTime);
            Date startTime2 = formatter.parse(closeTime);

            long one = startTime1.getTime();

            long two = startTime2.getTime();

            if (two <= one) {
                HelperView.Toast(InspectionSettingActivity.this, "歇业时间不能小于营业时间!");
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        HashMap<String, String> params = new HashMap<>();

        params.put("Token", HelperTool.getToken());

        params.put("projectId", projectId);

        params.put("openTime", openTime);
        params.put("closeTime", closeTime);

        params.put("inspectLeaderId", inspectLeaderId);

        params.put("isOn", String.valueOf(mSwitchButton.isChecked()));

        params.put("riskEvaluateIsOn", String.valueOf(mAssessSwitchButton.isChecked()));

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_INSPECTION_SETTING_SAVE)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        HelperView.Toast(InspectionSettingActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.showLoge("保存巡检设置结果!", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.getBoolean("success")) {
                                DialogUtil.showErrorMessage(InspectionSettingActivity.this, "已成功设置!", new DialogUtil.IComfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        finish();
                                    }
                                });
                            } else {
                                HelperView.Toast(InspectionSettingActivity.this, json.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    private void showTimePicker() {

        String queryTitle = "营业时间";
        if (showFlag == 2) {
            queryTitle = "歇业时间";
        }
        TimePickerDialog mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)//回调
                .setToolBarTextColor(getResources().getColor(R.color.fire_fire))
                .setCancelStringId("取消")//取消按钮
                .setSureStringId("确定")//确定按钮
                .setTitleStringId(queryTitle)//标题
                .setHourText("时")//Hour
                .setMinuteText("分")//Minute
                .setCyclic(false)//是否可循环
                .setThemeColor(getResources().getColor(R.color.tool_bar))
                .setType(Type.HOURS_MINS)//类型
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))//未选中的文本颜色
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.tool_bar))//当前文本颜色timepicker_toolbar_bg
                .setWheelItemTextSize(14)//字体大小
                .build();

        mDialogAll.show(getSupportFragmentManager(), "HOUR_MIN");
    }

    private void initSettingData() {
        HashMap<String, String> params = new HashMap<>();

        params.put("Token", HelperTool.getToken());

        params.put("projectId", projectId);

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_INSPECTION_GET_SETTING_DATA)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        HelperView.Toast(InspectionSettingActivity.this, e.getMessage());
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        LogUtils.showLoge("巡检设置数据获取1212---", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {

                                JSONObject json = jsonObject.getJSONObject("data");

                                openTime = json.getString("OpenTime");
                                closeTime = json.getString("CloseTime");
                                inspectLeaderId = json.getString("InspectLeaderId");

                                mStartTime.setText(openTime);

                                mEndTime.setText(closeTime);

                                mInspectorMan.setText(json.getString("InspectLeaderName"));

                                if (json.getBoolean("IsOn")) {
                                    mSwitchButton.setChecked(true);
                                } else {
                                    mSwitchButton.setChecked(false);
                                }

                                if (json.getBoolean("RiskEvaluateIsOn")) {
                                    mAssessSwitchButton.setChecked(true);
                                } else {
                                    mAssessSwitchButton.setChecked(false);
                                }

                            } else {
                                HelperView.Toast(InspectionSettingActivity.this, jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millSeconds) {
        //LogUtils.showLoge("选择的时间1212---", HelperTool.MillTimeToHour(millSeconds));

        if (showFlag == 1) {
            openTime = HelperTool.MillTimeToHour(millSeconds);
            mStartTime.setText(HelperTool.MillTimeToHour(millSeconds));
        } else {
            closeTime = HelperTool.MillTimeToHour(millSeconds);
            mEndTime.setText(HelperTool.MillTimeToHour(millSeconds));
        }
    }
}
