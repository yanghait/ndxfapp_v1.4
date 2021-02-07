package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.MultiItemTypeAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.SpecialCommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.CompanyOrderListInputBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.MainOwnerMarkCountBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.MaintenListAllBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.MaintenListBackBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.MaintenanceListInfoBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.IMainCompanyOrderListPresenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.impl.MaintenManagePresenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.ui.MyLoadMenuBackHeader;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.util.PermissionsUtil;
import com.ynzhxf.nd.xyfirecontrolapp.util.ScreenUtil;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.company.CompanyBackFillActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.company.CompanyConfirmOrderActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.company.CompanyHandOverOrderActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.company.CompanyHangUpActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.company.CompanyQrCodeActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.company.CompanySearchProjectActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.OwnerFinishOrderActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.OwnerMyWorkOrderActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.OwnerOrderOverDetailsActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.OwnerWorkOrderDetailsActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 维保公司端
 */
public class MaintenanceCompanyActivity extends BaseActivity implements IMainCompanyOrderListPresenter.ICompanyOrderListView, View.OnClickListener /*EasyPermissions.PermissionCallbacks*/ {

    private RecyclerView mRecyclerView;

    private static IMainCompanyOrderListPresenter presenter;

    private TextView mark10;
    private TextView mark20;
    private TextView mark30;

    private TextView mark40;
    private TextView mark50;
    private TextView mark60;

    private TextView tv_more;

    private TextView mark80;

    private RefreshLayout refreshLayout;

    public MaintenanceListInfoBean bean;

    private LinearLayout new_main_work;

    protected static ProgressDialog progressDialog;


    private TextView projectCompany;

    private CompanyOrderListInputBean inputBean;

    private static ProjectNodeBean projectNodeBean;

    private static String beanId;

    public static boolean flag = false;

    //public static OwnerMyWorkOrderActivity ownerActivity;

    public static int detailType = 2;

    private ProgressDialog dialog;

    private boolean isResetData = false;

    private SpecialCommonAdapter adapter;

