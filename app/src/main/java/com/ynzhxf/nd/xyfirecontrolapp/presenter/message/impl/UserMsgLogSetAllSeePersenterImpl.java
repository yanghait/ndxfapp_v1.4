package com.ynzhxf.nd.xyfirecontrolapp.presenter.message.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.message.MessageModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.IUserMsgLogSetAllSeePersenter;

/**
 * Created by nd on 2018-08-02.
 */

class UserMsgLogSetAllSeePersenterImpl extends BasePersenter implements IUserMsgLogSetAllSeePersenter {

    private IUserMsgLogSetAllSeeView view;
    private IUserMsgLogSetAllSeeModel model;


    public UserMsgLogSetAllSeePersenterImpl(IUserMsgLogSetAllSeeView view){
        this.view = view;
        this.model = MessageModelFactory.getUserMsgLogSetAllSeeModel(this);
    }

    @Override
    public void callBackError(ResultBean<String, String> result, String action) {
        if(view != null){
            view.callBackError(result ,action);
            this.model = MessageModelFactory.getUserMsgLogSetAllSeeModel(this);
        }
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void doUserMsgLogSetAllSee() {
        model.requestUserMsgLogSetAllSee();
    }

    @Override
    public void callBackUserMsgLogSetAllSee(ResultBean<Boolean, String> result) {
        if(view != null){
            view.callBackUserMsgLogSetAllSee(result);
        }
    }
}
