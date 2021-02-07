package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.diagnose;


import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.MultiItemTypeAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.diagnose.DeviceDiagnoseHomeBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * author hbzhou
 * date 2019/4/17 11:39
 * 查看诊断详情
 */
public class DeviceDiagnoseHomeFinishActivity extends DeviceDiagnoseHomeActivity {


    @Override
    protected void initCircleViewAnimation() {

        // 给WaveView 周围的旋转圈设置动画
        rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setDuration(2000); // 设置动画时间
        rotateAnimation.setInterpolator(new LinearInterpolator()); // 设置插入器
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setRepeatMode(Animation.RESTART);


        mWaveLoadingView.setProgressValue(70);
        mWaveLoadingView.setAmplitudeRatio(70);
        mProgressBar.getLayoutParams().width = screenWidth;
        mProgressBar.requestLayout();
        mProgressTitle.setText("诊断完成!");
    }

    @Override
    protected void initAdapterItemClick() {
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                DeviceDiagnoseHomeBean homeBean = homeBeanList.get(position);
                Intent intent = new Intent(DeviceDiagnoseHomeFinishActivity.this, DeviceDiagnoseDetailsActivity.class);
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

    /**
     * 使用查看详情接口获取上一次诊断的数据
     *
     * @param projectId
     */
    @Override
    protected void initDeviceData(String projectId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("projectDiagnoseID", getIntent().getStringExtra("projectDiagnoseID"));
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_DEVICE_DIAGNOSE_VIEW_DETAILS)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("查看详情1234---", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {

                                List<DeviceDiagnoseHomeBean> homeBeans = new Gson().fromJson(jsonObject.getJSONArray("data").toString(),
                                        new TypeToken<List<DeviceDiagnoseHomeBean>>() {
                                        }.getType());

                                if (homeBeans == null || homeBeans.size() == 0) {
                                    ToastUtils.showLong("未获取到设备诊断信息,请稍后再试!");
                                    finish();
                                    return;
                                }

                                homeBeanList.addAll(homeBeans);

                                int sumScore = 0;
                                for (DeviceDiagnoseHomeBean bean1 : homeBeanList) {
                                    bean1.setProgressStatus(3);
                                    sumScore = sumScore + bean1.getScore();
                                }

                                diagnoseScore = sumScore / homeBeanList.size();

                                mDiagnoseText.setText(String.valueOf(diagnoseScore));

                                initBackColor(diagnoseScore);

                                setFinishButtonColor();

                                adapter.notifyDataSetChanged();

                            } else {
                                ToastUtils.showLong(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
