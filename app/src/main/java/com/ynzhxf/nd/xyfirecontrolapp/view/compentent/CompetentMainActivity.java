package com.ynzhxf.nd.xyfirecontrolapp.view.compentent;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.chaychan.library.BottomBarItem;
import com.chaychan.library.BottomBarLayout;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.ui.HomeViewPager;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.charge.ChargeChartsFragment;
import com.ynzhxf.nd.xyfirecontrolapp.view.fragment.ChargeIndexFragment;
import com.ynzhxf.nd.xyfirecontrolapp.view.fragment.MessageFragmentNew;
import com.ynzhxf.nd.xyfirecontrolapp.view.fragment.PersonFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * 主管部门页面
 */
public class CompetentMainActivity extends BaseActivity {

    private HomeViewPager vp_box;

    private List<Fragment> fragmentList = new ArrayList<>();

    private BottomBarLayout mBottomBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compentent_main);
        mBottomBarLayout = findViewById(R.id.bbl);
        vp_box = findViewById(R.id.vp_box);
        init();
    }

    /**
     * 适配部分平板和手机存在底部虚拟导航栏时底部bar布局向上自动移动来显示布局
     * 修复沉浸式状态栏由于底部虚拟导航栏不正确显示导致无法实现沉浸式的问题
     */
    @Override
    protected void setBarLintColor() {

        getWindow().getDecorView().setFitsSystemWindows(true);
        //透明状态栏 @顶部
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

            getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 初始化
     */
    private void init() {

        fragmentList.add(ChargeIndexFragment.newInstance());//CompetentIndexFragment
        fragmentList.add(ChargeChartsFragment.newInstance());
        fragmentList.add(MessageFragmentNew.newInstance());
        fragmentList.add(PersonFragment.newInstance());

        vp_box.setOffscreenPageLimit(3);

        // 初始化viewpager
        vp_box.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                /*switch (position) {
                    case 0:
                        return fragmentList.get(0);
                    case 1:
                        return fragmentList.get(1);
                    case 2:
                        return fragmentList.get(2);
                }*/
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });

        vp_box.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBottomBarLayout.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // 新的底部导航栏
        mBottomBarLayout.setOnItemSelectedListener(new BottomBarLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(BottomBarItem bottomBarItem, int i, int i1) {
                vp_box.setCurrentItem(i1);
            }
        });

        mBottomBarLayout.setViewPager(vp_box);

        mBottomBarLayout.setSmoothScroll(true);
    }
}
