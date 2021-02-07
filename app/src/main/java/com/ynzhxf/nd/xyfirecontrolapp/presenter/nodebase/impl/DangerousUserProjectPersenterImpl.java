package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.nodebase.NodeBaseModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IDangeroursUserProjectPersenter;

import java.util.List;

/**
 * 精准治理桥梁
 * Created by nd on 2018-07-16.
 */

class DangerousUserProjectPersenterImpl extends BasePersenter implements IDangeroursUserProjectPersenter{

    private IDangeroursUserProjectPersenter.IDangeroursUserProjectView view;
    private IDangeroursUserProjectPersenter.IDangeroursUserProjectModel model;

    public DangerousUserProjectPersenterImpl(IDangeroursUserProjectPersenter.IDangeroursUserProjectView view){
        this.view = view;
        model = NodeBaseModelFactory.getDangeroursUserProjectModelImpl(this);
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
    public void doDangeroursUserProject() {
        model.requestDangeroursUserProject();
    }

    @Override
    public void callBackDangeroursUserProject(ResultBean<List<ProjectNodeBean>, String> result) {
        view.callBackDangeroursUserProject(result);
    }
}
