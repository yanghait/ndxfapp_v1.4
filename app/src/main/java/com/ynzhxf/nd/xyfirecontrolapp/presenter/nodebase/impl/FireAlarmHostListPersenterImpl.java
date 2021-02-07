package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireAlarmHostBuildInfoCountBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.nodebase.NodeBaseModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IFireAlarmHostListPersenter;

import java.util.List;

/**
 * 获取火灾报警主机列表
 * Created by nd on 2018-07-25.
 */

class FireAlarmHostListPersenterImpl extends BasePersenter implements IFireAlarmHostListPersenter {

    private IFireAlarmHostListView view;
    private IFireAlarmHostListModel model;

    public FireAlarmHostListPersenterImpl(IFireAlarmHostListView view){
        this.view = view;
        this.model = NodeBaseModelFactory.getFireAlarmHostListModel(this);

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
    public void doFireAlarmHostList(String proSysID) {
        model.requestFireAlarmHostList(proSysID);
    }

    @Override
    public void callBackFireAlarmHostList(ResultBean<List<FireAlarmHostBuildInfoCountBean>, String> result) {
        if(view !=null ){
            view.callBackFireAlarmHostList(result);
        }
    }
}
