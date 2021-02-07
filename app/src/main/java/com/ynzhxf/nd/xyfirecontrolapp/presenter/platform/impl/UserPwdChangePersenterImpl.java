package com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.Platform.PlatformModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.IUserPwdChangePersenter;

/**
 * 用户密码修改
 * Created by nd on 2018-08-01.
 */

class UserPwdChangePersenterImpl extends BasePersenter implements IUserPwdChangePersenter {
    private IUserPwdChangeView view;
    private IUserPwdChangeModel model;
    public UserPwdChangePersenterImpl(IUserPwdChangeView view){
        this.view = view;
        this.model = PlatformModelFactory.getUserPwdChangeModel(this);
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
    public void dolUserPwdChangePersenter(String oldPwd , String newPwd) {
        model.requestUserPwdChange(oldPwd , newPwd);
    }

    @Override
    public void callBackUserPwdChange(ResultBean<String, String> result) {
        if(view != null){
            view.callBackUserPwdChange(result);
        }
    }
}
