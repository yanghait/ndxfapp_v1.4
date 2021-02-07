package com.ynzhxf.nd.xyfirecontrolapp.presenter.message;

import com.ynzhxf.nd.xyfirecontrolapp.bean.PagingBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.message.UserMessagePushLogBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

/**
 * 获取用户相关的消息记录
 * Created by nd on 2018-07-16.
 */

public interface IUserPushMsgLogPersenter extends IBasePersenter {

    String TAG = "UserPushMsgLog";

    /**
     * 视图接口
     */
    interface IUserPushMsgLogView extends IBaseView {
        /**
         * 数据请求完成回调
         */
        void callBackUserPushMsgLog(ResultBean<PagingBean<UserMessagePushLogBean>, String> result);
    }

    /**
     * 模型接口
     */
    interface IUserPushMsgLogModel {

        /**
         * 数据加载请求
         *
         * @param pageSize 当前页码
         */
        void requestUserPushMsgLog(int pageSize, String typeId);
    }

    /**
     * 发送数据请求
     *
     * @param pageSize 当前页码
     */
    void doUserPushMsgLog(int pageSize, String typeId);

    /**
     * 完成请求返回数据
     */
    void callBackUserPushMsgLog(ResultBean<PagingBean<UserMessagePushLogBean>, String> result);
}
