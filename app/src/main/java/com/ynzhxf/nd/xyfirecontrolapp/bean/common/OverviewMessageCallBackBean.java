package com.ynzhxf.nd.xyfirecontrolapp.bean.common;


import java.util.List;

/**
 * author hbzhou
 * date 2019/9/17 13:43
 */
public class OverviewMessageCallBackBean {

    /**
     * ID : e95515418f844ee38462673e7e0385ff
     * OverviewMsg : 设备诊断低分项目42个，高风险项目2个，待处理工单0个，待完成巡检任务0个。
     * LsProjectId : ["7e569f263b10409bbff45ade170948cb","94b6970a772448cea351e438d4e9487e","b8207a5e34964bf9ade808266203132b","b3479beca31f4018b49025ce2fff7b27","330c8e3c1ade4397a39aa16476328f2e","8805823603f44efe9cdbbaae38a6ecb2","959c621b46944ba1a7ee4b78694ded3f","ef6005a88fe84bf9afedede4574a00ec","36575daf203540c98e326abebcf1286d","660f3735bfbd4d70be2ea67140c7c67e","1edd371e9dd744f59bb2a2b4577594e7","1fbd54470c954c42acbcde403e3df4ab","3a425cb5c4d14cd399c8b5d54d0e2d52","83aeb40c8a2a4886815564f861c37417","867b6a438f414eaeb56da79e5d15c8e1","9474675b76684ea496b3aaf6ee83c35e","9842078c28964a239724f42bb675c051","9882a0f482ab46598418107886c7b70f","9e34b955e93e4ec9a2412bbff25f22c1","bb1e82b92c394e999d15e03f0d4099e9","c3f04c26abed48eabfa2b389a05453e3","df064484810a460e9b6a7a928d69c72a","b20679a1787943288d31ac8d43349f72","da9a3dfb5aa542429a79e91df4d1b366","dc62d29b5ec24b55aa6d9cde875b750a","060f443ae65c420691ddff028c152ee6","2a17cb75231d41da93ea164e10751c0c","6f4fad022cf34d73ac8f67324867a6e0","9f0b4fb319cd415488178e11a22bc900","ca42db7a9e114287b15422e542b5928b","d2e3f337f132457db36599fc49147bc7","eb957edf604d4afc9c2f1bc341637691","f699a45a37fb4ef49c1d0789e1f56d9b","3438d8b8c65a4b4b8678744b70bcd822","38c38486bb7b4e15ad7991dae2d03660","41f30025f07d44588859dde50bf1ab18","50330f414ab5403ca7f2b8a9876259ce","fead1560cfc644589afc960262ddf2f7","02c06ccff4be4eafad54896fa53fa919","d2759e3a703944d391ee913f20935e38","e58400df774e4bee9e3a6ce6016ac79f","c0b60818392c46ac85cfeeb5aa4a3ed0"]
     * AlarmData : {"ProjectCount":1,"DetailTemplate":"报警{0}次(故障{1}次，报警率{2}%)；","AlarmSum":3,"BreakDownSum":1,"lsAlarmData":[{"ProjectId":"94b6970a772448cea351e438d4e9487e","ProjectName":"云南捷诺科技","AlarmCount":3,"BreakDownCount":1,"AlarmRate":23.3}]}
     * EquipDetectData : {"ProjectCount":42,"DetailTemplate":"设备诊断{0}分；","lsEquipDetectData":[{"ProjectId":"7e569f263b10409bbff45ade170948cb","ProjectName":"东部公交车场","LowScore":0},{"ProjectId":"94b6970a772448cea351e438d4e9487e","ProjectName":"云南捷诺科技","LowScore":0},{"ProjectId":"b8207a5e34964bf9ade808266203132b","ProjectName":"北部公交站","LowScore":0},{"ProjectId":"b3479beca31f4018b49025ce2fff7b27","ProjectName":"新册公交车场","LowScore":0},{"ProjectId":"330c8e3c1ade4397a39aa16476328f2e","ProjectName":"曲靖分输站2号阀室","LowScore":0},{"ProjectId":"8805823603f44efe9cdbbaae38a6ecb2","ProjectName":"曲靖分输站3号阀室","LowScore":0},{"ProjectId":"959c621b46944ba1a7ee4b78694ded3f","ProjectName":"曲靖分输站1号阀室","LowScore":0},{"ProjectId":"ef6005a88fe84bf9afedede4574a00ec","ProjectName":"沾益区卓越明郡","LowScore":0},{"ProjectId":"36575daf203540c98e326abebcf1286d","ProjectName":"会泽县宁瑞小区","LowScore":0},{"ProjectId":"660f3735bfbd4d70be2ea67140c7c67e","ProjectName":"罗平振兴新城二期","LowScore":0},{"ProjectId":"1edd371e9dd744f59bb2a2b4577594e7","ProjectName":"云南驰宏锌锗股份有限公司","LowScore":0},{"ProjectId":"1fbd54470c954c42acbcde403e3df4ab","ProjectName":"曲靖鼎盛世家4期","LowScore":0},{"ProjectId":"3a425cb5c4d14cd399c8b5d54d0e2d52","ProjectName":"官房大酒店","LowScore":0},{"ProjectId":"83aeb40c8a2a4886815564f861c37417","ProjectName":"曲靖燃气集团中心调度站","LowScore":0},{"ProjectId":"867b6a438f414eaeb56da79e5d15c8e1","ProjectName":"曲靖天道园","LowScore":0},{"ProjectId":"9474675b76684ea496b3aaf6ee83c35e","ProjectName":"曲靖南兴花园金穗花园五期","LowScore":0},{"ProjectId":"9842078c28964a239724f42bb675c051","ProjectName":"曲靖雍景湾雍景别苑","LowScore":0},{"ProjectId":"9882a0f482ab46598418107886c7b70f","ProjectName":"曲靖汇景园","LowScore":0},{"ProjectId":"9e34b955e93e4ec9a2412bbff25f22c1","ProjectName":"曲靖一中卓立学校","LowScore":0},{"ProjectId":"bb1e82b92c394e999d15e03f0d4099e9","ProjectName":"曲靖金麟湾温泉度假酒店","LowScore":0},{"ProjectId":"c3f04c26abed48eabfa2b389a05453e3","ProjectName":"曲靖市麒麟区通利佳苑","LowScore":0},{"ProjectId":"df064484810a460e9b6a7a928d69c72a","ProjectName":"曲靖安厦十五城上东区","LowScore":0},{"ProjectId":"b20679a1787943288d31ac8d43349f72","ProjectName":"曲靖分输站4号阀室","LowScore":0},{"ProjectId":"da9a3dfb5aa542429a79e91df4d1b366","ProjectName":"宣威分输站","LowScore":0},{"ProjectId":"dc62d29b5ec24b55aa6d9cde875b750a","ProjectName":"宣威市万盛国际城·名汇广场","LowScore":0},{"ProjectId":"060f443ae65c420691ddff028c152ee6","ProjectName":"东盟?陆良丝绸皮革城","LowScore":0},{"ProjectId":"2a17cb75231d41da93ea164e10751c0c","ProjectName":"陆良县中医院","LowScore":0},{"ProjectId":"6f4fad022cf34d73ac8f67324867a6e0","ProjectName":"陆良县弘悦湖大酒店","LowScore":0},{"ProjectId":"9f0b4fb319cd415488178e11a22bc900","ProjectName":"陆良县康隆新洲","LowScore":0},{"ProjectId":"ca42db7a9e114287b15422e542b5928b","ProjectName":"云南欧罗汉姆肥业科技有限公司二期","LowScore":0},{"ProjectId":"d2e3f337f132457db36599fc49147bc7","ProjectName":"陆良温州商贸城","LowScore":0},{"ProjectId":"eb957edf604d4afc9c2f1bc341637691","ProjectName":"云南欧罗汉姆肥业科技有限公司一期","LowScore":0},{"ProjectId":"f699a45a37fb4ef49c1d0789e1f56d9b","ProjectName":"陆良图腾商业中心","LowScore":0},{"ProjectId":"3438d8b8c65a4b4b8678744b70bcd822","ProjectName":"曲靖万乐城","LowScore":0},{"ProjectId":"38c38486bb7b4e15ad7991dae2d03660","ProjectName":"曲靖分输站","LowScore":0},{"ProjectId":"41f30025f07d44588859dde50bf1ab18","ProjectName":"曲靖经开区燃气门站","LowScore":0},{"ProjectId":"50330f414ab5403ca7f2b8a9876259ce","ProjectName":"曲靖经开区新兴产业示范园科技孵化基地","LowScore":0},{"ProjectId":"fead1560cfc644589afc960262ddf2f7","ProjectName":"曲靖万达广场","LowScore":0},{"ProjectId":"02c06ccff4be4eafad54896fa53fa919","ProjectName":"师宗锦绣豪庭小区","LowScore":0},{"ProjectId":"d2759e3a703944d391ee913f20935e38","ProjectName":"师宗凯莱财富中心","LowScore":0},{"ProjectId":"e58400df774e4bee9e3a6ce6016ac79f","ProjectName":"师宗凤凰商都","LowScore":0},{"ProjectId":"c0b60818392c46ac85cfeeb5aa4a3ed0","ProjectName":"富源县翡翠新城","LowScore":0}]}
     * FireRiskData : {"ProjectCount":2,"DetailTemplate":"风险级别{0}；","LsFireRiskData":[{"ProjectId":"7e569f263b10409bbff45ade170948cb","ProjectName":"东部公交车场","FireRiskRate":"1.8"},{"ProjectId":"1edd371e9dd744f59bb2a2b4577594e7","ProjectName":"云南驰宏锌锗股份有限公司","FireRiskRate":"4.8"}]}
     * WorkOrderData : {"WorkOrderNotDoneSum":1,"DetailTemplate":"工单{0}个；","LsWorkOrderData":[{"ProjectId":"94b6970a772448cea351e438d4e9487e","ProjectName":"云南捷诺科技","WorkOrderNotDoneCount":1}]}
     * InspTaskData : {"TaskNotDoneSum":1,"DetailTemplate":"巡检任务{0}个；","LsInspectionData":[{"ProjectId":"94b6970a772448cea351e438d4e9487e","ProjectName":"云南捷诺科技","TaskNotDoneCount":1}]}
     */

