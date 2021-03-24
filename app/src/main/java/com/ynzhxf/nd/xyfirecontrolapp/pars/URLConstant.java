package com.ynzhxf.nd.xyfirecontrolapp.pars;


import android.os.Build;

import com.ynzhxf.nd.xyfirecontrolapp.BuildConfig;

/**
 * 网络请求参数
 * http://www.ynzhxf.com:8181/
 * http://192.168.100.151:8556/
 * http://www.qjzhxfy.com:8181
 * https://www.ynzhxf.com:8188
 * 贵州兴义：http://fire.jinzhoucloud.com
 */

public class URLConstant {
    //    public static final String URL_BASE1 = "http://fire.jinzhoucloud.com";//http://192.168.50.153
    public static final String URL_BASE1 = BuildConfig.API_URL;
    public static final String URL_BASE = URL_BASE1 + "/";


    /**
     * 最版本获取地址
     */
    public static final String URL_VERSION_CHECK = "NoAuthorApp/CheckAppVersion";

    /**
     * 登陆令牌获取
     */
    public static final String URL_LOGIN_KEY = "NoAuthorApp/AppLoginIdentifyKey";

    /**
     * 获取登陆验证的图片
     */
    public static final String URL_LOGIN_CODE = "NoAuthorApp/GetValidateCode";

    /**
     * 登录
     */
    public static final String URL_LOGIN = "NoAuthorApp/AppLogin";

    /**
     * 检测用户token
     */
    public static final String URL_CHECK_TOKEN = "NoAuthorApp/AppLoginCheck";

    /**
     * 获取用户相关的信息
     */
    public static final String URL_USER_INFO = "CommonApp/UserAndOrgInfo";

    /**
     * 获取用户是否接受消息推送
     */
    public static final String URL_USER_PUSH_STATE = "CommonApp/UserReceiveState";

    /**
     * 设置用户是否接受消息推送
     */
    public static final String URL_USER_PUSH_STATE_CHANGE = "CommonApp/UserReceiveMsgChange";

    //用户密码修改
    public static final String URL_USER_PWD_CHANGE = "CommonApp/UserPwdChange";


    /**
     * 退出登录
     */
    public static final String URL_loginOut = "CommonApp/AppLoginOut";


    /**
     * 获取当前登陆用户首页实时报警的统计和历史事件的统计
     */
    public static final String URL_USER_MAX_EVENT_COUNT = "CommonApp/UserTotalEventTypeAsy";

    /**
     * 获取项目的实时报警数量和事件数量统计
     */
    public static final String URL_PROJECT_EVENT_COUNT = "CommonApp/ProjectTotalEventTypeAsy";

    /**
     * 获取全局基础节点信息地址
     */
    public static final String URL_BASE_NODE_INOF = "CommonApp/BaseNodeInfoByID";//原来的获取项目信息CommonApp/BaseNodeInfoByID

    /**
     * 设置项目消防接管和维保状态
     */
    public static final String URL_SETTING_REPAIRE_AND_TOKEN = "CommonApp/ProjectFireRepairSetting";

    // 主管部门区域列表
    public static final String URL_AREA = "CompententApp/CompentAreaAuthor";

    // 根据县、区ID分页获取项目列表
    public static final String URL_AREA_COUNTY_PROLIST = "CompententApp/AreaCountyProjectPaging";

    // 根据区域ID、关键字分页获取项目列表
    public static final String URL_PPRO_LIST_KEYWORDS = "CompententApp/AreaProjectPagingKeyName";


    //获取用户有权限的项目列表
    public static final String URL_USER_HAS_AUTHORY_PROJECT = "CommonApp/ProjectsListByUserName";

    // 获取用户关联的最危险项目排序
    public static final String URL_Dangerours = "CommonApp/DangeroursUserProject";

    //获取项目视频监控列表
    public static final String URL_PROJECT_VIDEO_LIST = "CommonApp/RealVideoInfo";

