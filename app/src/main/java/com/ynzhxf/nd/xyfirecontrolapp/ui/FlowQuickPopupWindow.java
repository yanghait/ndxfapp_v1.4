package com.ynzhxf.nd.xyfirecontrolapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.share.FileTypeAdapter;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import razerdp.basepopup.BasePopupWindow;


/**
 * author hbzhou
 * date 2019/2/28 10:10
 */
public class FlowQuickPopupWindow extends BasePopupWindow {


    private TagAdapter mAdapter;

    public static int checkItemPosition = 0;

    public FlowQuickPopupWindow(final Context context, List<String> mTitleList, final FileTypeAdapter.OnClickSelectListener callBack) {
        super(context);

        TagFlowLayout mFlowLayout = findViewById(R.id.tag_flow_layout);

        mFlowLayout.setAdapter(mAdapter = new TagAdapter<String>(mTitleList) {
            @Override
            public View getView(FlowLayout parent, final int position, String title) {

                View view = LayoutInflater.from(context).inflate(R.layout.item_file_type, parent, false);

                Button mTitle = view.findViewById(R.id.file_item_text);

                mTitle.setText(title);

                mTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkItemPosition = position;
                        notifyDataChanged();
                        dismiss();
                        callBack.onClick(view, position);
                    }
                });

                if (checkItemPosition == position) {

                    setSelected(checkItemPosition, title);

                    mTitle.setTextColor(context.getResources().getColor(R.color.primary_text_color));

                    mTitle.setBackgroundResource(R.drawable.inspect_home_shape2);

                } else {

                    mTitle.setTextColor(context.getResources().getColor(R.color.black));

                    mTitle.setBackgroundResource(R.drawable.inspect_home_shape1);

                }

                return view;
            }
        });

        mAdapter.notifyDataChanged();
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.base_popup_flowlayout);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultAlphaAnimation(true);
    }
}
