package com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter;

import com.google.gson.Gson;
import com.ynzhxf.nd.xyfirecontrolapp.BuildConfig;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.bean.SmartPowerInfo;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.bean.WaterCannonInfo;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.view.ISmartPowerListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter.SmlSysPresenter.simolaToken;

public class SmartPowerListPresenter {
    ISmartPowerListView mView;

    public SmartPowerListPresenter(ISmartPowerListView view) {
        this.mView = view;
    }

    public void getLableBySystemId(String Id) {
        HttpUtils.getRetrofit(BuildConfig.SML_API_URL).getSmartPowerList(simolaToken, Id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<List<SmartPowerInfo>, String>>() {
                               @Override
                               public void onNext(ResultBean<List<SmartPowerInfo>, String> resultBean) {
                                   mView.getSmartPowerListSuccess(resultBean.getData());
                               }

                               @Override
                               public void onError(Throwable e) {
                                   System.out.println("1");
                               }

                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }

    public void writeLablevalue(String lableId) {
        HttpUtils.getRetrofit(BuildConfig.SML_API_URL).setSmartPowerStauts(lableId, "12345678", simolaToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean>() {
                               @Override
                               public void onNext(ResultBean resultBean) {
                                   mView.writeLableSuccess();
                               }

                               @Override
                               public void onError(Throwable e) {
                                   System.out.println("1");
                               }

                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }

}
