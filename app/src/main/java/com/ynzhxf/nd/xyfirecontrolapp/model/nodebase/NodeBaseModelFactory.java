package com.ynzhxf.nd.xyfirecontrolapp.model.nodebase;

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

/**
 * 基础节点数据获取实例生成工厂
 * Created by nd on 2018-07-16.
 */

public class NodeBaseModelFactory {

    /**
     * 获取最危险项目的请求实例
     * @return
     */
    public static IDangeroursUserProjectPersenter.IDangeroursUserProjectModel getDangeroursUserProjectModelImpl(IDangeroursUserProjectPersenter persenter ){
        return new DangeroursUserProjectModel(persenter);
    }

    /**
     * 获取主管部门区域列表数据
     * @param persenter
     * @return
     */
    public static ICompententAreaPersenter.ICompententAreaModel getICompententAreaModel( ICompententAreaPersenter persenter){
        return new CompententAreaModel(persenter);
    }


    /**
     * 主管部门区域ID和关键字获取项目列表分页
     * @param persenter
     * @return
     */

    public static ICompententSearchProjectPersenter.ICompententSearchProjectModel getCompententSearchProjectModel(ICompententSearchProjectPersenter persenter){
        return new CompententSearchProjectModel(persenter);
    }

    /**
     * 获取项目信息数据请求实例
     * @param persenter
     * @return
     */
    public static IProjectInfoPersenter.IProjectInfoModel getProjectInfoModelImpl(IProjectInfoPersenter persenter){
        return  new ProjectInfoModel(persenter);
    }

    /**
     * 获取消防接管和维保设置模型
     * @param persenter
     * @return
     */
    public  static IProjectSettingRepaireOrTokenPersenter.IProjectSettingRepaireOrTokenModel getProjectSettingRepaireOrTokenModel(IProjectSettingRepaireOrTokenPersenter persenter){
        return new ProjectSettingRepaireOrTokenModel(persenter);
    }

    /**
     * 获取项目区域列表分页
     * @param persenter
     * @return
     */
    public static ICompententAreaCountyProPagingPersenter.ICompententAreaCountyProPagingModel getCompententAreaCountyProPagingModelImpl(ICompententAreaCountyProPagingPersenter persenter){
        return new CompententAreaCountyProPagingModel(persenter);
    }

    /**
     * 获取项目视频列表
     * @param persenter
     * @return
     */
    public static IProjectVideoPersenter.IProjectVideoModel getProjectVideoModelImpl(IProjectVideoPersenter persenter){
        return new ProjectVideoModel(persenter);
    }

    /**
     * 注册视频播放通道
     * @param persenter
     * @return
     */
    public static IVideoPlayPersenter.IVideoPlayModel getVideoPlayModel(IVideoPlayPersenter persenter){
        return new VideoPlayModel(persenter);
    }

    /**
     * 视频播放通道心跳
     * @param persenter
     * @return
     */
    public static IVideoPlayHeartPersenter.IVideoPlayHeartModel getVideoPlayHeartModel(IVideoPlayHeartPersenter persenter){
        return new VideoPlayHeartModel(persenter);
    }

    /**
     * 获取项目有权限的系统列表
     * @param persenter
     * @return
     */
    public static IProjectSystemListPersenter.IIProjectSystemListModel getProjectSystemListModel(IProjectSystemListPersenter persenter){
        return new ProjectSystemListModel(persenter);
    }

    /**
     * 获取普通系统的设备和标签列表
     * @param persenter
     * @return
     */
    public static IEquipmentLabelPersenter.IEquipmentLabelModel getEquipmentLabelModel(IEquipmentLabelPersenter persenter){
        return new EquipmentLabelModel(persenter);
    }

    /**
     * 获取对标签的写值操作
     * @param persenter
     * @return
     */
    public static ILabelWriteValuePersenter.ILabelWriteValueModel getLabelWriteValueModel(ILabelWriteValuePersenter persenter){
        return new LabelWriteValueModel(persenter);
    }

    /**
     * 获取标签的历史数据记录
     * @param persenter
     * @return
     */
    public  static ILabelRecordTweentyHourPersenter.ILabelRecordTweentyHourModel getLabelRecordTweentyHourModel(ILabelRecordTweentyHourPersenter persenter){
        return new LabelRecordTweentyHourModel(persenter);
    }

    /**
     * 获取火灾报警主机列表
     * @param persenter
     * @return
     */
    public  static IFireAlarmHostListPersenter.IFireAlarmHostListModel getFireAlarmHostListModel(IFireAlarmHostListPersenter persenter){
        return new FireAlarmHostListModel(persenter);
    }

    /**
     * 获取火灾报警建筑物列表统计
     * @param persenter
     * @return
     */
    public static IFireAlarmBuildListPersenter.IFireAlarmBuildListModel getFireAlarmHostBuildListModel (IFireAlarmBuildListPersenter persenter){
        return new FireAlarmBuildListModel(persenter);
    }

    /**
     * 获取火灾报警楼层探头点数据列表
     * @param persenter
     * @return
     */
    public  static IFireAlarmPointListPersenter.IFireAlarmPointListModel getFireAlarmPointListModel(IFireAlarmPointListPersenter persenter){
        return new FireAlarmPointListModel(persenter);
    }


    /**
     * 获取项目实时报警
     * @param persenter
     * @return
     */
    public static IProjectRealAlarmPersenter.IProjectRealAlarmModel getProjectRealAlarmModel(IProjectRealAlarmPersenter persenter){
        return new ProjectRealAlarmModel(persenter);
    }

    /**
     * 获取项目历史报警数据分页
     * @param persenter
     * @return
     */
    public static IProjectHistoryAlarmPersenter.IProjectHistoryAlarmModel getProjectHistoryAlarmModel(IProjectHistoryAlarmPersenter persenter){
        return new ProjectHistoryAlarmModel(persenter);
    }

    /**
     * 获取用户有权限的项目列表
     * @param persenter
     * @return
     */
    public static IUserHasAuthoryProjectPersenter.IUserHasAuthoryProjectModel getUserHasAuthoryProjectModel(IUserHasAuthoryProjectPersenter persenter){
        return new UserHasAuthoryProjectModel(persenter);
    }


    public static IAlarmLogInfoPersenter.IAlarmLogInfoModel getAlarmLogInfoModel(IAlarmLogInfoPersenter perserter){
        return new AlarmLogInfoModel(perserter);
    }
}
