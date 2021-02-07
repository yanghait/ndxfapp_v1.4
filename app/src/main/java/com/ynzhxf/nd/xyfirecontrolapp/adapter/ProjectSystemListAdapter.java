package com.ynzhxf.nd.xyfirecontrolapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.squareup.picasso.Picasso;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.SystemListMessageCountBean;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectSystemBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nd on 2018-07-23.
 */

public class ProjectSystemListAdapter extends RecyclerView.Adapter<ProjectSystemListAdapter.ViewHolder> {
    private List<ProjectSystemBean> proList;

    private IProjectSystemOnClick projectSystemOnClick;

    private List<SystemListMessageCountBean> alarmCount;
    /**
     * 尝试缓存
     */
    private List<ViewHolder> queryDatas = new ArrayList<>();

    public ProjectSystemListAdapter(List<ProjectSystemBean> proList, List<SystemListMessageCountBean> alarmCount, IProjectSystemOnClick projectSystemOnClick) {
        this.projectSystemOnClick = projectSystemOnClick;
        this.proList = proList;
        this.alarmCount = alarmCount;
    }

    /**
     * 系统列表点击回掉
     */
    public interface IProjectSystemOnClick {
        void ProjectSystemOnClick(ProjectSystemBean projectSystemBean);
        void ProjectSystemOnRealDataClick(ProjectSystemBean projectSystemBean);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View contentView,alarmLayout;
        ImageView imageView;
        TextView txtName, txtDescribe, mAlarmCount, mMoitorCount;


        public ViewHolder(View view) {
            super(view);
            contentView = view.findViewById(R.id.container);
            alarmLayout = view.findViewById(R.id.alarm_real_layout);
            imageView = view.findViewById(R.id.im_img);
            txtName = (TextView) view.findViewById(R.id.txt_name);
            txtDescribe = view.findViewById(R.id.txt_describe);

            mAlarmCount = view.findViewById(R.id.alarm_count);
            mMoitorCount = view.findViewById(R.id.monitor_count);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project_system_list, parent, false);
        view.findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HelperTool.isFastClick()) return;
                projectSystemOnClick.ProjectSystemOnClick((ProjectSystemBean) v.getTag());
            }
        });
        view.findViewById(R.id.alarm_real_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HelperTool.isFastClick()) return;
                projectSystemOnClick.ProjectSystemOnRealDataClick((ProjectSystemBean) v.getTag());
            }
        });
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProjectSystemBean queryPro = proList.get(position);
        holder.txtName.setText(queryPro.getName());
        if (StringUtils.isEmpty(queryPro.getAnotherName())) {
            holder.txtDescribe.setVisibility(View.GONE);
        } else {
            holder.txtDescribe.setText(queryPro.getAnotherName());
        }
        holder.contentView.setTag(queryPro);
        holder.alarmLayout.setTag(queryPro);


        for (int i = 0; i < alarmCount.size(); i++) {
            if (!StringUtils.isEmpty(proList.get(position).getID()) && proList.get(position).getID().equals(alarmCount.get(i).getID())) {
                holder.mAlarmCount.setText(alarmCount.get(i).getAlarmCount());
                holder.mMoitorCount.setText(alarmCount.get(i).getTotalCount());
                break;
            }
        }
        if (queryPro.getProjectSysType() != null) {
            Picasso.get().load(URLConstant.URL_BASE1 + queryPro.getProjectSysType().getImageUrl()).error(R.drawable.img_load).into(holder.imageView);
        } else {
            Picasso.get().load(URLConstant.URL_BASE1).error(R.drawable.img_load).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return proList.size();
    }
}