package com.ynzhxf.nd.xyfirecontrolapp.presenter.message.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.message.MessageModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.IUserMsgLogNotSeeCountPersenter;

/**
 * 获取用户还未查看的消息数量
 * Created by nd on 2018-08-02.
 */

class UserMsgLogNotSeeCountPersenterImpl extends BasePersenter implements IUserMsgLogNotSeeCountPersenter {

    private IUserMsgLogNotSeeCountView view;

    private IUserMsgLogNotSeeCountModel model;

    public UserMsgLogNotSeeCountPersenterImpl(IUserMsgLogNotSeeCountView view){
        this.view = view;
        this.model = MessageModelFactory.getUserMsgLogNotSeeCountModel(this);
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
    public void doUserUserMsgLogNotSeeCount() {
        model.requestUserMsgLogNotSeeCount();
    }

    @Override
    public void callBackUserMsgLogNotSeeCount(ResultBean<Integer, String> result) {
        if(view != null){
            view.callBackUserMsgLogNotSeeCount(result);
        }
    }
}
