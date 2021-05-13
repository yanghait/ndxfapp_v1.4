package com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.presenter;

import com.google.gson.Gson;
import com.ynzhxf.nd.xyfirecontrolapp.BuildConfig;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.platform.LoginInfoBean;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.bean.SmlSystemInfo;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.view.ISmlSysListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class SmlSysPresenter {

    ISmlSysListView mView;

    public static String simolaToken = "";

    public SmlSysPresenter(ISmlSysListView view) {
        this.mView = view;
    }

    public void loginToSml() {
        Map map = new HashMap();
        map.put("UserName", "simola");
        map.put("UserPwd", "123456");
        HttpUtils.getRetrofit(BuildConfig.SML_API_URL).loginToSimola(RequestBody.create(okhttp3.MediaType.parse("application/json"), new Gson().toJson(map)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<LoginInfoBean, Map<String, String>>>() {
                               @Override
                               public void onNext(ResultBean<LoginInfoBean, Map<String, String>> loginInfoBeanStringResultBean) {
                                   simolaToken = loginInfoBeanStringResultBean.getData().getToken();
                                   mView.loginSuccess();
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

    public void getSmlSysList() {
        Map map = new HashMap();
        map.put("projectID", "31647e210b3f4a51b3d5a0529981e688");
        HttpUtils.getRetrofit(BuildConfig.SML_API_URL).getSmlSys(RequestBody.create(okhttp3.MediaType.parse("application/json"), new Gson().toJson(map)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<List<SmlSystemInfo>, String>>() {
                               @Override
                               public void onNext(ResultBean<List<SmlSystemInfo>, String> resultBean) {
                                   mView.getSysListSuccess(resultBean.getData());
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
