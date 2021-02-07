package com.ynzhxf.nd.xyfirecontrolapp.model.nodebase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.LabelNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IProjectRealAlarmPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 获取项目实时报警数据
 * Created by nd on 2018-07-26.
 */

class ProjectRealAlarmModel extends BaseModel implements IProjectRealAlarmPersenter.IProjectRealAlarmModel {
    private IProjectRealAlarmPersenter persenter;

    public ProjectRealAlarmModel(IProjectRealAlarmPersenter persenter){
        this.persenter = persenter;
    }
    @Override
    public void requestProjectRealAlarmList(String proID) {

        HttpUtils.getRetrofit().getProjectRealAlarmList(HelperTool.getToken() , proID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<Map<String,Object>,String>>() {
                               @Override
                               public void onNext(ResultBean<Map<String,Object>,String> resultBean) {
                                   ResultBean<List<LabelNodeBean>,String> result = new ResultBean<>();
                                   result.setSuccess(resultBean.isSuccess());
                                   result.setMessage(resultBean.getMessage());
                                   if(resultBean.isSuccess() == true){
                                       Gson json = new Gson();
                                       String temps = json.toJson(resultBean.getData().get("lableList"));
                                       List<LabelNodeBean> queryList = json.fromJson(temps, new TypeToken<List<LabelNodeBean>>(){}.getType());
                                       result.setData(queryList);
                                   }
                                   persenter.callBackProjectRealAlarm(result);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e),IProjectRealAlarmPersenter.TAG);
                               }
                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}
