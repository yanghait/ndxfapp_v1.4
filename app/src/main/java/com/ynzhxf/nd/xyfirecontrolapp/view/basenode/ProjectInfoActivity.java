package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.suke.widget.SwitchButton;
import com.ynzhxf.nd.xyfirecontrolapp.GloblePlantformDatas;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.VideoChannelBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.count.IProjectEventCountPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.count.impl.CountPersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IProjectInfoPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IProjectSettingRepaireOrTokenPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IProjectVideoPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl.NodeBasePersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.FileShareHomeActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.assessment.RiskAssessmentHomeActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.diagnose.DeviceDiagnoseHomeActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.diagnose.DeviceDiagnoseHomeFinishActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.InspectionSettingActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.InspectionTaskHomeActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.InspectionUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.company.InspectionCompanyForOwnerHomeActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.itangqi.waveloadingview.WaveLoadingView;
import okhttp3.Call;

public class ProjectInfoActivity extends BaseActivity implements IProjectEventCountPersenter.IProjectEventCountView, IProjectInfoPersenter.IProjectInfoView, IProjectSettingRepaireOrTokenPersenter.IProjectSettingRepaireOrTokenView, IProjectVideoPersenter.IProjectVideoView {
    /**
     * 项目名称
     */
    private TextView txtOrgName;

    //刷新控件
    private RefreshLayout refreshLayout;

    /**
     * 通讯状态文本
     */
    private TextView txtProjectState;
    /**
     * 消防接管开关
     */
    private SwitchButton swFiretoken;


    //维修状态
    private SwitchButton swRepaire;

    /**
     * 全局缓存数据
     */
    private GloblePlantformDatas datas;

    //项目信息获取桥梁
    public IProjectInfoPersenter projectInfoPersenter;

    //消防接管和维保状态设置桥梁
    public IProjectSettingRepaireOrTokenPersenter projectFireTokenPersenter;

    //项目事件统计桥梁
    public IProjectEventCountPersenter projectEventCountPersenter;

    //项目视频列表获取桥梁
    public IProjectVideoPersenter projectVideoPersenter;

    //项目对象缓存
    public ProjectNodeBean projectNodeBean;

    //密码输入弹窗
    private AlertDialog alertDialogPwd;

    //密码输入错误消息提示
    private TextView txtError;
    //密码输入窗口标题
    private TextView txtAlarmTitle;

    //密码输入框
    private EditText etxtPwd;
    //取消操作按钮
    private Button btnCancelOperation;
    //确定操作按钮
    private Button btnSureOperation;

    //缓存正确的操作密码
    private String cacheSurePwd = "";

    /**
     * 消防接管和通讯状态设置标识
     */
    private String settingType = "1";


    //实时数据菜单
    public View menuRealData1;
    //实时报警
    public View menuRealAlarm2;
    //实时视频菜单
    public View menuRealViode6;
    //历史报警
    public View menuHistoryAlarm3;
    //项目维保
    public View menuRepaire4;
    //标准化
    public View menuStandar5;
    // 消防管理
    public View menuFireManager7;
    // 文件分享
    public View menuFileShare8;
    // 项目名
    public TextView projectNameInfo;

    // 消防接管
    private TextView fire_fighting;

    // 维保状态
    private TextView main_state;

    public ProgressDialog proDialog;

    private int isShowAssigned = 1;

    //
    protected ImageButton mSetInspect;

    private TextView projectInfoCount;

    protected LinearLayout mMonitorData;

    private int mInspectType = 1;

    protected LinearLayout mMessageState;

    // 需要根据诊断分数设置颜色
    protected Toolbar mToolBar;
    protected LinearLayout mDiagnoseBack;

