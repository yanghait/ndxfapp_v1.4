package com.ynzhxf.nd.xyfirecontrolapp.model.Platform;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.IUserPushStatePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 获取用户推送状态
 * Created by nd on 2018-08-01.
 */

class UserPushStateModel extends BaseModel implements IUserPushStatePersenter.IUserPushStateModel{
    private IUserPushStatePersenter persenter;

    public UserPushStateModel(IUserPushStatePersenter persenter){
        this.persenter = persenter;
    }

    @Override
    public void requestUserPushState() {
        HttpUtils.getRetrofit().getUserPushState(HelperTool.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<Boolean ,String>>() {
                               @Override
                               public void onNext(ResultBean<Boolean ,String> resultBean) {
                                   persenter.callBackUserPushState(resultBean);
                               }
                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e),IUserPushStatePersenter.TAG);
                               }
                               @Override
                               public void onComplete() {


                               }
                           }
                );
    }
}
