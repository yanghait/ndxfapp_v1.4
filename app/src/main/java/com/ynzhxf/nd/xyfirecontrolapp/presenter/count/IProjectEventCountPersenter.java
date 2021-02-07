package com.ynzhxf.nd.xyfirecontrolapp.presenter.count;

import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

import java.util.Map;

/**
 * 项目24小时事件统计和实时报警统计桥梁
 * Created by nd on 2018-07-16.
 */

public interface IProjectEventCountPersenter extends IBasePersenter{

    String TAG = "TAG";

    interface IProjectEventCountView extends IBaseView {

        /**
         *数据请求完成回调
         */
        void callBackProjectEventCount(Map<String, String> result);

    }

    interface IProjectEventCountModel{
        /**
         * 数据加载请求
         */
        void requestProjectEventCount(String proID);
    }

    /**
     * 发送数据请求（使用项目ID，获取该项目的事件和实时报警统计）
     */
    void doProjectEventCount(String ProID);

    /**
     * 完成请求返回数据
     */
    void callBackProjectEventCount(Map<String, String> result);


}
