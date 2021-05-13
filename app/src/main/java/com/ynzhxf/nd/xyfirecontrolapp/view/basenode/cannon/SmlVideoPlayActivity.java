package com.ynzhxf.nd.xyfirecontrolapp.view.basenode.cannon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLOnBufferingUpdateListener;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.PLOnVideoSizeChangedListener;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.squareup.picasso.Picasso;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.VideoChannelBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IVideoPlayHeartPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IVideoPlayPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl.NodeBasePersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.tool.utils.Config;
import com.ynzhxf.nd.xyfirecontrolapp.tool.utils.Utils;
import com.ynzhxf.nd.xyfirecontrolapp.tool.widget.MediaController;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;


/**
 * 视频播放页面
 */
public class SmlVideoPlayActivity extends BaseActivity implements IVideoPlayPersenter.IVideoPlayView, IVideoPlayHeartPersenter.IVideoVideoPlayHeartView {
    private PLVideoTextureView mVideoView;
    private int mRotation = 0;
    private int mDisplayAspectRatio = PLVideoTextureView.ASPECT_RATIO_4_3; //default//ASPECT_RATIO_FIT_PARENT
    private TextView mStatInfoTextView;

    //监控描述
    private TextView txtName;

    //容器
    private FrameLayout fl_contaner;


    private boolean mIsLiveStreaming;//是否是直播
    private String videoPath;//播放地址

    //项目对象
    private ProjectNodeBean projectNodeBean;

    //视频对象
    private VideoChannelBean videoChannelBean;

    //图片对象
    private String imgUrl;

    //设置是否是全屏
    private boolean isFull = false;
    //心跳是否继续发送
    private boolean needSendheart = true;


    //心跳发送周期
    private int sendTime = 10000;
    //计时间
    private int hasLoseTime = 0;

    IVideoPlayPersenter videoPlayPersenter;
    IVideoPlayHeartPersenter videoPlayHeartPersenter;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_video_play);
        super.onCreate(savedInstanceState);
        setBarTitle("监控");

        Intent intent = getIntent();
        projectNodeBean = (ProjectNodeBean) intent.getSerializableExtra("project");
        videoChannelBean = (VideoChannelBean) intent.getSerializableExtra("data");
        imgUrl = intent.getStringExtra("Url");

        //LogUtils.showLoge("url1212---",imgUrl);

        //LogUtils.showLoge("url1919---",videoChannelBean.getPlayAddress());

        txtName = findViewById(R.id.txt_name);
        fl_contaner = findViewById(R.id.fl_contaner);
//        txtName.setText(projectNodeBean.getAddress() + videoChannelBean.getCameraName());

        videoPlayPersenter = NodeBasePersenterFactory.getVideoPlayPersenterImpl(this);
        addPersenter(videoPlayPersenter);
        videoPlayHeartPersenter = NodeBasePersenterFactory.getVideoPlayHeartPersenterImpl(this);
        addPersenter(videoPlayHeartPersenter);
        mIsLiveStreaming = true; //true直播   false点播
        //获取播放控件
        mVideoView = findViewById(R.id.VideoView);

        //获取加载进度
        View loadingView = findViewById(R.id.LoadingView);
        mVideoView.setBufferingIndicator(loadingView);

        //设置初始截图
        ImageView coverView = findViewById(R.id.CoverView);
        Picasso.get().load(imgUrl).into(coverView);
        mVideoView.setCoverView(coverView);

        //播放码率
        mStatInfoTextView = findViewById(R.id.StatInfoTextView);

        int codec = AVOptions.MEDIA_CODEC_SW_DECODE; //AVOptions.MEDIA_CODEC_SW_DECODE 软解   AVOptions.MEDIA_CODEC_SW_DECODE 硬解    AVOptions.MEDIA_CODEC_AUTO//自动
        //设置参数
        AVOptions options = new AVOptions();
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);//设置连接超时
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, mIsLiveStreaming ? 1 : 0);//设置是直播还是点播 1 是直播  0点播(点播功能可以缓存)
        options.setInteger(AVOptions.KEY_MEDIACODEC, codec);//设置解码方式
        boolean disableLog = getIntent().getBooleanExtra("disable-log", false);//

        options.setInteger(AVOptions.KEY_LOG_LEVEL, 0);//关闭日志 5开启  0关闭

        boolean cache = getIntent().getBooleanExtra("cache", false);//离线缓存
        if (!mIsLiveStreaming && cache) {
            options.setString(AVOptions.KEY_CACHE_DIR, Config.DEFAULT_CACHE_DIR);
        }
        if (!mIsLiveStreaming) {//为点播的时候，设置开始时间(不生效)
            int startPos = getIntent().getIntExtra("start-pos", 0);
            options.setInteger(AVOptions.KEY_START_POSITION, startPos * 1000);
        }
        mVideoView.setAVOptions(options);


        MediaController mediaController = new MediaController(this, !mIsLiveStreaming, mIsLiveStreaming);
        mediaController.setOnClickSpeedAdjustListener(mOnClickSpeedAdjustListener);
        mVideoView.setMediaController(mediaController);

        //mVideoView.setDisplayAspectRatio(mDisplayAspectRatio);

        mVideoView.setOnInfoListener(mOnInfoListener);
        mVideoView.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
        mVideoView.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        mVideoView.setOnCompletionListener(mOnCompletionListener);
        mVideoView.setOnErrorListener(mOnErrorListener);

        mVideoView.setLooping(false);//不循环播放（点播生效）

