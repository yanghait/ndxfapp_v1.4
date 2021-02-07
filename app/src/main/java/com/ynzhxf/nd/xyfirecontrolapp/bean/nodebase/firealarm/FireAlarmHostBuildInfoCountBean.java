package com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm;

import com.ynzhxf.nd.xyfirecontrolapp.bean.BaseDataBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 火灾报警系统主机信息
 * Created by nd on 2018-07-25.
 */

public class FireAlarmHostBuildInfoCountBean extends BaseDataBean {
    public FireAlarmHostBuildInfoCountBean()
    {
        this.BuildList = new ArrayList<FireAlarmHostBuildCountBean>();
    }
    /// <summary>
    /// 火灾报警主机对象
    /// </summary>
    private FireControlHostObjBean HostObj;

    /// <summary>
    /// 建筑列表
    /// </summary>
    private List<FireAlarmHostBuildCountBean> BuildList;

    public FireControlHostObjBean getHostObj() {
        return HostObj;
    }

    public void setHostObj(FireControlHostObjBean hostObj) {
        HostObj = hostObj;
    }

    public List<FireAlarmHostBuildCountBean> getBuildList() {
        return BuildList;
    }

    public void setBuildList(List<FireAlarmHostBuildCountBean> buildList) {
        BuildList = buildList;
    }
}
