package com.ynzhxf.nd.xyfirecontrolapp.model.message;

import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.IIgnoreAlarmLogPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.IUserMsgLogNotSeeCountPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.IUserMsgLogSetAllSeePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.IUserMsgLogSetSeePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.IUserPushMsgLogBeyondTimePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.IUserPushMsgLogPersenter;

/**
 *
 * 消息相关数据模型生成工厂
 * Created by nd on 2018-08-02.
 */

public class MessageModelFactory {


    /**
     * 获取用户消息列表
     * @param persenter
     * @return
     */
    public static IUserPushMsgLogPersenter.IUserPushMsgLogModel getUserPushMsgLogModel(IUserPushMsgLogPersenter persenter){
        return new UserPushMsgLogModel(persenter);
    }

    public static IUserPushMsgLogBeyondTimePersenter.IUserPushMsgLogBeyondTimeModel getUserPushMsgLogBeyondTimeModel(IUserPushMsgLogBeyondTimePersenter persenter){
        return new UserPushMsgLogBeyondTimePersenterModel(persenter);
    }

    public static IUserMsgLogNotSeeCountPersenter.IUserMsgLogNotSeeCountModel getUserMsgLogNotSeeCountModel(IUserMsgLogNotSeeCountPersenter persenter){
        return new UserMsgLogNotSeeCountModel(persenter);
    }

    public static IUserMsgLogSetSeePersenter.IUserMsgLogSetSeeModel getUserMsgLogSetSeeModel(IUserMsgLogSetSeePersenter persenter){
        return new UserMsgLogSetSeeModel(persenter);
    }

    public static IUserMsgLogSetAllSeePersenter.IUserMsgLogSetAllSeeModel getUserMsgLogSetAllSeeModel(IUserMsgLogSetAllSeePersenter persenter){
        return new UserMsgLogSetAllSeeModel(persenter);
    }

    public static IIgnoreAlarmLogPersenter.IIgnoreAlarmLogModel getIgnoreAlarmLogModel(IIgnoreAlarmLogPersenter persenter){
        return new IgnoreAlarmLogModel(persenter);
    }
}
