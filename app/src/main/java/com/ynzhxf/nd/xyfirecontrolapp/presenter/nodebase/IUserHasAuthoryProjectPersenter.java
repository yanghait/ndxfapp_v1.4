package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

import java.util.List;

/**
 *
 * 获取用户有权限的所有项目列表
 * Created by nd on 2018-08-01.
 */

public interface IUserHasAuthoryProjectPersenter extends IBasePersenter {
    String TAG = "UserHasAuthoryProject";

    interface IUserHasAuthoryProjectView extends IBaseView {
        /**
         * 获取用户有权限的项目列表
         * @param resultBean
         */
        void callBackUserHasAuthoryProject(ResultBean<List<ProjectNodeBean>, String> resultBean);

    }

    interface IUserHasAuthoryProjectModel{
        /**
         * 发送项目列表获取请求
         */
        void requestUserHasAuthoryProject();
    }

    /**
     * 发送视频播放地址请求
     */
    void doUserHasAuthoryProject();

    /**
     * 请求完成回调
     * @param
     */
    void callBackUserHasAuthoryProject(ResultBean<List<ProjectNodeBean>, String> resultBean);
}