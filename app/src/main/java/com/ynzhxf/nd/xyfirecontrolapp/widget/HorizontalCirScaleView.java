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


public class HorizontalCirScaleView extends View {

    //缓存位图
    private Bitmap bufferBitmap;
    private Canvas bufferCanvas;

    private float mWidth;//控件宽度

    private float mHeight;//控件高度

    private float mBigRadius;//大半径

    private Paint circlePaint;//外圈画笔

    private float circleStokeWidth;//外圈宽度

    private float circlePadding;//外圈内边距

    private Paint scalePaint;//刻度画笔

    private float mScaleBigRadius;//刻度圈最大半径

    private float mScale_height = 20;//刻度长度

    private float mScaleSmallRadius;//刻度圈里面小圆的半径

    private float mScale_width;//刻度宽度

    private float mScalePadding;//刻度圈内边距

    private Paint centerCircleBgPaint;//中心圆外圈画笔

    private float centerCircleBgRadius;//中心圆外圆半径

    private float centerCircleBgPadding;//中心圆外圈内边距

    private Paint progressPaint;

    private float mProgressRadius;//大半径

    private float mProgressPadding;//Progress与外圈间距

    private RectF mProgressOval;

    private int starProgress = 270;

    private Paint centerHollowCirclePaint;//中心圆外圈画笔

    private float centerHollowCircleRadius;//中心圆内圆半径

    private float centerHollowCirclePadding;//中心圆内圆半径

    private Paint centerCirclePaint;//中心圆外圈画笔

    private float centerCircleRadius;//中心圆内圆半径

    private Bitmap pointbitmap;//指针图片

    private float centerWidth, centerheight;//中心点坐标

    private float pointCenterX, pointCenterY;//指针中心点坐标

    Matrix matrix = new Matrix();

    private Paint textPaint;

    private float textPadding;

    private float textRadius;//刻度圈最大半径

    private String str0 = "0°", str90 = "90°", str180 = "180°", str270 = "-90°";

    private int paintColor;

    public void setPaintColor(int paintColor) {
        this.paintColor = paintColor;
        this.invalidate();
    }

    private int scaleNum = 50;//刻度数

    private double angle = 360d;//角度

    private double smalAngle;//每个小的刻度角度

    private int progress = 0;

    public void setProgress(int progress) {
        this.progress = progress;
        this.invalidate();
    }

