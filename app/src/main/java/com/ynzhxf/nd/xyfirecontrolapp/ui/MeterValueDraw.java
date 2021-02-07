package com.ynzhxf.nd.xyfirecontrolapp.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 绘制值类型表盘
 * 1.bug
 * Created by nd on 2018-08-05.
 */

public class MeterValueDraw extends View {
    private Paint mPaint = new Paint();
    int width = this.getWidth();
    //是否可以绘制
    private boolean canLoad = false;

    //起始表盘度数（南方 顺时针计算）
    private int startArglueBiao = 40;
    //结束表盘度数（南方 顺时针计算）
    private int endBlueArglueBiao = 320;
    private int kedu = 5;

    //表针终点角度
    private int endArglue;

    //动画完成时间
    private int completeTime = 1000;

    //单帧时间
    private int timeDeltatime = 40;



    private boolean isRigth = false;

    //传入的值
    private double value;


    //每次刷新要旋转的度数
    private double speed;

    //是否时首次加载
    private boolean isFirstLoad = true;

    //是否开始首次加载
    private boolean hasFirst = true;

    //是否完成首次加载
    private boolean hasFinishFirst = false;


    //记录流逝时间
    private int loseTime;

    //已旋转的角度
    private float hasRoateAuglar = 0;

    //最小值
    private double minvalue;
    //最大值
    private double maxValue;

    private PpdateStep ppdateStep;


    public MeterValueDraw(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.FILL);
        isFirstLoad = true;
        hasRoateAuglar = 0;
        hasFirst = true;
        hasFinishFirst = false;
        speed = 0;
        ppdateStep = new PpdateStep();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canLoad) {
            mPaint.setStrokeWidth(3);
            mPaint.setColor(Color.parseColor("#aaa9a9"));
            canvas.rotate(180, getWidth() / 2, getWidth() / 2);
            //绘制表盘刻度
            int queryCount =  360/ kedu;
            int queryStartCount =  startArglueBiao/kedu;
            int queryEndCount = endBlueArglueBiao/kedu;
            for (int i = 0; i < queryCount; i++) {
                if (i < queryStartCount || i > queryEndCount) {
                    canvas.rotate(kedu, getWidth() / 2, getWidth() / 2);
                    continue;
                }
                mPaint.setColor(Color.parseColor("#aaa9a9"));
                canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, 10, mPaint);
                canvas.rotate(kedu, getWidth() / 2, getWidth() / 2);
            }

            //绘制显示刻度
            int temp = (int) ((hasRoateAuglar+startArglueBiao) / kedu);
            if(temp > queryEndCount){
                temp = queryEndCount;
            }
            for (int i = 0; i < queryCount; i++) {
                if (i < queryStartCount || i > queryEndCount) {
                    canvas.rotate(kedu, getWidth() / 2, getWidth() / 2);
                    continue;
                }
                mPaint.setStrokeWidth(4);
                if(i == temp){
                    mPaint.setStrokeWidth(8);
                    mPaint.setColor(Color.parseColor("#ec1a1a"));
                    canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, 30, mPaint);
                }else {
                    if(i<temp){
                        mPaint.setColor(Color.parseColor("#59c4ff"));
                        canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, 10, mPaint);
                    }

                }
                canvas.rotate(kedu, getWidth() / 2, getWidth() / 2);
            }

        }

    }

    @Override
    public boolean callOnClick() {
        return true;

    }

    public void loadData(double persent ,double maxValue) {
        try {

            double queryInt = persent;
            canLoad = true;
            if(isFirstLoad){
                isFirstLoad = false;
                if( hasFirst && !hasFinishFirst){
                    hasFirst = false;
                    this.maxValue = maxValue;
                    if(queryInt < 0){
                        this.minvalue = queryInt - maxValue/5;
                    }else {
                        this.minvalue = 0;
                    }
                    this.maxValue = maxValue;
                    //表针旋转终点角度
                    endArglue = Math.abs((int) (queryInt*((endBlueArglueBiao - startArglueBiao)/(maxValue - minvalue))));
                    speed = 1f * endArglue / (completeTime/timeDeltatime);

                    this.ppdateStep.start();
                }
            }else {
                //不完成首次加载，不改变数据
                if( hasFinishFirst){
                    this.maxValue = maxValue;
                    if(queryInt < 0){
                        this.minvalue = queryInt - maxValue/5;
                    }else {
                        this.minvalue = 0;
                    }
                    this.maxValue = maxValue;
                    //表针旋转终点角度
                    endArglue = Math.abs((int) (queryInt*((endBlueArglueBiao - startArglueBiao)/(maxValue - minvalue))));
                    //每次更新旋转的角度数
                    hasRoateAuglar = endArglue;
                    invalidate();
                }
            }



        } catch (Exception e) {
            Log.e("Draw", "表盘绘制数据错误");
            canLoad = false;
        }
    }

    public class PpdateStep extends Thread {
        @Override
        public void run() {
            try {
                while (loseTime <= completeTime){

                    if(!hasFinishFirst){
                        handler.sendEmptyMessage(0);
                        Thread.sleep(timeDeltatime);
                    }
                    loseTime += timeDeltatime;
                }
            }catch (Exception e){

            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            hasRoateAuglar += speed;
            if(Math.abs(hasRoateAuglar)  >= Math.abs(endArglue)){
                hasRoateAuglar = endArglue;
                hasFinishFirst = true;
            }
            invalidate();
        }
    };
}
