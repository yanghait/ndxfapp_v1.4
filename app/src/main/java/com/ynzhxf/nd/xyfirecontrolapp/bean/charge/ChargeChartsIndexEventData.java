package com.ynzhxf.nd.xyfirecontrolapp.bean.charge;


import java.util.List;

/**
 * author hbzhou
 * date 2019/10/30 17:43
 */
public class ChargeChartsIndexEventData {

    /**
     * ProjectOnlineSum : 42
     * ProjectConnectNormalCount : 0
     * ProjectConnectAbormalCount : 42
     * RealAlarmSum : 0
     * Recent24hEventCount : 0
     * InspectTaskSum : 3
     * InspectTaskNotDone : 4
     * InspectTaskNormal : 0
     * InspectTaskAbnormal : 0
     * LsAreaWorkOrderCount : []
     */

    private int ProjectOnlineSum;
    private int ProjectConnectNormalCount;
    private int ProjectConnectAbormalCount;
    private int RealAlarmSum;
    private int Recent24hEventCount;
    private int InspectTaskSum;
    private int InspectTaskNotDone;
    private int InspectTaskNormal;
    private int InspectTaskAbnormal;
    private List<IndexWorkOrderBean> LsAreaWorkOrderCount;

    public List<IndexWorkOrderBean> getLsAreaWorkOrderCount() {
        return LsAreaWorkOrderCount;
    }

    public void setLsAreaWorkOrderCount(List<IndexWorkOrderBean> lsAreaWorkOrderCount) {
        LsAreaWorkOrderCount = lsAreaWorkOrderCount;
    }

    public static class IndexWorkOrderBean{

        /**
         * AreaId : 2
         * AreaName : 昆明市
         * WorkOrderSum : 47
         * WorkOrderDone : 47
         * WorkOrderNotDone : 0
         */

        private String AreaId;
        private String AreaName;
        private int WorkOrderSum;
        private int WorkOrderDone;
        private int WorkOrderNotDone;

        public String getAreaId() {
            return AreaId;
        }

        public void setAreaId(String AreaId) {
            this.AreaId = AreaId;
        }

        public String getAreaName() {
            return AreaName;
        }

        public void setAreaName(String AreaName) {
            this.AreaName = AreaName;
        }

        public int getWorkOrderSum() {
            return WorkOrderSum;
        }

        public void setWorkOrderSum(int WorkOrderSum) {
            this.WorkOrderSum = WorkOrderSum;
        }

        public int getWorkOrderDone() {
            return WorkOrderDone;
        }

        public void setWorkOrderDone(int WorkOrderDone) {
            this.WorkOrderDone = WorkOrderDone;
        }

        public int getWorkOrderNotDone() {
            return WorkOrderNotDone;
        }

        public void setWorkOrderNotDone(int WorkOrderNotDone) {
            this.WorkOrderNotDone = WorkOrderNotDone;
        }
    }

    public int getProjectOnlineSum() {
        return ProjectOnlineSum;
    }

    public void setProjectOnlineSum(int ProjectOnlineSum) {
        this.ProjectOnlineSum = ProjectOnlineSum;
    }

    public int getProjectConnectNormalCount() {
        return ProjectConnectNormalCount;
    }

    public void setProjectConnectNormalCount(int ProjectConnectNormalCount) {
        this.ProjectConnectNormalCount = ProjectConnectNormalCount;
    }

    public int getProjectConnectAbormalCount() {
        return ProjectConnectAbormalCount;
    }

    public void setProjectConnectAbormalCount(int ProjectConnectAbormalCount) {
        this.ProjectConnectAbormalCount = ProjectConnectAbormalCount;
    }

    public int getRealAlarmSum() {
        return RealAlarmSum;
    }

    public void setRealAlarmSum(int RealAlarmSum) {
        this.RealAlarmSum = RealAlarmSum;
    }

    public int getRecent24hEventCount() {
        return Recent24hEventCount;
    }

    public void setRecent24hEventCount(int Recent24hEventCount) {
        this.Recent24hEventCount = Recent24hEventCount;
    }

    public int getInspectTaskSum() {
        return InspectTaskSum;
    }

    public void setInspectTaskSum(int InspectTaskSum) {
        this.InspectTaskSum = InspectTaskSum;
    }

    public int getInspectTaskNotDone() {
        return InspectTaskNotDone;
    }

    public void setInspectTaskNotDone(int InspectTaskNotDone) {
        this.InspectTaskNotDone = InspectTaskNotDone;
    }

    public int getInspectTaskNormal() {
        return InspectTaskNormal;
    }

    public void setInspectTaskNormal(int InspectTaskNormal) {
        this.InspectTaskNormal = InspectTaskNormal;
    }

    public int getInspectTaskAbnormal() {
        return InspectTaskAbnormal;
    }

    public void setInspectTaskAbnormal(int InspectTaskAbnormal) {
        this.InspectTaskAbnormal = InspectTaskAbnormal;
    }

}
