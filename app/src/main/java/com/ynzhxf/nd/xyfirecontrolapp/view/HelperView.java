package com.ynzhxf.nd.xyfirecontrolapp.view;

import android.content.Context;
import android.widget.Toast;

/**
 * 界面通用帮助
 * Created by nd on 2018-07-17.
 */

public class HelperView {
    /**
     * 通用Toast消息提示框
     * @param context
     * @param msg
     */
    public static void Toast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
