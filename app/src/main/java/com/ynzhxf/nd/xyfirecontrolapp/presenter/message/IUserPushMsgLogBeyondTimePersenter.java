package com.ynzhxf.nd.xyfirecontrolapp.presenter.message;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.message.UserMessagePushLogBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

import java.util.List;

/**
 * 获取用户相关的消息记录
 * Created by nd on 2018-07-16.
 */

public interface IUserPushMsgLogBeyondTimePersenter extends IBasePersenter {

    String TAG = "UserPushMsgLogBeyondTime";

    /**
     * 视图接口
     */
    interface IUserPushMsgLogBeyondTimeView extends IBaseView {
        /**
         * 数据请求完成回调
         */
        void callBackUserPushMsgLogBeyondTime(ResultBean<List<UserMessagePushLogBean>, String> result);
    }

    /**
     * 模型接口
     */
    interface IUserPushMsgLogBeyondTimeModel {

        /**
         * 数据加载请求
         *
         * @param pushLogID 消息记录ID
         */
        void requestUserPushMsgLog(String pushLogID, String typeId);
    }

    /**
     * 发送数据请求
     *
     * @param pushLogID 消息记录ID
     */
    void doUserPushMsgLogBeyondTime(String pushLogID, String typeId);

    /**
     * 完成请求返回数据
     */
    void callBackUserPushMsgLogBeyondTime(ResultBean<List<UserMessagePushLogBean>, String> result);
}
