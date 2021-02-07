package com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm;

/**火灾报警主机对象
 * Created by nd on 2018-07-25.
 */

public class FireControlHostObjBean {

    //对主机解释描述
    private String Describe;

    //火灾报警主机状态
    private int HostState;
    //主机ID
    private String HostId;

    //串口状态
    private String CodeTableId;

    public String getDescribe() {
        return Describe;
    }

    public void setDescribe(String describe) {
        Describe = describe;
    }

    public int getHostState() {
        return HostState;
    }

    public void setHostState(int hostState) {
        HostState = hostState;
    }

    public String getHostId() {
        return HostId;
    }

    public void setHostId(String hostId) {
        HostId = hostId;
    }

    public String getCodeTableId() {
        return CodeTableId;
    }

    public void setCodeTableId(String codeTableId) {
        CodeTableId = codeTableId;
    }

    public String getHostStateStr() {
        return HostStateStr;
    }

    public void setHostStateStr(String hostStateStr) {
        HostStateStr = hostStateStr;
    }

    //主机状态
    private String HostStateStr;
}
