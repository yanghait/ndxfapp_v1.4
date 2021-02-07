package com.ynzhxf.nd.xyfirecontrolapp.model.count;


import com.ynzhxf.nd.xyfirecontrolapp.GloblePlantformDatas;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.count.IUserMaxEventCountPersenter;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 等钱登陆用户最大权限项目列表的事件统计和实时报警数量统计
 * Created by nd on 2018-07-16.
 */

class UserMaxEventCountModel extends BaseModel implements IUserMaxEventCountPersenter.IUserMaxEventCountModel{
    private IUserMaxEventCountPersenter persenter;
    public  UserMaxEventCountModel(IUserMaxEventCountPersenter persenter) {
        this.persenter = persenter;
    }

    @Override
    public void requestUserMaxEventCount() {
        HttpUtils.getRetrofit().getUserMaxEventCount(GloblePlantformDatas.getInstance().getLoginInfoBean().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Map<String,String>>() {
                               @Override
                               public void onNext(Map<String,String> result) {
                                   persenter.callBackUserMaxEventCount(result);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e), IUserMaxEventCountPersenter.TAG);
                               }
                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }

}
