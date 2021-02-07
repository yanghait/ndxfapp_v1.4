package com.ynzhxf.nd.xyfirecontrolapp.ui;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.ynzhxf.nd.xyfirecontrolapp.R;


/**
 * author hbzhou
 * date 2019/4/3 10:57
 */
public class MyLoadMenuBackHeader extends LinearLayout implements RefreshHeader {
    private ImageView imageView;
    private AnimationDrawable animationDrawable1;
    private AnimationDrawable animationDrawable2;

    public MyLoadMenuBackHeader(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.my_load_menu_header, null);
        imageView = view.findViewById(R.id.header_img);
        animationDrawable1 = (AnimationDrawable) context.getResources().getDrawable(R.drawable.load_animation_before);
        animationDrawable2 = (AnimationDrawable) context.getResources().getDrawable(R.drawable.load_animation_later);
        addView(view,new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public MyLoadMenuBackHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
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
        animationDrawable1.stop();
        imageView.setImageDrawable(animationDrawable2);
        animationDrawable2.start();
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        animationDrawable2.stop();
        return 500;
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
            case None:
            case PullDownToRefresh:
                imageView.setImageDrawable(animationDrawable1);
                animationDrawable1.start();
        }
    }
}