    //获取监控的RTMP播放地址
    public static final String URL_VIDEO_PLAY = "CommonApp/ChannelVideoPlayRtmp";

    //视屏播放心跳
    public static final String URL_VIDEO_HEART = "CommonApp/APPChannelPlayHeart";

    // 获取项目下的系统列表
    public static final String URL_PROJECT_SYS_LIST = "CommonApp/ProjectSystemListByUserNameAndProID";


    // 获取普通系统实时数据
    public static final String URL_EQUIPMENT_LABEL = "CommonApp/AppGetEquipmentAndLabel";

    //标签控制操作安全验证
    public static final String URL_LABEL_CONTROL = "CommonApp/AppWriteValue";

    //标签历史数据记录
    public static final String URL_LABEL_RECORD_INFO = "CommonApp/LabelTwentyFourHoursDataLog";

    //获取标签历史数据记录数据统计
    public static final String URL_LABEL_INFO_COUNT = "CommonApp/LabelStateInfo";


    //获取火灾报警主机列表
    public static final String URL_FIRE_ALRM_HOST = "CommonApp/AppFireAlarmProSystemRealDataBuildFloor";

    //获取火灾报警主机控制范围内的建筑列表
    public static final String URL_FIRE_ALRM_HOST_BUILD = "CommonApp/FireAlarmFloorInfo";


    //获取火灾报警主机楼层探头点详细信息
    public static final String URL_FIRE_ALRM_FLOOR_POINT_INFO = "CommonApp/FireAlarmFloorPoints";

    // 项目实时报警
    public static final String URL_PROJECT_RealAlarm = "CommonApp/AppGetRealAlarmByProID";

    // 项目历史报警分页
    public static final String URL_PROJECT_HISTORY_ALARM = "CommonApp/AppGetProHistoryAlarmLogPaging";

    // 新闻列表获取地址
    public static final String URL_NEWS_LIST = "CommonApp/UserRelationNews";

    //新闻内容详情页
    public static final String URL_NEWS_READ_URL = "CommonApp/NewSeeCountAdd";


    //获取用户消息列表
    public static final String URL_USER_MESSAGE_LOG_LIST = "CommonApp/UserMsgLogList";

    //获取用户超过指定消息ID的消息
    public static final String URL_USER_MESSAGE_Beyond_Time = "CommonApp/UserMsgLogBeyondTime";

    //获取用户还未查看的消息数量
    public static final String URL_USER_MESSAGE_NOTSEE_COUNT = "CommonApp/UserMsgLogNotSee";

    //设置用户消息为已阅读
    public static final String URL_USER_MESSAGE_SET_SEE = "CommonApp/UserMsgLogSetSee";

    //设置用户消息为已阅读
    public static final String URL_USER_MESSAGE_SET_ALL_SEE = "CommonApp/UserMsgLogSetAllSee";


    //使用报警ID，获取报警的详情信息
    public static final String URL_ALARM_INFO = "CommonApp/AlarmLogByAlarmID";

    //忽略报警信息
    public static final String URL_IGNORE_ALARMlOG = "CommonApp/IgnoreAlarmLog";


    //获取业主维保工单列表
    public static final String URL_OWNER_MAIN_LIST = "api/MaintenanceApi/EnterpriseApi/GetWorkOrderList";

    //获取业主维保首页各个工单状态数量
    public static final String URL_OWNER_GET_MARK_COUNT = "/api/MaintenanceApi/CommonApi/GetWorkOrderCountByProjectId";

    //业主维保工单催单
    public static final String URL_OWNER_GET_REMINDER = "/api/MaintenanceApi/EnterpriseApi/Reminder";

    //业主维保工单终止工单
    public static final String URL_OWNER_FINISH_ORDER = "/api/MaintenanceApi/EnterpriseApi/SaveEndWorkOrder";

    //业主维保获取工单审核页面数据
    public static final String URL_OWNER_AUDIT_ORDER = "/api/MaintenanceApi/EnterpriseApi/GetEvaluateData";

