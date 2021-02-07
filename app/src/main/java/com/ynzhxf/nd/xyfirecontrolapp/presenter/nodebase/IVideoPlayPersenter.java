package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

/**
 * 监控视频播放地址获取注册
 * Created by nd on 2018-07-21.
 */

public interface IVideoPlayPersenter extends IBasePersenter{
    String TAG = "VideoPlay";

    interface IVideoPlayView extends IBaseView {
        /**
         * 回调视频列表处理
         * @param result
         */
        void callBackVideoPlay(ResultBean<String,String> result);

    }

    interface IVideoPlayModel{
        /**
         * 发送播放地址获取请求
         * @param channelID 视频通道ID
         */
        void requestVideoPlay(String channelID);
    }

    /**
     * 发送视频播放地址请求
     * @param channelID 通道ID
     */
    void doVideoPlay(String channelID);

    /**
     * 请求完成回调
     * @param result
     */
    void callBackPVideoPlay(ResultBean<String,String> result);
}
