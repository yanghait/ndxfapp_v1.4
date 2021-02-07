package com.ynzhxf.nd.xyfirecontrolapp.ui;


import android.view.View;

/**
 * author hbzhou
 * date 2019/4/23 15:09
 */
public class ViewWrapper {
    private View mTarget;

    public ViewWrapper(View target) {
        mTarget = target;
    }

    public int getWidth() {
        return mTarget.getLayoutParams().width;
    }

    public void setWidth(int width) {
        mTarget.getLayoutParams().width = width;
        mTarget.requestLayout();

        //mTarget.layout(mTarget.getLeft(), mTarget.getTop(), width, mTarget.getBottom());
        //mTarget.invalidate();
    }
}
