package com.ynzhxf.nd.xyfirecontrolapp.bean.charge;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;


/**
 * author hbzhou
 * date 2019/11/4 13:02
 */
public class ChargeChartsDetailsBean implements MultiItemEntity {


    /**
     * ProjectName :
     * ProjectAddress :
     * Has3DInformation : true
     * HasBeenControl : 0
     * ConnectState : -2
     * CompetName : 曲靖麒麟区公安消防大队
     * CompetPhone : 13888488762
     * IsMaintenance : false
     * MaintenanceName : 云南诺盾消防工程技术有限公司
     * MaintenanceConnectName : 张灵伟
     * MaintenancePhone : 13529157601
     * EnterpriseConnectName : 宁德娥
     * EnterprisePhone : 13508847846
     * LsAlarmLog : []
     */

    private String ProjectName;
    private String ProjectAddress;
    private boolean Has3DInformation;
    private int HasBeenControl;
    private int ConnectState;
    private String CompetName;
    private String CompetPhone;
    private boolean IsMaintenance;
    private String MaintenanceName;
    private String MaintenanceConnectName;
    private String MaintenancePhone;
    private String EnterpriseConnectName;
    private String EnterprisePhone;
    private List<LsAlarmLogBean> LsAlarmLog;
    private int beanType;

    public List<LsAlarmLogBean> getLsAlarmLog() {
        return LsAlarmLog;
    }

    public void setLsAlarmLog(List<LsAlarmLogBean> lsAlarmLog) {
        LsAlarmLog = lsAlarmLog;
    }

    public static class LsAlarmLogBean{
        /*EventTimeStr 时间
        AreaName 区域
        pointType 设备
        AlarmValue 报警值
        EventType.Name  事件类型
        SubconditionName 报警类型*/

        private String EventTimeStr;
        private String AreaName;
        private String pointType;
        private String AlarmValue;
        private String SubconditionName;
        private String EventTypeName;

        public String getEventTypeName() {
            return EventTypeName;
        }

        public void setEventTypeName(String eventTypeName) {
            EventTypeName = eventTypeName;
        }

        public String getEventTimeStr() {
            return EventTimeStr;
        }

        public void setEventTimeStr(String eventTimeStr) {
            EventTimeStr = eventTimeStr;
        }

        public String getAreaName() {
            return AreaName;
        }

        public void setAreaName(String areaName) {
            AreaName = areaName;
        }

        public String getPointType() {
            return pointType;
        }

        public void setPointType(String pointType) {
            this.pointType = pointType;
        }

        public String getAlarmValue() {
            return AlarmValue;
        }

        public void setAlarmValue(String alarmValue) {
            AlarmValue = alarmValue;
        }

        public String getSubconditionName() {
            return SubconditionName;
        }

        public void setSubconditionName(String subconditionName) {
            SubconditionName = subconditionName;
        }
    }

    public boolean isMaintenance() {
        return IsMaintenance;
    }

    public void setMaintenance(boolean maintenance) {
        IsMaintenance = maintenance;
    }

    public int getBeanType() {
        return beanType;
    }

    public void setBeanType(int beanType) {
        this.beanType = beanType;
    }

    @Override
    public int getItemType() {
        return getBeanType();
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String ProjectName) {
        this.ProjectName = ProjectName;
    }

    public String getProjectAddress() {
        return ProjectAddress;
    }

    public void setProjectAddress(String ProjectAddress) {
        this.ProjectAddress = ProjectAddress;
    }

    public boolean isHas3DInformation() {
        return Has3DInformation;
    }

    public void setHas3DInformation(boolean Has3DInformation) {
        this.Has3DInformation = Has3DInformation;
    }

    public int getHasBeenControl() {
        return HasBeenControl;
    }

    public void setHasBeenControl(int HasBeenControl) {
        this.HasBeenControl = HasBeenControl;
    }

    public int getConnectState() {
        return ConnectState;
    }

    public void setConnectState(int ConnectState) {
        this.ConnectState = ConnectState;
    }

    public String getCompetName() {
        return CompetName;
    }

    public void setCompetName(String CompetName) {
        this.CompetName = CompetName;
    }

    public String getCompetPhone() {
        return CompetPhone;
    }

    public void setCompetPhone(String CompetPhone) {
        this.CompetPhone = CompetPhone;
    }

    public boolean isIsMaintenance() {
        return IsMaintenance;
    }

    public void setIsMaintenance(boolean IsMaintenance) {
        this.IsMaintenance = IsMaintenance;
    }

    public String getMaintenanceName() {
        return MaintenanceName;
    }

    public void setMaintenanceName(String MaintenanceName) {
        this.MaintenanceName = MaintenanceName;
    }

    public String getMaintenanceConnectName() {
        return MaintenanceConnectName;
    }

    public void setMaintenanceConnectName(String MaintenanceConnectName) {
        this.MaintenanceConnectName = MaintenanceConnectName;
    }

    public String getMaintenancePhone() {
        return MaintenancePhone;
    }

    public void setMaintenancePhone(String MaintenancePhone) {
        this.MaintenancePhone = MaintenancePhone;
    }

    public String getEnterpriseConnectName() {
        return EnterpriseConnectName;
    }

    public void setEnterpriseConnectName(String EnterpriseConnectName) {
        this.EnterpriseConnectName = EnterpriseConnectName;
    }

    public String getEnterprisePhone() {
        return EnterprisePhone;
    }

    public void setEnterprisePhone(String EnterprisePhone) {
        this.EnterprisePhone = EnterprisePhone;
    }
}
