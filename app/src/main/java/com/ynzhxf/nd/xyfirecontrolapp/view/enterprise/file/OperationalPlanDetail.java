package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.file;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;

public class OperationalPlanDetail extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_operational_detail);
        super.onCreate(savedInstanceState);
        setBarTitle("预案详情");

        TextView plansDetail = findViewById(R.id.operational_plan_detail);

        plansDetail.setText(Html.fromHtml(getIntent().getStringExtra("planDetail")));
    }
}
