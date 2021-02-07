package com.ynzhxf.nd.xyfirecontrolapp.view.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.MultiItemTypeAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.PLVideoModelBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.VideoLiveStateInfoBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionTaskHomeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.message.MessageUpdateHeightBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.firealarm.FireAlarmSystemEventBean;
import com.ynzhxf.nd.xyfirecontrolapp.network.RetrofitUtils;
import com.ynzhxf.nd.xyfirecontrolapp.ui.NormalDividerItemDecoration;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.PLVideoTextureActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.PLVideoTextureHomeActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.ProjectStatisticsListErrorInfo;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.InspectionAssignedTasksActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.InspectionItemListActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.InspectionRecordsActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.InspectionResponsiblePersonActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
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
 * date 2019/6/19 09:40
 * 业主首页最多故障点和巡检任务列表子fragment
 */
public class OwnerIndexChildrenFragment extends BaseFragment {

    protected RecyclerView mRecyclerView;
    protected Context context;

    private List<InspectionTaskHomeBean> homeBeanList = new ArrayList<>();
    private List<FireAlarmSystemEventBean> topList = new ArrayList<>();
    private List<PLVideoModelBean> videoModelList = new ArrayList<>();

    protected TextView mNoDataView;

    protected int currentPosition;
    protected CommonAdapter adapter;

