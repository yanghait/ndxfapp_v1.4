package com.ynzhxf.nd.xyfirecontrolapp.model.message;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.IUserMsgLogNotSeeCountPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 获取用户还未查看的消息数量
 * Created by nd on 2018-08-02.
 */

 class UserMsgLogNotSeeCountModel extends BaseModel implements IUserMsgLogNotSeeCountPersenter.IUserMsgLogNotSeeCountModel{

    private IUserMsgLogNotSeeCountPersenter persenter;
    public UserMsgLogNotSeeCountModel(IUserMsgLogNotSeeCountPersenter persenter){
        this.persenter = persenter;
    }
    @Override
    public void requestUserMsgLogNotSeeCount() {
        HttpUtils.getRetrofit().getUserMsgLogNotSeeCount(HelperTool.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<Integer,String>>() {
                               @Override
                               public void onNext(ResultBean<Integer ,String>  resultBean) {
                                   persenter.callBackUserMsgLogNotSeeCount(resultBean);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e),IUserMsgLogNotSeeCountPersenter.TAG);
                               }
                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}
