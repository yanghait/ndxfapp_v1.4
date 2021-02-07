package com.ynzhxf.nd.xyfirecontrolapp.bean.common;


/**
 * author hbzhou
 * date 2019/9/17 16:27
 */
public class OverviewItemBean {
    private String allContent;
    private String projectName;
    private Integer[] hasModule;
    private String projectId;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getAllContent() {
        return allContent;
    }

    public void setAllContent(String allContent) {
        this.allContent = allContent;
    }

    public Integer[] getHasModule() {
        return hasModule;
    }

    public void setHasModule(Integer[] hasModule) {
        this.hasModule = hasModule;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
