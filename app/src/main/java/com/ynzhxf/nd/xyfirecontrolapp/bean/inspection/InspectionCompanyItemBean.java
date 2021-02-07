package com.ynzhxf.nd.xyfirecontrolapp.bean.inspection;


/**
 * author hbzhou
 * date 2019/1/24 09:49
 */
public class InspectionCompanyItemBean {

    /**
     * ID : 02cc5ae294d44b41aeda1ba2b9bc23e1
     * Name : 检查消防应急灯具的工作状态
     * InspectorName :
     * Result : null
     * ResultShow : -
     * State : 0
     * StateShow : 待巡检
     * OverTimeShow :
     */

    private String ID;
    private String Name;
    private String InspectorName;
    private String Result;
    private String ResultShow;
    private int State;
    private String StateShow;
    private String OverTimeShow;

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

    public String getInspectorName() {
        return InspectorName;
    }

    public void setInspectorName(String InspectorName) {
        this.InspectorName = InspectorName;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String Result) {
        this.Result = Result;
    }

    public String getResultShow() {
        return ResultShow;
    }

    public void setResultShow(String ResultShow) {
        this.ResultShow = ResultShow;
    }

    public int getState() {
        return State;
    }

    public void setState(int State) {
        this.State = State;
    }

    public String getStateShow() {
        return StateShow;
    }

    public void setStateShow(String StateShow) {
        this.StateShow = StateShow;
    }

    public String getOverTimeShow() {
        return OverTimeShow;
    }

    public void setOverTimeShow(String OverTimeShow) {
        this.OverTimeShow = OverTimeShow;
    }
}
