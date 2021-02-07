package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.diagnose;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.MultiItemTypeAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.diagnose.DeviceDiagnoseHomeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.diagnose.DeviceDiagnoseItemChildBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.ui.CustomRecyclerView;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.util.ScreenUtil;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.itangqi.waveloadingview.WaveLoadingView;
import okhttp3.Call;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


/**
 * author hbzhou
 * date 2019/4/15 11:28
 * 设备诊断页面
 */
public class DeviceDiagnoseHomeActivity extends BaseActivity {

    protected WaveLoadingView mWaveLoadingView;

    protected ImageView mImageView;

    protected TextView mProgressTitle;

    // 需要颜色渐变的布局和界面
    protected TextView mProgressBar;
    private RelativeLayout mProgressBarLayout;
    private LinearLayout mDiagnoseBackLayout;

    protected int screenWidth;

    private RecyclerView mRecyclerView;

    protected List<DeviceDiagnoseHomeBean> homeBeanList = new ArrayList<>();

    protected CommonAdapter adapter;

    protected ValueAnimator valueAnimator;

    protected RotateAnimation rotateAnimation;

    protected int diagnoseScore = 0;

    private Button mDiagnoseFinish;

    protected TextView mDiagnoseText;
    protected TextView mDiagnoseTextOne;

    private int animationValue;
    private int percentScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_device_diagnose_home);
        super.onCreate(savedInstanceState);

        mWaveLoadingView = findViewById(R.id.waveLoadingView);

        // Sets the length of the animation, default is 1000.
        mWaveLoadingView.setAnimDuration(3000);

        mDiagnoseText = findViewById(R.id.diagnose_text_score);
        mDiagnoseTextOne = findViewById(R.id.diagnose_text_score_one);

        mRecyclerView = findViewById(R.id.recyclerView);

        mImageView = findViewById(R.id.cir_rotate);

        ImageButton mBack = findViewById(R.id.diagnose_home_back);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mDiagnoseFinish = findViewById(R.id.diagnose_home_finish);

        mDiagnoseFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mProgressBar = findViewById(R.id.device_diagnose_progress_bar);
        mProgressBarLayout = findViewById(R.id.device_diagnose_progress_bar_layout);
        mDiagnoseBackLayout = findViewById(R.id.diagnose_home_diagnose_back);

        mProgressTitle = findViewById(R.id.device_diagnose_progress_title);

        screenWidth = ScreenUtil.getScreenWidth(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //显示view的宽度从0到screenWidth
        valueAnimator = ValueAnimator.ofInt(0, screenWidth);

        valueAnimator.setInterpolator(new LinearInterpolator());

        valueAnimator.setDuration(5000);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                final int h = (Integer) valueAnimator.getAnimatedValue();

                mProgressBar.getLayoutParams().width = h;

                mProgressBar.requestLayout();

                initProgressValue(h);
            }
        });

        initCircleViewAnimation();

        initDeviceData(getIntent().getStringExtra("projectId"));

        adapter = new CommonAdapter<DeviceDiagnoseHomeBean>(this, R.layout.item_device_diagnose_home_one, homeBeanList) {
            @Override
            protected void convert(ViewHolder holder, final DeviceDiagnoseHomeBean homeBean, int position) {
                TextView mItemTitleName = holder.getView(R.id.item_title_name);

                TextView mItemStatusName = holder.getView(R.id.item_status_name);

                ImageView mLoadingOk = holder.getView(R.id.item_loading_ok);

                GifImageView mGifDrawable = holder.getView(R.id.item_gif);

                TextView mItemChildTitle = holder.getView(R.id.item_device_title);

                CustomRecyclerView itemRecyclerView = holder.getView(R.id.item_recyclerView);

                ImageView itemDeviceImage = holder.getView(R.id.item_device_image);

                LinearLayout mItemChildLayout = holder.getView(R.id.item_child_layout);

                mItemTitleName.setText(homeBean.getName());

                final MediaController mc = new MediaController(DeviceDiagnoseHomeActivity.this);
                mc.setMediaPlayer((GifDrawable) mGifDrawable.getDrawable());

                mItemChildLayout.setVisibility(View.GONE);
                switch (homeBean.getProgressStatus()) {
                    case 1:
                        mGifDrawable.setVisibility(View.VISIBLE);
                        mItemStatusName.setVisibility(View.INVISIBLE);
                        mLoadingOk.setVisibility(View.INVISIBLE);

                        mc.show();
                        break;
                    case 2:
                        mGifDrawable.setVisibility(View.INVISIBLE);
                        mItemStatusName.setVisibility(View.INVISIBLE);
                        mLoadingOk.setVisibility(View.VISIBLE);
                        mc.hide();

                        break;
                    case 3:
                        mGifDrawable.setVisibility(View.INVISIBLE);
                        mItemStatusName.setVisibility(View.VISIBLE);
                        mLoadingOk.setVisibility(View.INVISIBLE);
                        mc.hide();

                        if (homeBean.getScore() == 100) {
                            mItemStatusName.setText(String.valueOf("正常"));
                            mItemStatusName.setTextColor(Color.parseColor("#2EC48C"));
                            itemDeviceImage.setImageDrawable(getResources().getDrawable(R.drawable.device_diagnose_nor));
                        } else {
                            mItemStatusName.setText(String.valueOf("异常"));
                            mItemStatusName.setTextColor(Color.parseColor("#FE6406"));
                            itemDeviceImage.setImageDrawable(getResources().getDrawable(R.drawable.device_diagnose_err));
                        }

                        // 遍历获得异常的项并在诊断动画完成之后才显示诊断项的诊断结果
                        mItemChildLayout.setVisibility(View.VISIBLE);
                        final List<DeviceDiagnoseItemChildBean> childBeans = new ArrayList<>();
                        for (DeviceDiagnoseItemChildBean bean : homeBean.getDiagnoseItemHistoryList()) {
                            if (bean.getResultType() != 1 && bean.getResultType() != 3) {
                                childBeans.add(bean);
                            }
                        }

                        String itemChildName = "共诊断了".concat(String.valueOf(homeBean.getDiagnoseItemHistoryList().size())).concat("项,");
                        if (childBeans.size() > 0) {
                            itemChildName = itemChildName.concat("以下").concat(String.valueOf(childBeans.size())).concat("项有问题");
                        } else {
                            itemChildName = itemChildName.concat("暂未发现问题");
                        }
                        mItemChildTitle.setText(itemChildName);

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DeviceDiagnoseHomeActivity.this);
                        itemRecyclerView.setLayoutManager(linearLayoutManager);

                        CommonAdapter childAdapter = new CommonAdapter<DeviceDiagnoseItemChildBean>(DeviceDiagnoseHomeActivity.this, R.layout.item_device_child_diagnose, childBeans) {
                            @Override
                            protected void convert(ViewHolder holder, DeviceDiagnoseItemChildBean childBean, int position) {
                                TextView childName = holder.getView(R.id.item_device_child_title);
                                childName.setText(childBean.getResult());
                            }
                        };

                        childAdapter.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                                Intent intent = new Intent(DeviceDiagnoseHomeActivity.this, DeviceDiagnoseDetailsActivity.class);
                                intent.putExtra("ID", homeBean.getID());
                                intent.putExtra("Name", homeBean.getName());
                                intent.putExtra("Score", homeBean.getScore());
                                intent.putExtra("ChildId", childBeans.get(position).getID());
                                startActivity(intent);
                            }

                            @Override
                            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                                return false;
                            }
                        });
                        itemRecyclerView.setAdapter(childAdapter);
                        break;
                }

            }
        };

        initAdapterItemClick();

        mRecyclerView.setAdapter(adapter);
    }

    protected void initAdapterItemClick() {
    }

    /**
     * 初始化水波纹周围的旋转动画 并每秒更新背景颜色
     * 优化动画进度中的耗时操作 解决了动画卡顿问题
     */
    protected void initCircleViewAnimation() {
        // 给WaveView 周围的旋转圈设置动画
        rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setDuration(1000); // 设置动画时间
        rotateAnimation.setInterpolator(new LinearInterpolator()); // 设置插入器
        rotateAnimation.setRepeatCount(5);
        //rotateAnimation.setRepeatMode(Animation.RELATIVE_TO_SELF);

        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (homeBeanList.size() > 0) {
                    mProgressTitle.setText(String.valueOf("正在诊断: " + homeBeanList.get(0).getName()));
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                animationValue = animationValue + screenWidth / 5;

                initListStatus(animationValue);

                initBackColor(percentScore);

                if (animationValue >= screenWidth) {
                    adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                            DeviceDiagnoseHomeBean homeBean = homeBeanList.get(position);
                            Intent intent = new Intent(DeviceDiagnoseHomeActivity.this, DeviceDiagnoseDetailsActivity.class);
                            intent.putExtra("ID", homeBean.getID());
                            intent.putExtra("Name", homeBean.getName());
                            intent.putExtra("Score", homeBean.getScore());
                            startActivity(intent);
                        }

                        @Override
                        public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                            return false;
                        }
                    });
                }
            }
        });
    }

    private void initProgressValue(int value) {

        if (screenWidth == 0 || homeBeanList.size() == 0) {

            return;
        }

        float percent = 1.0f * value / screenWidth;

        percentScore = Math.round(percent * diagnoseScore);

        mDiagnoseText.setText(String.valueOf((percentScore)));

        int position = Math.round(percent * 10f);

        if (position <= homeBeanList.size() - 1) {
            mProgressTitle.setText(String.valueOf("正在诊断: " + homeBeanList.get(position).getName()));
        }
    }

    /**
     * 处理诊断分析时下方列表状态变化和诊断标题文本
     *
     * @param value
     */
    private void initListStatus(int value) {

        int normalWidth = screenWidth / homeBeanList.size();

        switch (homeBeanList.size()) {
            case 1:
                DeviceDiagnoseHomeBean bean = homeBeanList.get(0);
                if (value >= normalWidth) {
                    bean.setProgressStatus(3);
                    //rotateAnimation.cancel();
                    mProgressTitle.setText("诊断完成!");
                    mWaveLoadingView.setProgressValue(75);
                    mWaveLoadingView.setAmplitudeRatio(70);
                    setFinishButtonColor();
                }
                break;
            case 2:
                DeviceDiagnoseHomeBean beanOne = homeBeanList.get(0);
                if (value >= normalWidth) {
                    beanOne.setProgressStatus(2);
                    // mProgressTitle.setText(String.valueOf("正在诊断: " + homeBeanList.get(1).getName()));
                }

                DeviceDiagnoseHomeBean beanTwo = homeBeanList.get(1);
                if (value >= 2 * normalWidth) {
                    beanOne.setProgressStatus(3);
                    beanTwo.setProgressStatus(3);
                    //rotateAnimation.cancel();
                    mProgressTitle.setText("诊断完成!");
                    mWaveLoadingView.setProgressValue(75);
                    mWaveLoadingView.setAmplitudeRatio(70);
                    setFinishButtonColor();
                }
                break;

            case 3:
                DeviceDiagnoseHomeBean beanThree = homeBeanList.get(0);
                if (value >= normalWidth) {
                    beanThree.setProgressStatus(2);
                    //mProgressTitle.setText(String.valueOf("正在诊断: " + homeBeanList.get(1).getName()));
                }

                DeviceDiagnoseHomeBean beanFour = homeBeanList.get(1);
                if (value >= 2 * normalWidth) {
                    beanFour.setProgressStatus(2);
                    // mProgressTitle.setText(String.valueOf("正在诊断: " + homeBeanList.get(2).getName()));
                }

                DeviceDiagnoseHomeBean beanFive = homeBeanList.get(2);
                if (value >= 3 * normalWidth) {
                    beanThree.setProgressStatus(3);
                    beanFour.setProgressStatus(3);
                    beanFive.setProgressStatus(3);
                    //rotateAnimation.cancel();
                    mProgressTitle.setText("诊断完成!");
                    mWaveLoadingView.setProgressValue(75);
                    mWaveLoadingView.setAmplitudeRatio(70);
                    setFinishButtonColor();
                }
                break;

            case 4:
                DeviceDiagnoseHomeBean beanSix = homeBeanList.get(0);
                if (value >= normalWidth) {
                    beanSix.setProgressStatus(2);
                    //mProgressTitle.setText(String.valueOf("正在诊断: " + homeBeanList.get(1).getName()));
                }

                DeviceDiagnoseHomeBean beanSeven = homeBeanList.get(1);
                if (value >= 2 * normalWidth) {
                    beanSeven.setProgressStatus(2);
                    //mProgressTitle.setText(String.valueOf("正在诊断: " + homeBeanList.get(2).getName()));
                }

                DeviceDiagnoseHomeBean beanEight = homeBeanList.get(2);
                if (value >= 3 * normalWidth) {
                    beanEight.setProgressStatus(2);
                    //mProgressTitle.setText(String.valueOf("正在诊断: " + homeBeanList.get(3).getName()));
                }

                DeviceDiagnoseHomeBean beanNine = homeBeanList.get(3);
                if (value >= 4 * normalWidth) {
                    beanSix.setProgressStatus(3);
                    beanSeven.setProgressStatus(3);
                    beanEight.setProgressStatus(3);
                    beanNine.setProgressStatus(3);
                    //rotateAnimation.cancel();
                    mProgressTitle.setText("诊断完成!");
                    mWaveLoadingView.setProgressValue(75);
                    mWaveLoadingView.setAmplitudeRatio(70);
                    setFinishButtonColor();
                }
                break;

            case 5:
                DeviceDiagnoseHomeBean beanTen = homeBeanList.get(0);
                if (value >= normalWidth) {
                    beanTen.setProgressStatus(2);
                    // mProgressTitle.setText(String.valueOf("正在诊断: " + homeBeanList.get(1).getName()));
                }

                DeviceDiagnoseHomeBean beanEleven = homeBeanList.get(1);
                if (value >= 2 * normalWidth) {
                    beanEleven.setProgressStatus(2);
                    //mProgressTitle.setText(String.valueOf("正在诊断: " + homeBeanList.get(2).getName()));
                }

                DeviceDiagnoseHomeBean beanTwelve = homeBeanList.get(2);
                if (value >= 3 * normalWidth) {
                    beanTwelve.setProgressStatus(2);
                    //mProgressTitle.setText(String.valueOf("正在诊断: " + homeBeanList.get(3).getName()));
                }

                DeviceDiagnoseHomeBean beanThirteen = homeBeanList.get(3);
                if (value >= 4 * normalWidth) {
                    beanThirteen.setProgressStatus(2);
                    //mProgressTitle.setText(String.valueOf("正在诊断: " + homeBeanList.get(4).getName()));
                }

                DeviceDiagnoseHomeBean beanFourteen = homeBeanList.get(4);
                if (value >= 5 * normalWidth) {
                    beanTen.setProgressStatus(3);
                    beanEleven.setProgressStatus(3);
                    beanTwelve.setProgressStatus(3);
                    beanThirteen.setProgressStatus(3);
                    beanFourteen.setProgressStatus(3);
                    //rotateAnimation.cancel();
                    mProgressTitle.setText("诊断完成!");
                    mWaveLoadingView.setProgressValue(75);
                    mWaveLoadingView.setAmplitudeRatio(70);
                    setFinishButtonColor();
                }
                break;

            default:
                if (value >= homeBeanList.size() * normalWidth) {
                    for (DeviceDiagnoseHomeBean deviceBean : homeBeanList) {
                        deviceBean.setProgressStatus(3);
                    }
                    mProgressTitle.setText("诊断完成!");
                    mWaveLoadingView.setProgressValue(75);
                    mWaveLoadingView.setAmplitudeRatio(70);
                    setFinishButtonColor();
                }
                break;
        }

        adapter.notifyDataSetChanged();
    }

    protected void setFinishButtonColor() {
        if (diagnoseScore < 80) {
            mDiagnoseFinish.setBackground(getResources().getDrawable(R.drawable.diagnose_home_btn_shape));
        } else if (diagnoseScore < 100) {
            mDiagnoseFinish.setBackground(getResources().getDrawable(R.drawable.diagnose_home_btn_shape_yellow));
        } else if (diagnoseScore == 100) {
            mDiagnoseFinish.setBackground(getResources().getDrawable(R.drawable.diagnose_home_btn_shape_green));
        }
        mDiagnoseFinish.setVisibility(View.VISIBLE);
    }

    /**
     * 处理诊断分析整体背景颜色随分数的变化而变化
     *
     * @param roundValue
     *
     */
    protected void initBackColor(int roundValue) {
        if (roundValue < 80) {
            mProgressBar.setBackgroundColor(getResources().getColor(R.color.device_diagnose_orange_bar_selected));
            mProgressBarLayout.setBackgroundColor(getResources().getColor(R.color.device_diagnose_orange_bar_normal));
            mDiagnoseBackLayout.setBackgroundColor(getResources().getColor(R.color.device_diagnose_orange));

            StatusBarUtil.setColor(this, getResources().getColor(R.color.device_diagnose_orange), 0);
            mWaveLoadingView.setBorderColor(getResources().getColor(R.color.device_diagnose_orange));
            mWaveLoadingView.setCenterTitleColor(getResources().getColor(R.color.device_diagnose_orange));

            mDiagnoseText.setTextColor(getResources().getColor(R.color.device_diagnose_orange));
            mDiagnoseTextOne.setTextColor(getResources().getColor(R.color.device_diagnose_orange));
        } else if (roundValue < 100) {
            mProgressBar.setBackgroundColor(getResources().getColor(R.color.device_diagnose_yellow_bar_selected));
            mProgressBarLayout.setBackgroundColor(getResources().getColor(R.color.device_diagnose_yellow_bar_normal));
            mDiagnoseBackLayout.setBackgroundColor(getResources().getColor(R.color.device_diagnose_yellow));

            StatusBarUtil.setColor(this, getResources().getColor(R.color.device_diagnose_yellow), 0);
            mWaveLoadingView.setBorderColor(getResources().getColor(R.color.device_diagnose_yellow));
            mWaveLoadingView.setCenterTitleColor(getResources().getColor(R.color.device_diagnose_yellow));

            mDiagnoseText.setTextColor(getResources().getColor(R.color.device_diagnose_yellow));
            mDiagnoseTextOne.setTextColor(getResources().getColor(R.color.device_diagnose_yellow));
        } else if (roundValue == 100) {
            mProgressBar.setBackgroundColor(getResources().getColor(R.color.device_diagnose_green_bar_selected));
            mProgressBarLayout.setBackgroundColor(getResources().getColor(R.color.device_diagnose_green_bar_normal));
            mDiagnoseBackLayout.setBackgroundColor(getResources().getColor(R.color.device_diagnose_green));

            StatusBarUtil.setColor(this, getResources().getColor(R.color.device_diagnose_green), 0);
            mWaveLoadingView.setBorderColor(getResources().getColor(R.color.device_diagnose_green));
            mWaveLoadingView.setCenterTitleColor(getResources().getColor(R.color.device_diagnose_green));

            mDiagnoseText.setTextColor(getResources().getColor(R.color.device_diagnose_green));
            mDiagnoseTextOne.setTextColor(getResources().getColor(R.color.device_diagnose_green));
        }
    }

    protected void initDeviceData(String projectId) {

        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("projectID", projectId);

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_DEVICE_DIAGNOSE_HOME_GET_INFO)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.showLoge("执行设备诊断返回8989---", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                JSONObject json = jsonObject.getJSONObject("data");
                                if (!json.getBoolean("IsSuccess")) {
                                    ToastUtils.showLong(json.getString("ResultMsg"));
                                    finish();
                                    return;
                                }
                                List<DeviceDiagnoseHomeBean> homeBeans = new Gson().fromJson(json.getJSONArray("EquipmentDiagnoseHistoryEntities").toString(),
                                        new TypeToken<List<DeviceDiagnoseHomeBean>>() {
                                        }.getType());

                                if (homeBeans == null || homeBeans.size() == 0) {
                                    ToastUtils.showLong("未获取到设备诊断信息,请稍后再试!");
                                    finish();
                                    return;
                                }

                                diagnoseScore = json.getInt("AvgEquipmentDiagnoseScore");

                                homeBeanList.addAll(homeBeans);

                                //测试test
//                                DeviceDiagnoseHomeBean bean0 = new DeviceDiagnoseHomeBean();
//                                bean0.setID("b4fe8dd20d154b23890837e6698e6530");
//                                bean0.setName("正压风机系统");
//                                homeBeanList.add(bean0);
//                                DeviceDiagnoseHomeBean bean2 = new DeviceDiagnoseHomeBean();
//                                bean2.setID("b4fe8dd20d154b23890837e6698e6531");
//                                bean2.setName("机械排烟系统");
//                                homeBeanList.add(bean2);
//                                DeviceDiagnoseHomeBean bean3 = new DeviceDiagnoseHomeBean();
//                                bean3.setID("b4fe8dd20d154b23890837e6698e6532");
//                                bean3.setName("大空间灭火给水系统");
//                                homeBeanList.add(bean3);
//                                DeviceDiagnoseHomeBean bean4 = new DeviceDiagnoseHomeBean();
//                                bean4.setID("b4fe8dd20d154b23890837e6698e6533");
//                                bean4.setName("消防栓系统");
//                                homeBeanList.add(bean4);
//                                DeviceDiagnoseHomeBean bean5 = new DeviceDiagnoseHomeBean();
//                                bean5.setID("b4fe8dd20d154b23890837e6698e6534");
//                                bean5.setName("消防栓给水系统");
//                                homeBeanList.add(bean5);

                                valueAnimator.start();

                                mImageView.startAnimation(rotateAnimation);

                                for (DeviceDiagnoseHomeBean bean1 : homeBeanList) {
                                    bean1.setProgressStatus(1);
                                }

                                adapter.notifyDataSetChanged();

                            } else {
                                ToastUtils.showLong(jsonObject.getString("message"));
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        valueAnimator.cancel();
        rotateAnimation.cancel();
    }

    @Override
    protected void setBarLintColor() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.device_diagnose_orange), 0);
    }
}
