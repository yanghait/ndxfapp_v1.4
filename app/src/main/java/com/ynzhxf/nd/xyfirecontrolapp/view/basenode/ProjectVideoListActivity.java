package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.ProjectVideoAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.bean.ResultBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.VideoChannelBean;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.HelperView;

import java.util.List;


/**
 * 项目实时视频列表
 */
public class ProjectVideoListActivity extends BaseActivity implements ProjectVideoAdapter.projectVideoClick{

    //视频列表
    private RecyclerView recyclerView;

    //项目对象
    private ProjectNodeBean projectNodeBean;

    //视频列表
    private List<VideoChannelBean> videoList;
    //视频列表适配器
    private ProjectVideoAdapter adapter;
    //项目对象
    private ProjectNodeBean prjectBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_project_video_list);
        super.onCreate(savedInstanceState);
        setBarTitle("视频列表");
        recyclerView = findViewById(R.id.rv_video_list);
        Intent intent = getIntent();
        Object queryPro = intent.getSerializableExtra("project");
        if(queryPro != null){
            projectNodeBean = (ProjectNodeBean)queryPro;
        }else {
            HelperView.Toast(this,"获取项目失败！");
            return;
        }
        Object queryVideoList = intent.getSerializableExtra("data");
        if(queryVideoList != null){
            ResultBean<List<VideoChannelBean>, String> queryList = (ResultBean<List<VideoChannelBean>, String>)queryVideoList;
            videoList = queryList.getData();
        }else {
            HelperView.Toast(this,"获取视频列表失败！");
            return;
        }
        init();
    }


    private void init(){
        adapter = new ProjectVideoAdapter(videoList,this);
        GridLayoutManager manage = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(manage);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 点击监控列表项
     * @param videoChannelBean
     */
    @Override
    public void projectVideoClick(VideoChannelBean videoChannelBean,String Url) {
        Intent intent = new Intent(this,VideoPlayActivity.class);
        intent.putExtra("data",videoChannelBean);

        intent.putExtra("Url",Url);
        intent.putExtra("project",projectNodeBean);
        startActivity(intent);
    }
}