    //业主维保评价工单
    public static final String UEL_OWNER_EVALUATE_ORDER = "/api/MaintenanceApi/EnterpriseApi/EvaluateWorkOrder";

    //业主维保获取授权系统列表
    public static final String URL_OWNER_GET_IMPOWER_LIST = "/api/MaintenanceApi/EnterpriseApi/GetProjectSystemAuthorizedList";

    // 业主维保创建工单上传图片
    public static final String URL_OWNER_UPLOAD_IMAGE = "/api/MaintenanceApi/CommonApi/Upload";

    // 业主维保创建提交工单
    public static final String URL_OWNER_COMMIT_ORDER = "/api/MaintenanceApi/EnterpriseApi/SaveWorkOrder";

    // 业主获取工单详情
    public static final String URL_OWNER_GET_ORDER_DETAILS = "/api/MaintenanceApi/EnterpriseApi/GetWorkOrderDetail";

    // 业主获取工单终止详情
    public static final String URL_OWNER_ORDER_OVER_DETAILS = "/api/MaintenanceApi/CommonApi/GetEndWorkOrderReason";

    // 业主同意终止工单
    public static final String URL_OWNER_YES_ORDER_OVER = "/api/MaintenanceApi/EnterpriseApi/AgreeEndWorkOrder";


    // 维保公司获取工单列表
    public static final String URL_COMPANY_GET_ORDER_LIST = "/api/MaintenanceApi/CompanyApi/GetWorkOrderList";

    // 维保公司确认工单
    public static final String URL_COMPANY_COMMIT_ORDER = "/api/MaintenanceApi/CompanyApi/ReceiveWorkOrder";

    // 维保公司获取待移交用户
    public static final String URL_COMPANY_HAND_OVER_GET_USER = "/api/MaintenanceApi/CompanyApi/GetUserInfoList";

    // 维保公司确认移交工单
    public static final String URL_COMPANY_HAND_OVER_CONFIRM = "/api/MaintenanceApi/CompanyApi/SaveTransferWorkOrder";

    // 维保公司终止工单
    public static final String URL_COMPANY_TERMINATION_ORDER = "/api/MaintenanceApi/CompanyApi/SaveEndWorkOrder";

    // 维保公司维修工单
    public static final String URL_COMPANY_MAINTAIN_ORDER = "/api/MaintenanceApi/CompanyApi/InRepair";

    // 维保公司挂起工单
    public static final String URL_COMPANY_HANG_UP_ORDER = "/api/MaintenanceApi/CompanyApi/HangUpWorkOrder";

    // 维保公司工单详情
    public static final String URL_COMPANY_ORDER_DETAILS = "/api/MaintenanceApi/CompanyApi/GetWorkOrderDetail";

    // 维保公司工单回填获取系统参数
    public static final String URL_COMPANY_ORDER_BACKFILL_GET_PARAMS = "/api/MaintenanceApi/CommonApi/GetFillBackWorkOrderData";

    // 维保公司工单回填提交
    public static final String URL_COMPANY_BACK_FILL_CONFIRM = "/api/MaintenanceApi/CompanyApi/SaveBackfillWorkOrder";

    // 维保公司获取项目GPS范围
    public static final String URL_COMPANY_GET_GPS = "/api/MaintenanceApi/CommonApi/GetScaleOfProject";

    // 维保公司搜索获取项目列表
    public static final String URL_COMPANY_SEARCH_PROJECT = "/api/MaintenanceApi/CommonApi/GetProjectList";

    // 主管部门获取工单列表
    public static final String URL_CHARGE_GET_ORDER_LIST = "/api/MaintenanceApi/CompetApi/GetWorkOrderList";

    // 主管部门获取工单详情
    public static final String URL_CHARGE_GET_ORDER_details = "/api/MaintenanceApi/CompetApi/GetWorkOrderDetail";


    // 文件分享文件分类列表
    public static final String URL_FILE_SHARE_FILE_TYPE_LIST = "/FireShareApi/GetFileClassList";

