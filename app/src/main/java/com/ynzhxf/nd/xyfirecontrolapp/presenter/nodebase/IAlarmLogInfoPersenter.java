package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.AlarmLogBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

/**
 * 据报警ID获得报警的详细信息
 * Created by nd on 2018-08-02.
 */

public interface IAlarmLogInfoPersenter extends IBasePersenter {
    String TAG = "AlarmLogInfo";
    interface IAlarmLogInfoView extends IBaseView {
        /**
         *数据请求完成回调
         */
        void callBackAlarmLogInfo(ResultBean<AlarmLogBean, String> result);

    }

    /**
     * 模型接口
     */
    interface IAlarmLogInfoModel{
        /**
         * 数据加载请求
         */
        void requestAlarmLogInfo(String ID);
    }

    /**
     * 发送数据请求
     */
    void doAlarmLogInfo(String ID);

    /**
     * 完成请求返回数据
     */
    void callBackAlarmLogInfo(ResultBean<AlarmLogBean, String> result);
}
