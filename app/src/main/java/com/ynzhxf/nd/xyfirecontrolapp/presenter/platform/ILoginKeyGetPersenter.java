package com.ynzhxf.nd.xyfirecontrolapp.presenter.platform;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;


/**
 * 获取平台授权登陆的Key
 * Created by nd on 2018-07-13.
 */

public interface ILoginKeyGetPersenter extends IBasePersenter{

    String TAG = "LoginKeyGet";

     interface ILoginKeyGetView extends IBaseView{
        /**
         * 登陆处理正常回调
         * @param resultBean
         */
        void callBackLoginKeyGet(ResultBean<String , String> resultBean);

    }

     interface ILoginKeyGetModel{
        /**
         * 请求登陆
         */
        void requestLoginKeyGet();
    }

    /**
     * 数据获取
     */
    void dologinKeyGetPersenter();

    /**
     * 获取成功回调
     * @param result
     */
    void callBackLoginKeyGet(ResultBean<String, String> result);




}
