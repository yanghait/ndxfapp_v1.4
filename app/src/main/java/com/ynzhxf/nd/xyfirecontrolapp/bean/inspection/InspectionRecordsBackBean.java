package com.ynzhxf.nd.xyfirecontrolapp.bean.inspection;

public class InspectionRecordsBackBean {

    /**
     * ID : e6f1a38093a74045b26d836b0add684a
     * InspectItemName : 灭火器E
     * InspectAreaName : 区域B
     * InspectorName : test132
     * InspectResultValue : 1
     * Remark :
     * UploadUrls :
     * CreateTimeShow : 2018/12/26 15:03:38
     *
     */

    private String ID;
    private String InspectItemName;
    private String InspectAreaName;
    private String InspectorName;
    private int InspectResultValue;
    private String Remark;
    private String UploadUrls;
    private String CreateTimeShow;
    private String SystemTypeName;

    public String getSystemItemName() {
        return SystemTypeName;
    }

    public void setSystemItemName(String systemItemName) {
        SystemTypeName = systemItemName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getInspectItemName() {
        return InspectItemName;
    }

    public void setInspectItemName(String InspectItemName) {
        this.InspectItemName = InspectItemName;
    }

    public String getInspectAreaName() {
        return InspectAreaName;
    }

    public void setInspectAreaName(String InspectAreaName) {
        this.InspectAreaName = InspectAreaName;
    }

    public String getInspectorName() {
        return InspectorName;
    }

    public void setInspectorName(String InspectorName) {
        this.InspectorName = InspectorName;
    }

    public int getInspectResultValue() {
        return InspectResultValue;
    }

    public void setInspectResultValue(int InspectResultValue) {
        this.InspectResultValue = InspectResultValue;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public String getUploadUrls() {
        return UploadUrls;
    }

    public void setUploadUrls(String UploadUrls) {
        this.UploadUrls = UploadUrls;
    }

    public String getCreateTimeShow() {
        return CreateTimeShow;
    }

    public void setCreateTimeShow(String CreateTimeShow) {
        this.CreateTimeShow = CreateTimeShow;
    }
}
