package com.ynzhxf.nd.xyfirecontrolapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.NewsBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;

import java.util.List;

/**
 * 新闻列表数据数据适配器
 * Created by nd on 2018-07-28.
 */

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder>{

    //数据列表
    private List<NewsBean> newsList;

    private NewsItemOnClick click;


    public interface NewsItemOnClick{
        void  NewsItemOnClick(NewsBean newsBean);
    }

    public NewsListAdapter(List<NewsBean> newsList , NewsItemOnClick click){
        this.newsList = newsList;
        this.click = click;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        View contentView;
        ImageView imImage ;
        TextView txtName , txtOrg , txtTime , txtCount;

        public ViewHolder(View view){
            super(view);
            contentView = view;
            imImage = view.findViewById(R.id.im_img);
            txtName = view.findViewById(R.id.txt_name);
            txtOrg = view.findViewById(R.id.txt_org);
            txtTime = view.findViewById(R.id.txt_publish_time);
            txtCount = view.findViewById(R.id.txt_count);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news,parent,false);
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(HelperTool.isFastClick()) return;
                click.NewsItemOnClick((NewsBean)v.getTag());
            }
        });
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewsBean queryPro = newsList.get(position);
        holder.contentView.setTag(queryPro);
        holder.txtName.setText(queryPro.getNewsTitle());
        holder.txtOrg.setText(queryPro.getPName());
        holder.txtTime.setText(queryPro.getNewsCreateTimeStr());
        holder.txtCount.setText(queryPro.getClickAmount()+"");
        Picasso.get().load(URLConstant.URL_BASE1 + queryPro.getNewsImg()).into(holder.imImage);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
