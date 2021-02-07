package com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.CompanyOrderListInputBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.MaintenListAllBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.maintenance.MaintenanceManageFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.IMainCompanyOrderListPresenter;

public class CompanyOrderListImpl extends BasePersenter implements IMainCompanyOrderListPresenter {

    private ICompanyOrderListModel model;
    private ICompanyOrderListView view;

    public CompanyOrderListImpl(IMainCompanyOrderListPresenter.ICompanyOrderListView view) {
        this.view = view;
        this.model = MaintenanceManageFactory.getMainCompanyOrderList(this);
    }

    @Override
    public void doCompanyOrderList(CompanyOrderListInputBean bean) {
        model.requestCompanyOrderList(bean);
    }

    @Override
    public void callBackCompanyOrderList(ResultBean<MaintenListAllBean, String> resultBean) {
        if (view != null) {
            view.callBackCompanyOrderList(resultBean);
        }
    }

    @Override
    public void callBackError(ResultBean<String, String> result, String action) {
        if (view != null) {
            view.callBackError(result, action);
        }
    }

    @Override
    public void detachView() {
        view = null;
    }
}