    // 项目风险评估结果
    protected LinearLayout mProjectAssessmentLayout;
    protected TextView mProjectAssessmentText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        datas = GloblePlantformDatas.getInstance();
        Intent intent = getIntent();
        Object queryPro = intent.getSerializableExtra("projectData");
        if (queryPro == null) {
            HelperView.Toast(this, "未找到项目");
            finish();
            return;
        }
        projectNodeBean = (ProjectNodeBean) queryPro;
        setContentView(R.layout.activity_project_info);
        setBarTitle("项目状态");
        super.onCreate(savedInstanceState);
        mMonitorData = findViewById(R.id.monitor_data_point);
        menuRealData1 = findViewById(R.id.project_info_table1);
        menuRealAlarm2 = findViewById(R.id.project_info_table2);
        menuRealViode6 = findViewById(R.id.project_info_table8);
        menuHistoryAlarm3 = findViewById(R.id.project_info_table3);
        menuRepaire4 = findViewById(R.id.project_info_table4);
        menuStandar5 = findViewById(R.id.project_info_table9);
        menuFireManager7 = findViewById(R.id.project_info_table6);
        menuFileShare8 = findViewById(R.id.project_info_table5);
        txtOrgName = findViewById(R.id.orgName);
        projectNameInfo = findViewById(R.id.project_info_name);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        txtProjectState = findViewById(R.id.project_info_message);
        mMessageState = findViewById(R.id.message_state);
        swFiretoken = findViewById(R.id.switch_button_fire);
        fire_fighting = findViewById(R.id.fire_fighting);
        main_state = findViewById(R.id.main_state);
        swRepaire = findViewById(R.id.switch_button_main);
        mSetInspect = findViewById(R.id.inspect_setting_btn);
        projectInfoCount = findViewById(R.id.project_info_position);
        mProjectAssessmentLayout = findViewById(R.id.message_assessment);
        mProjectAssessmentText = findViewById(R.id.project_info_assessment);
        projectEventCountPersenter = CountPersenterFactory.getProjectEventCountPersenterImpl(this);
        addPersenter(projectEventCountPersenter);
        projectInfoPersenter = NodeBasePersenterFactory.getProjectInfoPersenterImpl(this);
        addPersenter(projectInfoPersenter);
        projectFireTokenPersenter = NodeBasePersenterFactory.getProjectSettingRepaireOrTokenPersenter(this);
        addPersenter(projectFireTokenPersenter);
        projectVideoPersenter = NodeBasePersenterFactory.getProjectVideoPersenterImpl(this);
        addPersenter(projectVideoPersenter);
        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                reloadData();
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        View vPwd = getLayoutInflater().inflate(R.layout.alert_option_pwd, null);
        alertDialogPwd = new AlertDialog.Builder(this).setTitle("").setView(vPwd).create();
        txtAlarmTitle = vPwd.findViewById(R.id.txt_title);
        btnSureOperation = vPwd.findViewById(R.id.btn_sure);
        btnCancelOperation = vPwd.findViewById(R.id.btn_cancel);
        txtError = vPwd.findViewById(R.id.txt_error);
        etxtPwd = vPwd.findViewById(R.id.etxt_option_pwd);

        mToolBar = findViewById(R.id.toolbar);
        mDiagnoseBack = findViewById(R.id.project_info_diagnose_layout);

        proDialog = showProgress(this, "加载中,请稍后...", false);

        init();

        initStatistics();

