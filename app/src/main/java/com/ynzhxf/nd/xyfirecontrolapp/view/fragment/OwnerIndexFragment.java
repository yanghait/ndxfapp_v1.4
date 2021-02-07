package com.ynzhxf.nd.xyfirecontrolapp.view.fragment;


import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.PagingBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.NewsBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.OwnerIndexSixDataBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.message.MessageUpdateHeightBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.network.RetrofitUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.common.INewsListPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.common.INewsURLPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.common.impl.CommonPersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IUserHasAuthoryProjectPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl.NodeBasePersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.ui.SpecialLoadHeader;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.NewsInfoActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.NewsListActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.MaintenanceManageActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.ProjectHistoryAlarmActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.ProjectInfoActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.ProjectInfoMessageDataActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.ProjectRealAlarmActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.assessment.RiskAssessmentHomeActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.InspectionTaskHomeActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.adapter.SaleAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.widget.FragmentViewPager;
import com.ynzhxf.nd.xyfirecontrolapp.widget.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.bakumon.library.view.BulletinView;

/**
 * author hbzhou
 * date 2019/6/13 09:33
 * 业主新版首页fragment
 */
public class OwnerIndexFragment extends BaseFragment implements IUserHasAuthoryProjectPersenter.IUserHasAuthoryProjectView, INewsListPersenter.INewsListView, INewsURLPersenter.INewsURLView {
    private BulletinView mBulletView;
    private TextView mMoreMessage;

    private RefreshLayout mRefreshLayout;
    private Context context;

    //用户有权限的项目列表请求
    private IUserHasAuthoryProjectPersenter userHasAuthoryProjectPersenter;
    // 获取新闻列表
    private INewsListPersenter listPresenter;
    // 获取新闻详情
    private INewsURLPersenter urlPresenter;

    private ProgressDialog dialog;
    private RecyclerView mRecyclerView;

    private SlidingTabLayout mSlideLayout;
    private FragmentViewPager customViewPager;

    private Banner mBanner;
    private ArrayList<BaseFragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "最多故障点", "待巡检任务", "视频会议"
    };

    private List<OwnerIndexSixDataBean> sixDataBeans = new ArrayList<>();

    private boolean isOneLoad = true;
    private int pagePosition;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner_index, container, false);
        mBulletView = view.findViewById(R.id.bulletin_view);
        mMoreMessage = view.findViewById(R.id.owner_more_message);
