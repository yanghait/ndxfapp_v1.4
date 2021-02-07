package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ynzhxf.nd.xyfirecontrolapp.GloblePlantformDatas;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.SearchProjectAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.PagingBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.BaseNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.ICompententSearchProjectPersenter;
import com.ynzhxf.nd.xyfirecontrolapp.presenter.nodebase.impl.NodeBasePersenterFactory;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目查找
 */
public class CompententSearchProjectActivity extends BaseActivity implements SearchProjectAdapter.ISearchProjectClick,ICompententSearchProjectPersenter.ICompententSearchProjectView{

    /**
     * 区域ID
     */
    private String areaID;
    /**
     * 区域对象
     */
    private BaseNodeBean areaData;

    /**
     * 关键字文本
     */
    private EditText txtKeyword;

    private TextView txtResultMsg;
    /**
     * 项目列表
     */
    private RecyclerView rvProjectList;

    private ICompententSearchProjectPersenter persenter;
    private SearchProjectAdapter searchProjectAdapter;
    ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_compentent_search_project);
        setBarTitle("项目查找");
        Intent intent = getIntent();
        Object queryObj  = intent.getSerializableExtra("areaData");
        if(queryObj!=null){
            areaData = (BaseNodeBean)queryObj;
            areaID = areaData.getID();
            setBarTitle(areaData.getName()+"-项目查找");
        }else {
            HelperView.Toast(this,"未找到查找范围！");
            return;
        }
        super.onCreate(savedInstanceState);
        txtKeyword = findViewById(R.id.keyword);
        txtResultMsg = findViewById(R.id.search_toast);
        rvProjectList = findViewById(R.id.rv_pro_list);
        LinearLayoutManager manage = new LinearLayoutManager(this);
        manage.setOrientation(LinearLayoutManager.VERTICAL);
        rvProjectList.setLayoutManager(manage);
        init();
    }

    private void init(){
        persenter = NodeBasePersenterFactory.getCompententSearchProjectPersenterImpl(this);
        addPersenter(persenter);
        //关键改变监听
        txtKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String queryWord = String.valueOf(txtKeyword.getText()).trim();
                if(HelperTool.stringIsEmptyOrNull(queryWord)){
                    txtResultMsg.setText("");
                    reloadrvProjectList(new ArrayList<ProjectNodeBean>());
                    return;
                }
                persenter.doCompententArea(1, GloblePlantformDatas.getInstance().getLoginInfoBean().getToken(),areaID,queryWord);
            }
        });
    }

    /**
     * 项目列表点击回调
     * @param query
     */
    @Override
    public void SearchProjectItemClick(ProjectNodeBean query) {
        Intent intent  = new Intent(this, ProjectInfoActivity.class);
        intent.putExtra("projectData",query);
        startActivity(intent);
    }

    /**
     * 项目关键字搜索数据回调
     * @param result
     */
    @Override
    public void callBackCompententSearchProject(ResultBean<PagingBean<ProjectNodeBean>, String> result) {
        try{
            List<ProjectNodeBean> queryList = new ArrayList<>();
            if(result.isSuccess()){
                queryList = result.getData().getRows();
                txtResultMsg.setText("搜索结果：共"+result.getData().getTotal()+"条");
            }else{
                HelperView.Toast(this,result.getMessage());
            }
            reloadrvProjectList(queryList);
        }catch (Exception e){
            HelperView.Toast(this , "项目搜索失败："+e.getMessage());
        }


    }

    private void reloadrvProjectList(List<ProjectNodeBean> list){
        searchProjectAdapter = new SearchProjectAdapter(list,this);
        rvProjectList.setAdapter(searchProjectAdapter);
    }

}
