package com.ynzhxf.nd.xyfirecontrolapp.view;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * activity缓存，缓存全局已启动的活动,提供对活动的操作
 * Created by nd on 2018-07-15.
 */

public class ActivityController {

    private static List<Activity> activities = new ArrayList<>();

    /**
     * 添加活动
     *
     * @param activity
     */
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 移除活动
     *
     * @param activity
     */
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    /**
     * 销毁所有活动
     */
    public static void finishAll() {
        for (Activity query : activities) {
            if (!query.isFinishing()) {
                query.finish();
            }
        }
    }

    public static int getHasCount() {
        return activities.size();
    }
}
