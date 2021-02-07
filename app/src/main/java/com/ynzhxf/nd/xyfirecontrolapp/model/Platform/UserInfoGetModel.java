package com.ynzhxf.nd.xyfirecontrolapp.model.Platform;

import com.ynzhxf.nd.xyfirecontrolapp.GloblePlantformDatas;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.platform.LoginInfoBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.IUserInfoGetPersenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 获取用户相关信息
 * Created by nd on 2018-07-31.
 */

class UserInfoGetModel extends BaseModel implements IUserInfoGetPersenter.IUserInfoGetModel {

    private IUserInfoGetPersenter persenter;

    public UserInfoGetModel(IUserInfoGetPersenter persenter){
        this.persenter = persenter;
    }

    @Override
    public void requestUserInfoGet() {
        HttpUtils.getRetrofit().userInfoGet(GloblePlantformDatas.getInstance().getLoginInfoBean().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<LoginInfoBean,String>>() {
                               @Override
                               public void onNext(ResultBean<LoginInfoBean,String> bean) {
                                   persenter.callBackUserInfoGet(bean);
                               }
                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e),IUserInfoGetPersenter.TAG);
                               }
                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}
