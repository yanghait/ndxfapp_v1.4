package com.ynzhxf.nd.xyfirecontrolapp.view.basenode;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.ynzhxf.nd.xyfirecontrolapp.R;
import com.ynzhxf.nd.xyfirecontrolapp.view.BaseActivity;


/**
 * author hbzhou
 * date 2019/3/22 13:03
 */
public class ProjectRealAlarmDetailActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_real_alarm_details);
        super.onCreate(savedInstanceState);
        setBarTitle("报警详情");

        TextView mTitle = findViewById(R.id.alarm_event_title);

        TextView textView1 = findViewById(R.id.item_text1);
        TextView textView2 = findViewById(R.id.item_text2);
        TextView textView3 = findViewById(R.id.item_text3);
        TextView textView4 = findViewById(R.id.item_text4);

        TextView textView5 = findViewById(R.id.item_text5);
        TextView textView6 = findViewById(R.id.item_text6);
        TextView textView7 = findViewById(R.id.item_text7);

        String title = getIntent().getStringExtra("title");
        String text1 = getIntent().getStringExtra("text1");
        String text2 = getIntent().getStringExtra("text2");
        String text3 = getIntent().getStringExtra("text3");
        String text4 = getIntent().getStringExtra("text4");

        String text5 = getIntent().getStringExtra("text5");
        String text6 = getIntent().getStringExtra("text6");
        String text7 = getIntent().getStringExtra("text7");

        textView1.setText(text1);
        textView2.setText(text2);
        textView3.setText(text3);
        textView4.setText(text4);

        textView5.setText(text5);
        textView6.setText(text6);
        textView7.setText(text7);

        switch (text5) {
            case "故障事件":
                textView5.setTextColor(Color.parseColor("#FFC655"));
                break;
            case "控制事件":
                textView5.setTextColor(Color.parseColor("#C98ED0"));
                break;
            case "报警事件":
                textView5.setTextColor(Color.parseColor("#FC595A"));
                break;
            case "运行事件":
                textView5.setTextColor(Color.parseColor("#62BFFC"));
                break;
        }

        mTitle.setText(title);
    }
}
