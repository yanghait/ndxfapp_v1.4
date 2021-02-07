package com.ynzhxf.nd.xyfirecontrolapp.bean.inspection;


/**
 * author hbzhou
 * date 2019/1/23 17:48
 */
public class InspectionSystemListBean {

    /**
     * ID : 1
     * Name : 消防给水系统
     * Result : null
     * ResultShow : -
     * State : 0
     * StateShow : 待巡检
     */

    private String ID;
    private String Name;
    private Object Result;
    private String ResultShow;
    private int State;
    private String StateShow;

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

    public Object getResult() {
        return Result;
    }

    public void setResult(Object Result) {
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
}
