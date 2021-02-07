package com.ynzhxf.nd.xyfirecontrolapp.ui;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.CommonAdapter;
import com.ynzhxf.nd.xyfirecontrolapp.adapter.baserecycleradapter.base.ViewHolder;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.ChargeIndexAreaDataBean;
import com.ynzhxf.nd.xyfirecontrolapp.view.basenode.charge.ChargeAreaSelectGridAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;


/**
 * author hbzhou
 * date 2019/5/30 16:38
 * 统计分析省市区县筛选弹框
 */
public class AreaSelectedPopupWindow extends BasePopupWindow {
    public static int[] selectedPosition = {0, 0};

    public AreaSelectedPopupWindow(final Context context, final ChargeIndexAreaDataBean dataBean) {
        super(context);
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        ChargeIndexAreaDataBean.ChildrenNodesBeanX itemBean = new ChargeIndexAreaDataBean.ChildrenNodesBeanX();
        itemBean.setName(dataBean.getName());
        itemBean.setID(dataBean.getID());

        final List<ChargeIndexAreaDataBean.ChildrenNodesBeanX> dataBeanList = new ArrayList<>();

        final List<ChargeIndexAreaDataBean.ChildrenNodesBeanX.ChildrenNodesBean> threeBeanList = new ArrayList<>();

        //动态处理省市级别主管部门区域筛选列表
        if (dataBean.getNodeLevel() == 1) {
            dataBeanList.add(itemBean);
            dataBeanList.addAll(dataBean.getChildrenNodes());
        } else if (dataBean.getNodeLevel() == 2) {

            for (ChargeIndexAreaDataBean.ChildrenNodesBeanX bean : dataBean.getChildrenNodes()) {
                ChargeIndexAreaDataBean.ChildrenNodesBeanX.ChildrenNodesBean threeBean = new ChargeIndexAreaDataBean.ChildrenNodesBeanX.ChildrenNodesBean();
                threeBean.setName(bean.getName());
                threeBean.setID(bean.getID());
                threeBean.setNodeLevel(bean.getNodeLevel());
                threeBean.setParentID(bean.getParentID());
                threeBeanList.add(threeBean);
            }
            itemBean.setChildrenNodes(threeBeanList);

            dataBeanList.add(itemBean);
        }


        CommonAdapter adapter = new CommonAdapter<ChargeIndexAreaDataBean.ChildrenNodesBeanX>(context, R.layout.item_area_select_popup, dataBeanList) {
            @Override
            protected void convert(ViewHolder holder, final ChargeIndexAreaDataBean.ChildrenNodesBeanX childrenNodesBeanX, final int position) {
                final TextView mAreaTitle = holder.getView(R.id.area_item_title);
                mAreaTitle.setText(childrenNodesBeanX.getName());
                CustomGridView mGridView = holder.getView(R.id.area_grid_view);
                if (dataBean.getNodeLevel() == 2 && position == dataBeanList.size() - 1) {
                    mGridView.setAdapter(new ChargeAreaSelectGridAdapter(context, position, childrenNodesBeanX.getChildrenNodes()));
                }
                if (dataBean.getNodeLevel() == 1 && position > 0) {
                    mGridView.setAdapter(new ChargeAreaSelectGridAdapter(context, position, childrenNodesBeanX.getChildrenNodes()));
                }

                if (selectedPosition[0] == position && selectedPosition[1] == 0) {
                    mAreaTitle.setTextColor(context.getResources().getColor(R.color.device_diagnose_orange));
                } else {
                    mAreaTitle.setTextColor(context.getResources().getColor(R.color.black));
                }

                mAreaTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedPosition[0] = position;
                        selectedPosition[1] = 0;
                        notifyDataSetChanged();
                        EventBus.getDefault().post(childrenNodesBeanX);
                    }
                });
            }
        };

        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_area_select_layout);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultAlphaAnimation(true);
    }
}
