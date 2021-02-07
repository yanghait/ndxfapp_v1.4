package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.SPUtils;
import com.google.android.material.tabs.TabLayout;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.company.CompanyMyOrderListFragment;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.fragment.OwnerMyOrderListFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OwnerMyWorkOrderActivity extends BaseActivity implements OnDateSetListener {
    private TabLayout scrollTab;
    public ViewPager vpContent;
    public ArrayList<String> mTitles = new ArrayList<>();

    public List<Fragment> fragmentList;
    public TextView tv_start_time;
    public TextView tv_end_time;


    //时间选择控件初始时间
    public Calendar initStartTime = Calendar.getInstance();
    //时间选择控件结束时间
    public Calendar initEndTime = Calendar.getInstance();

    //开始时间毫秒
    public long startLongTime;

    //结束时间的毫秒数
    public long endLongTime;

    public int selectFlag = 0;

    public int currentPosition = 0;

    public TabFragmentAdapter tabAdapter;

    public boolean isCompany = false;

    protected String projectId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_work_order);
        setContentViewForCharge();
        super.onCreate(savedInstanceState);
        setBarTitle("我的工单");
        setBarTitleForCharge();
        scrollTab = findViewById(R.id.scroll_tab);
        getTabLayout();
        vpContent = findViewById(R.id.vp_content);
        tv_start_time = findViewById(R.id.txt_start_time);
        tv_end_time = findViewById(R.id.txt_end_time);

        if (!getIntent().getBooleanExtra("NoRemoveSPTime", false)) {
            SPUtils.getInstance().remove("start_Time");
            SPUtils.getInstance().remove("end_Time");
        }

        isCompany = getIntent().getBooleanExtra("isCompany", false);

        projectId = getIntent().getStringExtra("projectId");

        initTab();

        initFragment();

        int state = getIntent().getIntExtra("state", 0);

        LogUtils.showLoge("state111===", state + "~~~"+projectId);

        TabLayout.Tab tab = scrollTab.getTabAt(state);
        if (tab != null) {
            tab.select();
        }

    }

    public void setContentViewForCharge() {
    }

    public void setBarTitleForCharge() {
    }

    public void getTabLayout() {
    }

    public void updateTabAdapter() {
        tabAdapter = new TabFragmentAdapter(getSupportFragmentManager());
        vpContent.setAdapter(tabAdapter);
        TabLayout.Tab tab = scrollTab.getTabAt(currentPosition);
        if (tab != null) {
            tab.select();
        }
        tabAdapter.notifyDataSetChanged();
    }

    public void updateOrderStateSelected() {
        TabLayout.Tab tab = scrollTab.getTabAt(currentPosition);
        if (tab != null) {
            tab.select();
        }
    }


    public void initFragment() {

        fragmentList = new ArrayList<>();

        for (int i = 0; i < mTitles.size(); i++) {
            if (isCompany) {
                fragmentList.add(CompanyMyOrderListFragment.newInstance(String.valueOf(i * 10)));
            } else {
                fragmentList.add(OwnerMyOrderListFragment.newInstance(String.valueOf(i * 10),projectId));
            }
        }

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

    public void initTabForCharge() {
    }

    private void initTab() {
        mTitles.add("全部");
        mTitles.add("待确认");
        mTitles.add("待维修");
        mTitles.add("维修中");
        mTitles.add("已挂起");

        mTitles.add("待审核");
        mTitles.add("已返工");
        mTitles.add("已完结");
        mTitles.add("申请终止");
        mTitles.add("已终止");

        initTabForCharge();

        for (int i = 0; i < mTitles.size(); i++) {
            scrollTab.addTab(scrollTab.newTab().setText(mTitles.get(i)));
        }

        scrollTab.setTabMode(TabLayout.MODE_SCROLLABLE);

        scrollTab.setTabTextColors(getResources().getColor(R.color.black), getResources().getColor(R.color.primary_text_color));

        scrollTab.setSelectedTabIndicatorColor(getResources().getColor(R.color.primary_text_color));

        scrollTab.setupWithViewPager(vpContent);

        initSelectTime();

        initOnClick();

    }

    public void initSelectTime() {
        initStartTime.add(Calendar.YEAR, -1);
        initEndTime.add(Calendar.YEAR, 1);
        Calendar queryTime = Calendar.getInstance();
        endLongTime = queryTime.getTimeInMillis();
        queryTime.add(Calendar.YEAR, -1);
        startLongTime = queryTime.getTimeInMillis();
        tv_start_time.setText(HelperTool.MillTimeToStringDate(startLongTime).concat(" "));
        tv_end_time.setText(HelperTool.MillTimeToStringDate(endLongTime).concat(" "));

        // 默认传时间
        SPUtils.getInstance().put("start_Time", HelperTool.MillTimeToStringDate(startLongTime));
        SPUtils.getInstance().put("end_Time", HelperTool.MillTimeToStringDate(endLongTime));
    }

    public void initOnClick() {
        tv_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFlag = 0;
                showTimeSelect();
            }
        });
        tv_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFlag = 1;
                showTimeSelect();
            }
        });

    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millSeconds) {
        if (selectFlag == 0) {
            if (millSeconds >= endLongTime) {
                HelperView.Toast(this, "开始时间不能大于等于结束时间!");
                return;
            }
            startLongTime = millSeconds;
        } else {
            if (millSeconds <= startLongTime) {
                HelperView.Toast(this, "结束时间不能小于等于开始时间!");
                return;
            }
            endLongTime = millSeconds;
        }
        tv_start_time.setText(HelperTool.MillTimeToStringDate(startLongTime).concat(" "));
        tv_end_time.setText(HelperTool.MillTimeToStringDate(endLongTime).concat(" "));

        SPUtils.getInstance().put("start_Time", HelperTool.MillTimeToStringDate(startLongTime));
        SPUtils.getInstance().put("end_Time", HelperTool.MillTimeToStringDate(endLongTime));

        initCurrentFragment();
    }

    public void initCurrentFragment() {
        if (currentPosition > 0 && currentPosition < 9) {

            updateFragment(currentPosition);
            updateFragment(currentPosition - 1);
            updateFragment(currentPosition + 1);
            updateTabAdapter();

        } else if (currentPosition == 0) {

            updateFragment(currentPosition);
            updateFragment(currentPosition + 1);
            updateTabAdapter();
        } else if (currentPosition == 9) {

            updateFragment(currentPosition);
            updateFragment(currentPosition - 1);
            updateTabAdapter();
        }
    }

    public void updateFragment(int position) {
        if (isCompany) {
            fragmentList.remove(position);
            fragmentList.add(position, CompanyMyOrderListFragment.newInstance(String.valueOf(position * 10)));
        } else {
            fragmentList.remove(position);
            fragmentList.add(position, OwnerMyOrderListFragment.newInstance(String.valueOf(position * 10),projectId));
        }
    }

    public void showTimeSelect() {

        String queryTitle = "开始时间选择";
        if (selectFlag == 1) {
            queryTitle = "结束时间选择";
        }
        long selectTime = startLongTime;
        if (selectFlag == 1) {
            selectTime = endLongTime;
        }
        TimePickerDialog mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)//回调
                .setToolBarTextColor(getResources().getColor(R.color.fire_fire))
                .setCancelStringId("取消")//取消按钮
                .setSureStringId("确定")//确定按钮
                .setTitleStringId(queryTitle)//标题
                .setYearText("年")//Year
                .setMonthText("月")//Month
                .setDayText("日")//Day
                .setHourText("时")//Hour
                .setMinuteText("分")//Minute
                .setCyclic(false)//是否可循环
                .setMinMillseconds(initStartTime.getTimeInMillis())//最小日期和时间
                .setMaxMillseconds(initEndTime.getTimeInMillis())//最大日期和时间
                .setCurrentMillseconds(selectTime)
                .setThemeColor(getResources().getColor(R.color.tool_bar))
                .setType(Type.ALL)//类型
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))//未选中的文本颜色
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.tool_bar))//当前文本颜色timepicker_toolbar_bg
                .setWheelItemTextSize(14)//字体大小
                .build();

        mDialogAll.show(getSupportFragmentManager(), "ALL");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (isCompany) {
                switch (requestCode) {
                    case 99:
                        initCurrentFragment();
                        currentPosition = 8;
                        break;
                    case 103:
                        initCurrentFragment();
                        currentPosition = 2;
                        break;
                    case 104:
                        initCurrentFragment();
                        currentPosition = 5;
                        break;
                    case 120:
                        initCurrentFragment();
                        currentPosition = 4;
                        break;
                    case 100:
                    case 101:
                    case 102:
                    default:
                        initCurrentFragment();
                }
                updateOrderStateSelected();
            } else {
                switch (requestCode) {
                    case 99:
                        currentPosition = 9;
                        initCurrentFragment();
                        break;
                    case 100:
                        // 判断业主的审核操作结果是已返工还是已完结
                        if (data != null) {
                            int resultType = data.getIntExtra("ResultType", 2);
                            if (resultType == 1) {
                                currentPosition = 6;
                            } else {
                                currentPosition = 7;
                            }
                        }
                        initCurrentFragment();
                        break;
                    case 102:
                        currentPosition = 9;
                        initCurrentFragment();
                        break;

                    case 101:
                    case 103:
                    case 104:
                    default:
                        initCurrentFragment();
                }
                updateOrderStateSelected();
            }
        }
    }
}
