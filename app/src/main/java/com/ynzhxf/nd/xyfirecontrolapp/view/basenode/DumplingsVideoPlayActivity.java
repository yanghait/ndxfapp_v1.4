package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;

import android.os.Bundle;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;




/**
 * author hbzhou
 * date 2019/4/3 13:56
 */
public class DumplingsVideoPlayActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dumplings_video_play);
        super.onCreate(savedInstanceState);

//        JzvdStd jzvdStd = findViewById(R.id.video_player);
//
//        String url = getIntent().getStringExtra("url");
//        String title = getIntent().getStringExtra("title");
//
//        jzvdStd.setUp(url, title, Jzvd.SCREEN_WINDOW_FULLSCREEN);
    }

    @Override
    public void onBackPressed() {
//        if (Jzvd.backPress()) {
//            return;
//        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Jzvd.resetAllVideos();
    }
}
