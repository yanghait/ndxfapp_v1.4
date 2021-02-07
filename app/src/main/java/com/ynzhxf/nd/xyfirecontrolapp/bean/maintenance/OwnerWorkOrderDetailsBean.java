package com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance;

import java.util.List;

public class OwnerWorkOrderDetailsBean {

    public ModWorkOrder modWorkOrder;

    public List<IsWorkOrderLog> lsWorkOrderEventLog;

    public static class IsWorkOrderLog{

        /**
         * WorkOrder_Id : ea255cddc7d84142a2b4308862beea83
         * WorkOrderState : 10
         * EventUserName : 赵永志
         * EventTel : 暂无数据
         * EventUserCompany : 云南捷诺科技有限公司
         * EventDateTime : /Date(1543300452240)/
         * EventDateTimeShow : 2018/11/27 14:34:12
         * EventTag : 待确认
         * EventContent : 暂无
         * EventRemark :
         * IsDelete : false
         * StateIconOnUrl : /Uploads/Images/Icon/o_1cn5n93i0g4r1kuo1iko47oijg7.png
         * StateIconOffUrl : /Uploads/Images/Icon/o_1cn5n9d95h9gq83an01jpfnpic.png
         * ImagePath :
         */

        private String WorkOrder_Id;
        private int WorkOrderState;
        private String EventUserName;
        private String EventTel;
        private String EventUserCompany;
        private String EventDateTime;
        private String EventDateTimeShow;
        private String EventTag;
        private String EventContent;
        private String EventRemark;
        private boolean IsDelete;
        private String StateIconOnUrl;
        private String StateIconOffUrl;
        private String ImagePath;

        public String getWorkOrder_Id() {
            return WorkOrder_Id;
        }

        public void setWorkOrder_Id(String WorkOrder_Id) {
            this.WorkOrder_Id = WorkOrder_Id;
        }

        public int getWorkOrderState() {
            return WorkOrderState;
        }

        public void setWorkOrderState(int WorkOrderState) {
            this.WorkOrderState = WorkOrderState;
        }

        public String getEventUserName() {
            return EventUserName;
        }

        public void setEventUserName(String EventUserName) {
            this.EventUserName = EventUserName;
        }

        public String getEventTel() {
            return EventTel;
        }

        public void setEventTel(String EventTel) {
            this.EventTel = EventTel;
        }

        public String getEventUserCompany() {
            return EventUserCompany;
        }

        public void setEventUserCompany(String EventUserCompany) {
            this.EventUserCompany = EventUserCompany;
        }

        public String getEventDateTime() {
            return EventDateTime;
        }

        public void setEventDateTime(String EventDateTime) {
            this.EventDateTime = EventDateTime;
        }

        public String getEventDateTimeShow() {
            return EventDateTimeShow;
        }

        public void setEventDateTimeShow(String EventDateTimeShow) {
            this.EventDateTimeShow = EventDateTimeShow;
        }

        public String getEventTag() {
            return EventTag;
        }

        public void setEventTag(String EventTag) {
            this.EventTag = EventTag;
        }

        public String getEventContent() {
            return EventContent;
        }

        public void setEventContent(String EventContent) {
            this.EventContent = EventContent;
        }

        public String getEventRemark() {
            return EventRemark;
        }

        public void setEventRemark(String EventRemark) {
            this.EventRemark = EventRemark;
        }

        public boolean isIsDelete() {
            return IsDelete;
        }

        public void setIsDelete(boolean IsDelete) {
            this.IsDelete = IsDelete;
        }

        public String getStateIconOnUrl() {
            return StateIconOnUrl;
        }

        public void setStateIconOnUrl(String StateIconOnUrl) {
            this.StateIconOnUrl = StateIconOnUrl;
        }

        public String getStateIconOffUrl() {
            return StateIconOffUrl;
        }

        public void setStateIconOffUrl(String StateIconOffUrl) {
            this.StateIconOffUrl = StateIconOffUrl;
        }

        public String getImagePath() {
            return ImagePath;
        }

        public void setImagePath(String ImagePath) {
            this.ImagePath = ImagePath;
        }
    }

    public static class  ModWorkOrder{

        /**
         * ID : ea255cddc7d84142a2b4308862beea83
         * ProjectId : 94b6970a772448cea351e438d4e9487e
         * SystemId : b60e85b5185247cc8438157e65cfd069
         * Code : 20181127143412450133
         * StartUserName : 赵永志
         * StartUserTel : 222
         * ProjectSystemAuthorized_Id : c89efef0f71d447d8c84cfdf76616668
         * SpFaultType_Id : null
         * FaultPlace : null
         * SpRepairMethod_Id : null
         * RepairContent : null
         * OtherContent : null
         * State : 0
         * IsShowPosition : false
         * EndTypeShow : -
         * EndReason : null
         * EndTimeShow : -
         * EvaluateComment : null
         * EvaluateLevel : 0
         * EstimateEndTime : null
         * EstimateEndTimeShow : -
         * IsReminder : false
         * IsShowPositionShow : null
         * ProjectNameShow : 云南捷诺科技
         * ProjectSystemNameShow : 消防给水系统
         * StartTimeShow : 2018/11/27 14:34:12
         * MaintenancePersonConfirmTimeShow : -
         * AcceptUserInfoShow : 暂无数据
         * AcceptUserInfoTel : -
         * StateShow : 待确认
         * IsReminderShow : 未催办
         * FaultTypes : null
         * RepairMethod : null
         * StateIconUrl : null
         */

