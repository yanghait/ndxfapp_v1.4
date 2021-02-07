package com.ynzhxf.nd.xyfirecontrolapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;

import java.util.List;

/**
 * 项目搜索结果列表适配器
 * Created by nd on 2018-07-18.
 */

public class SearchProjectAdapter extends RecyclerView.Adapter<SearchProjectAdapter.ViewHolder> {
    private List<ProjectNodeBean> projectList;
    private ISearchProjectClick projectClick;

    public interface ISearchProjectClick {
        void SearchProjectItemClick(ProjectNodeBean query);
    }


    public SearchProjectAdapter(List<ProjectNodeBean> projectList, ISearchProjectClick projectClick) {
        this.projectList = projectList;
        this.projectClick = projectClick;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View contentView;
        TextView txtName;
        TextView txtAddress;

        ImageView messageConnect;

        ViewGroup parent;


        public ViewHolder(View view, ViewGroup parent1) {
            super(view);
            contentView = view.findViewById(R.id.container);
            txtName = view.findViewById(R.id.node_name);
            txtAddress = view.findViewById(R.id.node_discribe);
            messageConnect = view.findViewById(R.id.message_connect);
            parent = parent1;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_two, parent, false);
        View queryView = view.findViewById(R.id.container);
        queryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HelperTool.isFastClick()) return;
                ProjectNodeBean query = (ProjectNodeBean) v.getTag();
                projectClick.SearchProjectItemClick(query);

            }
        });
        ViewHolder holder = new ViewHolder(view, parent);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProjectNodeBean query = projectList.get(position);
        holder.txtName.setText(query.getName());
        holder.txtAddress.setText(query.getAddress());
        holder.contentView.setTag(query);

        if (query.getConnectionState() == 0) {
            holder.messageConnect.setImageDrawable(holder.parent.getContext().getResources().getDrawable(R.drawable.message_ok));
        } else {
            holder.messageConnect.setImageDrawable(holder.parent.getContext().getResources().getDrawable(R.drawable.message_error));
        }

    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }
}
