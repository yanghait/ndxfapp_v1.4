package com.ynzhxf.nd.xyfirecontrolapp.ui;

import android.view.animation.LinearInterpolator;


/**
 * author hbzhou
 * date 2019/4/23 14:46
 */
public class SpecialLinearInterpolator extends LinearInterpolator {

    @Override
    public float getInterpolation(float input) {
        if (Math.round(input) % 5 == 0) {
            return input;
        } else {
            return 0;
        }
    }
}
