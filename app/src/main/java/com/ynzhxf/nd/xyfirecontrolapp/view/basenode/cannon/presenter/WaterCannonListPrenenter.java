package com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter;

import com.google.gson.Gson;
import com.ynzhxf.nd.xyfirecontrolapp.BuildConfig;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.TreeGridBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.LabelNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.platform.LoginInfoBean;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.bean.WaterCannonInfo;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.view.IWaterCannonListview;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class WaterCannonListPrenenter {

    IWaterCannonListview mView = null;

    public WaterCannonListPrenenter(IWaterCannonListview view) {
        mView = view;
    }

    public void getLableBySystemId(String Id) {
        Map map = new HashMap();
        map.put("proSysID", Id);
        HttpUtils.getRetrofit(BuildConfig.SML_API_URL).getAppGetEquipmentSimola(RequestBody.create(okhttp3.MediaType.parse("application/json"), new Gson().toJson(map)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<List<WaterCannonInfo>, String>>() {
                               @Override
                               public void onNext(ResultBean<List<WaterCannonInfo>, String> resultBean) {
                                   mView.getWaterCannonListSuccess(resultBean.getData());
                               }

                               @Override
                               public void onError(Throwable e) {

                               }

                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}