        initDeviceDiagnoseData();
    }

    @Override
    protected void setBarLintColor() {
        //
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDeviceDiagnoseData();
        initRiskAssessmentState();
    }

    protected void initDeviceDiagnoseData() {

        final WaveLoadingView mWaveLoadingView = findViewById(R.id.waveLoadingView);
        final Button mDetails = findViewById(R.id.project_info_diagnose_detail);
        final Button mStartDiagnose = findViewById(R.id.project_info_diagnose_start);

        final TextView mDiagnoseTitle = findViewById(R.id.project_info_diagnose_title);
        final TextView mDiagnoseTime = findViewById(R.id.project_info_diagnose_time);
        final TextView mDiagnoseText = findViewById(R.id.diagnose_text_score);
        final TextView mDiagnoseTextOne = findViewById(R.id.diagnose_text_score_one);
        final ImageView mImageView = findViewById(R.id.cir_rotate);

        RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setDuration(1000); // 设置动画时间
        rotateAnimation.setInterpolator(new LinearInterpolator()); // 设置插入器
        rotateAnimation.setRepeatCount(1);
        mImageView.startAnimation(rotateAnimation);

        mWaveLoadingView.setAnimDuration(3000);

        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("projectID", projectNodeBean.getID());
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_DEVICE_DIAGNOSE_GET_INFO)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("诊断信息页0999---", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            if (json.getBoolean("success")) {
                                JSONObject jsonObject = json.getJSONObject("data");

                                if (jsonObject.getBoolean("IsSuccess")) {
                                    mDetails.setVisibility(View.VISIBLE);
                                    SPUtils.getInstance().put("projectDiagnoseID", jsonObject.getString("ID"));
                                }

                                mDiagnoseText.setText(String.valueOf(jsonObject.getInt("AvgEquipmentDiagnoseScore")));

                                mDiagnoseTitle.setText(jsonObject.getString("ResultMsg"));
                                mDiagnoseTime.setText(jsonObject.getString("DiagnoseTime"));

                                int diagnoseScore = jsonObject.getInt("AvgEquipmentDiagnoseScore");

                                if (diagnoseScore < 80 && diagnoseScore >= 0) {
                                    mToolBar.setBackgroundColor(getResources().getColor(R.color.device_diagnose_orange));
                                    mDiagnoseBack.setBackgroundColor(getResources().getColor(R.color.device_diagnose_orange));

                                    StatusBarUtil.setColor(ProjectInfoActivity.this, getResources().getColor(R.color.device_diagnose_orange), 0);
                                    mDetails.setBackground(getResources().getDrawable(R.drawable.project_status_btn_orange));
                                    mStartDiagnose.setTextColor(getResources().getColor(R.color.device_diagnose_orange));

                                    mWaveLoadingView.setCenterTitleColor(getResources().getColor(R.color.device_diagnose_orange));
                                    mWaveLoadingView.setBorderColor(getResources().getColor(R.color.device_diagnose_orange));

                                    mDiagnoseText.setTextColor(getResources().getColor(R.color.device_diagnose_orange));
                                    mDiagnoseTextOne.setTextColor(getResources().getColor(R.color.device_diagnose_orange));
                                } else if (diagnoseScore >= 80 && diagnoseScore < 100) {
                                    mToolBar.setBackgroundColor(getResources().getColor(R.color.device_diagnose_yellow));
                                    mDiagnoseBack.setBackgroundColor(getResources().getColor(R.color.device_diagnose_yellow));

                                    StatusBarUtil.setColor(ProjectInfoActivity.this, getResources().getColor(R.color.device_diagnose_yellow), 0);
                                    mDetails.setBackground(getResources().getDrawable(R.drawable.project_status_btn_yellow));
                                    mStartDiagnose.setTextColor(getResources().getColor(R.color.device_diagnose_yellow));

                                    mWaveLoadingView.setCenterTitleColor(getResources().getColor(R.color.device_diagnose_yellow));
                                    mWaveLoadingView.setBorderColor(getResources().getColor(R.color.device_diagnose_yellow));

                                    mDiagnoseText.setTextColor(getResources().getColor(R.color.device_diagnose_yellow));
                                    mDiagnoseTextOne.setTextColor(getResources().getColor(R.color.device_diagnose_yellow));
                                } else if (diagnoseScore == 100) {
                                    mToolBar.setBackgroundColor(getResources().getColor(R.color.device_diagnose_green));
                                    mDiagnoseBack.setBackgroundColor(getResources().getColor(R.color.device_diagnose_green));

                                    StatusBarUtil.setColor(ProjectInfoActivity.this, getResources().getColor(R.color.device_diagnose_green), 0);
                                    mDetails.setBackground(getResources().getDrawable(R.drawable.project_status_btn_one));
                                    mStartDiagnose.setTextColor(getResources().getColor(R.color.device_diagnose_green));

                                    mWaveLoadingView.setCenterTitleColor(getResources().getColor(R.color.device_diagnose_green));
                                    mWaveLoadingView.setBorderColor(getResources().getColor(R.color.device_diagnose_green));

                                    mDiagnoseText.setTextColor(getResources().getColor(R.color.device_diagnose_green));
                                    mDiagnoseTextOne.setTextColor(getResources().getColor(R.color.device_diagnose_green));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        mDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectInfoActivity.this, DeviceDiagnoseHomeFinishActivity.class);
                intent.putExtra("projectDiagnoseID", SPUtils.getInstance().getString("projectDiagnoseID"));
                startActivity(intent);
            }
        });
        mStartDiagnose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectInfoActivity.this, DeviceDiagnoseHomeActivity.class);
                intent.putExtra("projectId", projectNodeBean.getID());
                intent.putExtra("name", projectNodeBean.getName());
                startActivity(intent);
            }
        });
    }

    protected void initStatistics() {
        View view = findViewById(R.id.project_info_table7);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectInfoActivity.this, ProjectStatisticsActivity.class);
                intent.putExtra("projectId", projectNodeBean.getID());
                startActivity(intent);
            }
        });
    }


    /**
     * 初始化界面
     */
    private void init() {
        //注册确认密码按钮点击事件
        btnSureOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String queyrPwd = etxtPwd.getText().toString();
                if (queyrPwd == null || queyrPwd.length() < 6) {
                    txtError.setText("请输入正确的密码！");
                    return;
                }
                txtError.setText("");
                proDialog.show();
                projectFireTokenPersenter.doProjectSettingRepaireOrToken(projectNodeBean.getID(), queyrPwd, settingType);

            }
        });
        //注册取消按钮点击事件
        btnCancelOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogPwd.dismiss();
            }
        });

        alertDialogPwd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if ("1".equals(settingType)) {
                    swFiretoken.setChecked(false);
                } else if ("2".equals(settingType)) {
                    swRepaire.setChecked(false);
                }
            }
        });

        loadProjectInfo();

        swFiretoken.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    txtAlarmTitle.setText("消防接管设置");
                    settingType = "1";
                    showAlertDialogPwd();
                }
            }
        });

        swRepaire.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    txtAlarmTitle.setText("维保状态设置");
                    settingType = "2";
                    showAlertDialogPwd();
                }
            }
        });

        initInspectUtilData();

        // 巡检配置
        mSetInspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectInfoActivity.this, InspectionSettingActivity.class);
                intent.putExtra("projectId", projectNodeBean.getID());
                startActivity(intent);
            }
        });
        //获取项目信息
        projectInfoPersenter.doProjectInfo(projectNodeBean.getID());

        //获取项目统计数据
        projectEventCountPersenter.doProjectEventCount(projectNodeBean.getID());
    }

    protected void initInspectUtilData() {
        //  判断是否显示巡检配置按钮
        InspectionUtils.initData(projectNodeBean.getID(), null, new InspectionUtils.OnShowSettingInspectCallBack() {
            @Override
            public void OnResult(int type) {
                if (SPUtils.getInstance().getInt("LoginType") == 3 && (type == 2 || type == 3)) {
                    mSetInspect.setVisibility(View.VISIBLE);
                    mInspectType = type;
                }
            }

            @Override
            public void OnCompanyResult(int type) {
                //
            }

            @Override
            public void onShowRealVideo(boolean isShow) {
                TableRow tableRow3 = findViewById(R.id.tab_row3);

                LinearLayout mItemNine = findViewById(R.id.project_info_table9);
                ImageView mItemNineImage = findViewById(R.id.project_info_table9_img);
                TextView mItemNineText = findViewById(R.id.project_info_table9_tv);

                LinearLayout mItemTen = findViewById(R.id.project_info_table10);
                ImageView mItemTenImage = findViewById(R.id.project_info_table10_img);
                TextView mItemTenText = findViewById(R.id.project_info_table10_tv);

                ImageView mItemEightImage = findViewById(R.id.project_info_table8_img);
                TextView mItemEightText = findViewById(R.id.project_info_table8_tv);

                tableRow3.setVisibility(View.VISIBLE);
                if (isShow) {
                    menuRealViode6.setVisibility(View.VISIBLE);

                    //实时视频点击
                    menuRealViode6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            proDialog.show();
                            projectVideoPersenter.doProjectVideo(projectNodeBean.getID());
                        }
                    });

                    mItemNineImage.setImageDrawable(getResources().getDrawable(R.drawable.project_img_nine));
                    mItemNineText.setText("维保方巡检");
                    mItemNine.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(ProjectInfoActivity.this, InspectionCompanyForOwnerHomeActivity.class);
                            intent.putExtra("projectId", projectNodeBean.getID());
                            startActivity(intent);
                        }
                    });

                    mItemTenImage.setImageDrawable(getResources().getDrawable(R.drawable.risk_assessment_state_icon));
                    mItemTenText.setText("风险评估");
                    mItemTen.setVisibility(View.VISIBLE);
                    mItemTen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ProjectInfoActivity.this, RiskAssessmentHomeActivity.class);
                            intent.putExtra("projectId", projectNodeBean.getID());
                            intent.putExtra("name", projectNodeBean.getName());
                            startActivity(intent);
                        }
                    });


                } else {
                    menuRealViode6.setVisibility(View.VISIBLE);

                    mItemEightImage.setImageDrawable(getResources().getDrawable(R.drawable.project_img_nine));
                    mItemEightText.setText("维保方巡检");
                    menuRealViode6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(ProjectInfoActivity.this, InspectionCompanyForOwnerHomeActivity.class);
                            intent.putExtra("projectId", projectNodeBean.getID());
                            startActivity(intent);
                        }
                    });


                    mItemNineImage.setImageDrawable(getResources().getDrawable(R.drawable.risk_assessment_state_icon));
                    mItemNineText.setText("风险评估");
                    mItemNine.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ProjectInfoActivity.this, RiskAssessmentHomeActivity.class);
                            intent.putExtra("projectId", projectNodeBean.getID());
                            intent.putExtra("name", projectNodeBean.getName());
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    /**
     * 显示密码输入窗体
     */
    private void showAlertDialogPwd() {
        if (cacheSurePwd.length() > 0) {
            etxtPwd.setText(cacheSurePwd);
        } else {
            etxtPwd.setText("");
        }
        etxtPwd.setFocusable(true);
        txtError.setText("");
        alertDialogPwd.show();
    }

    /**
     * 加载项目状态的信息
     */
    private void loadProjectInfo() {
        //组织架构名称
        txtOrgName.setText(projectNodeBean.getAddress());
        //设置项目名
        projectNameInfo.setText(projectNodeBean.getName());
        int normalColor = getResources().getColor(R.color.flat_greensea);//正常颜色
        int exceptionColor = getResources().getColor(R.color.flat_alizarin);//异常颜色
        //txtProjectState.setTextColor(getResources().getColor(R.color.white));
        if (projectNodeBean.getConnectionState() == 1) {
            txtProjectState.setText("异常");
            txtProjectState.setTextColor(getResources().getColor(R.color.risk_assessment_state_color_red1));
        } else {
            txtProjectState.setText("正常");
            txtProjectState.setTextColor(getResources().getColor(R.color.risk_assessment_state_color_green));
        }
        //txtProjectState.setText(projectNodeBean.getTrasentConnection());

        initRiskAssessmentState();
        //设置消防接管状态颜色和文本
        int fireColor = exceptionColor;
        if (projectNodeBean.getFireControlTake() == 0) {
            fireColor = normalColor;
        }
        if (projectNodeBean.getFireControlTake() == 1) {
            swFiretoken.setChecked(true);
        } else {
            swFiretoken.setChecked(false);
        }
        fire_fighting.setText(projectNodeBean.getTrasentFireTake());

        fire_fighting.setTextColor(fireColor);
        //设置维保颜色和文本
        int repaireColor = exceptionColor;
        if (!projectNodeBean.isRepair()) {
            repaireColor = normalColor;
            swRepaire.setChecked(false);
        } else {
            swRepaire.setChecked(true);
        }
        main_state.setText(projectNodeBean.getTrasentIsRepair());
        main_state.setTextColor(repaireColor);

        fire_fighting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectInfoActivity.this, ProjectInfoMessageDataActivity.class);

                intent.putExtra("ID", projectNodeBean.getID());

                intent.putExtra("Name", projectNameInfo.getText().toString());

                intent.putExtra("labelType", 1);

                startActivity(intent);
            }
        });

        main_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectInfoActivity.this, ProjectInfoMessageDataActivity.class);

                intent.putExtra("ID", projectNodeBean.getID());

                intent.putExtra("Name", projectNameInfo.getText().toString());

                intent.putExtra("labelType", 3);

                startActivity(intent);
            }
        });

        mMessageState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectInfoActivity.this, ProjectInfoMessageDataActivity.class);

                intent.putExtra("ID", projectNodeBean.getID());

                intent.putExtra("Name", projectNameInfo.getText().toString());

                intent.putExtra("labelType", 2);

                startActivity(intent);
            }
        });
        // 点击监控数据点
        mMonitorData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectInfoActivity.this, ProjectSystemListActivity.class);
                intent.putExtra("data", projectNodeBean);
                startActivity(intent);
            }
        });

        initMenuEvent();
    }

    private void initRiskAssessmentState() {
        mProjectAssessmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectInfoActivity.this, RiskAssessmentHomeActivity.class);
                intent.putExtra("projectId", projectNodeBean.getID());
                intent.putExtra("name", projectNodeBean.getName());
                startActivity(intent);
            }
        });
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("projectId", projectNodeBean.getID());
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_RISK_ASSESSMENT_GET_BASICS_DATA)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                JSONObject json = jsonObject.getJSONObject("data");
                                //mRiskTime = json.getString("RecentEvaluateTime");
                                double score;
                                if ("暂无".equals(json.getString("ProjectMaxRisk"))) {
                                    score = 0;
                                } else {
                                    score = Double.parseDouble(json.getString("ProjectMaxRisk"));
                                }
                                if (score <= 1.0) {
                                    mProjectAssessmentText.setText("安全");
                                    mProjectAssessmentText.setTextColor(getResources().getColor(R.color.risk_assessment_state_color_green));
                                } else if (score > 1.0 && score <= 1.6) {
                                    mProjectAssessmentText.setText("轻度");
                                    mProjectAssessmentText.setTextColor(getResources().getColor(R.color.risk_assessment_state_color_yellow));
                                } else if (score > 1.6 && score <= 2.7) {
                                    mProjectAssessmentText.setText("中度");
                                    mProjectAssessmentText.setTextColor(getResources().getColor(R.color.risk_assessment_state_color_orange));
                                } else if (score > 2.7 && score <= 4.5) {
                                    mProjectAssessmentText.setText("高度");
                                    mProjectAssessmentText.setTextColor(getResources().getColor(R.color.risk_assessment_state_color_red1));
                                } else if (score > 4.5) {
                                    mProjectAssessmentText.setText("严重");
                                    mProjectAssessmentText.setTextColor(getResources().getColor(R.color.risk_assessment_state_color_red2));
                                }
                            } else {
                                ToastUtils.showLong(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 注册菜单点击事件
     */
    public void initMenuEvent() {
        //实时数据菜单点击
        menuRealData1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProjectInfoActivity.this, ProjectSystemListActivity.class);
                intent.putExtra("data", projectNodeBean);
                startActivity(intent);
            }
        });
        //实时报警点击
        menuRealAlarm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProjectInfoActivity.this, ProjectRealAlarmActivity.class);
                intent.putExtra("data", projectNodeBean);
                startActivity(intent);
            }
        });

        //历史报警记录点击
        menuHistoryAlarm3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProjectInfoActivity.this, ProjectHistoryAlarmActivity.class);
                intent.putExtra("data", projectNodeBean);
                startActivity(intent);
            }
        });
        //项目维保点击
        menuRepaire4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProjectInfoActivity.this, MaintenanceManageActivity.class);
                intent.putExtra("data", projectNodeBean);
                startActivity(intent);
            }
        });

        //消防管理
        menuFireManager7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProjectInfoActivity.this, InspectionTaskHomeActivity.class);
                intent.putExtra("projectId", projectNodeBean.getID());
                intent.putExtra("isShow", isShowAssigned);
                startActivity(intent);
            }
        });
        // 文件分享
        menuFileShare8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectInfoActivity.this, FileShareHomeActivity.class);
                intent.putExtra("id", projectNodeBean.getID());
                intent.putExtra("Name", projectNodeBean.getName());
                startActivity(intent);
            }
        });
    }

    /**
     * 重载网路请求数据
     */
    private void reloadData() {
        init();
        initStatistics();
        initDeviceDiagnoseData();
        projectInfoPersenter.doProjectInfo(projectNodeBean.getID());
    }

    /**
     * 数据异常返回
     *
     * @param resultBean
     * @param code       请求标识
     */
    @Override
    public void callBackError(ResultBean<String, String> resultBean, String code) {
        super.callBackError(resultBean, code);
        if (proDialog.isShowing()) {
            proDialog.dismiss();
        }
    }

    /**
     * 项目信息请求数据回调
     *
     * @param result
     */
    @Override
    public void callBackProjectInfo(ResultBean<ProjectNodeBean, String> result) {
        if (proDialog.isShowing()) {
            proDialog.dismiss();
        }
        try {
            if (result.isSuccess()) {
                projectNodeBean = result.getData();
                isShowAssigned = result.getData().getFireInspectUserType();
                loadProjectInfo();

            } else {
                HelperView.Toast(this, result.getMessage());
            }
        } catch (Exception e) {
            HelperView.Toast(this, "项目信息失败:" + e.getMessage());
        }

    }

    /**
     * 项目维保状态设置和维保状态设置数据请求完成回调
     *
     * @param result
     */
    @Override
    public void callBackProjectSettingRepaireOrToken(ResultBean<ProjectNodeBean, String> result) {
        if (proDialog.isShowing()) {
            proDialog.dismiss();
        }
        try {
            if (result.isSuccess()) {//请求成功！
                this.projectNodeBean = result.getData();
                alertDialogPwd.dismiss();
                this.cacheSurePwd = etxtPwd.getText().toString();
                loadProjectInfo();
            } else {
                this.txtError.setText(result.getMessage());
            }
        } catch (Exception e) {
            HelperView.Toast(this, "设置失败:" + e.getMessage());
        }

    }

    /**
     * 项目视频列表获取回调
     *
     * @param result
     */
    @Override
    public void callBackProjectVideo(ResultBean<List<VideoChannelBean>, String> result) {
        if (proDialog.isShowing()) {
            proDialog.dismiss();
        }
        try {
            if (result.isSuccess()) {
                if (result.getData().size() > 0) {
                    Intent intent = new Intent(ProjectInfoActivity.this, ProjectVideoListActivity.class);
                    intent.putExtra("project", projectNodeBean);
                    intent.putExtra("data", result);
                    startActivity(intent);
                } else {
                    HelperView.Toast(this, "该项目未开通视频");
                }
            } else {
                HelperView.Toast(this, result.getMessage());
            }
        } catch (Exception e) {
            HelperView.Toast(this, "项目视频失败:" + e.getMessage());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (proDialog.isShowing()) {
            proDialog.dismiss();
        }
        alertDialogPwd.dismiss();
    }

    @Override
    public void callBackProjectEventCount(Map<String, String> result) {
        if (result.size() <= 0) {
            String msg = "统计信息异常";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtils.isEmpty(result.get("equipmentTotal"))) {
            projectInfoCount.setText(result.get("equipmentTotal"));
        }
    }
}
