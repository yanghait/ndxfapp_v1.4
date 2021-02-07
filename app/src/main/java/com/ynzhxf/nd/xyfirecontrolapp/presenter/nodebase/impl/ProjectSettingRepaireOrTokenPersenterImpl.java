package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.nodebase.NodeBaseModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IProjectSettingRepaireOrTokenPersenter;

/**
 * 消防接管和维保状态设置数据请求桥梁
 * Created by nd on 2018-07-19.
 */

class ProjectSettingRepaireOrTokenPersenterImpl extends BasePersenter implements IProjectSettingRepaireOrTokenPersenter {

    private IProjectSettingRepaireOrTokenView view;
    private IProjectSettingRepaireOrTokenModel model;

    public ProjectSettingRepaireOrTokenPersenterImpl(IProjectSettingRepaireOrTokenView view){
        this.view = view;
        this.model = NodeBaseModelFactory.getProjectSettingRepaireOrTokenModel(this);

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

    @Override
    public void doProjectSettingRepaireOrToken(String proID, String confirmPwd, String type) {
        model.requestProjectSettingRepaireOrToken(proID , confirmPwd , type);
    }

    @Override
    public void callBackProjectSettingRepaireOrToken(ResultBean<ProjectNodeBean, String> result) {
        if(view != null){
            view.callBackProjectSettingRepaireOrToken(result);
        }
    }
}
