package com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.Platform.PlatformModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.ILoginKeyGetPersenter;

/**
 * 获取平台授权的令牌
 * Created by nd on 2018-07-29.
 */
class LoginKeyGetPersenterImpl extends BasePersenter implements ILoginKeyGetPersenter{

    public ILoginKeyGetView view;
    public ILoginKeyGetModel model;

    public LoginKeyGetPersenterImpl(ILoginKeyGetView view){
        this.view = view;
        this.model = PlatformModelFactory.getLoginKeyGetModel(this);
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
    public void dologinKeyGetPersenter() {
        model.requestLoginKeyGet();
    }

    @Override
    public void callBackLoginKeyGet(ResultBean<String, String> result) {
        if(view != null){
            view.callBackLoginKeyGet(result);
        }
    }
}
