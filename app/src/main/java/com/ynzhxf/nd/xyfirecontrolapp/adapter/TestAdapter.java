package com.ynzhxf.nd.xyfirecontrolapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ynzhxf.nd.xyfirecontrolapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nd on 2018-07-11.
 */

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder>{
    public static List<String> TestDatas = new ArrayList<String>();
    static {
        for (int i=0;i<20;i++){
            TestDatas.add("测试项目" + i );
        }
    }

    private Map<Integer,ViewHolder> mapdata;

    private List<String> list;
    public class ViewHolder extends RecyclerView.ViewHolder{
        View contentView;
        TextView txtAddress;
        TextView txtName;

        public ViewHolder(View view){
            super(view);
            contentView = view;
            txtAddress = (TextView)view.findViewById(R.id.pro_address);
            txtName = (TextView)view.findViewById(R.id.pro_name);
        }
    }

    public TestAdapter(List<String> list){
        this.list = list;
        mapdata = new HashMap<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
       final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project_precision,parent,false);

       ViewHolder holder = new ViewHolder(view);
       return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String temp = list.get(position);
        holder.txtName.setText(temp);
        holder.contentView.setTag(temp);
        mapdata.put(position,holder);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void settemp(int po){
        mapdata.get(po).txtName.setText("我是我成功了");
    }
}