    public static OwnerIndexChildrenFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt("position", position);
        OwnerIndexChildrenFragment fragment = new OwnerIndexChildrenFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner_index_children, container, false);
        mRecyclerView = view.findViewById(R.id.children_index_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mNoDataView = view.findViewById(R.id.all_no_data);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            currentPosition = bundle.getInt("position", 0);
        }
        if (currentPosition == 2) {
            NormalDividerItemDecoration div = new NormalDividerItemDecoration(context, DividerItemDecoration.VERTICAL);
            div.setDrawable(getResources().getDrawable(R.drawable.divider_shape_inspection));
            mRecyclerView.addItemDecoration(div);
        }
    }

    protected void initInspectList() {
        RetrofitUtils.getInstance().getOwnerIndexInspectProject(HelperTool.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultBean<List<InspectionTaskHomeBean>, String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResultBean<List<InspectionTaskHomeBean>, String> resultBean) {
                        if (resultBean.isSuccess()) {
                            homeBeanList.clear();
                            homeBeanList.addAll(resultBean.getData());
                        }
                        if (homeBeanList.size() == 0) {
                            mNoDataView.setVisibility(View.VISIBLE);
                            return;
                        } else {
                            mNoDataView.setVisibility(View.GONE);
                        }
                        adapter = new CommonAdapter<InspectionTaskHomeBean>(context, R.layout.item_inspection_task_list_one, homeBeanList) {
                            @Override
                            protected void convert(ViewHolder holder, final InspectionTaskHomeBean homeBean, final int position) {
                                TextView title = holder.getView(R.id.inspection_home_title);
                                TextView stateName = holder.getView(R.id.inspection_home_state);
                                TextView headName = holder.getView(R.id.inspection_home_head_name);
                                TextView startTime = holder.getView(R.id.inspection_home_start_time);
                                TextView endTime = holder.getView(R.id.inspection_home_end_time);
                                TextView projectName = holder.getView(R.id.inspection_home_project_name);

                                TextView mInspectCount = holder.getView(R.id.inspect_count);

                                Button mResult = holder.getView(R.id.item_state_result);

                                Button setPerson = holder.getView(R.id.inspection_assigned_task_button);

                                Button assignedTask = holder.getView(R.id.inspection_view_task_button);

                                Button viewHistory = holder.getView(R.id.inspection_home_task_button);

                                if (!StringUtils.isEmpty(homeBean.getStateCount())) {
                                    mInspectCount.setText(homeBean.getStateCount());
                                }

                                title.setText(homeBean.getName());

                                stateName.setText(homeBean.getStateShow());

                                if (!StringUtils.isEmpty(homeBean.getChargeManName())) {
                                    headName.setText(String.valueOf("负责人:" + homeBean.getChargeManName()));
                                } else {
                                    headName.setText("负责人:");
                                }
                                if (!StringUtils.isEmpty(homeBean.getProjectName())) {
                                    projectName.setText(String.valueOf("项目:" + homeBean.getProjectName()));
                                } else {
                                    projectName.setText("项目:");
                                }

                                //headName.setText(String.valueOf("负责人:" + homeBean.getChargeManName()));

                                startTime.setText(String.valueOf("开始时间:" + homeBean.getStartTimeShow()));

                                endTime.setText(String.valueOf("截止时间:" + homeBean.getEndTimeShow()));

                                // 定义是否显示负责人或者显示维保公司
                                //initInspectCompany(headName, homeBean);
                                // 查看记录
                                viewHistory.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        goToInspectRecords(homeBean);
                                    }
                                });
                                // 设置负责人
                                setPerson.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        Intent intent = new Intent(context, InspectionResponsiblePersonActivity.class);

                                        intent.putExtra("position", position);

                                        intent.putExtra("taskId", homeBean.getID());

                                        startActivityForResult(intent, 25);
                                    }
                                });
                                // 分配任务
                                assignedTask.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        Intent intent = new Intent(context, InspectionAssignedTasksActivity.class);

                                        intent.putExtra("projectId", homeBean.getProjectId());

                                        intent.putExtra("taskId", homeBean.getID());

                                        startActivity(intent);
                                    }
                                });

                                if (homeBean.getCurrentUserType() == 2) {
                                    assignedTask.setVisibility(View.VISIBLE);
                                } else if (homeBean.getCurrentUserType() == 3) {
                                    assignedTask.setVisibility(View.VISIBLE);
                                    setPerson.setVisibility(View.VISIBLE);
                                } else {
                                    assignedTask.setVisibility(View.INVISIBLE);
                                    setPerson.setVisibility(View.INVISIBLE);
                                }

                                if (homeBean.getState() == 10) {
                                    assignedTask.setVisibility(View.INVISIBLE);
                                    setPerson.setVisibility(View.INVISIBLE);
                                    mResult.setVisibility(View.VISIBLE);
                                    stateName.setTextColor(getResources().getColor(R.color.inspection_list_btn));
                                    if (homeBean.getResult() == 0) {
                                        mResult.setText(homeBean.getResultShow());//异常
                                        mResult.setBackground(getResources().getDrawable(R.drawable.inspection_item_btn_one));
                                    } else if (homeBean.getResult() == 1) {
                                        mResult.setText(homeBean.getResultShow());//正常
                                        mResult.setBackground(getResources().getDrawable(R.drawable.inspection_item_btn));
                                    }
                                } else if (homeBean.getState() == 0) {
                                    mResult.setVisibility(View.INVISIBLE);
                                    stateName.setTextColor(getResources().getColor(R.color.fire_fire));
                                } else if (homeBean.getState() == 20) {
                                    mResult.setVisibility(View.INVISIBLE);
                                    stateName.setTextColor(getResources().getColor(R.color.global_button_stroke_color));
                                }
                            }
                        };

                        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, RecyclerView.ViewHolder holder, final int position) {

                                InspectionTaskHomeBean homeBean = homeBeanList.get(position);

                                if (homeBean.getState() == 10 || homeBean.getState() == 20) {
                                    goToInspectRecords(homeBean);
                                } else {
                                    // String[] permissions = new String[]{Manifest.permission.CAMERA};
                                    // 使用新的权限请求框架 更稳定兼容性更好
                                    AndPermission.with(context)
                                            .runtime()
                                            .permission(Permission.CAMERA)
                                            .onGranted(new Action<List<String>>() {
                                                @Override
                                                public void onAction(List<String> data) {
                                                    goToInspectItemList(position);
                                                }
                                            })
                                            .onDenied(new Action<List<String>>() {
                                                @Override
                                                public void onAction(List<String> data) {
                                                    ToastUtils.showLong("权限被拒绝,巡检功能无法使用!");
                                                }
                                            })
                                            .start();
                                }
                            }

                            @Override
                            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                                return false;
                            }
                        });

                        mRecyclerView.setAdapter(adapter);

                        mRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                // mRecyclerView初始化完成后通知ViewPager当前高度
                                MessageUpdateHeightBean bean = new MessageUpdateHeightBean();
                                bean.setId(1);
                                bean.setHeight(mRecyclerView.computeVerticalScrollRange());
                                EventBus.getDefault().post(bean);
                            }
                        });

