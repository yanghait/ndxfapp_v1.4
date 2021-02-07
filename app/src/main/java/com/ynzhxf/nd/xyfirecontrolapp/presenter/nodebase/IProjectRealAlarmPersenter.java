package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.LabelNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

import java.util.List;

/**
 * 获取项目的实时报警数据
 *
 * Created by nd on 2018-07-25.
 */
public interface IProjectRealAlarmPersenter extends IBasePersenter {

    String TAG = "ProjectRealAlarm";

    /**
     * 视图接口
     */
    interface IProjectRealAlarmView extends IBaseView {
        /**
         * 数据请求完成回调
         */
        void callBackProjectRealAlarm(ResultBean<List<LabelNodeBean>, String> result);

    }

    /**
     * 模型接口
     */
    interface IProjectRealAlarmModel {

        /**
         * 数据请求
         *
         * @param proID 项目ID
         */
        void requestProjectRealAlarmList(String proID);
    }

    /**
     * 发送数据请求
     *
     * @param proID 项目ID
     */
    void doProjectRealAlarm(String proID);


    /**
     * 完成请求返回数据
     */
    void callBackProjectRealAlarm(ResultBean<List<LabelNodeBean>, String> result);
}
