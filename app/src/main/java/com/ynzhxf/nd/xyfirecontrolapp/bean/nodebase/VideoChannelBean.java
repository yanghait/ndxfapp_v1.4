package com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.BaseDataBean;

/**
 * 流媒体通道对象
 * Created by nd on 2018-07-21.
 */

public class VideoChannelBean extends BaseDataBean {
    //项目ID
    private String ProjectID;
    //摄像机名称
    private String CameraName;

    //摄像机图片地址
    private String ImageUrl;

    //播放地址
    public String PlayAddress;

    public String getCameraName() {
        return CameraName;
    }

    public void setCameraName(String cameraName) {
        CameraName = cameraName;
    }

    public String getProjectID() {
        return ProjectID;
    }

    public void setProjectID(String projectID) {
        ProjectID = projectID;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public String getPlayAddress() {
        return PlayAddress;
    }

    public void setPlayAddress(String playAddress) {
        PlayAddress = playAddress;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
