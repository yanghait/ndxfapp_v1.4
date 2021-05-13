package com.ynzhxf.nd.xyfirecontrolapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.ynzhxf.nd.xyfirecontrolapp.R;


public class VerticalCirScaleView extends View {

    //缓存位图
    private Bitmap bufferBitmap;
    private Canvas bufferCanvas;

    private float mWidth;//控件宽度

    private float mHeight;//控件高度

    private float centerWidth, centerheight;//中心点坐标

    //边框扇形
    private Paint framePaint;
    private RectF frameRectF;
    //中心扇形
    private Paint centerSectorPaint;
    private RectF centerSectorRectF;
    //中心边框扇形
    private Paint centerFramePaint;
    private RectF centerFrameRectF;

    //刻度
    private Paint mScalePaint;//画笔
    private float mScale_height = 20;//刻度长度
    private float mBigRadius;//大半径
    private float mSmallRadius;//里面小圆的半径
    //指针
    private Bitmap pointbitmap;//指针图片
    Matrix matrix = new Matrix();
    //进度条
    private Paint progressPaint;
    private float mProgressRadius;//大半径
    private float mProgressPadding;//Progress与外圈间距
    private RectF mProgressOval;

    private Paint textPaint;

    private float padding;

    private String str0 = "0°", str90 = "90°", str_30 = "-30°";

    private int starProgress = 360;

    private int startAngle = 270;

    private float progress = 0;

    public void setProgress(float progress) {
        this.progress = progress;
        this.invalidate();
    }


    private float mScale_width;

    private int paintColor;

    public void setPaintColor(int paintColor) {
        this.paintColor = paintColor;
        this.invalidate();
    }

    private int scaleNum = 50;//刻度数

    private double angle = 120d;//角度

    private double smalAngle;//每个小的刻度角度

    private boolean isFirstDraw = true;

    public VerticalCirScaleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CirScaleView, defStyleAttr, 0);


        mScale_height = typedArray.getDimension(R.styleable.CirScaleView_scale_height, 1);
        mScale_width = typedArray.getDimension(R.styleable.CirScaleView_scale_width, 1);
        paintColor = typedArray.getColor(R.styleable.CirScaleView_color, Color.RED);
        scaleNum = typedArray.getInt(R.styleable.CirScaleView_scale_num, 50);
        //外边框
        framePaint = new Paint();
        framePaint.setStrokeWidth(dp2px(context, 2));
        framePaint.setAntiAlias(true);
        framePaint.setStyle(Paint.Style.STROKE);
        framePaint.setColor(Color.WHITE);
        framePaint.setAlpha(40);
        //中心扇形
        centerSectorPaint = new Paint();
        centerSectorPaint.setAntiAlias(true);
        centerSectorPaint.setColor(Color.parseColor("#70E8B0"));
        //中心扇形边框
        centerFramePaint = new Paint();
        centerFramePaint.setStrokeWidth(dp2px(context, 2));
        centerFramePaint.setAntiAlias(true);
        centerFramePaint.setStyle(Paint.Style.STROKE);
        centerFramePaint.setColor(Color.WHITE);
        centerFramePaint.setAlpha(40);
        //刻度
        mScalePaint = new Paint();
        mScalePaint.setStrokeWidth(mScale_width);
        mScalePaint.setColor(paintColor);
        mScalePaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(dp2px(context, 12));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.WHITE);

        padding = dp2px(context, 10);
        //进度条
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setColor(Color.parseColor("#70E8B0"));
        progressPaint.setStyle(Paint.Style.STROKE);
        mProgressPadding = dp2px(context, 9);

