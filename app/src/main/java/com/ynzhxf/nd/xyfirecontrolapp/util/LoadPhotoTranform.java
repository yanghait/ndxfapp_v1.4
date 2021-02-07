package com.ynzhxf.nd.xyfirecontrolapp.util;

import android.graphics.Bitmap;
import android.widget.LinearLayout;

import com.squareup.picasso.Transformation;

public class LoadPhotoTranform implements Transformation {

    private LinearLayout mContentView;

    public LoadPhotoTranform(LinearLayout mContentView) {
        this.mContentView = mContentView;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        return source;
    }

    @Override
    public String key() {
        return null;
    }
}
