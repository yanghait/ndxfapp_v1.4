package com.ynzhxf.nd.xyfirecontrolapp.presenter.inspection.impl;

import com.ynzhxf.nd.xyfirecontrolapp.presenter.inspection.ITaskInspectionHomePresenter;

public class InspectionTaskPresenterFactory {

    /**
     * 巡检任务列表
     *
     * @param view
     * @return
     */
    public static ITaskInspectionHomePresenter getTaskInspectionHomePresenterImpl(ITaskInspectionHomePresenter.ITaskInspectionHomeListView view) {
        return new InspectionTaskHomeListImpl(view);
    }
}
