package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireAlarmHostBuildInfoCountBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

import java.util.List;

/**
 * 火灾报警系统主机列表获取
 *
 * Created by nd on 2018-07-25.
 */
public interface IFireAlarmHostListPersenter extends IBasePersenter {
    String TAG = "FireAlarmHostList";

    /**
     * 视图接口
     */
    interface IFireAlarmHostListView extends IBaseView {
        /**
         * 数据请求完成回调
         */
        void callBackFireAlarmHostList(ResultBean<List<FireAlarmHostBuildInfoCountBean>, String> result);

    }

    /**
     * 模型接口
     */
    interface IFireAlarmHostListModel {

        /**
         * 数据请求
         *
         * @param proSysID 系统ID
         */
        void requestFireAlarmHostList(String proSysID);
    }

    /**
     * 发送数据请求
     *
     * @param proSysID 标签ID
     */
    void doFireAlarmHostList(String proSysID);


    /**
     * 完成请求返回数据
     */
    void callBackFireAlarmHostList(ResultBean<List<FireAlarmHostBuildInfoCountBean>, String> result);
}
