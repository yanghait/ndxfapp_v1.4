package com.ynzhxf.nd.xyfirecontrolapp.model.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IProjectSettingRepaireOrTokenPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 设置消防接管和维保状态
 * Created by nd on 2018-07-19.
 */

class ProjectSettingRepaireOrTokenModel  extends BaseModel implements IProjectSettingRepaireOrTokenPersenter.IProjectSettingRepaireOrTokenModel{

    private IProjectSettingRepaireOrTokenPersenter persenter;

    public  ProjectSettingRepaireOrTokenModel(IProjectSettingRepaireOrTokenPersenter persenter){
        this.persenter = persenter;
    }

    @Override
    public void requestProjectSettingRepaireOrToken(String proID, String confirmPwd, String type) {
        HttpUtils.getRetrofit().settingRepaireAndFireToken(HelperTool.getToken(),proID,confirmPwd,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<ProjectNodeBean,String>>() {
                               @Override
                               public void onNext(ResultBean<ProjectNodeBean,String> resultBean) {
                                   persenter.callBackProjectSettingRepaireOrToken(resultBean);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e),IProjectSettingRepaireOrTokenPersenter.TAG);
                               }
                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}
