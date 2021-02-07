package com.ynzhxf.nd.xyfirecontrolapp.bean.common;


/**
 * author hbzhou
 * date 2019/1/30 16:16
 */
public class StatisticsListErrorInfoBean {

    /**
     * BaseNodeID : 7652fad35ccf4a2cbe3bb16786405609
     * ProjectID : f114938a456b447e9152a9b17d7694d5
     * SubconditionName : 状态变化报警
     * AlarmValue : 火警
     * EventTime : /Date(1548831592000)/
     * Message : 8＃楼25层 0号火灾自动报警主机51回路 2号设备感烟探测器 设备火警
     * AreaName : 昆明市-西山区-昆明保利中心-火灾自动报警系统-0号火灾自动报警主机-8＃楼25层
     * Serverity : null
     * Quality : null
     * EventTypeID : 3
     * EventType : null
     * HostID : 1
     * HostNum : 0
     * VC_HostDec : 0号火灾自动报警主机
     * UserID : 051002
     * LoopID : 51
     * PointId : 2
     * pointType : 感烟探测器
     * EventSource : 2
     * isPushCheck : true
     * ReviewState : 0
     * ReviewStateStr : 未操作
     * EventTimeStr : 2019-01-30 14:59:52
     * ID : 1830211d3063412cac268b2c590f9da5
     * IsNew : true
     */

    private String BaseNodeID;
    private String ProjectID;
    private String SubconditionName;
    private String AlarmValue;
    private String EventTime;
    private String Message;
    private String AreaName;
    private Object Serverity;
    private Object Quality;
    private String EventTypeID;
    private EventTypeBean EventType;
    private String HostID;
    private int HostNum;
    private String VC_HostDec;
    private String UserID;
    private int LoopID;
    private int PointId;
    private String pointType;
    private int EventSource;
    private boolean isPushCheck;
    private int ReviewState;
    private String ReviewStateStr;
    private String EventTimeStr;
    private String ID;
    private boolean IsNew;

    public EventTypeBean getEventType() {
        return EventType;
    }

    public void setEventType(EventTypeBean eventType) {
        EventType = eventType;
    }

    public class EventTypeBean{

        /**
         * IsPush : false
         * Name : 恢复事件
         * ID : 5
         * IsNew : true
         */

        private boolean IsPush;
        private String Name;
        private String ID;
        private boolean IsNew;

        public boolean isIsPush() {
            return IsPush;
        }

        public void setIsPush(boolean IsPush) {
            this.IsPush = IsPush;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
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

    private String EventName;

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getBaseNodeID() {
        return BaseNodeID;
    }

    public void setBaseNodeID(String BaseNodeID) {
        this.BaseNodeID = BaseNodeID;
    }

    public String getProjectID() {
        return ProjectID;
    }

    public void setProjectID(String ProjectID) {
        this.ProjectID = ProjectID;
    }

    public String getSubconditionName() {
        return SubconditionName;
    }

    public void setSubconditionName(String SubconditionName) {
        this.SubconditionName = SubconditionName;
    }

    public String getAlarmValue() {
        return AlarmValue;
    }

    public void setAlarmValue(String AlarmValue) {
        this.AlarmValue = AlarmValue;
    }

    public String getEventTime() {
        return EventTime;
    }

    public void setEventTime(String EventTime) {
        this.EventTime = EventTime;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String AreaName) {
        this.AreaName = AreaName;
    }

    public Object getServerity() {
        return Serverity;
    }

    public void setServerity(Object Serverity) {
        this.Serverity = Serverity;
    }

    public Object getQuality() {
        return Quality;
    }

    public void setQuality(Object Quality) {
        this.Quality = Quality;
    }

    public String getEventTypeID() {
        return EventTypeID;
    }

    public void setEventTypeID(String EventTypeID) {
        this.EventTypeID = EventTypeID;
    }

    public String getHostID() {
        return HostID;
    }

    public void setHostID(String HostID) {
        this.HostID = HostID;
    }

    public int getHostNum() {
        return HostNum;
    }

    public void setHostNum(int HostNum) {
        this.HostNum = HostNum;
    }

    public String getVC_HostDec() {
        return VC_HostDec;
    }

    public void setVC_HostDec(String VC_HostDec) {
        this.VC_HostDec = VC_HostDec;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public int getLoopID() {
        return LoopID;
    }

    public void setLoopID(int LoopID) {
        this.LoopID = LoopID;
    }

    public int getPointId() {
        return PointId;
    }

    public void setPointId(int PointId) {
        this.PointId = PointId;
    }

    public String getPointType() {
        return pointType;
    }

    public void setPointType(String pointType) {
        this.pointType = pointType;
    }

    public int getEventSource() {
        return EventSource;
    }

    public void setEventSource(int EventSource) {
        this.EventSource = EventSource;
    }

    public boolean isIsPushCheck() {
        return isPushCheck;
    }

    public void setIsPushCheck(boolean isPushCheck) {
        this.isPushCheck = isPushCheck;
    }

    public int getReviewState() {
        return ReviewState;
    }

    public void setReviewState(int ReviewState) {
        this.ReviewState = ReviewState;
    }

    public String getReviewStateStr() {
        return ReviewStateStr;
    }

    public void setReviewStateStr(String ReviewStateStr) {
        this.ReviewStateStr = ReviewStateStr;
    }

    public String getEventTimeStr() {
        return EventTimeStr;
    }

    public void setEventTimeStr(String EventTimeStr) {
        this.EventTimeStr = EventTimeStr;
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
