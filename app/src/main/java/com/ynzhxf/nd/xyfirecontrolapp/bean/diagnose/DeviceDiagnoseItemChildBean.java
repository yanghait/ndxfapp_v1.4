package com.ynzhxf.nd.xyfirecontrolapp.bean.diagnose;


/**
 * author hbzhou
 * date 2019/7/15 14:35
 */
public class DeviceDiagnoseItemChildBean {

    /**
     * ID : 31321088f9974d80976a9ca5b5aceae5
     * Name : 室外消火栓管网压力
     * DianoseTypeDesc : 室外消火栓系统是否失效
     * ResultType : 1
     * Result : 室外消火栓管网压力正常
     * Cause : null
     * Solution : null
     * ValueJson : [{"id":"464fcf87437445eb8fa387ce7e8d28d9","name":"室外消火栓给水系统-公共数据-室外消火栓泵出口压力","value":"0.35MPa"}]
     * DiagnoseTime : 2019-07-15 14:09:22
     */

    private String ID;
    private String Name;
    private String DianoseTypeDesc;
    private int ResultType;
    private String Result;
    private Object Cause;
    private Object Solution;
    private String ValueJson;
    private String DiagnoseTime;

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

    public String getDianoseTypeDesc() {
        return DianoseTypeDesc;
    }

    public void setDianoseTypeDesc(String DianoseTypeDesc) {
        this.DianoseTypeDesc = DianoseTypeDesc;
    }

    public int getResultType() {
        return ResultType;
    }

    public void setResultType(int ResultType) {
        this.ResultType = ResultType;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String Result) {
        this.Result = Result;
    }

    public Object getCause() {
        return Cause;
    }

    public void setCause(Object Cause) {
        this.Cause = Cause;
    }

    public Object getSolution() {
        return Solution;
    }

    public void setSolution(Object Solution) {
        this.Solution = Solution;
    }

    public String getValueJson() {
        return ValueJson;
    }

    public void setValueJson(String ValueJson) {
        this.ValueJson = ValueJson;
    }

    public String getDiagnoseTime() {
        return DiagnoseTime;
    }

    public void setDiagnoseTime(String DiagnoseTime) {
        this.DiagnoseTime = DiagnoseTime;
    }
}
