package com.ynzhxf.nd.xyfirecontrolapp.presenter.platform;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;


/**
 * 用户是否接受消息状态获取
 * Created by nd on 2018-07-13.
 */

public interface IUserPushStatePersenter extends IBasePersenter{
    String TAG = "UserPushState";
     interface IUserPushStateView extends IBaseView{
        /**
         * 请求完成回调
         * @param resultBean
         */
        void callBackrUserPushState(ResultBean<Boolean, String> resultBean);

    }

     interface IUserPushStateModel{

         /**\
          * 数据获取信息请求
          */
        void requestUserPushState();
    }

    /**
     * 数据获取请请求
     */
    void dolUserPushStatePersenter();

    /**
     * 获取成功回调
     * @param result
     */
    void callBackUserPushState(ResultBean<Boolean, String> result);




}
