package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;

import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.listener.ClickListener;
import com.cjt2325.cameralibrary.listener.ErrorListener;
import com.cjt2325.cameralibrary.listener.JCameraListener;
import com.jaeger.library.StatusBarUtil;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.util.FileOutUtil;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;

import java.io.File;


/**
 * author hbzhou
 * date 2019/3/27 14:00
 */
public class VideoPhotoSelectActivity extends BaseActivity {

    private JCameraView jCameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_video_photo_select);
        super.onCreate(savedInstanceState);
        setBarTitle("拍照或拍视频");

//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.hide();
//        }

//        if (Build.VERSION.SDK_INT >= 19) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        } else {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
//            decorView.setSystemUiVisibility(option);
//        }

        //1.1.1
        jCameraView = findViewById(R.id.camera_view);

        //设置视频保存路径
        jCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "JCamera");

        //设置只能录像或只能拍照或两种都可以（默认两种都可以）
        jCameraView.setFeatures(JCameraView.BUTTON_STATE_BOTH);

        //设置视频质量
        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);

        //JCameraView监听
        jCameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {

            }

            @Override
            public void AudioPermissionError() {

            }
        });
        jCameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //LogUtils.showLoge("获取到的图片0000---", "00000" + FileUtil.saveBitmap("jCamera", bitmap));

                //LogUtils.showLoge("输出本应用缓存路径09009---", getCacheDir().getAbsolutePath() + "~~~~");

                String path = FileOutUtil.saveBitmap(getCacheDir().getAbsolutePath(), bitmap);

                Intent intent = new Intent();

                setResult(Activity.RESULT_OK, intent.putExtra("path", path));

                finish();
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {

                //LogUtils.showLoge("获取视频路径09876----", "11111"+"~~~~"+url);

                String firstPath = FileOutUtil.saveBitmap(getCacheDir().getAbsolutePath(), firstFrame);

                Intent intent = new Intent();

                intent.putExtra("firstFrame", firstPath);

                intent.putExtra("path", url);

                setResult(Activity.RESULT_OK, intent);

                finish();
            }
        });
        //左边按钮点击事件
        jCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                finish();

                //LogUtils.showLoge("获取视频路径22222----", "22222"+"~~~~"+"点击左边按钮");
            }
        });
        //右边按钮点击事件
        jCameraView.setRightClickListener(new ClickListener() {
            @Override
            public void onClick() {
                //LogUtils.showLoge("获取视频路径33333----", "33333"+"~~~~"+"点击右边按钮");
            }
        });
    }

    @Override
    protected void setBarLintColor() {
        //
        //StatusBarUtil.setColor(this, 0xffffff, 80);

        StatusBarUtil.setTranslucent(this,255);

        //StatusBarUtil.setTranslucentForImageView(this, 0, jCameraView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        jCameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jCameraView.onPause();
    }
}
