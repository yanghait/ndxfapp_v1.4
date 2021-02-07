package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.charge;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.charge.ChargeChartsDetailsBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.charge.ChargeChartsDetailsListModelBean;
import com.ynzhxf.nd.xyfirecontrolapp.network.RetrofitUtils;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * author hbzhou
 * date 2019/11/4 11:01
 * 主管部门总览模块项目项目详情
 */
public class ChargeChartsProjectDetailsActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_charge_charts_project_details);
        super.onCreate(savedInstanceState);
        setBarTitle("项目详情");
        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        dialog = showProgress(this,"加载中...",false);
        initData(getIntent().getStringExtra("ID"));
    }

    /**
     * 获取主管部门总览模块项目项目详情
     * @param ID
     */
    private void initData(String ID) {
        // 无数据时显示此view
        final List<ChargeChartsDetailsListModelBean> detailsBeans = new ArrayList<>();
        final MultipleItemAdapter itemAdapter = new MultipleItemAdapter(detailsBeans);
        View view = LayoutInflater.from(this).inflate(R.layout.all_no_data, null);
        view.setVisibility(View.VISIBLE);
        itemAdapter.setEmptyView(view);

        RetrofitUtils.getInstance().getChargeChartsProjectDetailsData(HelperTool.getToken(), ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultBean<ChargeChartsDetailsBean, String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResultBean<ChargeChartsDetailsBean, String> resultBean) {
                        dialog.dismiss();
                        if (resultBean.isSuccess() && resultBean.getData() != null) {
                            for (int i = 0; i < resultBean.getData().getLsAlarmLog().size() + 1; i++) {
                                ChargeChartsDetailsListModelBean modelBean = new ChargeChartsDetailsListModelBean();
                                if (i == 0) {
                                    modelBean.setBeanType(1);
                                    modelBean.setCompetName(resultBean.getData().getCompetName());
                                    modelBean.setCompetPhone(resultBean.getData().getCompetPhone());
                                    modelBean.setConnectState(resultBean.getData().getConnectState());
                                    modelBean.setEnterpriseConnectName(resultBean.getData().getEnterpriseConnectName());
                                    modelBean.setEnterprisePhone(resultBean.getData().getEnterprisePhone());
                                    modelBean.setHas3DInformation(resultBean.getData().isHas3DInformation());
                                    modelBean.setHasBeenControl(resultBean.getData().getHasBeenControl());

                                    modelBean.setIsMaintenance(resultBean.getData().isMaintenance());
                                    modelBean.setMaintenance(resultBean.getData().isMaintenance());

                                    modelBean.setMaintenanceConnectName(resultBean.getData().getMaintenanceConnectName());
                                    modelBean.setProjectName(resultBean.getData().getProjectName());
                                    modelBean.setProjectAddress(resultBean.getData().getProjectAddress());
                                    modelBean.setMaintenanceName(resultBean.getData().getMaintenanceName());
                                    modelBean.setMaintenancePhone(resultBean.getData().getMaintenancePhone());
                                } else {
                                    modelBean.setBeanType(2);
                                    modelBean.setLsAlarmLog(resultBean.getData().getLsAlarmLog().get(i - 1));
                                }
                                // 构造list实体model bean
                                detailsBeans.add(modelBean);
                            }
                            itemAdapter.notifyDataSetChanged();
                        }
                        recyclerView.setAdapter(itemAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        dialog.dismiss();
                    }
                });


    }

    private class MultipleItemAdapter extends BaseMultiItemQuickAdapter<ChargeChartsDetailsListModelBean, BaseViewHolder> {

        /**
         * Same as QuickAdapter#QuickAdapter(Context,int) but with
         * some initialization data.
         *
         * @param data A new list is created out of this one to avoid mutable list
         */
        private MultipleItemAdapter(List<ChargeChartsDetailsListModelBean> data) {
            super(data);
            addItemType(1, R.layout.item_charge_charts_details);
            addItemType(2, R.layout.item_charge_charts_details_two);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder helper, ChargeChartsDetailsListModelBean item) {
            if (item.getBeanType() == 1) {

                TextView projectName = helper.getView(R.id.item_details_title);
                TextView projectAddress = helper.getView(R.id.item_details_location);
                TextView has3DInformation = helper.getView(R.id.icon1_text);
                TextView hasBeenControl = helper.getView(R.id.icon2_text);
                TextView connectState = helper.getView(R.id.icon3_text);
                TextView competName = helper.getView(R.id.icon4_text);
                TextView competPhone = helper.getView(R.id.text_011);
                TextView enterpriseConnectName = helper.getView(R.id.icon5_text);
                TextView enterprisePhone = helper.getView(R.id.text_012);
                TextView isMaintenance = helper.getView(R.id.icon6_text);
                TextView maintenanceName = helper.getView(R.id.text_013);
                TextView maintenanceConnectName = helper.getView(R.id.icon8_text);
                TextView maintenancePhone = helper.getView(R.id.text_014);

                projectName.setText(item.getProjectName());
                projectAddress.setText(item.getProjectAddress());
                if (item.isHas3DInformation()) {
                    has3DInformation.setText("有");
                } else {
                    has3DInformation.setText("无");
                }
                if (item.getHasBeenControl() == 1) {
                    hasBeenControl.setText("已接管");
                } else {
                    hasBeenControl.setText("未接管");
                }
                if (item.getConnectState() == 0) {
                    connectState.setText("正常");
                } else if (item.getConnectState() == 1) {
                    connectState.setText("异常");
                } else {
                    connectState.setText("离线");
                }

                competName.setText(item.getCompetName());
                competPhone.setText(item.getCompetPhone());
                enterpriseConnectName.setText(item.getEnterpriseConnectName());
                enterprisePhone.setText(item.getEnterprisePhone());
                if (item.isMaintenance()) {
                    isMaintenance.setText("维保中");
                } else {
                    isMaintenance.setText("未维保");
                }

                maintenanceName.setText(item.getMaintenanceName());
                maintenanceConnectName.setText(item.getMaintenanceConnectName());
                maintenancePhone.setText(item.getMaintenancePhone());
            } else if (item.getBeanType() == 2) {

                TextView AreaName = helper.getView(R.id.title);
                TextView AlarmValue = helper.getView(R.id.text_content1);
                TextView EventTypeName = helper.getView(R.id.text_content2);
                TextView SubconditionName = helper.getView(R.id.text_content3);
                TextView EventTimeStr = helper.getView(R.id.text_content4);

                AreaName.setText(item.getLsAlarmLog().getAreaName());
                AlarmValue.setText(item.getLsAlarmLog().getAlarmValue());
                EventTimeStr.setText(item.getLsAlarmLog().getEventTimeStr());
                EventTypeName.setText(item.getLsAlarmLog().getEventTypeName());
                SubconditionName.setText(item.getLsAlarmLog().getSubconditionName());
            }
        }
    }
}
