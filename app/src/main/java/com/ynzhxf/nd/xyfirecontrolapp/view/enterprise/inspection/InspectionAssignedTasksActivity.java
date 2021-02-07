package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.ui.MyTabLayout;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class InspectionAssignedTasksActivity extends BaseActivity {

    private MyTabLayout scrollTab;

    private ViewPager vp_content;

    private List<String> mTitles = new ArrayList<>();

    private List<Fragment> fragmentList = new ArrayList<>();

    private String projectId;

    private String taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_inspection_assigned_tasks);
        super.onCreate(savedInstanceState);
        setBarTitle("分配任务");

        scrollTab = findViewById(R.id.inspection_assigned_scroll_tab);

        vp_content = findViewById(R.id.inspection_assigned_vp_content);

        projectId = getIntent().getStringExtra("projectId");

        taskId = getIntent().getStringExtra("taskId");

        initTab();

        initFragment();
    }

    private void initTab() {
        mTitles.add("巡检区域");
        mTitles.add("巡检项(无区域)");

        for (int i = 0; i < mTitles.size(); i++) {
            scrollTab.addTab(scrollTab.newTab().setText(mTitles.get(i)));
        }

        scrollTab.setTabMode(TabLayout.MODE_SCROLLABLE);

        scrollTab.setTabTextColors(getResources().getColor(R.color.black), getResources().getColor(R.color.primary_text_color));

        scrollTab.setSelectedTabIndicatorColor(getResources().getColor(R.color.primary_text_color));

        scrollTab.setupWithViewPager(vp_content);

    }

    public void initFragment() {

        fragmentList = new ArrayList<>();

        //fragmentList.add(InspectionItemFragment.newInstance(projectId, inspectionTypeId, taskId, 0));

        //fragmentList.add(InspectionItemFragment.newInstance(projectId, inspectionTypeId, taskId, 10));

        //fragmentList.add(InspectionItemFragment.newInstance(projectId, inspectionTypeId, taskId, 20));

        fragmentList.add(InspectionAssignedTaskFragment.newInstance(projectId, 0,taskId));

        fragmentList.add(InspectionAssignedTaskFragment.newInstance(projectId, 1,taskId));

        TabFragmentAdapter tabAdapter = new TabFragmentAdapter(getSupportFragmentManager());

        vp_content.setAdapter(tabAdapter);

        vp_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                TabLayout.Tab tab = scrollTab.getTabAt(position);
                if (tab != null) {
                    tab.select();
                }
                //currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    public class TabFragmentAdapter extends FragmentStatePagerAdapter {
        private FragmentManager fm;

        public TabFragmentAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % fragmentList.size();
            return super.instantiateItem(container, position);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return super.getItemPosition(object);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }
    }
}