    private List<MaintenListBackBean> beanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_maintenance_manage);
        super.onCreate(savedInstanceState);
        setBarTitle("维保管理");

        Intent intent = getIntent();
        Object queryPro = intent.getSerializableExtra("data");
        if (queryPro == null) {
            projectNodeBean = null;
        } else {
            projectNodeBean = (ProjectNodeBean) queryPro;
        }

        mRecyclerView = findViewById(R.id.main_manage_rv_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        projectCompany = findViewById(R.id.project_company);
        RelativeLayout state1 = findViewById(R.id.relative_state1);
        RelativeLayout state2 = findViewById(R.id.relative_state2);
        RelativeLayout state3 = findViewById(R.id.relative_state3);

        RelativeLayout state4 = findViewById(R.id.relative_state4);
        RelativeLayout state5 = findViewById(R.id.relative_state5);
        RelativeLayout state6 = findViewById(R.id.relative_state6);

        RelativeLayout state7 = findViewById(R.id.relative_state7);
        RelativeLayout state8 = findViewById(R.id.relative_state8);
        RelativeLayout state9 = findViewById(R.id.relative_state9);
        state1.setOnClickListener(this);
        state2.setOnClickListener(this);
        state3.setOnClickListener(this);

        state4.setOnClickListener(this);
        state5.setOnClickListener(this);
        state6.setOnClickListener(this);

        state7.setOnClickListener(this);
        state8.setOnClickListener(this);
        state9.setOnClickListener(this);

        mark10 = findViewById(R.id.mark10);
        mark20 = findViewById(R.id.mark20);
        mark30 = findViewById(R.id.mark30);

        mark40 = findViewById(R.id.mark40);
        mark50 = findViewById(R.id.mark50);
        mark60 = findViewById(R.id.mark60);

        mark80 = findViewById(R.id.mark80);

        tv_more = findViewById(R.id.tv_main_manage_more);
        new_main_work = findViewById(R.id.main_manage_new_work);
        new_main_work.setVisibility(View.GONE);

        tv_more.setOnClickListener(this);

        ImageView img_projectCompany = findViewById(R.id.main_btn_screen);
        img_projectCompany.setVisibility(View.VISIBLE);
        //img_projectCompany.setOnClickListener(this);

        LinearLayout mSelectCompany = findViewById(R.id.company_select_btn);
        mSelectCompany.setOnClickListener(this);


        refreshLayout = findViewById(R.id.refreshLayout);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        presenter = MaintenManagePresenterFactory.getCompanyOrderListImpl(this);
        addPersenter(presenter);
        inputBean = new CompanyOrderListInputBean();
        inputBean.setToken(HelperTool.getToken());
        inputBean.setState("0");
        inputBean.setPageIndex("1");
        inputBean.setPageSize("15");
        inputBean.setIsWorking("1");
        if (projectNodeBean != null) {
            SPUtils.getInstance().put("projectNodeId", projectNodeBean.getID());
            inputBean.setProjectId(projectNodeBean.getID());
        } else if (projectNodeBean == null || StringUtils.isEmpty(projectNodeBean.getID())) {
            inputBean.setProjectId(SPUtils.getInstance().getString("projectNodeId"));
        }
        presenter.doCompanyOrderList(inputBean);

        MyLoadMenuBackHeader myLoadHeader = new MyLoadMenuBackHeader(this);
        refreshLayout.setRefreshHeader(myLoadHeader);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                presenter.doCompanyOrderList(inputBean);
                refreshLayout.finishRefresh(2000);
            }
        });
        DividerItemDecoration div = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        div.setDrawable(getResources().getDrawable(R.drawable.divider_shape));
        mRecyclerView.addItemDecoration(div);

        dialog = showProgress(this, "加载中,请稍后...", false);

        //LogUtils.showLoge("维保公司工单首页参数信息00000---",inputBean.toString());

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isResetData) {
            isResetData = false;
            presenter.doCompanyOrderList(inputBean);
        }

        //LogUtils.showLoge("维保公司工单首页参数信息00001---",inputBean.toString());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relative_state1:
                goToCompanyMyOrder(1);
                break;
            case R.id.relative_state2:
                goToCompanyMyOrder(2);
                break;
            case R.id.relative_state3:
                goToCompanyMyOrder(3);
                break;
            case R.id.relative_state4:
                goToCompanyMyOrder(4);
                break;
            case R.id.relative_state5:
                goToCompanyMyOrder(5);
                break;

            case R.id.relative_state6:
                goToCompanyMyOrder(6);
                break;
            case R.id.relative_state7:
                goToCompanyMyOrder(7);
                break;
            case R.id.relative_state8:
                goToCompanyMyOrder(8);
                break;
            case R.id.relative_state9:
                goToCompanyMyOrder(9);
                break;

            case R.id.tv_main_manage_more:
                goToCompanyMyOrder(0);
                break;

            case R.id.company_select_btn:
                Intent intent = new Intent(this, CompanySearchProjectActivity.class);
                startActivityForResult(intent, 110);
                break;
        }
    }

