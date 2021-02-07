package com.ynzhxf.nd.xyfirecontrolapp.presenter.message;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

/**
 * 获取用户还未查看的消息数量
 * Created by nd on 2018-08-02.
 */
public interface IUserMsgLogNotSeeCountPersenter extends IBasePersenter {

    String TAG = "UserMsgLogNotSeeCount";

    /**
     * 视图接口
     */
    interface IUserMsgLogNotSeeCountView extends IBaseView {
        /**
         *数据请求完成回调
         */
        void callBackUserMsgLogNotSeeCount(ResultBean<Integer, String> result);
    }

    /**
     * 模型接口
     */
    interface IUserMsgLogNotSeeCountModel{

        /**
         * 数据加载请求
         */
        void requestUserMsgLogNotSeeCount();
    }

    /**
     * 发送数据请求
     */
    void doUserUserMsgLogNotSeeCount();

    /**
     * 完成请求返回数据
     */
    void callBackUserMsgLogNotSeeCount(ResultBean<Integer, String> result);
}