//        mProjectName = view.findViewById(R.id.owner_project_name);
//        mProjectDetails = view.findViewById(R.id.owner_project_details);
        mRecyclerView = view.findViewById(R.id.recyclerView1);
        mRefreshLayout = view.findViewById(R.id.owner_refreshLayout);
        mRefreshLayout.setRefreshHeader(new SpecialLoadHeader(context));
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mSlideLayout = view.findViewById(R.id.slide_tab_layout);
        customViewPager = view.findViewById(R.id.view_pager);
        mBanner = view.findViewById(R.id.owner_banner);

        // 请求获取业主项目列表
        userHasAuthoryProjectPersenter = NodeBasePersenterFactory.getUserHasAuthoryProjectPersenterImpl(this);
        addPersenter(userHasAuthoryProjectPersenter);
        initProjectList();
        //userHasAuthoryProjectPersenter.doUserHasAuthoryProject();
        // 获取新闻列表
        listPresenter = CommonPersenterFactory.getNewListPersenter(this);
        addPersenter(listPresenter);
        listPresenter.doNewsList(1);
        // 获取新闻详情
        urlPresenter = CommonPersenterFactory.getNewsURLPersenter(this);
        addPersenter(urlPresenter);
        // 初始化轮播图
        initTopBanner();
        return view;
    }

    private void initTopBanner() {

        List<Integer> images2 = new ArrayList<>();
        images2.add(R.drawable.owner_banner1);
        images2.add(R.drawable.owner_banner2);
        images2.add(R.drawable.owner_banner3);
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
    }

    private void initProjectList() {
        RetrofitUtils.getInstance().getOwnerIndexSixData(HelperTool.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultBean<List<OwnerIndexSixDataBean>, String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResultBean<List<OwnerIndexSixDataBean>, String> resultBean) {
                        if (resultBean.isSuccess()) {
                            sixDataBeans.clear();
                            sixDataBeans.addAll(resultBean.getData());
                        }
                        userHasAuthoryProjectPersenter.doUserHasAuthoryProject();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static OwnerIndexFragment newInstance() {
        return new OwnerIndexFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dialog = showProgress(context, "加载中...", false);
        initNoticeTitle();

        initBottomProjectList();
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
    }

    private void initBottomProjectList() {
        mFragments.add(OwnerIndexChildrenFragment.newInstance(1));
        mFragments.add(OwnerIndexChildrenFragment.newInstance(2));
        mFragments.add(OwnerIndexChildrenFragment.newInstance(3));

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
                userHasAuthoryProjectPersenter.doUserHasAuthoryProject();
                listPresenter.doNewsList(1);

                // 刷新下面选项卡列表
                mFragments.clear();
                initProjectList();
                initBottomProjectList();
            }
        });
    }

    @Override
    public void callBackNewsList(final ResultBean<PagingBean<NewsBean>, String> result) {
        dialog.dismiss();
        if (result != null && result.getData() != null && result.getData().getRows() != null) {
            mBulletView.setAdapter(new SaleAdapter(getActivity(), result.getData().getRows()));
            mBulletView.setOnBulletinItemClickListener(new BulletinView.OnBulletinItemClickListener() {
                @Override
                public void onBulletinItemClick(int position) {
                    dialog = showProgress(getActivity(), "加载中,请稍后...", false);
                    urlPresenter.doNewsURL(result.getData().getRows().get(position).getNewsID());
                }
            });
        }
    }

    @Override
    public void callBackNewsURL(ResultBean<String, String> result) {
        dialog.dismiss();
        if (result.isSuccess()) {
            Intent intent = new Intent(context, NewsInfoActivity.class);
            intent.putExtra("data", result.getExtra());
            startActivity(intent);
        } else {
            HelperView.Toast(getActivity(), result.getMessage());
        }
    }

    private void initValueAnimator(final int sumCount, final TextView mContentText) {
        if (sumCount == 0) {
            return;
        }
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, sumCount);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                mContentText.setText(String.valueOf(value));
            }
        });
        valueAnimator.start();
    }

    @Override
    public void callBackUserHasAuthoryProject(ResultBean<List<ProjectNodeBean>, String> resultBean) {
        dialog.dismiss();
        if (resultBean.isSuccess()) {
            if (resultBean.getData().size() > 0) {
                ProjectNodeBean bean = resultBean.getData().get(0);
                SPUtils.getInstance().put("OwnerProjectId", bean.getID());
                SPUtils.getInstance().put("OwnerProjectName", bean.getName());
                SPUtils.getInstance().put("ProjectNum", resultBean.getData().size());
            }


            CommonAdapter adapter = new CommonAdapter<ProjectNodeBean>(context, R.layout.fragment_owner_index_extra, resultBean.getData()) {
                @Override
                protected void convert(ViewHolder holder, final ProjectNodeBean projectNodeBean, int position) {
                    TextView mProjectName = holder.getView(R.id.project_name);
                    TextView mProjectDetils = holder.getView(R.id.project_details);
                    mProjectName.setText(projectNodeBean.getName());

                    RelativeLayout mLayout1 = holder.getView(R.id.click_layout1);
                    TextView mEventCount1 = holder.getView(R.id.charge_event_one);
                    RelativeLayout mLayout2 = holder.getView(R.id.click_layout2);
                    TextView mEventCount2 = holder.getView(R.id.charge_event_two);
                    RelativeLayout mLayout3 = holder.getView(R.id.click_layout3);
                    TextView mEventCount3 = holder.getView(R.id.charge_event_three);

                    RelativeLayout mLayout4 = holder.getView(R.id.click_layout4);
                    TextView mEventCount4 = holder.getView(R.id.charge_event_four);
                    RelativeLayout mLayout5 = holder.getView(R.id.click_layout5);
                    TextView mEventCount5 = holder.getView(R.id.charge_event_five);
                    RelativeLayout mLayout6 = holder.getView(R.id.click_layout6);
                    TextView mEventCount6 = holder.getView(R.id.charge_event_six);

                    if (sixDataBeans.size() - 1 >= position) {
                        OwnerIndexSixDataBean bean = sixDataBeans.get(position);

                        try {
                            initValueAnimator(Integer.parseInt(bean.getWorkOrderSum()), mEventCount1);
                            initValueAnimator(Integer.parseInt(bean.getInspectionTaskSum()), mEventCount2);

                            initValueAnimator(Integer.parseInt(bean.getRealAlarmCount()), mEventCount4);
                            initValueAnimator(Integer.parseInt(bean.getRecent24HourCount()), mEventCount5);
                            initValueAnimator(Integer.parseInt(bean.getProjectRepairTimes()), mEventCount6);
                        } catch (NumberFormatException e) {
                            ToastUtils.showLong("获取事件统计数出错!");
                            e.printStackTrace();
                        }

//                        mEventCount1.setText(bean.getWorkOrderSum());
//                        mEventCount2.setText(bean.getInspectionTaskSum());
//
//                        mEventCount4.setText(bean.getRealAlarmCount());
//                        mEventCount5.setText(bean.getRecent24HourCount());
//                        mEventCount6.setText(bean.getProjectRepairTimes());

                        try {
                            double score = Double.parseDouble(bean.getFireRiskLevel());
                            if (score <= 1.0) {
                                mEventCount3.setText("安全");
                                //mProjectAssessmentText.setTextColor(getResources().getColor(R.color.risk_assessment_state_color_green));
                            } else if (score > 1.0 && score <= 1.6) {
                                mEventCount3.setText("轻度");
                                //mProjectAssessmentText.setTextColor(getResources().getColor(R.color.risk_assessment_state_color_yellow));
                            } else if (score > 1.6 && score <= 2.7) {
                                mEventCount3.setText("中度");
                                //mProjectAssessmentText.setTextColor(getResources().getColor(R.color.risk_assessment_state_color_orange));
                            } else if (score > 2.7 && score <= 4.5) {
                                mEventCount3.setText("高度");
                                //mProjectAssessmentText.setTextColor(getResources().getColor(R.color.risk_assessment_state_color_red1));
                            } else if (score > 4.5) {
                                mEventCount3.setText("严重");
                                //mProjectAssessmentText.setTextColor(getResources().getColor(R.color.risk_assessment_state_color_red2));
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                    //处理项目详情点击
                    mProjectDetils.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, ProjectInfoActivity.class);
                            intent.putExtra("projectData", projectNodeBean);
                            startActivity(intent);
                        }
                    });

                    //处理六个数据统计模块点击事件
                    mLayout1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //ToastUtils.showLong("点击了区域1");
                            Intent intent = new Intent(context, MaintenanceManageActivity.class);
                            intent.putExtra("data", projectNodeBean);
                            startActivity(intent);
                        }
                    });
                    mLayout2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, InspectionTaskHomeActivity.class);
                            intent.putExtra("projectId", projectNodeBean.getID());
                            intent.putExtra("isShow", projectNodeBean.getFireInspectUserType());
                            startActivity(intent);
                        }
                    });
                    mLayout3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, RiskAssessmentHomeActivity.class);
                            intent.putExtra("projectId", projectNodeBean.getID());
                            intent.putExtra("name", projectNodeBean.getName());
                            startActivity(intent);
                        }
                    });
                    mLayout4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, ProjectRealAlarmActivity.class);
                            intent.putExtra("data", projectNodeBean);
                            startActivity(intent);
                        }
                    });
                    mLayout5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, ProjectHistoryAlarmActivity.class);
                            intent.putExtra("data", projectNodeBean);
                            intent.putExtra("OneDay", true);
                            startActivity(intent);
                        }
                    });
                    mLayout6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, ProjectInfoMessageDataActivity.class);
                            intent.putExtra("ID", projectNodeBean.getID());
                            intent.putExtra("Name", projectNodeBean.getName());
                            intent.putExtra("labelType", 2);
                            startActivity(intent);
                        }
                    });
                }
            };
            mRecyclerView.setAdapter(adapter);
        } else {
            HelperView.Toast(context, "项目列表：" + resultBean.getMessage());
        }
    }

    @Override
    public void callBackError(ResultBean<String, String> resultBean, String code) {
        super.callBackError(resultBean, code);
        dialog.dismiss();
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
