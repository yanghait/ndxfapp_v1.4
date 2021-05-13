package com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon;


import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.ui.GridDividerDecoration;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon.adapter.SmlvideoAdapter;

public class SmlVideoListActivity extends BaseActivity {

    RecyclerView sml_video_recycle;

    SmlvideoAdapter smlvideoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sml_video_list);
        super.onCreate(savedInstanceState);
        setBarTitle("视频监控");
        initLayout();
    }

    private void initLayout() {
        sml_video_recycle = this.findViewById(R.id.sml_video_recycle);
        smlvideoAdapter = new SmlvideoAdapter();
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        GridDividerDecoration dividerDecoration = new GridDividerDecoration(2, getResources().getDimensionPixelSize(R.dimen.margin_top_bottom), true);
        sml_video_recycle.addItemDecoration(dividerDecoration);
        sml_video_recycle.setLayoutManager(manager);
        sml_video_recycle.setAdapter(smlvideoAdapter);

        smlvideoAdapter.setOnItemClickListener(videoPath -> {
            Intent intent = new Intent(SmlVideoListActivity.this, SmlVideoPlayActivity.class);
            intent.putExtra("viewPath", videoPath);
            startActivity(intent);
        });
    }
}