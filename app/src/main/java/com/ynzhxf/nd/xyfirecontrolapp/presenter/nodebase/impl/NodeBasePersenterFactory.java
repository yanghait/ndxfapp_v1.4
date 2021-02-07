package com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl;

import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IAlarmLogInfoPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.ICompententAreaCountyProPagingPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.ICompententAreaPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.ICompententSearchProjectPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IDangeroursUserProjectPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IEquipmentLabelPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IFireAlarmBuildListPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IFireAlarmHostListPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IFireAlarmPointListPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.ILabelRecordTweentyHourPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.ILabelWriteValuePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IProjectHistoryAlarmPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IProjectInfoPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IProjectRealAlarmPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IProjectSettingRepaireOrTokenPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IProjectSystemListPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IProjectVideoPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IUserHasAuthoryProjectPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IVideoPlayHeartPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IVideoPlayPersenter;

/**基础节点桥梁实例生成工厂
 * Created by nd on 2018-07-16.
 */

public  class NodeBasePersenterFactory {
    /**
     * 获取精准治理项目列表
     * @param view
     * @return
     */
    public static IDangeroursUserProjectPersenter getDangeroursUserProjectPersenterImpl(IDangeroursUserProjectPersenter.IDangeroursUserProjectView view){
        return new DangerousUserProjectPersenterImpl(view);
    }

    /**
     * 获取主管部门区域列表
     * @param view
     * @return
     */
    public  static ICompententAreaPersenter getCompententAreaPersenterImpl(ICompententAreaPersenter.ICompententAreaView view){
        return new CompententAreaPersenterImpl(view);
    }

    /**
     * 获取项目关键子搜索
     * @param view
     * @return
     */
    public static ICompententSearchProjectPersenter getCompententSearchProjectPersenterImpl(ICompententSearchProjectPersenter.ICompententSearchProjectView view){
        return  new CompententSearchProjectPersenterImpl(view);
    }


    /**
     * 获取项目信息
     * @param view
     * @return
     */
    public static IProjectInfoPersenter getProjectInfoPersenterImpl(IProjectInfoPersenter.IProjectInfoView view){
        return new ProjectInfoPersenterImpl(view);
    }

    /**
     * 获取项目维保和消防接管设置桥梁实现
      * @param view
     * @return
     */
    public static IProjectSettingRepaireOrTokenPersenter getProjectSettingRepaireOrTokenPersenter(IProjectSettingRepaireOrTokenPersenter.IProjectSettingRepaireOrTokenView view){
        return new ProjectSettingRepaireOrTokenPersenterImpl(view);
    }

    /**
     * 获取县区级下的项目列表分页
     * @param view
     * @return
     */
    public  static ICompententAreaCountyProPagingPersenter getCompententAreaCountyProPagingPersenterImpl(ICompententAreaCountyProPagingPersenter.ICompententAreaCountyProPagingView view){
        return new CompententAreaCountyProPagingPersenterImpl(view);
    }

    /**
     * 获取项目的视频列表
     * @param view
     * @return
     */
    public static IProjectVideoPersenter getProjectVideoPersenterImpl(IProjectVideoPersenter.IProjectVideoView view){
        return new ProjectVideoPersenterImpl(view);
    }

    //注册通道，获取播放地址
    public static IVideoPlayPersenter getVideoPlayPersenterImpl(IVideoPlayPersenter.IVideoPlayView view){
        return new VideoPlayPersenterImpl(view);
    }

    //通道心跳
    public  static IVideoPlayHeartPersenter getVideoPlayHeartPersenterImpl(IVideoPlayHeartPersenter.IVideoVideoPlayHeartView view){
        return new VideoPlayHeartPersenterImpl(view);
    }

    /**
     * 获取项目下的系统列表
     * @param view
     * @return
     */
    public static IProjectSystemListPersenter getProjectSystemListPersenterImpl(IProjectSystemListPersenter.IIProjectSystemListView view){
        return new ProjectSystemListPersenterImpl(view);
    }


    /**
     * 获取系统下的设备和标签数据
     * @param view
     * @return
     */
    public static IEquipmentLabelPersenter getEquipmentLabelPersenterImpl(IEquipmentLabelPersenter.IEquipmentLabelView view){
        return new EquipmentLabelPersenterImpl(view);
    }


    /**
     * 获取标签写值桥梁
     * @param view
     * @return
     */
    public static ILabelWriteValuePersenter getLabelWriteValuePersenterImpl(ILabelWriteValuePersenter.ILabelWriteValueView view){
        return new LabelWriteValuePersenterImpl(view);
    }

    /**
     * 获取标签历史数据记录
     * @param view
     * @return
     */
    public static ILabelRecordTweentyHourPersenter getILabelRecordTweentyHourPersenter(ILabelRecordTweentyHourPersenter.ILabelRecordTweentyHourView view){
        return  new LabelRecordTweentyHourPensenterImpl(view);
    }

    /**
     * 获取火灾报警列表
     * @param view
     * @return
     */
    public static IFireAlarmHostListPersenter getFireAlarmHostListPersenterImpl(IFireAlarmHostListPersenter.IFireAlarmHostListView view){
        return new FireAlarmHostListPersenterImpl(view);
    }

    /**
     * 获取火灾报警主机下的建筑物列表
     * @param view
     * @return
     */
    public static IFireAlarmBuildListPersenter getFireAlarmBuildListPersenterImpl(IFireAlarmBuildListPersenter.IFireAlarmBuildListView view){
        return new FireAlarmBuildListPersenterImpl(view);
    }

    /**
     * 获取火灾报警楼层内探头点的信息桥梁
     * @param view
     * @return
     */
    public static IFireAlarmPointListPersenter getFireAlarmPointListPersenter(IFireAlarmPointListPersenter.IFireAlarmPointListView view){
        return new FireAlarmPointListPersenterImpl(view);
    }

    /**
     * 获取项目实时报警数据桥梁
     * @param view
     * @return
     */
    public static IProjectRealAlarmPersenter getProjectRealAlarmPersenter(IProjectRealAlarmPersenter.IProjectRealAlarmView view){
        return new ProjectRealAlarmPersenterImpl(view);
    }

    /**
     * 获取项目的历史报警记录
     * @param view
     * @return
     */
    public static IProjectHistoryAlarmPersenter getProjectHistoryAlarmPersenterImpl(IProjectHistoryAlarmPersenter.IProjectHistoryAlarmView view){
        return new ProjectHistoryAlarmPersenterImpl(view);
    }

    public static IUserHasAuthoryProjectPersenter getUserHasAuthoryProjectPersenterImpl(IUserHasAuthoryProjectPersenter.IUserHasAuthoryProjectView view){
        return new UserHasAuthoryProjectPersenterImpl(view);
    }


    /**
     * 获取报警信息详情
     * @param view
     * @return
     */
    public static IAlarmLogInfoPersenter getAlarmLogInfoImpl(IAlarmLogInfoPersenter.IAlarmLogInfoView view){
        return new AlarmLogInfoPersenterImpl(view);
    }

}
