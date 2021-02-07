package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireAlarmPointBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

import java.util.List;

/**
 * 火灾报警楼层内探头点获取
 *
 * Created by nd on 2018-07-26.
 */

public interface IFireAlarmPointListPersenter extends IBasePersenter {

    String TAG = "FireAlarmPointList";
    /**
     * 视图接口
     */
    interface IFireAlarmPointListView extends IBaseView {
        /**
         * 数据请求完成回调
         */
        void callBackFireAlarmPointList(ResultBean<List<FireAlarmPointBean>, String> result);

    }

    /**
     * 模型接口
     */
    interface IFireAlarmPointListModel {

        /**
         *
         * @param proSysID 系统ID
         * @param hostID 报警主机ID
         * @param buildName 建筑名称
         * @param floor 楼层名称
         */
        void requestFireAlarmPointList(String proSysID , String hostID , String buildName,int floor);
    }

    /**
     * 发送数据请求
     * @param proSysID 系统ID
     * @param hostID 报警主机ID
     * @param buildName 建筑名称
     * @param floor 楼层名称
     */
    void doFireAlarmPointList(String proSysID , String hostID , String buildName,int floor);


    /**
     * 完成请求返回数据
     */
    void callBackFireAlarmPointList(ResultBean<List<FireAlarmPointBean>, String> result);
}
