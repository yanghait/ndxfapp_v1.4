package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.model.nodebase.NodeBaseModelFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.BasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IVideoPlayPersenter;

/**
 * Created by nd on 2018-07-21.
 */

class VideoPlayPersenterImpl extends BasePersenter implements IVideoPlayPersenter {

    private IVideoPlayView view;
    private IVideoPlayModel model;

    public VideoPlayPersenterImpl(IVideoPlayView view){
        this.view = view;
        this.model = NodeBaseModelFactory.getVideoPlayModel(this);
    }

    @Override
    public void callBackError(ResultBean<String, String> result, String action) {
        if(view != null){
            view.callBackError(result , action);
        }
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void doVideoPlay(String channelID) {
        model.requestVideoPlay(channelID);
    }

    @Override
    public void callBackPVideoPlay(ResultBean<String, String> result) {
        if(view != null){
            view.callBackVideoPlay(result);
        }
    }
}
