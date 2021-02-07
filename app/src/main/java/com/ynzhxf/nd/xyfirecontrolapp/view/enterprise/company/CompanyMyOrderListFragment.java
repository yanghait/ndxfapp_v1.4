package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.company;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.SpecialCommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.CompanyOrderListInputBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.MaintenListAllBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.MaintenListBackBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.IMainCompanyOrderListPresenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.impl.MaintenManagePresenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.MaintenanceCompanyActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.fragment.OwnerMyOrderListFragment;

import java.util.ArrayList;
import java.util.List;

//维保公司fragment
public class CompanyMyOrderListFragment extends OwnerMyOrderListFragment implements IMainCompanyOrderListPresenter.ICompanyOrderListView {

    public CompanyOrderListInputBean inputBean;
    private IMainCompanyOrderListPresenter companyPresenter;

    private int companyIndex = 1;

    final List<MaintenListBackBean> companyBeanList = new ArrayList<>();

    private Context mContext;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        String start_Time = SPUtils.getInstance().getString("start_Time");
        String end_Time = SPUtils.getInstance().getString("end_Time");
        inputBean = new CompanyOrderListInputBean();

        if (!StringUtils.isEmpty(start_Time) && !StringUtils.isEmpty(end_Time)) {
            inputBean.setStartTime(start_Time);
            inputBean.setEndTime(end_Time);
        }
        initInputBeanType();
        companyPresenter = MaintenManagePresenterFactory.getCompanyOrderListImpl(this);

        inputBean.setToken(HelperTool.getToken());
        inputBean.setState(state);
        inputBean.setPageIndex("1");
        inputBean.setPageSize("10");
        inputBean.setProjectId(SPUtils.getInstance().getString("projectNodeId"));
        companyPresenter.doCompanyOrderList(inputBean);

        LogUtils.showLoge("输出bean2222---" + state, inputBean.toString());

        isCompany = true;

        super.onActivityCreated(savedInstanceState);

        companyIndex = 1;

        companyBeanList.clear();

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                inputBean.setPageIndex("1");
                companyIndex = 1;
                if (companyBeanList.size() > 0) {
                    companyBeanList.clear();
                }
                companyPresenter.doCompanyOrderList(inputBean);
                refreshLayout.finishRefresh(2000);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                companyIndex++;
                inputBean.setPageIndex(String.valueOf(companyIndex));
                companyPresenter.doCompanyOrderList(inputBean);
                refreshLayout.finishLoadMore();
            }
        });

    }

    public static CompanyMyOrderListFragment newInstance(String state) {
        Bundle args = new Bundle();
        args.putString("state", state);
        CompanyMyOrderListFragment fragment = new CompanyMyOrderListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void callBackCompanyOrderList(ResultBean<MaintenListAllBean, String> resultBean) {
        if (resultBean == null || resultBean.getData() == null || resultBean.getData().getRows() == null || resultBean.getData().getRows().size() == 0) {
            if (companyBeanList.size() == 0) {
                mNoDataView.setVisibility(View.VISIBLE);
            }
            return;
        } else {
            mNoDataView.setVisibility(View.GONE);
        }
        MaintenanceCompanyActivity.flag = true;

        initFlagCharge();

//        if (MaintenanceCompanyActivity.ownerActivity == null) {
//            MaintenanceCompanyActivity.setActivity((OwnerMyWorkOrderActivity) mContext);
//        }

        companyBeanList.addAll(resultBean.getData().getRows());

       // LogUtils.showLoge("输出获取到的工单数量7878---",resultBean.getData().getRows().size()+"~~~~~");

        SpecialCommonAdapter adapter = MaintenanceCompanyActivity.initWorkOrderList(recyclerView, (Activity) mContext, companyBeanList);

        adapter.notifyDataSetChanged();

        //LogUtils.showLoge("数据返回123---", String.valueOf(state) + "~~~" + resultBean.getData().getRows());
    }


    public void initInputBeanType() {
    }

    public void initFlagCharge() {
    }

    @Override
    public void callBackMaintenanceList(ResultBean<MaintenListAllBean, String> resultBean) {
        //
    }
}
