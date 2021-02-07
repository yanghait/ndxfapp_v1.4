package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.assessment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.fragment.RiskAssessmentNormalFragment;
import com.ynzhxf.nd.xyfirecontrolapp.view.fragment.BaseFragment;

import java.util.ArrayList;


/**
 * author hbzhou
 * date 2019/4/28 14:14
 * 风险评估状态页既建筑物列表页
 */
public class RiskAssessmentStateTypeActivity extends BaseActivity {

    private ArrayList<BaseFragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "全部", "安全", "轻度"
            , "中度", "高度", "严重"
    };

    private MyPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_risk_assessment_type);
        super.onCreate(savedInstanceState);
        setBarTitle("建筑物列表");

        int state = getIntent().getIntExtra("state", 0);
        String projectId = getIntent().getStringExtra("projectId");

        for (int i = 0; i < 6; i++) {
            mFragments.add(RiskAssessmentNormalFragment.newInstance(i, projectId));
        }

        ViewPager viewPager = findViewById(R.id.view_pager);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);

        SlidingTabLayout tabLayout = findViewById(R.id.slide_tab_layout);

        tabLayout.setViewPager(viewPager);

        tabLayout.setCurrentTab(state);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
