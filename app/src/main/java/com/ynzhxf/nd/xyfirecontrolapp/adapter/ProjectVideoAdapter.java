package com.ynzhxf.nd.xyfirecontrolapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.VideoChannelBean;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目视频列表适配器
 * Created by nd on 2018-07-21.
 */

public class ProjectVideoAdapter extends RecyclerView.Adapter<ProjectVideoAdapter.ViewHolder>{

    private List<VideoChannelBean> proList;
    private long step = System.currentTimeMillis();
    /**
     * 尝试缓存
     */
    private List<ViewHolder> queryDatas = new ArrayList<>();

    private projectVideoClick click;


    public ProjectVideoAdapter(List<VideoChannelBean> proList,projectVideoClick click){
        this.proList = proList;
        this.click = click;
    }

    public  interface projectVideoClick{
        void projectVideoClick(VideoChannelBean videoChannelBean , String url);
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        View contentView;
        ImageView imgVideo;
        TextView txtName;

        public ViewHolder(View view){
            super(view);
            contentView = view;
            imgVideo = view.findViewById(R.id.im_img);
            txtName = view.findViewById(R.id.txt_name);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project_video,parent,false);
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(HelperTool.isFastClick()) return;
                VideoChannelBean videoChannelBean  = (VideoChannelBean)v.getTag();
                ImageView query = v.findViewById(R.id.im_img);
                click.projectVideoClick(videoChannelBean , query.getTag()+"");
            }
        });
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VideoChannelBean queryVideo = proList.get(position);
        holder.txtName.setText(queryVideo.getCameraName());
        holder.contentView.setTag(queryVideo);
        Picasso.get().load(queryVideo.getImageUrl()+"?step="+step).into(holder.imgVideo);
        holder.imgVideo.setTag(queryVideo.getImageUrl());

    }



    @Override
    public int getItemCount() {
        return proList.size();
    }
}
