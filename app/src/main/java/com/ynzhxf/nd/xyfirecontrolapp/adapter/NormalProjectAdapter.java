package com.ynzhxf.nd.xyfirecontrolapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.pars.URLConstant;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.ChargeProjectInfoActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.CompanyProjectInfoActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.ProjectInfoActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 正常显示项目列表数据适配器
 * Created by nd on 2018-07-20.
 */

public class NormalProjectAdapter extends RecyclerView.Adapter<NormalProjectAdapter.ViewHolder> {

    //数据列表
    private List<ProjectNodeBean> proList;

    // 设置当前用户登录类型
    private int loginType = 3;

    /**
     * 尝试缓存
     */
    private List<ViewHolder> queryDatas = new ArrayList<>();

    public NormalProjectAdapter(List<ProjectNodeBean> proList) {
        this.proList = proList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View contentView;
        TextView txtAddress;
        TextView txtName;
        ImageView imgIcom;
        ImageView messageName;
        ViewGroup mParent;

        public ViewHolder(View view,ViewGroup parent) {
            super(view);
            contentView = view.findViewById(R.id.container);
            txtAddress = (TextView) view.findViewById(R.id.pro_address);
            txtName = (TextView) view.findViewById(R.id.pro_name);
            imgIcom = view.findViewById(R.id.img_icon);
            messageName = view.findViewById(R.id.message_state);
            mParent = parent;
        }
    }

    public void setLoginType(int type) {
        this.loginType = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project_normal, parent, false);
        View queryView = view.findViewById(R.id.container);
        queryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HelperTool.isFastClick()) return;
                Intent intent;
                if (loginType == 2) {
                    intent = new Intent(parent.getContext(), ChargeProjectInfoActivity.class);
                } else if (loginType == 4) {
                    intent = new Intent(parent.getContext(), CompanyProjectInfoActivity.class);
                } else {
                    intent = new Intent(parent.getContext(), ProjectInfoActivity.class);
                }
                intent.putExtra("projectData", (ProjectNodeBean) v.getTag());
                parent.getContext().startActivity(intent);
            }
        });
        ViewHolder holder = new ViewHolder(view,parent);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProjectNodeBean queryPro = proList.get(position);
        loadProjectNode(queryPro, holder);

       // LogUtils.showLoge("输出通讯状态翻译1212---", queryPro.getTrasentConnection());
        //LogUtils.showLoge("输出通讯状态标识1515---", queryPro.getConnectionState() + "~~~");
    }

    public void loadProjectNode(ProjectNodeBean pronBean, ViewHolder holder) {
        holder.contentView.setTag(pronBean);
        holder.txtName.setText(pronBean.getName());
        holder.txtAddress.setText(pronBean.getAddress());//if ("正常".equals(errorInfoBean.getAlarmValue())) {

        if (pronBean.getConnectionState() == 0) {
//            holder.messageName.setText(String.valueOf("通讯正常"));
//            holder.messageName.setTextColor(Color.parseColor("#16A085"));
            holder.messageName.setImageDrawable(holder.mParent.getContext().getResources().getDrawable(R.drawable.message_ok));
        } else {
//            holder.messageName.setText(String.valueOf("通讯异常"));
//            holder.messageName.setTextColor(Color.parseColor("#E74C3C"));
            holder.messageName.setImageDrawable(holder.mParent.getContext().getResources().getDrawable(R.drawable.message_error));
        }

        Picasso.get().load(URLConstant.URL_BASE1 + pronBean.getProjectIcon()).error(R.drawable.img_load).into(holder.imgIcom);

    }

    /**
     * 添加数据
     *
     * @param queryList
     */
    public void addDatas(List<ProjectNodeBean> queryList) {
        this.proList.addAll(queryList);
    }

    /**
     * 移除节点
     *
     * @param queryList
     */
    public void removeDatas(List<ProjectNodeBean> queryList) {
        this.proList.removeAll(queryList);
    }

    /**
     * 重新设置数据
     */
    public void reSettingDatas(List<ProjectNodeBean> queryList) {
        this.proList = queryList;
    }

    @Override
    public int getItemCount() {
        return proList.size();
    }
}