//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                // mRecyclerView初始化完成后通知ViewPager当前高度
//                                MessageUpdateHeightBean bean = new MessageUpdateHeightBean();
//                                bean.setId(1);
//                                bean.setHeight(mRecyclerView.computeVerticalScrollRange());
//                                EventBus.getDefault().post(bean);
//                            }
//                        }, 1000);
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

    /**
     * 跳转到巡检记录
     *
     * @param homeBean
     */
    protected void goToInspectRecords(InspectionTaskHomeBean homeBean) {
        Intent intent = new Intent(context, InspectionRecordsActivity.class);
        intent.putExtra("taskId", homeBean.getID());
        intent.putExtra("bean", homeBean);
        startActivity(intent);
    }

    /**
     * 跳转到巡检项列表
     *
     * @param position
     */
    protected void goToInspectItemList(int position) {
        Intent intent = new Intent(context, InspectionItemListActivity.class);
        intent.putExtra("projectId", homeBeanList.get(position).getProjectId());
        intent.putExtra("inspectionTypeId", homeBeanList.get(position).getInspectTypeId());
        intent.putExtra("taskId", homeBeanList.get(position).getID());
        intent.putExtra("state", homeBeanList.get(position).getState());
        startActivity(intent);
    }

    protected void initProjectList() {
        RetrofitUtils.getInstance().getOwnerIndexMorePoint(HelperTool.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultBean<List<List<String>>, String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResultBean<List<List<String>>, String> resultBean) {
                        if (resultBean.isSuccess()) {

                            topList.clear();

                            List<List<String>> eventTopList = resultBean.getData();

                            if (eventTopList.size() == 0) {
                                mNoDataView.setVisibility(View.VISIBLE);
                                return;
                            } else {
                                mNoDataView.setVisibility(View.GONE);
                            }

                            if (eventTopList.size() > 0) {
                                for (int i = 0; i < eventTopList.size(); i++) {
                                    FireAlarmSystemEventBean bean = new FireAlarmSystemEventBean();
                                    if (eventTopList.get(i).size() >= 3) {
                                        bean.setID(eventTopList.get(i).get(0));
                                        bean.setName(eventTopList.get(i).get(1));
                                        bean.setNum(eventTopList.get(i).get(2));
                                    }
                                    topList.add(bean);
                                }
                            }

                            adapter = new CommonAdapter<FireAlarmSystemEventBean>(context, R.layout.item_statistics_recycler_two, topList) {
                                @Override
                                protected void convert(ViewHolder holder, FireAlarmSystemEventBean bean, int position) {
                                    TextView title = holder.getView(R.id.title);
                                    TextView mItemFlag = holder.getView(R.id.item_flag);
                                    mItemFlag.setVisibility(View.GONE);
                                    title.setText(bean.getName());
                                    TextView num = holder.getView(R.id.num);
                                    num.setText(bean.getNum());
                                }
                            };

                            adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                                    Intent intent = new Intent(context, ProjectStatisticsListErrorInfo.class);

                                    intent.putExtra("ID", topList.get(position).getID());

                                    intent.putExtra("Name", topList.get(position).getName());

                                    intent.putExtra("EventId", topList.get(position).getNum());

                                    startActivity(intent);
                                }

                                @Override
                                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                                    return false;
                                }
                            });

                            mRecyclerView.setAdapter(adapter);

                            mRecyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    // mRecyclerView初始化完成后通知ViewPager当前高度
                                    MessageUpdateHeightBean bean = new MessageUpdateHeightBean();
                                    bean.setId(0);
                                    bean.setHeight(mRecyclerView.computeVerticalScrollRange());
                                    EventBus.getDefault().post(bean);
                                }
                            });

