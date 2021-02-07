package com.ynzhxf.nd.xyfirecontrolapp.model.Platform;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.ILoginKeyGetPersenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 平台登陆令牌获取
 * Created by nd on 2018-07-29.
 */

class LoginKeyGetModel extends BaseModel implements ILoginKeyGetPersenter.ILoginKeyGetModel {

    private ILoginKeyGetPersenter persenter;

    public LoginKeyGetModel(ILoginKeyGetPersenter persenter){
        this.persenter = persenter;
    }
    @Override
    public void requestLoginKeyGet() {
        HttpUtils.getRetrofit().loginKeyGet()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<String , String>>() {
                               @Override
                               public void onNext(ResultBean<String , String> result) {
                                   persenter.callBackLoginKeyGet(result);
                               }
                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e),ILoginKeyGetPersenter.TAG);
                               }
                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}