        private String ID;
        private String ProjectId;
        private String SystemId;
        private String Code;
        private String StartUserName;
        private String StartUserTel;
        private String ProjectSystemAuthorized_Id;
        private Object SpFaultType_Id;
        private Object FaultPlace;
        private Object SpRepairMethod_Id;
        private Object RepairContent;
        private Object OtherContent;
        private int State;
        private boolean IsShowPosition;
        private String EndTypeShow;
        private Object EndReason;
        private String EndTimeShow;
        private Object EvaluateComment;
        private int EvaluateLevel;
        private Object EstimateEndTime;
        private String EstimateEndTimeShow;
        private boolean IsReminder;
        private Object IsShowPositionShow;
        private String ProjectNameShow;
        private String ProjectSystemNameShow;
        private String StartTimeShow;
        private String MaintenancePersonConfirmTimeShow;
        private String AcceptUserInfoShow;
        private String AcceptUserInfoTel;
        private String StateShow;
        private String IsReminderShow;
        private Object FaultTypes;
        private Object RepairMethod;
        private Object StateIconUrl;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getProjectId() {
            return ProjectId;
        }

        public void setProjectId(String ProjectId) {
            this.ProjectId = ProjectId;
        }

        public String getSystemId() {
            return SystemId;
        }

        public void setSystemId(String SystemId) {
            this.SystemId = SystemId;
        }

        public String getCode() {
            return Code;
        }

        public void setCode(String Code) {
            this.Code = Code;
        }

        public String getStartUserName() {
            return StartUserName;
        }

        public void setStartUserName(String StartUserName) {
            this.StartUserName = StartUserName;
        }

        public String getStartUserTel() {
            return StartUserTel;
        }

        public void setStartUserTel(String StartUserTel) {
            this.StartUserTel = StartUserTel;
        }

        public String getProjectSystemAuthorized_Id() {
            return ProjectSystemAuthorized_Id;
        }

        public void setProjectSystemAuthorized_Id(String ProjectSystemAuthorized_Id) {
            this.ProjectSystemAuthorized_Id = ProjectSystemAuthorized_Id;
        }

        public Object getSpFaultType_Id() {
            return SpFaultType_Id;
        }

        public void setSpFaultType_Id(Object SpFaultType_Id) {
            this.SpFaultType_Id = SpFaultType_Id;
        }

        public Object getFaultPlace() {
            return FaultPlace;
        }

        public void setFaultPlace(Object FaultPlace) {
            this.FaultPlace = FaultPlace;
        }

        public Object getSpRepairMethod_Id() {
            return SpRepairMethod_Id;
        }

        public void setSpRepairMethod_Id(Object SpRepairMethod_Id) {
            this.SpRepairMethod_Id = SpRepairMethod_Id;
        }

        public Object getRepairContent() {
            return RepairContent;
        }

        public void setRepairContent(Object RepairContent) {
            this.RepairContent = RepairContent;
        }

        public Object getOtherContent() {
            return OtherContent;
        }

        public void setOtherContent(Object OtherContent) {
            this.OtherContent = OtherContent;
        }

        public int getState() {
            return State;
        }

        public void setState(int State) {
            this.State = State;
        }

        public boolean isIsShowPosition() {
            return IsShowPosition;
        }

        public void setIsShowPosition(boolean IsShowPosition) {
            this.IsShowPosition = IsShowPosition;
        }

        public String getEndTypeShow() {
            return EndTypeShow;
        }

        public void setEndTypeShow(String EndTypeShow) {
            this.EndTypeShow = EndTypeShow;
        }

        public Object getEndReason() {
            return EndReason;
        }

        public void setEndReason(Object EndReason) {
            this.EndReason = EndReason;
        }

        public String getEndTimeShow() {
            return EndTimeShow;
        }

        public void setEndTimeShow(String EndTimeShow) {
            this.EndTimeShow = EndTimeShow;
        }

        public Object getEvaluateComment() {
            return EvaluateComment;
        }

        public void setEvaluateComment(Object EvaluateComment) {
            this.EvaluateComment = EvaluateComment;
        }

        public int getEvaluateLevel() {
            return EvaluateLevel;
        }

        public void setEvaluateLevel(int EvaluateLevel) {
            this.EvaluateLevel = EvaluateLevel;
        }

        public Object getEstimateEndTime() {
            return EstimateEndTime;
        }

        public void setEstimateEndTime(Object EstimateEndTime) {
            this.EstimateEndTime = EstimateEndTime;
        }

        public String getEstimateEndTimeShow() {
            return EstimateEndTimeShow;
        }

        public void setEstimateEndTimeShow(String EstimateEndTimeShow) {
            this.EstimateEndTimeShow = EstimateEndTimeShow;
        }

