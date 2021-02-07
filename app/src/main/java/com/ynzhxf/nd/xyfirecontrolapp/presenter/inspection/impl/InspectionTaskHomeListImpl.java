package com.ynzhxf.nd.xyfirecontrolapp.presenter.inspection.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionTaskHomeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionTaskHomeInputBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.inspection.InspectionTaskFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.inspection.ITaskInspectionHomePresenter;

import java.util.List;

public class InspectionTaskHomeListImpl extends BasePersenter implements ITaskInspectionHomePresenter {

    private ITaskInspectionHomePresenter.ITaskInspectionHomeListModel model;
    private ITaskInspectionHomePresenter.ITaskInspectionHomeListView view;

    public InspectionTaskHomeListImpl(ITaskInspectionHomePresenter.ITaskInspectionHomeListView view) {
        this.view = view;
        model = InspectionTaskFactory.getTaskInspectionHome(this);
    }

    @Override
    public void doTaskInspectionHomeList(InspectionTaskHomeInputBean bean) {
        model.requestTaskInspectionHomeList(bean);
    }

    @Override
    public void callBackTaskInspectionHomeList(ResultBean<List<InspectionTaskHomeBean>, String> resultBean) {
        if (view != null) {
            view.callBackTaskInspectionHomeList(resultBean);
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
