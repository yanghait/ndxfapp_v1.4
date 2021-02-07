package com.ynzhxf.nd.xyfirecontrolapp.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义动态增长的TextView
 * Created by nd on 2018-07-15.
 */

@SuppressLint("AppCompatCustomView")
public class RiseNumberTextView extends TextView {
    /**
     * 要增长的数字
     */
    private int count;
    /**
     * 多长时间执行完
     */
    private int excuteTime;

    /**
     * 单步更新时间
     */
    private int sleepTime = 50;

    /**
     * 单步增长的速度
     */
    private int countStep;

    /**
     * 当前的值
     */
    private double nowTotalCount;

    /**
     * 要显示的数量
     */
    private int NowCount;

    /**
     * 处理线程要接收的信息标识
     */
    private int flag;

    /**
     * 标识新增线程是否继续运行
     */
    private Map<Integer,Boolean> markThreadRun = new HashMap<>();

    Handler handler;

    /**
     * 是否需要重新执行
     */
    private boolean reExcute;
    public RiseNumberTextView(Context context) {
        super(context);
    }

    public RiseNumberTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RiseNumberTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public RiseNumberTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == flag){
                    NowCount = (int)nowTotalCount;
                    if(NowCount >= count){
                        NowCount = count;
                    }
                    setText(NowCount+"");
                }
            }
        };
    }


    /**
     * 执行数据更新
     * @param count
     */
    public void setExcuteRise(int count){
        this.count = count;

        nowTotalCount = 0;
        NowCount = 0;
        if(count<100){
            this.excuteTime = 1000;
        }else if ( count >=100 && count < 1000 ){
            this.excuteTime = 1500;
        }else {
            this.excuteTime = 2000;
        }
        if(flag>0){
            markThreadRun.put(flag,false);
        }
        flag++;
        markThreadRun.put(flag,true);
        caculateStepTime();
    }

    /**
     *计算步进速度
     */
    private void caculateStepTime(){
        countStep = sleepTime*count/excuteTime;
        if(countStep == 0) countStep = 1;
        final int queryFlag = flag;
        try {
            new Thread(){
                @Override
                public void run() {
                    while (markThreadRun.get(flag)){
                        try {
                            Thread.sleep(sleepTime);
                            nowTotalCount+=countStep;
                            handler.sendEmptyMessage(queryFlag);
                            if(nowTotalCount>=count){
                                markThreadRun.put(queryFlag,false);
                                return;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }catch (Exception ex){
            System.out.print("");
        }
    }
}
