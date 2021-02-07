package com.ynzhxf.nd.xyfirecontrolapp.view.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.DangerProjectAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.MultiItemTypeAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.SpecialCommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionTaskHomeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.CompanyOrderListInputBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.MaintenListAllBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.MaintenListBackBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.message.MessageUpdateHeightBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.network.RetrofitUtils;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.IMainCompanyOrderListPresenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.impl.MaintenManagePresenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IDangeroursUserProjectPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl.NodeBasePersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.ui.NormalDividerItemDecoration;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.MaintenanceCompanyActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.InspectionAssignedTasksActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.InspectionRecordsActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.InspectionResponsiblePersonActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.company.InspectionCompanySystemListActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * author hbzhou
 * date 2019/6/26 15:34
 * 维保公司首页viewpager下fragment
 */
public class CompanyIndexChildrenFragment extends OwnerIndexChildrenFragment implements IDangeroursUserProjectPersenter.IDangeroursUserProjectView, IMainCompanyOrderListPresenter.ICompanyOrderListView {
    /**
     * 异常项目列表请求
     */
    public IDangeroursUserProjectPersenter dangerousUserProjectPresenter;

    private IMainCompanyOrderListPresenter orderListPresenter;

    private CompanyOrderListInputBean inputBean;

    private List<MaintenListBackBean> companyBeanList = new ArrayList<>();
    private List<InspectionTaskHomeBean> homeBeanList = new ArrayList<>();

    public static CompanyIndexChildrenFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt("position", position);
        CompanyIndexChildrenFragment fragment = new CompanyIndexChildrenFragment();
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

    private void initDividerRecyclerView() {
        NormalDividerItemDecoration div = new NormalDividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        div.setDrawable(getResources().getDrawable(R.drawable.divider_shape_inspection));
        mRecyclerView.addItemDecoration(div);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dangerousUserProjectPresenter = NodeBasePersenterFactory.getDangeroursUserProjectPersenterImpl(this);
        addPersenter(dangerousUserProjectPresenter);
        orderListPresenter = MaintenManagePresenterFactory.getCompanyOrderListImpl(this);
        addPersenter(orderListPresenter);

        inputBean = new CompanyOrderListInputBean();
        inputBean.setToken(HelperTool.getToken());
        inputBean.setState("0");
        inputBean.setPageIndex("1");
        inputBean.setPageSize("10");
        inputBean.setIsWorking("1");
        // 除第一个列表外其他列表使用分隔条
        if (currentPosition == 2 || currentPosition == 3) {
            initDividerRecyclerView();
        }

        //LogUtils.showLoge("list高度010---", mRecyclerView.getHeight() + "~~~~~~");

        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
               // LogUtils.showLoge("list高度009---", mRecyclerView.getHeight() + "~~~~~~");
                mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (currentPosition == 1) {
            dangerousUserProjectPresenter.doDangeroursUserProject();
        } else if (currentPosition == 2) {
            orderListPresenter.doCompanyOrderList(inputBean);
        } else if (currentPosition == 3) {
            getInspectProjectList();
        }

        //com.blankj.utilcode.util.LogUtils.eTag("inputbean00234---", inputBean.toString());
    }


    private void getInspectProjectList() {
        RetrofitUtils.getInstance().getCompanyInspectOrderList(HelperTool.getToken(), "0", "0", "10")
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
                        InspectionTaskHomeBean bean = new InspectionTaskHomeBean();
                        homeBeanList.add(bean);
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

                                if (position != homeBeanList.size() - 1) {

                                    if (title == null) {
                                        return;
                                    }

                                    if (!StringUtils.isEmpty(homeBean.getStateCount())) {
                                        mInspectCount.setText(homeBean.getStateCount());
                                    }

                                    title.setText(homeBean.getName());

                                    stateName.setText(homeBean.getStateShow());

                                    if (!StringUtils.isEmpty(homeBean.getChargeManShow())) {
                                        headName.setText(String.valueOf("负责人:" + homeBean.getChargeManShow()));
                                    } else {
                                        headName.setText("负责人:");
                                    }
                                    if (!StringUtils.isEmpty(homeBean.getProjectName())) {
                                        projectName.setText(String.valueOf("项目:" + homeBean.getProjectName()));
                                    } else {
                                        projectName.setText("项目:-");
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
                                } else {
                                    View mItemView = LayoutInflater.from(context).inflate(R.layout.item_risk_assessment_add_view, null);
                                    TextView mHintText = mItemView.findViewById(R.id.hint_text_message);
                                    mHintText.setText("仅显示最近10条");
                                    RelativeLayout mLayout = holder.getView(R.id.item_view);
                                    mLayout.setBackgroundColor(getResources().getColor(R.color.menu_normal_bac));
                                    mLayout.removeAllViews();
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mLayout.getLayoutParams());
                                    layoutParams.gravity = Gravity.CENTER;
                                    mLayout.addView(mItemView, layoutParams);
                                }
                            }
                        };

                        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, RecyclerView.ViewHolder holder, final int position) {

                                if (position == homeBeanList.size() - 1) {
                                    return;
                                }

                                InspectionTaskHomeBean homeBean = homeBeanList.get(position);

                                if (homeBean.getState() == 10 || homeBean.getState() == 20) {
                                    goToInspectRecords(homeBean);
                                } else {
                                    // String[] permissions = new String[]{Manifest.permission.CAMERA};
                                    // 使用新的权限请求框架 更稳定兼容性更好
                                    AndPermission.with(getActivity())
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
                                bean.setId(2);
                                bean.setHeight(mRecyclerView.computeVerticalScrollRange());
                                EventBus.getDefault().post(bean);

                                LogUtils.showLoge("获取list高度001212---", mRecyclerView.computeVerticalScrollRange() + "~~~~~");
                            }
                        });

