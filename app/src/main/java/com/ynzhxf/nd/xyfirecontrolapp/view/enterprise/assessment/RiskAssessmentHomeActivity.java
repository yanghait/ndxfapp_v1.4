package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.assessment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.MultiItemTypeAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.assessment.BuildingListEntityBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.ui.NormalDividerItemDecoration;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
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


/**
 * author hbzhou
 * date 2019/4/28 10:56
 * 风险评估首页
 */
public class RiskAssessmentHomeActivity extends BaseActivity implements View.OnClickListener {

    protected RotateAnimation rotateAnimation;
    private ImageView rotateImageView;

    private String mRiskTime;
    private double mRiskScore;

    private WaveLoadingView mWaveLoadingView;

    private TextView mAssessmentTime;

    private TextView mAssessmentTitle;
    private TextView mAssessmentDesc;

    private double mCurrentScore = 0.0;

    private LinearLayout mTitleBar;
    private LinearLayout mWaveViewLayout;


    private Button mStartAssessmentBtn;
    private TextView mHintMessage;

    private String projectId;

    private CommonAdapter adapter;

    private List<BuildingListEntityBean> buildingListBeans = new ArrayList<>();

    private TextView mRiskCount1;
    private TextView mRiskCount2;
    private TextView mRiskCount3;
    private TextView mRiskCount4;
    private TextView mRiskCount5;
    private TextView mRiskCount6;