//    public static void setActivity(OwnerMyWorkOrderActivity activity1) {
//        ownerActivity = activity1;
//    }

    private void goToCompanyMyOrder(int state) {
        isResetData = true;
        Intent intent = new Intent(this, OwnerMyWorkOrderActivity.class);
        intent.putExtra("state", state);
        intent.putExtra("isCompany", true);
        startActivity(intent);
    }

    @Override
    public void callBackCompanyOrderList(ResultBean<MaintenListAllBean, String> resultBean) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        if (resultBean != null && resultBean.getData() != null && resultBean.getData().getRows() != null && resultBean.getData().getRows().size() > 0) {

            hideNoDataView();

            flag = false;

            initWorkOrderList(mRecyclerView, this, resultBean.getData().getRows());

            initMark(resultBean.getData().getRows().get(0));
        } else {
            showNoDataView();
            initMessageCount();
            adapter = initWorkOrderList(mRecyclerView, this, beanList);
            adapter.notifyDataSetChanged();
        }
    }

    public static SpecialCommonAdapter initWorkOrderList(RecyclerView recyclerView, final Activity activity, final List<MaintenListBackBean> beanList) {
        SpecialCommonAdapter adapter1 = new SpecialCommonAdapter<MaintenListBackBean>(activity, R.layout.activity_owner_worklist_item, beanList) {
            @Override
            protected void convert(ViewHolder holder, final MaintenListBackBean bean, int position) {
                TextView title = holder.getView(R.id.title);
                TextView title_one = holder.getView(R.id.title_one);
                title_one.setText(bean.getProjectSystemNameShow());
                title.setText(bean.getProjectNameShow());
                ImageView img = holder.getView(R.id.image);
                Picasso.get().load(URLConstant.URL_BASE1 + bean.getStateIconUrl()).error(R.drawable.img_load).into(img);
                TextView tv_content1 = holder.getView(R.id.tv_content1);
                TextView tv_content2 = holder.getView(R.id.tv_content2);
                TextView tv_content3 = holder.getView(R.id.tv_content3);
                TextView tv_content4 = holder.getView(R.id.tv_content4);
                tv_content1.setText(bean.getStateShow());
                tv_content2.setText(bean.getCode());
                tv_content3.setText(bean.getStartTimeShow());
                tv_content4.setText(bean.getAcceptUserInfoShow());
                initLayoutForCompany(activity, holder, bean);
            }
        };
        recyclerView.setAdapter(adapter1);
        adapter1.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(activity, OwnerWorkOrderDetailsActivity.class);
                intent.putExtra("ID", beanList.get(position).getID());
                intent.putExtra("detailType", detailType);
                activity.startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return adapter1;
    }

    private static void initTestData(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("workOrderId", id);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_COMPANY_ORDER_DETAILS))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        //HelperView.Toast(MaintenanceCompanyActivity.this, e.getMessage());
                        LogUtils.showLoge("维保公司详情错误1111---", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.showLoge("维保公司详情返回0909---", response);
                    }
                });
    }


    public static void initLayoutForCompany(final Activity activity, ViewHolder holder, final MaintenListBackBean bean) {
        LinearLayout layout = holder.getView(R.id.button_layout);
        final Button button1 = holder.getView(R.id.button1);
        final Button button2 = holder.getView(R.id.button2);

        final Button button3 = holder.getView(R.id.button3);
        final Button button4 = holder.getView(R.id.button4);

        button2.getLayoutParams().width = ScreenUtil.dp2px(activity, 60);

        switch (bean.getState()) {
            case 10:
                button2.setText("确认");
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goToConfirmOrder(activity, bean);
                    }
                });
                button1.setVisibility(View.GONE);
                break;
            case 20:
                button4.setVisibility(View.VISIBLE);
                button4.setText("移交");
                button1.setText("终止");
                button2.setText("开始维修");

                button2.getLayoutParams().width = ScreenUtil.dp2px(activity, 70);
                button4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goToHandOverOrder(activity, bean);
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goToTerminationOrder(activity, bean);
                    }
                });
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String[] perm = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
                        beanId = bean.getID();
                        if (EasyPermissions.hasPermissions(activity, perm)) {
                            Intent intent = new Intent(activity, CompanyQrCodeActivity.class);
                            intent.putExtra("ID", bean.getID());
                            intent.putExtra("projectId", bean.getProjectId());
                            intent.putExtra("flag", flag);
                            intent.putExtra("state", 20);
                            activity.startActivity(intent);

                        } else {
                            requestPermissionsCallBack(activity, "需要相机权限", 10, perm, new PermissionsUtil.IGrantCallBack() {
                                @Override
                                public void result(boolean Success, int requestCode) {
                                    if (requestCode == 10 && Success) {
                                        Intent intent = new Intent(activity, CompanyQrCodeActivity.class);
                                        intent.putExtra("ID", bean.getID());
                                        intent.putExtra("projectId", bean.getProjectId());
                                        intent.putExtra("flag", flag);
                                        activity.startActivity(intent);
                                    }
                                }
                            });
                        }
                    }
                });
                break;
            case 60:
                button3.setVisibility(View.VISIBLE);
                button4.setVisibility(View.VISIBLE);
                button3.setText("挂起");
                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        goToHangUpOrder(activity, bean);

