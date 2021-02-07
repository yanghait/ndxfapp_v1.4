package com.ynzhxf.nd.xyfirecontrolapp.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目信息页那两坨
 * Created by nd on 2018-08-06.
 */

public class RingDraw2 extends View {

    //绘制背景
    private Paint paint;

    //绘制背景
    private Paint pbac;

    private boolean isCan;

    //弧形开始角度
    private int startAgle;
    //弧形结束角度
    private int endAgle;
    //环形宽度
    private int circleWidth =50;
    //结束颜色
    private int endColor = Color.parseColor("#ffcd02");
    //开始颜色
    private int statrColor = Color.parseColor("#ff3155");
    //绘制完成时间
    private int completeTime = 1200;
    //每次步进的时间
    private int stepTime = 20;

    private Shader linearGradient;

    //已绘制的圆弧度数
    private float hasDrawAgle = 270;
    //绘制步进速度速度
    private float speedDrawagle;

    private Map<Integer , Boolean> marks = new HashMap<>();
    private int flag = 0;

    private boolean canLoad = false;

    private int totalCount = 0;

    private int nowCount = 0;

    private float speedcount;
    private float riseCcount ;

    //宽度
    private int width;
    //高度
    private int height;

    //是首次加载
    private boolean isFirst;
    private boolean canEnd = false;



    public RingDraw2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        pbac = new Paint();
        pbac.setStrokeWidth(20);
        pbac.setAntiAlias(true);

        paint = new Paint();
        paint.setStrokeWidth (15);//设置画笔宽度
        paint.setAntiAlias(true); //指定是否使用抗锯齿功能，如果使用，会使绘图速度变慢
        paint.setStyle(Paint.Style.FILL);//绘图样式，对于设文字和几何图形都有效
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(48);
        paint.setColor(Color.parseColor("#ff4549"));
        paint.setFakeBoldText(true);//设置是否为粗体文字

        endColor = Color.parseColor("#1f9ef9");
        statrColor = Color.parseColor("#b5dcf9");
        pbac.setColor(statrColor);
        pbac.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.height = getHeight();
        this.width = getWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.height = getHeight();
        this.width = getWidth();
        synchronized (this){

            canvas.drawText(nowCount+"", width/2,width/2 + circleWidth/2, paint);
            pbac.setColor(Color.parseColor("#ddd5d3"));
            pbac.setShader(null);
            float x = 0+circleWidth;
            float y = 0+circleWidth;
            RectF oval = new RectF( x, y,width - circleWidth, width - circleWidth);
            canvas.drawArc(oval,30,300,false , pbac);
            float sx1 =(float) (width/2 +  (width/2 - circleWidth) * Math.cos(30 *3.14 /180));
            float sy1 =(float) (width/2 +  (width/2 - circleWidth) * Math.sin(30 *3.14 /180));
            canvas.drawCircle( sx1 ,sy1 ,circleWidth/32, pbac);
            float ex1 = (float) (width/2 +  (width/2 - circleWidth) * Math.cos(300 *3.14 /180));
            float ey1 = (float) (width/2 +  (width/2 - circleWidth) * Math.sin(300 *3.14 /180));
            canvas.drawCircle( ex1 ,ey1 ,circleWidth/32, pbac);


            if(canLoad){
                pbac.setStyle(Paint.Style.STROKE);
                linearGradient = new  LinearGradient(width, height, 0, 0 , endColor,statrColor ,  Shader.TileMode.MIRROR);
                pbac.setShader(linearGradient);
                canvas.drawArc(oval,startAgle,hasDrawAgle,false,pbac);
                //绘制起点圆角
                float sx =(float) (width/2 +  (width/2 - circleWidth) * Math.cos(startAgle *3.14 /180));
                float sy =(float) (width/2 +  (width/2 - circleWidth) * Math.sin(startAgle *3.14 /180));
                canvas.drawCircle( sx ,sy ,circleWidth/32, pbac);
                if(canEnd){
                    float ex = (float) (width/2 +  (width/2 - circleWidth) * Math.cos(endAgle *3.14 /180));
                    float ey = (float) (width/2 +  (width/2 - circleWidth) * Math.sin(endAgle *3.14 /180));
                    canvas.drawCircle( ex ,ey ,circleWidth/32, pbac);
                    canEnd = false;
                }
            }
        }

    }

    /**
     * 通知重新绘制
     * @param startAgle 开始角度
     * @param endAgle 结束角度
     * @param count 增长数量
     */
    public void setAgle(int startAgle , int endAgle , int count ){
        this.totalCount = count;
        this.nowCount = 0;
        riseCcount = 0;
        this.startAgle = startAgle;
        this.endAgle = endAgle;
        speedDrawagle = (endAgle - startAgle)/(1f*completeTime/stepTime);
        speedcount = 1f*totalCount / (completeTime/stepTime);

        synchronized(this){
            marks.put(flag , false);
            flag++;
            canLoad = true;
            marks.put(flag , true);

            hasDrawAgle = 0;
        }
        caculateStepTime();
    }

    /**
     * 通知重新绘制
     * @param startAgle 开始角度
     * @param endAgle 结束角度
     * @param count 增长数量
     */
    public void setAgle(int startAgle , int endAgle , int count ,int startColor , int endColor){
        this.statrColor = startColor;
        this.endColor = endColor;
        setAgle(startAgle , endAgle , count);
    }

    /**
     *计算步进速度
     */
    private void caculateStepTime(){
        final int queryFlag = flag;
        try {
            new Thread(){
                @Override
                public void run() {
                    int totalCount = completeTime/stepTime + 1;
                    while (marks.get(flag)){
                        try {
                            Thread.sleep(stepTime);
                            totalCount -- ;
                            synchronized(this){
                                if(marks.get(flag)){
                                    handler.sendEmptyMessage(0);
                                }
                                if(totalCount<0){
                                    marks.put(queryFlag,false);
                                    return;
                                }

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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            hasDrawAgle += speedDrawagle;
            if(hasDrawAgle >= endAgle - startAgle){
                hasDrawAgle = endAgle - startAgle;
                canEnd = true;
            }
            riseCcount += speedcount;
            nowCount = (int) riseCcount;
            if(nowCount>= riseCcount){
                nowCount = totalCount;
            }
            invalidate();
        }
    };
}
