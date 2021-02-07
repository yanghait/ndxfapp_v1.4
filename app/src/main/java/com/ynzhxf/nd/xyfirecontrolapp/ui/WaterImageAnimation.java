package com.ynzhxf.nd.xyfirecontrolapp.ui;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.ynzhxf.nd.xyfirecontrolapp.GloblePlantformDatas;

import java.util.ArrayList;
import java.util.List;

/**
 * 水波动态图片  用完之后记得释放，用之前初始化
 * Created by nd on 2018-08-07.
 */

@SuppressLint("AppCompatCustomView")
public class WaterImageAnimation extends ImageView {

    private static List<WaterImageAnimation> images = new ArrayList<>();

    private static UpdateImageWidthThread updateMoveXThread;

    //完成一次平移的时间
    private static int completeTimewidth = 10000;

    //步进时间
    private static int timeStep = 40;

    //每次更新旋转的角度
    private static double stepMoveX;


    /**
     * 初始化执行列表清空并启动
     */
    public static void start() {
        GloblePlantformDatas.isRealUIMoveThreadRun = true;
        try {
            Thread.sleep(timeStep*2);
            synchronized (images){
                images.clear();
                updateMoveXThread = new UpdateImageWidthThread();
                updateMoveXThread.start();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 停止所有图片旋转动画
     * @throws InterruptedException
     */
    public  static void stop() {
        synchronized (images){
            GloblePlantformDatas.isRealUIMoveThreadRun = false;
            images.clear();
            try {
                Thread.sleep(timeStep*2);
                if(updateMoveXThread != null){
                    updateMoveXThread.interrupt();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    static {
        updateMoveXThread = new UpdateImageWidthThread();
        updateMoveXThread.start();
    }


    public double StepMoveX;
    public double hasMoveX;
    public double endMoveX;

    //终点位置
    private double endMoveY;

    //动画完成时间
    private int completeTime = 1000;

    //单帧时间
    private int timeDeltatime = 40;


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
    private double hasMoveY = 0;

    private PpdateStep ppdateStep;
    private ObjectAnimator translationX;


    public WaterImageAnimation(Context context) {
        super(context);
    }

    public WaterImageAnimation(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        isFirstLoad = true;
        endMoveY = 0;
        hasFirst = true;
        hasFinishFirst = false;
        speed = 0;
        ppdateStep = new PpdateStep();
        images.add(this);
    }

    public WaterImageAnimation(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void loadAnimation(double value , Double maxValue){
        synchronized (this){
            float heightInit = 80;
            float widthInit = 160;
            float scale= getContext().getResources().getDisplayMetrics().density;
            int width =(int)(widthInit*scale+0.5f);
            int height =(int)(heightInit*scale+0.5f);
            float query1 = this.getTranslationX();
            float query2 = this.getTranslationY();
            endMoveX = width;
            if(isFirstLoad){
                isFirstLoad = false;
                if (hasFirst && !hasFinishFirst){
                    this.setTranslationY(0);
                    this.setTranslationX(0);
                    if(value > 0 ){
                        if(maxValue == null || maxValue- value < 0){
                            maxValue = value + value * 0.05;
                        }
                        endMoveY = (int)(value*(height/maxValue));
                        if(endMoveY < height * 0.05){
                            endMoveY = (int)(height * 0.05);
                        }
                        if(endMoveY > height - height*0.05){
                            endMoveY = height - height*0.05;
                        }
                    }
                    speed = endMoveY/(completeTime/timeDeltatime);
                    StepMoveX = 1f*width/(completeTimewidth/timeDeltatime);
                    this.ppdateStep.start();;
                }
            }else {
                if(hasFinishFirst){
                    if(value > 0 ){
                        if(maxValue == null || maxValue- value < 0){
                            maxValue = value + value * 0.05;
                        }
                        endMoveY = (int)(value*(height/maxValue));
                        if(endMoveY < height * 0.05){
                            endMoveY = (int)(height * 0.05);
                        }

                        if(endMoveY > height - height*0.05){
                            endMoveY = height - height*0.05;
                        }
                    }else {
                        endMoveY = 0;
                    }
                    this.setTranslationY(-(float) endMoveY);
                    //this.setTranslationX(0);
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
            if(msg.what == 0){
                hasMoveY += speed;
                if(hasMoveY  >= endMoveY){
                    hasMoveY = endMoveY;
                    hasFinishFirst = true;
                }
                WaterImageAnimation.this.setTranslationY((float) -hasMoveY);
            }

            if(msg.what == 1){
                hasMoveX += StepMoveX;
                if(hasMoveX >= endMoveX){
                    hasMoveX = 0;
                }
                WaterImageAnimation.this.setTranslationX((float) -hasMoveX);
            }
        }
    };


    private static class UpdateImageWidthThread extends Thread{
        @Override
        public void run() {
            while (GloblePlantformDatas.isRealUIMoveThreadRun){
                try{
                    Thread.sleep(timeStep);
                    synchronized (images){
                        for(int i = 0 ; i<images.size();i++){
                            WaterImageAnimation queryObj = images.get(i);
                            queryObj.handler.sendEmptyMessage(1);

                        }
                    }
                }catch (Exception e){

                }
            }
        }
    }
}
