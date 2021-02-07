package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.company;

import android.content.Intent;
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
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.InspectionItemCompanyFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * author hbzhou
 * date 2019/1/24 09:24
 */
public class InspectionCompanyItemActivity extends BaseActivity {

    private MyTabLayout scrollTab;

    protected ViewPager vp_content;

    protected List<Fragment> fragmentList = new ArrayList<>();

    protected List<String> mTitles = new ArrayList<>();

    protected int currentPosition = 0;

    protected String projectId;

    protected String systemId;

    protected String taskId;

    protected int selectedPosition = 0;

    protected String systemName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_company_inspection_item_list);
        super.onCreate(savedInstanceState);
        setBarTitle("巡检项");

        scrollTab = findViewById(R.id.inspection_item_scroll_tab);

        vp_content = findViewById(R.id.inspection_item_vp_content);

        projectId = getIntent().getStringExtra("projectId");

        taskId = getIntent().getStringExtra("taskId");

        systemId = getIntent().getStringExtra("systemId");

        systemName = getIntent().getStringExtra("systemName");

        initTab();

        initFragment();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        selectedPosition = getIntent().getIntExtra("selectedPosition", 0);

        //LogUtils.showLoge("输出选择的item位置1010---",String.valueOf(intent.getIntExtra("selectedPosition",0)));
    }

    @Override
    protected void onResume() {
        super.onResume();

        initFragment();
    }

    private void initTab() {

        mTitles.add("待巡检");
        mTitles.add("已完成");

        for (int i = 0; i < mTitles.size(); i++) {
            if (i == selectedPosition) {
                scrollTab.addTab(scrollTab.newTab().setText(mTitles.get(i)), true);
            } else {
                scrollTab.addTab(scrollTab.newTab().setText(mTitles.get(i)));
            }
        }

        scrollTab.setTabMode(TabLayout.MODE_FIXED);

        scrollTab.setTabTextColors(getResources().getColor(R.color.black), getResources().getColor(R.color.primary_text_color));

        scrollTab.setSelectedTabIndicatorColor(getResources().getColor(R.color.primary_text_color));

        scrollTab.setupWithViewPager(vp_content);

    }

    private void initFragment() {
        //LogUtils.showLoge("输出选择的item位置1212---",String.valueOf(selectedPosition));

        fragmentList = new ArrayList<>();

        fragmentList.add(InspectionItemCompanyFragment.newInstance(systemName, projectId, systemId, taskId, 0));

        fragmentList.add(InspectionItemCompanyFragment.newInstance(systemName, projectId, systemId, taskId, 10));

        TabFragmentAdapter tabAdapter = new TabFragmentAdapter(getSupportFragmentManager());

        vp_content.setAdapter(tabAdapter);

        vp_content.setCurrentItem(selectedPosition);

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
                selectedPosition = position;
                currentPosition = position;
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
