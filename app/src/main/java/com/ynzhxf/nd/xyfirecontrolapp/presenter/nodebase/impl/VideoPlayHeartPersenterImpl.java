package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.nodebase.NodeBaseModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IVideoPlayHeartPersenter;

/**
 *
 * Created by nd on 2018-07-21.
 */

 class VideoPlayHeartPersenterImpl extends BasePersenter implements IVideoPlayHeartPersenter {

    private IVideoVideoPlayHeartView view;

    private IVideoPlayHeartModel model;

    public VideoPlayHeartPersenterImpl(IVideoVideoPlayHeartView view){
        this.view = view;
        this.model = NodeBaseModelFactory.getVideoPlayHeartModel(this);
    }

    @Override
    public void callBackError(ResultBean<String, String> result, String action) {
        if(view !=null) {
            view.callBackError(result ,action);
        }
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void doVideoPlayHeart(String channelID) {
        model.requestVideoPlayHeart(channelID);
    }

    @Override
    public void callBackPVideoPlayHeart(String result) {
        if(view!=null){
            view.callBackVideoPlayHeartModel(result);
        }
    }
}
