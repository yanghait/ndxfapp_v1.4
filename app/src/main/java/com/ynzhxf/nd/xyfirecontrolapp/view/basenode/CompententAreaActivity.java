package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.AreaAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.BaseNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;

import java.util.ArrayList;
import java.util.List;

public class CompententAreaActivity extends BaseActivity implements AreaAdapter.IAreaClick{
    //选中的界面容器
    private LinearLayout select_row;
    //动态加载的区域列表
    private RecyclerView rvArea;
    //查找按钮
    private View btnSearch;
    /**
     * 查找范围区域
     */
    private TextView txtSearchArea;

    //当前选中的区域ID
    private String selectAreaID;
    //所有的区域数据
    private List<ProjectNodeBean> allNodes;

    //根基础节点
    private ProjectNodeBean rootAreaData;

    //根节点文本
    private View rootText;

    /**
     * 添加到导航中的区域
     */
    private List<View> newAddTextList;

    /**
     * 当前选中的节点的文本
     */
    private BaseNodeBean nowSelectData;

    /**
     * 区域列表数据适配器
     */
    private AreaAdapter areaAdapter;

    //选中时候的颜色
    private int selectColor;
    //未选中的颜色
    private int unselectColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_compentent_area);
        setBarTitle("区域选择");
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Object data = (ResultBean<List<ProjectNodeBean>, String[]>)intent.getSerializableExtra("data");
        if(data==null){
            HelperView.Toast(this,"未获得数据");
            return;
        }
        ResultBean<List<ProjectNodeBean>, String[]> datas = (ResultBean<List<ProjectNodeBean>, String[]>)data;
        allNodes = datas.getData();
        selectAreaID = allNodes.get(0).getID();
        select_row = findViewById(R.id.select_row);
        rvArea = findViewById(R.id.rv_area_list);
        btnSearch = findViewById(R.id.btn_seach);
        txtSearchArea = findViewById(R.id.search_Area);
        newAddTextList = new ArrayList<>();
        selectColor = getResources().getColor(R.color.flat_alizarin);
        unselectColor = getResources().getColor(R.color.backgroundcolor);
        init();
    }


    private void init(){
        //查找按钮
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HelperTool.isFastClick()) return;
                Intent intent = new Intent(CompententAreaActivity.this,CompententSearchProjectActivity.class);
                intent.putExtra("areaData",nowSelectData);
                startActivity(intent);
            }
        });

        rootAreaData = allNodes.get(0);
        rootText = createNavText(rootAreaData);
        select_row.addView(rootText);
        nowSelectData = rootAreaData;
        txtSearchArea.setText(nowSelectData.getName());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvArea.setLayoutManager(manager);
        refreshArealistbyNode(rootAreaData);
    }

    /**
     * 根据选择节点，刷新选择列表中的数据
     */
    private void refreshArealistbyNode(BaseNodeBean query){
        List<BaseNodeBean> queryAreaList = new ArrayList<>();
        for(ProjectNodeBean queryNode : allNodes){
            if(queryNode.getParentID() == null) continue;
            if(queryNode.getParentID().equals(query.getID())){
                queryAreaList.add(queryNode);
            }
        }
        areaAdapter = new AreaAdapter(queryAreaList,this);
        rvArea.setAdapter(areaAdapter);
        nowSelectData = query;
        txtSearchArea.setText(query.getName());
    }

    @Override
    public void areaNodeClick(BaseNodeBean query) {
        View queryText = null;
        if(query.getNodeLevel() ==3){
            for(View queryView : newAddTextList){
                BaseNodeBean temp = (BaseNodeBean)queryView.getTag();
                if(temp.getNodeLevel() == query.getNodeLevel()){
                    newAddTextList.remove(queryView);
                    select_row.removeView(queryView);
                    break;
                }
            }
            queryText = createNavText(query);
            select_row.addView(queryText);
            newAddTextList.add(queryText);
            nowSelectData = query;
            txtSearchArea.setText(query.getName());
            Intent intent = new Intent(this,CompententAreaCoutryActivity.class);
            intent.putExtra("code",2);
            intent.putExtra("data",nowSelectData);
            startActivity(intent);
        }else {
            queryText = createNavText(query);
            select_row.addView(queryText);
            newAddTextList.add(queryText);
            refreshArealistbyNode(query);
        }

        for(View queryview : newAddTextList){
            queryview.findViewById(R.id.v_underline).setBackgroundColor(unselectColor);
        }
        rootText.findViewById(R.id.v_underline).setBackgroundColor(unselectColor);
        queryText.findViewById(R.id.v_underline).setBackgroundColor(selectColor);

    }

    /**
     * 使用区域对象创建一个文本导航对象
     * @param baseNodeBean
     * @return
     */
    private View createNavText(BaseNodeBean baseNodeBean){

        View resultView = getLayoutInflater().inflate(R.layout.item_area_select,null);
        TextView queryName =  resultView.findViewById(R.id.txt_name);
        queryName.setText(baseNodeBean.getName());
        resultView.setTag(baseNodeBean);
        resultView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(HelperTool.isFastClick()) return;;
                BaseNodeBean queryBean = (BaseNodeBean)v.getTag();
                List<View> tempRemove = new ArrayList<>();
                switch (queryBean.getNodeLevel()){
                    case 1:
                        for (View queryText : newAddTextList){
                            select_row.removeView(queryText);
                            tempRemove.add(queryText);
                        }
                        newAddTextList.removeAll(tempRemove);
                        rootText.findViewById(R.id.v_underline).setBackgroundColor(selectColor);
                        //重载列表数据
                        refreshArealistbyNode(queryBean);
                        break;
                    case  2:
                        for (View queryText : newAddTextList){
                            BaseNodeBean queryBeanText = (BaseNodeBean)queryText.getTag();
                            if(queryBeanText.getNodeLevel() >= 2){
                                select_row.removeView(queryText);
                                tempRemove.add(queryText);
                            }
                        }
                        rootText.findViewById(R.id.v_underline).setBackgroundColor(selectColor);
                        newAddTextList.removeAll(tempRemove);
                        refreshArealistbyNode(rootAreaData);
                        break;
                    case 3:
                        select_row.removeView(v);
                        newAddTextList.remove(v);
                        for (int i=0;i<allNodes.size();i++){
                            if(queryBean.getParentID().equals(allNodes.get(i).getID())){
                                nowSelectData = allNodes.get(i);
                                txtSearchArea.setText(allNodes.get(i).getName());
                                break;
                            }
                        }
                        if(newAddTextList.size()>0){
                            newAddTextList.get(0).findViewById(R.id.v_underline).setBackgroundColor(selectColor);
                        }else {
                            rootText.findViewById(R.id.v_underline).setBackgroundColor(selectColor);
                        }
                        break;
                        default:
                }
            }
        });
        return resultView;
    }
}
