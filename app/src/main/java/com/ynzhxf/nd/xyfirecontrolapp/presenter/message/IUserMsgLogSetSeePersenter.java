package com.ynzhxf.nd.xyfirecontrolapp.presenter.message;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

/**
 * 设置用户消息为已读
 * Created by nd on 2018-08-02.
 */

public interface IUserMsgLogSetSeePersenter extends IBasePersenter {

    String TAG = "UserMsgLogSetSee";

    /**
     * 视图接口
     */
    interface IUserMsgLogSetSeeView extends IBaseView {
        /**
         *数据请求完成回调
         */
        void callBackUserMsgLogSetSee(ResultBean<Boolean, String> result);
    }

    /**
     * 模型接口
     */
    interface IUserMsgLogSetSeeModel{

        /**
         * 数据加载请求
         * @param msgLogID 消息ID
         */
        void requestUserMsgLogSetSee(String msgLogID);
    }

    /**
     * 发送数据请求
     * @param msgLogID 消息ID
     */
    void doUserMsgLogSetSee(String msgLogID);

    /**
     * 完成请求返回数据
     */
    void callBackUserMsgLogSetSee(ResultBean<Boolean, String> result);
}
