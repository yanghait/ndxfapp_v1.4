package com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.OwnerOrderDetailsInputBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.OwnerWorkOrderDetailsBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.maintenance.MaintenanceManageFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.IOwnerOrderDetailsPresenter;

public class OwnerOrderDetailsImpl extends BasePersenter implements IOwnerOrderDetailsPresenter {

    private IOwnerOrderDetailsPresenter.IOwnerOrderDetailsModel model;
    private IOwnerOrderDetailsPresenter.IOwnerOrderDetailsView view;

    public OwnerOrderDetailsImpl(IOwnerOrderDetailsPresenter.IOwnerOrderDetailsView view) {
        this.view = view;
        this.model = MaintenanceManageFactory.getMainOwnerOrderDetails(this);
    }

    @Override
    public void doOwnerOrderDetails(OwnerOrderDetailsInputBean bean) {
        model.requestOwnerOrderDetails(bean);
    }

    @Override
    public void callBackOwnerOrderDetails(ResultBean<OwnerWorkOrderDetailsBean, String> resultBean) {
        if (view != null) {
            view.callBackOwnerOrderDetails(resultBean);
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
