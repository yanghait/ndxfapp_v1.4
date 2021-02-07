package com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.Platform.PlatformModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.IUserPushStatePersenter;

/**
 *用户消息推送状态获取
 * Created by nd on 2018-08-01.
 */

class UserPushStatePersenterImpl extends BasePersenter implements IUserPushStatePersenter {

    private IUserPushStateView view;

    private IUserPushStateModel model;

    public UserPushStatePersenterImpl(IUserPushStateView view){
        this.view = view;
        this.model = PlatformModelFactory.getUserPushStateModel(this);
    }

    @Override
    public void callBackError(ResultBean<String, String> result, String action) {
        if(view != null){
            view .callBackError(result , action);
        }
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void dolUserPushStatePersenter() {
        model.requestUserPushState();
    }

    @Override
    public void callBackUserPushState(ResultBean<Boolean, String> result) {
        if(view !=null){
            view.callBackrUserPushState(result);
        }
    }
}
