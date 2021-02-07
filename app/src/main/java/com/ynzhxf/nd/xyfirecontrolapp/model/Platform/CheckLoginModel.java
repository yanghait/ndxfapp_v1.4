package com.ynzhxf.nd.xyfirecontrolapp.model.Platform;

import com.ynzhxf.nd.xyfirecontrolapp.bean.platform.LoginInfoBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.ICheckLoginPersenter;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 登陆检测网络请求
 * Created by nd on 2018-07-14.
 */

public class CheckLoginModel extends BaseModel implements ICheckLoginPersenter.ICheckLoginModel {

    private ICheckLoginPersenter persenter;

    public CheckLoginModel(ICheckLoginPersenter persenter){
        this.persenter = persenter;
    }

    @Override
    public void requestCheckLogin(LoginInfoBean loginInfo) {
        HttpUtils.getRetrofit().checkLogin(loginInfo.getDeviceUUID(), loginInfo.getDevicePlatform(), loginInfo.getUserName(), loginInfo.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<LoginInfoBean, Map<String, String>>>() {
                               @Override
                               public void onNext(ResultBean<LoginInfoBean, Map<String, String>> loginInfoBeanStringResultBean) {
                                   persenter.callBackCheckLogin(loginInfoBeanStringResultBean);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e),ICheckLoginPersenter.TAG);
                               }
                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}
