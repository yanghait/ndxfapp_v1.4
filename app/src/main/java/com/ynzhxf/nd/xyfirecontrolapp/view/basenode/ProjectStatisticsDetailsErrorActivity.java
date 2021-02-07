package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;


import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.util.HelperTool;

import java.util.Calendar;
import java.util.List;

/**
 * author hbzhou
 * date 2019/2/22 09:49
 */
public class ProjectStatisticsDetailsErrorActivity extends ProjectStatisticsListErrorInfo {

    //时间选择控件初始时间
    public Calendar initStartTime = Calendar.getInstance();

    //开始时间毫秒
    public long startLongTime;

    //结束时间的毫秒数
    public long endLongTime;

    protected TextView mStartTime;

    protected TextView mEndTime;

    TextView mEventName1;
    TextView mEventCount1;

    TextView mEventName2;
    TextView mEventCount2;

    private LinearLayout layout2;

    @Override
    protected void initDataForDetailsError() {
        //

        setBarTitle("事件记录详情");
        RelativeLayout layout1 = findViewById(R.id.statistics_details_layout1);

        layout2 = findViewById(R.id.statistics_details_layout2);

        ImageButton down1 = findViewById(R.id.im_right_down1);

        ImageButton down2 = findViewById(R.id.im_right_down2);

        mEventName1 = findViewById(R.id.event_name1);
        mEventCount1 = findViewById(R.id.event_count1);

        mEventName2 = findViewById(R.id.event_name2);
        mEventCount2 = findViewById(R.id.event_count2);


        mStartTime = findViewById(R.id.txt_start_time);

        mEndTime = findViewById(R.id.txt_end_time);

        View bg1 = findViewById(R.id.error_bg1);
        View bg2 = findViewById(R.id.error_bg2);

        bg1.setVisibility(View.VISIBLE);
        bg2.setVisibility(View.VISIBLE);


        down1.setVisibility(View.GONE);

        down2.setVisibility(View.GONE);

        //layout2.setVisibility(View.VISIBLE);

        layout1.setVisibility(View.VISIBLE);

        iniTimeInfo();
    }

    private void iniTimeInfo() {
        initStartTime.add(Calendar.DAY_OF_MONTH, -31);

        Calendar queryTime = Calendar.getInstance();
        endLongTime = queryTime.getTimeInMillis();
        queryTime.add(Calendar.DAY_OF_MONTH, -7);
        startLongTime = queryTime.getTimeInMillis();
        mStartTime.setText(HelperTool.MillTimeToStringDate(startLongTime).concat(" "));
        mEndTime.setText(HelperTool.MillTimeToStringDate(endLongTime).concat(" "));
    }

    @Override
    protected void initEventCount(List<List<String>> extraList) {
        //
        if (extraList != null && extraList.size() > 1) {
            layout2.setVisibility(View.VISIBLE);
            if(extraList.get(0).size()>=5){
                mEventName1.setText(extraList.get(0).get(3));
                mEventCount1.setText(extraList.get(0).get(4));
            }

            if(extraList.get(1).size()>=5){
                mEventName2.setText(extraList.get(1).get(3));
                mEventCount2.setText(extraList.get(1).get(4));
            }
        }else{
            layout2.setVisibility(View.GONE);
        }
    }
}
