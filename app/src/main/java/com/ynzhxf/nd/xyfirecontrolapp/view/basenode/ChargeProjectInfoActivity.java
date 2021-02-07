package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.FileShareHomeActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.assessment.RiskAssessmentHomeActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.charge.ChargeChartsProjectDetailsActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.file.OperationalPlansActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.InspectionUtils;

public class ChargeProjectInfoActivity extends ProjectInfoActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initMenuEvent() {
        //实时数据菜单点击
        menuRealData1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChargeProjectInfoActivity.this, ProjectSystemListActivity.class);
                intent.putExtra("data", projectNodeBean);
                startActivity(intent);
            }
        });
        //实时报警点击
        menuRealAlarm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChargeProjectInfoActivity.this, ProjectRealAlarmActivity.class);
                intent.putExtra("data", projectNodeBean);
                startActivity(intent);
            }
        });

        //历史报警记录点击
        menuHistoryAlarm3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChargeProjectInfoActivity.this, ProjectHistoryAlarmActivity.class);
                intent.putExtra("data", projectNodeBean);
                startActivity(intent);
            }
        });
        //项目维保点击
        menuRepaire4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChargeProjectInfoActivity.this, MaintenanceChargeActivity.class);
                intent.putExtra("data", projectNodeBean);
                startActivity(intent);
            }
        });

        //menuStandar5.setVisibility(View.GONE);

        // 文件分享
        menuFileShare8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ChargeProjectInfoActivity.this, FileShareHomeActivity.class);
                intent.putExtra("id", projectNodeBean.getID());
                intent.putExtra("Name", projectNodeBean.getName());
                startActivity(intent);
            }
        });

        ImageView img = findViewById(R.id.project_info_table6_img);
        img.setImageDrawable(getResources().getDrawable(R.drawable.charge_plan_one));
        TextView tv = findViewById(R.id.project_info_table6_tv);
        tv.setText("作战预案");

        //作战预案
        menuFireManager7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ChargeProjectInfoActivity.this, OperationalPlansActivity.class);
                intent.putExtra("projectId", projectNodeBean.getID());
                startActivity(intent);
            }
        });

        // 数据分析
        View viewStatistics = findViewById(R.id.project_info_table7);
        viewStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChargeProjectInfoActivity.this, ProjectStatisticsActivity.class);
                intent.putExtra("projectId", projectNodeBean.getID());
                startActivity(intent);
            }
        });
        // 主管部门项目详情入口
        projectNameInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChargeProjectInfoActivity.this, ChargeChartsProjectDetailsActivity.class);
                intent.putExtra("ID", projectNodeBean.getID());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initInspectUtilData() {
        //  判断是否显示巡检配置按钮
        InspectionUtils.initData(projectNodeBean.getID(), null, new InspectionUtils.OnShowSettingInspectCallBack() {
            @Override
            public void OnResult(int type) {

            }

            @Override
            public void OnCompanyResult(int type) {

            }

            @Override
            public void onShowRealVideo(boolean isShow) {
                TableRow tableRow3 = findViewById(R.id.tab_row3);

                LinearLayout mItemNine = findViewById(R.id.project_info_table9);
                ImageView mItemNineImage = findViewById(R.id.project_info_table9_img);
                TextView mItemNineText = findViewById(R.id.project_info_table9_tv);

                ImageView mItemEightImage = findViewById(R.id.project_info_table8_img);
                TextView mItemEightText = findViewById(R.id.project_info_table8_tv);

                tableRow3.setVisibility(View.VISIBLE);
                menuRealViode6.setVisibility(View.VISIBLE);

                if (isShow) {
                    //实时视频点击
                    menuRealViode6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            proDialog.show();
                            projectVideoPersenter.doProjectVideo(projectNodeBean.getID());
                        }
                    });

                    mItemNineImage.setImageDrawable(getResources().getDrawable(R.drawable.risk_assessment_state_icon));
                    mItemNineText.setText("风险评估");
                    mItemNine.setVisibility(View.VISIBLE);
                    mItemNine.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ChargeProjectInfoActivity.this, RiskAssessmentHomeActivity.class);
                            intent.putExtra("projectId", projectNodeBean.getID());
                            intent.putExtra("name", projectNodeBean.getName());
                            startActivity(intent);
                        }
                    });

                } else {
                    mItemEightImage.setImageDrawable(getResources().getDrawable(R.drawable.risk_assessment_state_icon));
                    mItemEightText.setText("风险评估");
                    menuRealViode6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ChargeProjectInfoActivity.this, RiskAssessmentHomeActivity.class);
                            intent.putExtra("projectId", projectNodeBean.getID());
                            intent.putExtra("name", projectNodeBean.getName());
                            startActivity(intent);
                        }
                    });
                    tableRow3.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void initStatistics() {

    }
}
