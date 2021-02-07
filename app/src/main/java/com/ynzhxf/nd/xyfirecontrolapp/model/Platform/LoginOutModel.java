package com.ynzhxf.nd.xyfirecontrolapp.model.Platform;

import com.ynzhxf.nd.xyfirecontrolapp.GloblePlantformDatas;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.ILoginOutPersenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nd on 2018-07-15.
 */

public class LoginOutModel extends BaseModel implements ILoginOutPersenter.ILoginOutModel {

    private ILoginOutPersenter persenter;


    public LoginOutModel(ILoginOutPersenter persenter){

        this.persenter = persenter;
    }
    @Override
    public void requestLoginOut() {

        HttpUtils.getRetrofit().loginOut(GloblePlantformDatas.getInstance().getLoginInfoBean().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<String,String>>() {
                               @Override
                               public void onNext(ResultBean<String,String> bean) {
                                   persenter.callBackLoginOut(bean);
                               }
                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e),ILoginOutPersenter.TAG);
                               }
                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}
