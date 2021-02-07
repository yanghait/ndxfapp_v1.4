package com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.Platform.PlatformModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.ILoginOutPersenter;

/**
 * 退出登陆
 * Created by nd on 2018-07-15.
 */

 class LoginOutPersenterImpl extends BasePersenter implements ILoginOutPersenter {

    private ILoginOutPersenter.ILoginOutView view;

    private ILoginOutPersenter.ILoginOutModel model;

    public LoginOutPersenterImpl(ILoginOutPersenter.ILoginOutView view) {
        this.view = view;
        model = PlatformModelFactory.getLoginOutModel(this);
    }

    @Override
    public void callBackError(ResultBean<String, String> result, String action) {
        if(view!=null) view.callBackError(result,action);
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void dologinOut() {
        model.requestLoginOut();
    }

    @Override
    public void callBackLoginOut(ResultBean<String, String> result) {
        view.callBackLoginOut(result);
    }
}
