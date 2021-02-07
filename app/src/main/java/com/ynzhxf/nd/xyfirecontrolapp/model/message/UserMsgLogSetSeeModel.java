package com.ynzhxf.nd.xyfirecontrolapp.model.message;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.IUserMsgLogSetSeePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nd on 2018-08-02.
 */

class UserMsgLogSetSeeModel extends BaseModel implements IUserMsgLogSetSeePersenter.IUserMsgLogSetSeeModel {
    private IUserMsgLogSetSeePersenter persenter;
    public UserMsgLogSetSeeModel(IUserMsgLogSetSeePersenter persenter){
        this.persenter = persenter;
    }
    @Override
    public void requestUserMsgLogSetSee(String msgLogID) {
        HttpUtils.getRetrofit().settingUserMsgLogSetSee(HelperTool.getToken() ,msgLogID )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<Boolean,String>>() {
                               @Override
                               public void onNext(ResultBean<Boolean ,String>  resultBean) {
                                   persenter.callBackUserMsgLogSetSee(resultBean);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e),IUserMsgLogSetSeePersenter.TAG);
                               }
                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}