    private String ID;
    private String OverviewMsg;
    private AlarmDataBean AlarmData;
    private EquipDetectDataBean EquipDetectData;
    private FireRiskDataBean FireRiskData;
    private WorkOrderDataBean WorkOrderData;
    private InspTaskDataBean InspTaskData;
    private List<String> LsProjectId;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getOverviewMsg() {
        return OverviewMsg;
    }

    public void setOverviewMsg(String OverviewMsg) {
        this.OverviewMsg = OverviewMsg;
    }

    public AlarmDataBean getAlarmData() {
        return AlarmData;
    }

    public void setAlarmData(AlarmDataBean AlarmData) {
        this.AlarmData = AlarmData;
    }

    public EquipDetectDataBean getEquipDetectData() {
        return EquipDetectData;
    }

    public void setEquipDetectData(EquipDetectDataBean EquipDetectData) {
        this.EquipDetectData = EquipDetectData;
    }

    public FireRiskDataBean getFireRiskData() {
        return FireRiskData;
    }

    public void setFireRiskData(FireRiskDataBean FireRiskData) {
        this.FireRiskData = FireRiskData;
    }

    public WorkOrderDataBean getWorkOrderData() {
        return WorkOrderData;
    }

    public void setWorkOrderData(WorkOrderDataBean WorkOrderData) {
        this.WorkOrderData = WorkOrderData;
    }

