package com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.Platform.PlatformModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.IVersionCheckPersenter;

import java.util.Map;

/**
 * 获取版本号
 * Created by nd on 2018-08-08.
 */

class VersionCheckPersenterImpl extends BasePersenter implements IVersionCheckPersenter {

    private IVersionCheckView view;
    private IVersionCheckModel model;

    public VersionCheckPersenterImpl(IVersionCheckView view){
        this.view = view;
        this.model = PlatformModelFactory.getVersionCheckModel(this);
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
    public void doVersionCheck() {
        model.requestVersionCheck();
    }

    @Override
    public void callBackVersionCheck(Map<String , String> result) {
        if(view != null){
            view.callBackVersionCheck(result);
        }
    }
}
