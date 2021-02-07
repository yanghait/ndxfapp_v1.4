package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;

import android.content.Intent;

import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.SPUtils;
import com.google.android.material.tabs.TabLayout;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.ui.MyTabLayout;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.charge.ChargeMyOrderFragment;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.OwnerMyWorkOrderActivity;

import java.util.ArrayList;

public class MaintenanceChargeActivity extends OwnerMyWorkOrderActivity {

    private MyTabLayout scrollTab;

    @Override
    public void setContentViewForCharge() {
        setContentView(R.layout.activity_main_charge_order);
    }

    @Override
    public void setBarTitleForCharge() {
        setBarTitle("维保管理");
    }

    @Override
    public void getTabLayout() {
        scrollTab = findViewById(R.id.scroll_tab);

        MyTabLayout.TabViewNumber = 2;

        Intent intent = getIntent();
        Object queryPro = intent.getSerializableExtra("data");

        ProjectNodeBean projectNodeBean = (ProjectNodeBean) queryPro;

        SPUtils.getInstance().put("projectNodeId", projectNodeBean.getID());
    }

    @Override
    public void initFragment() {
        scrollTab.setTabMode(TabLayout.MODE_FIXED);

        fragmentList = new ArrayList<>();

        fragmentList.add(ChargeMyOrderFragment.newInstance(String.valueOf(70)));
        fragmentList.add(ChargeMyOrderFragment.newInstance(String.valueOf(90)));

        tabAdapter = new TabFragmentAdapter(getSupportFragmentManager());

        vpContent.setAdapter(tabAdapter);

        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                TabLayout.Tab tab = scrollTab.getTabAt(position);
                if (tab != null) {
                    tab.select();
                }
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @Override
    public void initCurrentFragment() {

        fragmentList.remove(0);
        fragmentList.add(0, ChargeMyOrderFragment.newInstance(String.valueOf(70)));

        fragmentList.remove(1);
        fragmentList.add(1, ChargeMyOrderFragment.newInstance(String.valueOf(90)));
        updateTabAdapter();
    }

    @Override
    public void initTabForCharge() {
        mTitles.clear();
        mTitles.add("已完结");
        mTitles.add("已终止");
    }
}