    public InspTaskDataBean getInspTaskData() {
        return InspTaskData;
    }

    public void setInspTaskData(InspTaskDataBean InspTaskData) {
        this.InspTaskData = InspTaskData;
    }

    public List<String> getLsProjectId() {
        return LsProjectId;
    }

    public void setLsProjectId(List<String> LsProjectId) {
        this.LsProjectId = LsProjectId;
    }

    public static class AlarmDataBean {
        /**
         * ProjectCount : 1
         * DetailTemplate : 报警{0}次(故障{1}次，报警率{2}%)；
         * AlarmSum : 3
         * BreakDownSum : 1
         * lsAlarmData : [{"ProjectId":"94b6970a772448cea351e438d4e9487e","ProjectName":"云南捷诺科技","AlarmCount":3,"BreakDownCount":1,"AlarmRate":23.3}]
         */

        private int ProjectCount;
        private String DetailTemplate;
        private int AlarmSum;
        private int BreakDownSum;
        private List<LsAlarmDataBean> lsAlarmData;

        public int getProjectCount() {
            return ProjectCount;
        }

        public void setProjectCount(int ProjectCount) {
            this.ProjectCount = ProjectCount;
        }

        public String getDetailTemplate() {
            return DetailTemplate;
        }

        public void setDetailTemplate(String DetailTemplate) {
            this.DetailTemplate = DetailTemplate;
        }

