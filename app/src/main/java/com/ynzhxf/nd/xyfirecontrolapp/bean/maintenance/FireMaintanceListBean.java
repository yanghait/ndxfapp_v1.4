package com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance;


/**
 * author hbzhou
 * date 2019/1/30 18:06
 */
public class FireMaintanceListBean {

    /**
     * ProjectID : f114938a456b447e9152a9b17d7694d5
     * EventTime : /Date(1548820912300)/
     * RealValue : 0
     * TrasentValue : 未接管
     * ProjectLabelTagTypeID : 1
     * EventTypeID : 2
     * ID : 6196fe68989f4980b352f5801257b0bc
     * IsNew : true
     */

    private String ProjectID;
    private String EventTime;
    private String RealValue;
    private String TrasentValue;
    private String ProjectLabelTagTypeID;
    private String EventTypeID;
    private String ID;
    private boolean IsNew;
    private String Message;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getProjectID() {
        return ProjectID;
    }

    public void setProjectID(String ProjectID) {
        this.ProjectID = ProjectID;
    }

    public String getEventTime() {
        return EventTime;
    }

    public void setEventTime(String EventTime) {
        this.EventTime = EventTime;
    }

    public String getRealValue() {
        return RealValue;
    }

    public void setRealValue(String RealValue) {
        this.RealValue = RealValue;
    }

    public String getTrasentValue() {
        return TrasentValue;
    }

    public void setTrasentValue(String TrasentValue) {
        this.TrasentValue = TrasentValue;
    }

    public String getProjectLabelTagTypeID() {
        return ProjectLabelTagTypeID;
    }

    public void setProjectLabelTagTypeID(String ProjectLabelTagTypeID) {
        this.ProjectLabelTagTypeID = ProjectLabelTagTypeID;
    }

    public String getEventTypeID() {
        return EventTypeID;
    }

    public void setEventTypeID(String EventTypeID) {
        this.EventTypeID = EventTypeID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public boolean isIsNew() {
        return IsNew;
    }

    public void setIsNew(boolean IsNew) {
        this.IsNew = IsNew;
    }
}
