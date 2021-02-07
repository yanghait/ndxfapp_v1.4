package com.ynzhxf.nd.xyfirecontrolapp.ui;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.ynzhxf.nd.xyfirecontrolapp.R;

/**
 * author hbzhou
 * date 2019/6/11 14:49
 * 适配带刘海机型下拉刷新时遮挡刷新动画的问题
 */
@SuppressLint("RestrictedApi")
public class SpecialLoadHeader extends LinearLayout implements RefreshHeader {
    protected ImageView imageView;
    protected AnimationDrawable animationDrawable1;
    protected AnimationDrawable animationDrawable2;

    public SpecialLoadHeader(Context context) {
        this(context, null);
    }

    public SpecialLoadHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = View.inflate(context, R.layout.special_load_header, this);
        imageView = view.findViewById(R.id.header_img);
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        imageView.setPadding(0, 0, 0, 0);
        if (animationDrawable2 != null && animationDrawable2.isRunning()) {
            animationDrawable2.stop();
        }
        return 0;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case PullDownToRefresh:
                imageView.setImageResource(R.drawable.load_animation_before);
                imageView.setPadding(0, 40, 0, 0);
                animationDrawable1 = (AnimationDrawable) imageView.getDrawable();
                animationDrawable1.start();
                break;
            case Refreshing:
                animationDrawable1.stop();
                imageView.setImageResource(R.drawable.load_animation_later);
                animationDrawable2 = (AnimationDrawable) imageView.getDrawable();
                animationDrawable2.start();
                break;
        }
    }
}