        public int getAlarmSum() {
            return AlarmSum;
        }

        public void setAlarmSum(int AlarmSum) {
            this.AlarmSum = AlarmSum;
        }

        public int getBreakDownSum() {
            return BreakDownSum;
        }

        public void setBreakDownSum(int BreakDownSum) {
            this.BreakDownSum = BreakDownSum;
        }

        public List<LsAlarmDataBean> getLsAlarmData() {
            return lsAlarmData;
        }

        public void setLsAlarmData(List<LsAlarmDataBean> lsAlarmData) {
            this.lsAlarmData = lsAlarmData;
        }

        public static class LsAlarmDataBean {
            /**
             * ProjectId : 94b6970a772448cea351e438d4e9487e
             * ProjectName : 云南捷诺科技
             * AlarmCount : 3
             * BreakDownCount : 1
             * AlarmRate : 23.3
             */

            private String ProjectId;
            private String ProjectName;
            private int AlarmCount;
            private int BreakDownCount;
            private double AlarmRate;

            public String getProjectId() {
                return ProjectId;
            }

            public void setProjectId(String ProjectId) {
                this.ProjectId = ProjectId;
            }

            public String getProjectName() {
                return ProjectName;
            }

            public void setProjectName(String ProjectName) {
                this.ProjectName = ProjectName;
            }

            public int getAlarmCount() {
                return AlarmCount;
            }

            public void setAlarmCount(int AlarmCount) {
                this.AlarmCount = AlarmCount;
            }

            public int getBreakDownCount() {
                return BreakDownCount;
            }

            public void setBreakDownCount(int BreakDownCount) {
                this.BreakDownCount = BreakDownCount;
            }

            public double getAlarmRate() {
                return AlarmRate;
            }

            public void setAlarmRate(double AlarmRate) {
                this.AlarmRate = AlarmRate;
            }
        }
    }

