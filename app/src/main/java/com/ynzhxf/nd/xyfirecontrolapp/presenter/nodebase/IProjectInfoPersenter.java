package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

/**
 * 使用项目ID，获取项目信息数据获取桥梁
 * Created by nd on 2018-07-16.
 */

public interface IProjectInfoPersenter extends IBasePersenter{

    String TAG = "ProjectInfo";

    /**
     * 视图接口
     */
    interface IProjectInfoView extends IBaseView {
        /**
         *数据请求完成回调
         */
        void callBackProjectInfo(ResultBean<ProjectNodeBean, String> result);

    }

    /**
     * 模型接口
     */
    interface IProjectInfoModel{

        /**
         * 数据加载请求
         * @param proID 项目ID
         */
        void requestProjectInfo(String proID);
    }

    /**
     * 发送数据请求
     * @param proID 项目ID
     */
    void doProjectInfo(String proID);

    /**
     * 完成请求返回数据
     */
    void callBackProjectInfo(ResultBean<ProjectNodeBean, String> result);
}
