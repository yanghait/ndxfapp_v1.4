package com.ynzhxf.nd.xyfirecontrolapp.presenter.platform;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;


/**
 * 退出登陆
 * Created by nd on 2018-07-13.
 */

public interface ILoginOutPersenter extends IBasePersenter{
    String TAG = "LoginOut";
     interface ILoginOutView extends IBaseView{
        /**
         *退出登陆处理正常回调
         */
        void callBackLoginOut(ResultBean<String,String> result);

    }

     interface ILoginOutModel{
        /**
         * 退出登陆请求
         */
        void requestLoginOut();
    }

    /**
     * 退出登陆请求
     */
    void dologinOut();

    /**
     * 退出登陆处理正常回调
     */
    void callBackLoginOut(ResultBean<String,String> result);




}
