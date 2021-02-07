package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.bean.common.NewsBean;

import java.util.List;

import me.bakumon.library.adapter.BulletinAdapter;

public class SaleAdapter extends BulletinAdapter<NewsBean> {
    public SaleAdapter(Context context, List<NewsBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int position) {

        // 获取 item 根 view
        View view = getRootView(R.layout.item_two_text);
        // 实例化子 View
        TextView tVSaleTitle = view.findViewById(R.id.tv_item_title_1);
        // 获取当前 bean
        tVSaleTitle.setText(mData.get(position).getNewsTitle());
        return view;
    }
}
