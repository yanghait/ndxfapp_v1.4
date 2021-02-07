package com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.platform.LoginInfoBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.Platform.PlatformModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.IUserInfoGetPersenter;

/**
 * 用户相关信息获取
 * Created by nd on 2018-07-31.
 */

class UserInfoGetPersenterImpl implements IUserInfoGetPersenter{

    private IUserInfoGetView view;
    private IUserInfoGetModel model;

    public UserInfoGetPersenterImpl(IUserInfoGetView view){
        this.view = view;
        this.model = PlatformModelFactory.getUserInfoGetModel(this);
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
    public void dolUserInfoGetPersenter() {
        model.requestUserInfoGet();
    }

    @Override
    public void callBackUserInfoGet(ResultBean<LoginInfoBean, String> result) {
        if(view != null){
            view.callBackLoginKeyGet(result);
        }
    }
}
