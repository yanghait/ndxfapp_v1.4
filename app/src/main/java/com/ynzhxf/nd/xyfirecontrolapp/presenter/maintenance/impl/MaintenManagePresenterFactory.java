package com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.impl;

import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.ICompanyBackFillParamsPresenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.IMainCompanyOrderListPresenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.IMainOwnerImpowerListPresenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.IMaintenanceListPresenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.IOwnerOrderDetailsPresenter;

public class MaintenManagePresenterFactory {
    /**
     * 获取业主维保工单列表
     *
     * @param view
     * @return
     */
    public static IMaintenanceListPresenter getOwnerListPersenterImpl(IMaintenanceListPresenter.IMaintenanceListView view) {
        return new MaintenanceOwnerListImpl(view);
    }

    /**
     * 获取业主维保授权系统列表
     *
     * @param view
     * @return
     */
    public static IMainOwnerImpowerListPresenter getOwnerImpowerListPersenterImpl(IMainOwnerImpowerListPresenter.IMainOwnerImpowerListView view) {
        return new MainOwnerImpowerListImpl(view);
    }

    /**
     * 获取业主维保工单详情
     *
     * @param view
     * @return
     */
    public static IOwnerOrderDetailsPresenter getOwnerOrderDetailsImpl(IOwnerOrderDetailsPresenter.IOwnerOrderDetailsView view) {
        return new OwnerOrderDetailsImpl(view);
    }

    /**
     * 获取维保公司工单列表
     *
     * @param view
     * @return
     */
    public static IMainCompanyOrderListPresenter getCompanyOrderListImpl(IMainCompanyOrderListPresenter.ICompanyOrderListView view) {
        return new CompanyOrderListImpl(view);
    }


    /**
     * 获取维保公司回填系统参数
     *
     * @param view
     * @return
     */
    public static ICompanyBackFillParamsPresenter getCompanyBackFillParamsImpl(ICompanyBackFillParamsPresenter.ICompanyBackFillParamsView view) {
        return new CompanyBackFillParamsImpl(view);
    }

}
