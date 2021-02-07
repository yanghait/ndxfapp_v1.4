package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.nodebase.NodeBaseModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IProjectInfoPersenter;

/**
 * 项目信息数据获取桥梁
 * Created by nd on 2018-07-19.
 */

class ProjectInfoPersenterImpl extends BasePersenter implements IProjectInfoPersenter {

    private IProjectInfoModel model;
    private IProjectInfoView view;

    public ProjectInfoPersenterImpl(IProjectInfoView view){
        this.view = view;
        this.model = NodeBaseModelFactory.getProjectInfoModelImpl(this);
    }

    @Override
    public void callBackError(ResultBean<String, String> result, String action) {
        if(view != null){
            view.callBackError(result,action);
        }
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void doProjectInfo(String proID) {
        model.requestProjectInfo(proID);
    }

    @Override
    public void callBackProjectInfo(ResultBean<ProjectNodeBean, String> result) {
        if(view != null){
            view.callBackProjectInfo(result);
        }
    }
}
