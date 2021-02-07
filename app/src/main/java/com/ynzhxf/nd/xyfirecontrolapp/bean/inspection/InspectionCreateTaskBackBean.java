package com.ynzhxf.nd.xyfirecontrolapp.bean.inspection;

public class InspectionCreateTaskBackBean {

    /**
     * Name : 每日巡查
     * InspectItemTypeIds : 1b509542b3714d9a8961181a6f908f20,49c49e55679b4d09b5f07bd4b704d7ac,54a9e58022114da191863b263869ecc1,87c16824db4945fdbc2c3945b7c1cdaa,a52192e114c845bebb069bd133ea1bcb,c3ad4f76ca34457cbb2d538eb7ed4e9d
     * ID : aa697273780a4e558330d509ee2862ac
     * IsNew : true
     */

    private String Name;
    private String InspectItemTypeIds;
    private String ID;
    private boolean IsNew;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getInspectItemTypeIds() {
        return InspectItemTypeIds;
    }

    public void setInspectItemTypeIds(String InspectItemTypeIds) {
        this.InspectItemTypeIds = InspectItemTypeIds;
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
