package com.ynzhxf.nd.xyfirecontrolapp.view.message;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.message.UserMessagePushLogBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.AlarmLogBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.IIgnoreAlarmLogPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.impl.MessagePersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IAlarmLogInfoPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl.NodeBasePersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.HistoryAlarmCreateOrderActivity;

public class AlarmMessageInfoActivity extends BaseActivity implements IAlarmLogInfoPersenter.IAlarmLogInfoView, IIgnoreAlarmLogPersenter.IIgnoreAlarmLogView {
    //消息数据
    private UserMessagePushLogBean userMessagePushLogBean;

    //报警对象
    private AlarmLogBean alarmLogBean;

    //项目名称
    private TextView txtName;
    //事件类型
    private TextView TextEventName;
    //报警位置
    private TextView txtAlarmPostion;
    //报警值
    private TextView txtAlarmValue;
    //报警时间
    private TextView txtAlarmTime;
    //提示消息
    private TextView txtMessage;

    //推送时间
    private TextView txtPushTime;

    //处理状态
    private TextView txtHandelState;

    //按钮状态容器(未处理显示， 已处理隐藏)
    private View lyStateCon;

    //维保 忽略
    private Button btnRepaire;

    // 发起工单
    private Button btnErroeAlarm;

    private IAlarmLogInfoPersenter alarmLogInfoPersenter;
    private IIgnoreAlarmLogPersenter iIgnoreAlarmLogPersenter;

    private String projectId;
    private String baseNodeId;

    private String createorderContent = "";

    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_alarm_message_info);
        super.onCreate(savedInstanceState);
        setBarTitle("报警详情");
        Object queryObj = getIntent().getSerializableExtra("data");
        if (queryObj == null) {
            HelperView.Toast(this, "未参入参数!");
            return;
        }
        userMessagePushLogBean = (UserMessagePushLogBean) queryObj;
        txtName = findViewById(R.id.txt_name);
        TextEventName = findViewById(R.id.txt_event);
        txtAlarmPostion = findViewById(R.id.txt_alarm_position);
        txtAlarmValue = findViewById(R.id.txt_alarm_value);
        txtAlarmTime = findViewById(R.id.txt_alarm_time);
        txtMessage = findViewById(R.id.txt_alarm_msg);
        txtPushTime = findViewById(R.id.txt_push_time);
        txtHandelState = findViewById(R.id.txt_handel_state);
        lyStateCon = findViewById(R.id.ly_state_con);
        btnRepaire = findViewById(R.id.btn_repaire);
        btnErroeAlarm = findViewById(R.id.btn_error_alarm);

        alarmLogInfoPersenter = NodeBasePersenterFactory.getAlarmLogInfoImpl(this);
        addPersenter(alarmLogInfoPersenter);
        iIgnoreAlarmLogPersenter = MessagePersenterFactory.getIgnoreAlarmLogPersenterImpl(this);
        addPersenter(iIgnoreAlarmLogPersenter);

        projectId = getIntent().getStringExtra("projectId");

        init();

        dialog = showProgress(this, "加载中...", false);
        alarmLogInfoPersenter.doAlarmLogInfo(userMessagePushLogBean.getAppPushMsgLogObj().getAlarmLogID());

        int loginType = SPUtils.getInstance().getInt("LoginType", 2);
        if (loginType == 2) {
            lyStateCon.setVisibility(View.INVISIBLE);
        }
    }


    private void init() {
        btnErroeAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlarmMessageInfoActivity.this, HistoryAlarmCreateOrderActivity.class);
                intent.putExtra("projectId", projectId);
                intent.putExtra("baseNodeId", baseNodeId);
                intent.putExtra("content", createorderContent);
                if (StringUtils.isEmpty(projectId) || StringUtils.isEmpty(baseNodeId) || StringUtils.isEmpty(createorderContent)) {
                    HelperView.Toast(AlarmMessageInfoActivity.this, "未获取到报警信息,无法发起工单!");
                    return;
                }
                startActivity(intent);
            }
        });

        btnRepaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                iIgnoreAlarmLogPersenter.doIgnoreAlarmLog(alarmLogBean.getID());
            }
        });
    }

    /**
     * 获取报警记录回调
     *
     * @param result
     */
    @Override
    public void callBackAlarmLogInfo(ResultBean<AlarmLogBean, String> result) {
        dialog.dismiss();
        try {
            if (result.isSuccess()) {
                this.alarmLogBean = result.getData();
                baseNodeId = result.getData().getBaseNodeID();
                projectId = result.getData().getProjectID();

                String queryProName = "";
                String queryPosition = "";
                if (alarmLogBean.getAreaName() != null) {
                    String[] temp = alarmLogBean.getAreaName().split("-");
                    if (temp.length > 3) ;
                    queryProName = temp[2];
                    for (int i = 3; i < temp.length; i++) {
                        if (i < temp.length - 1) {
                            queryPosition = queryPosition + temp[i] + " - ";
                        } else {
                            queryPosition = queryPosition + temp[i];
                        }
                    }
                }
                txtName.setText(queryProName);
                TextEventName.setText(userMessagePushLogBean.getAppPushMsgLogObj().getMessageTitle());
                txtAlarmPostion.setText(queryPosition);
                txtAlarmValue.setText(alarmLogBean.getAlarmValue());
                txtAlarmTime.setText(alarmLogBean.getEventTimeStr());
                txtMessage.setText(userMessagePushLogBean.getAppPushMsgLogObj().getMessageContent());
                txtPushTime.setText(userMessagePushLogBean.getLastPushTimeStr());
                txtHandelState.setText(alarmLogBean.getReviewStateStr());

                createorderContent = queryProName + "\n" + userMessagePushLogBean.getAppPushMsgLogObj().getMessageTitle() + "\n" +
                        queryPosition + "\n" + alarmLogBean.getAlarmValue() + "\n" + alarmLogBean.getEventTimeStr() + "\n" + userMessagePushLogBean.getAppPushMsgLogObj().getMessageContent()
                        + "\n" + userMessagePushLogBean.getLastPushTimeStr();
            } else {
                HelperView.Toast(this, "获取报警记录失败!");
            }
        } catch (Exception e) {
            HelperView.Toast(this, "报警记录失败" + e.getMessage());
        }
    }

    /**
     * 设置消息误报
     *
     * @param result
     */
    @Override
    public void callBackIgnoreAlarmLog(ResultBean<AlarmLogBean, String> result) {
        dialog.dismiss();
        try {
            if (result.isSuccess()) {
                alarmLogBean = result.getData();
                if (alarmLogBean.getReviewState() == 1) {
                    HelperView.Toast(AlarmMessageInfoActivity.this, alarmLogBean.getReviewStateStr());
                    finish();
                } else {
                    HelperView.Toast(AlarmMessageInfoActivity.this, "忽略失败,请稍后再试!");
                }
            } else {
                HelperView.Toast(this, "设置误报失败:" + result.getMessage());
            }
        } catch (Exception e) {
            HelperView.Toast(this, "设置误报失败:" + e.getMessage());
        }

    }

    @Override
    public void callBackError(ResultBean<String, String> resultBean, String action) {
        super.callBackError(resultBean, action);
        dialog.dismiss();
    }
}
