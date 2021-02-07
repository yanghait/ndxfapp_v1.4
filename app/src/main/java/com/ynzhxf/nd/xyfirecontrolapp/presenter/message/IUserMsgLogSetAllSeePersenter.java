package com.ynzhxf.nd.xyfirecontrolapp.presenter.message;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

/**
 * 设置所有未读的消息未已阅读
 * Created by nd on 2018-08-02.
 */

public interface IUserMsgLogSetAllSeePersenter extends IBasePersenter {

    String TAG = "UserMsgLogSetAllSee";

    /**
     * 视图接口
     */
    interface IUserMsgLogSetAllSeeView extends IBaseView {
        /**
         *数据请求完成回调
         */
        void callBackUserMsgLogSetAllSee(ResultBean<Boolean, String> result);
    }

    /**
     * 模型接口
     */
    interface IUserMsgLogSetAllSeeModel{

        /**
         * 数据加载请求
         */
        void requestUserMsgLogSetAllSee();
    }

    /**
     * 发送数据请求
     */
    void doUserMsgLogSetAllSee();

    /**
     * 完成请求返回数据
     */
    void callBackUserMsgLogSetAllSee(ResultBean<Boolean, String> result);
}
