package com.ynzhxf.nd.xyfirecontrolapp.model.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.PagingBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.AlarmLogBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IProjectHistoryAlarmPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 获取项目的历史报警记录
 * Created by nd on 2018-07-27.
 */

class ProjectHistoryAlarmModel extends BaseModel implements IProjectHistoryAlarmPersenter.IProjectHistoryAlarmModel {

     private IProjectHistoryAlarmPersenter persenter;

     public ProjectHistoryAlarmModel(IProjectHistoryAlarmPersenter persenter){
         this.persenter = persenter;
     }

    @Override
    public void requestProjectHistoryAlarm(int pageSize, int count, String proID, String startTime, String endTime) {
        HttpUtils.getRetrofit().getProjectHistoryAlarmLogPaging(HelperTool.getToken() ,proID ,count, pageSize ,  startTime , endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<PagingBean<AlarmLogBean>,String>>() {
                               @Override
                               public void onNext(ResultBean<PagingBean<AlarmLogBean>,String> resultBean) {
                                   persenter.callBackProjectInfo(resultBean);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e),IProjectHistoryAlarmPersenter.TAG);
                               }
                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}
