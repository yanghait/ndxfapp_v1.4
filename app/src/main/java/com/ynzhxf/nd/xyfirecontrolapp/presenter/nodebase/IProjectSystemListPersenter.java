package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectSystemBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

import java.util.List;

/**
 *
 * 使用项目ID和用户名获取系统列表
 * Created by nd on 2018-07-23.
 */

public interface IProjectSystemListPersenter extends IBasePersenter{

    String TAG = "ProjectSystemList";

    /**
     * 视图接口
     */
    interface IIProjectSystemListView extends IBaseView {
        /**
         *数据请求完成回调
         */
        void callBackProjectSystemList(ResultBean<List<ProjectSystemBean>, String> result);

    }

    /**
     * 模型接口
     */
    interface IIProjectSystemListModel{

        /**
         * 数据请求
         * @param proID 项目ID
         */
        void requestProjectSystemList(String proID);
    }

    /**
     * 发送数据请求
     * @param proID 项目ID
     */
    void doProjectSystemList(String proID );


    /**
     * 完成请求返回数据
     */
    void callBackProjectSettingRepaireOrToken(ResultBean<List<ProjectSystemBean>, String> result);
}
