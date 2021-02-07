package com.ynzhxf.nd.xyfirecontrolapp.ui;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;

public class MyTabLayoutNew extends MyTabLayout {
    public MyTabLayoutNew(Context context) {
        super(context);
    }

    public MyTabLayoutNew(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTabLayoutNew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initTabMinWidth() {

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int tabMinWidth = screenWidth / 3;
        Field field;
        try {
            field = TabLayout.class.getDeclaredField(SCROLLABLE_TAB_MIN_WIDTH);
            field.setAccessible(true);
            field.set(this, tabMinWidth);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
