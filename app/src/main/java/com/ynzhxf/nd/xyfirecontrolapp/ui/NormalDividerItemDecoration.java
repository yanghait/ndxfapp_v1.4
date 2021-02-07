package com.ynzhxf.nd.xyfirecontrolapp.ui;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;


/**
 * author hbzhou
 * date 2019/3/25 14:41
 */
public class NormalDividerItemDecoration extends DividerItemDecoration {
    /**
     * Creates a divider {@link RecyclerView.ItemDecoration} that can be used with a
     * {@link }.
     *
     * @param context     Current context, it will be used to access resources.
     * @param orientation Divider orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    public NormalDividerItemDecoration(Context context, int orientation) {
        super(context, orientation);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        // hide the divider for the last child
        if (position == parent.getAdapter().getItemCount() - 1) {
            outRect.setEmpty();
        } else {
            super.getItemOffsets(outRect, view, parent, state);
        }
    }
}
