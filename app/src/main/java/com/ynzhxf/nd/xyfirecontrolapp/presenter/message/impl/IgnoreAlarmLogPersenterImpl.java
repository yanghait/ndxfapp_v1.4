package com.ynzhxf.nd.xyfirecontrolapp.presenter.message.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.AlarmLogBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.message.MessageModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.IIgnoreAlarmLogPersenter;

/**
 * Created by nd on 2018-08-02.
 */

class IgnoreAlarmLogPersenterImpl extends BasePersenter implements IIgnoreAlarmLogPersenter {
    private IIgnoreAlarmLogView view;
    private IIgnoreAlarmLogModel model;


    public IgnoreAlarmLogPersenterImpl(IIgnoreAlarmLogView view){
        this.view = view;
        this.model = MessageModelFactory.getIgnoreAlarmLogModel(this);
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
    public void doIgnoreAlarmLog(String ID) {
        model.requestIgnoreAlarmLog(ID);
    }

    @Override
    public void callBackIgnoreAlarmLog(ResultBean<AlarmLogBean, String> result) {
        if(view != null){
            view.callBackIgnoreAlarmLog(result);
        }
    }
}
