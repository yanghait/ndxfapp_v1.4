package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectSystemBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.nodebase.NodeBaseModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IProjectSystemListPersenter;

import java.util.List;

/**
 * 系统列表获取桥梁
 * Created by nd on 2018-07-23.
 */

class ProjectSystemListPersenterImpl extends BasePersenter implements IProjectSystemListPersenter {

    private IIProjectSystemListView view;

    private IIProjectSystemListModel model;

    public ProjectSystemListPersenterImpl(IIProjectSystemListView view){
        this.view = view;
        this.model = NodeBaseModelFactory.getProjectSystemListModel(this);
    }

    @Override
    public void doProjectSystemList(String proID) {
        model.requestProjectSystemList(proID);
    }

    @Override
    public void callBackProjectSettingRepaireOrToken(ResultBean<List<ProjectSystemBean>, String> result) {
        if(view != null){
            view.callBackProjectSystemList(result);
        }
    }

    @Override
    public void callBackError(ResultBean<String, String> result, String action) {
        if(view != null){
            view.callBackError(result , action);
        }
    }

    @Override
    public void detachView() {
        view = null;
    }
}
