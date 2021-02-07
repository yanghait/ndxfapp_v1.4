package com.ynzhxf.nd.xyfirecontrolapp.model.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.ILabelWriteValuePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 标签写值
 * Created by nd on 2018-07-24.
 */

class LabelWriteValueModel extends BaseModel implements ILabelWriteValuePersenter.ILabelWriteValueModel {

    private ILabelWriteValuePersenter persenter;

    public LabelWriteValueModel(ILabelWriteValuePersenter persenter){
        this.persenter = persenter;
    }


    @Override
    public void requestLabelWriteValue(String labelID, String confirmPwd) {
        HttpUtils.getRetrofit().settingLabelWriteValue(HelperTool.getToken() , labelID , confirmPwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<String,String>>() {
                               @Override
                               public void onNext(ResultBean<String,String> resultBean) {
                                   persenter.callBackLabelWriteValue(resultBean);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e),ILabelWriteValuePersenter.TAG);
                               }
                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}
