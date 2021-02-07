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
 * 绘制偏差率表盘
 * 1.存在bug   RingDraw实现方式
 * Created by nd on 2018-08-05.
 */

public class CircularDraw extends View {
    private Paint mPaint = new Paint();
    int width = this.getWidth();
    //是否可以绘制
    private boolean canLoad = false;

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

    private PpdateStep ppdateStep;




    public CircularDraw(Context context, @Nullable AttributeSet attrs) {
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
        if(canLoad){
            mPaint.setStrokeWidth(2);
            mPaint.setColor(Color.parseColor("#aaa9a9"));//#040404

            //绘制表盘刻度
            for(int i=0;i<36;i++){
                canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, 10 , mPaint);
                canvas.rotate(10, getWidth() / 2, getWidth() / 2);
            }
            //表针绘制
            mPaint.setStrokeWidth(4);
            mPaint.setColor(Color.parseColor("#ec1a1a"));
            canvas.rotate(hasRoateAuglar, getWidth() / 2, getWidth() / 2);
            canvas.drawLine(getWidth()/2 ,20 , getWidth()/2 , getWidth()/2 , mPaint);
            canvas.rotate(-hasRoateAuglar, getWidth() / 2, getWidth() / 2);

            //中心点绘制
            canvas.drawCircle(getWidth()/2 , getHeight()/2 , 6 , mPaint);

            //蓝色表盘绘制
            mPaint.setStrokeWidth(3);
            int temp = (int) (hasRoateAuglar/10);
            temp = Math.abs(temp);
            if(!isRigth){
                mPaint.setColor(Color.parseColor("#59c4ff"));
                for(int i=0;i <temp+1;i++){
                    canvas.rotate(-10 * i, getWidth() / 2, getWidth() / 2);
                    canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, 10 , mPaint);
                    canvas.rotate(10 * i, getWidth() / 2, getWidth() / 2);
                }
            }else{
                mPaint.setColor(Color.parseColor("#59c4ff"));
                for(int i=0;i <temp+1;i++){
                    canvas.rotate(10 * i, getWidth() / 2, getWidth() / 2);
                    canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, 10 , mPaint);
                    canvas.rotate(-10 * i, getWidth() / 2, getWidth() / 2);
                }
            }

        }
    }

    @Override
    public boolean callOnClick() {
        return true;

    }



    public void loadData(String persent){
        if(persent != null){
            if( persent.indexOf("%") != -1){
                try {
                    double queryInt = Double.parseDouble(persent.split("%")[0]);
                    canLoad = true;
                    if(isFirstLoad){
                        isFirstLoad = false;
                        if( hasFirst && !hasFinishFirst){
                            value = queryInt;
                            if(queryInt < 0 ){
                                if(queryInt < -100){
                                    queryInt = -100;
                                }
                                isRigth = false;
                            }else {
                                if(queryInt > 100){
                                    queryInt = 100;
                                }
                                isRigth = true;
                            }
                            hasFirst = false;
                            //表针旋转终点角度
                            endArglue = (int) (180 * queryInt/100);
                            //每次更新旋转的角度数
                            speed = 1f * endArglue / (completeTime/timeDeltatime);
                            this.ppdateStep.start();
                        }
                    }else {
                        //不完成首次加载，不改变数据
                        if( hasFinishFirst){
                            value = queryInt;
                            if(queryInt < 0 ){
                                if(queryInt < -100){
                                    queryInt = -100;
                                }
                                isRigth = false;
                            }else {
                                if(queryInt > 100){
                                    queryInt = 100;
                                }
                                isRigth = true;
                            }
                            //表针旋转终点角度
                            endArglue = (int) (180 * queryInt/100);
                            //每次更新旋转的角度数
                            hasRoateAuglar = endArglue;
                            invalidate();
                        }
                    }
                }catch (Exception e){
                    Log.e("Draw" , "表盘绘制数据错误");
                    canLoad =false;
                }
            }
        }
    }

    public class PpdateStep extends Thread{
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
    private Handler handler = new Handler(){
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
