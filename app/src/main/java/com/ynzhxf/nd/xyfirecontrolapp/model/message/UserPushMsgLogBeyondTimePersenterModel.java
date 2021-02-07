package com.ynzhxf.nd.xyfirecontrolapp.model.message;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.message.UserMessagePushLogBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.IUserPushMsgLogBeyondTimePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 获取APP用户超过指定时间的消息（只获取最近1天的消息）
 * 该接口用于动态更新消息列表
 * Created by nd on 2018-08-02.
 */

class UserPushMsgLogBeyondTimePersenterModel extends BaseModel implements IUserPushMsgLogBeyondTimePersenter.IUserPushMsgLogBeyondTimeModel {

    private IUserPushMsgLogBeyondTimePersenter persenter;

    public UserPushMsgLogBeyondTimePersenterModel(IUserPushMsgLogBeyondTimePersenter persenter) {
        this.persenter = persenter;
    }

    @Override
    public void requestUserPushMsgLog(String pushLogID, String typeId) {
        HttpUtils.getRetrofit().getUserMsgLogBeyondTime(HelperTool.getToken(), pushLogID, typeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<List<UserMessagePushLogBean>, String>>() {
                               @Override
                               public void onNext(ResultBean<List<UserMessagePushLogBean>, String> resultBean) {
                                   persenter.callBackUserPushMsgLogBeyondTime(resultBean);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e), IUserPushMsgLogBeyondTimePersenter.TAG);
                               }

                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}
