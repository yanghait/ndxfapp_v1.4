package com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance;

import java.util.List;

public class CompanyBackFillParamsBean {


    /**
     * modWorkOrder : {"SpFaultTypeId":null,"FaultPlace":null,"SpRepairMethodId":null,"RepairContent":null}
     * lsFaultTypeParam : [{"Type":{"Sort":10,"Name":"故障类型","ID":"6cf241f6c6f74b3ea8d7d7521df8af63","IsNew":true},"TypeId":"6cf241f6c6f74b3ea8d7d7521df8af63","EnterpriseId":null,"Value":null,"Remark":null,"InputTime":"/Date(1536828977747)/","Name":"故障类型1","ID":"dae6bd5d37084aada4be5a686876ce74","IsNew":true}]
     * lsFixMethodParam : [{"Type":{"Sort":20,"Name":"修复方法","ID":"d7ea23e908a7461c8ce9c1e2bd07d925","IsNew":true},"TypeId":"d7ea23e908a7461c8ce9c1e2bd07d925","EnterpriseId":null,"Value":null,"Remark":null,"InputTime":"/Date(1536829576327)/","Name":"修复方法1","ID":"0172b6e8cbbe43f49a078390d9a09a00","IsNew":true}]
     */

    private ModWorkOrderBean modWorkOrder;
    private List<LsFaultTypeParamBean> lsFaultTypeParam;
    private List<LsFixMethodParamBean> lsFixMethodParam;

    public ModWorkOrderBean getModWorkOrder() {
        return modWorkOrder;
    }

    public void setModWorkOrder(ModWorkOrderBean modWorkOrder) {
        this.modWorkOrder = modWorkOrder;
    }

    public List<LsFaultTypeParamBean> getLsFaultTypeParam() {
        return lsFaultTypeParam;
    }

    public void setLsFaultTypeParam(List<LsFaultTypeParamBean> lsFaultTypeParam) {
        this.lsFaultTypeParam = lsFaultTypeParam;
    }

    public List<LsFixMethodParamBean> getLsFixMethodParam() {
        return lsFixMethodParam;
    }

    public void setLsFixMethodParam(List<LsFixMethodParamBean> lsFixMethodParam) {
        this.lsFixMethodParam = lsFixMethodParam;
    }

    public static class ModWorkOrderBean {
        @Override
        public String toString() {
            return "ModWorkOrderBean{" +
                    "SpFaultTypeId=" + SpFaultTypeId +
                    ", FaultPlace=" + FaultPlace +
                    ", SpRepairMethodId=" + SpRepairMethodId +
                    ", RepairContent=" + RepairContent +
                    '}';
        }

        /**
         * SpFaultTypeId : null
         * FaultPlace : null
         * SpRepairMethodId : null
         * RepairContent : null
         */

        private String SpFaultTypeId;
        private String FaultPlace;
        private String SpRepairMethodId;
        private String RepairContent;

        public String getSpFaultTypeId() {
            return SpFaultTypeId;
        }

        public void setSpFaultTypeId(String SpFaultTypeId) {
            this.SpFaultTypeId = SpFaultTypeId;
        }

        public String getFaultPlace() {
            return FaultPlace;
        }

        public void setFaultPlace(String FaultPlace) {
            this.FaultPlace = FaultPlace;
        }

        public String getSpRepairMethodId() {
            return SpRepairMethodId;
        }

        public void setSpRepairMethodId(String SpRepairMethodId) {
            this.SpRepairMethodId = SpRepairMethodId;
        }

        public String getRepairContent() {
            return RepairContent;
        }

        public void setRepairContent(String RepairContent) {
            this.RepairContent = RepairContent;
        }
    }

    public static class LsFaultTypeParamBean {
        /**
         * Type : {"Sort":10,"Name":"故障类型","ID":"6cf241f6c6f74b3ea8d7d7521df8af63","IsNew":true}
         * TypeId : 6cf241f6c6f74b3ea8d7d7521df8af63
         * EnterpriseId : null
         * Value : null
         * Remark : null
         * InputTime : /Date(1536828977747)/
         * Name : 故障类型1
         * ID : dae6bd5d37084aada4be5a686876ce74
         * IsNew : true
         */

        private TypeBean Type;
        private String TypeId;
        private Object EnterpriseId;
        private Object Value;
        private Object Remark;
        private String InputTime;
        private String Name;
        private String ID;
        private boolean IsNew;

        public TypeBean getType() {
            return Type;
        }

        public void setType(TypeBean Type) {
            this.Type = Type;
        }

        public String getTypeId() {
            return TypeId;
        }

        public void setTypeId(String TypeId) {
            this.TypeId = TypeId;
        }

        public Object getEnterpriseId() {
            return EnterpriseId;
        }

        public void setEnterpriseId(Object EnterpriseId) {
            this.EnterpriseId = EnterpriseId;
        }

        public Object getValue() {
            return Value;
        }

        public void setValue(Object Value) {
            this.Value = Value;
        }

        public Object getRemark() {
            return Remark;
        }

        public void setRemark(Object Remark) {
            this.Remark = Remark;
        }

        public String getInputTime() {
            return InputTime;
        }

        public void setInputTime(String InputTime) {
            this.InputTime = InputTime;
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

        public static class TypeBean {
            /**
             * Sort : 10
             * Name : 故障类型
             * ID : 6cf241f6c6f74b3ea8d7d7521df8af63
             * IsNew : true
             */

            private int Sort;
            private String Name;
            private String ID;
            private boolean IsNew;

            public int getSort() {
                return Sort;
            }

            public void setSort(int Sort) {
                this.Sort = Sort;
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
    }

    public static class LsFixMethodParamBean {
        /**
         * Type : {"Sort":20,"Name":"修复方法","ID":"d7ea23e908a7461c8ce9c1e2bd07d925","IsNew":true}
         * TypeId : d7ea23e908a7461c8ce9c1e2bd07d925
         * EnterpriseId : null
         * Value : null
         * Remark : null
         * InputTime : /Date(1536829576327)/
         * Name : 修复方法1
         * ID : 0172b6e8cbbe43f49a078390d9a09a00
         * IsNew : true
         */

        private TypeBeanX Type;
        private String TypeId;
        private Object EnterpriseId;
        private Object Value;
        private Object Remark;
        private String InputTime;
        private String Name;
        private String ID;
        private boolean IsNew;

        public TypeBeanX getType() {
            return Type;
        }

        public void setType(TypeBeanX Type) {
            this.Type = Type;
        }

        public String getTypeId() {
            return TypeId;
        }

        public void setTypeId(String TypeId) {
            this.TypeId = TypeId;
        }

        public Object getEnterpriseId() {
            return EnterpriseId;
        }

        public void setEnterpriseId(Object EnterpriseId) {
            this.EnterpriseId = EnterpriseId;
        }

        public Object getValue() {
            return Value;
        }

        public void setValue(Object Value) {
            this.Value = Value;
        }

        public Object getRemark() {
            return Remark;
        }

        public void setRemark(Object Remark) {
            this.Remark = Remark;
        }

        public String getInputTime() {
            return InputTime;
        }

        public void setInputTime(String InputTime) {
            this.InputTime = InputTime;
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

        public static class TypeBeanX {
            /**
             * Sort : 20
             * Name : 修复方法
             * ID : d7ea23e908a7461c8ce9c1e2bd07d925
             * IsNew : true
             */

            private int Sort;
            private String Name;
            private String ID;
            private boolean IsNew;

            public int getSort() {
                return Sort;
            }

            public void setSort(int Sort) {
                this.Sort = Sort;
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
    }
}
