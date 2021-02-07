package com.ynzhxf.nd.xyfirecontrolapp.util.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

import com.chad.library.adapter.base.animation.BaseAnimation;


/**
 * author hbzhou
 * date 2019/9/20 17:30
 * 列表缩放动画
 */
public class CustomAnimation implements BaseAnimation {
    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view, "scaleY", 1, 1.2f, 1),
                ObjectAnimator.ofFloat(view, "scaleX", 1, 1.2f, 1)
        };
    }
}
