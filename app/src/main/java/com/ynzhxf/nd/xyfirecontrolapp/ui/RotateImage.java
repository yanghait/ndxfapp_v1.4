package com.ynzhxf.nd.xyfirecontrolapp.ui;

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
 * 旋转的图片  用完之后记得释放，用之前初始化
 * 1.自定义类型动画，以后考虑封装成通用
 * Created by nd on 2018-08-07.
 */

@SuppressLint("AppCompatCustomView")
public class RotateImage extends ImageView {

    private static List<RotateImage> images = new ArrayList<>();

    private static UpdateImageThread updateThread;

    //每周旋转的时间
    private static int completeTime = 3000;

    //步进时间
    private static int timeStep = 40;
    //每次更新旋转的角度
    private static double stepArgle = 1f*360/completeTime*timeStep;;

    /**
     * 初始化执行列表清空并启动
     */
    public static void start(){
        GloblePlantformDatas.isRealUIRotateThreadRun = true;
        try {
            Thread.sleep(timeStep*2);
            synchronized (images){
                images.clear();
                updateThread = new UpdateImageThread();
                updateThread.start();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 停止所有图片旋转动画
     * @throws InterruptedException
     */
    public  static void stop(){
        synchronized (images){
            GloblePlantformDatas.isRealUIRotateThreadRun = false;
            images.clear();
            try {
                Thread.sleep(timeStep*2);
                if(updateThread != null){
                    updateThread.interrupt();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    //已旋转的角度
    public float hasRotate;

    //是否可以旋转
    public boolean canRoate;


    public RotateImage(Context context) {
        super(context);
    }

    public RotateImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RotateImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        images.add(this);
    }

    /**
     * 窗口中分离
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        images.remove(this);
    }


    private static class UpdateImageThread extends Thread{
        @Override
        public void run() {
            while (GloblePlantformDatas.isRealUIRotateThreadRun){
                try{
                    Thread.sleep(timeStep);
                    synchronized (images){
                        for(int i = 0 ; i<images.size();i++){
                            RotateImage queryObj = images.get(i);
                            if(queryObj.canRoate){
                                queryObj.handler.sendEmptyMessage(0);
                            }

                        }
                    }
                }catch (Exception e){

                }
            }
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            hasRotate+= stepArgle;
            if(hasRotate >= 360){
                hasRotate = 0;
            }
            RotateImage.this.setRotation(hasRotate % 360);
        }
    };
}
