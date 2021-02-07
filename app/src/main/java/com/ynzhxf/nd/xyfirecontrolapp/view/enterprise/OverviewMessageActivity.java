package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.OverviewItemBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.OverviewMessageCallBackBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.network.RetrofitUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IProjectInfoPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl.NodeBasePersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.MaintenanceChargeActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.MaintenanceCompanyActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.MaintenanceManageActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.ProjectHistoryAlarmActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.assessment.RiskAssessmentHomeActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.diagnose.DeviceDiagnoseHomeActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.diagnose.DeviceDiagnoseHomeFinishActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.InspectionTaskHomeActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.company.InspectionHomeCompanyActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * author hbzhou
 * date 2019/9/17 12:11
 * 概览消息详情页
 */
public class OverviewMessageActivity extends BaseActivity implements IProjectInfoPersenter.IProjectInfoView {
    private TextView mOverviewTitle;
    private RecyclerView mRecyclerView;
    private List<OverviewItemBean> contentsList = new ArrayList<>();
    public ProjectNodeBean projectNodeBean = new ProjectNodeBean();

    //项目信息获取桥梁
    public IProjectInfoPersenter projectInfoPresenter;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_overview_message);
        super.onCreate(savedInstanceState);
        setBarTitle("本周统计");
        TextView mOverviewDate = findViewById(R.id.overviewDate);
        mOverviewTitle = findViewById(R.id.overviewDetail);
        mRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mOverviewDate.setText(getIntent().getStringExtra("date"));
        //projectNodeBean.setID(getIntent().getStringExtra("projectId"));

        projectInfoPresenter = NodeBasePersenterFactory.getProjectInfoPersenterImpl(this);
        addPersenter(projectInfoPresenter);

        dialog =  showProgress(this,"加载中...",false);
        initData();
    }

    private void initData() {
        RetrofitUtils.getInstance().getOverviewMessageDetails(HelperTool.getToken(), getIntent().getStringExtra("ID"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultBean<OverviewMessageCallBackBean, String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResultBean<OverviewMessageCallBackBean, String> resultBean) {
                        dialog.dismiss();
                        //LogUtils.json(LogUtils.E,resultBean.getData().);
                        if (resultBean.isSuccess() && resultBean.getData() != null && resultBean.getData().getLsProjectId() != null &&
                                resultBean.getData().getLsProjectId().size() > 0) {
                            mOverviewTitle.setText(resultBean.getData().getOverviewMsg());
                            // 根据projectId遍历是否包含各类消息 有消息就按照顺序拼接
                            for (int i = 0; i < resultBean.getData().getLsProjectId().size(); i++) {
                                String allContent = "";
                                String projectName = "";
                                String itemProjectId = "";
                                // 用来记录是否包含各类消息 1包含  0不包含---从左至右依次是工单消息  巡检消息 报警消息 风险评估消息 设备诊断消息
                                Integer[] hasModuleList = new Integer[]{0, 0, 0, 0, 0};
                                String projectId = resultBean.getData().getLsProjectId().get(i);
                                if (!StringUtils.isEmpty(projectId)) {
                                    int workCount = resultBean.getData().getWorkOrderData().getLsWorkOrderData().size();
                                    int inspectCount = resultBean.getData().getInspTaskData().getLsInspectionData().size();
                                    int alarmCount = resultBean.getData().getAlarmData().getLsAlarmData().size();
                                    int fireCount = resultBean.getData().getFireRiskData().getLsFireRiskData().size();
                                    int deviceCount = resultBean.getData().getEquipDetectData().getLsEquipDetectData().size();

                                    //获取某个项目工单消息拼接完成字符串
                                    for (int j = 0; j < workCount; j++) {
                                        if (projectId.equals(resultBean.getData().getWorkOrderData().getLsWorkOrderData().get(j).getProjectId())) {
                                            String temp = resultBean.getData().getWorkOrderData().getDetailTemplate();
                                            temp = temp.replace("{0}", String.valueOf(resultBean.getData().getWorkOrderData().getLsWorkOrderData().get(j).getWorkOrderNotDoneCount()));
                                            projectName = resultBean.getData().getWorkOrderData().getLsWorkOrderData().get(j).getProjectName();
                                            itemProjectId = resultBean.getData().getWorkOrderData().getLsWorkOrderData().get(j).getProjectId();

                                            allContent = allContent.concat(temp);
                                            hasModuleList[0] = 1;
                                            break;
                                        }
                                    }
                                    // 获取巡检消息拼接字符串
                                    for (int j = 0; j < inspectCount; j++) {
                                        if (projectId.equals(resultBean.getData().getInspTaskData().getLsInspectionData().get(j).getProjectId())) {
                                            String temp = resultBean.getData().getInspTaskData().getDetailTemplate();
                                            temp = temp.replace("{0}", String.valueOf(resultBean.getData().getInspTaskData().getLsInspectionData().get(j).getTaskNotDoneCount()));
                                            projectName = resultBean.getData().getInspTaskData().getLsInspectionData().get(j).getProjectName();
                                            itemProjectId = resultBean.getData().getInspTaskData().getLsInspectionData().get(j).getProjectId();

                                            allContent = allContent.concat(temp);
                                            hasModuleList[1] = 1;
                                            break;
                                        }
                                    }
                                    // 获取历史报警消息拼接字符串
                                    for (int j = 0; j < alarmCount; j++) {
                                        if (projectId.equals(resultBean.getData().getAlarmData().getLsAlarmData().get(j).getProjectId())) {
                                            String temp = resultBean.getData().getAlarmData().getDetailTemplate();
                                            temp = temp.replace("{0}", String.valueOf(resultBean.getData().getAlarmData().getLsAlarmData().get(j).getAlarmCount()));
                                            temp = temp.replace("{1}", String.valueOf(resultBean.getData().getAlarmData().getLsAlarmData().get(j).getBreakDownCount()));
                                            temp = temp.replace("{2}", String.valueOf(resultBean.getData().getAlarmData().getLsAlarmData().get(j).getAlarmRate()));
                                            projectName = resultBean.getData().getAlarmData().getLsAlarmData().get(j).getProjectName();
                                            itemProjectId = resultBean.getData().getAlarmData().getLsAlarmData().get(j).getProjectId();

                                            allContent = allContent.concat(temp);
                                            hasModuleList[2] = 1;
                                            break;
                                        }
                                    }

                                    // 获取风险评估等级消息并拼接
                                    for (int j = 0; j < fireCount; j++) {
                                        if (projectId.equals(resultBean.getData().getFireRiskData().getLsFireRiskData().get(j).getProjectId())) {
                                            String temp = resultBean.getData().getFireRiskData().getDetailTemplate();

                                            double riskTitle = Double.parseDouble(resultBean.getData().getFireRiskData().getLsFireRiskData().get(j).getFireRiskRate());
                                            temp = temp.replace("{0}", getRiskText(riskTitle));
                                            projectName = resultBean.getData().getFireRiskData().getLsFireRiskData().get(j).getProjectName();
                                            itemProjectId = resultBean.getData().getFireRiskData().getLsFireRiskData().get(j).getProjectId();

                                            allContent = allContent.concat(temp);
                                            //hasModuleList[3] = 1;
                                            break;
                                        }
                                    }
                                    // 设备诊断消息拼接字符串
                                    for (int j = 0; j < deviceCount; j++) {
                                        if (projectId.equals(resultBean.getData().getEquipDetectData().getLsEquipDetectData().get(j).getProjectId())) {
                                            String temp = resultBean.getData().getEquipDetectData().getDetailTemplate();
                                            temp = temp.replace("{0}", String.valueOf(resultBean.getData().getEquipDetectData().getLsEquipDetectData().get(j).getLowScore()));
                                            projectName = resultBean.getData().getEquipDetectData().getLsEquipDetectData().get(j).getProjectName();
                                            itemProjectId = resultBean.getData().getEquipDetectData().getLsEquipDetectData().get(j).getProjectId();

                                            allContent = allContent.concat(temp);
                                            hasModuleList[4] = 1;
                                            break;
                                        }
                                    }
                                }
                                // 拼接完成一条项目消息 构造一个实体bean
                                if (!StringUtils.isEmpty(allContent) && !StringUtils.isEmpty(projectName)) {
                                    OverviewItemBean bean = new OverviewItemBean();
                                    bean.setAllContent(allContent);
                                    bean.setProjectName(projectName);
                                    bean.setHasModule(hasModuleList);
                                    bean.setProjectId(itemProjectId);
                                    contentsList.add(bean);
                                }
                            }

                            BaseQuickAdapter quickAdapter = new BaseQuickAdapter<OverviewItemBean, BaseViewHolder>(R.layout.item_overview_message, contentsList) {
                                @Override
                                protected void convert(@NonNull BaseViewHolder helper, OverviewItemBean overviewItemBean) {
                                    TextView mProjectName = helper.getView(R.id.itemTitle);
                                    TextView mProjectContent = helper.getView(R.id.itemContent);

                                    //TextView mProjectName = holder.getView(R.id.itemTitle);
                                    //TextView mProjectContent = holder.getView(R.id.itemContent);
                                    mProjectName.setText(overviewItemBean.getProjectName());

                                    String allContent = overviewItemBean.getAllContent();

                                    mProjectContent.setText(allContent);
                                    Pattern pattern = Pattern.compile("\\d+");
                                    Matcher ma = pattern.matcher(allContent);
                                    // 使用SpannableStringBuilder可以实现多个字符串点击和颜色大小自定义
                                    SpannableStringBuilder ssBuilder = new SpannableStringBuilder();
                                    ssBuilder.append(allContent);

                                    Integer[] integers = new Integer[5];
                                    for (int i = 0; i < overviewItemBean.getHasModule().length; i++) {
                                        integers[i] = overviewItemBean.getHasModule()[i];
                                    }

                                    // 处理风险评估风险等级汉字点击事件和字体颜色
                                    int risk = 0;
                                    int resRiskId = 0;
                                    if (allContent.indexOf("安全") > 0) {
                                        risk = allContent.indexOf("安全");
                                        resRiskId = R.color.risk_assessment_state_color_green;
                                    } else if (allContent.indexOf("轻度") > 0) {
                                        risk = allContent.indexOf("轻度");
                                        resRiskId = R.color.risk_assessment_state_color_yellow;
                                    } else if (allContent.indexOf("中度") > 0) {
                                        risk = allContent.indexOf("中度");
                                        resRiskId = R.color.risk_assessment_state_color_orange;
                                    } else if (allContent.indexOf("高度") > 0) {
                                        risk = allContent.indexOf("高度");
                                        resRiskId = R.color.risk_assessment_state_color_red1;
                                    } else if (allContent.indexOf("严重") > 0) {
                                        risk = allContent.indexOf("严重");
                                        resRiskId = R.color.risk_assessment_state_color_red2;
                                    }
                                    if (risk > 0) {
                                        ssBuilder.setSpan(new MyClickText(OverviewMessageActivity.this, resRiskId, 3, overviewItemBean), risk, risk + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    }

                                    //寻找其他事件数量并处理颜色,字体大小和点击事件
                                    int index = 0;

                                    while (ma.find()) {
                                        //LogUtils.eTag("find_matcher-007--", ma.group());
                                        // 过滤掉故障次数和故障率这三个整数字符串不处理（一个浮点数会被匹配成两个整数字符串）分四种情况讨论
                                        if (overviewItemBean.getHasModule()[2] == 1 && overviewItemBean.getHasModule()[0] == 1 && overviewItemBean.getHasModule()[1] == 1 && (index == 3 || index == 4 || index == 5)) {
                                            index++;
                                            continue;
                                        } else if ((overviewItemBean.getHasModule()[2] == 1 && (overviewItemBean.getHasModule()[0] == 1 && overviewItemBean.getHasModule()[1] == 0)) && (index == 2 || index == 3 || index == 4)) {
                                            index++;
                                            continue;
                                        } else if ((overviewItemBean.getHasModule()[2] == 1 && (overviewItemBean.getHasModule()[0] == 0 && overviewItemBean.getHasModule()[1] == 1)) && (index == 2 || index == 3 || index == 4)) {
                                            index++;
                                            continue;
                                        } else if (overviewItemBean.getHasModule()[2] == 1 && overviewItemBean.getHasModule()[1] == 0 && overviewItemBean.getHasModule()[0] == 0 && (index == 1 || index == 2 || index == 3)) {
                                            index++;
                                            continue;
                                        }
                                        for (int i = 0; i < integers.length; i++) {
                                            if (integers[i] == 1) {
                                                switch (i) {
                                                    case 0:
                                                        ssBuilder.setSpan(new MyClickText(OverviewMessageActivity.this, R.color.work_message_color, 0, overviewItemBean), ma.start(), ma.start() + ma.group().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                        break;
                                                    case 1:
                                                        ssBuilder.setSpan(new MyClickText(OverviewMessageActivity.this, R.color.inspect_message_color, 1, overviewItemBean), ma.start(), ma.start() + ma.group().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                        break;
                                                    case 2:
                                                        ssBuilder.setSpan(new MyClickText(OverviewMessageActivity.this, R.color.alarm_message_color, 2, overviewItemBean), ma.start(), ma.start() + ma.group().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                        break;
                                                    case 3:
                                                        break;
                                                    case 4:
                                                        String deviceScore = ma.group();
                                                        if (!StringUtils.isEmpty(deviceScore)) {
                                                            int score = Integer.parseInt(deviceScore);
                                                            if (score < 80) {
                                                                ssBuilder.setSpan(new MyClickText(OverviewMessageActivity.this, R.color.device_diagnose_orange, 4, overviewItemBean), ma.start(), ma.start() + deviceScore.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                                            } else if (score < 100) {
                                                                ssBuilder.setSpan(new MyClickText(OverviewMessageActivity.this, R.color.device_diagnose_yellow, 4, overviewItemBean), ma.start(), ma.start() + deviceScore.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                                            } else if (score == 100) {
                                                                ssBuilder.setSpan(new MyClickText(OverviewMessageActivity.this, R.color.device_diagnose_green, 4, overviewItemBean), ma.start(), ma.start() + deviceScore.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                                            }
                                                        }
                                                        break;
                                                    default:
                                                }
                                                integers[i] = 0;
                                                break;
                                            }
                                        }


                                        mProjectContent.setText(ssBuilder);
                                        mProjectContent.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件
                                        mProjectContent.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明

                                        index++;
                                    }
                                }
                            };

                            quickAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);

//                            CommonAdapter adapter = new CommonAdapter<OverviewItemBean>(OverviewMessageActivity.this, R.layout.item_overview_message, contentsList) {
//                                @Override
//                                protected void convert(ViewHolder holder, OverviewItemBean overviewItemBean, int position) {
//                                    TextView mProjectName = holder.getView(R.id.itemTitle);
//                                    TextView mProjectContent = holder.getView(R.id.itemContent);
//                                    mProjectName.setText(overviewItemBean.getProjectName());
//
//                                    String allContent = overviewItemBean.getAllContent();
//
//                                    mProjectContent.setText(allContent);
//                                    Pattern pattern = Pattern.compile("\\d+");
//                                    Matcher ma = pattern.matcher(allContent);
//                                    // 使用SpannableStringBuilder可以实现多个字符串点击和颜色大小自定义
//                                    SpannableStringBuilder ssBuilder = new SpannableStringBuilder();
//                                    ssBuilder.append(allContent);
//
//                                    Integer[] integers = new Integer[5];
//                                    for (int i = 0; i < overviewItemBean.getHasModule().length; i++) {
//                                        integers[i] = overviewItemBean.getHasModule()[i];
//                                    }
//
//                                    // 处理风险评估风险等级汉字点击事件和字体颜色
//                                    int risk = 0;
//                                    int resRiskId = 0;
//                                    if (allContent.indexOf("安全") > 0) {
//                                        risk = allContent.indexOf("安全");
//                                        resRiskId = R.color.risk_assessment_state_color_green;
//                                    } else if (allContent.indexOf("轻度") > 0) {
//                                        risk = allContent.indexOf("轻度");
//                                        resRiskId = R.color.risk_assessment_state_color_yellow;
//                                    } else if (allContent.indexOf("中度") > 0) {
//                                        risk = allContent.indexOf("中度");
//                                        resRiskId = R.color.risk_assessment_state_color_orange;
//                                    } else if (allContent.indexOf("高度") > 0) {
//                                        risk = allContent.indexOf("高度");
//                                        resRiskId = R.color.risk_assessment_state_color_red1;
//                                    } else if (allContent.indexOf("严重") > 0) {
//                                        risk = allContent.indexOf("严重");
//                                        resRiskId = R.color.risk_assessment_state_color_red2;
//                                    }
//                                    if (risk > 0) {
//                                        ssBuilder.setSpan(new MyClickText(OverviewMessageActivity.this, resRiskId, 3, overviewItemBean), risk, risk + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                                    }
//
//                                    //寻找其他事件数量并处理颜色,字体大小和点击事件
//                                    int index = 0;
//
//                                    while (ma.find()) {
//                                        //LogUtils.eTag("find_matcher-007--", ma.group());
//                                        // 过滤掉故障次数和故障率这三个整数字符串不处理（一个浮点数会被匹配成两个整数字符串）分四种情况讨论
//                                        if (overviewItemBean.getHasModule()[2] == 1 && overviewItemBean.getHasModule()[0] == 1 && overviewItemBean.getHasModule()[1] == 1 && (index == 3 || index == 4 || index == 5)) {
//                                            index++;
//                                            continue;
//                                        } else if ((overviewItemBean.getHasModule()[2] == 1 && (overviewItemBean.getHasModule()[0] == 1 && overviewItemBean.getHasModule()[1] == 0)) && (index == 2 || index == 3 || index == 4)) {
//                                            index++;
//                                            continue;
//                                        } else if ((overviewItemBean.getHasModule()[2] == 1 && (overviewItemBean.getHasModule()[0] == 0 && overviewItemBean.getHasModule()[1] == 1)) && (index == 2 || index == 3 || index == 4)) {
//                                            index++;
//                                            continue;
//                                        } else if (overviewItemBean.getHasModule()[2] == 1 && overviewItemBean.getHasModule()[1] == 0 && overviewItemBean.getHasModule()[0] == 0 && (index == 1 || index == 2 || index == 3)) {
//                                            index++;
//                                            continue;
//                                        }
//                                        for (int i = 0; i < integers.length; i++) {
//                                            if (integers[i] == 1) {
//                                                switch (i) {
//                                                    case 0:
//                                                        ssBuilder.setSpan(new MyClickText(OverviewMessageActivity.this, R.color.work_message_color, 0, overviewItemBean), ma.start(), ma.start() + ma.group().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                                                        break;
//                                                    case 1:
//                                                        ssBuilder.setSpan(new MyClickText(OverviewMessageActivity.this, R.color.inspect_message_color, 1, overviewItemBean), ma.start(), ma.start() + ma.group().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                                                        break;
//                                                    case 2:
//                                                        ssBuilder.setSpan(new MyClickText(OverviewMessageActivity.this, R.color.alarm_message_color, 2, overviewItemBean), ma.start(), ma.start() + ma.group().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                                                        break;
//                                                    case 3:
//                                                        break;
//                                                    case 4:
//                                                        String deviceScore = ma.group();
//                                                        if (!StringUtils.isEmpty(deviceScore)) {
//                                                            int score = Integer.parseInt(deviceScore);
//                                                            if (score < 80) {
//                                                                ssBuilder.setSpan(new MyClickText(OverviewMessageActivity.this, R.color.device_diagnose_orange, 4, overviewItemBean), ma.start(), ma.start() + deviceScore.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                                                            } else if (score < 100) {
//                                                                ssBuilder.setSpan(new MyClickText(OverviewMessageActivity.this, R.color.device_diagnose_yellow, 4, overviewItemBean), ma.start(), ma.start() + deviceScore.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                                                            } else if (score == 100) {
//                                                                ssBuilder.setSpan(new MyClickText(OverviewMessageActivity.this, R.color.device_diagnose_green, 4, overviewItemBean), ma.start(), ma.start() + deviceScore.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                                                            }
//                                                        }
//                                                        break;
//                                                    default:
//                                                }
//                                                integers[i] = 0;
//                                                break;
//                                            }
//                                        }
//
//
//                                        mProjectContent.setText(ssBuilder);
//                                        mProjectContent.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件
//                                        mProjectContent.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明
//
//                                        index++;
//                                    }
//                                }
//                            };

                            mRecyclerView.setAdapter(quickAdapter);
                        } else {
                            ToastUtils.showLong("未发现消息内容!");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        LogUtils.iTag("overviewMessage---onError---", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                    }
                });
    }

    @Override
    public void callBackProjectInfo(ResultBean<ProjectNodeBean, String> result) {
        try {
            if (result.isSuccess()) {
                int isShowAssigned = result.getData().getFireInspectUserType();
                Intent intent = new Intent(OverviewMessageActivity.this, InspectionTaskHomeActivity.class);
                intent.putExtra("projectId", projectNodeBean.getID());
                intent.putExtra("isShow", isShowAssigned);
                startActivity(intent);

            } else {
                HelperView.Toast(this, result.getMessage());
            }
        } catch (Exception e) {
            HelperView.Toast(this, "项目信息失败:" + e.getMessage());
        }
    }

    class MyClickText extends ClickableSpan {
        private Context context;
        private int color;
        private int clickType;
        private OverviewItemBean itemBean;

        private MyClickText(Context context, int color, int clickType, OverviewItemBean itemBean) {
            this.context = context;
            this.color = color;
            this.clickType = clickType;
            this.itemBean = itemBean;
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            super.updateDrawState(ds);
            //设置文本的颜色
            ds.setColor(context.getResources().getColor(color));
            //超链接形式的下划线，false 表示不显示下划线，true表示显示下划线
            ds.setUnderlineText(true);
            //文本字体加粗
            ds.setFakeBoldText(true);
            //ds.setStyle(new StyleSpan(Typeface.BOLD), 0);
            //ds.setTextSize(1.5f);
        }

        @Override
        public void onClick(@NonNull View widget) {
            Intent intent;
            int loginType = SPUtils.getInstance().getInt("LoginType", 0);
            projectNodeBean.setID(itemBean.getProjectId());
            if (loginType == 2) {
                switch (clickType) {
                    case 0:
                        intent = new Intent(OverviewMessageActivity.this, MaintenanceChargeActivity.class);
                        intent.putExtra("data", projectNodeBean);
                        startActivity(intent);
                        //ToastUtils.showLong("待处理工单");
                        break;
                    case 1:
                        //ToastUtils.showLong("待巡检任务");
                        break;
                    case 2:
                        //ToastUtils.showLong("报警次数");
                        intent = new Intent(OverviewMessageActivity.this, ProjectHistoryAlarmActivity.class);
                        intent.putExtra("data", projectNodeBean);
                        startActivity(intent);
                        break;
                    case 3:
                        //ToastUtils.showLong("风险级别");
                        intent = new Intent(OverviewMessageActivity.this, RiskAssessmentHomeActivity.class);
                        intent.putExtra("projectId", projectNodeBean.getID());
                        intent.putExtra("name", itemBean.getProjectName());
                        startActivity(intent);
                        break;
                    case 4:
                        //ToastUtils.showLong("设备诊断");

                        intent = new Intent(OverviewMessageActivity.this, DeviceDiagnoseHomeFinishActivity.class);
                        intent.putExtra("projectDiagnoseID", SPUtils.getInstance().getString("projectDiagnoseID"));
                        startActivity(intent);
                        break;
                    default:
                }

            } else if (loginType == 3) {
                switch (clickType) {
                    case 0:
                        intent = new Intent(OverviewMessageActivity.this, MaintenanceManageActivity.class);
                        intent.putExtra("data", projectNodeBean);
                        startActivity(intent);
                        //ToastUtils.showLong("待处理工单");
                        break;
                    case 1:
                        //ToastUtils.showLong("待巡检任务");
                        projectInfoPresenter.doProjectInfo(projectNodeBean.getID());

                        break;
                    case 2:
                        //ToastUtils.showLong("报警次数");
                        intent = new Intent(OverviewMessageActivity.this, ProjectHistoryAlarmActivity.class);
                        intent.putExtra("data", projectNodeBean);
                        startActivity(intent);
                        break;
                    case 3:
                        //ToastUtils.showLong("风险级别");
                        intent = new Intent(OverviewMessageActivity.this, RiskAssessmentHomeActivity.class);
                        intent.putExtra("projectId", projectNodeBean.getID());
                        intent.putExtra("name", itemBean.getProjectName());
                        startActivity(intent);
                        break;
                    case 4:
                        //ToastUtils.showLong("设备诊断");

                        intent = new Intent(OverviewMessageActivity.this, DeviceDiagnoseHomeActivity.class);
                        intent.putExtra("projectId", projectNodeBean.getID());
                        intent.putExtra("name", itemBean.getProjectName());
                        startActivity(intent);
                        break;
                    default:
                }

            } else if (loginType == 4) {
                switch (clickType) {
                    case 0:
                        intent = new Intent(OverviewMessageActivity.this, MaintenanceCompanyActivity.class);
                        MaintenanceCompanyActivity.detailType = 2;
                        projectNodeBean.setName(itemBean.getProjectName());
                        intent.putExtra("data", projectNodeBean);
                        startActivity(intent);
                        //ToastUtils.showLong("待处理工单");
                        break;
                    case 1:
                        //ToastUtils.showLong("待巡检任务");
                        intent = new Intent(OverviewMessageActivity.this, InspectionHomeCompanyActivity.class);
                        intent.putExtra("projectId", projectNodeBean.getID());
                        startActivity(intent);
                        break;
                    case 2:
                        //ToastUtils.showLong("报警次数");
                        intent = new Intent(OverviewMessageActivity.this, ProjectHistoryAlarmActivity.class);
                        intent.putExtra("data", projectNodeBean);
                        startActivity(intent);
                        break;
                    case 3:
                        //ToastUtils.showLong("风险级别");
                        intent = new Intent(OverviewMessageActivity.this, RiskAssessmentHomeActivity.class);
                        intent.putExtra("projectId", projectNodeBean.getID());
                        intent.putExtra("name", itemBean.getProjectName());
                        startActivity(intent);
                        break;
                    case 4:
                        //ToastUtils.showLong("设备诊断");

                        intent = new Intent(OverviewMessageActivity.this, DeviceDiagnoseHomeActivity.class);
                        intent.putExtra("projectId", projectNodeBean.getID());
                        intent.putExtra("name", itemBean.getProjectName());
                        startActivity(intent);
                        break;
                    default:
                }
            }

        }
    }

    private String getRiskText(double score) {
        String riskScore = "";
        if (score <= 1.0) {
            riskScore = "安全";
        } else if (score > 1.0 && score <= 1.6) {
            riskScore = "轻度";
        } else if (score > 1.6 && score <= 2.7) {
            riskScore = "中度";
        } else if (score > 2.7 && score <= 4.5) {
            riskScore = "高度";
        } else if (score > 4.5) {
            riskScore = "严重";
        }
        return riskScore;
    }
}
