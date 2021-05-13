package com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter;

import com.google.gson.Gson;
import com.ynzhxf.nd.xyfirecontrolapp.BuildConfig;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.platform.LoginInfoBean;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.view.IWaterCannonView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter.SmlSysPresenter.simolaToken;


public class WaterCannonPresenter {

    public final static int CloseSkyLight = 10,  // 关闭天窗
            OpenSkyLight = 11,  // 开启天窗
            CloseWaterValve = 12,  // 关闭水阀
            OpenWaterValve = 13,  // 开启水阀
            ResetWaterCannon = 14, // 复位水炮（复位后才可关闭天窗）
            WaterColumn = 15,  // 切换水柱
            WaterMist = 16,// 切换水雾
            Left = 21, StopLeft = 25,
            Right = 22, StopRight = 26,
            Up = 23, StopUp = 27,
            Down = 24, StopDown = 28;


    IWaterCannonView mView;

    public WaterCannonPresenter(IWaterCannonView view) {
        mView = view;
    }

    public void setWaterCannonCommend(List<String> equipList, int operationType) {
        Map map = new HashMap();
        map.put("EquipmentIdList", equipList);
        map.put("operationType", operationType);
        map.put("Token", simolaToken);
        HttpUtils.getRetrofit(BuildConfig.SML_API_URL).setWaterCannonCmd(RequestBody.create(okhttp3.MediaType.parse("application/json"), new Gson().toJson(map)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean>() {
                               @Override
                               public void onNext(ResultBean resultBean) {
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

    public void getEquipmentStaus(String equipId) {
        Map map = new HashMap();
        map.put("EquipmentId", equipId);
        HttpUtils.getRetrofit(BuildConfig.SML_API_URL).getWaterCannonData(RequestBody.create(okhttp3.MediaType.parse("application/json"), new Gson().toJson(map)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean>() {
                               @Override
                               public void onNext(ResultBean resultBean) {
                                   mView.getWaterCannonDateSuccess();
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
