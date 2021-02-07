package com.ynzhxf.nd.xyfirecontrolapp.view.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.statement.StatisticsStatementActivity;

/**
 * author hbzhou
 * date 2019/3/20 12:03
 */
public class StatisticsFragment extends BaseFragment {

    private RelativeLayout mInspectLayout;

    private RelativeLayout mMainLayout;

    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_statistics_layout, null, false);

        mInspectLayout = view.findViewById(R.id.statistics_layout2);

        mMainLayout = view.findViewById(R.id.statistics_layout1);

        mInspectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, StatisticsStatementActivity.class);

                int loginType = SPUtils.getInstance().getInt("LoginType", 3);

                intent.putExtra("title","巡检统计报表");

                if (loginType == 3) {
                    intent.putExtra("projectId", SPUtils.getInstance().getString("OwnerProjectId"));
                    intent.putExtra("projectName", SPUtils.getInstance().getString("OwnerProjectName"));
                    intent.putExtra("urlOne", URLConstant.URL_BASE1 + URLConstant.URL_GET_INSPECT_MONTH_OWNER);
                    intent.putExtra("urlTwo", URLConstant.URL_BASE1 + URLConstant.URL_GET_INSPECT_LIST_OWNER);
                } else if (loginType == 4) {
                    intent.putExtra("projectId", SPUtils.getInstance().getString("CompanyProjectId"));
                    intent.putExtra("projectName", SPUtils.getInstance().getString("CompanyProjectName"));
                    intent.putExtra("urlOne", URLConstant.URL_BASE1 + URLConstant.URL_GET_INSPECT_MONTH_COMPANY);
                    intent.putExtra("urlTwo", URLConstant.URL_BASE1 + URLConstant.URL_GET_INSPECT_LIST_COMPANY);
                }
                startActivity(intent);
            }
        });
        mMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, StatisticsStatementActivity.class);

                int loginType = SPUtils.getInstance().getInt("LoginType", 3);

                intent.putExtra("title","维保统计报表");

                if (loginType == 3) {
                    intent.putExtra("projectId", SPUtils.getInstance().getString("OwnerProjectId"));
                    intent.putExtra("projectName", SPUtils.getInstance().getString("OwnerProjectName"));
                    intent.putExtra("urlOne", URLConstant.URL_BASE1 + URLConstant.URL_GET_MAINTANCE_MONTH_OWNER);
                    intent.putExtra("urlTwo", URLConstant.URL_BASE1 + URLConstant.URL_GET_MAINTANCE_LIST_OWNER);
                } else if (loginType == 4) {
                    intent.putExtra("projectId", SPUtils.getInstance().getString("CompanyProjectId"));
                    intent.putExtra("projectName", SPUtils.getInstance().getString("CompanyProjectName"));
                    intent.putExtra("urlOne", URLConstant.URL_BASE1 + URLConstant.URL_GET_MAINTANCE_MONTH_COMPANY);
                    intent.putExtra("urlTwo", URLConstant.URL_BASE1 + URLConstant.URL_GET_MAINTANCE_LIST_COMPANY);
                }
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public static StatisticsFragment newInstance() {
        return new StatisticsFragment();
    }
}
