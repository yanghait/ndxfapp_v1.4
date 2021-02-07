package com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.MaintenListAllBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.MaintenanceListInfoBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

public interface IMaintenanceListPresenter extends IBasePersenter {

    String TAG = "IMaintenanceListPresenter";

    interface IMaintenanceListView extends IBaseView {
        /**
         * 维保获取列表回调
         *
         * @param
         */
        void callBackMaintenanceList(ResultBean<MaintenListAllBean, String> resultBean);

    }

    interface IMaintenanceListModel {
        /**
         * 请求维保列表
         *
         * @param bean
         */
        void requestgetMaintenList(MaintenanceListInfoBean bean);
    }

    /**
     * 维保列表
     *
     * @param bean
     */
    void doMaintenanceList(MaintenanceListInfoBean bean);

    /**
     * 获取维保回调
     *
     * @param
     */
    void callBackMaintenanceList(ResultBean<MaintenListAllBean, String> resultBean);
}
