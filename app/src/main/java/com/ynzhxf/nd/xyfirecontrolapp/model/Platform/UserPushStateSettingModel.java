package com.ynzhxf.nd.xyfirecontrolapp.model.Platform;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.IUserPushStateSettingPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 修改用户消息提醒状态
 * Created by nd on 2018-08-01.
 */

class UserPushStateSettingModel extends BaseModel implements IUserPushStateSettingPersenter.IUserPushStateSettingModel {

    private IUserPushStateSettingPersenter persenter;
    public UserPushStateSettingModel(IUserPushStateSettingPersenter persenter){
        this.persenter = persenter;
    }
    @Override
    public void requestUserPushStateSetting() {
        HttpUtils.getRetrofit().getSettingUserPushState(HelperTool.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<Boolean ,String>>() {
                               @Override
                               public void onNext(ResultBean<Boolean ,String> resultBean) {
                                   persenter.callBackUserPushStateSetting(resultBean);
                               }
                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e),IUserPushStateSettingPersenter.TAG);
                               }
                               @Override
                               public void onComplete() {


                               }
                           }
                );
    }
}
