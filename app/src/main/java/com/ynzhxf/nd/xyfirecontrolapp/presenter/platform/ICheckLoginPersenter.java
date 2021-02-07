package com.ynzhxf.nd.xyfirecontrolapp.presenter.platform;

import com.ynzhxf.nd.xyfirecontrolapp.bean.platform.LoginInfoBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

import java.util.Map;

/**
 * 用户登陆检测
 * Created by nd on 2018-07-14.
 */

public interface ICheckLoginPersenter extends IBasePersenter{

     String TAG = "CheckLogin";

    interface ICheckLoginView extends IBaseView {
        /**
         * 检测用户是否登陆处理正常回调
         * @param resultBean
         */
        void callBackCheckLogin(ResultBean<LoginInfoBean,Map<String,String>> resultBean);

    }

    interface ICheckLoginModel{
        /**
         * 检测用户登陆
         * @param bean
         */
        void requestCheckLogin(LoginInfoBean bean);
    }

    /**
     * 登陆检测处理
     * @param bean
     */
    void doChecklogin(LoginInfoBean bean);

    /**
     * 登陆检测成功回调
     * @param result
     */
    void callBackCheckLogin(ResultBean<LoginInfoBean,Map<String,String>> result);

}
