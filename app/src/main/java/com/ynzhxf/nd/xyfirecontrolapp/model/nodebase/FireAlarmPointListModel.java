package com.ynzhxf.nd.xyfirecontrolapp.model.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireAlarmPointBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IFireAlarmPointListPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 获取火灾报警数据
 * Created by nd on 2018-07-26.
 */

class FireAlarmPointListModel extends BaseModel implements IFireAlarmPointListPersenter.IFireAlarmPointListModel {

    private IFireAlarmPointListPersenter persenter;

    public FireAlarmPointListModel(IFireAlarmPointListPersenter persenter){
        this.persenter = persenter;
    }

    @Override
    public void requestFireAlarmPointList(String proSysID, String hostID, String buildName, int floor) {
        HttpUtils.getRetrofit().getFireAlarmPointList(HelperTool.getToken() , proSysID , hostID ,buildName, floor)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<List<FireAlarmPointBean>,String>>() {
                               @Override
                               public void onNext(ResultBean<List<FireAlarmPointBean>,String> resultBean) {
                                   persenter.callBackFireAlarmPointList(resultBean);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e),IFireAlarmPointListPersenter.TAG);
                               }
                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}
