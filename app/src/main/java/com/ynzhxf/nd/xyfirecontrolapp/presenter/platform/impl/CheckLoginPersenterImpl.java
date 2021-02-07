package com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.platform.LoginInfoBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.Platform.PlatformModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.platform.ICheckLoginPersenter;

import java.util.Map;

/**
 * 登陆检测
 * Created by nd on 2018-07-14.
 */

 class CheckLoginPersenterImpl extends BasePersenter implements ICheckLoginPersenter{

    private ICheckLoginPersenter.ICheckLoginView view;
    private ICheckLoginPersenter.ICheckLoginModel model;

    public CheckLoginPersenterImpl(ICheckLoginPersenter.ICheckLoginView view){
       this.view = view;
       model = PlatformModelFactory.getCheckedModel(this);
    }

    @Override
    public void callBackError(ResultBean<String, String> result,String action) {
        if(view!=null){//检测是否还持有View的引用
            view.callBackError(result,action);
        }

    }

    @Override
    public void detachView() {
       this.view = null;
    }

   @Override
   public void doChecklogin(LoginInfoBean bean) {
        model.requestCheckLogin(bean);
   }

   @Override
   public void callBackCheckLogin(ResultBean<LoginInfoBean, Map<String, String>> result) {
        if(view!=null){//检测是否还持有View的引用
            view.callBackCheckLogin(result);
        }
   }
}