//        videoPlayPersenter.doVideoPlay(videoChannelBean.getID());
        videoPath = getIntent().getStringExtra("viewPath");
        handler.sendEmptyMessage(0x124);
        progressDialog = showProgress(this, "加载中...", false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
        needSendheart = false;
    }

    /**
     * 设置屏幕旋转
     *
     * @param v
     */
    public void onClickRotate(View v) {
        mRotation = (mRotation + 90) % 360;
        mVideoView.setDisplayOrientation(mRotation);
    }

    public void onClickFullBtn(View v) {
        if (!isFull) {
            isFull = true;
            mVideoView.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
            mVideoView.setDisplayOrientation(90);
            toolbar.setVisibility(View.GONE);

        } else {
            toolbar.setVisibility(View.VISIBLE);
            isFull = false;
            mVideoView.setDisplayOrientation(0);
            mVideoView.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_FIT_PARENT);
        }
        changeViewState();
    }

    /**
     * 改变视频的窗体大小
     *
     * @param v
     */
    public void onClickSwitchScreen(View v) {
        mDisplayAspectRatio = (mDisplayAspectRatio + 1) % 5;
        mVideoView.setDisplayAspectRatio(mDisplayAspectRatio);
        switch (mVideoView.getDisplayAspectRatio()) {
            case PLVideoTextureView.ASPECT_RATIO_ORIGIN:
                Utils.showToastTips(this, "Origin mode");
                break;
            case PLVideoTextureView.ASPECT_RATIO_FIT_PARENT:
                Utils.showToastTips(this, "Fit parent !");
                break;
            case PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT:
                Utils.showToastTips(this, "Paved parent !");
                break;
            case PLVideoTextureView.ASPECT_RATIO_16_9:
                Utils.showToastTips(this, "16 : 9 !");
                break;
            case PLVideoTextureView.ASPECT_RATIO_4_3:
                Utils.showToastTips(this, "4 : 3 !");
                break;
            default:
                break;
        }
    }

    private PLOnInfoListener mOnInfoListener = new PLOnInfoListener() {
        @Override
        public void onInfo(int what, int extra) {
            Log.i(TAG, "OnInfo, what = " + what + ", extra = " + extra);
            switch (what) {
                case PLOnInfoListener.MEDIA_INFO_BUFFERING_START:
                    break;
                case PLOnInfoListener.MEDIA_INFO_BUFFERING_END:
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_RENDERING_START://连接成功回掉
                    //hideProgressDig();
                    progressDialog.dismiss();
                    //Utils.showToastTips(VideoPlayActivity.this, "First video render time: " + extra + "ms");
                    break;
                case PLOnInfoListener.MEDIA_INFO_AUDIO_RENDERING_START:
                    Log.i(TAG, "First audio render time: " + extra + "ms");
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_FRAME_RENDERING:
                    Log.i(TAG, "video frame rendering, ts = " + extra);
                    break;
                case PLOnInfoListener.MEDIA_INFO_AUDIO_FRAME_RENDERING:
                    Log.i(TAG, "audio frame rendering, ts = " + extra);
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_GOP_TIME:
                    Log.i(TAG, "Gop Time: " + extra);
                    break;
                case PLOnInfoListener.MEDIA_INFO_SWITCHING_SW_DECODE:
                    Log.i(TAG, "您的设备不支持播放!");
                    break;
                case PLOnInfoListener.MEDIA_INFO_METADATA:
                    Log.i(TAG, mVideoView.getMetadata().toString());
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_BITRATE:
                case PLOnInfoListener.MEDIA_INFO_VIDEO_FPS://fps计算
                    updateStatInfo();
                    break;
                case PLOnInfoListener.MEDIA_INFO_CONNECTED:
                    Log.i(TAG, "Connected !");
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                    Log.i(TAG, "Rotation changed: " + extra);
                    break;
                default:
                    break;
            }
        }
    };

    private PLOnErrorListener mOnErrorListener = new PLOnErrorListener() {
        @Override
        public boolean onError(int errorCode) {
            Log.e(TAG, "Error happened, errorCode = " + errorCode);
            switch (errorCode) {
                case PLOnErrorListener.ERROR_CODE_IO_ERROR:
                    Utils.showToastTips(SmlVideoPlayActivity.this, "网络异常!");
                    progressDialog.dismiss();
                    return false;
                case PLOnErrorListener.ERROR_CODE_OPEN_FAILED:
                    Utils.showToastTips(SmlVideoPlayActivity.this, "播放器打开失败 !");
                    progressDialog.dismiss();
                    break;
                case PLOnErrorListener.ERROR_CODE_SEEK_FAILED:
                    Utils.showToastTips(SmlVideoPlayActivity.this, "拖动失败!");
                    break;
                default:
                    Utils.showToastTips(SmlVideoPlayActivity.this, "未知类型异常 !");
                    progressDialog.dismiss();
                    break;
            }
            finish();
            return true;
        }
    };

    private PLOnCompletionListener mOnCompletionListener = new PLOnCompletionListener() {
        @Override
        public void onCompletion() {
            Log.i(TAG, "Play Completed !");
            Utils.showToastTips(SmlVideoPlayActivity.this, "Play Completed !");

            progressDialog.dismiss();

        }
    };

    private PLOnBufferingUpdateListener mOnBufferingUpdateListener = new PLOnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(int precent) {
            Log.i(TAG, "onBufferingUpdate: " + precent);
        }
    };

    private PLOnVideoSizeChangedListener mOnVideoSizeChangedListener = new PLOnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(int width, int height) {
            Log.i(TAG, "onVideoSizeChanged: width = " + width + ", height = " + height);
        }
    };

    private MediaController.OnClickSpeedAdjustListener mOnClickSpeedAdjustListener = new MediaController.OnClickSpeedAdjustListener() {
        @Override
        public void onClickNormal() {
            // 0x0001/0x0001 = 2
            mVideoView.setPlaySpeed(0X00010001);
        }

        @Override
        public void onClickFaster() {
            // 0x0002/0x0001 = 2
            mVideoView.setPlaySpeed(0X00020001);
        }

        @Override
        public void onClickSlower() {
            // 0x0001/0x0002 = 0.5
            mVideoView.setPlaySpeed(0X00010002);
        }
    };

    private void updateStatInfo() {
        long bitrate = mVideoView.getVideoBitrate() / 1024;
        final String stat = bitrate + "kbps, " + mVideoView.getVideoFps() + "fps";
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatInfoTextView.setText(stat);
            }
        });
    }

    //改变全屏状态
    private void changeViewState() {
        if (isFull) {
            txtName.setVisibility(View.GONE);
            //fl_contaner.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mStatInfoTextView.setVisibility(View.VISIBLE);
        } else {
            txtName.setVisibility(View.VISIBLE);
            //fl_contaner.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 350));
            mStatInfoTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void callBackVideoPlayHeartModel(String result) {

    }

    @Override
    public void callBackError(ResultBean<String, String> resultBean, String action) {
        super.callBackError(resultBean, action);
        progressDialog.dismiss();
    }

    @Override
    public void callBackVideoPlay(ResultBean<String, String> result) {

        if (result.isSuccess()) {

            videoPath = result.getData();
            if (StringUtils.isEmpty(videoPath)) {
                progressDialog.dismiss();
                ToastUtils.showLong("未发现视频流!");
                finish();
                return;
            }
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(20);
                        handler.sendEmptyMessage(0x124);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } else {
            HelperView.Toast(this, result.getMessage());
            finish();
        }
    }

    private Thread th = new Thread() {
        @Override
        public void run() {
            while (needSendheart) {
                try {
                    Thread.sleep(1000);
                    hasLoseTime += 1000;
                    if (hasLoseTime >= sendTime) {
                        hasLoseTime = 0;
                        handler.sendEmptyMessage(0x123);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
//                videoPlayHeartPersenter.doVideoPlayHeart(videoChannelBean.getID());
            }
            if (msg.what == 0x124) {
                mVideoView.setVideoPath(videoPath);
                th.start();

            }
        }
    };
}
