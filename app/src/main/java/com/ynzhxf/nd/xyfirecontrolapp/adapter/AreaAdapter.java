package com.ynzhxf.nd.xyfirecontrolapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.BaseNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;

import java.util.List;

/**
 * 精准治理项目列表数据适配器
 * Created by nd on 2018-07-11.
 */

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.ViewHolder>{
    private List<BaseNodeBean> areaList;
    private IAreaClick areaClick;

    public interface IAreaClick{
        void areaNodeClick(BaseNodeBean query);
    }


    public AreaAdapter(List<BaseNodeBean> areaList,IAreaClick areaClick){
        this.areaList = areaList;
        this.areaClick= areaClick;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        View contentView;
        TextView txtName;


        public ViewHolder(View view){
            super(view);
            contentView = view;
            txtName = (TextView)view.findViewById(R.id.node_name);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
       final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text,parent,false);
       view.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               if(HelperTool.isFastClick()) return;
               BaseNodeBean query = (BaseNodeBean)v.getTag();
               areaClick.areaNodeClick(query);

           }
       });
       ViewHolder holder = new ViewHolder(view);
       return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BaseNodeBean query = areaList.get(position);
        holder.txtName.setText(query.getName());
        holder.contentView.setTag(query);
    }

    @Override
    public int getItemCount() {
        return areaList.size();
    }
}
