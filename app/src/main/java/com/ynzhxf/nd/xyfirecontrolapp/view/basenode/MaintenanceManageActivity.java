package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;

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
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.MainOwnerMarkCountBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.MaintenListAllBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.MaintenListBackBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.MaintenanceListInfoBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.IMaintenanceListPresenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.impl.MaintenManagePresenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.ui.MyLoadMenuBackHeader;
import com.ynzhxf.nd.xyfirecontrolapp.util.DialogUtil;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.util.ScreenUtil;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.OwnerAuditOrderActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.OwnerCreateOrderActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.OwnerFinishOrderActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.OwnerMyWorkOrderActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.OwnerOrderOverDetailsActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.OwnerWorkOrderDetailsActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class MaintenanceManageActivity extends BaseActivity implements IMaintenanceListPresenter.IMaintenanceListView, View.OnClickListener {

    private RecyclerView mRecyclerView;

    private IMaintenanceListPresenter presenter;

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

    private ProgressDialog dialog;

    private ProjectNodeBean projectNodeBean;

    private boolean isResetData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_maintenance_manage);
        super.onCreate(savedInstanceState);
        setBarTitle("维保管理");
        mRecyclerView = findViewById(R.id.main_manage_rv_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        Intent intent = getIntent();
        Object queryPro = intent.getSerializableExtra("data");
        projectNodeBean = (ProjectNodeBean) queryPro;


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

        refreshLayout = findViewById(R.id.refreshLayout);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);


        presenter = MaintenManagePresenterFactory.getOwnerListPersenterImpl(this);
        addPersenter(presenter);
        bean = new MaintenanceListInfoBean();
        bean.setToken(HelperTool.getToken());
        bean.setState("0");
        bean.setPageIndex("1");
        bean.setPageSize("15");
        bean.setIsWorking("1");
        bean.setProjectId(projectNodeBean.getID());
        presenter.doMaintenanceList(bean);
        MyLoadMenuBackHeader myLoadHeader = new MyLoadMenuBackHeader(this);
        refreshLayout.setRefreshHeader(myLoadHeader);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                presenter.doMaintenanceList(bean);
                refreshLayout.finishRefresh(2000);
            }
        });
        DividerItemDecoration div = new DividerItemDecoration(MaintenanceManageActivity.this, DividerItemDecoration.VERTICAL);
        div.setDrawable(getResources().getDrawable(R.drawable.divider_shape));
        mRecyclerView.addItemDecoration(div);
        initOnclick();
        dialog = showProgress(this, "加载中...", false);

        /*LogUtils.showLoge("业主工单首页列表9090---",bean.toString());*/
    }

    public void initData(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("workOrderId", id);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_OWNER_ORDER_OVER_DETAILS))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        HelperView.Toast(MaintenanceManageActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.showLoge("终止详情返回0909---", response);
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relative_state1:
                goToMyWorkOrder(1);
                break;
            case R.id.relative_state2:
                goToMyWorkOrder(2);
                break;
            case R.id.relative_state3:
                goToMyWorkOrder(3);
                break;

            case R.id.relative_state4:
                goToMyWorkOrder(4);
                break;
            case R.id.relative_state5:
                goToMyWorkOrder(5);
                break;
            case R.id.relative_state6:
                goToMyWorkOrder(6);
                break;

            case R.id.relative_state7:
                goToMyWorkOrder(7);
                break;
            case R.id.relative_state8:
                goToMyWorkOrder(8);
                break;
            case R.id.relative_state9:
                goToMyWorkOrder(9);
                break;
        }
    }

    private void goToMyWorkOrder(int state) {
        isResetData = true;
        Intent intent = new Intent(this, OwnerMyWorkOrderActivity.class);
        intent.putExtra("state", state);
        intent.putExtra("projectId", projectNodeBean.getID());
        startActivity(intent);
    }

    private void initOnclick() {
        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isResetData = true;
                Intent intent = new Intent(MaintenanceManageActivity.this, OwnerMyWorkOrderActivity.class);
                intent.putExtra("state", 0);
                intent.putExtra("projectId", projectNodeBean.getID());
                startActivityForResult(intent, 103);
            }
        });

        new_main_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MaintenanceManageActivity.this, OwnerCreateOrderActivity.class);
                intent.putExtra("projectId", projectNodeBean.getID());
                startActivityForResult(intent, 101);
            }
        });
    }

    private void initViewWork(final MaintenListBackBean bean) {
        projectCompany.setText(bean.getProjectNameShow());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isResetData) {
            isResetData = false;
            presenter.doMaintenanceList(bean);
        }
    }

    @Override
    public void callBackMaintenanceList(final ResultBean<MaintenListAllBean, String> resultBean) {
        dialog.dismiss();
        if (resultBean != null && resultBean.getData() != null && resultBean.getData().getRows() != null && resultBean.getData().getRows().size() > 0) {

            hideNoDataView();
            //LogUtils.showLoge("输出维保工单列表图片地址8989---", resultBean.getData().getRows().get(0).getStateIconUrl());

            initWorkOrderList(mRecyclerView, this, resultBean.getData().getRows());

            initMark(resultBean.getData().getRows().get(0));

            initViewWork(resultBean.getData().getRows().get(0));

            // initData(resultBean.getData().getRows().get(0).getID());

        } else {
            showNoDataView();
        }
    }

    @Override
    public void callBackError(ResultBean<String, String> resultBean, String action) {
        super.callBackError(resultBean, action);
        dialog.dismiss();
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

                initLayout(activity, holder, bean);
            }
        };
        recyclerView.setAdapter(adapter1);
        adapter1.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(activity, OwnerWorkOrderDetailsActivity.class);
                intent.putExtra("ID", beanList.get(position).getID());
                intent.putExtra("detailType", 1);
                activity.startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return adapter1;
    }

    public static void initLayout(Activity activity, ViewHolder holder, final MaintenListBackBean bean) {
        LinearLayout layout = holder.getView(R.id.button_layout);
        Button button1 = holder.getView(R.id.button1);
        final Button button2 = holder.getView(R.id.button2);
        button2.getLayoutParams().width = ScreenUtil.dp2px(activity, 60);
        switch (bean.getState()) {
            case 10:
            case 20:
            case 60:
                break;
            case 30:
            case 40:
                button2.setVisibility(View.GONE);
                break;
            case 50:
                button1.setVisibility(View.GONE);
                button2.setText("审核");
                break;
            case 80:
                button1.setVisibility(View.GONE);
                button2.setText("查看原因");
                button2.getLayoutParams().width = ScreenUtil.dp2px(activity, 70);
                break;
            case 70:
            case 90:
                layout.setVisibility(View.GONE);
                break;
        }

        initButton1(activity, button1, bean.getID());

        initButton2(activity, button2, bean);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 99:
                case 100:
                case 101:
                case 102:
                case 103:
                default:
                    presenter.doMaintenanceList(bean);
            }
        }
    }

    private static void initButton1(final Activity activity, Button button, final String id) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, OwnerFinishOrderActivity.class);
                intent.putExtra("ID", id);
                activity.startActivityForResult(intent, 99);
            }
        });
    }

    private static void initButton2(final Activity activity, final Button button, final MaintenListBackBean bean) {
        if (bean.isReminder() && bean.getState() != 80 && bean.getState() != 50) {
            button.setText("催办中");
        } else {
            if (bean.getState() == 50) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(bean.getID())) {
                            HelperView.Toast(activity, "无法获取工单编号，请稍后再试！");
                            return;
                        }
                        Intent intent = new Intent(activity, OwnerAuditOrderActivity.class);
                        intent.putExtra("id", bean.getID());
                        activity.startActivityForResult(intent, 100);
                    }
                });

            } else if (bean.getState() == 80) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(bean.getID())) {
                            HelperView.Toast(activity, "无法获取工单编号，请稍后再试！");
                            return;
                        }
                        Intent intent = new Intent(activity, OwnerOrderOverDetailsActivity.class);
                        intent.putExtra("ID", bean.getID());
                        activity.startActivityForResult(intent, 102);
                    }
                });

            } else {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DialogUtil.showSelectMessage(activity, "确认催办?", new DialogUtil.IComfirmCancelListener() {
                            @Override
                            public void onConfirm() {
                                initReminder(activity, button, bean.getID());
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                    }
                });
            }
        }
    }

    private static ProgressDialog showProgressDigOne(Activity activity, String title, String message, boolean cancelable) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(cancelable);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
        return progressDialog;
    }

    // 催单
    private static void initReminder(final Activity activity, final Button button, String workOrderId) {
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("workOrderId", workOrderId);

        final ProgressDialog progressDialog = showProgressDigOne(activity, "提示", "催单中...", false);
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1.concat(URLConstant.URL_OWNER_GET_REMINDER))
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        progressDialog.dismiss();
                        HelperView.Toast(activity, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        progressDialog.dismiss();

                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONObject json = new JSONObject(response);
                                if ((boolean) json.get("success")) {
                                    HelperView.Toast(activity, "催单成功!");
                                    button.setText("催办中");
                                    button.setClickable(false);
                                } else {
                                    HelperView.Toast(activity, "催单失败,请稍后再试!");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void initMark(MaintenListBackBean bean) {
        Map<String, String> params = new HashMap<>();
        params.put("Token", HelperTool.getToken());
        params.put("projectId", bean.getProjectId());
        OkHttpUtils.post()
                .url(URLConstant.URL_BASE1 + URLConstant.URL_OWNER_GET_MARK_COUNT)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        HelperView.Toast(MaintenanceManageActivity.this, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONArray jsonArr = json.getJSONArray("data");
                                List<MainOwnerMarkCountBean> listBean = new Gson().fromJson(jsonArr.toString(), new TypeToken<List<MainOwnerMarkCountBean>>() {
                                }.getType());
                                if (listBean != null && listBean.size() > 0) {
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
                                HelperView.Toast(MaintenanceManageActivity.this, e.getMessage());
                            }
                        }
                    }
                });
    }


}
