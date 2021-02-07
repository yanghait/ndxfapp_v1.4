package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.SPUtils;
import com.chaychan.library.BottomBarItem;
import com.chaychan.library.BottomBarLayout;
import com.ynzhxf.nd.xyfirecontrolapp.GloblePlantformDatas;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.IUserMsgLogNotSeeCountPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.impl.MessagePersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.ui.HomeViewPager;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.fragment.MaintenanceIndexFragment;
import com.ynzhxf.nd.xyfirecontrolapp.view.fragment.MessageFragmentNew;
import com.ynzhxf.nd.xyfirecontrolapp.view.fragment.OwnerIndexFragment;
import com.ynzhxf.nd.xyfirecontrolapp.view.fragment.PersonFragment;
import com.ynzhxf.nd.xyfirecontrolapp.view.fragment.StatisticsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 业主页面
 */

public class EnterpriseMainActivity extends BaseActivity implements IUserMsgLogNotSeeCountPersenter.IUserMsgLogNotSeeCountView {
    //用户未读消息获取桥梁
    private IUserMsgLogNotSeeCountPersenter msgLogNotSeeCountPersenter;

    ///用户消息消息数量获取线程是否运行
    private boolean isRunMsgLogNotSeeGet;
    //活动是否处于暂停状态
    private boolean activityIsPause;

    //获取消息网络异常状态是否已通知用户
    private boolean hasNoty = false;

    //时间记录器
    private int hasLostTime = 0;

    //是否完成上一次的小数数量数据请求
    private boolean finishMsgRequest = true;

    private HomeViewPager vp_box;

    private List<Fragment> fragmentList = new ArrayList<>();

    private int loginType = 3;

    private BottomBarLayout mBottomBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compentent_main1);
        vp_box = findViewById(R.id.vp_box);
        mBottomBarLayout = findViewById(R.id.bbl);
        msgLogNotSeeCountPersenter = MessagePersenterFactory.getUserMsgLogNotSeeCountPersenterImpl(this);
        addPersenter(msgLogNotSeeCountPersenter);
        isRunMsgLogNotSeeGet = true;
        activityIsPause = false;
        loginType = SPUtils.getInstance().getInt("LoginType");
        init();
        msgCoutnTh.start();
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (intent.getFlags() == Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TOP) {
            mBottomBarLayout.setCurrentItem(2);
        }
    }

    /**
     * 初始化
     */
    private void init() {

        if (loginType == 4) {
            fragmentList.add(MaintenanceIndexFragment.newInstance());
        } else {
            fragmentList.add(OwnerIndexFragment.newInstance());
        }
        fragmentList.add(StatisticsFragment.newInstance());
        fragmentList.add(MessageFragmentNew.newInstance());
        fragmentList.add(PersonFragment.newInstance());

        vp_box.setOffscreenPageLimit(3);
        // 初始化viewpager
        vp_box.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return fragmentList.get(0);
                    case 1:
                        return fragmentList.get(1);
                    case 2:
                        return fragmentList.get(2);
                    case 3:
                        return fragmentList.get(3);
                }
                return null;
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

        // 处理底部导航栏
        mBottomBarLayout.setOnItemSelectedListener(new BottomBarLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(BottomBarItem bottomBarItem, int i, int i1) {
                vp_box.setCurrentItem(i1);
            }
        });

        mBottomBarLayout.setViewPager(vp_box);

        mBottomBarLayout.setSmoothScroll(true);
    }

    /**
     * 暂停状态
     */
    @Override
    protected void onPause() {
        super.onPause();
        activityIsPause = true;
    }

    /**
     * 启用状态
     */
    @Override
    protected void onResume() {
        super.onResume();
        activityIsPause = false;

    }

    /**
     * 销毁
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunMsgLogNotSeeGet = false;
    }

    //消息数量获取线程
    private Thread msgCoutnTh = new Thread(new Runnable() {
        @Override
        public void run() {
            while (isRunMsgLogNotSeeGet) {
                try {
                    Thread.sleep(300);
                    if (!activityIsPause && finishMsgRequest) {

                        hasLostTime += 300;
                        if (hasLostTime >= GloblePlantformDatas.messageLogCountTime) {
                            hasLostTime = 0;
                            finishMsgRequest = false;
                            msgLogNotSeeCountPersenter.doUserUserMsgLogNotSeeCount();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    /**
     * 获取用户还未查看的消息数量
     */
    @Override
    public void callBackUserMsgLogNotSeeCount(ResultBean<Integer, String> result) {
        if (result.isSuccess()) {
            int queryData = result.getData();
            if (queryData > 1000) {
                queryData = 999;
            }

            mBottomBarLayout.setUnread(2, queryData);
            GloblePlantformDatas.UserNotSeeCount = result.getData();
            hasNoty = false;
        }

        finishMsgRequest = true;
    }

    @Override
    public void callBackError(ResultBean<String, String> resultBean, String code) {
        super.callBackError(resultBean, code);
        finishMsgRequest = true;
    }
}
