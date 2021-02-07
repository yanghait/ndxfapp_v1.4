package com.ynzhxf.nd.xyfirecontrolapp.view.fragment;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.github.mikephil.charting.charts.PieChart;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.PagingBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.CompanyIndexEventBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.NewsBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.MainOwnerMarkCountBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.SearchProjectBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.message.MessageUpdateHeightBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.network.RetrofitUtils;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.common.INewsListPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.common.INewsURLPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.common.impl.CommonPersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.ui.CustomGridView;
import com.ynzhxf.nd.xyfirecontrolapp.ui.SpecialLoadHeader;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.PieChartUtil;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.NewsInfoActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.NewsListActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.MaintenanceCompanyActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.PLVideoTextureHomeActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.company.CompanyTwoPageActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.FileShareHomeActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.company.CompanySearchForHomeActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.adapter.SaleAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.widget.FragmentViewPager;
import com.ynzhxf.nd.xyfirecontrolapp.widget.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.bakumon.library.view.BulletinView;
import okhttp3.Call;

/**
 * author hbzhou
 * date 2019/6/25 14:19
 * 新版本维保公司首页 新增事件统计 维保工单统计图
 */
public class MaintenanceIndexFragment extends BaseFragment implements INewsListPersenter.INewsListView, INewsURLPersenter.INewsURLView, View.OnClickListener {

    private Context context;

    private BulletinView mBulletView;
    private TextView mMoreMessage;

    private RefreshLayout mRefreshLayout;

    List<Disposable> disposableList = new ArrayList<>();

    private PieChart mPieChart;
    private LinearLayout mPieLayout;
    private CustomGridView mGridView;

    private RelativeLayout mMiddleIcon1;
    private RelativeLayout mMiddleIcon2;
    private RelativeLayout mMiddleIcon3;
    private RelativeLayout mMiddleIcon4;

    private TextView mMiddleIconCount1;
    private TextView mMiddleIconCount2;
    private TextView mMiddleIconCount3;
    private TextView mMiddleIconCount4;