    // 文件分享我的文件列表
    public static final String URL_FILE_SHARE_MY_FILE_LIST = "/FireShareApi/GetFileSharingListAll";

    // 文件分享共享文件列表
    public static final String URL_FILE_SHARE_ALL_FILE_LIST = "/FireShareApi/GetFileSharing";

    // 文件分享删除文件
    public static final String URL_FILE_DELETE_ACTION = "/FireShareApi/DelFile";

    // 文件分享文件编辑保存
    public static final String URL_FILE_SHARE_EDIT_SAVE = "/FireShareApi/SaveFileInfo";

    // 文件分享上传文件
    public static final String URL_FILE_SHARE_UPLOAD_FILE = "/FireShareApi/FileUpload";

    // 文件分类删除
    public static final String URL_FILE_TYPE_DELETE = "/FireShareApi/DelClass";

    // 文件分类添加
    public static final String URL_FILE_TYPE_ADD = "/FireShareApi/AddClass";

    // 文件分类更新
    public static final String URL_FILE_TYPE_UPDATE = "/FireShareApi/UpdateClass";

    // 更新文件界面数据
    public static final String URL_FILE_UPDATE_INFO = "/FireShareApi/GetFileInfo";

    // 作战预案
    public static final String URL_OPERATIONAL_PLANS = "/OperationPlanApi/GetOperationalPlanData";

    // 获取消息分类列表
    public static final String URL_GET_MESSAGE_TYPE = "/CommonApp/GetNotificationType";

    // 历史报警生成工单获取系统授权系统列表
    public static final String URL_OWNER_GET_SYSTEM_LIST = "/api/MaintenanceApi/EnterpriseApi/GetProjectSystemAuthedListWithSeletedId";

    // 巡检任务列表获取接口
    public static final String URL_INSPECTION_TASK_LIST = "/api/FireInspectionApi/EnterpriseApi/GetInspectTaskList";

    // 巡检项列表获取
    public static final String URL_INSPECTION_ITEM_LIST_ONE = "/api/FireInspectionApi/EnterpriseApi/GetInspectItemList";

    // 巡检任务巡检项
    public static final String URL_INSPECTION_ITEM_LIST = "/api/FireInspectionApi/EnterpriseApi/GetInspectTaskItemList";

    // 自定义任务添加
    public static final String URL_INSPECTION_CREATE_TASK = "/api/FireInspectionApi/EnterpriseApi/AddFireInspectTask";

    // 获取自定义任务类型
    public static final String URL_INSPECTION_GET_TASK_TYPE = "/api/FireInspectionApi/EnterpriseApi/GetFireInspectTypeList";

    // 获取自定义按钮显示状态
    public static final String URL_INSPECTION_GET_CREATE_TASK_STATE = "CommonApp/BaseNodeInfoWithModuleIsShowByID";

    // 巡检用户列表获取接口
    public static final String URL_INSPECTION_GET_RESPONSIBLE_PERSON = "/api/FireInspectionApi/EnterpriseApi/GetInspectorList";

    // 设置巡检任务负责人
    public static final String URL_INSPECTION_SET_PERSON = "/api/FireInspectionApi/EnterpriseApi/EditFireInspectTask";

    // 巡检记录获取
    public static final String URL_INSPECTION_GET_RECORDS = "/api/FireInspectionApi/EnterpriseApi/GetFireInspectLogList";

    // 巡检项扫码校验接口
    public static final String URL_INSPECTION_QR_CODE_VERIFY = "/api/FireInspectionApi/EnterpriseApi/ItemQrcodeCheck";

    // 巡检结果保存
    public static final String URL_INSPECTION_RESULT_SAVE = "/api/FireInspectionApi/EnterpriseApi/SaveInspection";

    // 巡检上传图片
    public static final String URL_INSPECTION_UPLOAD_IMAGE = "/api/FireInspectionApi/CommonApi/Upload";