    public HorizontalCirScaleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        setDrawingCacheEnabled(true);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CirScaleView, defStyleAttr, 0);

        mScale_height = typedArray.getDimension(R.styleable.CirScaleView_scale_height, 1);
        mScale_width = typedArray.getDimension(R.styleable.CirScaleView_scale_width, 1);
        paintColor = typedArray.getColor(R.styleable.CirScaleView_color, Color.RED);
        scaleNum = typedArray.getInt(R.styleable.CirScaleView_scale_num, 50);

        circlePaint = new Paint();
        circlePaint.setStrokeWidth(mScale_width);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.STROKE);
        circleStokeWidth = dp2px(context, 2);
        circlePadding = dp2px(context, 35);

        scalePaint = new Paint();
        scalePaint.setStrokeWidth(mScale_width);
        scalePaint.setAntiAlias(true);
        scalePaint.setColor(paintColor);
        mScalePadding = dp2px(context, 40);

        centerCircleBgPaint = new Paint();
        centerCircleBgPaint.setAntiAlias(true);
        circlePaint.setColor(Color.WHITE);
        circlePaint.setAlpha(40);
        centerCircleBgPadding = dp2px(context, 15);


        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setColor(Color.parseColor("#70E8B0"));
        mProgressPadding = dp2px(context, 9);

        centerHollowCirclePaint = new Paint();
        centerHollowCirclePaint.setStrokeWidth(circleStokeWidth);
        centerHollowCirclePaint.setAntiAlias(true);
        centerHollowCirclePaint.setStyle(Paint.Style.STROKE);
        centerHollowCirclePadding = dp2px(context, 3);

        centerCirclePaint = new Paint();
        centerCirclePaint.setAntiAlias(true);

        pointbitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.icon_point);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(dp2px(context, 12));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.WHITE);
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
        centerWidth = mWidth / 2;
        centerheight = mHeight / 2;

        mBigRadius = mWidth / 2 - circleStokeWidth;//最外层圈半径
        mScaleBigRadius = mBigRadius - circlePadding;//刻度最大半径
        mScaleSmallRadius = mScaleBigRadius - mScale_height;//刻度最小半径
        smalAngle = angle / scaleNum;//刻度角度
        textRadius = centerWidth - textPadding;

        mProgressRadius = mBigRadius - mProgressPadding * 2;
        progressPaint.setStrokeWidth(mProgressRadius - mScaleSmallRadius);
        mProgressOval = new RectF(centerWidth - mProgressRadius, centerWidth - mProgressRadius,
                centerWidth + mProgressRadius, centerWidth + mProgressRadius);

        centerCircleBgRadius = mScaleSmallRadius - mScalePadding;
        centerHollowCircleRadius = centerCircleBgRadius - centerCircleBgPadding;


        centerCircleRadius = centerHollowCircleRadius - centerHollowCirclePadding;


        pointCenterX = centerWidth - (pointbitmap.getWidth() / 2);
        pointCenterY = centerheight - pointbitmap.getHeight();

        textPadding = (mBigRadius - mScaleSmallRadius) / 2;

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
        canvas.drawArc(mProgressOval, starProgress, progress, false, progressPaint);

        canvas.drawText(progress + "°", centerWidth + textRadius * (float) Math.sin(Math.toRadians(progress)),
                centerheight - textRadius * (float) Math.cos(Math.toRadians(progress)), textPaint);
        //指针
        matrix.postTranslate(-pointbitmap.getWidth() / 2, -pointbitmap.getHeight());//步骤1
        matrix.postRotate(progress);//步骤2
        matrix.postTranslate(centerWidth, centerheight);//步骤3  屏幕的中心点
        canvas.drawBitmap(pointbitmap, matrix, null);
        matrix.reset();
        canvas.drawBitmap(bufferBitmap, 0, 0, null);
    }

    private void onDrawBackground() {
        bufferBitmap = Bitmap.createBitmap((int) mWidth, (int) mHeight, Bitmap.Config.ARGB_8888);
        bufferCanvas = new Canvas(bufferBitmap);
        //外圈
        bufferCanvas.drawCircle(centerWidth, centerheight, mBigRadius, circlePaint);
        //刻度
        for (int i = 0; i < scaleNum; i++) {
            float cosAngle = (float) Math.cos(Math.toRadians(smalAngle * i));
            float sinAngle = (float) Math.sin(Math.toRadians(smalAngle * i));
            bufferCanvas.drawLine(centerWidth + mScaleSmallRadius * cosAngle, centerheight - mScaleSmallRadius * sinAngle,
                    centerWidth + mScaleBigRadius * cosAngle, centerheight - mScaleBigRadius * sinAngle, scalePaint);
        }
        //中心圆底部透明圈
        centerCircleBgPaint.setColor(Color.parseColor("#4470E8B0"));
        bufferCanvas.drawCircle(centerWidth, centerheight, centerCircleBgRadius, centerCircleBgPaint);
        //中心圆外圈
        centerHollowCirclePaint.setColor(Color.parseColor("#70E8B0"));
        bufferCanvas.drawCircle(centerWidth, centerheight, centerHollowCircleRadius, centerHollowCirclePaint);
        //中心圆内圆
        centerCirclePaint.setColor(Color.parseColor("#70E8B0"));
        bufferCanvas.drawCircle(centerWidth, centerheight, centerCircleRadius, centerCirclePaint);
        //刻度标
        bufferCanvas.drawText(str0, centerWidth, textPadding, textPaint);
        bufferCanvas.drawText(str90, mWidth - textPadding, centerheight, textPaint);
        bufferCanvas.drawText(str180, centerWidth, mHeight - textPadding, textPaint);
        bufferCanvas.drawText(str270, textPadding, centerheight, textPaint);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (bufferBitmap!=null&&bufferBitmap.isRecycled())
            bufferBitmap.recycle();
        bufferCanvas = null;
        super.onDetachedFromWindow();
    }

    private int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }
}
