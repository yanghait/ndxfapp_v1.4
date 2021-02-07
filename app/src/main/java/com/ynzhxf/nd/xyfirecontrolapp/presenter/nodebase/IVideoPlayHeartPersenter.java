package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBasePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.IBaseView;

/**
 * Created by nd on 2018-07-21.
 */

public interface IVideoPlayHeartPersenter extends IBasePersenter{
    String TAG = "VideoPlayHeart";
    interface IVideoVideoPlayHeartView extends IBaseView {
        /**
         * 回调视频列表处理
         * @param result
         */
        void callBackVideoPlayHeartModel(String result);

    }

    interface IVideoPlayHeartModel{
        /**
         * 发送播放地址获取请求
         * @param channelID 视频通道ID
         */
        void requestVideoPlayHeart(String channelID);
    }

    /**
     * 发送视频播放地址请求
     * @param channelID 通道ID
     */
    void doVideoPlayHeart(String channelID);

    /**
     * 请求完成回调
     * @param result
     */
    void callBackPVideoPlayHeart(String result);
}
