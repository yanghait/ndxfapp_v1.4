package com.ynzhxf.nd.xyfirecontrolapp.presenter.message.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.message.MessageModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.IUserMsgLogSetSeePersenter;

/**
 * Created by nd on 2018-08-02.
 */

class UserMsgLogSetSeePersenterImpl extends BasePersenter implements IUserMsgLogSetSeePersenter {

    private IUserMsgLogSetSeeView view;

    private IUserMsgLogSetSeeModel model;

    public UserMsgLogSetSeePersenterImpl(IUserMsgLogSetSeeView view){
        this.view = view;
        this.model = MessageModelFactory.getUserMsgLogSetSeeModel(this);
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
    public void doUserMsgLogSetSee(String msgLogID) {
        model.requestUserMsgLogSetSee(msgLogID);
    }

    @Override
    public void callBackUserMsgLogSetSee(ResultBean<Boolean, String> result) {
        if(view != null){
            view.callBackUserMsgLogSetSee(result);
        }
    }
}
