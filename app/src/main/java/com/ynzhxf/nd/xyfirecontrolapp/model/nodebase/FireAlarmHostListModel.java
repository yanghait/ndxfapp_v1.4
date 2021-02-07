package com.ynzhxf.nd.xyfirecontrolapp.model.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireAlarmHostBuildInfoCountBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IFireAlarmHostListPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 使用火灾报警系统ID，获取火灾报警主机
 * Created by nd on 2018-07-25.
 */

public class FireAlarmHostListModel extends BaseModel implements IFireAlarmHostListPersenter.IFireAlarmHostListModel {
    private IFireAlarmHostListPersenter persenter;

    public FireAlarmHostListModel(IFireAlarmHostListPersenter persenter){
        this.persenter = persenter;
    }
    @Override
    public void requestFireAlarmHostList(String proSysID) {
        HttpUtils.getRetrofit().getFireAlarmHostList(HelperTool.getToken() , proSysID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<List<FireAlarmHostBuildInfoCountBean>,String>>() {
                               @Override
                               public void onNext(ResultBean<List<FireAlarmHostBuildInfoCountBean>,String> resultBean) {
                                   persenter.callBackFireAlarmHostList(resultBean);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e),IFireAlarmHostListPersenter.TAG);
                               }
                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}
