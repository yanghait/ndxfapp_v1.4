package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.statement;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.MultiItemTypeAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.maintenance.SearchProjectBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IDangeroursUserProjectPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.IUserHasAuthoryProjectPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl.NodeBasePersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.company.CompanySearchProjectActivity;

import java.util.List;


/**
 * author hbzhou
 * date 2019/3/20 16:56
 */
public class StatementSearchActivity extends CompanySearchProjectActivity implements IUserHasAuthoryProjectPersenter.IUserHasAuthoryProjectView, IDangeroursUserProjectPersenter.IDangeroursUserProjectView {

    private IUserHasAuthoryProjectPersenter userHasAuthoryProjectPresenter;

    public IDangeroursUserProjectPersenter dangeroursUserProjectPresenter;

    @Override
    public void initStartData() {

        userHasAuthoryProjectPresenter = NodeBasePersenterFactory.getUserHasAuthoryProjectPersenterImpl(this);
        addPersenter(userHasAuthoryProjectPresenter);
        dangeroursUserProjectPresenter = NodeBasePersenterFactory.getDangeroursUserProjectPersenterImpl(this);
        addPersenter(dangeroursUserProjectPresenter);

        searchView.setVisibility(View.GONE);

        setBarTitle("选择项目");

        adapter = new CommonAdapter<SearchProjectBean>(StatementSearchActivity.this, R.layout.item_search_project_main, listBean) {
            @Override
            protected void convert(ViewHolder holder, SearchProjectBean searchProjectBean, int position) {
                ((TextView) holder.getView(R.id.search_item_text)).setText(listBean.get(position).getName());
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                initGotoOnclick(position);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setAdapter(adapter);

        initGetData("");
    }

    @Override
    public void initGetData(String query) {

        int loginType = SPUtils.getInstance().getInt("LoginType", 3);

        if (loginType == 3) {
            userHasAuthoryProjectPresenter.doUserHasAuthoryProject();
        } else if (loginType == 4) {
            dangeroursUserProjectPresenter.doDangeroursUserProject();
        }

    }

    @Override
    public void callBackError(ResultBean<String, String> resultBean, String action) {
        super.callBackError(resultBean, action);
    }

    @Override
    public void callBackUserHasAuthoryProject(ResultBean<List<ProjectNodeBean>, String> resultBean) {
        initSearchData(resultBean);
    }

    @Override
    public void callBackDangeroursUserProject(ResultBean<List<ProjectNodeBean>, String> resultBean) {
        initSearchData(resultBean);
    }

    private void initSearchData(ResultBean<List<ProjectNodeBean>, String> resultBean) {
        if (resultBean.isSuccess()) {
            for (ProjectNodeBean bean : resultBean.getData()) {
                SearchProjectBean searchBean = new SearchProjectBean();
                searchBean.setID(bean.getID());
                searchBean.setName(bean.getName());
                listBean.add(searchBean);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