//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    // mRecyclerView初始化完成后通知ViewPager当前高度
//                                    MessageUpdateHeightBean bean = new MessageUpdateHeightBean();
//                                    bean.setId(0);
//                                    bean.setHeight(mRecyclerView.computeVerticalScrollRange());
//                                    EventBus.getDefault().post(bean);
//                                }
//                            }, 1000);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initLiveStateInfo(final List<String> roomIds, String server, final List<PLVideoModelBean> modelBeans, final PLVideoTextureHomeActivity.OnCallBackVideoBeanListener callBack) {
        String ids = "";
        final List<PLVideoModelBean> modelBeansOk = new ArrayList<>();
        for (String id : roomIds) {
            ids = ids.concat(id).concat(",");
        }
        if (StringUtils.isEmpty(ids) || StringUtils.isEmpty(server)) {
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
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("获取所有直播间状态00123---", roomIds.size() + "~~~~~~" + response);

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

    protected void initVideoPath() {
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
                        videoModelList.clear();
                        if (resultBean.isSuccess() && resultBean.getData().size() > 0) {
                            videoModelList.addAll(resultBean.getData());
                            mNoDataView.setVisibility(View.GONE);

                            // 获取所有直播间直播状态并筛选显示正在直播的直播间
                            List<String> ids = new ArrayList<>();
                            for (PLVideoModelBean bean : resultBean.getData()) {
                                ids.add(bean.getRoomId());
                            }
                            initLiveStateInfo(ids, videoModelList.get(0).getServer(), videoModelList, new PLVideoTextureHomeActivity.OnCallBackVideoBeanListener() {
                                @Override
                                public void onResult(final List<PLVideoModelBean> modelBeans) {
                                    if (modelBeans.size() == 0) {
                                        mNoDataView.setVisibility(View.VISIBLE);
                                    } else {
                                        mNoDataView.setVisibility(View.GONE);
                                    }
                                    adapter = new CommonAdapter<PLVideoModelBean>(context, R.layout.item_video_live_list, modelBeans) {
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
                                            final ProgressDialog dialog = showProgress(context, "加载中...", false);
                                            HashMap<String, String> params = new HashMap<>();
                                            params.put("token", modelBeans.get(position).getToken());
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
                                                            //LogUtils.showLoge("获取会议直播状态0007---", e.getMessage());
                                                        }

                                                        @Override
                                                        public void onResponse(String response, int id) {
                                                            dialog.dismiss();
                                                            //LogUtils.showLoge("获取会议直播状态0008---", response);
                                                            VideoLiveStateInfoBean videoBean = new Gson().fromJson(response, VideoLiveStateInfoBean.class);
                                                            if (videoBean != null && videoBean.getSession() != null) {
                                                                Intent intent = new Intent(context, PLVideoTextureActivity.class);
                                                                intent.putExtra("videoPath", modelBeans.get(position).getUrl());
                                                                intent.putExtra("videoName", modelBeans.get(position).getOrganizationName().concat(" - ").concat(modelBeans.get(position).getTitle()));
                                                                intent.putExtra("id", modelBeans.get(position).getRoomId());
                                                                intent.putExtra("url", plVideoModelBean.getServer().concat("/live/get"));
                                                                intent.putExtra("token", modelBeans.get(position).getToken());
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

                                    mRecyclerView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            MessageUpdateHeightBean bean = new MessageUpdateHeightBean();
                                            bean.setId(2);
                                            bean.setHeight(mRecyclerView.computeVerticalScrollRange());
                                            EventBus.getDefault().post(bean);
                                        }
                                    });
                                }
                            });
                        } else {
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                            mNoDataView.setVisibility(View.VISIBLE);
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
                            RequestBuilder<Drawable> requestErrorBuilder = Glide.with(context).asDrawable().load(R.drawable.no_video_icon).apply(options);
                            JSONObject jsonObject = new JSONObject(response);
                            Glide.with(context)
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

    @Override
    public void onResume() {
        super.onResume();
        if (SPUtils.getInstance().getBoolean("updateHomeItem")) {
            SPUtils.getInstance().put("updateHomeItem", false);
        }

        if (currentPosition == 1) {
            initProjectList();
        } else if (currentPosition == 2) {
            initInspectList();
        } else {
            initVideoPath();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 20:
                    homeBeanList.clear();
                    if (currentPosition == 1) {
                        initProjectList();
                    } else if (currentPosition == 2) {
                        initInspectList();
                    } else {
                        initVideoPath();
                    }
                    break;
                case 25:
                    if (homeBeanList.size() > 0) {
                        InspectionTaskHomeBean bean = homeBeanList.get(data.getIntExtra("position", 0));
                        bean.setChargeManName(data.getStringExtra("Name"));
                    }
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}
