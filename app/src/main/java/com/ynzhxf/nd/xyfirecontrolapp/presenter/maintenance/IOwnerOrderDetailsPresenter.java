package com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.OwnerOrderDetailsInputBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.OwnerWorkOrderDetailsBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

public interface IOwnerOrderDetailsPresenter extends IBasePersenter {

    String TAG = "IOwnerOrderDetailsPresenter";

    interface IOwnerOrderDetailsView extends IBaseView {
        /**
         * 维保业主获取工单详情回调
         *
         * @param
         */
        void callBackOwnerOrderDetails(ResultBean<OwnerWorkOrderDetailsBean, String> resultBean);

    }

    interface IOwnerOrderDetailsModel {
        /**
         * 请求维保业主工单详情
         *
         * @param bean
         */
        void requestOwnerOrderDetails(OwnerOrderDetailsInputBean bean);
    }

    /**
     * 维保业主工单详情
     *
     * @param bean
     */
    void doOwnerOrderDetails(OwnerOrderDetailsInputBean bean);

    /**
     * 获取业主维保详情回调
     *
     * @param
     */
    void callBackOwnerOrderDetails(ResultBean<OwnerWorkOrderDetailsBean, String> resultBean);
}