        public boolean isIsReminder() {
            return IsReminder;
        }

        public void setIsReminder(boolean IsReminder) {
            this.IsReminder = IsReminder;
        }

        public Object getIsShowPositionShow() {
            return IsShowPositionShow;
        }

        public void setIsShowPositionShow(Object IsShowPositionShow) {
            this.IsShowPositionShow = IsShowPositionShow;
        }

        public String getProjectNameShow() {
            return ProjectNameShow;
        }

        public void setProjectNameShow(String ProjectNameShow) {
            this.ProjectNameShow = ProjectNameShow;
        }

        public String getProjectSystemNameShow() {
            return ProjectSystemNameShow;
        }

        public void setProjectSystemNameShow(String ProjectSystemNameShow) {
            this.ProjectSystemNameShow = ProjectSystemNameShow;
        }

        public String getStartTimeShow() {
            return StartTimeShow;
        }

        public void setStartTimeShow(String StartTimeShow) {
            this.StartTimeShow = StartTimeShow;
        }

        public String getMaintenancePersonConfirmTimeShow() {
            return MaintenancePersonConfirmTimeShow;
        }

        public void setMaintenancePersonConfirmTimeShow(String MaintenancePersonConfirmTimeShow) {
            this.MaintenancePersonConfirmTimeShow = MaintenancePersonConfirmTimeShow;
        }

        public String getAcceptUserInfoShow() {
            return AcceptUserInfoShow;
        }

        public void setAcceptUserInfoShow(String AcceptUserInfoShow) {
            this.AcceptUserInfoShow = AcceptUserInfoShow;
        }

        public String getAcceptUserInfoTel() {
            return AcceptUserInfoTel;
        }

        public void setAcceptUserInfoTel(String AcceptUserInfoTel) {
            this.AcceptUserInfoTel = AcceptUserInfoTel;
        }

        public String getStateShow() {
            return StateShow;
        }

        public void setStateShow(String StateShow) {
            this.StateShow = StateShow;
        }

        public String getIsReminderShow() {
            return IsReminderShow;
        }

        public void setIsReminderShow(String IsReminderShow) {
            this.IsReminderShow = IsReminderShow;
        }

        public Object getFaultTypes() {
            return FaultTypes;
        }

        public void setFaultTypes(Object FaultTypes) {
            this.FaultTypes = FaultTypes;
        }

        public Object getRepairMethod() {
            return RepairMethod;
        }

        public void setRepairMethod(Object RepairMethod) {
            this.RepairMethod = RepairMethod;
        }

        public Object getStateIconUrl() {
            return StateIconUrl;
        }

        public void setStateIconUrl(Object StateIconUrl) {
            this.StateIconUrl = StateIconUrl;
        }

        @Override
        public String toString() {
            return "ModWorkOrder{" +
                    "ID='" + ID + '\'' +
                    ", ProjectId='" + ProjectId + '\'' +
                    ", SystemId='" + SystemId + '\'' +
                    ", Code='" + Code + '\'' +
                    ", StartUserName='" + StartUserName + '\'' +
                    ", StartUserTel='" + StartUserTel + '\'' +
                    ", ProjectSystemAuthorized_Id='" + ProjectSystemAuthorized_Id + '\'' +
                    ", SpFaultType_Id=" + SpFaultType_Id +
                    ", FaultPlace=" + FaultPlace +
                    ", SpRepairMethod_Id=" + SpRepairMethod_Id +
                    ", RepairContent=" + RepairContent +
                    ", OtherContent=" + OtherContent +
                    ", State=" + State +
                    ", IsShowPosition=" + IsShowPosition +
                    ", EndTypeShow='" + EndTypeShow + '\'' +
                    ", EndReason=" + EndReason +
                    ", EndTimeShow='" + EndTimeShow + '\'' +
                    ", EvaluateComment=" + EvaluateComment +
                    ", EvaluateLevel=" + EvaluateLevel +
                    ", EstimateEndTime=" + EstimateEndTime +
                    ", EstimateEndTimeShow='" + EstimateEndTimeShow + '\'' +
                    ", IsReminder=" + IsReminder +
                    ", IsShowPositionShow=" + IsShowPositionShow +
                    ", ProjectNameShow='" + ProjectNameShow + '\'' +
                    ", ProjectSystemNameShow='" + ProjectSystemNameShow + '\'' +
                    ", StartTimeShow='" + StartTimeShow + '\'' +
                    ", MaintenancePersonConfirmTimeShow='" + MaintenancePersonConfirmTimeShow + '\'' +
                    ", AcceptUserInfoShow='" + AcceptUserInfoShow + '\'' +
                    ", AcceptUserInfoTel='" + AcceptUserInfoTel + '\'' +
                    ", StateShow='" + StateShow + '\'' +
                    ", IsReminderShow='" + IsReminderShow + '\'' +
                    ", FaultTypes=" + FaultTypes +
                    ", RepairMethod=" + RepairMethod +
                    ", StateIconUrl=" + StateIconUrl +
                    '}';
        }
    }
}
