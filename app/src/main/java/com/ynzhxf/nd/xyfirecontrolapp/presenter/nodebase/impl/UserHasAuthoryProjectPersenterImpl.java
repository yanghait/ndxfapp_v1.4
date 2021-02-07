package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.nodebase.NodeBaseModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IUserHasAuthoryProjectPersenter;

import java.util.List;

/**
 * Created by nd on 2018-08-01.
 */

class UserHasAuthoryProjectPersenterImpl extends BasePersenter implements IUserHasAuthoryProjectPersenter {

    private IUserHasAuthoryProjectView view;
    private IUserHasAuthoryProjectModel model;

    public UserHasAuthoryProjectPersenterImpl(IUserHasAuthoryProjectView view){
        this.view = view;
        this.model = NodeBaseModelFactory.getUserHasAuthoryProjectModel(this);
    }

    @Override
    public void callBackError(ResultBean<String, String> result, String action) {
        if(this.view != null){
            view.callBackError(result ,action);
        }
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void doUserHasAuthoryProject() {
        model.requestUserHasAuthoryProject();
    }

    @Override
    public void callBackUserHasAuthoryProject(ResultBean<List<ProjectNodeBean>, String> resultBean) {
        if(view != null){
            view.callBackUserHasAuthoryProject(resultBean);
        }
    }
}
