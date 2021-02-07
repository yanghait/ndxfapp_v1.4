package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.charge;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.charge.ChargeChartsIndexEventData;
import com.ynzhxf.nd.xyfirecontrolapp.bean.charge.ChargeChartsListBean;
import com.ynzhxf.nd.xyfirecontrolapp.network.RetrofitUtils;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * author hbzhou
 * date 2019/10/28 15:31
 * 新版主管部门统计页
 */
public class ChargeChartsFragment extends BaseFragment {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_charge_charts, container, false);
        recyclerView = view.findViewById(R.id.charts_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    public static ChargeChartsFragment newInstance() {
        return new ChargeChartsFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initIndexCharts();
    }

    private void initData(ChargeChartsIndexEventData eventData) {

        List<ChargeChartsListBean> listBeans = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ChargeChartsListBean bean = new ChargeChartsListBean();
            bean.setModeltype(i + 1);
            bean.setBeanModel(eventData);
            listBeans.add(bean);
        }
        recyclerView.setAdapter(new MultipleItemQuickAdapter(listBeans));
    }

    private class MultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<ChargeChartsListBean, BaseViewHolder> {

        /**
         * Same as QuickAdapter#QuickAdapter(Context,int) but with
         * some initialization data.
         *
         * @param data A new list is created out of this one to avoid mutable list
         */
        private MultipleItemQuickAdapter(List<ChargeChartsListBean> data) {
            super(data);
            addItemType(1, R.layout.item_charge_charts_one);
            addItemType(2, R.layout.item_charge_charts_two);
            addItemType(3, R.layout.item_charge_charts_three);
            addItemType(4, R.layout.item_charge_charts_four);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, ChargeChartsListBean item) {
            switch (item.getItemType()) {
                case 1:

                    TextView networkProject = helper.getView(R.id.charts_title1);
                    TextView networkError = helper.getView(R.id.charts_count2);
                    TextView networkSuccess = helper.getView(R.id.charts_success);

                    networkProject.setText(String.valueOf(item.getBeanModel().getProjectOnlineSum()));
                    networkError.setText(String.valueOf(item.getBeanModel().getProjectConnectAbormalCount()));
                    networkSuccess.setText(String.valueOf(item.getBeanModel().getProjectConnectNormalCount()));
                    break;
                case 2:
                    final TextView alarmCount = helper.getView(R.id.charts_two_alarm_count);
                    final TextView alarmText = helper.getView(R.id.charts_two_alarm_text);
                    final ImageView alarmImage = helper.getView(R.id.charts_two_image);

                    final TextView alarmCount1 = helper.getView(R.id.charts_two_history_count);
                    final TextView alarmText1 = helper.getView(R.id.charts_two_history_title);
                    final ImageView alarmImage1 = helper.getView(R.id.charts_two_image2);

                    alarmCount.setText(String.valueOf(item.getBeanModel().getRealAlarmSum()));
                    alarmCount1.setText(String.valueOf(item.getBeanModel().getRecent24hEventCount()));

                    alarmCount.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getActivity(), ChargeCurrentAlarmActivity.class));
                        }
                    });
                    alarmText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getActivity(), ChargeCurrentAlarmActivity.class));
                        }
                    });
                    alarmImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getActivity(), ChargeCurrentAlarmActivity.class));
                        }
                    });


                    alarmCount1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getActivity(), ChargeChartsHistoryActivity.class));
                        }
                    });
                    alarmText1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getActivity(), ChargeChartsHistoryActivity.class));
                        }
                    });
                    alarmImage1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getActivity(), ChargeChartsHistoryActivity.class));
                        }
                    });
                    break;
                case 3:

                    TextView inspectTaskNotDone = helper.getView(R.id.inspect_work_count1);
                    TextView inspectTaskNormal = helper.getView(R.id.inspect_work_count2);
                    TextView inspectTaskAbnormal = helper.getView(R.id.inspect_work_count3);

                    final CircleProgressBar mCustomProgressBar1 = helper.getView(R.id.custom_progress1);
                    final CircleProgressBar mCustomProgressBar2 = helper.getView(R.id.custom_progress2);
                    final CircleProgressBar mCustomProgressBar3 = helper.getView(R.id.custom_progress3);

                    inspectTaskNotDone.setText(String.valueOf(item.getBeanModel().getInspectTaskNotDone()));
                    inspectTaskNormal.setText(String.valueOf(item.getBeanModel().getInspectTaskNormal()));
                    inspectTaskAbnormal.setText(String.valueOf(item.getBeanModel().getInspectTaskAbnormal()));

                    int sum = item.getBeanModel().getInspectTaskSum();
                    int taskNotDone = Math.round(item.getBeanModel().getInspectTaskNotDone() * 1.0f / sum * 100);
                    int taskNormal = Math.round(item.getBeanModel().getInspectTaskNormal() * 1.0f / sum * 100);
                    int taskAbnormal = Math.round(item.getBeanModel().getInspectTaskAbnormal() * 1.0f / sum * 100);

                    simulateProgress(mCustomProgressBar1, taskNotDone);
                    simulateProgress(mCustomProgressBar2, taskNormal);
                    simulateProgress(mCustomProgressBar3, taskAbnormal);
                    break;
                case 4:
                    RecyclerView itemRecyclerView = helper.getView(R.id.charts_four_recycler);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    itemRecyclerView.setLayoutManager(layoutManager);

                    BaseQuickAdapter quickAdapter = new BaseQuickAdapter<ChargeChartsIndexEventData.IndexWorkOrderBean, BaseViewHolder>(R.layout.item_charge_charts_item_four,item.getBeanModel().getLsAreaWorkOrderCount() ) {
                        @Override
                        protected void convert(@NonNull BaseViewHolder helper, ChargeChartsIndexEventData.IndexWorkOrderBean item) {
                            TextView countryName = helper.getView(R.id.country_name);
                            TextView leftText = helper.getView(R.id.rate_text1);
                            TextView rightText = helper.getView(R.id.rate_text2);
                            TextView rateGreen = helper.getView(R.id.rate_green);
                            TextView rateYellow = helper.getView(R.id.rate_yellow);

                            ConstraintLayout constraintLayout = helper.getView(R.id.constrain_root);
                            ConstraintSet set = new ConstraintSet();
                            set.clone(constraintLayout);
                            if (item.getWorkOrderNotDone() == 0) {
                                set.setHorizontalWeight(R.id.rate_green, 100);
                                set.setHorizontalWeight(R.id.rate_yellow, 0);
                                rateGreen.setBackground(getResources().getDrawable(R.drawable.item_rate_shape_four_green_100));
                            } else if (item.getWorkOrderDone() == 0 && item.getWorkOrderNotDone() > 0) {
                                set.setHorizontalWeight(R.id.rate_green, 100);
                                set.setHorizontalWeight(R.id.rate_yellow, 0);
                                rateGreen.setBackground(getResources().getDrawable(R.drawable.item_rate_shape_four2_yellow_100));
                            } else {

                                rateGreen.setBackground(getResources().getDrawable(R.drawable.item_rate_shape_four));
                                rateYellow.setBackground(getResources().getDrawable(R.drawable.item_rate_shape_four2));
                                set.setHorizontalWeight(R.id.rate_green, item.getWorkOrderDone());
                                set.setHorizontalWeight(R.id.rate_yellow, item.getWorkOrderNotDone());
                            }
                            set.applyTo(constraintLayout);

                            countryName.setText(item.getAreaName());
                            leftText.setText(String.valueOf(item.getWorkOrderDone()));
                            rightText.setText(String.valueOf(item.getWorkOrderNotDone()));
                        }
                    };

                    itemRecyclerView.setAdapter(quickAdapter);
                    break;
            }
        }
    }

    private void initIndexCharts() {
        RetrofitUtils.getInstance().getChargeIndexEventData(HelperTool.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultBean<ChargeChartsIndexEventData, String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResultBean<ChargeChartsIndexEventData, String> resultBean) {
                        if (resultBean.isSuccess() && resultBean.getData() != null) {
                            initData(resultBean.getData());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //LogUtils.showLoge("11--11-", e.getMessage() + "~~~~");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void simulateProgress(final CircleProgressBar mCustomProgressBar, final int value) {
        ValueAnimator animator = ValueAnimator.ofInt(0, value);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();
                mCustomProgressBar.setProgress(progress);
            }
        });
        animator.setDuration(4000);
        animator.start();
    }
}