    private SlidingTabLayout mSlideLayout;
    private FragmentViewPager customViewPager;//UltraViewPager  FragmentViewPager
    private ArrayList<BaseFragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "异常项目", "待办工单", "待巡检任务"
    };

    private Banner mBanner;
    private TextView mSearchButton;

    // 获取新闻列表
    private INewsListPersenter listPresenter;
    // 获取新闻详情
    private INewsURLPersenter urlPresenter;

    private boolean isOneLoad = true;

    private int pagePosition;

    public static MaintenanceIndexFragment newInstance() {
        return new MaintenanceIndexFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maintenance_index, container, false);
        mBulletView = view.findViewById(R.id.bulletin_view);
        mMoreMessage = view.findViewById(R.id.main_more_message);
        mSlideLayout = view.findViewById(R.id.slide_tab_layout);
        customViewPager = view.findViewById(R.id.view_pager);

        LinearLayout mTopIcon1 = view.findViewById(R.id.main_top_icon1);
        LinearLayout mTopIcon2 = view.findViewById(R.id.main_top_icon2);
        LinearLayout mTopIcon3 = view.findViewById(R.id.main_top_icon3);
        LinearLayout mTopIcon4 = view.findViewById(R.id.main_top_icon4);

        mMiddleIcon1 = view.findViewById(R.id.middle_icon1);
        mMiddleIcon2 = view.findViewById(R.id.middle_icon2);
        mMiddleIcon3 = view.findViewById(R.id.middle_icon3);
        mMiddleIcon4 = view.findViewById(R.id.middle_icon4);
        mMiddleIconCount1 = view.findViewById(R.id.middle_icon_count1);
        mMiddleIconCount2 = view.findViewById(R.id.middle_icon_count2);
        mMiddleIconCount3 = view.findViewById(R.id.middle_icon_count3);
        mMiddleIconCount4 = view.findViewById(R.id.middle_icon_count4);

        mRefreshLayout = view.findViewById(R.id.maintenance_refreshLayout);
        mRefreshLayout.setRefreshHeader(new SpecialLoadHeader(context));

        mPieChart = view.findViewById(R.id.mPieChart);
        mPieLayout = view.findViewById(R.id.popupWindow_layout);
        mGridView = view.findViewById(R.id.mPieChart_grid_view);

        mBanner = view.findViewById(R.id.company_banner);
        mSearchButton = view.findViewById(R.id.company_search_button);

        View mLine = view.findViewById(R.id.expandable_two_line);
        mLine.setVisibility(View.GONE);

        mTopIcon1.setOnClickListener(this);
        mTopIcon2.setOnClickListener(this);
        mTopIcon3.setOnClickListener(this);
        mTopIcon4.setOnClickListener(this);

        // 获取新闻列表
        listPresenter = CommonPersenterFactory.getNewListPersenter(this);
        addPersenter(listPresenter);
        listPresenter.doNewsList(1);
        // 获取新闻详情
        urlPresenter = CommonPersenterFactory.getNewsURLPersenter(this);
        addPersenter(urlPresenter);
        // 初始化顶部banner和搜索按钮
        initBannerView();
        return view;
    }

    private void initBannerView() {
        List<Integer> images2 = new ArrayList<>();
        images2.add(R.drawable.company_banner1);
        images2.add(R.drawable.company_banner2);
        images2.add(R.drawable.company_banner3);
        //Integer[] images1={R.mipmap.home_icon2,R.mipmap.device_icon2,R.mipmap.application_icon2};
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(images2);
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.Default);
        //设置标题集合（当banner样式有显示title时）
        //homeBanner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
        // 点击顶部搜索框
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CompanySearchForHomeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_top_icon1:
                Intent intent1 = new Intent(context, CompanySearchForHomeActivity.class);
                startActivity(intent1);
                break;
            case R.id.main_top_icon2:
                Intent intent2 = new Intent(getActivity(), MaintenanceCompanyActivity.class);
                MaintenanceCompanyActivity.detailType = 2;
                ProjectNodeBean nodeBean1 = new ProjectNodeBean();
                nodeBean1.setID(null);
                intent2.putExtra("data", nodeBean1);
                startActivity(intent2);
                break;
            case R.id.main_top_icon3:
                initGetProjectData("");
                break;
            case R.id.main_top_icon4:
                Intent intent = new Intent(getActivity(), PLVideoTextureHomeActivity.class);
                startActivity(intent);
                break;
            case R.id.middle_icon1:
                Intent intent6 = new Intent(getActivity(), MaintenanceCompanyActivity.class);
                MaintenanceCompanyActivity.detailType = 2;
                ProjectNodeBean nodeBean2 = new ProjectNodeBean();
                nodeBean2.setID(null);
                intent6.putExtra("data", nodeBean2);
                startActivity(intent6);
                break;
            case R.id.middle_icon2:
                Intent intent3 = new Intent(context, CompanyTwoPageActivity.class);
                intent3.putExtra("TitleName", "待巡检项目");
                intent3.putExtra("position", 1);
                intent3.putExtra("Sum", mMiddleIconCount2.getText().toString());
                startActivity(intent3);
                break;
            case R.id.middle_icon3:
                Intent intent4 = new Intent(context, CompanyTwoPageActivity.class);
                intent4.putExtra("TitleName", "维保项目");
                intent4.putExtra("position", 0);
                intent4.putExtra("Sum", mMiddleIconCount3.getText().toString());
                startActivity(intent4);
                break;
            case R.id.middle_icon4:
                Intent intent5 = new Intent(context, CompanyTwoPageActivity.class);
                intent5.putExtra("TitleName", "近24小时事件项目");
                intent5.putExtra("position", 2);
                intent5.putExtra("Sum", mMiddleIconCount4.getText().toString());
                startActivity(intent5);
                break;

        }
    }

    public void initGetProjectData(String query) {
        if (StringUtils.isEmpty(HelperTool.getToken())) {
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("keyword", query);
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setCancelable(false);
        dialog.setTitle("提示");
        dialog.setMessage("加载中...");
        dialog.show();
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_COMPANY_SEARCH_PROJECT))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        HelperView.Toast(getActivity(), e.getMessage());
                        dialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dialog.dismiss();
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray jsonArray = json.getJSONArray("data");
                            final List<SearchProjectBean> list = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<SearchProjectBean>>() {
                            }.getType());
                            if (list != null && list.size() > 0) {

                                Intent intent3 = new Intent(context, FileShareHomeActivity.class);
                                intent3.putExtra("id", list.get(0).getID());
                                intent3.putExtra("Name", list.get(0).getName());
                                startActivity(intent3);

                            } else {
                                HelperView.Toast(getActivity(), "未查找到项目!");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            HelperView.Toast(getActivity(), e.getMessage());
                        }
                    }
                });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 处理刷新动画 查看更多新闻公告列表
        initNoticeTitle();
        // 处理饼状统计图逻辑
        initGraphData();
        // 处理底部项目 工单 巡检任务列表
        initBottomProjectList();
        // 获取顶部事件统计数量
        getTopEventSum();
    }

    /**
     * 给顶部事件统计数添加渐变动画
     *
     * @param sumCount
     * @param mContentText
     */
    private void initValueAnimator(final int sumCount, final TextView mContentText) {
        if (sumCount == 0) {
            return;
        }
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, sumCount);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                mContentText.setText(String.valueOf(value));
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mMiddleIcon1.setClickable(false);
                mMiddleIcon2.setClickable(false);
                mMiddleIcon3.setClickable(false);
                mMiddleIcon4.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mMiddleIcon1.setOnClickListener(MaintenanceIndexFragment.this);
                mMiddleIcon2.setOnClickListener(MaintenanceIndexFragment.this);
                mMiddleIcon3.setOnClickListener(MaintenanceIndexFragment.this);
                mMiddleIcon4.setOnClickListener(MaintenanceIndexFragment.this);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        valueAnimator.start();
    }

    private void getTopEventSum() {
        RetrofitUtils.getInstance().getCompanyTopEventCount(HelperTool.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultBean<CompanyIndexEventBean, String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableList.add(d);
                    }

                    @Override
                    public void onNext(ResultBean<CompanyIndexEventBean, String> resultBean) {
                        if (resultBean.isSuccess()) {
                            try {
                                initValueAnimator(Integer.parseInt(resultBean.getData().getProjectOnlineSum()), mMiddleIconCount3);
                                initValueAnimator(Integer.parseInt(resultBean.getData().getInspectionTaskSum()), mMiddleIconCount2);
                                initValueAnimator(Integer.parseInt(resultBean.getData().getRecent24HourCount()), mMiddleIconCount4);
                                initValueAnimator(Integer.parseInt(resultBean.getData().getWorkOrderSum()), mMiddleIconCount1);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ToastUtils.showLong("未获取到事件统计数!");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showLong(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mBanner.startAutoPlay();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageUpdate(MessageUpdateHeightBean heightBean) {
        customViewPager.addHeight(heightBean.getId(), heightBean.getHeight());
        if (isOneLoad && heightBean.getId() == 0) {
            isOneLoad = false;
            customViewPager.resetHeight(0);
        }
//        if (pagePosition == heightBean.getId()) {
//            customViewPager.resetHeight(heightBean.getId());
//        }
    }

    private void initBottomProjectList() {
        mFragments.add(CompanyIndexChildrenFragment.newInstance(1));
        mFragments.add(CompanyIndexChildrenFragment.newInstance(2));
        mFragments.add(CompanyIndexChildrenFragment.newInstance(3));

        //defaultUltraViewPager();

        customViewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        customViewPager.setOffscreenPageLimit(1);

        mSlideLayout.setViewPager(customViewPager);
        mSlideLayout.setCurrentTab(pagePosition);
        mSlideLayout.setTextUnselectColor(getResources().getColor(R.color.charge_children_selected));

        customViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                customViewPager.resetHeight(i);
                pagePosition = i;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void initNoticeTitle() {
        mMoreMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewsListActivity.class);
                startActivity(intent);
            }
        });
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000);
                // 处理刷新动画 查看更多新闻公告列表
                initNoticeTitle();
                // 处理饼状统计图逻辑
                initGraphData();
                // 处理底部项目 工单 巡检任务列表
                initBottomProjectList();
                // 获取顶部事件统计数量
                getTopEventSum();
            }
        });
    }

    private void initGraphData() {
        RetrofitUtils.getInstance().getCompanyOrderStateCount(HelperTool.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultBean<List<MainOwnerMarkCountBean>, String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableList.add(d);
                    }

                    @Override
                    public void onNext(ResultBean<List<MainOwnerMarkCountBean>, String> resultBean) {
                        if (resultBean.isSuccess() && resultBean.getData().size() > 0) {
                            List<MainOwnerMarkCountBean> beans = resultBean.getData();
                            Iterator<MainOwnerMarkCountBean> iterator = beans.iterator();
                            int num1 = 0, num2 = 0, num3 = 0;
                            while (iterator.hasNext()) {
                                MainOwnerMarkCountBean bean = iterator.next();
                                if (bean.getWorkOrderState() == 0) {
                                    num1 = bean.getCount();
                                }
                                if (bean.getWorkOrderState() == 70) {
                                    num2 = bean.getCount();
                                }
                                if (bean.getWorkOrderState() == 90) {
                                    num3 = bean.getCount();
                                }
                                if (bean.getWorkOrderState() == 0 || bean.getWorkOrderState() == 70 || bean.getWorkOrderState() == 90) {
                                    iterator.remove();
                                }
                            }
                            num1 = num1 - (num2 + num3);
                            PieChartUtil.initPieChart(context, num1, mGridView, mPieLayout, mPieChart, beans);
                        } else {
                            mPieChart.setNoDataText("饼图暂无数据!");
                            ToastUtils.showLong("未获取到饼图统计数据!");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showLong(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (Disposable d : disposableList) {
            if (d != null && !d.isDisposed()) {
                d.dispose();
            }
        }
    }

    @Override
    public void callBackNewsList(final ResultBean<PagingBean<NewsBean>, String> result) {
        //dialog.dismiss();
        if (result != null && result.getData() != null && result.getData().getRows() != null) {
            mBulletView.setAdapter(new SaleAdapter(getActivity(), result.getData().getRows()));
            mBulletView.setOnBulletinItemClickListener(new BulletinView.OnBulletinItemClickListener() {
                @Override
                public void onBulletinItemClick(int position) {
                    //dialog = showProgress(getActivity(), "加载中,请稍后...", false);
                    urlPresenter.doNewsURL(result.getData().getRows().get(position).getNewsID());
                }
            });
        }
    }

    @Override
    public void callBackNewsURL(ResultBean<String, String> result) {
        //dialog.dismiss();
        if (result.isSuccess()) {
            Intent intent = new Intent(context, NewsInfoActivity.class);
            intent.putExtra("data", result.getExtra());
            startActivity(intent);
        } else {
            HelperView.Toast(getActivity(), result.getMessage());
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        private MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mTitles.length;
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