//                        DialogUtil.showSelectMessage(activity, "是否挂起?", new DialogUtil.IComfirmCancelListener() {
//                            @Override
//                            public void onConfirm() {
//
//                            }
//
//                            @Override
//                            public void onCancel() {
//
//                            }
//                        });

                    }
                });
                button4.setText("移交");
                button4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goToHandOverOrder(activity, bean);
                    }
                });
                button1.setText("终止");
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goToTerminationOrder(activity, bean);
                    }
                });
                button2.setText("开始维修");
                button2.getLayoutParams().width = ScreenUtil.dp2px(activity, 70);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String[] perm = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
                        beanId = bean.getID();
                        if (EasyPermissions.hasPermissions(activity, perm)) {
                            Intent intent = new Intent(activity, CompanyQrCodeActivity.class);
                            intent.putExtra("ID", bean.getID());
                            intent.putExtra("projectId", bean.getProjectId());
                            intent.putExtra("flag", flag);
                            activity.startActivity(intent);
                        } else {
                            requestPermissionsCallBack(activity, "需要相机权限", 10, perm, new PermissionsUtil.IGrantCallBack() {
                                @Override
                                public void result(boolean Success, int requestCode) {
                                    if (requestCode == 10 && Success) {
                                        Intent intent = new Intent(activity, CompanyQrCodeActivity.class);
                                        intent.putExtra("ID", bean.getID());
                                        intent.putExtra("projectId", bean.getProjectId());
                                        intent.putExtra("flag", flag);
                                        activity.startActivity(intent);
                                    }
                                }
                            });
                        }
                    }
                });
                break;
            case 30:
                button3.setVisibility(View.VISIBLE);
                button4.setVisibility(View.VISIBLE);
                button3.setText("挂起");
                button4.setText("回填");
                button1.setText("移交");
                button2.setText("终止");
                button2.setTextColor(activity.getResources().getColor(R.color.black));
                button2.setBackground(activity.getResources().getDrawable(R.drawable.button_worklist_shape));

                button4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goToOrderBackFill(activity, bean);
                    }
                });
                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        goToHangUpOrder(activity, bean);