    public static class EquipDetectDataBean {
        /**
         * ProjectCount : 42
         * DetailTemplate : 设备诊断{0}分；
         * lsEquipDetectData : [{"ProjectId":"7e569f263b10409bbff45ade170948cb","ProjectName":"东部公交车场","LowScore":0},{"ProjectId":"94b6970a772448cea351e438d4e9487e","ProjectName":"云南捷诺科技","LowScore":0},{"ProjectId":"b8207a5e34964bf9ade808266203132b","ProjectName":"北部公交站","LowScore":0},{"ProjectId":"b3479beca31f4018b49025ce2fff7b27","ProjectName":"新册公交车场","LowScore":0},{"ProjectId":"330c8e3c1ade4397a39aa16476328f2e","ProjectName":"曲靖分输站2号阀室","LowScore":0},{"ProjectId":"8805823603f44efe9cdbbaae38a6ecb2","ProjectName":"曲靖分输站3号阀室","LowScore":0},{"ProjectId":"959c621b46944ba1a7ee4b78694ded3f","ProjectName":"曲靖分输站1号阀室","LowScore":0},{"ProjectId":"ef6005a88fe84bf9afedede4574a00ec","ProjectName":"沾益区卓越明郡","LowScore":0},{"ProjectId":"36575daf203540c98e326abebcf1286d","ProjectName":"会泽县宁瑞小区","LowScore":0},{"ProjectId":"660f3735bfbd4d70be2ea67140c7c67e","ProjectName":"罗平振兴新城二期","LowScore":0},{"ProjectId":"1edd371e9dd744f59bb2a2b4577594e7","ProjectName":"云南驰宏锌锗股份有限公司","LowScore":0},{"ProjectId":"1fbd54470c954c42acbcde403e3df4ab","ProjectName":"曲靖鼎盛世家4期","LowScore":0},{"ProjectId":"3a425cb5c4d14cd399c8b5d54d0e2d52","ProjectName":"官房大酒店","LowScore":0},{"ProjectId":"83aeb40c8a2a4886815564f861c37417","ProjectName":"曲靖燃气集团中心调度站","LowScore":0},{"ProjectId":"867b6a438f414eaeb56da79e5d15c8e1","ProjectName":"曲靖天道园","LowScore":0},{"ProjectId":"9474675b76684ea496b3aaf6ee83c35e","ProjectName":"曲靖南兴花园金穗花园五期","LowScore":0},{"ProjectId":"9842078c28964a239724f42bb675c051","ProjectName":"曲靖雍景湾雍景别苑","LowScore":0},{"ProjectId":"9882a0f482ab46598418107886c7b70f","ProjectName":"曲靖汇景园","LowScore":0},{"ProjectId":"9e34b955e93e4ec9a2412bbff25f22c1","ProjectName":"曲靖一中卓立学校","LowScore":0},{"ProjectId":"bb1e82b92c394e999d15e03f0d4099e9","ProjectName":"曲靖金麟湾温泉度假酒店","LowScore":0},{"ProjectId":"c3f04c26abed48eabfa2b389a05453e3","ProjectName":"曲靖市麒麟区通利佳苑","LowScore":0},{"ProjectId":"df064484810a460e9b6a7a928d69c72a","ProjectName":"曲靖安厦十五城上东区","LowScore":0},{"ProjectId":"b20679a1787943288d31ac8d43349f72","ProjectName":"曲靖分输站4号阀室","LowScore":0},{"ProjectId":"da9a3dfb5aa542429a79e91df4d1b366","ProjectName":"宣威分输站","LowScore":0},{"ProjectId":"dc62d29b5ec24b55aa6d9cde875b750a","ProjectName":"宣威市万盛国际城·名汇广场","LowScore":0},{"ProjectId":"060f443ae65c420691ddff028c152ee6","ProjectName":"东盟?陆良丝绸皮革城","LowScore":0},{"ProjectId":"2a17cb75231d41da93ea164e10751c0c","ProjectName":"陆良县中医院","LowScore":0},{"ProjectId":"6f4fad022cf34d73ac8f67324867a6e0","ProjectName":"陆良县弘悦湖大酒店","LowScore":0},{"ProjectId":"9f0b4fb319cd415488178e11a22bc900","ProjectName":"陆良县康隆新洲","LowScore":0},{"ProjectId":"ca42db7a9e114287b15422e542b5928b","ProjectName":"云南欧罗汉姆肥业科技有限公司二期","LowScore":0},{"ProjectId":"d2e3f337f132457db36599fc49147bc7","ProjectName":"陆良温州商贸城","LowScore":0},{"ProjectId":"eb957edf604d4afc9c2f1bc341637691","ProjectName":"云南欧罗汉姆肥业科技有限公司一期","LowScore":0},{"ProjectId":"f699a45a37fb4ef49c1d0789e1f56d9b","ProjectName":"陆良图腾商业中心","LowScore":0},{"ProjectId":"3438d8b8c65a4b4b8678744b70bcd822","ProjectName":"曲靖万乐城","LowScore":0},{"ProjectId":"38c38486bb7b4e15ad7991dae2d03660","ProjectName":"曲靖分输站","LowScore":0},{"ProjectId":"41f30025f07d44588859dde50bf1ab18","ProjectName":"曲靖经开区燃气门站","LowScore":0},{"ProjectId":"50330f414ab5403ca7f2b8a9876259ce","ProjectName":"曲靖经开区新兴产业示范园科技孵化基地","LowScore":0},{"ProjectId":"fead1560cfc644589afc960262ddf2f7","ProjectName":"曲靖万达广场","LowScore":0},{"ProjectId":"02c06ccff4be4eafad54896fa53fa919","ProjectName":"师宗锦绣豪庭小区","LowScore":0},{"ProjectId":"d2759e3a703944d391ee913f20935e38","ProjectName":"师宗凯莱财富中心","LowScore":0},{"ProjectId":"e58400df774e4bee9e3a6ce6016ac79f","ProjectName":"师宗凤凰商都","LowScore":0},{"ProjectId":"c0b60818392c46ac85cfeeb5aa4a3ed0","ProjectName":"富源县翡翠新城","LowScore":0}]
         */

