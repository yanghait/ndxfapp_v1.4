package com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm;

import com.ynzhxf.nd.xyfirecontrolapp.bean.BaseDataBean;

/**
 * 火灾报警系统楼层对象
 * Created by nd on 2018-07-25.
 */

public class FireAlarmHostFloorCountBean extends BaseDataBean {

    public FireAlarmHostFloorCountBean()
    {
        InfoCount = new FireAlarmBuildFloorCountBean();
    }
    /// <summary>
    /// 楼层
    /// </summary>
    private int Floot;

    /// <summary>
    /// 楼层统计信息
    /// </summary>
    private FireAlarmBuildFloorCountBean InfoCount;

    public int getFloot() {
        return Floot;
    }

    public void setFloot(int floot) {
        Floot = floot;
    }

    public FireAlarmBuildFloorCountBean getInfoCount() {
        return InfoCount;
    }

    public void setInfoCount(FireAlarmBuildFloorCountBean infoCount) {
        InfoCount = infoCount;
    }
}
