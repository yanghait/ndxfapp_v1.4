package com.ynzhxf.nd.xyfirecontrolapp.presenter.count.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.count.CountModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.count.IProjectEventCountPersenter;

import java.util.Map;

/**
 * 项目统计数据获取
 * Created by nd on 2018-07-18.
 */

 class ProjectEventCountPersenterImpl extends BasePersenter implements IProjectEventCountPersenter {

    private IProjectEventCountView view;
    private IProjectEventCountModel model;
    public ProjectEventCountPersenterImpl(IProjectEventCountView view){
        this.view = view;
        this.model = CountModelFactory.getProjectEventCountModel(this);
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
    public void doProjectEventCount(String proID) {
        model.requestProjectEventCount(proID);
    }

    @Override
    public void callBackProjectEventCount(Map<String, String> result) {
        if(view != null){
            view.callBackProjectEventCount(result);
        }
    }
}
