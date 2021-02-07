package com.ynzhxf.nd.xyfirecontrolapp.bean.diagnose;


import java.util.List;

/**
 * author hbzhou
 * date 2019/4/15 17:05
 */
public class DeviceDiagnoseHomeBean {
    /**
     * ID : b4fe8dd20d154b23890837e6698e6529
     * Name : 室内消火栓系统
     * Score : 100
     * ResultDesc : 系统运行状态良好
     */

    private String ID;
    private String Name;
    private int Score;
    private String ResultDesc;
    private List<DeviceDiagnoseItemChildBean> DiagnoseItemHistoryList;
    private int progressStatus;

    public List<DeviceDiagnoseItemChildBean> getDiagnoseItemHistoryList() {
        return DiagnoseItemHistoryList;
    }

    public void setDiagnoseItemHistoryList(List<DeviceDiagnoseItemChildBean> diagnoseItemHistoryList) {
        DiagnoseItemHistoryList = diagnoseItemHistoryList;
    }

    public int getProgressStatus() {
        return progressStatus;
    }

    public void setProgressStatus(int progressStatus) {
        this.progressStatus = progressStatus;
    }

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

    public int getScore() {
        return Score;
    }

    public void setScore(int Score) {
        this.Score = Score;
    }

    public String getResultDesc() {
        return ResultDesc;
    }

    public void setResultDesc(String ResultDesc) {
        this.ResultDesc = ResultDesc;
    }
}
