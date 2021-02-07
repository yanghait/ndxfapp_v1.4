package com.ynzhxf.nd.xyfirecontrolapp.model.Platform;

import com.ynzhxf.nd.xyfirecontrolapp.bean.platform.LoginInfoBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.ILoginPersenter;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 用户登陆网络请求
 * Created by nd on 2018-07-13.
 */

 class LoginModel extends BaseModel implements ILoginPersenter.ILoginModel {
    private ILoginPersenter persenter;

    public LoginModel(ILoginPersenter persenter){
        this.persenter = persenter;
    }
    @Override
    public void requestLogin(LoginInfoBean loginInfo) {
        HttpUtils.getRetrofit().login(loginInfo.getDeviceUUID(), loginInfo.getDevicePlatform(), loginInfo.getUserName(), loginInfo.getUserPwd(),loginInfo.getKey() , loginInfo.getCode())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<LoginInfoBean, Map<String, String>>>() {
                               @Override
                               public void onNext(ResultBean<LoginInfoBean, Map<String, String>> loginInfoBeanStringResultBean) {
                                   persenter.callBackLogin(loginInfoBeanStringResultBean);
                               }
                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e),ILoginPersenter.TAG);
                               }
                               @Override
                               public void onComplete() {


                               }
                           }
                );
    }
}
