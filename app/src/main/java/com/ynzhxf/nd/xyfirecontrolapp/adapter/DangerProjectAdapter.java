package com.ynzhxf.nd.xyfirecontrolapp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.nodebase.ProjectNodeBean;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.ChargeProjectInfoActivity;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.CompanyProjectInfoActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 精准治理项目列表数据适配器
 * Created by nd on 2018-07-11.
 */

public class DangerProjectAdapter extends RecyclerView.Adapter<DangerProjectAdapter.ViewHolder> {
    private List<ProjectNodeBean> proList;

    private int loginType = 2;

    /**
     * 尝试缓存
     */
    private List<ViewHolder> queryDatas = new ArrayList<>();

    public DangerProjectAdapter(List<ProjectNodeBean> proList) {
        this.proList = proList;
    }

    public void setLoginType(int type) {
        loginType = type;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View contentView;
        TextView txtAddress;
        TextView txtName, txtproIndex;
        TextView attrName1, attrName2, attrValue1, attrValue2;

        ViewGroup parent;


        public ViewHolder(View view, ViewGroup parent1) {
            super(view);
            parent = parent1;
            contentView = view.findViewById(R.id.container);
            txtproIndex = view.findViewById(R.id.pro_index);
            txtAddress = view.findViewById(R.id.pro_address);
            txtName = view.findViewById(R.id.pro_name);
            attrName1 = view.findViewById(R.id.pro_attr1_name);
            attrName2 = view.findViewById(R.id.pro_attr2_name);
            attrValue1 = view.findViewById(R.id.pro_attr1_value);
            attrValue2 = view.findViewById(R.id.pro_attr2_value);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project_precision, parent, false);
        View queryView = view.findViewById(R.id.container);
        queryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HelperTool.isFastClick()) return;
                Intent intent;
                if (loginType == 4) {
                    intent = new Intent(parent.getContext(), CompanyProjectInfoActivity.class);
                } else {
                    intent = new Intent(parent.getContext(), ChargeProjectInfoActivity.class);
                }
                intent.putExtra("projectData", (ProjectNodeBean) v.getTag());
                parent.getContext().startActivity(intent);
            }
        });
        ViewHolder holder = new ViewHolder(view, parent);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProjectNodeBean queryPro = proList.get(position);
        loadProjectNode(queryPro, holder);
        holder.txtproIndex.setText(position + 1 + "");

        if (position == 0) {
            holder.txtproIndex.setBackground(holder.parent.getContext().getResources().getDrawable(R.drawable.item_project_list_cir));
        } else if (position == 1) {
            holder.txtproIndex.setBackground(holder.parent.getContext().getResources().getDrawable(R.drawable.item_project_list_cir));
        } else if (position == 2) {
            holder.txtproIndex.setBackground(holder.parent.getContext().getResources().getDrawable(R.drawable.item_project_list_cir));
        } else {
            holder.txtproIndex.setBackground(holder.parent.getContext().getResources().getDrawable(R.drawable.item_main_cir_yellow));
            holder.txtproIndex.setTextColor(holder.parent.getResources().getColor(R.color.yellow_star));
        }
    }

    public void loadProjectNode(ProjectNodeBean pronBean, ViewHolder holder) {
        holder.contentView.setTag(pronBean);
        holder.txtName.setText(pronBean.getName());
        holder.txtAddress.setText(pronBean.getAddress());
        holder.attrName1.setText(pronBean.getExtraData().get(0));
        holder.attrName2.setText(String.valueOf(pronBean.getExtraData().get(2) + " "));
        holder.attrValue1.setText(pronBean.getExtraData().get(1));

        if (!StringUtils.isEmpty(pronBean.getExtraData().get(3)) && pronBean.getExtraData().get(3).contains(" ")) {
            String[] splits = pronBean.getExtraData().get(3).split(" ");
            if (splits.length > 1) {
                holder.attrValue2.setText(String.valueOf(splits[0] + " "));
            }
        } else {
            holder.attrValue2.setText(pronBean.getExtraData().get(3).replace("个", ""));
        }

//        LogUtils.showLoge("输出首页列表字段1515---", pronBean.getExtraData().get(0) + "~~" + pronBean.getExtraData().get(1)
//                + "~~" + pronBean.getExtraData().get(2) + "~~" + pronBean.getExtraData().get(3));
    }

    @Override
    public int getItemCount() {
        return proList.size();
    }
}
