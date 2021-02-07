package com.ynzhxf.nd.xyfirecontrolapp.model.common;

import com.ynzhxf.nd.xyfirecontrolapp.GloblePlantformDatas;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.common.INewsURLPersenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nd on 2018-07-28.
 */

public class NewsURLModel extends BaseModel implements INewsURLPersenter.INewsURLModel {

    private INewsURLPersenter persenter;

    public NewsURLModel(INewsURLPersenter persenter){
        this.persenter = persenter;
    }

    @Override
    public void requestNewsURL(String newsID) {
        HttpUtils.getRetrofit().addNewsReadCountAndURL(GloblePlantformDatas.getInstance().getLoginInfoBean().getToken(),newsID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<String,String>>() {
                               @Override
                               public void onNext(ResultBean<String,String> result) {
                                   persenter.callBackNewsURL(result);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e),INewsURLPersenter.TAG);
                               }
                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}
