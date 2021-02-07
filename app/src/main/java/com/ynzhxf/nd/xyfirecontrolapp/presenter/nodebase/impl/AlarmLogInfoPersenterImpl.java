package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.AlarmLogBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.nodebase.NodeBaseModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IAlarmLogInfoPersenter;

/**
 * Created by nd on 2018-08-02.
 */

class AlarmLogInfoPersenterImpl extends BasePersenter implements IAlarmLogInfoPersenter {

    private IAlarmLogInfoView view;

    private  IAlarmLogInfoModel model;

    public AlarmLogInfoPersenterImpl(IAlarmLogInfoView view){
        this.view = view;
        this.model = NodeBaseModelFactory.getAlarmLogInfoModel(this);
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
    public void doAlarmLogInfo(String ID) {
        model.requestAlarmLogInfo(ID);
    }

    @Override
    public void callBackAlarmLogInfo(ResultBean<AlarmLogBean, String> result) {
        if(view != null){
            view.callBackAlarmLogInfo(result);
        }
    }
}
