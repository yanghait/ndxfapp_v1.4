package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.InspectionUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.company.InspectionCompanySettingActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.company.InspectionHomeCompanyActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.OwnerMyWorkOrderActivity;

public class CompanyProjectInfoActivity extends ProjectInfoActivity {

    private View view7;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout mTopMessageLayout = findViewById(R.id.top_message_assessment);
        mTopMessageLayout.setVisibility(View.GONE);
        TableRow tableRow3 = findViewById(R.id.tab_row3);
        tableRow3.setVisibility(View.GONE);

        ImageView img = findViewById(R.id.project_info_table4_img);
        TextView textView = findViewById(R.id.project_info_table4_tv);
        textView.setText("维保记录");
        img.setImageDrawable(getResources().getDrawable(R.drawable.com_log_one));


        View view6 = findViewById(R.id.project_info_table6);
        view7 = findViewById(R.id.project_info_table7);
        View view8 = findViewById(R.id.project_info_table8);

        view7.setVisibility(View.INVISIBLE);
        view8.setVisibility(View.GONE);

        ImageView img7 = findViewById(R.id.project_info_table7_img);
        img7.setImageDrawable(getResources().getDrawable(R.drawable.real_video_one));
        TextView tv7 = findViewById(R.id.project_info_table7_tv);
        tv7.setText("实时视频");
        view7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proDialog.show();
                projectVideoPersenter.doProjectVideo(projectNodeBean.getID());
            }
        });


        View view = findViewById(R.id.project_info_table5);
        ImageView img5 = findViewById(R.id.project_info_table5_img);
        img5.setImageDrawable(getResources().getDrawable(R.drawable.project_statistics_one));
        TextView tv = findViewById(R.id.project_info_table5_tv);
        tv.setText("数据分析");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompanyProjectInfoActivity.this, ProjectStatisticsActivity.class);
                intent.putExtra("projectId", projectNodeBean.getID());
                startActivity(intent);
            }
        });

        ImageView img6 = findViewById(R.id.project_info_table6_img);
        img6.setImageDrawable(getResources().getDrawable(R.drawable.fire_manager_one));
        TextView tv6 = findViewById(R.id.project_info_table6_tv);
        tv6.setText("消防管理");
        view6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompanyProjectInfoActivity.this, InspectionHomeCompanyActivity.class);
                intent.putExtra("projectId", projectNodeBean.getID());
                startActivity(intent);
            }
        });


        // 巡检配置
        mSetInspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompanyProjectInfoActivity.this, InspectionCompanySettingActivity.class);
                intent.putExtra("projectId", projectNodeBean.getID());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initInspectUtilData() {
        //
        //  判断是否显示巡检配置按钮
        InspectionUtils.initData(projectNodeBean.getID(), null, new InspectionUtils.OnShowSettingInspectCallBack() {
            @Override
            public void OnResult(int type) {
                //
            }

            @Override
            public void OnCompanyResult(int type) {
                if (SPUtils.getInstance().getInt("LoginType") == 4 && (type == 2 || type == 3)) {
                    mSetInspect.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onShowRealVideo(boolean isShow) {
                if (isShow) {
                    view7.setVisibility(View.VISIBLE);
                    menuRealViode6.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    protected void initStatistics() {
        //
    }

    @Override
    public void initMenuEvent() {

        // 实时数据菜单点击
        menuRealData1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyProjectInfoActivity.this, ProjectSystemListActivity.class);
                intent.putExtra("data", projectNodeBean);
                startActivity(intent);
            }
        });

        // 实时报警点击
        menuRealAlarm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyProjectInfoActivity.this, ProjectRealAlarmActivity.class);
                intent.putExtra("data", projectNodeBean);
                startActivity(intent);
            }
        });

        //历史报警记录点击
        menuHistoryAlarm3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyProjectInfoActivity.this, ProjectHistoryAlarmActivity.class);
                intent.putExtra("data", projectNodeBean);
                startActivity(intent);
            }
        });

        // 维保记录
        menuRepaire4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SPUtils.getInstance().put("projectNodeId", projectNodeBean.getID());

                MaintenanceCompanyActivity.detailType = 2;
                Intent intent = new Intent(CompanyProjectInfoActivity.this, OwnerMyWorkOrderActivity.class);
                intent.putExtra("state", 0);
                intent.putExtra("isCompany", true);
                startActivity(intent);
            }
        });
    }
}
