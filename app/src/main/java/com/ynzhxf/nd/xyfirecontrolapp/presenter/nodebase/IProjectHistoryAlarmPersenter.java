package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.PagingBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.AlarmLogBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

/**
 * 获取项目的历史报警
 * Created by nd on 2018-07-27.
 */

public interface IProjectHistoryAlarmPersenter  extends IBasePersenter {

    String TAG = "ProjectHistoryAlarm";
    /**
     * 视图接口
     */
    interface IProjectHistoryAlarmView extends IBaseView {
        /**
         *数据请求完成回调
         */
        void callBackProjectHistoryAlarm(ResultBean<PagingBean<AlarmLogBean>, String> result);

    }

    /**
     * 模型接口
     */
    interface IProjectHistoryAlarmModel{

        /**
         * 数据加载请求
         * @param pageSize 页码
         * @param count 每页数量
         * @param proID 项目ID
         * @param startTime 开始时间
         * @param endTime 结束时间
         */
        void requestProjectHistoryAlarm(int pageSize, int count, String proID,String startTime,String endTime);
    }

    /**
     * 发送数据请求
     * @param pageSize 页码
     * @param count 每页数量
     * @param proID 项目ID
     * @param startTime 开始时间  yyyy-MM-dd HH:mm:ss
     * @param endTime 结束时间
     */
    void doProjectHistoryAlarm(int pageSize, int count, String proID,String startTime,String endTime);

    /**
     * 完成请求返回数据
     */
    void callBackProjectInfo(ResultBean<PagingBean<AlarmLogBean>, String> result);
}