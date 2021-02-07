package com.ynzhxf.nd.xyfirecontrolapp.model.count;

import com.ynzhxf.nd.xyfirecontrolapp.GloblePlantformDatas;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.count.ILabelInfoCountPersenter;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 *
 * 标签记录数据统计
 * Created by nd on 2018-07-24.
 */
class LabelInfoCountModel extends BaseModel implements ILabelInfoCountPersenter.ILabelInfoCountModel {

    private ILabelInfoCountPersenter persenter;

    public LabelInfoCountModel(ILabelInfoCountPersenter persenter){
        this.persenter = persenter;
    }

    @Override
    public void requestLabelInfoCountCount(String labelID) {
        HttpUtils.getRetrofit().getLabelInforCount(GloblePlantformDatas.getInstance().getLoginInfoBean().getToken(),labelID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Map<String,String>>() {
                               @Override
                               public void onNext(Map<String,String> result) {
                                   persenter.callBackLabelInfoCount(result);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e),ILabelInfoCountPersenter.TAG);
                               }
                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}
