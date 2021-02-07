package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.company;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.DialogUtil;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class CompanyConfirmOrderActivity extends BaseActivity implements OnDateSetListener {
    private TextView tv_time;
    private Button hand_over;

    private Calendar initStartTime = Calendar.getInstance();
    private Calendar initEndTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_company_confirm_order);
        super.onCreate(savedInstanceState);
        setBarTitle("确认工单");
        tv_time = findViewById(R.id.hand_over_time);
        hand_over = findViewById(R.id.hand_over_commit);

        ImageButton hand_over_img = findViewById(R.id.hand_over_img);

        final String id = getIntent().getStringExtra("ID");
        hand_over_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeSelect();
            }
        });

        hand_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData(id);
            }
        });
    }

    private void initData(String id) {
        String select = tv_time.getText().toString();
        if (StringUtils.isEmpty(select)) {
            HelperView.Toast(CompanyConfirmOrderActivity.this, "请选择预计完成时间!");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("workOrderId", id);
        params.put("estimateEndTime", select);
        showProgressDig(false,"确认中...");
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_COMPANY_COMMIT_ORDER))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        hideProgressDig();
                        HelperView.Toast(CompanyConfirmOrderActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.showLoge("确认工单结果8357---", response);
                        hideProgressDig();
                        try {
                            JSONObject json = new JSONObject(response);
                            boolean isOK = json.getBoolean("success");
                            if (isOK) {
                                DialogUtil.showDialogMessage(CompanyConfirmOrderActivity.this, "已确认!", new DialogUtil.IComfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        setResult(Activity.RESULT_OK);
                                        finish();
                                    }
                                });
                            } else {
                                HelperView.Toast(CompanyConfirmOrderActivity.this, json.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(CompanyConfirmOrderActivity.this, e.getMessage());
                        }
                    }
                });
    }

    private void showTimeSelect() {
        initStartTime.add(Calendar.YEAR, 0);

        initEndTime.add(Calendar.YEAR, 1);

        Calendar queryTime = Calendar.getInstance();

        queryTime.add(Calendar.DAY_OF_MONTH,1);

        String queryTitle = "预计完成时间";
        long select = queryTime.getTimeInMillis();
        TimePickerDialog mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)//回调
                .setToolBarTextColor(getResources().getColor(R.color.fire_fire))
                .setCancelStringId("取消")//取消按钮
                .setSureStringId("确定")//确定按钮
                .setTitleStringId(queryTitle)//标题
                .setYearText("年")//Year
                .setMonthText("月")//Month
                .setDayText("日")//Day
//                .setHourText("时")//Hour
//                .setMinuteText("分")//Minute
                .setCyclic(false)//是否可循环
                .setMinMillseconds(initStartTime.getTimeInMillis())//最小日期和时间
                .setMaxMillseconds(initEndTime.getTimeInMillis())//最大日期和时间
                .setCurrentMillseconds(select)
                .setThemeColor(getResources().getColor(R.color.tool_bar))
                .setType(Type.YEAR_MONTH_DAY)//类型
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))//未选中的文本颜色
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.tool_bar))//当前文本颜色
                .setWheelItemTextSize(14)//字体大小
                .build();

        mDialogAll.show(getSupportFragmentManager(), "ALL");
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millSeconds) {
        tv_time.setText(HelperTool.MillTimeToYearMonth(millSeconds));
    }
}