    // 巡检区域列表巡检项列表
    public static final String URL_INSPECTION_AREA_POINT_LIST = "/api/FireInspectionApi/EnterpriseApi/GetInspectItemListByInspectAreaId";

    // 巡检区域
    public static final String URL_INSPECTION_AREA_LIST = "/api/FireInspectionApi/EnterpriseApi/GetInspectAreaList";

    //绑定巡检点标签
    public static final String URL_INSPECTION_POINT_BINDIND= "/api/FireInspectionApi/EnterpriseApi/InspectItemRegister";

    // 设置巡检区域负责人
    public static final String URL_INSPECTION_SET_AREA_PERSON = "/api/FireInspectionApi/EnterpriseApi/EditFireInspectArea";

    // 设置防火检查项巡检人接口
    public static final String URL_INSPECTION_SET_FIRE_PERSON = "/api/FireInspectionApi/EnterpriseApi/EditFireInspectItem";

    // 13.巡检设置获取
    public static final String URL_INSPECTION_GET_SETTING_DATA = "/api/FireInspectionApi/EnterpriseApi/GetFireInspectSetup";

    // 巡检设置保存
    public static final String URL_INSPECTION_SETTING_SAVE = "/api/FireInspectionApi/EnterpriseApi/SaveFireInspectSetup";

    // 火灾报警历史记录
    public static final String URL_FIRE_ALARM_HISTORY_INFO = "/CommonApp/GetFirePointThreeDayHistory";

    // 项目统计页信息
    public static final String URL_PROJECT_STATISTICS_INFO = "/ProjectAnalisysApp/ProjectSevenDayCount";

    // 火灾报警主机和探头历史报警数据接口
    public static final String URL_FIRE_ALARM_COMPUTER_INFO = "/ProjectAnalisysApp/FireHostOrPointHistoryRecord";

    // 项目统计详情页
    public static final String URL_PROJECT_STATISTICS_DETAILS = "/ProjectAnalisysApp/ProjectSystemSevenDayCount";


    // 维保公司巡检设置获取
    public static final String URL_COMPANY_INSPECT_SETTING_GET_INFO = "/api/FireInspectionApi/CompanyApi/GetFireInspectSetup";

    // 维保公司巡检设置保存
    public static final String URL_COMPANY_INSPECT_SETTING_SAVE = "/api/FireInspectionApi/CompanyApi/SaveFireInspectSetup";

    // 维保公司巡检类型列表获取接口
    public static final String URL_COMPANY_INSPECT_GET_TYPE = "/api/FireInspectionApi/CompanyApi/GetFireInspectFrequencyList";

    // 维保公司巡检任务列表
    public static final String URL_COMPANY_INSPECT_GET_TASK_LIST = "/api/FireInspectionApi/CompanyApi/GetInspectTaskList";

    // 业主查看维保公司巡检任务列表记录
    public static final String URL_COMPANY_INSPECT_FOR_OWNER_GET_TASK_LIST = "/api/FireInspectionApi/EnterpriseApi/GetMaintenanceInspectTaskList";

    // 维保公司创建任务获取界面数据
    public static final String URL_COMPANY_ADD_TASK_GET_DATA = "/api/FireInspectionApi/CompanyApi/GetInspectTaskAddData";

    // 维保公司巡检任务添加接口
    public static final String URL_COMPANY_INSPECT_ADD_TASK = "/api/FireInspectionApi/CompanyApi/AddFireInspectTask";

    // 维保公司巡检记录获取
    public static final String URL_COMPANY_INSPECT_GET_RECORDS = "/api/FireInspectionApi/CompanyApi/GetFireInspectLogList";

    // 维保公司巡检系统列表获取
    public static final String URL_COMPANY_INSPECT_GET_SYSTEM_LIST = "/api/FireInspectionApi/CompanyApi/GetSystemList";

    // 维保公司获取巡检项列表
    public static final String URL_COMPANY_INSPECTION_GET_ITEM_LIST = "/api/FireInspectionApi/CompanyApi/GetInspectTaskItemList";

