package com.ynzhxf.nd.xyfirecontrolapp.network;


import com.ynzhxf.nd.xyfirecontrolapp.bean.PagingBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.TreeGridBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.charge.ChargeChartsDetailsBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.charge.ChargeChartsHistoryAlarmListBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.charge.ChargeChartsIndexEventData;
import com.ynzhxf.nd.xyfirecontrolapp.bean.charge.ChargeExtraContentBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.charge.ChargePieChartsDataBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.ChargeChildrenProjectListBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.CompanyIndexEventBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.NewsBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.OverviewMessageCallBackBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.OwnerIndexSixDataBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.PLVideoModelBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionTaskHomeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.CompanyBackFillParamsBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.MainOwnerMarkCountBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.MaintenListAllBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.OwnerImpowerListBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.OwnerWorkOrderDetailsBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.message.UserMessagePushLogBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.AlarmLogBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.LabelNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectSystemBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.RealDataLogBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.VideoChannelBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireAlarmHostBuildInfoCountBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireAlarmPointBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.platform.LoginInfoBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.share.FileShareFileTypeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.share.FileShareMyFileBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitInterface {
    //============================================Platform     系统相关交互信息=====================================================================

    /**
     * 获取服务器的APP版本号
     *
     * @return
     */
    @POST(URLConstant.URL_VERSION_CHECK)
    Observable<Map<String, String>> getAppCheckVersion();


    /**
     * 获取平台的登陆令牌
     *
     * @return
     */
    @POST(URLConstant.URL_LOGIN_KEY)
    Observable<ResultBean<String, String>> loginKeyGet();

    /**
     * 登陆
     *
     * @param DeviceUUID     设备ID
     * @param DevicePlatform 设备类型
     * @param UserName       用户名
     * @param UserPwd        用户密码
     * @return
     */
    @POST(URLConstant.URL_LOGIN)
    Observable<ResultBean<LoginInfoBean, Map<String, String>>> login(@Query("DeviceUUID") String DeviceUUID, @Query("DevicePlatform") String DevicePlatform, @Query("UserName") String UserName, @Query("UserPwd") String UserPwd, @Query("Key") String key, @Query("Code") String Code);

    /**
     * 检测Token是否有效
     *
     * @param DeviceUUID     设备ID
     * @param DevicePlatform 设备类型
     * @param UserName       用户名
     * @param token          令牌
     * @return
     */
    @POST(URLConstant.URL_CHECK_TOKEN)
    Observable<ResultBean<LoginInfoBean, Map<String, String>>> checkLogin(@Query("DeviceUUID") String DeviceUUID, @Query("DevicePlatform") String DevicePlatform, @Query("UserName") String UserName, @Query("Token") String token);


    /**
     * 退出登陆
     *
     * @param token 令牌
     * @return
     */
    @POST(URLConstant.URL_loginOut)
    Observable<ResultBean<String, String>> loginOut(@Query("Token") String token);


    /**
     * 用户个人相关信息获取
     *
     * @return
     */
    @POST(URLConstant.URL_USER_INFO)
    Observable<ResultBean<LoginInfoBean, String>> userInfoGet(@Query("Token") String token);

    /**
     * 用户密码修改
     *
     * @param token
     * @param oldPwd
     * @param newPwd
     * @return
     */
    @POST(URLConstant.URL_USER_PWD_CHANGE)
    Observable<ResultBean<String, String>> userPwdChange(@Query("Token") String token, @Query("oldPwd") String oldPwd, @Query("newPwd") String newPwd);

    /**
     * 获取用户是否接受消息推送状态
     *
     * @param token
     * @return
     */
    @POST(URLConstant.URL_USER_PUSH_STATE)
    Observable<ResultBean<Boolean, String>> getUserPushState(@Query("Token") String token);

    /**
     * 设置用户接受消息推送状态
     *
     * @param token
     * @return
     */
    @POST(URLConstant.URL_USER_PUSH_STATE_CHANGE)
    Observable<ResultBean<Boolean, String>> getSettingUserPushState(@Query("Token") String token);


    //================================nodeBase   与平台基础节点数据节点相关的交互信息===============================================================

    /**
     * 获取用户关联的最危险项目排序
     *
     * @param token 令牌
     * @return
     */
    @POST(URLConstant.URL_Dangerours)
    Observable<ResultBean<List<Object[]>, String>> getDangeroursProjectList(@Query("Token") String token);

    /**
     * 主管部门区域列表
     *
     * @param token 令牌
     * @return
     */
    @POST(URLConstant.URL_AREA)
    Observable<ResultBean<List<ProjectNodeBean>, String[]>> getCompententAreaList(@Query("Token") String token);

    /**
     * 主管部门根据区域ID、关键字分页获取项目列表
     *
     * @param PageSize 页码
     * @param token    令牌
     * @param areaID   区域ID
     * @param keyWord  名称关键字
     * @return
     */
    @POST(URLConstant.URL_PPRO_LIST_KEYWORDS)
    Observable<ResultBean<PagingBean<ProjectNodeBean>, String>> getAreaAndKeywordsProjectList(@Query("PageSize") int PageSize, @Query("Token") String token, @Query("AreaID") String areaID, @Query("keyWord") String keyWord);

    /**
     * 获取项目信息
     *
     * @param token 令牌
     * @param proID 项目ID
     * @return
     */
    @POST(URLConstant.URL_BASE_NODE_INOF)
    Observable<ResultBean<ProjectNodeBean, String>> getProjectInfo(@Query("Token") String token, @Query("nodeID") String proID);

    /**
     * 根据县、区ID分页获取项目列表分页
     *
     * @param token
     * @param PageSize    页码
     * @param AreaCoutyID 县区级ID
     * @return
     */
    @POST(URLConstant.URL_AREA_COUNTY_PROLIST)
    Observable<ResultBean<PagingBean<ProjectNodeBean>, String>> getAreaCountyProjectList(@Query("Token") String token, @Query("PageSize") int PageSize, @Query("AreaCoutyID") String AreaCoutyID);


    /**
     * 使用用户名获取用户有权限的项目列表
     *
     * @param username 用户名
     * @return
     */
    @POST(URLConstant.URL_USER_HAS_AUTHORY_PROJECT)
    Observable<ResultBean<List<ProjectNodeBean>, String>> getUserHasAuthoryProject(@Query("Token") String token, @Query("userName") String username);


    //=====================================count   相关数据统计接口===================================================================================
    // 首页获取统计数据
    @POST(URLConstant.URL_USER_MAX_EVENT_COUNT)
    Observable<Map<String, String>> getUserMaxEventCount(@Query("Token") String token);

    /**
     * 取项目的统计和实时报警数量
     *
     * @param token
     * @param proID 项目ID
     * @return
     */
    @POST(URLConstant.URL_PROJECT_EVENT_COUNT)
    Observable<Map<String, String>> getProjectEventCount(@Query("Token") String token, @Query("projectID") String proID);

    /**
     * 设置消防接管状态和维保状态
     *
     * @param token
     * @param proID      项目ID
     * @param confirmPwd 操作密码
     * @param type       操作类型 1.为消防接管  2.项目维保
     * @return 操作结果
     */
    @POST(URLConstant.URL_SETTING_REPAIRE_AND_TOKEN)
    Observable<ResultBean<ProjectNodeBean, String>> settingRepaireAndFireToken(@Query("Token") String token, @Query("proID") String proID, @Query("confirmPwd") String confirmPwd, @Query("type") String type);


    //***************************************************项目信息和相关功能********************************************************************

    /**
     * 根据项目ID，获取视频监控列表
     *
     * @param Token
     * @param projectID 项目ID
     * @return
     */
    @POST(URLConstant.URL_PROJECT_VIDEO_LIST)
    Observable<ResultBean<List<VideoChannelBean>, String>> getProjectVideoListByprojectID(@Query("Token") String Token, @Query("projectID") String projectID);

    /**
     * 注册视频播放通道
     *
     * @param token
     * @param channelID 通道ID
     * @return
     */
    @POST(URLConstant.URL_VIDEO_PLAY)
    Observable<ResultBean<String, String>> getVideoPlayRtmp(@Query("Token") String token, @Query("channelID") String channelID);

    /**
     * 视频播放心跳
     *
     * @param token
     * @param channelID 通道ID
     * @return
     */
    @POST(URLConstant.URL_VIDEO_HEART)
    Observable<ResponseBody> sendVideoPlayheart(@Query("Token") String token, @Query("channelID") String channelID);


    /**
     * 根据用户名和项目ID获取系统列表
     *
     * @param token
     * @param userName 用户名
     * @param proID    项目ID
     * @return
     */
    @POST(URLConstant.URL_PROJECT_SYS_LIST)
    Observable<ResultBean<List<ProjectSystemBean>, String>> getProjectSystemListByUserNameAndProID(@Query("Token") String token, @Query("userName") String userName, @Query("proID") String proID);

    /**
     * 根据系统ID获取该系统下的设备和标签列表(一般系统)
     *
     * @param token
     * @param proSysID 系统ID
     * @return
     */
    @POST(URLConstant.URL_EQUIPMENT_LABEL)
    Observable<ResultBean<List<TreeGridBean<LabelNodeBean>>, String>> getAppGetEquipmentAndLabel(@Query("Token") String token, @Query("proSysID") String proSysID);

    /**
     * 标签写值安全验证操作接口
     *
     * @param token
     * @param labelID    标签ID
     * @param confirmPwd 确认密码
     * @return
     */
    @POST(URLConstant.URL_LABEL_CONTROL)
    Observable<ResultBean<String, String>> settingLabelWriteValue(@Query("Token") String token, @Query("labelID") String labelID, @Query("confirmPwd") String confirmPwd);

    /**
     * 获取标签的历史数据记录
     *
     * @param token
     * @param labelID 标签ID
     * @return
     */
    @POST(URLConstant.URL_LABEL_RECORD_INFO)
    Observable<ResultBean<List<RealDataLogBean>, String>> getLabelTwentyFourHoursDataLog(@Query("Token") String token, @Query("labelID") String labelID, @Query("startTime") String startTime
            , @Query("endTime") String endTime);


    /**
     * 获取标签的近30天 历史记录数量统计
     *
     * @param token
     * @param labelID 标签ID
     * @return
     */
    @POST(URLConstant.URL_LABEL_INFO_COUNT)
    Observable<Map<String, String>> getLabelInforCount(@Query("Token") String token, @Query("labelID") String labelID);

    /**
     * 获取火灾报警主机列表
     *
     * @param token
     * @param proID
     * @return
     */
    @POST(URLConstant.URL_FIRE_ALRM_HOST)
    Observable<ResultBean<List<FireAlarmHostBuildInfoCountBean>, String>> getFireAlarmHostList(@Query("Token") String token, @Query("proSysID") String proID);

    /**
     * 获取主机下的建筑物列表
     *
     * @param token
     * @param proID  系统ID
     * @param hostID 主机ID
     * @return
     */
    @POST(URLConstant.URL_FIRE_ALRM_HOST_BUILD)
    Observable<ResultBean<FireAlarmHostBuildInfoCountBean, String>> getFireAalarmHostBuildList(@Query("Token") String token, @Query("proSysID") String proID, @Query("hostID") String hostID);

    /**
     * 火灾报警楼层内的探头点详细信息
     *
     * @param token
     * @param ProjectSystemID 火灾报警系统ID
     * @param FireHostId      主机ID
     * @param BuildName       建筑名称
     * @param floor           楼层名称
     * @return
     */
    @POST(URLConstant.URL_FIRE_ALRM_FLOOR_POINT_INFO)
    Observable<ResultBean<List<FireAlarmPointBean>, String>> getFireAlarmPointList(@Query("Token") String token, @Query("ProjectSystemID") String ProjectSystemID, @Query("FireHostId") String FireHostId, @Query("BuildName") String BuildName, @Query("floor") int floor);


    /**
     * 项目实时报警数据列表
     *
     * @param token
     * @param proID 项目ID
     * @return
     */
    @POST(URLConstant.URL_PROJECT_RealAlarm)
    Observable<ResultBean<Map<String, Object>, String>> getProjectRealAlarmList(@Query("Token") String token, @Query("proID") String proID);

    /**
     * 项目历史报警分页
     *
     * @param token
     * @param proID
     * @param count
     * @param pageSize
     * @return
     */
    @POST(URLConstant.URL_PROJECT_HISTORY_ALARM)
    Observable<ResultBean<PagingBean<AlarmLogBean>, String>> getProjectHistoryAlarmLogPaging(@Query("Token") String token, @Query("proID") String proID, @Query("count") int count, @Query("pageSize") int pageSize, @Query("startTime") String startTime, @Query("endTime") String endTime);


    //========================================公共功能===================================================
    // 新闻列表分页获取
    @POST(URLConstant.URL_NEWS_LIST)
    Observable<ResultBean<PagingBean<NewsBean>, String>> getNewsList(@Query("Token") String token, @Query("pageSize") int pageSize);

    // 增加新闻阅读量并获取新闻的链接地址
    @POST(URLConstant.URL_NEWS_READ_URL)
    Observable<ResultBean<String, String>> addNewsReadCountAndURL(@Query("Token") String token, @Query("ID") String newsId);

    @GET(URLConstant.URL_NEWS_LIST)
    Observable<ResultBean<String,String>> addNewsReadCountAndUrl(@Query("Token") String token,@Query("ID") String newsId);


    //========================================消息==========================================================
    // 分页获取用户消息列表
    @POST(URLConstant.URL_USER_MESSAGE_LOG_LIST)
    Observable<ResultBean<PagingBean<UserMessagePushLogBean>, String>> getUserMessageList(@Query("Token") String token, @Query("pageSize") int pageSize, @Query("MsgTypeId") String MsgTypeId);

    // 获取用户超过指定消息ID的消息
    @POST(URLConstant.URL_USER_MESSAGE_Beyond_Time)
    Observable<ResultBean<List<UserMessagePushLogBean>, String>> getUserMsgLogBeyondTime(@Query("Token") String token, @Query("pushLogID") String pushLogID, @Query("MsgTypeId") String MsgTypeId);

    // 获取用户还未查看的消息数量
    @POST(URLConstant.URL_USER_MESSAGE_NOTSEE_COUNT)
    Observable<ResultBean<Integer, String>> getUserMsgLogNotSeeCount(@Query("Token") String token);

    // 设置用户消息已阅读
    @POST(URLConstant.URL_USER_MESSAGE_SET_SEE)
    Observable<ResultBean<Boolean, String>> settingUserMsgLogSetSee(@Query("Token") String token, @Query("msgLogID") String msgLogID);

    // 用户设置所有未读的消息未已阅读
    @POST(URLConstant.URL_USER_MESSAGE_SET_ALL_SEE)
    Observable<ResultBean<Boolean, String>> settingUserMsgLogSetAllSee(@Query("Token") String token);


    // 根据报警ID获得报警的详细信息
    @POST(URLConstant.URL_ALARM_INFO)
    Observable<ResultBean<AlarmLogBean, String>> getAlarmLogByAlarmID(@Query("Token") String token, @Query("ID") String ID);

    // 忽略报警信息
    @POST(URLConstant.URL_IGNORE_ALARMlOG)
    Observable<ResultBean<AlarmLogBean, String>> setIgnoreAlarmLog(@Query("Token") String token, @Query("ID") String ID);


    //========================================维保==========================================================

    // 获取业主维保工单列表
    @POST(URLConstant.URL_OWNER_MAIN_LIST)
    Observable<ResultBean<MaintenListAllBean, String>> getOwnerMainList(@Query("Token") String token, @Query("projectId") String projectId, @Query("State") String state, @Query("PageIndex") String PageIndex, @Query("PageSize") String PageSize
            , @Query("startTime") String startTime, @Query("endTime") String endTime, @Query("isWorking") String isWorking);

    // 业主获取维保授权列表
    @POST(URLConstant.URL_OWNER_GET_IMPOWER_LIST)
    Observable<ResultBean<List<OwnerImpowerListBean>, String>> getOwnerImpowerList(@Query("Token") String token, @Query("projectID") String projectID);

    // 业主获取维保工单详情
    @POST(URLConstant.URL_OWNER_GET_ORDER_DETAILS)
    Observable<ResultBean<OwnerWorkOrderDetailsBean, String>> getOwnerOrderDetails(@Query("Token") String token, @Query("workOrderId") String workOrderId);

    // 维保公司获取工单列表
    @POST(URLConstant.URL_COMPANY_GET_ORDER_LIST)
    Observable<ResultBean<MaintenListAllBean, String>> getCompanyOrderList(@Query("Token") String token, @Query("projectId") String projectId, @Query("state") String state, @Query("pageIndex") String pageIndex,
                                                                           @Query("pageSize") String pageSize, @Query("systemId") String systemId, @Query("startTime") String startTime, @Query("endTime") String endTime, @Query("isWorking") String isWorking);

    // 维保公司获取维保工单详情
    @POST(URLConstant.URL_COMPANY_ORDER_DETAILS)
    Observable<ResultBean<OwnerWorkOrderDetailsBean, String>> getCompanyOrderDetails(@Query("Token") String token, @Query("workOrderId") String workOrderId);

    // 维保公司获取回填系统参数
    @POST(URLConstant.URL_COMPANY_ORDER_BACKFILL_GET_PARAMS)
    Observable<ResultBean<CompanyBackFillParamsBean, String>> getCompanyBackFillParams(@Query("Token") String token, @Query("workOrderId") String workOrderId);

    // 主管部门获取工单列表
    @POST(URLConstant.URL_CHARGE_GET_ORDER_LIST)
    Observable<ResultBean<MaintenListAllBean, String>> getChargeOrderList(@Query("Token") String token, @Query("projectId") String projectId, @Query("state") String state, @Query("pageIndex") String pageIndex,
                                                                          @Query("pageSize") String pageSize, @Query("systemId") String systemId, @Query("startTime") String startTime, @Query("endTime") String endTime);

    // 主管部门获取维保工单详情
    @POST(URLConstant.URL_CHARGE_GET_ORDER_details)
    Observable<ResultBean<OwnerWorkOrderDetailsBean, String>> getChargeOrderDetails(@Query("Token") String token, @Query("workOrderId") String workOrderId);

    // 文件分享文件分类列表
    @POST(URLConstant.URL_FILE_SHARE_FILE_TYPE_LIST)
    Observable<ResultBean<List<FileShareFileTypeBean>, String>> getFileShareFileType(@Query("Token") String token, @Query("projectId") String projectId, @Query("keyword") String keyword);

    // 文件分享我的文件列表
    @POST(URLConstant.URL_FILE_SHARE_MY_FILE_LIST)
    Observable<ResultBean<List<FileShareMyFileBean>, String>> getFileShareMyFileList(@Query("Token") String token, @Query("projectId") String projectId, @Query("typeId") String typeId, @Query("pageIndex") String pageIndex,
                                                                                     @Query("pageSize") String pageSize, @Query("keyword") String keyword, @Query("startTime") String startTime, @Query("endTime") String endTime);

    // 文件分享共享文件列表
    @POST(URLConstant.URL_FILE_SHARE_ALL_FILE_LIST)
    Observable<ResultBean<List<FileShareMyFileBean>, String>> getFileShareAllFileList(@Query("Token") String token, @Query("projectId") String projectId, @Query("typeId") String typeId, @Query("pageIndex") String pageIndex,
                                                                                      @Query("pageSize") String pageSize, @Query("keyword") String keyword, @Query("startTime") String startTime, @Query("endTime") String endTime);

    // 巡检任务列表
    @POST(URLConstant.URL_INSPECTION_TASK_LIST)
    Observable<ResultBean<List<InspectionTaskHomeBean>, String>> getInspectionTaskHomeList(@Query("Token") String token, @Query("projectId") String projectId, @Query("State") String state, @Query("PageIndex") String pageIndex,
                                                                                           @Query("PageSize") String PageSize, @Query("startTime") String startTime, @Query("endTime") String endTime, @Query("inspectTypeId") String inspectTypeId);

    // 维保公司巡检任务列表
    @POST(URLConstant.URL_COMPANY_INSPECT_GET_TASK_LIST)
    Observable<ResultBean<List<InspectionTaskHomeBean>, String>> getCompanyInspectionTaskHomeList(@Query("Token") String token, @Query("projectId") String projectId, @Query("State") String state, @Query("PageIndex") String pageIndex,
                                                                                                  @Query("PageSize") String PageSize, @Query("startTime") String startTime, @Query("endTime") String endTime, @Query("inspectFrequencyId") String inspectFrequencyId);

    // 业主获取维保公司巡检任务记录列表
    @POST(URLConstant.URL_COMPANY_INSPECT_FOR_OWNER_GET_TASK_LIST)
    Observable<ResultBean<List<InspectionTaskHomeBean>, String>> getCompanyInspectForOwnerTaskHomeList(@Query("Token") String token, @Query("projectId") String projectId, @Query("State") String state, @Query("PageIndex") String pageIndex,
                                                                                                       @Query("PageSize") String PageSize, @Query("startTime") String startTime, @Query("endTime") String endTime, @Query("inspectFrequencyId") String inspectFrequencyId);

    // 业主首页六个模块统计数据
    @POST(URLConstant.URL_OWNER_INDEX_GET_SIX_DATA)
    Observable<ResultBean<List<OwnerIndexSixDataBean>, String>> getOwnerIndexSixData(@Query("Token") String token);

    // 业主首页获取最多故障点
    @POST(URLConstant.URL_OWNER_INDEX_GET_MORE_ERROR_POINT)
    Observable<ResultBean<List<List<String>>, String>> getOwnerIndexMorePoint(@Query("Token") String token);

    // 业主首页获取待巡检项目列表
    @POST(URLConstant.URL_OWNER_INDEX_GET_INSPECT_PROJECT)
    Observable<ResultBean<List<InspectionTaskHomeBean>, String>> getOwnerIndexInspectProject(@Query("Token") String token);

    // 主管部门获取维保工单状态统计
    @POST(URLConstant.URL_OWNER_GET_MARK_COUNT)
    Observable<ResultBean<List<MainOwnerMarkCountBean>, String>> getCompanyOrderStateCount(@Query("Token") String token);

    // 维保公司首页事件统计数量获取
    @POST(URLConstant.URL_COMPANY_INDEX_GET_TOP_EVENT_DATA)
    Observable<ResultBean<CompanyIndexEventBean, String>> getCompanyTopEventCount(@Query("Token") String token);

    // 维保公司首页获取二级项目列表
    @POST(URLConstant.URL_COMPANY_INDEX_GET_TWO_PAGE_PROJECT_LIST)
    Observable<ResultBean<List<ChargeChildrenProjectListBean>, String>> getCompanyTwoPageProject(@Query("Token") String token, @Query("type") String type);

    /// 维保公司首页待巡检任务列表
    @POST(URLConstant.URL_COMPANY_INDEX_GET_INSPECT_LIST)
    Observable<ResultBean<List<InspectionTaskHomeBean>, String>> getCompanyInspectOrderList(@Query("Token") String token, @Query("projectId") String projectId, @Query("State") String State, @Query("PageSize") String PageSize);

    /// 获取视频直播会议流列表
    @POST(URLConstant.URL_OWNER_GET_VIDEO_URL_LIST)
    Observable<ResultBean<List<PLVideoModelBean>, String>> getVideoLiveUrlList(@Query("Token") String token);

    /// 获取概览消息详情
    @POST(URLConstant.URL_OVERVIEW_MESSAGE_DETAILS)
    Observable<ResultBean<OverviewMessageCallBackBean, String>> getOverviewMessageDetails(@Query("Token") String token, @Query("ID") String id);

    /// 获取主管部门饼状统计图列表数据
    @POST(URLConstant.URL_GET_CHARGE_CURRENT_ALARM_PIE_DATA)
    Observable<ResultBean<ChargePieChartsDataBean, ChargeExtraContentBean>> getChargeCurrentPieData(@Query("Token") String token);

    /// 获取主管部门首页事件数据
    @POST(URLConstant.URL_GET_CHARGE_CHARTS_GET_EVENT_DATA)
    Observable<ResultBean<ChargeChartsIndexEventData, String>> getChargeIndexEventData(@Query("Token") String token);

    // 获取主管部门历史报警统计图列表数据
    @POST(URLConstant.URL_GET_CHARGE_CHARTS_GET_HISTORY_DATA)
    Observable<ResultBean<ChargeChartsHistoryAlarmListBean, ChargeExtraContentBean>> getChargeChartsHistoryAlarmData(@Query("Token") String token, @Query("Days") String days);

    //URL_GET_CHARGE_CHARTS_GET_PROJECT_DETAILS
    // 获取主管部门历史报警项目列表项目详情
    @POST(URLConstant.URL_GET_CHARGE_CHARTS_GET_PROJECT_DETAILS)
    Observable<ResultBean<ChargeChartsDetailsBean, String>> getChargeChartsProjectDetailsData(@Query("Token") String token, @Query("ID") String ID);
}