//                        DialogUtil.showSelectMessage(activity, "是否挂起?", new DialogUtil.IComfirmCancelListener() {
//                            @Override
//                            public void onConfirm() {
//
//                            }
//
//                            @Override
//                            public void onCancel() {
//
//                            }
//                        });
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goToHandOverOrder(activity, bean);
                    }
                });
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goToTerminationOrder(activity, bean);
                    }
                });
                break;
            case 40:
                button4.setVisibility(View.VISIBLE);
                button4.setText("移交");
                button4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goToHandOverOrder(activity, bean);
                    }
                });
                button1.setText("终止");
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goToTerminationOrder(activity, bean);
                    }
                });
                button2.setText("开始维修");
                button2.getLayoutParams().width = ScreenUtil.dp2px(activity, 70);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String[] perm = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
                        beanId = bean.getID();
                        if (EasyPermissions.hasPermissions(activity, perm)) {
                            Intent intent = new Intent(activity, CompanyQrCodeActivity.class);
                            intent.putExtra("ID", bean.getID());
                            intent.putExtra("projectId", bean.getProjectId());
                            intent.putExtra("flag", flag);
                            activity.startActivity(intent);

                        } else {
                            requestPermissionsCallBack(activity, "需要相机权限", 10, perm, new PermissionsUtil.IGrantCallBack() {
                                @Override
                                public void result(boolean Success, int requestCode) {
                                    if (requestCode == 10 && Success) {
                                        Intent intent = new Intent(activity, CompanyQrCodeActivity.class);
                                        intent.putExtra("ID", bean.getID());
                                        intent.putExtra("projectId", bean.getProjectId());
                                        intent.putExtra("flag", flag);
                                        activity.startActivity(intent);
                                    }
                                }
                            });
                        }
                    }
                });
                break;
            case 50:
                layout.setVisibility(View.GONE);
                break;
            case 80:
                button1.setVisibility(View.GONE);
                button2.setText("查看原因");
                button2.getLayoutParams().width = ScreenUtil.dp2px(activity, 70);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goToViewReason(activity, bean);
                    }
                });
                break;
            case 70:
                layout.setVisibility(View.GONE);
                break;
            case 90:
                layout.setVisibility(View.GONE);
                break;
        }
    }

    // 确认
    private static void goToConfirmOrder(Activity activity, final MaintenListBackBean bean) {
        Intent intent = new Intent(activity, CompanyConfirmOrderActivity.class);
        intent.putExtra("ID", bean.getID());
        activity.startActivityForResult(intent, 103);
    }

    // 移交
    private static void goToHandOverOrder(Activity activity, final MaintenListBackBean bean) {
        Intent intent = new Intent(activity, CompanyHandOverOrderActivity.class);
        intent.putExtra("ID", bean.getID());
        activity.startActivityForResult(intent, 100);
    }

    // 终止
    private static void goToTerminationOrder(Activity activity, final MaintenListBackBean bean) {
        Intent intent = new Intent(activity, OwnerFinishOrderActivity.class);
        intent.putExtra("ID", bean.getID());
        intent.putExtra("isFromCompany", true);
        activity.startActivityForResult(intent, 99);
    }

    // 查看原因
    private static void goToViewReason(Activity activity, final MaintenListBackBean bean) {
        Intent intent = new Intent(activity, OwnerOrderOverDetailsActivity.class);
        intent.putExtra("ID", bean.getID());
        intent.putExtra("isFromCompany", true);
        activity.startActivity(intent);
    }

    // 回填
    private static void goToOrderBackFill(Activity activity, final MaintenListBackBean bean) {
        Intent intent = new Intent(activity, CompanyBackFillActivity.class);
        intent.putExtra("id", bean.getID());
        activity.startActivityForResult(intent, 104);
    }

    // 挂起
    private static void goToHangUpOrder(final Activity activity, final MaintenListBackBean bean) {

        Intent intent = new Intent(activity, CompanyHangUpActivity.class);

        intent.putExtra("ID", bean.getID());

        activity.startActivityForResult(intent, 120);

//        Map<String, String> params = new HashMap<>();
//        params.put("Token", HelperTool.getToken());
//        params.put("workOrderId", bean.getID());
//        final ProgressDialog progressDialog = showProgressDigOne(activity, "提示", "挂起中...", false);
//        OkHttpUtils.post()
//                .url(URLConstant.URL_BASE1 + URLConstant.URL_COMPANY_HANG_UP_ORDER)
//                .params(params)
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        progressDialog.dismiss();
//                        HelperView.Toast(activity, e.getMessage());
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        progressDialog.dismiss();
//
//                        LogUtils.showLoge("挂起结果9090---", response + "~~~" + flag);
//
//                        try {
//                            JSONObject json = new JSONObject(response);
//                            boolean isOK = json.getBoolean("success");
//                            if (isOK) {
//                                if (!flag) {
//                                    CompanyOrderListInputBean inputBean = new CompanyOrderListInputBean();
//                                    inputBean.setToken(HelperTool.getToken());
//                                    inputBean.setState("0");
//                                    inputBean.setPageIndex("1");
//                                    inputBean.setPageSize("10");
//                                    inputBean.setIsWorking("1");
//                                    inputBean.setProjectId(SPUtils.getInstance().getString("projectNodeId"));
//                                    presenter.doCompanyOrderList(inputBean);
//
//                                } else {
////                                    if (ownerActivity != null) {
////                                        ownerActivity.updateFragment(4);
////                                        ownerActivity.updateFragment(3);
////                                        ownerActivity.updateFragment(5);
////                                        ownerActivity.updateTabAdapter();
////
////                                        ownerActivity.currentPosition = 4;
////                                        ownerActivity.updateOrderStateSelected();
////                                    } else {
////                                        LogUtils.showLoge("挂起结果9090000---", ownerActivity + "~~~");
////                                    }
//
//                                    if (activity instanceof OwnerMyWorkOrderActivity) {
//
//                                        OwnerMyWorkOrderActivity orderActivity = (OwnerMyWorkOrderActivity)activity;
//
//                                        orderActivity.updateFragment(4);
//                                        orderActivity.updateFragment(3);
//                                        orderActivity.updateFragment(5);
//                                        orderActivity.updateTabAdapter();
//
//                                        orderActivity.currentPosition = 4;
//                                        orderActivity.updateOrderStateSelected();
//                                    }
//
//
//                                }
//
//                            } else {
//                                HelperView.Toast(activity, json.getString("message"));
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
    }


    private static ProgressDialog showProgressDigOne(Activity activity, String title, String message, boolean cancelable) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(cancelable);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
        return progressDialog;
    }

    private void initMark(MaintenListBackBean bean) {
        Map<String, String> params = new HashMap<>();
        if (!StringUtils.isEmpty(inputBean.getProjectId())) {
            projectCompany.setText(bean.getProjectNameShow());
            params.put("projectId", bean.getProjectId());
        } else {
            projectCompany.setText("全部项目");
        }
        params.put("Token", HelperTool.getToken());

        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_OWNER_GET_MARK_COUNT)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        HelperView.Toast(MaintenanceCompanyActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //LogUtils.showLoge("输出维保公司工单状态数1212---", response);
                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONArray jsonArr = json.getJSONArray("data");
                                List<MainOwnerMarkCountBean> listBean = new Gson().fromJson(jsonArr.toString(), new TypeToken<List<MainOwnerMarkCountBean>>() {
                                }.getType());
                                if (listBean != null && listBean.size() > 0) {
                                    initMessageCount();
                                    for (int i = 0; i < listBean.size(); i++) {
                                        int count = listBean.get(i).getCount();
                                        String num;
                                        if (count > 99) {
                                            num = "99+";
                                        } else {
                                            num = String.valueOf(count);
                                        }
                                        if (count <= 0) {
                                            continue;
                                        }
                                        switch (listBean.get(i).getWorkOrderState()) {
                                            case 10:
                                                mark10.setVisibility(View.VISIBLE);
                                                mark10.setText(String.valueOf(num));
                                                break;
                                            case 20:
                                                mark20.setVisibility(View.VISIBLE);
                                                mark20.setText(String.valueOf(num));
                                                break;
                                            case 30:
                                                mark30.setVisibility(View.VISIBLE);
                                                mark30.setText(String.valueOf(num));
                                                break;

                                            case 40:
                                                mark40.setVisibility(View.VISIBLE);
                                                mark40.setText(String.valueOf(num));
                                                break;
                                            case 50:
                                                mark50.setVisibility(View.VISIBLE);
                                                mark50.setText(String.valueOf(num));
                                                break;
                                            case 60:
                                                mark60.setVisibility(View.VISIBLE);
                                                mark60.setText(String.valueOf(num));
                                                break;

                                            case 80:
                                                mark80.setVisibility(View.VISIBLE);
                                                mark80.setText(String.valueOf(num));
                                                break;
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                HelperView.Toast(MaintenanceCompanyActivity.this, e.getMessage());
                            }
                        }
                    }
                });
    }

    private void initMessageCount() {
        mark10.setVisibility(View.INVISIBLE);
        mark20.setVisibility(View.INVISIBLE);
        mark30.setVisibility(View.INVISIBLE);
        mark40.setVisibility(View.INVISIBLE);

        mark50.setVisibility(View.INVISIBLE);
        mark60.setVisibility(View.INVISIBLE);

        mark80.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        projectCompany.setFocusable(true);
        projectCompany.setFocusableInTouchMode(true);
        projectCompany.requestFocus();
    }

    @Override
    public void callBackError(ResultBean<String, String> resultBean, String action) {
        super.callBackError(resultBean, action);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 110:
                    if (data != null) {
                        SPUtils.getInstance().put("projectNodeId", data.getStringExtra("ID"));
                        inputBean.setProjectId(data.getStringExtra("ID"));
                        projectCompany.setText(data.getStringExtra("Name"));
                        presenter.doCompanyOrderList(inputBean);
                    }
                    break;
                case 99:
                case 100:
                case 101:
                case 102:
                case 103:
                case 104:
                case 120:
                default:
                    presenter.doCompanyOrderList(inputBean);
            }
        }
    }


}
