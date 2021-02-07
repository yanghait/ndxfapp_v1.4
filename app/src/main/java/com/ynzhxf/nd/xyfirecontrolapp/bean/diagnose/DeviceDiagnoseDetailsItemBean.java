package com.ynzhxf.nd.xyfirecontrolapp.bean.diagnose;


/**
 * author hbzhou
 * date 2019/4/17 09:45
 */
public class DeviceDiagnoseDetailsItemBean {

    /**
     * ID : 8dc0af3fa24f49e98877a378bcad741f
     * Name : 2电压诊断
     * DianoseTypeDesc : 诊断消火栓系统是否失效
     * ResultType : 1
     * Result : 消火栓泵电压正常
     * Cause : null
     * Solution : null
     * ValueJson :
     * DiagnoseTime : 2019-04-17 09:43:07
     */

    private String ID;
    private String Name;
    private String DianoseTypeDesc;
    private int ResultType;
    private String Result;
    private String Cause;
    private String Solution;
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

    public String getCause() {
        return Cause;
    }

    public void setCause(String Cause) {
        this.Cause = Cause;
    }

    public String getSolution() {
        return Solution;
    }

    public void setSolution(String Solution) {
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
