package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.NormalProjectAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.PagingBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.BaseNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.ICompententAreaCountyProPagingPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl.NodeBasePersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;

import java.util.ArrayList;
import java.util.List;

/**
 *主管部门县区级区域列表
 */
public class CompententAreaCoutryActivity extends BaseActivity implements ICompententAreaCountyProPagingPersenter.ICompententAreaCountyProPagingView {
    //当前选中的区域
    private BaseNodeBean areaBean;

    //项目集合
    private List<ProjectNodeBean> projectList;

    //下拉刷新
    private SmartRefreshLayout refreshLayout;

    //项目总数统计
    private TextView txtTotalProject;

    //查找按钮
    private View btnSearch;

    //项目列表
    private RecyclerView rvProList;

    //请求码，标识请求来源
    private int requestCode = 1;

    //项目列表分页页码
    private int pageSize = 1;

    //项目总数量
    private int totalCount = 0;

    //总页数
    private int totalPageGount = 0;

    //县区级项目数据分页请求
    private ICompententAreaCountyProPagingPersenter persenter;

    //普通项目列表适配器
    private NormalProjectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_compentent_area_county);
        projectList = new ArrayList<>();
        super.onCreate(savedInstanceState);
        refreshLayout = findViewById(R.id.refreshLayout);
        btnSearch = findViewById(R.id.btn_seach);
        rvProList = findViewById(R.id.rv_pro_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvProList.setLayoutManager(manager);
        adapter = new NormalProjectAdapter(projectList);
        rvProList.setAdapter(adapter);
        txtTotalProject = findViewById(R.id.txt_total_count);
        persenter = NodeBasePersenterFactory.getCompententAreaCountyProPagingPersenterImpl(this);
        addPersenter(persenter);
        if(!loadIntentData()){
            HelperView.Toast(this,"数据加载异常！");
            return;
        }
        setBarTitle(areaBean.getName());
        init();
    }

    /**
     * 加载传入的数据
     */
    public boolean loadIntentData(){
        try{
            Intent intent = getIntent();
            int code = intent.getIntExtra("code",-1);
            if (code == -1){//标识界面错误
                return false;
            }
            Object queryData =  intent.getSerializableExtra("data");
            if(queryData == null) return false;
            if(code == 1){//由主管部门首页跳转过来的
                ResultBean<List<ProjectNodeBean>, String[]> result = (ResultBean<List<ProjectNodeBean>, String[]>)queryData;
                areaBean = result.getData().get(0);
                for (int i=1;i<result.getData().size();i++){
                    projectList.add(result.getData().get(i));
                }
                totalCount = Integer.parseInt(result.getExtra()[1]);
                totalPageGount = Integer.parseInt(result.getExtra()[2]);
                pageSize = 1;
                loadViewData();
            }else if (code == 2){//由区域选择列表跳转过来的
                areaBean = (BaseNodeBean)queryData;
                persenter.doCompententAreaCountyProPaging(1,areaBean.getID());
            }else{
                return false;
            }
            requestCode = code;
        }catch (Exception e){
            return false;
        }

        return true;
    }

    /**
     * c初始化
     */
    private void init(){
        //查找按钮点击
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HelperTool.isFastClick()) return;
                Intent intent = new Intent(CompententAreaCoutryActivity.this,CompententSearchProjectActivity.class);
                intent.putExtra("areaData",areaBean);
                startActivity(intent);
            }
        });

        //refreshLayout.setRefreshHeader(new MaterialHeader(this));

        //上拉
        refreshLayout.setOnLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(10000/*,false*/);//传入false表示刷新失败
                if(pageSize >= totalPageGount){
                    refreshLayout.finishLoadMore(true);
                    return;
                }
                pageSize++;
                persenter.doCompententAreaCountyProPaging(pageSize,areaBean.getID());
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

            }
        });
    }

    /**
     * 加载界面数据
     */
    private void loadViewData(){
        adapter.notifyDataSetChanged();
        txtTotalProject.setText(totalCount+"");
    }

    /**
     * 请求项目分页数据返回
     * @param result
     */
    @Override
    public void callBackCompententAreaCountyProPaging(ResultBean<PagingBean<ProjectNodeBean>, String> result) {
        refreshLayout.finishLoadMore(true);
        try{
            if(result.isSuccess()){
                pageSize = result.getData().getPageNow();
                totalCount = result.getData().getTotal();
                totalPageGount = result.getData().getTotalPageCount();
                projectList.addAll(result.getData().getRows());
                adapter.notifyDataSetChanged();
                loadViewData();
            }else{
                pageSize--;
                HelperView.Toast(this,result.getMessage());
            }
        }catch (Exception e){
            HelperView.Toast(this , "分页失败:"+e.getMessage());
        }

    }
}
