package com.ynzhxf.nd.xyfirecontrolapp.bean.dict;

/**
 * 系统类型
 * Created by nd on 2018-07-15.
 */

public class ProjectSystemTypeDictBean extends BaseDictBean {

    /**
     * 系统样式
     */
    private String AppIconClass;

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    //系统图标
    private String ImageUrl;

    public String getAppIconClass() {
        return AppIconClass;
    }

    public void setAppIconClass(String appIconClass) {
        AppIconClass = appIconClass;
    }

    @Override
    public String toString() {
        return "ProjectSystemTypeDictBean{" +
                "AppIconClass='" + AppIconClass + '\'' +
                ", ImageUrl='" + ImageUrl + '\'' +
                '}';
    }
}
