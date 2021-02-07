package com.ynzhxf.nd.xyfirecontrolapp.bean.assessment;


/**
 * author hbzhou
 * date 2019/4/29 10:06
 * 风险评估建筑物列表bean
 */
public class BuildingListEntityBean {
    private String ProjectRiskLogId;
    private String ProjectId;

    private String BuildId;
    private String BuildName;
    private double R_Max;
    private double R;
    private double R0;
    private double R1;
    private double R2;
    private double R3;

    private String FloorRiskJson;
    private String CreateTime;

    public String getProjectRiskLogId() {
        return ProjectRiskLogId;
    }

    public void setProjectRiskLogId(String projectRiskLogId) {
        ProjectRiskLogId = projectRiskLogId;
    }

    public String getProjectId() {
        return ProjectId;
    }

    public void setProjectId(String projectId) {
        ProjectId = projectId;
    }

    public String getBuildId() {
        return BuildId;
    }

    public void setBuildId(String buildId) {
        BuildId = buildId;
    }

    public String getBuildName() {
        return BuildName;
    }

    public void setBuildName(String buildName) {
        BuildName = buildName;
    }

    public double getR_Max() {
        return R_Max;
    }

    public void setR_Max(double r_Max) {
        R_Max = r_Max;
    }

    public double getR() {
        return R;
    }

    public void setR(double r) {
        R = r;
    }

    public double getR0() {
        return R0;
    }

    public void setR0(double r0) {
        R0 = r0;
    }

    public double getR1() {
        return R1;
    }

    public void setR1(double r1) {
        R1 = r1;
    }

    public double getR2() {
        return R2;
    }

    public void setR2(double r2) {
        R2 = r2;
    }

    public double getR3() {
        return R3;
    }

    public void setR3(double r3) {
        R3 = r3;
    }

    public String getFloorRiskJson() {
        return FloorRiskJson;
    }

    public void setFloorRiskJson(String floorRiskJson) {
        FloorRiskJson = floorRiskJson;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }
}
