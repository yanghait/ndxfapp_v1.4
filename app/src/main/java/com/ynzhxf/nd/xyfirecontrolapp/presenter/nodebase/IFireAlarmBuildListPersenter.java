package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireAlarmHostBuildInfoCountBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

/**
 * 获取火灾报警主机覆盖的建筑物列表
 * Created by nd on 2018-07-25.
 */

public interface IFireAlarmBuildListPersenter extends IBasePersenter {

    String TAG = "FireAlarmBuildList";

    /**
     * 视图接口
     */
    interface IFireAlarmBuildListView extends IBaseView {
        /**
         * 数据请求完成回调
         */
        void callBackFireAlarmBuildList(ResultBean<FireAlarmHostBuildInfoCountBean, String> result);

    }

    /**
     * 模型接口
     */
    interface IFireAlarmBuildListModel {

        /**
         * 数据请求
         *
         * @param proSysID 系统ID
         * @param hostID 报警主机ID
         */
        void requestFireAlarmBuildList(String proSysID , String hostID);
    }

    /**
     * 发送数据请求
     *
     * @param proSysID 标签ID
     */
    void doFireAlarmBuildList(String proSysID , String hostID);


    /**
     * 完成请求返回数据
     */
    void callBackFireAlarmBuildList(ResultBean<FireAlarmHostBuildInfoCountBean, String> result);
}