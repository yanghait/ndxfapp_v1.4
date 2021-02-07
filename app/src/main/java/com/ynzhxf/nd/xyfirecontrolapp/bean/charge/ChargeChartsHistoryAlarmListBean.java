package com.ynzhxf.nd.xyfirecontrolapp.bean.charge;


import java.io.Serializable;
import java.util.List;

/**
 * author hbzhou
 * date 2019/10/31 11:30
 */
public class ChargeChartsHistoryAlarmListBean {

    /**
     * HistoryAlarmSum : 46684
     * LsHistoryAlarmTrend : [{"Day":"/Date(1541347200000)/","StatisticsValue":51},{"Day":"/Date(1541433600000)/","StatisticsValue":33},{"Day":"/Date(1541520000000)/","StatisticsValue":167},{"Day":"/Date(1541606400000)/","StatisticsValue":71},{"Day":"/Date(1541865600000)/","StatisticsValue":14},{"Day":"/Date(1541952000000)/","StatisticsValue":6},{"Day":"/Date(1542038400000)/","StatisticsValue":15},{"Day":"/Date(1542816000000)/","StatisticsValue":110},{"Day":"/Date(1542902400000)/","StatisticsValue":67},{"Day":"/Date(1543075200000)/","StatisticsValue":17},{"Day":"/Date(1543248000000)/","StatisticsValue":1},{"Day":"/Date(1543334400000)/","StatisticsValue":16},{"Day":"/Date(1543420800000)/","StatisticsValue":209},{"Day":"/Date(1543939200000)/","StatisticsValue":37},{"Day":"/Date(1544025600000)/","StatisticsValue":7},{"Day":"/Date(1544112000000)/","StatisticsValue":42},{"Day":"/Date(1544198400000)/","StatisticsValue":31},{"Day":"/Date(1544457600000)/","StatisticsValue":16},{"Day":"/Date(1544544000000)/","StatisticsValue":2},{"Day":"/Date(1544630400000)/","StatisticsValue":3},{"Day":"/Date(1545148800000)/","StatisticsValue":622},{"Day":"/Date(1545408000000)/","StatisticsValue":5},{"Day":"/Date(1545494400000)/","StatisticsValue":143},{"Day":"/Date(1545926400000)/","StatisticsValue":13},{"Day":"/Date(1546012800000)/","StatisticsValue":122},{"Day":"/Date(1546099200000)/","StatisticsValue":96},{"Day":"/Date(1546358400000)/","StatisticsValue":57},{"Day":"/Date(1546444800000)/","StatisticsValue":6572},{"Day":"/Date(1546531200000)/","StatisticsValue":9903},{"Day":"/Date(1546617600000)/","StatisticsValue":11990},{"Day":"/Date(1546704000000)/","StatisticsValue":15992},{"Day":"/Date(1546790400000)/","StatisticsValue":237},{"Day":"/Date(1546876800000)/","StatisticsValue":17}]
     * LsAreaHistoryAlarm : [{"AreaId":"2","AreaName":"昆明市","StatisticsValue":46684},{"AreaId":"3","AreaName":"曲靖市","StatisticsValue":46684},{"AreaId":"ff746eca8a204886acaa10a0a899abf1","AreaName":"楚雄州","StatisticsValue":46684}]
     * LsBusinessTypeHistoryAlarm : [{"BusinessTypeId":"2dca7e5062bf4c4b9152cbf86aaae4ea","BusinessTypeName":"商场农贸市场","StatisticsValue":0},{"BusinessTypeId":"35cae8de08c249089eece4b1c4166f83","BusinessTypeName":"商业综合体","StatisticsValue":0},{"BusinessTypeId":"4d8f6806eda74d7b816acff132e19cc6","BusinessTypeName":"餐饮、酒店、宾馆","StatisticsValue":0},{"BusinessTypeId":"6a29588a10ca4f3787600ae91eb71de9","BusinessTypeName":"住宅民居","StatisticsValue":0},{"BusinessTypeId":"a620aab3f59049d9982d4d70e1adc663","BusinessTypeName":"商务办公楼","StatisticsValue":0},{"BusinessTypeId":"b42701f6fc0241ab827aafa167c1d3f0","BusinessTypeName":"危化品工厂","StatisticsValue":0},{"BusinessTypeId":"cde9bf394cab44f6802bafead2a563e0","BusinessTypeName":"公众聚集型场所","StatisticsValue":0}]
     */

    private int HistoryAlarmSum;
    private List<LsHistoryAlarmTrendBean> LsHistoryAlarmTrend;
    private List<LsAreaHistoryAlarmBean> LsAreaHistoryAlarm;
    private List<LsBusinessTypeHistoryAlarmBean> LsBusinessTypeHistoryAlarm;

    private List<LsHistoryAlarmProjectBean> LsProjectWithAreaBusiness;

    public List<LsHistoryAlarmProjectBean> getLsProjectWithAreaBusiness() {
        return LsProjectWithAreaBusiness;
    }

    public void setLsProjectWithAreaBusiness(List<LsHistoryAlarmProjectBean> lsProjectWithAreaBusiness) {
        LsProjectWithAreaBusiness = lsProjectWithAreaBusiness;
    }

