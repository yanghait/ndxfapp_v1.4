package com.ynzhxf.nd.xyfirecontrolapp.model.Platform;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.IUserPwdChangePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 用户密码修改
 * Created by nd on 2018-08-01.
 */

 class UserPwdChangeModel extends BaseModel implements IUserPwdChangePersenter.IUserPwdChangeModel {

    private IUserPwdChangePersenter persenter;

    public UserPwdChangeModel(IUserPwdChangePersenter persenter){
        this.persenter = persenter;
    }
    @Override
    public void requestUserPwdChange(String oldPwd, String newPwd) {
        HttpUtils.getRetrofit().userPwdChange(HelperTool.getToken() , oldPwd ,newPwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<String ,String>>() {
                               @Override
                               public void onNext(ResultBean<String ,String> resultBean) {
                                   persenter.callBackUserPwdChange(resultBean);
                               }
                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e),IUserPwdChangePersenter.TAG);
                               }
                               @Override
                               public void onComplete() {


                               }
                           }
                );
    }
}