//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                // mRecyclerView初始化完成后通知ViewPager当前高度
//                                MessageUpdateHeightBean bean = new MessageUpdateHeightBean();
//                                bean.setId(2);
//                                bean.setHeight(mRecyclerView.computeVerticalScrollRange());
//                                EventBus.getDefault().post(bean);
//                                LogUtils.showLoge("获取list高度001313---",mRecyclerView.computeVerticalScrollRange()+"~~~~~");
//
//                            }
//                        }, 2000);
                    }

                    @Override
                    public void onError(Throwable e) {

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
        intent.putExtra("isCompany", true);
        startActivity(intent);
    }

    /**
     * 跳转到巡检项列表
     *
     * @param position
     */
    protected void goToInspectItemList(int position) {
        Intent intent = new Intent(context, InspectionCompanySystemListActivity.class);
        intent.putExtra("projectId", homeBeanList.get(position).getProjectId());
        intent.putExtra("inspectionTypeId", homeBeanList.get(position).getInspectTypeId());
        intent.putExtra("taskId", homeBeanList.get(position).getID());
        intent.putExtra("state", homeBeanList.get(position).getState());
        startActivity(intent);
    }

    @Override
    protected void initInspectList() {
        //
    }

    @Override
    protected void initProjectList() {
        //
    }

    @Override
    protected void initVideoPath() {
        //
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void callBackDangeroursUserProject(ResultBean<List<ProjectNodeBean>, String> result) {
        try {
            if (result.isSuccess()) {
                DangerProjectAdapter adapter = new DangerProjectAdapter(result.getData());
                adapter.setLoginType(4);
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

//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        // mRecyclerView初始化完成后通知ViewPager当前高度
//                        MessageUpdateHeightBean bean = new MessageUpdateHeightBean();
//                        bean.setId(currentPosition - 1);
//                        bean.setHeight(mRecyclerView.computeVerticalScrollRange());
//                        EventBus.getDefault().post(bean);
//                    }
//                }, 2000);
            } else {
                String msg = "精准治理" + result.getMessage();
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                Log.e(TAG, msg);
            }
        } catch (Exception e) {
            HelperView.Toast(context, "精准治理失败：" + e.getMessage());
        }
    }

    @Override
    public void callBackCompanyOrderList(ResultBean<MaintenListAllBean, String> resultBean) {
        if (resultBean == null || resultBean.getData() == null || resultBean.getData().getRows() == null || resultBean.getData().getRows().size() == 0) {
            mNoDataView.setVisibility(View.VISIBLE);
            return;
        } else {
            mNoDataView.setVisibility(View.GONE);
        }
        MaintenanceCompanyActivity.flag = true;

        companyBeanList.clear();

        companyBeanList.addAll(resultBean.getData().getRows());

        SpecialCommonAdapter adapter = MaintenanceCompanyActivity.initWorkOrderList(mRecyclerView, (Activity) context, companyBeanList);

        adapter.notifyDataSetChanged();

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

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // mRecyclerView初始化完成后通知ViewPager当前高度
//                MessageUpdateHeightBean bean = new MessageUpdateHeightBean();
//                bean.setId(1);
//                bean.setHeight(mRecyclerView.computeVerticalScrollRange());
//                EventBus.getDefault().post(bean);
//            }
//        }, 3000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 25:
                    if (homeBeanList.size() > 0) {
                        InspectionTaskHomeBean bean = homeBeanList.get(data.getIntExtra("position", 0));
                        bean.setChargeManName(data.getStringExtra("Name"));
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case 99:
                case 100:
                case 101:
                case 102:
                case 103:
                case 104:
                case 120:
                default:
                    orderListPresenter.doCompanyOrderList(inputBean);
            }
        }
    }
}