//        //指针
        pointbitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.icon_point);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widhSize = getResolveSize(200, widthMeasureSpec);
        int heightSize = getResolveSize(200, heightMeasureSpec);

        if (widhSize > heightSize) {
            heightSize = widhSize;
        }
        mWidth = widhSize;
        mHeight = heightSize;
        centerWidth = mWidth / 3 * 2;
        centerheight = mHeight / 3 * 2;

        frameRectF = new RectF(-centerWidth, 0, centerWidth, centerheight * 2);

        float centerSectorFrameRadius = centerWidth / 5;
        centerSectorRectF = new RectF(-centerSectorFrameRadius, centerheight - centerSectorFrameRadius, +centerSectorFrameRadius, centerheight + centerSectorFrameRadius);

        float centerFrameRadius = centerWidth / 5 * 2;
        centerFrameRectF = new RectF(-centerFrameRadius, centerheight - centerFrameRadius, centerFrameRadius, centerheight + centerFrameRadius);

        mBigRadius = centerWidth / 3 * 2;
        mSmallRadius = mBigRadius - mScale_height;
        smalAngle = angle / scaleNum;

        mProgressRadius = mBigRadius + mProgressPadding * 3;
        progressPaint.setStrokeWidth(mProgressRadius - mBigRadius);

        mProgressOval = new RectF(-mProgressRadius, centerheight - mProgressRadius,
                +mProgressRadius, centerheight + mProgressRadius);

        setMeasuredDimension(widhSize, heightSize);
    }

    private int getResolveSize(int size, int measureSec) {
        int result = size;

        int meSize = MeasureSpec.getSize(measureSec);
        int meMode = MeasureSpec.getMode(measureSec);
        switch (meMode) {
            case MeasureSpec.AT_MOST:
                result = Math.min(size, meSize);
                break;
            case MeasureSpec.EXACTLY:
                result = meSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
        }


        return result;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (bufferBitmap == null) {
            onDrawBackground();
        }
        //进度条
        canvas.drawArc(mProgressOval, starProgress, -progress, false, progressPaint);
        canvas.drawText(progress + "°", centerWidth + mWidth / 4 * 3 * (float) Math.cos(Math.toRadians(progress)),
                centerheight - mWidth / 4 * 3 * (float) Math.sin(Math.toRadians(progress)), textPaint);
        //指针
        matrix.postTranslate(-pointbitmap.getWidth() / 2, -pointbitmap.getHeight());//步骤1
        matrix.postRotate(90 - progress);//步骤2
        matrix.postTranslate(0, centerheight);//步骤3  屏幕的中心点
        canvas.drawBitmap(pointbitmap, matrix, null);
        matrix.reset();
        canvas.drawBitmap(bufferBitmap, 0, 0, null);
    }

    private void onDrawBackground() {
        bufferBitmap = Bitmap.createBitmap((int) mWidth, (int) mHeight, Bitmap.Config.ARGB_8888);
        bufferCanvas = new Canvas(bufferBitmap);
        //绘制边框
        bufferCanvas.drawArc(frameRectF, startAngle, 120, true, framePaint);
        //绘制中心扇形
        bufferCanvas.drawArc(centerSectorRectF, startAngle, 180, true, centerSectorPaint);
        //绘制中心边框
        bufferCanvas.drawArc(centerFrameRectF, startAngle, 120, true, centerFramePaint);


        for (int i = 0; i < scaleNum; i++) {
            float cosAngle = (float) Math.cos(Math.toRadians(smalAngle * i));
            float sinAngle = (float) Math.sin(Math.toRadians(smalAngle * i));
            bufferCanvas.drawLine(mSmallRadius * sinAngle, centerheight - mSmallRadius * cosAngle,
                    mBigRadius * sinAngle, centerheight - mBigRadius * cosAngle, mScalePaint);

        }
        bufferCanvas.drawLine(0, centerheight, centerWidth, centerheight, centerFramePaint);
        bufferCanvas.drawText(str90, padding, padding, textPaint);
        bufferCanvas.drawText(str0, centerWidth - padding, centerheight, textPaint);
        bufferCanvas.drawText(str_30, centerWidth * (float) Math.sin(Math.toRadians(smalAngle * scaleNum)) + padding, centerheight - centerheight * (float) Math.cos(Math.toRadians(smalAngle * scaleNum)), textPaint);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (bufferBitmap != null && bufferBitmap.isRecycled())
            bufferBitmap.recycle();
        bufferCanvas = null;
        super.onDetachedFromWindow();
    }

    private int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }
}