        private int ProjectCount;
        private String DetailTemplate;
        private List<LsEquipDetectDataBean> lsEquipDetectData;

        public int getProjectCount() {
            return ProjectCount;
        }

        public void setProjectCount(int ProjectCount) {
            this.ProjectCount = ProjectCount;
        }

        public String getDetailTemplate() {
            return DetailTemplate;
        }

        public void setDetailTemplate(String DetailTemplate) {
            this.DetailTemplate = DetailTemplate;
        }

        public List<LsEquipDetectDataBean> getLsEquipDetectData() {
            return lsEquipDetectData;
        }

        public void setLsEquipDetectData(List<LsEquipDetectDataBean> lsEquipDetectData) {
            this.lsEquipDetectData = lsEquipDetectData;
        }

        public static class LsEquipDetectDataBean {
            /**
             * ProjectId : 7e569f263b10409bbff45ade170948cb
             * ProjectName : 东部公交车场
             * LowScore : 0
             */

            private String ProjectId;
            private String ProjectName;
            private int LowScore;

            public String getProjectId() {
                return ProjectId;
            }

            public void setProjectId(String ProjectId) {
                this.ProjectId = ProjectId;
            }

            public String getProjectName() {
                return ProjectName;
            }

            public void setProjectName(String ProjectName) {
                this.ProjectName = ProjectName;
            }

            public int getLowScore() {
                return LowScore;
            }

            public void setLowScore(int LowScore) {
                this.LowScore = LowScore;
            }
        }
    }

    public static class FireRiskDataBean {
        /**
         * ProjectCount : 2
         * DetailTemplate : 风险级别{0}；
         * LsFireRiskData : [{"ProjectId":"7e569f263b10409bbff45ade170948cb","ProjectName":"东部公交车场","FireRiskRate":"1.8"},{"ProjectId":"1edd371e9dd744f59bb2a2b4577594e7","ProjectName":"云南驰宏锌锗股份有限公司","FireRiskRate":"4.8"}]
         */

        private int ProjectCount;
        private String DetailTemplate;
        private List<LsFireRiskDataBean> LsFireRiskData;

        public int getProjectCount() {
            return ProjectCount;
        }

        public void setProjectCount(int ProjectCount) {
            this.ProjectCount = ProjectCount;
        }

        public String getDetailTemplate() {
            return DetailTemplate;
        }

        public void setDetailTemplate(String DetailTemplate) {
            this.DetailTemplate = DetailTemplate;
        }

        public List<LsFireRiskDataBean> getLsFireRiskData() {
            return LsFireRiskData;
        }

        public void setLsFireRiskData(List<LsFireRiskDataBean> LsFireRiskData) {
            this.LsFireRiskData = LsFireRiskData;
        }

        public static class LsFireRiskDataBean {
            /**
             * ProjectId : 7e569f263b10409bbff45ade170948cb
             * ProjectName : 东部公交车场
             * FireRiskRate : 1.8
             */

            private String ProjectId;
            private String ProjectName;
            private String FireRiskRate;

            public String getProjectId() {
                return ProjectId;
            }

            public void setProjectId(String ProjectId) {
                this.ProjectId = ProjectId;
            }

            public String getProjectName() {
                return ProjectName;
            }

            public void setProjectName(String ProjectName) {
                this.ProjectName = ProjectName;
            }

            public String getFireRiskRate() {
                return FireRiskRate;
            }

            public void setFireRiskRate(String FireRiskRate) {
                this.FireRiskRate = FireRiskRate;
            }
        }
    }

