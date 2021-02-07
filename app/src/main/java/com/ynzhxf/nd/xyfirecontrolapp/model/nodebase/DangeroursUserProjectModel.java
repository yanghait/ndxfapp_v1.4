package com.ynzhxf.nd.xyfirecontrolapp.model.nodebase;

import com.google.gson.Gson;
import com.ynzhxf.nd.xyfirecontrolapp.GloblePlantformDatas;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.BaseModel;
import com.ynzhxf.nd.xyfirecontrolapp.network.HttpUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IDangeroursUserProjectPersenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nd on 2018-07-16.
 */

public class DangeroursUserProjectModel extends BaseModel implements IDangeroursUserProjectPersenter.IDangeroursUserProjectModel{

   private IDangeroursUserProjectPersenter persenter;

   public DangeroursUserProjectModel(IDangeroursUserProjectPersenter persenter){
       this.persenter = persenter;
   }

    @Override
    public void requestDangeroursUserProject() {
        HttpUtils.getRetrofit().getDangeroursProjectList(GloblePlantformDatas.getInstance().getLoginInfoBean().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResultBean<List<Object[]>,String>>() {
                               @Override
                               public void onNext(ResultBean<List<Object[]>,String> body) {
                                   ResultBean<List<ProjectNodeBean>,String> result = new ResultBean<List<ProjectNodeBean>,String>();
                                   result.setSuccess(body.isSuccess());
                                   result.setMessage(body.getMessage());
                                   Gson per = new Gson();
                                   try {
                                       List<Object[]> queryDatas = body.getData();
                                       List<ProjectNodeBean>  proList = new ArrayList<>();
                                       for (int i=0;i<queryDatas.size();i++){
                                           Object[] objs = queryDatas.get(i);
                                           ProjectNodeBean queryPro = per.fromJson(per.toJson(objs[0]),ProjectNodeBean.class) ;
                                           queryPro.getExtraData().add(objs[1]+"");
                                           queryPro.getExtraData().add(objs[2]+"");
                                           queryPro.getExtraData().add(objs[3]+"");
                                           queryPro.getExtraData().add(objs[4]+"");
                                           proList.add(queryPro);
                                       }
                                       result.setData(proList);
                                   } catch (Exception e) {
                                       result.setSuccess(false);
                                       result.setMessage("数据转换异常！");
                                   }
                                   persenter.callBackDangeroursUserProject(result);
                               }

                               @Override
                               public void onError(Throwable e) {
                                   persenter.callBackError(createResult(e),IDangeroursUserProjectPersenter.TAG);
                               }
                               @Override
                               public void onComplete() {

                               }
                           }
                );
    }
}
