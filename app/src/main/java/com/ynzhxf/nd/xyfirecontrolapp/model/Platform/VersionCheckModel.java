package com.ynzhxf.nd.xyfirecontrolapp.model.Platform;

import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.IVersionCheckPersenter;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * APP版本检测
 * Created by nd on 2018-08-08.
 */

class VersionCheckModel extends BaseModel implements IVersionCheckPersenter.IVersionCheckModel{
    private IVersionCheckPersenter persenter;

    public VersionCheckModel(IVersionCheckPersenter persenter){
        this.persenter = persenter;
    }

    @Override
    public void requestVersionCheck() {
        HttpUtils.getRetrofit().getAppCheckVersion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Map<String , String>>() {
                               @Override
                               public void onNext(Map<String , String>  resultBean) {
                                   persenter.callBackVersionCheck(resultBean);
                               }
                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e),IVersionCheckPersenter.TAG);
                               }
                               @Override
                               public void onComplete() {


                               }
                           }
                );
    }
}
