package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.assessment;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.assessment.RiskFloorListEntityBean;
import com.ynzhxf.nd.xyfirecontrolapp.ui.NormalDividerItemDecoration;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;

import java.util.List;


/**
 * author hbzhou
 * date 2019/4/29 16:46
 */
public class RiskAssessmentFloorListActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activty_risk_floor_list);
        super.onCreate(savedInstanceState);

        setBarTitle("楼层列表");
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        NormalDividerItemDecoration div = new NormalDividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        div.setDrawable(getResources().getDrawable(R.drawable.divider_shape));
        mRecyclerView.addItemDecoration(div);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        String listBeanStr = getIntent().getStringExtra("JsonData");

        List<RiskFloorListEntityBean> listEntityBeans = new Gson().fromJson(listBeanStr,
                new TypeToken<List<RiskFloorListEntityBean>>() {
                }.getType());

        CommonAdapter adapter = new CommonAdapter<RiskFloorListEntityBean>(this, R.layout.item_floor_list_normal, listEntityBeans) {
            @Override
            protected void convert(ViewHolder holder, RiskFloorListEntityBean buildBean, int position) {

                //if (position < buildingListBeans.size() - 1) {

                LinearLayout mItemView = holder.getView(R.id.item_view);
                mItemView.setBackgroundColor(getResources().getColor(R.color.white));

                TextView mBuildingTitle = holder.getView(R.id.item_risk_title);
                TextView mBuildingStateName = holder.getView(R.id.item_risk_state_name);

                TextView mBuildingR = holder.getView(R.id.item_risk_state_back1);
                TextView mBuildingR3 = holder.getView(R.id.item_risk_state_back2);
                TextView mBuildingR1 = holder.getView(R.id.item_risk_state_back3);
                TextView mBuildingR2 = holder.getView(R.id.item_risk_state_back4);
                TextView mBuildingR0 = holder.getView(R.id.item_risk_state_back5);

                mBuildingTitle.setText(buildBean.getBuildName());

                initItemTextFlag(mBuildingR, buildBean.getR());
                initItemTextFlag(mBuildingR3, buildBean.getR3());
                initItemTextFlag(mBuildingR1, buildBean.getR1());
                initItemTextFlag(mBuildingR2, buildBean.getR2());
                initItemTextFlag(mBuildingR0, buildBean.getR0());

                double score = buildBean.getR_Max();

                if (score <= 1.0) {
                    mBuildingStateName.setText("安全");
                    mBuildingStateName.setBackground(getResources().getDrawable(R.drawable.item_risk_btn_shape_green));
                } else if (score > 1.0 && score <= 1.6) {
                    mBuildingStateName.setText("轻度");
                    mBuildingStateName.setBackground(getResources().getDrawable(R.drawable.item_risk_btn_shape_yellow));

                } else if (score > 1.6 && score <= 2.7) {
                    mBuildingStateName.setText("中度");
                    mBuildingStateName.setBackground(getResources().getDrawable(R.drawable.item_risk_btn_shape_orange));

                } else if (score > 2.7 && score <= 4.5) {
                    mBuildingStateName.setText("高度");
                    mBuildingStateName.setBackground(getResources().getDrawable(R.drawable.item_risk_btn_shape_red1));
                } else if (score > 4.5) {
                    mBuildingStateName.setText("严重");
                    mBuildingStateName.setBackground(getResources().getDrawable(R.drawable.item_risk_btn_shape_red2));
                }
                //}

                /*else {

                    View mItemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_risk_assessment_add_view, null);

                    LinearLayout mLayout = holder.getView(R.id.item_view);

                    mLayout.setBackgroundColor(getResources().getColor(R.color.menu_normal_bac));

                    mLayout.removeAllViews();

                    mLayout.addView(mItemView);
                }*/
            }
        };

        mRecyclerView.setAdapter(adapter);
    }

    private void initItemTextFlag(TextView mFlag, double score) {
        if (score <= 1.0) {
            mFlag.setText("安全");
            mFlag.setBackground(getResources().getDrawable(R.drawable.item_risk_text_shape));
            mFlag.setTextColor(getResources().getColor(R.color.risk_assessment_state_color_green));
        } else if (score > 1.0 && score <= 1.6) {
            mFlag.setText("轻度");
            mFlag.setBackground(getResources().getDrawable(R.drawable.item_risk_text_shape_yellow));
            mFlag.setTextColor(getResources().getColor(R.color.risk_assessment_state_color_yellow));
        } else if (score > 1.6 && score <= 2.7) {
            mFlag.setText("中度");
            mFlag.setBackground(getResources().getDrawable(R.drawable.item_risk_text_shape_orange));
            mFlag.setTextColor(getResources().getColor(R.color.risk_assessment_state_color_orange));
        } else if (score > 2.7 && score <= 4.5) {
            mFlag.setText("高度");
            mFlag.setBackground(getResources().getDrawable(R.drawable.item_risk_text_shape_red1));
            mFlag.setTextColor(getResources().getColor(R.color.risk_assessment_state_color_red1));
        } else if (score > 4.5) {
            mFlag.setText("严重");
            mFlag.setBackground(getResources().getDrawable(R.drawable.item_risk_text_shape_red2));
            mFlag.setTextColor(getResources().getColor(R.color.risk_assessment_state_color_red2));
        }
    }
}
