package com.ynzhxf.nd.xyfirecontrolapp.model.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IVideoPlayHeartPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by nd on 2018-07-21.
 */

public class VideoPlayHeartModel extends BaseModel implements IVideoPlayHeartPersenter.IVideoPlayHeartModel {

    private IVideoPlayHeartPersenter persenter;

    public VideoPlayHeartModel(IVideoPlayHeartPersenter persenter){
        this.persenter = persenter;
    }


    @Override
    public void requestVideoPlayHeart(String channelID) {
        HttpUtils.getRetrofit().sendVideoPlayheart(HelperTool.getToken(),channelID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResponseBody>() {
                               @Override
                               public void onNext(ResponseBody result) {
                                   //persenter(result); 这里就不返回了
                               }

                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e),IVideoPlayHeartPersenter.TAG);
                               }
                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}