    // 维保公司巡检任务巡检范围
    public static final String URL_COMPANY_INSPECTION_GET_GPS = "/api/FireInspectionApi/CommonApi/GetScaleOfProject";

    // 维保公司巡检任务结果保存
    public static final String URL_COMPANY_INSPECTION_RESULT_SAVE = "/api/FireInspectionApi/CompanyApi/SaveInspection";

    // 维保公司巡检结果界面数据获取
    public static final String URL_COMPANY_INSPECTION_RESULT_GET_DATA = "/api/FireInspectionApi/CompanyApi/GetSaveInspectData";

    // 维保公司获取巡检类型列表
    public static final String URL_COMPANY_INSPECTION_TYPE_LIST = "/api/FireInspectionApi/CompanyApi/GetFireInspectFrequencyList";

    // 维保公司获取巡检类型列表供业主
    public static final String URL_COMPANY_INSPECTION_TYPE_LIST_FOR_OWNER = "/api/FireInspectionApi/EnterpriseApi/GetFireInspectFrequencyList";

    // 实时数据系统列表获取实时报警数量和数据监控数量
    public static final String URL_GET_SYSTEM_ALARM_COUNT = "/CommonApp/ProjectSystemListByUserNameAndProID";

    // 实时数据普通数据标签历史趋势添加时段查询
    public static final String URL_GET_STSTEM_HISTORY_TREND_INFO = "/CommonApp/LabelTwentyFourHoursDataLog";

    // 获取消防接管 通讯状态 和维修状态信息
    public static final String URL_GET_ALL_STATE_INFO = "/ProjectAnalisysApp/GetProjectRepaireLog";// /ProjectAnalisysApp/GetProjectFireAlarmPortLog

    // 获取串口连接状态记录
    public static final String URL_GET_ALL_PORT_STATE_INFO = "/ProjectAnalisysApp/GetProjectFireAlarmPortLog";

    // 统计分析最多故障点列表点击记录
    public static final String URL_GET_STATISTICS_ALARM_ALL_ERROR = "/ProjectAnalisysApp/GetDataPointServenDayAlarmLog";

    // 实时数据内点击实时报警
    public static final String URL_GET_REAL_DATA_FOR_SYSTEM_LIST = "/CommonApp/CommonApp/ProjectSystemListByUserNameAndProID";

    // 饼图实例事件点击
    public static final String URL_STATISTICS_LEGEND_CLICK_LIST = "/ProjectAnalisysApp/GetBaseNodeGroupEvent";

    // 实时数据内点击实时报警更换接口
    public static final String URL_GET_REAL_DATA_FOR_SYSTEM_LIST_ONE = "/CommonApp/AppGetRealAlarmByProIDAndProSysID";


    // 统计报表获取业主巡检报表列表
    public static final String URL_GET_INSPECT_LIST_OWNER = "/api/JournalSheetApi/InspectSheetApi/GetEnterpriseInspectionSheet";

    // 统计报表获取业主巡检当月报表
    public static final String URL_GET_INSPECT_MONTH_OWNER = "/api/JournalSheetApi/InspectSheetApi/GenerateEnterpriseInspectTaskPDF";

    // 统计报表获取维保巡检报表列表
    public static final String URL_GET_INSPECT_LIST_COMPANY = "/api/JournalSheetApi/InspectSheetApi/GetCompanyInspectionSheet";

    // 统计报表获取维保巡检报表列表
    public static final String URL_GET_INSPECT_MONTH_COMPANY = "/api/JournalSheetApi/InspectSheetApi/GenerateCompanyInspectTaskPDF";


    // 统计报表获取业主维保报表列表
    public static final String URL_GET_MAINTANCE_LIST_OWNER = "/api/JournalSheetApi/MaintenanceSheetApi/GetEnterpriseMaintenanceSheet";

    // 统计报表获取业主维保当月报表
    public static final String URL_GET_MAINTANCE_MONTH_OWNER = "/api/JournalSheetApi/MaintenanceSheetApi/GenerateEnterpriseWorkOrderPDF";

