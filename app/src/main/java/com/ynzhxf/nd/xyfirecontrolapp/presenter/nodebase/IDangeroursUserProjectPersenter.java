package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

import java.util.List;


/**
 * 当前登陆用户最危险项目列表（精准治理项目列表）
 */

public interface IDangeroursUserProjectPersenter extends IBasePersenter{

    String TAG = "DangeroursUserProjec";

     interface IDangeroursUserProjectView extends IBaseView{
        /**
         *数据请求完成回调
         */
        void callBackDangeroursUserProject(ResultBean<List<ProjectNodeBean>, String> result);

    }

     interface IDangeroursUserProjectModel{
        /**
         * 数据加载请求
         */
        void requestDangeroursUserProject();
    }

    /**
     * 发送数据请求
     */
    void doDangeroursUserProject();

    /**
     * 完成请求返回数据
     */
    void callBackDangeroursUserProject(ResultBean<List<ProjectNodeBean>,String> result);




}