    public static class WorkOrderDataBean {
        /**
         * WorkOrderNotDoneSum : 1
         * DetailTemplate : 工单{0}个；
         * LsWorkOrderData : [{"ProjectId":"94b6970a772448cea351e438d4e9487e","ProjectName":"云南捷诺科技","WorkOrderNotDoneCount":1}]
         */

        private int WorkOrderNotDoneSum;
        private String DetailTemplate;
        private List<LsWorkOrderDataBean> LsWorkOrderData;

        public int getWorkOrderNotDoneSum() {
            return WorkOrderNotDoneSum;
        }

        public void setWorkOrderNotDoneSum(int WorkOrderNotDoneSum) {
            this.WorkOrderNotDoneSum = WorkOrderNotDoneSum;
        }

        public String getDetailTemplate() {
            return DetailTemplate;
        }

        public void setDetailTemplate(String DetailTemplate) {
            this.DetailTemplate = DetailTemplate;
        }

        public List<LsWorkOrderDataBean> getLsWorkOrderData() {
            return LsWorkOrderData;
        }

        public void setLsWorkOrderData(List<LsWorkOrderDataBean> LsWorkOrderData) {
            this.LsWorkOrderData = LsWorkOrderData;
        }

        public static class LsWorkOrderDataBean {
            /**
             * ProjectId : 94b6970a772448cea351e438d4e9487e
             * ProjectName : 云南捷诺科技
             * WorkOrderNotDoneCount : 1
             */

            private String ProjectId;
            private String ProjectName;
            private int WorkOrderNotDoneCount;

            public String getProjectId() {
                return ProjectId;
            }

            public void setProjectId(String ProjectId) {
                this.ProjectId = ProjectId;
            }

            public String getProjectName() {
                return ProjectName;
            }

            public void setProjectName(String ProjectName) {
                this.ProjectName = ProjectName;
            }

            public int getWorkOrderNotDoneCount() {
                return WorkOrderNotDoneCount;
            }

            public void setWorkOrderNotDoneCount(int WorkOrderNotDoneCount) {
                this.WorkOrderNotDoneCount = WorkOrderNotDoneCount;
            }
        }
    }

    public static class InspTaskDataBean {
        /**
         * TaskNotDoneSum : 1
         * DetailTemplate : 巡检任务{0}个；
         * LsInspectionData : [{"ProjectId":"94b6970a772448cea351e438d4e9487e","ProjectName":"云南捷诺科技","TaskNotDoneCount":1}]
         */

        private int TaskNotDoneSum;
        private String DetailTemplate;
        private List<LsInspectionDataBean> LsInspectionData;

        public int getTaskNotDoneSum() {
            return TaskNotDoneSum;
        }

        public void setTaskNotDoneSum(int TaskNotDoneSum) {
            this.TaskNotDoneSum = TaskNotDoneSum;
        }

        public String getDetailTemplate() {
            return DetailTemplate;
        }

        public void setDetailTemplate(String DetailTemplate) {
            this.DetailTemplate = DetailTemplate;
        }

        public List<LsInspectionDataBean> getLsInspectionData() {
            return LsInspectionData;
        }

        public void setLsInspectionData(List<LsInspectionDataBean> LsInspectionData) {
            this.LsInspectionData = LsInspectionData;
        }

        public static class LsInspectionDataBean {
            /**
             * ProjectId : 94b6970a772448cea351e438d4e9487e
             * ProjectName : 云南捷诺科技
             * TaskNotDoneCount : 1
             */

            private String ProjectId;
            private String ProjectName;
            private int TaskNotDoneCount;

            public String getProjectId() {
                return ProjectId;
            }

            public void setProjectId(String ProjectId) {
                this.ProjectId = ProjectId;
            }

            public String getProjectName() {
                return ProjectName;
            }

            public void setProjectName(String ProjectName) {
                this.ProjectName = ProjectName;
            }

            public int getTaskNotDoneCount() {
                return TaskNotDoneCount;
            }

            public void setTaskNotDoneCount(int TaskNotDoneCount) {
                this.TaskNotDoneCount = TaskNotDoneCount;
            }
        }
    }
}
