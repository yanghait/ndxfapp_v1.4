package com.ynzhxf.nd.xyfirecontrolapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.ynzhxf.nd.xyfirecontrolapp.R;

public class WaterCannonOCView extends View {

    private int mWidth;

    private int mHeight;

    private int centerWidth;

    private int centerHeight;

    private int startAngle = 0;

    public void setStartAngle(int startAngle) {
        this.startAngle = startAngle;
        invalidate();
    }

    private Matrix leftMatrix, rightMatrix;

    private Bitmap centerbitmap;
    private float centerBimapWidth;
    private float centerBitmapHeight;
    private Bitmap leftLidBitmap;
    private float leftLidBitmapWidth;
    private float leftLidBitmapHeight;
    private Bitmap rightLidBitmap;
    private float rightLidBitmapWidth;
    private float rightLidBitmapHeight;

    public WaterCannonOCView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CirScaleView, defStyleAttr, 0);

        startAngle = typedArray.getInt(R.styleable.CirScaleView_scale_num, 0);

        leftMatrix = new Matrix();
        rightMatrix = new Matrix();
        centerbitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.bg_cannon_center);
        centerBimapWidth = centerbitmap.getWidth();
        centerBitmapHeight = centerbitmap.getHeight();
        leftLidBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.bg_cannon_leftlid);
        leftLidBitmapWidth = leftLidBitmap.getWidth();
        leftLidBitmapHeight = leftLidBitmap.getHeight();
        rightLidBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.bg_cannon_rightlid);
        rightLidBitmapWidth = rightLidBitmap.getWidth();
        rightLidBitmapHeight = rightLidBitmap.getHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = getResolveSize((int) leftLidBitmapWidth, heightMeasureSpec);
        centerWidth = mWidth / 2;
        centerHeight = mHeight;
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
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
        canvas.drawBitmap(centerbitmap, centerWidth - centerBimapWidth / 2, centerHeight - centerbitmap.getHeight(), null);

        leftMatrix.postTranslate(-leftLidBitmap.getWidth() / 2, -leftLidBitmap.getHeight());//步骤1
        leftMatrix.postRotate(-startAngle);//步骤2
        leftMatrix.postTranslate(centerWidth - leftLidBitmapWidth / 2, centerHeight);//步骤3  屏幕的中心点
        canvas.drawBitmap(leftLidBitmap, leftMatrix, null);


        rightMatrix.postTranslate(-rightLidBitmap.getWidth() / 2, -rightLidBitmap.getHeight());//步骤1
        rightMatrix.postRotate(startAngle);//步骤2
        rightMatrix.postTranslate(centerWidth + rightLidBitmapWidth / 2, centerHeight);//步骤3  屏幕的中心点
        canvas.drawBitmap(rightLidBitmap, rightMatrix, null);

        leftMatrix.reset();
        rightMatrix.reset();
    }
}
