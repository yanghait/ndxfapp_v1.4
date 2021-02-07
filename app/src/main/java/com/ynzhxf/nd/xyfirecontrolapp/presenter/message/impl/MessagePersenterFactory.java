package com.ynzhxf.nd.xyfirecontrolapp.presenter.message.impl;

import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.IIgnoreAlarmLogPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.IUserMsgLogNotSeeCountPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.IUserMsgLogSetAllSeePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.IUserMsgLogSetSeePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.IUserPushMsgLogBeyondTimePersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.message.IUserPushMsgLogPersenter;

/**
 * 消息桥梁工厂
 * Created by nd on 2018-08-02.
 */

public class MessagePersenterFactory {

    /**
     * 获取用户消息分页
     * @param view
     * @return
     */
    public static IUserPushMsgLogPersenter getUserPushMsgLogPersenter(IUserPushMsgLogPersenter.IUserPushMsgLogView view){
        return new UserPushMsgLogPersenterImpl(view);
    }

    /**
     * 获取用户超过指定消息ID的消息
     * @param view
     * @return
     */
    public static IUserPushMsgLogBeyondTimePersenter getUserPushMsgLogBeyondTimePersenterImpl(IUserPushMsgLogBeyondTimePersenter.IUserPushMsgLogBeyondTimeView view){
        return new UserPushMsgLogBeyondTimePersenterImpl(view);
    }


    public static IUserMsgLogNotSeeCountPersenter getUserMsgLogNotSeeCountPersenterImpl(IUserMsgLogNotSeeCountPersenter.IUserMsgLogNotSeeCountView view){
        return new UserMsgLogNotSeeCountPersenterImpl(view);
    }


    public static IUserMsgLogSetSeePersenter getUserMsgLogSetSeePersenterImpl(IUserMsgLogSetSeePersenter.IUserMsgLogSetSeeView view){
        return new UserMsgLogSetSeePersenterImpl(view);
    }

    public static IUserMsgLogSetAllSeePersenter getUserMsgLogSetAllSeePersenterImpl(IUserMsgLogSetAllSeePersenter.IUserMsgLogSetAllSeeView view){
        return new UserMsgLogSetAllSeePersenterImpl(view);
    }

    public  static IIgnoreAlarmLogPersenter getIgnoreAlarmLogPersenterImpl (IIgnoreAlarmLogPersenter.IIgnoreAlarmLogView view){
        return new IgnoreAlarmLogPersenterImpl(view);
    }

}