    // 统计报表获取维保公司维保报表列表
    public static final String URL_GET_MAINTANCE_LIST_COMPANY = "/api/JournalSheetApi/MaintenanceSheetApi/GetCompanyMaintenanceSheet";

    // 统计报表获取维保公司维保报表列表
    public static final String URL_GET_MAINTANCE_MONTH_COMPANY = "/api/JournalSheetApi/MaintenanceSheetApi/GenerateCompanyWorkOrderPDF";


    // 维保工单业主导出工单记录报表
    public static final String URL_GET_MAINTANCE_OWNER_OUT_PDF = "/api/JournalSheetApi/MaintenanceSheetApi/GenerateEnterpriseWorkOrderLogPDF";

    // 维保工单维保公司导出工单记录报表
    public static final String URL_GET_MAINTANCE_COMPANY_OUT_PDF = "/api/JournalSheetApi/MaintenanceSheetApi/GenerateCompanyWorkOrderLogPDF";

    // 业主巡检记录报表
    public static final String URL_GET_INSPECT_OWNER_OUT_PDF = "/api/JournalSheetApi/InspectSheetApi/GenerateEnterpriseInspectLogPDF";

    // 维保公司巡检记录报表
    public static final String URL_GET_INSPECT_COMPANY_OUT_PDF = "/api/JournalSheetApi/InspectSheetApi/GenerateCompanyInspectLogPDF";


    // 巡检首页各状态统计数量
    public static final String URL_INSPECT_HOME_STATE_NUM = "/api/FireInspectionApi/CommonApi/GetInspectTaskCountByProjectId";

    // 业主获取维保公司巡检任务记录
    public static final String URL_INSPECT_TASK_RECORDS_LIST_FOR_COMPANY = "/api/FireInspectionApi/EnterpriseApi/GetMaintenanceFireInspectLogList";

    // 业主查看维保首页巡检首页各状态统计数量
    public static final String URL_INSPECT_HOME_STATE_NUM_FOR_HOME = "/api/FireInspectionApi/EnterpriseApi/GetMaintenanceInspectTaskCount";


    // 巡检首页业主各状态统计数量
    public static final String URL_INSPECT_HOME_STATE_NUM_FOR_OWNER = "/api/FireInspectionApi/EnterpriseApi/GetInspectTaskCountByProjectId";

    // 巡检首页维保公司各状态统计数量
    public static final String URL_INSPECT_HOME_STATE_NUM_FOR_COMPANY = "/api/FireInspectionApi/CompanyApi/GetInspectTaskCountByProjectId";


    // 查看诊断信息
    public static final String URL_DEVICE_DIAGNOSE_GET_INFO = "/AppEquipmentDiagnoseExcute/GetLastTimeProjectDiagnose";

    // 执行诊断操作
    public static final String URL_DEVICE_DIAGNOSE_HOME_GET_INFO = "/AppEquipmentDiagnoseExcute/ExcuteProjectDiagnose";

    // 诊断完成系统详情列表
    public static final String URL_DEVICE_DIAGNOSE_SYSTEM_DETAILS = "/AppEquipmentDiagnoseExcute/GetEquipmentDiagnoseHisInfo";

    // 项目状态查看诊断详情
    public static final String URL_DEVICE_DIAGNOSE_VIEW_DETAILS = "/AppEquipmentDiagnoseExcute/GetProjectDiagnoseHistory";

    // 风险评估获取基础数据
    public static final String URL_RISK_ASSESSMENT_GET_BASICS_DATA = "/api/FireRiskEvaluateApi/EnterpriseApi/GetBasicRiskData";

    // 风险评估获取建筑物列表数据
    public static final String URL_RISK_ASSESSMENT_GET_BUILD_RISK_LOG = "/api/FireRiskEvaluateApi/EnterpriseApi/GetBuildRiskLog";

