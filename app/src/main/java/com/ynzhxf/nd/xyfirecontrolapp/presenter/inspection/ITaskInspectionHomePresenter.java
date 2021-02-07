package com.ynzhxf.nd.xyfirecontrolapp.presenter.inspection;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionTaskHomeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionTaskHomeInputBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

import java.util.List;

public interface ITaskInspectionHomePresenter extends IBasePersenter {

    String TAG = "ITaskInspectionHomePresenter";

    interface ITaskInspectionHomeListView extends IBaseView {
        /**
         * 巡检任务列表回调
         *
         * @param
         */
        void callBackTaskInspectionHomeList(ResultBean<List<InspectionTaskHomeBean>, String> resultBean);

    }

    interface ITaskInspectionHomeListModel {
        /**
         * 巡检任务列表
         *
         * @param bean
         */
        void requestTaskInspectionHomeList(InspectionTaskHomeInputBean bean);
    }

    /**
     * 巡检任务列表
     *
     * @param bean
     */
    void doTaskInspectionHomeList(InspectionTaskHomeInputBean bean);

    /**
     * 获取巡检任务列表回调
     *
     * @param
     */
    void callBackTaskInspectionHomeList(ResultBean<List<InspectionTaskHomeBean>, String> resultBean);
}
