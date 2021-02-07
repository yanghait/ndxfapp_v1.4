package com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm;

import com.ynzhxf.nd.xyfirecontrolapp.bean.BaseDataBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 建筑对象
 * Created by nd on 2018-07-25.
 */

public class FireAlarmHostBuildCountBean extends BaseDataBean {

    public FireAlarmHostBuildCountBean()
    {
        FloorCountInfoList = new ArrayList<>();
    }

    /// <summary>
    /// 建筑名称
    /// </summary>
    private String BuildName;
    /// <summary>
    /// 楼层火灾报警设备统计
    /// </summary>
    private List<FireAlarmHostFloorCountBean> FloorCountInfoList;

    public String getBuildName() {
        return BuildName;
    }

    public void setBuildName(String buildName) {
        BuildName = buildName;
    }

    public List<FireAlarmHostFloorCountBean> getFloorCountInfoList() {
        return FloorCountInfoList;
    }

    public void setFloorCountInfoList(List<FireAlarmHostFloorCountBean> floorCountInfoList) {
        FloorCountInfoList = floorCountInfoList;
    }
}
