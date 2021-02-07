package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireAlarmPointBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.nodebase.NodeBaseModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IFireAlarmPointListPersenter;

import java.util.List;

/**
 * 获取火灾报警主机覆盖的建筑物列表
 * Created by nd on 2018-07-26.
 */

class FireAlarmPointListPersenterImpl extends BasePersenter implements IFireAlarmPointListPersenter {

    private IFireAlarmPointListView view;
    private IFireAlarmPointListModel model;

    public FireAlarmPointListPersenterImpl(IFireAlarmPointListView view){
        this.view = view;
        this.model = NodeBaseModelFactory.getFireAlarmPointListModel(this);
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
    public void doFireAlarmPointList(String proSysID, String hostID, String buildName, int floor) {
        model.requestFireAlarmPointList(proSysID , hostID , buildName ,floor);
    }

    @Override
    public void callBackFireAlarmPointList(ResultBean<List<FireAlarmPointBean>, String> result) {
        if(view != null){
            view.callBackFireAlarmPointList(result);
        }
    }
}
