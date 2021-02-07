package com.ynzhxf.nd.xyfirecontrolapp.model.inspection;

import com.ynzhxf.nd.xyfirecontrolapp.presenter.inspection.ITaskInspectionHomePresenter;

public class InspectionTaskFactory {
    // 巡检任务列表
    public static ITaskInspectionHomePresenter.ITaskInspectionHomeListModel getTaskInspectionHome(ITaskInspectionHomePresenter presenter) {
        return new InspectionTaskHomeModel(presenter);
    }
}