    private boolean isLoadFirstData = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_risk_assessment_home);
        super.onCreate(savedInstanceState);
        projectId = getIntent().getStringExtra("projectId");

        String titleName = getIntent().getStringExtra("name");

        rotateImageView = findViewById(R.id.cir_rotate);

        mWaveLoadingView = findViewById(R.id.waveLoadingView);

        mAssessmentTime = findViewById(R.id.assessment_time);

        mAssessmentTitle = findViewById(R.id.risk_assessment_home_title);
        mAssessmentDesc = findViewById(R.id.risk_assessment_home_score_desc);

        mTitleBar = findViewById(R.id.title_bar);
        mWaveViewLayout = findViewById(R.id.risk_assessment_home_back);

        mWaveLoadingView.setAnimDuration(3000);

        mStartAssessmentBtn = findViewById(R.id.start_assessment);
        mHintMessage = findViewById(R.id.hint_message);

        LinearLayout mRiskLayout1 = findViewById(R.id.risk_count_layout1);
        LinearLayout mRiskLayout2 = findViewById(R.id.risk_count_layout2);
        LinearLayout mRiskLayout3 = findViewById(R.id.risk_count_layout3);
        LinearLayout mRiskLayout4 = findViewById(R.id.risk_count_layout4);
        LinearLayout mRiskLayout5 = findViewById(R.id.risk_count_layout5);
        LinearLayout mRiskLayout6 = findViewById(R.id.risk_count_layout6);
        mRiskLayout1.setOnClickListener(this);
        mRiskLayout2.setOnClickListener(this);
        mRiskLayout3.setOnClickListener(this);
        mRiskLayout4.setOnClickListener(this);
        mRiskLayout5.setOnClickListener(this);
        mRiskLayout6.setOnClickListener(this);

        mRiskCount1 = findViewById(R.id.risk_count1);
        mRiskCount2 = findViewById(R.id.risk_count2);
        mRiskCount3 = findViewById(R.id.risk_count3);
        mRiskCount4 = findViewById(R.id.risk_count4);
        mRiskCount5 = findViewById(R.id.risk_count5);
        mRiskCount6 = findViewById(R.id.risk_count6);

        TextView mTitleName = findViewById(R.id.risk_assessment_name);
        mTitleName.setText(titleName);

        ImageButton back = findViewById(R.id.risk_home_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(linearLayoutManager);

        NormalDividerItemDecoration div = new NormalDividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        div.setDrawable(getResources().getDrawable(R.drawable.divider_shape));
        mRecyclerView.addItemDecoration(div);

        mStartAssessmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotateImageView.startAnimation(rotateAnimation);
                initStartAssessment();
            }
        });

        adapter = new CommonAdapter<BuildingListEntityBean>(this, R.layout.item_risk_assessment_normal, buildingListBeans) {
            @Override
            protected void convert(ViewHolder holder, BuildingListEntityBean buildBean, int position) {

                if (position <=buildingListBeans.size() - 1) {

                    LinearLayout mItemView = holder.getView(R.id.item_view);
                    mItemView.setBackgroundColor(getResources().getColor(R.color.white));

                    TextView mBuildingTitle = holder.getView(R.id.item_risk_title);
                    TextView mBuildingStateName = holder.getView(R.id.item_risk_state_name);

                    TextView mBuildingR = holder.getView(R.id.item_risk_state_back1);
                    TextView mBuildingR3 = holder.getView(R.id.item_risk_state_back2);
                    TextView mBuildingR1 = holder.getView(R.id.item_risk_state_back3);
                    TextView mBuildingR2 = holder.getView(R.id.item_risk_state_back4);
                    TextView mBuildingR0 = holder.getView(R.id.item_risk_state_back5);

                    if (mBuildingTitle != null && !StringUtils.isEmpty(buildBean.getBuildName())) {
                        mBuildingTitle.setText(buildBean.getBuildName());
                    } else if (mBuildingTitle == null) {
                        return;
                    }

                    initItemTextFlag(mBuildingR, buildBean.getR());
                    initItemTextFlag(mBuildingR3, buildBean.getR3());
                    initItemTextFlag(mBuildingR1, buildBean.getR1());
                    initItemTextFlag(mBuildingR2, buildBean.getR2());
                    initItemTextFlag(mBuildingR0, buildBean.getR0());


                    double score = buildBean.getR_Max();

                    if (score <= 1.0) {
                        mBuildingStateName.setText("安全");
                        mBuildingStateName.setBackground(getResources().getDrawable(R.drawable.item_risk_btn_shape_green));
                    } else if (score > 1.0 && score <= 1.6) {
                        mBuildingStateName.setText("轻度");
                        mBuildingStateName.setBackground(getResources().getDrawable(R.drawable.item_risk_btn_shape_yellow));

                    } else if (score > 1.6 && score <= 2.7) {
                        mBuildingStateName.setText("中度");
                        mBuildingStateName.setBackground(getResources().getDrawable(R.drawable.item_risk_btn_shape_orange));

                    } else if (score > 2.7 && score <= 4.5) {
                        mBuildingStateName.setText("高度");
                        mBuildingStateName.setBackground(getResources().getDrawable(R.drawable.item_risk_btn_shape_red1));
                    } else if (score > 4.5) {
                        mBuildingStateName.setText("严重");
                        mBuildingStateName.setBackground(getResources().getDrawable(R.drawable.item_risk_btn_shape_red2));
                    }
                } else {

                    View mItemView = LayoutInflater.from(RiskAssessmentHomeActivity.this).inflate(R.layout.item_risk_assessment_add_view, null);

                    LinearLayout mLayout = holder.getView(R.id.item_view);

                    mLayout.setBackgroundColor(getResources().getColor(R.color.menu_normal_bac));

                    mLayout.removeAllViews();

                    mLayout.addView(mItemView);
                }
            }
        };

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                if (position == buildingListBeans.size() - 1) {
//                    return;
//                }
                Intent intent = new Intent(RiskAssessmentHomeActivity.this, RiskAssessmentFloorListActivity.class);
                intent.putExtra("JsonData", buildingListBeans.get(position).getFloorRiskJson());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        mRecyclerView.setAdapter(adapter);

        initAnimator();

        // 获取上一次基础数据和列表
        initData();
        initGetAssessmentList(true);
    }

    private void initItemTextFlag(TextView mFlag, double score) {
        if (score <= 1.0) {
            mFlag.setText("安全");
            mFlag.setBackground(getResources().getDrawable(R.drawable.item_risk_text_shape));
            mFlag.setTextColor(getResources().getColor(R.color.risk_assessment_state_color_green));
        } else if (score > 1.0 && score <= 1.6) {
            mFlag.setText("轻度");
            mFlag.setBackground(getResources().getDrawable(R.drawable.item_risk_text_shape_yellow));
            mFlag.setTextColor(getResources().getColor(R.color.risk_assessment_state_color_yellow));
        } else if (score > 1.6 && score <= 2.7) {
            mFlag.setText("中度");
            mFlag.setBackground(getResources().getDrawable(R.drawable.item_risk_text_shape_orange));
            mFlag.setTextColor(getResources().getColor(R.color.risk_assessment_state_color_orange));
        } else if (score > 2.7 && score <= 4.5) {
            mFlag.setText("高度");
            mFlag.setBackground(getResources().getDrawable(R.drawable.item_risk_text_shape_red1));
            mFlag.setTextColor(getResources().getColor(R.color.risk_assessment_state_color_red1));
        } else if (score > 4.5) {
            mFlag.setText("严重");
            mFlag.setBackground(getResources().getDrawable(R.drawable.item_risk_text_shape_red2));
            mFlag.setTextColor(getResources().getColor(R.color.risk_assessment_state_color_red2));
        }
    }


    @Override
    protected void setBarLintColor() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.risk_assessment_state_color_green), 0);
    }

    private void initAnimator() {

        // 给WaveView 周围的旋转圈设置动画
        rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setDuration(1000); // 设置动画时间
        rotateAnimation.setInterpolator(new LinearInterpolator()); // 设置插入器
        rotateAnimation.setRepeatCount(5);
        rotateAnimation.setRepeatMode(Animation.RESTART);

        mWaveLoadingView.setProgressValue(75);
        mWaveLoadingView.setAmplitudeRatio(75);

        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mWaveLoadingView.setAmplitudeRatio(10);
                mWaveLoadingView.setProgressValue(100);
                mAssessmentTitle.setText("评估中");

                mHintMessage.setVisibility(View.VISIBLE);
                mHintMessage.setText("评估中...");
                mStartAssessmentBtn.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mWaveLoadingView.setProgressValue(75);
                mWaveLoadingView.setAmplitudeRatio(75);

                mHintMessage.setVisibility(View.INVISIBLE);
                mStartAssessmentBtn.setVisibility(View.VISIBLE);
                mStartAssessmentBtn.setText("重新评估");

                initScoreData(mRiskScore);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                if (mRiskScore > 0) {
                    mCurrentScore = mCurrentScore + mRiskScore / 5.0;
                }
            }
        });

    }

    /**
     * 开始评估
     */
    private void initStartAssessment() {
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("projectId", projectId);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_RISK_ASSESSMENT_RISK_EVALUATE)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        buildingListBeans.clear();
                        adapter.notifyDataSetChanged();
                        showNoDataView();
                        ToastUtils.showLong("风险评估失败,请稍后再试!");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("点击开始评估001---", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                hideNoDataView();
                                // 获取评估后新的界面数据
                                initData();
                                initGetAssessmentList(false);

                            } else {
                                showNoDataView();
                                ToastUtils.showLong(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 获取评估列表前十条
     */
    private void initGetAssessmentList(final boolean isRepeat) {
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("projectId", projectId);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_RISK_ASSESSMENT_GET_BUILD_RISK_LOG)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        buildingListBeans.clear();
                        adapter.notifyDataSetChanged();
                        showNoDataView();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("打印风险评估列表000---", response + "~~~~");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {

                                List<BuildingListEntityBean> listEntityBeans = new Gson().fromJson(jsonObject.getJSONArray("data").toString(),
                                        new TypeToken<List<BuildingListEntityBean>>() {
                                        }.getType());

                                if (listEntityBeans == null || listEntityBeans.size() == 0) {
                                    showNoDataView();
                                    // 第一次进来加载列表  如果为空自动评估一次
                                    if (isRepeat) {
                                        rotateImageView.startAnimation(rotateAnimation);
                                        initStartAssessment();
                                    }
                                    return;
                                } else {
                                    hideNoDataView();
                                }

                                buildingListBeans.clear();

                                buildingListBeans.addAll(listEntityBeans);

                                //buildingListBeans.add(new BuildingListEntityBean());

                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 获取上一次评估的基础数据
     */
    private void initData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("projectId", projectId);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_RISK_ASSESSMENT_GET_BASICS_DATA)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("风险评估首页基础数据0909---", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                JSONObject json = jsonObject.getJSONObject("data");
                                mRiskTime = json.getString("RecentEvaluateTime");

                                if ("暂无".equals(json.getString("ProjectMaxRisk"))) {
                                    mAssessmentTime.setText("暂无");
                                    mRiskScore = -1;
                                } else {
                                    mRiskScore = Double.parseDouble(json.getString("ProjectMaxRisk"));
                                }

                                if (!StringUtils.isEmpty(mRiskTime)) {
                                    if (mRiskTime.contains(" ")) {
                                        String[] arrays = mRiskTime.split(" ");
                                        if (arrays.length >= 2) {
                                            mAssessmentTime.setText(arrays[0] + "\n" + arrays[1]);
                                        }
                                    } else {
                                        mAssessmentTime.setText(mRiskTime);
                                    }
                                }
                                List<Integer> countList = new Gson().fromJson(json.getJSONArray("RiskCounts").toString(),
                                        new TypeToken<List<Integer>>() {
                                        }.getType());
                                if (countList != null && countList.size() == 6) {
                                    mRiskCount1.setText(String.valueOf(countList.get(0)));
                                    mRiskCount2.setText(String.valueOf(countList.get(1)));
                                    mRiskCount3.setText(String.valueOf(countList.get(2)));

                                    mRiskCount4.setText(String.valueOf(countList.get(3)));
                                    mRiskCount5.setText(String.valueOf(countList.get(4)));
                                    mRiskCount6.setText(String.valueOf(countList.get(5)));
                                }

                                if (isLoadFirstData) {
                                    isLoadFirstData = false;
                                    initScoreData(mRiskScore);
                                }

                            } else {
                                ToastUtils.showLong(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 根据分数处理颜色变化
     *
     * @param score
     */
    private void initScoreData(double score) {
        if (score == -1) {
            mAssessmentTitle.setText("暂无");
            initBackColor(getResources().getColor(R.color.risk_assessment_state_color_green));
        } else if (score <= 1.0) {
            mAssessmentTitle.setText("安全");
            initBackColor(getResources().getColor(R.color.risk_assessment_state_color_green));
        } else if (score > 1.0 && score <= 1.6) {
            mAssessmentTitle.setText("轻度");
            initBackColor(getResources().getColor(R.color.risk_assessment_state_color_yellow));
        } else if (score > 1.6 && score <= 2.7) {
            mAssessmentTitle.setText("中度");
            initBackColor(getResources().getColor(R.color.risk_assessment_state_color_orange));
        } else if (score > 2.7 && score <= 4.5) {
            mAssessmentTitle.setText("高度");
            initBackColor(getResources().getColor(R.color.risk_assessment_state_color_red1));
        } else if (score > 4.5) {
            mAssessmentTitle.setText("严重");
            initBackColor(getResources().getColor(R.color.risk_assessment_state_color_red2));
        }
    }

    /**
     * 处理颜色随评估分数变化
     *
     * @param flag
     */
    private void initBackColor(int flag) {

        StatusBarUtil.setColor(this, flag, 0);
        mTitleBar.setBackgroundColor(flag);
        mWaveViewLayout.setBackgroundColor(flag);
        mAssessmentDesc.setTextColor(flag);
        mAssessmentTitle.setTextColor(flag);
        mStartAssessmentBtn.setTextColor(flag);
        mWaveLoadingView.setBorderColor(flag);

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(RiskAssessmentHomeActivity.this, RiskAssessmentStateTypeActivity.class);
        intent.putExtra("projectId", projectId);
        switch (view.getId()) {
            case R.id.risk_count_layout1:
                intent.putExtra("state", 0);
                break;
            case R.id.risk_count_layout2:
                intent.putExtra("state", 1);
                break;
            case R.id.risk_count_layout3:
                intent.putExtra("state", 2);
                break;
            case R.id.risk_count_layout4:
                intent.putExtra("state", 3);
                break;
            case R.id.risk_count_layout5:
                intent.putExtra("state", 4);
                break;
            case R.id.risk_count_layout6:
                intent.putExtra("state", 5);
                break;
        }
        startActivity(intent);
    }
}
