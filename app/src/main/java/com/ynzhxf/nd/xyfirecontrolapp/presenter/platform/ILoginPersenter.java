package com.ynzhxf.nd.xyfirecontrolapp.presenter.platform;

import com.ynzhxf.nd.xyfirecontrolapp.bean.platform.LoginInfoBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

import java.util.Map;


/**
 * 登陆数据处理
 * Created by nd on 2018-07-13.
 */

public interface ILoginPersenter extends IBasePersenter{

    String TAG = "Login";

     interface ILoginView extends IBaseView{
        /**
         * 登陆处理正常回调
         * @param resultBean
         */
        void callBackLogin(ResultBean<LoginInfoBean,Map<String,String>> resultBean);

    }

     interface ILoginModel{
        /**
         * 请求登陆
         * @param bean
         */
        void requestLogin(LoginInfoBean bean);
    }

    /**
     * 登陆处理
     * @param bean
     */
    void dologin(LoginInfoBean bean);

    /**
     * 登陆成功回调
     * @param result
     */
    void callBackLogin(ResultBean<LoginInfoBean,Map<String,String>> result);




}
