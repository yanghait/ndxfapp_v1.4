package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.owner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;


public class SlidingTabsAdapter implements TabAdapter {

	private int layoutStyle = -1;
	private ArrayList<String> mTitles;

	private Context context;
	public SlidingTabsAdapter(ArrayList<String> mTitles,Context context) {
		this.mTitles = mTitles;
		this.context = context;
	}

	public SlidingTabsAdapter(ArrayList<String> mTitles, int layoutStyle) {
		this.mTitles = mTitles;
		this.layoutStyle = layoutStyle;
	}

	@Override
	public View getView(int position) {
		TextView tab = null;
		if (layoutStyle == -1) {
			//tab = (TextView) LayoutInflater.from(context).inflate(R.layout.view_tabs, null);
		} else {
			tab = (TextView) LayoutInflater.from(context).inflate(layoutStyle,null);
		}

		String[] tabs_new = new String[mTitles.size()];
		// 过滤相同String的title
//		Set<String> tab_sets = new HashSet<String>(mTitles);
//		String[] tabs_new = new String[tab_sets.size()];
		int cnt = 0;
		for (int i = 0; i < mTitles.size(); i++) {
			tabs_new[cnt] = mTitles.get(i);
			cnt++;
//			if (tab_sets.contains(mTitles.get(i))) {
//
//			}
		}
		if (position < tabs_new.length)
			tab.setText(tabs_new[position].toUpperCase());
		return tab;
	}

}