    // 风险评估手动评估
    public static final String URL_RISK_ASSESSMENT_RISK_EVALUATE = "/api/FireRiskEvaluateApi/EnterpriseApi/RiskEvaluate";


    // 主管部门	首页6个统计模块数据获取接口
    public static final String URL_CHARGE_INDEX_GET_SIX_DATA_COUNT = "/api/StatisticsApi/CompetApi/GetIndexStatistics";

    // 主管部门 	首页项目列表获取接口
    public static final String URL_CHARGE_INDEX_GET_PROJECT_LIST = "/api/StatisticsApi/CompetApi/GetIndexProjectList";

    // 主管部门 获取首页项目统计图
    public static final String URL_CHARGE_INDEX_GET_BAR_CHART_DATA = "/api/StatisticsApi/CompetApi/GetIndexStatisticalChatDataList";

    // 主管部门 获取区域省市区县数据
    public static final String URL_CHARGE_INDEX_GET_AREA_DATA = "/api/StatisticsApi/CompetApi/GetAreaTree";

    // 主管部门 项目分类类型获取
    public static final String URL_CHARGE_INDEX_PROJECT_TYPE = "/api/StatisticsApi/CompetApi/GetAreaProjectDataTypeList";

    // 主管部门 获取分类醒目列表
    public static final String URL_CHARGE_INDEX_PROJECT_LIST_DATA = "/api/StatisticsApi/CompetApi/GetAreaProjectList";

    // 业主首页六个模块统计数据
    public static final String URL_OWNER_INDEX_GET_SIX_DATA = "/api/StatisticsApi/EnterpriseApi/GetIndexStatistics";

    // 业主首页获取最多故障点项目列表
    public static final String URL_OWNER_INDEX_GET_MORE_ERROR_POINT = "/api/StatisticsApi/EnterpriseApi/GetProjectDefaultPointTopTweenty";

    // 业主首页获取待巡检项目列表
    public static final String URL_OWNER_INDEX_GET_INSPECT_PROJECT = "/api/StatisticsApi/EnterpriseApi/GetInspectTaskList";

    // 维保公司首页事件统计数量获取
    public static final String URL_COMPANY_INDEX_GET_TOP_EVENT_DATA = "/api/StatisticsApi/CompanyApi/GetIndexStatistics";

    // 维保公司首页获取二级项目列表
    public static final String URL_COMPANY_INDEX_GET_TWO_PAGE_PROJECT_LIST = "/api/StatisticsApi/CompanyApi/GetMaintenanceProjectList";

    // 维保公司首页待巡检任务列表
    public static final String URL_COMPANY_INDEX_GET_INSPECT_LIST = "/api/FireInspectionApi/CompanyApi/GetInspectTaskList";

    // 业主或主管部门获取视频直播流列表
    public static final String URL_OWNER_GET_VIDEO_URL_LIST = "/api/VideoMeetingApi/CommonApi/GetMeetingList";

    // 获取概览消息详情
    public static final String URL_OVERVIEW_MESSAGE_DETAILS = "/CommonApp/GetOverviewMsgDetail";


    // 主管部门统计模块 获取实时报警饼图统计数据
    public static final String URL_GET_CHARGE_CURRENT_ALARM_PIE_DATA = "api/StatisticsApi/CompetApi/GetAlarmRate";
    // 主管部门统计模块 首页获取事件数量
    public static final String URL_GET_CHARGE_CHARTS_GET_EVENT_DATA = "api/StatisticsApi/CompetApi/GetStatisticIndexData";
    // 主管部门统计 历史报警统计图列表数据
    public static final String URL_GET_CHARGE_CHARTS_GET_HISTORY_DATA = "api/StatisticsApi/CompetApi/GetStatisticsHistoryAlarm";
    // 主管部门统计 历史报警项目列表项目详情
    public static final String URL_GET_CHARGE_CHARTS_GET_PROJECT_DETAILS = "api/StatisticsApi/CompetApi/GetProjectDetailHistoryAlarm";
}
