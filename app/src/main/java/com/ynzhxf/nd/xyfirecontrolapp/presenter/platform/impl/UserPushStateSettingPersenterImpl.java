package com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.Platform.PlatformModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.IUserPushStateSettingPersenter;

/**
 * 设置用户是否接受推送消息提醒状态
 * Created by nd on 2018-08-01.
 */

class UserPushStateSettingPersenterImpl extends BasePersenter implements IUserPushStateSettingPersenter {

    private IUserPushStateSettingView view;

    private IUserPushStateSettingModel model;

    public UserPushStateSettingPersenterImpl(IUserPushStateSettingView view){
        this.view = view;
        this.model = PlatformModelFactory.getUserPushStateSettingModel(this);
    }

    @Override
    public void callBackError(ResultBean<String, String> result, String action) {
        if(view != null){
            view.callBackError(result ,action);
        }
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void dolUserPushStateSettingPersenter() {
        model.requestUserPushStateSetting();;
    }

    @Override
    public void callBackUserPushStateSetting(ResultBean<Boolean, String> result) {
        if(view != null){
            view.callBackrUserPushStateSetting(result);
        }
    }
}
