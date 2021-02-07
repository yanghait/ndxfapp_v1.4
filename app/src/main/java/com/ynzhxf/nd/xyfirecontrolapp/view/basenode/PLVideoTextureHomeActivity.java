package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.MultiItemTypeAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.PLVideoModelBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.VideoLiveStateInfoBean;
import com.ynzhxf.nd.xyfirecontrolapp.network.RetrofitUtils;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;


/**
 * author hbzhou
 * date 2019/7/29 15:31
 * 视频会议首页视频列表
 */
public class PLVideoTextureHomeActivity extends BaseActivity {

    private RecyclerView mRecyclerView;

    private RefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_video_texture_home);
        super.onCreate(savedInstanceState);
        setBarTitle("视频会议");

        mRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRefreshLayout = findViewById(R.id.refreshLayout);

        initVideoPath();
        initRefreshLayout();
    }

    private void initRefreshLayout() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000);
                initVideoPath();
            }
        });
    }

    private void getThumbnailUrl(final ImageView imageView, final PLVideoModelBean plVideoModelBean) {
        if (StringUtils.isEmpty(plVideoModelBean.getRoomId()) || StringUtils.isEmpty(plVideoModelBean.getServer())
                || StringUtils.isEmpty(plVideoModelBean.getToken())) {
            ToastUtils.showLong("未获取到视频快照地址!");
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("token", plVideoModelBean.getToken());
        params.put("id", plVideoModelBean.getRoomId());
        OkHttpUtils.post()
                .url(plVideoModelBean.getServer().concat("/snap/current"))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("获取到的url0000005", response);
                        //设置图片圆角角度
                        RoundedCorners roundedCorners = new RoundedCorners(10);
                        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
                        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners)
                                .override(800, 450)
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                        try {
                            // 设置加载失败占位图
                            RequestBuilder<Drawable> requestErrorBuilder = Glide.with(PLVideoTextureHomeActivity.this).asDrawable().load(R.drawable.no_video_icon).apply(options);
                            JSONObject jsonObject = new JSONObject(response);
                            Glide.with(PLVideoTextureHomeActivity.this)
                                    .load(plVideoModelBean.getServer().concat(jsonObject.getString(plVideoModelBean.getRoomId())))
                                    .apply(options)
                                    .error(requestErrorBuilder)
                                    .into(imageView);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public interface OnCallBackVideoBeanListener {
        void onResult(List<PLVideoModelBean> modelBeans);
    }

    /**
     * 处理直播间列表筛选正在直播的直播间
     *
     * @param roomIds
     * @param server
     * @param modelBeans
     * @param callBack
     */
    private void initLiveStateInfo(final List<String> roomIds, String server, final List<PLVideoModelBean> modelBeans, final OnCallBackVideoBeanListener callBack) {
        String ids = "";
        final List<PLVideoModelBean> modelBeansOk = new ArrayList<>();
        for (String id : roomIds) {
            ids = ids.concat(id).concat(",");
        }
        if (StringUtils.isEmpty(ids) || StringUtils.isEmpty(server)) {
            ToastUtils.showLong("未获取到直播流!");
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        if (modelBeans.size() > 0) {
            params.put("token", modelBeans.get(0).getToken());
        }
        params.put("id", ids.substring(0, ids.length() - 1));
        OkHttpUtils.post()
                .url(server.concat("/live/get"))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callBack.onResult(modelBeans);
                        //LogUtils.showLoge("获取所有直播间状态00199---", e.getMessage());
                        //重构视频会议列表代码
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("获取所有直播间状态0012345---", response);

                        try {
                            if (roomIds.size() == 1 && modelBeans.size() == 1) {
                                VideoLiveStateInfoBean videoBean = new Gson().fromJson(response, VideoLiveStateInfoBean.class);
                                if (videoBean != null && videoBean.getSession() != null) {
                                    modelBeansOk.add(modelBeans.get(0));
                                }
                                callBack.onResult(modelBeansOk);
                            } else {
                                List<VideoLiveStateInfoBean> videoLiveStateBeans = new Gson().fromJson(response, new TypeToken<List<VideoLiveStateInfoBean>>() {
                                }.getType());
                                if (videoLiveStateBeans == null || videoLiveStateBeans.size() == 0) {
                                    callBack.onResult(modelBeansOk);
                                    return;
                                }
                                if (videoLiveStateBeans.size() == modelBeans.size()) {
                                    for (int i = 0; i < videoLiveStateBeans.size(); i++) {
                                        if (videoLiveStateBeans.get(i).getSession() != null) {
                                            modelBeansOk.add(modelBeans.get(i));
                                        }
                                    }
                                }
                                callBack.onResult(modelBeansOk);
                            }
                        } catch (Exception e) {
                            callBack.onResult(modelBeansOk);
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initVideoPath() {
        RetrofitUtils.getInstance().getVideoLiveUrlList(HelperTool.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultBean<List<PLVideoModelBean>, String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable.add(d);
                    }

                    @Override
                    public void onNext(final ResultBean<List<PLVideoModelBean>, String> resultBean) {
                        //LogUtils.showLoge("输出视频流列表0009---", resultBean.getData());
                        if (resultBean.isSuccess() && resultBean.getData().size() > 0) {
                            hideNoDataView();
                            // 获取所有直播间直播状态并筛选显示正在直播的直播间
                            List<String> ids = new ArrayList<>();
                            for (PLVideoModelBean bean : resultBean.getData()) {
                                ids.add(bean.getRoomId());
                            }
                            initLiveStateInfo(ids, resultBean.getData().get(0).getServer(), resultBean.getData(), new OnCallBackVideoBeanListener() {
                                @Override
                                public void onResult(final List<PLVideoModelBean> modelBeans) {
                                    if (modelBeans.size() == 0) {
                                        showNoDataView();
                                    } else {
                                        hideNoDataView();
                                    }
                                    CommonAdapter adapter = new CommonAdapter<PLVideoModelBean>(PLVideoTextureHomeActivity.this, R.layout.item_video_live_list, modelBeans) {
                                        @Override
                                        protected void convert(ViewHolder holder, final PLVideoModelBean plVideoModelBean, int position) {
                                            final ImageView imageView = holder.getView(R.id.video_live_image);
                                            TextView mItemTitle = holder.getView(R.id.video_live_title);

                                            getThumbnailUrl(imageView, plVideoModelBean);

                                            mItemTitle.setText(plVideoModelBean.getOrganizationName().concat(" - ").concat(plVideoModelBean.getTitle()));
                                        }
                                    };

                                    adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, RecyclerView.ViewHolder holder, final int position) {

                                            final PLVideoModelBean plVideoModelBean = resultBean.getData().get(position);
                                            if (StringUtils.isEmpty(plVideoModelBean.getRoomId()) || StringUtils.isEmpty(plVideoModelBean.getServer())
                                                    || StringUtils.isEmpty(plVideoModelBean.getToken())) {
                                                ToastUtils.showLong("此会议暂未开播!");
                                                return;
                                            }
                                            final ProgressDialog dialog = showProgress(PLVideoTextureHomeActivity.this, "加载中...", false);
                                            HashMap<String, String> params = new HashMap<>();
                                            params.put("token", plVideoModelBean.getToken());
                                            params.put("id", modelBeans.get(position).getRoomId());
                                            OkHttpUtils.post()
                                                    .url(plVideoModelBean.getServer().concat("/live/get"))
                                                    .params(params)
                                                    .build()
                                                    .execute(new StringCallback() {
                                                        @Override
                                                        public void onError(Call call, Exception e, int id) {
                                                            dialog.dismiss();
                                                            ToastUtils.showLong("此会议暂未开播!");
                                                            //LogUtils.showLoge("获取会议直播状态0005---", e.getMessage());
                                                        }

                                                        @Override
                                                        public void onResponse(String response, int id) {
                                                            dialog.dismiss();
                                                            //LogUtils.showLoge("获取会议直播状态0008---", response);
                                                            VideoLiveStateInfoBean videoBean = new Gson().fromJson(response, VideoLiveStateInfoBean.class);
                                                            if (videoBean != null && videoBean.getSession() != null) {
                                                                Intent intent = new Intent(PLVideoTextureHomeActivity.this, PLVideoTextureActivity.class);
                                                                intent.putExtra("videoPath", modelBeans.get(position).getUrl());
                                                                intent.putExtra("videoName", modelBeans.get(position).getOrganizationName().concat(" - ").concat(modelBeans.get(position).getTitle()));
                                                                intent.putExtra("id", modelBeans.get(position).getRoomId());
                                                                intent.putExtra("url", plVideoModelBean.getServer().concat("/live/get"));
                                                                intent.putExtra("token", plVideoModelBean.getToken());
                                                                startActivity(intent);
                                                            } else {
                                                                ToastUtils.showLong("此会议暂未开播!");
                                                            }
                                                        }
                                                    });
                                        }

                                        @Override
                                        public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                                            return false;
                                        }
                                    });
                                    mRecyclerView.setAdapter(adapter);
                                }
                            });
                        } else {
                            showNoDataView();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showLong(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