    public static class LsHistoryAlarmProjectBean implements Serializable {

        /**
         * ID : fead1560cfc644589afc960262ddf2f7
         * Name : 曲靖万达广场
         * ParentId : 6ea9f43e4a8f4c849462c2afa1555759
         * Address : 云南省曲靖市麒麟区
         * BusinessTypeId : 2dca7e5062bf4c4b9152cbf86aaae4ea
         * BusinessTypeName : 曲靖万达广场
         * AlarmCount : 0
         */

        private String ID;
        private String Name;
        private String ParentId;
        private String Address;
        private String BusinessTypeId;
        private String BusinessTypeName;
        private int AlarmCount;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getParentId() {
            return ParentId;
        }

        public void setParentId(String ParentId) {
            this.ParentId = ParentId;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String Address) {
            this.Address = Address;
        }

        public String getBusinessTypeId() {
            return BusinessTypeId;
        }

        public void setBusinessTypeId(String BusinessTypeId) {
            this.BusinessTypeId = BusinessTypeId;
        }

        public String getBusinessTypeName() {
            return BusinessTypeName;
        }

        public void setBusinessTypeName(String BusinessTypeName) {
            this.BusinessTypeName = BusinessTypeName;
        }

        public int getAlarmCount() {
            return AlarmCount;
        }

        public void setAlarmCount(int AlarmCount) {
            this.AlarmCount = AlarmCount;
        }
    }

    public int getHistoryAlarmSum() {
        return HistoryAlarmSum;
    }

    public void setHistoryAlarmSum(int HistoryAlarmSum) {
        this.HistoryAlarmSum = HistoryAlarmSum;
    }

    public List<LsHistoryAlarmTrendBean> getLsHistoryAlarmTrend() {
        return LsHistoryAlarmTrend;
    }

    public void setLsHistoryAlarmTrend(List<LsHistoryAlarmTrendBean> LsHistoryAlarmTrend) {
        this.LsHistoryAlarmTrend = LsHistoryAlarmTrend;
    }

    public List<LsAreaHistoryAlarmBean> getLsAreaHistoryAlarm() {
        return LsAreaHistoryAlarm;
    }

    public void setLsAreaHistoryAlarm(List<LsAreaHistoryAlarmBean> LsAreaHistoryAlarm) {
        this.LsAreaHistoryAlarm = LsAreaHistoryAlarm;
    }

    public List<LsBusinessTypeHistoryAlarmBean> getLsBusinessTypeHistoryAlarm() {
        return LsBusinessTypeHistoryAlarm;
    }

    public void setLsBusinessTypeHistoryAlarm(List<LsBusinessTypeHistoryAlarmBean> LsBusinessTypeHistoryAlarm) {
        this.LsBusinessTypeHistoryAlarm = LsBusinessTypeHistoryAlarm;
    }

    public static class LsHistoryAlarmTrendBean {
        /**
         * Day : /Date(1541347200000)/
         * StatisticsValue : 51
         */

        private String Day;
        private int StatisticsValue;
        private String DayShow;

        public String getDayShow() {
            return DayShow;
        }

        public void setDayShow(String dayShow) {
            DayShow = dayShow;
        }

        public String getDay() {
            return Day;
        }

        public void setDay(String Day) {
            this.Day = Day;
        }

        public int getStatisticsValue() {
            return StatisticsValue;
        }

        public void setStatisticsValue(int StatisticsValue) {
            this.StatisticsValue = StatisticsValue;
        }
    }

    public static class LsAreaHistoryAlarmBean {
        /**
         * AreaId : 2
         * AreaName : 昆明市
         * StatisticsValue : 46684
         */

        private String AreaId;
        private String AreaName;
        private int StatisticsValue;

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

        public int getStatisticsValue() {
            return StatisticsValue;
        }

        public void setStatisticsValue(int StatisticsValue) {
            this.StatisticsValue = StatisticsValue;
        }
    }

    public static class LsBusinessTypeHistoryAlarmBean {
        /**
         * BusinessTypeId : 2dca7e5062bf4c4b9152cbf86aaae4ea
         * BusinessTypeName : 商场农贸市场
         * StatisticsValue : 0
         */

        private String BusinessTypeId;
        private String BusinessTypeName;
        private int StatisticsValue;
        private int StatisticsCount;

        public int getStatisticsCount() {
            return StatisticsCount;
        }

        public void setStatisticsCount(int statisticsCount) {
            StatisticsCount = statisticsCount;
        }

        public String getBusinessTypeId() {
            return BusinessTypeId;
        }

        public void setBusinessTypeId(String BusinessTypeId) {
            this.BusinessTypeId = BusinessTypeId;
        }

        public String getBusinessTypeName() {
            return BusinessTypeName;
        }

        public void setBusinessTypeName(String BusinessTypeName) {
            this.BusinessTypeName = BusinessTypeName;
        }

        public int getStatisticsValue() {
            return StatisticsValue;
        }

        public void setStatisticsValue(int StatisticsValue) {
            this.StatisticsValue = StatisticsValue;
        }
    }
}
