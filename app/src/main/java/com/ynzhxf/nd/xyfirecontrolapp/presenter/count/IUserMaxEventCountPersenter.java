package com.ynzhxf.nd.xyfirecontrolapp.presenter.count;

import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

import java.util.Map;

/**
 * 用户24小时事件统计和实时报警统计桥梁
 * Created by nd on 2018-07-16.
 */

public interface IUserMaxEventCountPersenter extends IBasePersenter{

    String TAG = "UserMaxEventCount";

    interface IUserMaxEventCountView extends IBaseView {

        /**
         *数据请求完成回调
         */
        void callBackUserMaxEventCount(Map<String,String> result);

    }

    interface IUserMaxEventCountModel{
        /**
         * 数据加载请求
         */
        void requestUserMaxEventCount();
    }

    /**
     * 发送数据请求
     */
    void doUserMaxEventCount();

    /**
     * 完成请求返回数据
     */
    void callBackUserMaxEventCount(Map<String, String> result);


}
