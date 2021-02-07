package com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.company;

import com.ynzhxf.nd.xyfirecontrolapp.ui.MyTabLayout;
import com.ynzhxf.nd.xyfirecontrolapp.view.enterprise.inspection.InspectionItemListActivity;


/**
 * author hbzhou
 * date 2019/1/23 18:44
 */
public class InspectionCompanyItemListActivity extends InspectionItemListActivity {


    private MyTabLayout scrollTab;

    private String taskId;

    private String systemId;

//    @Override
//    protected void initTabLayout() {
//
//        scrollTab = findViewById(R.id.inspection_item_scroll_tab);
//
//        vp_content = findViewById(R.id.inspection_item_vp_content);
//
//        taskId = getIntent().getStringExtra("taskId");
//
//        systemId = getIntent().getStringExtra("systemId");
//
//    }
//
//    @Override
//    protected void initSetTitle() {
//        setBarTitle("巡检项");
//    }
//
//    @Override
//    protected void initLayout() {
//        setContentView(R.layout.activity_company_inspection_item_list);
//    }

//    @Override
//    protected void addFragmentSum() {
//        //super.addFragmentSum();
//
//        fragmentList.add(InspectionItemCompanyFragment.newInstance(systemId, taskId));
//
//        fragmentList.add(InspectionItemCompanyFragment.newInstance(systemId, taskId));
//    }
//
//    @Override
//    protected void addTitleSum() {
//        //super.addTitleSum();
//
//        mTitles.add("待巡检");
//        mTitles.add("已完成");
//    }
}
