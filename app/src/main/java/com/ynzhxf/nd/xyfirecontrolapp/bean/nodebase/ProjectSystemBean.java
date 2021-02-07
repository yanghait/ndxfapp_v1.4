package com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase;

import com.ynzhxf.nd.xyfirecontrolapp.bean.dict.BaseDictBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.dict.ProjectSystemTypeDictBean;

/**
 * 系统实体
 * Created by nd on 2018-07-15.
 */

public class ProjectSystemBean extends  BaseNodeBean{

    /**
     * 系统类型
     */
    private ProjectSystemTypeDictBean ProjectSysType;

    private String ProjectSystemTypeID;

    public String getProjectSystemTypeID() {
        return ProjectSystemTypeID;
    }

    public void setProjectSystemTypeID(String projectSystemTypeID) {
        ProjectSystemTypeID = projectSystemTypeID;
    }

    /**
     * 系统别名
     */
    private String AnotherName;

    /**
     * 系统分类类型
     */
    private BaseDictBean ProSysClsType;

    public ProjectSystemTypeDictBean getProjectSysType() {
        return ProjectSysType;
    }

    public void setProjectSysType(ProjectSystemTypeDictBean projectSysType) {
        ProjectSysType = projectSysType;
    }

    public String getAnotherName() {
        return AnotherName;
    }

    public void setAnotherName(String anotherName) {
        AnotherName = anotherName;
    }

    public BaseDictBean getProSysClsType() {
        return ProSysClsType;
    }

    public void setProSysClsType(BaseDictBean proSysClsType) {
        ProSysClsType = proSysClsType;
    }

    @Override
    public String toString() {
        return "ProjectSystemBean{" +
                "ProjectSysType=" + ProjectSysType +
                ", AnotherName='" + AnotherName + '\'' +
                ", ProSysClsType=" + ProSysClsType +
                '}';
    }
}
