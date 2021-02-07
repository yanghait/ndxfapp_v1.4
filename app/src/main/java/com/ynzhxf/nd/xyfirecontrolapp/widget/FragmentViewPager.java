package com.ynzhxf.nd.xyfirecontrolapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;


/**
 * author hbzhou
 * date 2019/6/21 11:36
 * 切换时能自适应fragment高度的viewpager
 */
public class FragmentViewPager extends ViewPager {

    private int[] map = new int[]{0, 0, 0};
    private int currentPage;

    public FragmentViewPager(@NonNull Context context) {
        super(context);
    }

    public FragmentViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        if (map.length > currentPage) {
            height = map[currentPage];
        }
        if (height == 0) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                if (h > height) height = h;
            }
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 在切换tab的时候，重置ViewPager的高度
     *
     * @param current
     */
    public void resetHeight(int current) {
        this.currentPage = current;
        //LogUtils.showLoge("选择的viewpager位置高度~~~~", map[0] + "~~~~" + map[1] + "~~~~" + map[2]);
        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        if (map.length > currentPage) {
            if (params == null) {
                params = new MarginLayoutParams(LayoutParams.MATCH_PARENT, map[current]);
            } else {
                params.height = map[current];
            }
            setLayoutParams(params);
        }
    }

    /**
     * 获取、存储每一个tab的高度，在需要的时候显示存储的高度
     *
     * @param current tab的position
     * @param height  当前tab的高度
     */
    public void addHeight(int current, int height) {
        map[current] = height;
    }
}
