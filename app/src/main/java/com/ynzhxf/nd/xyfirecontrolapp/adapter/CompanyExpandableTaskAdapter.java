package com.ynzhxf.nd.xyfirecontrolapp.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.inspection.InspectionCreateTaskGroupBean;

import java.util.List;

/**
 * author hbzhou
 * date 2019/1/22 11:56
 */
public class CompanyExpandableTaskAdapter extends BaseExpandableListAdapter {

    private Context mContext;

    private List<InspectionCreateTaskGroupBean> groupList;

    public CompanyExpandableTaskAdapter(Context context, List<InspectionCreateTaskGroupBean> groupList) {
        this.mContext = context;
        this.groupList = groupList;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return groupList.get(i).getChildren().size();
    }

    @Override
    public Object getGroup(int i) {
        return groupList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return groupList.get(i).getChildren().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int i, boolean b, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        final GroupHolder holder;
        if (view == null) {
            holder = new GroupHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_inspect_create_group, null);
            holder.groupName = view.findViewById(R.id.item_group_name);
            holder.selectedState = view.findViewById(R.id.item_state_image);
            holder.groupIcon = view.findViewById(R.id.item_group_extension);
            holder.onClickLayout  =view.findViewById(R.id.item_state_image_layout);
            view.setTag(holder);
        } else {
            holder = (GroupHolder) view.getTag();
        }

        if (b) {
            holder.groupIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.extension));
        } else {
            holder.groupIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.shrink));
        }

        holder.groupName.setText(groupList.get(i).getTitle());

        if (groupList.get(i).getCheckArr().size() > 0) {
            final String isChecked = groupList.get(i).getCheckArr().get(0).getIsChecked();
            switch (isChecked) {
                case "0":
                    holder.selectedState.setImageDrawable(mContext.getResources().getDrawable(R.drawable.inspect_state_zero));
                    break;
                case "1":
                    holder.selectedState.setImageDrawable(mContext.getResources().getDrawable(R.drawable.inspect_state_two));
                    break;
                case "2":
                    holder.selectedState.setImageDrawable(mContext.getResources().getDrawable(R.drawable.inspect_state_one));
                    break;
            }

            holder.onClickLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    InspectionCreateTaskGroupBean.CheckArrBean bean = groupList.get(i).getCheckArr().get(0);

                    switch (bean.getIsChecked()) {
                        case "0":
                            bean.setIsChecked("1");
                            holder.selectedState.setImageDrawable(mContext.getResources().getDrawable(R.drawable.inspect_state_two));
                            updateChildData(true, i);
                            break;
                        case "1":
                            bean.setIsChecked("0");
                            holder.selectedState.setImageDrawable(mContext.getResources().getDrawable(R.drawable.inspect_state_zero));
                            updateChildData(false, i);
                            break;
                        case "2":
                            bean.setIsChecked("1");
                            holder.selectedState.setImageDrawable(mContext.getResources().getDrawable(R.drawable.inspect_state_two));
                            updateChildData(true, i);
                            break;
                    }
                    notifyDataSetChanged();
                }
            });
        } else {
            holder.selectedState.setImageDrawable(mContext.getResources().getDrawable(R.drawable.inspect_state_zero));
        }

        return view;
    }

    private void updateChildData(boolean isChecked, int position) {
        List<InspectionCreateTaskGroupBean.ChildrenBean> childList = groupList.get(position).getChildren();
        if (childList != null && childList.size() > 0) {
            for (int i = 0; i < childList.size(); i++) {
                InspectionCreateTaskGroupBean.ChildrenBean bean = childList.get(i);
                switch (bean.getCheckArr().get(0).getIsChecked()) {
                    case "0":
                        if (isChecked) {
                            bean.getCheckArr().get(0).setIsChecked("1");
                        }
                        break;
                    case "1":
                        if (!isChecked) {
                            bean.getCheckArr().get(0).setIsChecked("0");
                        }
                        break;
                }
            }
        }
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        ChildHolder holder;
        if (view == null) {
            holder = new ChildHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_inspect_create_child, null);
            holder.childName = view.findViewById(R.id.item_child_name);
            holder.selectedState = view.findViewById(R.id.item_state_image);
            view.setTag(holder);
        } else {
            holder = (ChildHolder) view.getTag();
        }
        if (groupList.get(i).getChildren().get(i1).getCheckArr().size() > 0) {

            String isChecked = groupList.get(i).getChildren().get(i1).getCheckArr().get(0).getIsChecked();

            switch (isChecked) {
                case "0":
                    holder.selectedState.setImageDrawable(mContext.getResources().getDrawable(R.drawable.inspect_state_zero));
                    break;
                case "1":
                    holder.selectedState.setImageDrawable(mContext.getResources().getDrawable(R.drawable.inspect_state_two));
                    break;
            }

            holder.selectedState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InspectionCreateTaskGroupBean.ChildrenBean bean = groupList.get(i).getChildren().get(i1);

                    switch (bean.getCheckArr().get(0).getIsChecked()) {
                        case "0":
                            bean.getCheckArr().get(0).setIsChecked("1");

                            if (isAllSelectedData(true, groupList.get(i).getChildren())) {
                                groupList.get(i).getCheckArr().get(0).setIsChecked("1");
                            } else {
                                groupList.get(i).getCheckArr().get(0).setIsChecked("2");
                            }
                            break;
                        case "1":
                            bean.getCheckArr().get(0).setIsChecked("0");

                            if (isAllSelectedData(false, groupList.get(i).getChildren())) {
                                groupList.get(i).getCheckArr().get(0).setIsChecked("0");
                            } else {
                                groupList.get(i).getCheckArr().get(0).setIsChecked("2");
                            }
                            break;
                    }
                    notifyDataSetChanged();
                }
            });
        } else {
            holder.selectedState.setImageDrawable(mContext.getResources().getDrawable(R.drawable.inspect_state_zero));
        }

        holder.childName.setText(groupList.get(i).getChildren().get(i1).getTitle());

        return view;
    }

    /**
     * @param isChecked
     * @param childList
     * @return 判断所有的子列表是否都选中或都未选中
     */
    public boolean isAllSelectedData(boolean isChecked, List<InspectionCreateTaskGroupBean.ChildrenBean> childList) {
        for (int i = 0; i < childList.size(); i++) {
            if (isChecked) {
                if ("0".equals(childList.get(i).getCheckArr().get(0).getIsChecked())) {
                    break;
                }
            } else {
                if ("1".equals(childList.get(i).getCheckArr().get(0).getIsChecked())) {
                    break;
                }
            }
            if (i == childList.size() - 1) {
                return true;
            }
        }
        return false;
    }

    public List<InspectionCreateTaskGroupBean> getGroupList() {
        return groupList;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private class GroupHolder {
        private TextView groupName;
        private ImageView groupIcon;
        private ImageView selectedState;
        private LinearLayout onClickLayout;
    }

    private class ChildHolder {
        private TextView childName;
        private ImageView selectedState;
    }
}
