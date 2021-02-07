package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.SpecialCommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.MaintenListAllBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.MaintenListBackBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.MaintenanceListInfoBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.IMaintenanceListPresenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.maintenance.impl.MaintenManagePresenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.ui.MyLoadMenuBackHeader;
import com.ynzhxf.nd.xyfirecontrolapp.ui.NormalDividerItemDecoration;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.util.LogUtils;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.MaintenanceManageActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 业主我的工单列表fragment
 */
public class OwnerMyOrderListFragment extends BaseFragment implements IMaintenanceListPresenter.IMaintenanceListView {

    public RefreshLayout refreshLayout;

    public RecyclerView recyclerView;

    private IMaintenanceListPresenter presenter;

    private int pageIndex = 1;

    final List<MaintenListBackBean> beanList = new ArrayList<>();

    private MaintenanceListInfoBean bean;

    public String state;
    protected String projectId;

    public boolean isCompany = false;

    public LinearLayout mNoDataView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner_my_order, null);
        refreshLayout = view.findViewById(R.id.owner_my_order_refreshLayout);
        recyclerView = view.findViewById(R.id.owner_mt_order_rv_list);

        mNoDataView = view.findViewById(R.id.all_no_data);

        MyLoadMenuBackHeader myLoadHeader = new MyLoadMenuBackHeader(getActivity());
        refreshLayout.setRefreshHeader(myLoadHeader);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        NormalDividerItemDecoration div = new NormalDividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        div.setDrawable(getResources().getDrawable(R.drawable.divider_shape));
        recyclerView.addItemDecoration(div);
        recyclerView.setLayoutManager(linearLayoutManager);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            state = bundle.getString("state");
            projectId = bundle.getString("projectId");
        }
    }

    public static OwnerMyOrderListFragment newInstance(String state, String projectId) {
        Bundle args = new Bundle();
        args.putString("state", state);
        args.putString("projectId", projectId);
        OwnerMyOrderListFragment fragment = new OwnerMyOrderListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = MaintenManagePresenterFactory.getOwnerListPersenterImpl(this);
        addPersenter(presenter);
        bean = new MaintenanceListInfoBean();
        bean.setToken(HelperTool.getToken());
        bean.setState(state);
        bean.setProjectId(projectId);

        String start_Time = SPUtils.getInstance().getString("start_Time");
        String end_Time = SPUtils.getInstance().getString("end_Time");

        if (!StringUtils.isEmpty(start_Time) && !StringUtils.isEmpty(end_Time)) {
            bean.setStartTime(start_Time);
            bean.setEndTime(end_Time);
        }

        pageIndex = 1;
        bean.setPageIndex(String.valueOf(pageIndex));
        bean.setPageSize("10");
        LogUtils.showLoge("输出bean1111---" + state, bean.toString());
        beanList.clear();

        if (!isCompany) {
            presenter.doMaintenanceList(bean);
        }

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                bean.setPageIndex("1");
                pageIndex = 1;
                if (beanList.size() > 0) {
                    beanList.clear();
                }
                presenter.doMaintenanceList(bean);
                refreshLayout.finishRefresh(2000);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex++;
                bean.setPageIndex(String.valueOf(pageIndex));
                presenter.doMaintenanceList(bean);
                refreshLayout.finishLoadMore();
            }
        });
    }

    @Override
    public void callBackMaintenanceList(ResultBean<MaintenListAllBean, String> resultBean) {
        if (resultBean == null || resultBean.getData() == null || resultBean.getData().getRows() == null || resultBean.getData().getRows().size() == 0) {
            if (beanList.size() == 0) {
                mNoDataView.setVisibility(View.VISIBLE);
            }
            return;
        } else {
            mNoDataView.setVisibility(View.GONE);
        }

        //LogUtils.showLoge("输出获取到的工单数量787800---",resultBean.getData().getRows().size()+"~~~~~");

        beanList.addAll(resultBean.getData().getRows());
        SpecialCommonAdapter adapter = MaintenanceManageActivity.initWorkOrderList(recyclerView, getActivity(), beanList);
        adapter.notifyDataSetChanged();
    }

}
