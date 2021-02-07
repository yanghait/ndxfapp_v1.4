package com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.CompanyBackFillParamsBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.OwnerOrderDetailsInputBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

public interface ICompanyBackFillParamsPresenter extends IBasePersenter {

    String TAG = "ICompanyBackFillParamsPresenter";

    interface ICompanyBackFillParamsView extends IBaseView {
        /**
         * 公司维保回填参数回调
         *
         * @param
         */
        void callBackCompanyBackFillParams(ResultBean<CompanyBackFillParamsBean, String> resultBean);

    }

    interface ICompanyBackFillParamsModel {
        /**
         * 请求公司维保回填参数
         *
         * @param bean
         */
        void requestCompanyBackFillParams(OwnerOrderDetailsInputBean bean);
    }

    /**
     * 公司维保回填参数
     *
     * @param bean
     */
    void doCompanyBackFillParams(OwnerOrderDetailsInputBean bean);

    /**
     * 公司维保回填参数回调
     *
     * @param
     */
    void callBackCompanyBackFillParams(ResultBean<CompanyBackFillParamsBean, String> resultBean);
}
